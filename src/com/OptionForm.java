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
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.Ticker;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.dominizer.GameApp;
import com.util.Cards;
import com.util.Dominion;
import com.util.SettingsRecordStorage;

import de.enough.polish.ui.Alert;
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
		options = new ChoiceGroup[4];
		info = new String[options.length];
		//#style horizontalChoice
		options[0] = new ChoiceGroup(Locale.get("screen.options.sort"), ChoiceGroup.EXCLUSIVE);
		info[0] = new String(Locale.get("screen.options.sort.info"));
		options[0].append(Locale.get("cmd.Sort.ExpName"), null);
		options[0].append(Locale.get("cmd.Sort.ExpCost"), null);
		options[0].append(Locale.get("cmd.Sort.Name"), null);
		options[0].append(Locale.get("cmd.Sort.CostName"), null);
		options[0].append(Locale.get("cmd.Sort.CostExp"), null);
		options[0].setSelectedIndex(Cards.COMPARE_PREFERRED, true);
		//#style horizontalChoice
		options[1]  = new ChoiceGroup(Locale.get("screen.options.numberofsavedsets"), ChoiceGroup.EXCLUSIVE);
		info[1] = new String(Locale.get("screen.options.numberofsavedsets.info"));
		for (int i = 0 ; i < Dominion.TOTAL_CARDS / 5 ; i++ )
			options[1].append("" + i, null);
		options[1].setSelectedIndex(Dominion.SETS_SAVE, true);
		//#style horizontalChoice
		options[2]  = new ChoiceGroup(Locale.get("screen.options.preferredcondition"), ChoiceGroup.EXCLUSIVE);
		info[2] = new String(Locale.get("screen.options.preferredcondition.info"));
		for (int i = 0 ; i < Dominion.condition.size() ; i++ ) {
			options[2].append(Dominion.condition.getName(i), null);
			if ( Dominion.condition.getPreferredCondition() == i )
				options[2].setSelectedIndex(i, true);
		}
		//#style horizontalChoice
		options[3] = new ChoiceGroup(Locale.get("screen.Options.CardsInSet"), ChoiceGroup.EXCLUSIVE);
		info[3] = new String(Locale.get("screen.Options.CardsInSet.info"));
		for ( int i = 5 ; i < 16 ; i++ ) {
			options[3].append("" + i , null);
			if ( i == 10 )
				options[3].setSelectedIndex(i - 5, true);
		}

		for (int i = 0 ; i < options.length ; i++ )
			append(options[i]);
		setItemStateListener(this);
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
		String s = "";
		if ( cmd.equals(saveCmd) ) {
			try {
				if ( SettingsRecordStorage.instance().changeToRecordStore("settings") ) {
					//#= Cards.COMPARE_PREFERRED = options[0].getSelectedIndex();
					s += Cards.COMPARE_PREFERRED;
					SettingsRecordStorage.instance().addData("sort", "" + Cards.COMPARE_PREFERRED);
					s += "." + options[1].getSelectedIndex();
					Dominion.SETS_SAVE = options[1].getSelectedIndex();
					SettingsRecordStorage.instance().addData("randsave", "" + options[1].getSelectedIndex());
					SettingsRecordStorage.instance().writeData();
					SettingsRecordStorage.instance().closeRecord();
				}
				if ( SettingsRecordStorage.instance().changeToRecordStore("condition") ) {
					s += "." + options[2].getSelectedIndex();
					for ( int i = 0 ; i < Dominion.condition.size() ; i++ ) {
						if ( Dominion.condition.getName(i).equals(options[2].getString(options[2].getSelectedIndex())) ) {
							Dominion.condition.setPreferredCondition(i);
							i = Dominion.condition.size();
						}
					}
					SettingsRecordStorage.instance().addData("preferred", "" + options[2].getString(options[2].getSelectedIndex()));
				}
				GameApp.instance().showInfo(Locale.get("screen.Options.SaveSuccessful"), Alert.FOREVER);
			} catch (RecordStoreFullException e) {
			} catch (RecordStoreNotFoundException e) {
			} catch (RecordStoreException e) {
			}
			//#debug dominizer
			System.out.println("the saved options are: " + s);
		} else if ( cmd.equals(backCmd) ) {
			GameApp.instance().returnToPreviousScreen();
		}
	}

	public void itemStateChanged(Item item) {
		for ( int i = 0 ; i < options.length ; i++ )
			if ( item.equals(options[i]) ) 
				ticker.setString(info[i]);
	}
}
