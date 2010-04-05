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

import de.enough.polish.ui.MessageItem;
import de.enough.polish.ui.TableItem;

/**
 * @author nick
 *
 */
public class OptionTableForm extends Form implements CommandListener {

	public static final int PARENT_START = 0;
	public static final int PARENT_END = 2;
	public static final int OR = 3;
	public static final int AND = 5;
	
	public static final int LESS = 0;
	public static final int EQUAL = 1;
	public static final int GREATER = 2;
	
	public static final int TABLE_IFS = 0;
	public static final int TABLE_TYPE = 1;
	public static final int TABLE_ADDS = 2;
	public static final int TABLE_NUMBER = 3;
	
	private int currentTableType = 0;
	
	private String option = null;
	
	private TableItem tableItem = null;
	
	/**
     * @param title
     */
    public OptionTableForm(String title) {
    	//#style mainScreen
	    super(title);
	    //#style defaultTable
	    tableItem = new TableItem(3,3);
	    tableItem.setSelectionMode(TableItem.SELECTION_MODE_CELL);
	    append(tableItem);
    }
    
    public void changeToTable(int tableType) {
    	currentTableType = tableType;
    	tableItem.releaseResources();
    	tableItem.setDimension(3, 3);
    	for ( int i = 0 ; i < 9 ; i++ ) {
    		//#style tableCell
    		tableItem.set(i, new MessageItem("" + (i + 1), "--"));
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
			break;
		case Canvas.KEY_POUND:
		case Canvas.KEY_STAR:
		default:
			//#= super.keyPressed(keyCode);
		}
	}
	
	/* (non-Javadoc)
     * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable
     */
    public void commandAction(Command cmd, Displayable disp) {
	    // TODO Auto-generated method stub
	    
    }
	
	
	
	

}