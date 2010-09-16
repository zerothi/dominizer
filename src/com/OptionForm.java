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
//#if dominizer.ticker
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.Ticker;
//#endif
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.dominizer.GameApp;
import com.util.Cards;
import com.util.Dominion;
import com.util.SettingsRecordStorage;

import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class OptionForm extends Form implements CommandListener
//#if dominizer.ticker
	, ItemStateListener
//#endif
{

	private Command saveCmd = new Command( Locale.get("cmd.save"), Command.BACK, 1);
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.SCREEN, 3);
	
	private ChoiceGroup[] options = null;
	private String[] info = null;
	//#if dominizer.ticker
		private Ticker ticker = null;
	//#endif
	
	/**
	 * @param title the title of the screen
	 */
	public OptionForm(String title) {
		//#style behindScreen
		super(title);
		options = new ChoiceGroup[3];
		info = new String[options.length];
		//#style horizontalChoice
		options[0] = new ChoiceGroup(Locale.get("screen.options.sort"), ChoiceGroup.EXCLUSIVE);
		info[0] = new String(Locale.get("screen.options.sort.info"));
		options[0].append(Locale.get("cmd.Sort.ExpName"), null);
		options[0].append(Locale.get("cmd.Sort.ExpCost"), null);
		options[0].append(Locale.get("cmd.Sort.Name"), null);
		options[0].append(Locale.get("cmd.Sort.CostName"), null);
		options[0].append(Locale.get("cmd.Sort.CostExp"), null);
		append(options[0]);
		
		options[1]  = new ChoiceGroup(Locale.get("screen.options.numberofsavedsets"));
		info[1] = new String(Locale.get("screen.options.numberofsavedsets.info"));
		for (int i = 0 ; i < Dominion.MAX_SETS ; i++ )
			options[1].append("" + i, null);
		append(options[1]);
		
		options[2]  = new ChoiceGroup(Locale.get("screen.options.preferredcondition"));
		info[2] = new String(Locale.get("screen.options.preferredcondition.info"));
		for (int i = 0 ; i < Dominion.I().condition.size() ; i++ )
			options[2].append(Dominion.I().condition.getName(i), null);
		append(options[2]);
		
		
		//#if dominizer.ticker
			setItemStateListener(this);
			//#style mainTicker
			ticker = new Ticker(info[0]);
			setTicker(ticker);
		//#endif
		
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
				GameApp.instance().showInfo(""); //TODO SAVE SUCCESFULL
			} catch (RecordStoreFullException e) {
			} catch (RecordStoreNotFoundException e) {
			} catch (RecordStoreException e) {
			}
		} else if ( cmd.equals(backCmd) ) {
			GameApp.instance().returnToPreviousScreen();
		}
	}
	//#if dominizer.ticker
		public void itemStateChanged(Item itm) {
			for ( int i = 0 ; i < options.length ; i++ )
				if ( itm.equals(options[i]) ) 
					ticker.setString(info[i]);
			
		}
	//#endif
}
