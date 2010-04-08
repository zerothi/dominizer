/**
 * 
 */
package com;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import com.dominizer.GameApp;

import de.enough.polish.ui.Item;
import de.enough.polish.ui.ItemCommandListener;
import de.enough.polish.ui.MessageItem;
import de.enough.polish.ui.TableItem;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class OptionTableForm extends Form implements CommandListener, ItemCommandListener {
	
	private Command selectCmd = new Command( Locale.get("polish.command.select"), Command.BACK, 0);

	public static final int PARENT_START = 0;
	public static final int PARENT_END = 2;
	public static final int OR = 3;
	public static final int AND = 5;
	
	public static final int LESS = 3;
	public static final int EQUAL = 4;
	public static final int GREATER = 5;
	
	public static final int TABLE_IFS = 0;
	public static final int TABLE_TYPE = 6;
	public static final int TABLE_ADDS = 7;
	public static final int TABLE_NUMBER = 8;
	
	private int currentTableType = 0;
	
	private String option = null;
	
	private TableItem tableItem = null;
	
	private StringItem sI = null;
	
	/**
     * @param title
     */
    public OptionTableForm(String title) {
    	//#style mainScreen
	    super(title);
	    //#style defaultTable
	    tableItem = new TableItem();
	    tableItem.setDimension(3, 3);
	    tableItem.setSelectionMode(TableItem.SELECTION_MODE_CELL | TableItem.SELECTION_MODE_COLUMN | TableItem.SELECTION_MODE_ROW);
	    tableItem.setDefaultCommand(selectCmd);
	    tableItem.setItemCommandListener(this);
	    append(tableItem);
	    option = "";
	    //#style label
	    sI = new StringItem(null, option);
	    append(sI);
    }
    
    public void changeToTable(int tableType) {
    	currentTableType = tableType;
    	tableItem.releaseResources();
    	tableItem.setDimension(3, 3);
    	for ( int i = 0 ; i < 9 ; i++ ) {
    		//#style tableCell
    		tableItem.set(i, new MessageItem("" + (i + 1), ""));
    	}
		//#debug dominizer
		System.out.println("adding table type information");
		if ( currentTableType == TABLE_IFS ) {
			//#style tableCell
			tableItem.set(PARENT_START, new MessageItem("" + (PARENT_START+1), "Paren Start"));
			//#style tableCell
			tableItem.set(PARENT_END, new MessageItem("" + (PARENT_END+1), "Paren End"));
			//#style tableCell
			tableItem.set(OR, new MessageItem("" + (OR+1), "Or"));
			//#style tableCell
			tableItem.set(AND, new MessageItem("" + (AND+1), "And"));
			//#style tableCell
			tableItem.set(TABLE_TYPE, new MessageItem("" + (TABLE_TYPE+1), "Type"));
			//#style tableCell
			tableItem.set(TABLE_ADDS, new MessageItem("" + (TABLE_ADDS+1), "Adds"));
			//#style tableCell
			tableItem.set(TABLE_NUMBER, new MessageItem("" + (TABLE_NUMBER+1), "Number"));
		} else if ( currentTableType == TABLE_TYPE ) {
			//#style tableCell
			tableItem.set(Cards.TYPE_ACTION, new MessageItem("" + (Cards.TYPE_ACTION+1), "Action"));
			//#style tableCell
			tableItem.set(Cards.TYPE_VICTORY, new MessageItem("" + (Cards.TYPE_VICTORY+1), "Victory"));
			//#style tableCell
			tableItem.set(Cards.TYPE_TREASURY, new MessageItem("" + (Cards.TYPE_TREASURY+1), "Treasure"));
			//#style tableCell
			tableItem.set(Cards.TYPE_ATTACK, new MessageItem("" + (Cards.TYPE_ATTACK+1), "Attack"));
			//#style tableCell
			tableItem.set(Cards.TYPE_REACTION, new MessageItem("" + (Cards.TYPE_REACTION+1), "Reaction"));
			//#style tableCell
			tableItem.set(Cards.TYPE_DURATION, new MessageItem("" + (Cards.TYPE_DURATION+1), "Duration"));
			//#style tableCell
			tableItem.set(Cards.TYPE_POTION, new MessageItem("" + (Cards.TYPE_POTION+1), "Potion"));
		} else if ( currentTableType == TABLE_ADDS ) {
			//#style tableCell
			tableItem.set(Cards.ADDS_CARDS, new MessageItem("" + (Cards.ADDS_CARDS+1), "Cards"));
			//#style tableCell
			tableItem.set(Cards.ADDS_ACTIONS, new MessageItem("" + (Cards.ADDS_ACTIONS+1), "Actions"));
			//#style tableCell
			tableItem.set(Cards.ADDS_BUYS, new MessageItem("" + (Cards.ADDS_BUYS+1), "Buys"));
			//#style tableCell
			tableItem.set(Cards.ADDS_COINS, new MessageItem("" + (Cards.ADDS_COINS+1), "Coins"));
			//#style tableCell
			tableItem.set(Cards.ADDS_TRASH, new MessageItem("" + (Cards.ADDS_TRASH+1), "Trash"));
			//#style tableCell
			tableItem.set(Cards.ADDS_CURSE, new MessageItem("" + (Cards.ADDS_CURSE+1), "Curse"));
			//#style tableCell
			tableItem.set(Cards.ADDS_POTIONS, new MessageItem("" + (Cards.ADDS_POTIONS+1), "Potion"));
		} else if ( currentTableType == TABLE_NUMBER ) {
			//#style tableCell
			tableItem.set(LESS, new MessageItem("" + (LESS+1), "Less <"));
			//#style tableCell
			tableItem.set(EQUAL, new MessageItem("" + (EQUAL+1), "Equal ="));
			//#style tableCell
			tableItem.set(GREATER, new MessageItem("" + (GREATER+1), "Greater >"));
		}
		//tableItem.focusChild(4);
    }

	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_NUM0:
			if ( option.length() > 0 )
				option = option.substring(0, option.length() - 1);
			break;
		case Canvas.KEY_NUM1:
		case Canvas.KEY_NUM2:
		case Canvas.KEY_NUM3:
		case Canvas.KEY_NUM4:
		case Canvas.KEY_NUM5:
		case Canvas.KEY_NUM6:
		case Canvas.KEY_NUM7:
		case Canvas.KEY_NUM8:
		case Canvas.KEY_NUM9:
			if ( currentTableType == TABLE_IFS ) {
				switch ( keyCode - Canvas.KEY_NUM1 ) {
				case PARENT_START:
					option += "(";break;
				case PARENT_END:
					option += ")";break;
				case OR:
					option += "|";break;
				case AND:
					option += "&";break;
				case TABLE_TYPE:
					changeToTable(TABLE_TYPE);break;
				case TABLE_ADDS:
					changeToTable(TABLE_ADDS);break;
				case TABLE_NUMBER:
					changeToTable(TABLE_NUMBER);break;
				}
			} else if ( currentTableType == TABLE_TYPE ) {
				switch ( keyCode - Canvas.KEY_NUM1 ) {
				case Cards.TYPE_ACTION:
					option += "C";break;
				case Cards.TYPE_VICTORY:
					option += "V";break;
				case Cards.TYPE_TREASURY:
					option += "T";break;
				case Cards.TYPE_ATTACK:
					option += "A";break;
				case Cards.TYPE_REACTION:
					option += "R";break;
				case Cards.TYPE_DURATION:
					option += "D";break;
				case Cards.TYPE_POTION:
					option += "P";break;
				}
				changeToTable(TABLE_NUMBER);break;
			} else if ( currentTableType == TABLE_ADDS ) {
				switch ( keyCode - Canvas.KEY_NUM1 ) {
				case Cards.ADDS_CARDS:
					option += "d";break;
				case Cards.ADDS_ACTIONS:
					option += "a";break;
				case Cards.ADDS_BUYS:
					option += "b";break;
				case Cards.ADDS_COINS:
					option += "c";break;
				case Cards.ADDS_TRASH:
					option += "t";break;
				case Cards.ADDS_CURSE:
					option += "u";break;
				case Cards.ADDS_POTIONS:
					option += "p";break;
				}
				changeToTable(TABLE_NUMBER);break;
			} else if ( currentTableType == TABLE_NUMBER ) {
				GaugeForm.instance().setGauge("asd", true, 10, 1);
				GaugeForm.instance().setCommandListener(this);
				switch ( keyCode - Canvas.KEY_NUM1 ) {
				case LESS:
					option += "<";break;
				case EQUAL:
					option += "=";break;
				case GREATER:
					option += ">";break;
				}
				GameApp.instance().changeToScreen(GaugeForm.instance());
			}
			break;
		case Canvas.KEY_POUND:
		case Canvas.KEY_STAR:
			GameApp.instance().showAlert(option);
		default:
			//#= super.keyPressed(keyCode);
		}
		sI.setText(option);
		//tableItem.setSelectedCell(1,1);
	}
	
	/* (non-Javadoc)
     * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable
     */
    public void commandAction(Command cmd, Displayable disp) {
    	if ( cmd.getLabel().equals(Locale.get("polish.command.ok")) ) {
    		option += GaugeForm.instance().getGaugeValue();
    		sI.setText(option);
    		changeToTable(TABLE_IFS);
    		GameApp.instance().changeToScreen(this);
    	} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel")) ) {
    		option = option.substring(0, option.length() - 2);
    		changeToTable(TABLE_IFS);
    		GameApp.instance().changeToScreen(this);
    	}
    }
    
    public String getOption() {
    	if ( option.equals("") )
    		return null;
    	return option;
    	
    }

	/* (non-Javadoc)
     * @see de.enough.polish.ui.ItemCommandListener#commandAction(de.enough.polish.ui.Command, de.enough.polish.ui.Item)
     */
    public void commandAction(de.enough.polish.ui.Command cmd, Item arg1) {
		keyPressed(tableItem.getSelectedColumn() + 1 + tableItem.getSelectedRow() * 3 + Canvas.KEY_NUM0);
    }
}