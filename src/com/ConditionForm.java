/**
 * 
 */
package com;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.dominizer.GameApp;

import de.enough.polish.ui.List;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class ConditionForm extends List implements CommandListener {
	
	private ConditionTableForm ptF = null;
	private Command selectCmd = new Command( Locale.get("polish.command.select"), Command.BACK, 0);
	private Command newCmd = new Command( Locale.get("screen.Condition.New"), Command.ITEM, 5);
	private Command perGaugeCmd = new Command( Locale.get("cmd.Percentage.Gauge"), Command.ITEM, 7);
	private Command deleteCmd = new Command( Locale.get("polish.command.delete"), Command.ITEM, 10);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.ITEM, 50);
	
	private String newName = null, newCondition = null;
	
	private boolean isOnGauge = true;
	/**
	 * 
	 */
	public ConditionForm(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		addCommand(selectCmd);
		populateConditions();
		//#= setSelectCommand(selectCmd);
		addCommand(newCmd);
		addCommand(perGaugeCmd);
		addCommand(deleteCmd);
		addCommand(quitCmd);
		setCommandListener(this);
		//#debug dominizer
		System.out.println("initializing conditionTableForm");
		ptF = new ConditionTableForm();
		//#debug dominizer
		System.out.println("done initializing");
	}
	
	public void populateConditions() {
		deleteAll();
		for ( int i = 0 ; i < Dominion.I().condition.size() ; i++ ) {
			//#debug dominizer
			System.out.println("adding condition: " + i);
			if ( Dominion.I().condition.getName(i) != null ) {
				//#style label
				append(Dominion.I().condition.getName(i), null);
				setPercentage(i, Dominion.I().condition.getPercentage(i));
			}
		}
		updateCards(true, -1);
	}
	
	public void setPercentage(int index, int deciPercentage) {
		if ( deciPercentage > 0 ) {
			set(index, Dominion.I().condition.getName(index)+" "+(deciPercentage*10)+"%", null);
			Dominion.I().condition.setPercentage(index, deciPercentage);
		} else {
			set(index, Dominion.I().condition.getName(index), null);
			Dominion.I().condition.setPercentage(index, 0);
		}
		if ( index > 0 )
			focus(index - 1);
		if ( size() > index + 1)
			focus(index + 1);
		focus(index);
	}
	
	public void removeCondition(int index) {
		updateCards(true, -1);
		if ( 0 <= index & index < size() ) {
			Dominion.I().condition.deleteCondition(index);
			delete(index);
			if ( index > 0 )
				focus(index - 1);
			else if ( size() > index + 1)
				focus(index + 1);
		}
	}
	
	public void saveCondition() {
		newCondition = ptF.getOption();
		if ( Dominion.I().condition.addCondition(newName, newCondition) ) {
			updateCards(true, -1);
			populateConditions();
		}
	}
	
	public void updateCards(boolean localUpdate, int specific) {
		if ( localUpdate ) {
			if ( specific == -1 ) {
				for ( int i = 0 ; i < size() ; i++ ) {
					//#debug dominizer
					System.out.println("updating: "+i);
					Dominion.I().condition.setAvailable(i, getItem(i).isSelected);
				}
			} else {
				Dominion.I().condition.setAvailable(specific, getItem(specific).isSelected);
			}
		} else {
			if ( specific == -1 ) {
				for ( int i = 0 ; i < size() ; i++ ) {
					//#debug dominizer
					System.out.println("updating: "+i);
					setSelectedIndex(i, Dominion.I().condition.isAvailable(i));
				}
			} else {
				setSelectedIndex(specific, Dominion.I().condition.isAvailable(specific));
			}
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(selectCmd) ) {
			// TODO randomize using condition
		} else if ( cmd.equals(newCmd) ) {
			isOnGauge = false;
			InputForm.instance().clearInput();
			GameApp.instance().changeToScreen(InputForm.instance().instance(Locale.get("screen.RandomizedCards.InputMessage"), this));
		} else if ( cmd.equals(perGaugeCmd) ) {
			isOnGauge = true;
			GaugeForm.instance().setGauge(Locale.get("screen.Condition.Percentage"), true, 10, 0);
			GaugeForm.instance().setCommandListener(this);
			GaugeForm.instance().setGaugeValue(Dominion.I().condition.getPercentage(getCurrentIndex()));
			GameApp.instance().changeToScreen(GaugeForm.instance());
		} else if ( cmd.equals(deleteCmd) ) {
			removeCondition(getCurrentIndex());
		} else if ( cmd.equals(quitCmd) ) {
			GameApp.instance().quit();
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok"))) {
			if ( isOnGauge ) {
				setPercentage(getCurrentIndex(), GaugeForm.instance().getGaugeValue());
			} else {
				if ( InputForm.instance().getInput().indexOf(SettingsRecordStorage.BIG_SPLITTER) > 0 )
					GameApp.instance().showAlert(Locale.get("alert.Randomization.Save.IllegalChar"));
				else if ( !InputForm.instance().getInput().equals("") ) {
					newName = InputForm.instance().getInput();
					ptF.setConditionTitle(newName);
					ptF.changeToTable(ConditionTableForm.TABLE_IFS);
					GameApp.instance().changeToScreen(ptF);
				}
			}
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.clear"))) {
			InputForm.instance().clearInput();
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel"))) {
			GameApp.instance().changeToScreen(null);
		}
	}
}
