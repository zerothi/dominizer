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
import com.util.Cards;
import com.util.Dominion;

import de.enough.polish.ui.Alert;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.ItemCommandListener;
import de.enough.polish.ui.MessageItem;
import de.enough.polish.ui.TableItem;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class ConditionTableForm extends Form implements CommandListener, ItemCommandListener {
	
	private Command doneCmd = new Command( Locale.get("cmd.Condition.Save"), Command.SCREEN, 0);
	private Command deleteCmd = new Command( Locale.get("polish.command.delete"), Command.SCREEN, 25);
	private Command backCmd = new Command( Locale.get("polish.command.back"), Command.SCREEN, 50);
	
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
	public static final int TABLE_EXPANSIONS = 8;
	public static final int TABLE_NUMBER = 10;
	
	private int currentTableType = 0;
	
	private String option = null;
	
	private TableItem tableItem = null;
	
	private StringItem sITitle, sI = null;
	
	/**
     * @param title
     */
    public ConditionTableForm() {
    	//#style mainScreen
	    super(null);
	    //#style label
	    sITitle = new StringItem(null, "");
	    append(sITitle);
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
	    addCommand(doneCmd);
	    addCommand(deleteCmd);
	    addCommand(backCmd);
	    setCommandListener(this);
    }
      
    public void setConditionTitle(String title) {
    	sITitle.setLabel(title);
    	GaugeForm.instance().setCommandListener(this);
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
			tableItem.set(PARENT_START, new MessageItem("" + (PARENT_START+1), "("));
			//#style tableCell
			tableItem.set(PARENT_END, new MessageItem("" + (PARENT_END+1), ")"));
			//#style tableCell
			tableItem.set(OR, new MessageItem("" + (OR+1), "|"));
			//#style tableCell
			tableItem.set(AND, new MessageItem("" + (AND+1), "&"));
			//#style tableCell
			tableItem.set(TABLE_TYPE, new MessageItem("" + (TABLE_TYPE+1), Locale.get("card.type")));
			//#style tableCell
			tableItem.set(TABLE_ADDS, new MessageItem("" + (TABLE_ADDS+1), Locale.get("card.adds")));
			//#style tableCell
			tableItem.set(TABLE_EXPANSIONS, new MessageItem("" + (TABLE_EXPANSIONS+1), "Expansions"));
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
			// # style tableCell
			//tableItem.set(Cards.TYPE_POTION, new MessageItem("" + (Cards.TYPE_POTION+1), "Potion"));
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
			tableItem.set(Cards.ADDS_VICTORY_POINTS, new MessageItem("" + (Cards.ADDS_VICTORY_POINTS+1), "Potions"));
			//#style tableCell
			tableItem.set(Cards.ADDS_POTIONS, new MessageItem("" + (Cards.ADDS_POTIONS+1), "Potions"));
		} else if ( currentTableType == TABLE_EXPANSIONS ) {
			//#style tableCell
			tableItem.set(Dominion.BASE, new MessageItem("" + (Dominion.BASE+1), Locale.get("base")));
			//#style tableCell
			tableItem.set(Dominion.INTRIGUE, new MessageItem("" + (Dominion.INTRIGUE+1), Locale.get("intrigue")));
			//#style tableCell
			tableItem.set(Dominion.SEASIDE, new MessageItem("" + (Dominion.SEASIDE+1), Locale.get("seaside")));
			//#style tableCell
			tableItem.set(Dominion.ALCHEMY, new MessageItem("" + (Dominion.ALCHEMY+1), Locale.get("alchemy")));
			//#style tableCell
			tableItem.set(Dominion.PROSPERITY, new MessageItem("" + (Dominion.PROSPERITY+1), Locale.get("prosperity")));
			//#style tableCell
			tableItem.set(Dominion.PROMO, new MessageItem("" + (Dominion.PROMO+1), Locale.get("promo")));
			//#style tableCell
			tableItem.set(Dominion.USER, new MessageItem("" + (Dominion.USER+1), Locale.get("user")));			
		} else if ( currentTableType == TABLE_NUMBER ) {
			//#style tableCell
			tableItem.set(LESS, new MessageItem("" + (LESS+1), "<"));
			//#style tableCell
			tableItem.set(EQUAL, new MessageItem("" + (EQUAL+1), "="));
			//#style tableCell
			tableItem.set(GREATER, new MessageItem("" + (GREATER+1), ">"));
		}
		tableItem.setSelectedCell(0, 0);
		tableItem.setSelectedCell(1, 1);
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
				case TABLE_EXPANSIONS:
					changeToTable(TABLE_EXPANSIONS);break;
				}
			} else if ( currentTableType == TABLE_TYPE ) {
				switch ( keyCode - Canvas.KEY_NUM1 ) {
				case Cards.TYPE_ACTION:
					GaugeForm.instance().setGauge(Locale.get("card.action"), true, 10, 1);
					option += "C";break;
				case Cards.TYPE_VICTORY:
					GaugeForm.instance().setGauge(Locale.get("card.victory"), true, 10, 1);
					option += "V";break;
				case Cards.TYPE_TREASURY:
					GaugeForm.instance().setGauge(Locale.get("card.treasury"), true, 10, 1);
					option += "T";break;
				case Cards.TYPE_ATTACK:
					GaugeForm.instance().setGauge(Locale.get("card.attack"), true, 10, 1);
					option += "A";break;
				case Cards.TYPE_REACTION:
					GaugeForm.instance().setGauge(Locale.get("card.reaction"), true, 10, 1);
					option += "R";break;
				case Cards.TYPE_DURATION:
					GaugeForm.instance().setGauge(Locale.get("card.duration"), true, 10, 1);
					option += "D";break;
				default: return;
				}
				changeToTable(TABLE_NUMBER);break;
			} else if ( currentTableType == TABLE_ADDS ) {
				switch ( keyCode - Canvas.KEY_NUM1 ) {
				case Cards.ADDS_CARDS:
					GaugeForm.instance().setGauge(Locale.get("card.attack"), true, 10, 1);
					option += "d";break;
				case Cards.ADDS_ACTIONS:
					GaugeForm.instance().setGauge(Locale.get("card.adds.actions"), true, 10, 1);
					option += "a";break;
				case Cards.ADDS_BUYS:
					GaugeForm.instance().setGauge(Locale.get("card.adds.buys"), true, 10, 1);
					option += "b";break;
				case Cards.ADDS_COINS:
					GaugeForm.instance().setGauge(Locale.get("card.adds.coins"), true, 10, 1);
					option += "c";break;
				case Cards.ADDS_TRASH:
					GaugeForm.instance().setGauge(Locale.get("card.adds.trash"), true, 10, 1);
					option += "t";break;
				case Cards.ADDS_CURSE:
					GaugeForm.instance().setGauge(Locale.get("card.adds.curses"), true, 10, 1);
					option += "u";break;
				case Cards.ADDS_POTIONS:
					GaugeForm.instance().setGauge(Locale.get("card.adds.potions"), true, 10, 1);
					option += "p";break;
				case Cards.ADDS_VICTORY_POINTS:
					GaugeForm.instance().setGauge(Locale.get("card.adds.victorypoints"), true, 10, 1);
					option += "v";break;
				default: return;
				}
				changeToTable(TABLE_NUMBER);break;
			} else if ( currentTableType == TABLE_EXPANSIONS ) {
				switch ( keyCode - Canvas.KEY_NUM0 ) {
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					GaugeForm.instance().setGauge(Dominion.getExpansionName(keyCode - Canvas.KEY_NUM1), true, 10, 1);
					option += "" + (keyCode - Canvas.KEY_NUM1);break;
				default: return;
				}
				changeToTable(TABLE_NUMBER);break;
			} else if ( currentTableType == TABLE_NUMBER ) {
				switch ( keyCode - Canvas.KEY_NUM1 ) {
				case LESS:
					option += "<";break;
				case EQUAL:
					option += "=";break;
				case GREATER:
					option += ">";break;
				default: return;
				}
				GameApp.instance().changeToScreen(GaugeForm.instance());
			}
			break;
		case Canvas.KEY_POUND:
		case Canvas.KEY_STAR:
			GameApp.instance().showInfo(Locale.get("screen.Condition.currentoption", option), Alert.FOREVER);
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
    		if ( option.length() > 2 )
    			option = option.substring(0, option.length() - 2);
    		else
    			option = "";
    		changeToTable(TABLE_IFS);
    		GameApp.instance().changeToScreen(this);
    	} else if ( cmd.equals(doneCmd) ) {
    		GameApp.instance().cF.saveCondition();
    		GameApp.instance().changeToScreen(null);
    	} else if ( cmd.equals(backCmd) ) {
    		GameApp.instance().changeToScreen(null);
    	} else if ( cmd.equals(deleteCmd) ) {
    		keyPressed(Canvas.KEY_NUM0);
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