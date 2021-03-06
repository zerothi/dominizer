/**
 * 
 */
package com;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;

import com.dominizer.GameApp;
import com.util.Dominion;
import com.util.DominionException;
import com.util.SettingsRecordStorage;

import de.enough.polish.ui.Alert;
import de.enough.polish.ui.List;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class ConditionList extends List implements CommandListener, ItemStateListener {
	
	private ConditionTableForm ptF = null;
	private Command selectCmd = new Command( Locale.get("polish.command.select"), Command.BACK, 0);
	private Command newCmd = new Command( Locale.get("screen.Condition.New"), Command.ITEM, 5);
	private Command showCmd = new Command( Locale.get("cmd.Condition.Show"), Command.ITEM, 6);
	private Command gotoCmd = new Command( Locale.get("cmd.Goto.RandomizeSets"), Command.ITEM, 8);
	private Command optionCmd = new Command( Locale.get("cmd.Options.Main"), Command.ITEM, 12);
	private Command perGaugeCmd = new Command( Locale.get("cmd.Percentage.Gauge"), Command.ITEM, 7);
	private Command deleteCmd = new Command( Locale.get("polish.command.delete"), Command.ITEM, 10);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.ITEM, 50);
	
	private String newName = null, newCondition = null;
	private int listType = 0;
	
	private boolean isOnGauge = true;
	/**
	 * 
	 */
	public ConditionList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		this.listType = listType;
		addCommand(selectCmd);
		populateConditions();
		//#= setSelectCommand(selectCmd);
		addCommand(newCmd);
		addCommand(showCmd);
		addCommand(gotoCmd);
		//addCommand(optionCmd);
		//addCommand(perGaugeCmd);
		//addCommand(deleteCmd);
		addCommand(quitCmd);
		setCommandListener(this);
		setItemStateListener(this);
		//#debug dominizer
		System.out.println("initializing conditionTableForm");
		ptF = new ConditionTableForm();
		if ( Dominion.condition.preferredCondition < this.size() ) {
			setSelectedIndex(Dominion.condition.preferredCondition, true);
			focus(Dominion.condition.preferredCondition);
		}
		//#debug dominizer
		System.out.println("done initializing with " + Dominion.condition.preferredCondition + " size= " + this.size());
	}
	
	public void populateConditions() {
		deleteAll();
		for ( int i = 0 ; i < Dominion.condition.size() ; i++ ) {
			//#debug dominizer
			System.out.println("adding condition: " + i);
			if ( Dominion.condition.getName(i) != null ) {
				//#style label
				CardItem ci = new CardItem(Dominion.condition.getName(i), listType);
				if ( i >= Dominion.condition.initialConditions )
					ci.addCommand(deleteCmd);
				append(ci);
				//setPercentage(i, Dominion.I().condition.getPercentage(i));
			}
		}
	}
	
	public void setPercentage(int index, int deciPercentage) {
		CardItem ci;
		if ( deciPercentage > 0 ) {
			//#style label
			ci = new CardItem(Dominion.condition.getName(index)+" "+(deciPercentage*10)+"%", listType);
			Dominion.condition.setPercentage(index, deciPercentage);
		} else {
			//#style label
			ci = new CardItem(Dominion.condition.getName(index), listType);
			Dominion.condition.setPercentage(index, 0);
		}
		set(index, ci);
		if ( size() > index + 1)
			focus(index + 1);
		if ( index > 0 )
			focus(index - 1);
		focus(index);
	}
	
	public void removeCondition(int index) {
		//updateCards(true, -1);
		if ( 0 <= index && index < size() ) {
			Dominion.condition.deleteCondition(index);
			delete(index);
			if ( index > 0 )
				focus(index - 1);
			else if ( size() > index + 1)
				focus(index + 1);
		}
	}
	
	public void saveCondition() {
		newCondition = ptF.getOption();
		if ( Dominion.condition.addCondition(newName, newCondition) ) {
			//updateCards(true, -1);
			populateConditions();
		}
	}
	
	public void updateCards(boolean localUpdate, int specific) {
		if ( localUpdate ) {
			if ( specific == -1 ) {
				for ( int i = 0 ; i < size() ; i++ ) {
					//#debug dominizer
					System.out.println("updating: "+i);
					Dominion.condition.setAvailable(i, getItem(i).isSelected);
				}
			} else {
				Dominion.condition.setAvailable(specific, getItem(specific).isSelected);
			}
		} else {
			if ( specific == -1 ) {
				for ( int i = 0 ; i < size() ; i++ ) {
					//#debug dominizer
					System.out.println("updating: "+i);
					setSelectedIndex(i, Dominion.condition.isAvailable(i));
				}
			} else {
				setSelectedIndex(specific, Dominion.condition.isAvailable(specific));
			}
		}
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_NUM0:
		case Canvas.KEY_NUM1:
		case Canvas.KEY_NUM2:
		case Canvas.KEY_NUM3:
		case Canvas.KEY_NUM4:
		case Canvas.KEY_NUM5:
		case Canvas.KEY_NUM6:
		case Canvas.KEY_NUM7:
		case Canvas.KEY_NUM8:
		case Canvas.KEY_NUM9:
			//setPercentage(getCurrentIndex(), keyCode - Canvas.KEY_NUM0);
			break;
		case Canvas.KEY_POUND:
		case Canvas.KEY_STAR:
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
		Dominion.condition.currentCondition = getSelectedIndex();
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(selectCmd) ) {
			try {
				setSelectedIndex(getCurrentIndex(), true);
				Dominion.condition.currentCondition = getCurrentIndex();
				Dominion.I().randomizeCards(-1, Dominion.RAND_HOLD + Dominion.RAND_CONDITION);
				//ShowCardsForm.instance().addNewCards(Dominion.I().getSelectedCards(Dominion.I().getCurrentSet()));
				CardsList.instance().setCards(Dominion.I().getSelectedCards(Dominion.I().getCurrentSet()), Dominion.I().getCurrentSet());
				//GameApp.instance().changeToScreen(ShowCardsForm.instance());
				GameApp.instance().changeToScreen(CardsList.instance());
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(gotoCmd) ) {
			if ( Dominion.I().getCurrentSet() > 0 ) {
				//GameApp.instance().changeToScreen(ShowCardsForm.instance());
				GameApp.instance().changeToScreen(CardsList.instance());
			} else
				GameApp.instance().showInfo(Locale.get("info.randomized.Sets.NoneCreated"), Alert.FOREVER);
		} else if ( cmd.equals(newCmd) ) {
			isOnGauge = false;
			InputForm.instance().clearInput();
			GameApp.instance().changeToScreen(InputForm.instance().instance(Locale.get("screen.Condition.InputMessage"), this));
		} else if ( cmd.equals(this.optionCmd) ) {
			GameApp.instance().changeToScreen(new OptionForm(""));
		} else if ( cmd.equals(perGaugeCmd) ) {
			isOnGauge = true;
			GaugeForm.instance().setGauge(Locale.get("screen.Condition.Percentage"), true, 10, 0);
			GaugeForm.instance().setCommandListener(this);
			GaugeForm.instance().setGaugeValue(Dominion.condition.getPercentage(getCurrentIndex()));
			GameApp.instance().changeToScreen(GaugeForm.instance());
		} else if ( cmd.equals(showCmd) ) {
			String tmp = Dominion.condition.getCondition(getCurrentIndex());
			GameApp.instance().showInfo(Locale.get("screen.Condition.currentoption", tmp), Alert.FOREVER);
		} else if ( cmd.equals(deleteCmd) ) {
			removeCondition(getCurrentIndex());
		} else if ( cmd.equals(quitCmd) ) {
			GameApp.instance().quit();
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok"))) {
			if ( isOnGauge ) {
				setPercentage(getCurrentIndex(), GaugeForm.instance().getGaugeValue());
				GameApp.instance().changeToScreen(null);
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

	public void itemStateChanged(Item arg0) {
		Dominion.condition.currentCondition = getSelectedIndex();
		//#debug dominizer
		System.out.println("item state changed" + getCurrentIndex() + " and " + getSelectedIndex()+ " current condition is: " + Dominion.condition.currentCondition);
	}
}
