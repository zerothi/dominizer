/**
 * 
 */
package com;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.Ticker;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.dominizer.GameApp;
import com.util.Cards;
import com.util.SettingsRecordStorage;

import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class OptionForm extends Form implements CommandListener, ItemStateListener {

	private Command saveCmd = new Command( Locale.get("cmd.save"), Command.BACK, 1);
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.SCREEN, 3);
	
	private ChoiceGroup[] options = null;
	private String[] info = null;
	private Ticker ticker = null;
	
	/**
	 * @param title the title of the screen
	 */
	public OptionForm(String title) {
		//#style behindScreen
		super(title);
		options = new ChoiceGroup[1];
		info = new String[1];
		//#style horizontalChoice
		options[0] = new ChoiceGroup(Locale.get("screen.options.sort"), ChoiceGroup.EXCLUSIVE);
		options[0].append(Locale.get("cmd.Sort.ExpName"), null);
		options[0].append(Locale.get("cmd.Sort.ExpCost"), null);
		options[0].append(Locale.get("cmd.Sort.Name"), null);
		options[0].append(Locale.get("cmd.Sort.CostName"), null);
		options[0].append(Locale.get("cmd.Sort.CostExp"), null);
		info[0] = new String(Locale.get("screen.options.info"));
		append(options[0]);
		//#style mainTicker
		ticker = new Ticker(info[0]);
		setTicker(ticker);
		
		addCommand(saveCmd);
		addCommand(backCmd);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command cmd, Displayable screen) {
		if ( cmd.equals(saveCmd) ) {
			try {
				if ( SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.settings")) ) {
					//#= Cards.COMPARE_PREFERRED = options[0].getSelectedIndex();
					SettingsRecordStorage.instance().addData(Locale.get("rms.preferredsort"), "" + Cards.COMPARE_PREFERRED);
					SettingsRecordStorage.instance().writeData();
					SettingsRecordStorage.instance().closeRecord();
				}
			} catch (RecordStoreFullException e) {
			} catch (RecordStoreNotFoundException e) {
			} catch (RecordStoreException e) {
			}
		} else if ( cmd.equals(backCmd) ) {
			GameApp.instance().returnToPreviousScreen();
		}
	}

	public void itemStateChanged(Item itm) {
		for ( int i = 0 ; i < options.length ; i++ )
			if ( itm.equals(options[i]) ) 
				ticker.setString(info[i]);
		
	}
}
