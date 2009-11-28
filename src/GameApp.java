/*
 * Created on 26-June-2007 at 16:14:27.
 * 
 * Copyright (c) 2007 Robert Virkus / Enough Software
 *
 * This file is part of J2ME Polish.
 *
 * J2ME Polish is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * J2ME Polish is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with J2ME Polish; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Commercial licenses are also available, please
 * refer to the accompanying LICENSE.txt or visit
 * http://www.j2mepolish.org for details.
 */


import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import de.enough.polish.util.Debug;
import de.enough.polish.util.DeviceControl;
import de.enough.polish.util.Locale;
/**
 * <p>The main app for Dominizer</p>
 *
 * <p>Copyright Nick Papior Andersen Software</p>

 * <pre>
 * history
 *        21-November-2009 - Nick - creation
 * </pre>
 * @author Nick Papior Andersen, nickpapior@gmail.com
 */
public class GameApp extends MIDlet implements CommandListener, ItemStateListener, ItemCommandListener {
	
	ChoiceGroup whatToDoCG = null;
	ChoiceGroup quickGameRandomizerCG = null;
	Form mainForm = null;
	Command chooseCmd = new Command("Choose", Command.SCREEN, 1);
	Command showRandomizedCardsCmd = new Command( Locale.get( "cmd.Randomize" ), Command.ITEM, 5 );
	Command showEditCardsCmd = new Command( Locale.get( "cmd.EditCards" ), Command.ITEM, 6 );
	Command showCardsListCmd = new Command( Locale.get( "cmd.ShowCards" ), Command.ITEM, 8 );
	Command showLogCmd = new Command( Locale.get("cmd.ShowLog"), Command.ITEM, 9 );
	Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	Display display = null;
	public Dominion dominion = null;
	
	public GameApp() {
		super();
		//#debug
		System.out.println("starting Dominion");
		dominion = new Dominion();
		//#style mainScreen
		this.mainForm = new Form("Dominion randomizer");
		//#style choiceGroup
		this.whatToDoCG = new ChoiceGroup("What to do?", ChoiceGroup.EXCLUSIVE);// doesn't work yet with multiple
		//style choiceItem
		this.whatToDoCG.append("Randomize", null);
		//style choiceItem
		this.whatToDoCG.append("Settings", null);
		this.whatToDoCG.addCommand(chooseCmd);
		this.whatToDoCG.setDefaultCommand(chooseCmd);
		this.whatToDoCG.setItemCommandListener(this);
		//#style filterCards
		this.quickGameRandomizerCG = new ChoiceGroup("Expansions used:", ChoiceGroup.MULTIPLE);
		//style choiceItem
		this.quickGameRandomizerCG.append("Base", null);
		//style choiceItem
		this.quickGameRandomizerCG.append("Promo", null);
		//style choiceItem
		this.quickGameRandomizerCG.append("Intrigue", null);
		//style choiceItem
		this.quickGameRandomizerCG.append("Seaside", null);
		for ( int i = 0 ; i < 1 ; i ++ )
			this.quickGameRandomizerCG.setSelectedIndex(i, true);
		this.quickGameRandomizerCG.addCommand(chooseCmd);
		this.quickGameRandomizerCG.setDefaultCommand(chooseCmd);
		this.quickGameRandomizerCG.setItemCommandListener(this);
		//this.commandAction(chooseCmd, (Item) this.quickGameRandomizerCG);
		//this.mainForm.addCommand(chooseCmd);
		this.mainForm.addCommand(this.showRandomizedCardsCmd);
		this.mainForm.addCommand(this.showEditCardsCmd);
		this.mainForm.addCommand(this.showCardsListCmd);
		this.mainForm.addCommand(this.quitCmd);
		//#ifdef polish.debugEnabled
			this.mainForm.addCommand(this.showLogCmd);
		//#endif
		this.mainForm.setCommandListener(this);
		this.mainForm.setItemStateListener(this);
		
		this.mainForm.append(this.whatToDoCG);
		this.mainForm.append(this.quickGameRandomizerCG);
		// You can also use further localization features like the following: 
		//System.out.println("Today is " + Locale.formatDate( System.currentTimeMillis() ));
		//#debug
		System.out.println("initialisation done.");
	}
	
	public void commandAction(Command cmd, Displayable screen) {
		showAlert("Cmd: " + cmd.getLabel() + ". Screen: " + screen.getTitle());
		if ( screen == this.mainForm ) {
			if ( cmd == this.showLogCmd ) {
				Debug.showLog(this.display);
				return;
			}
			if ( cmd == this.showRandomizedCardsCmd ) {
				this.showRandomizedCards();
			} else if ( cmd == this.showEditCardsCmd ) {
				this.showEditCards();
			} else if ( cmd == this.showCardsListCmd ) {
				this.showCardListTable();
			} else if ( 2 == 1 ){
				int selectedItem = this.whatToDoCG.getSelectedIndex();
				switch ( selectedItem ) {
				case 0:
					//this.showRandomizedCards();
					break;
				case 1:
					showAlert( this.whatToDoCG.getString(1));
					break;
				}
			}
		}
		if ( cmd == this.quitCmd ) {
			quit();
		}
		//showAlert(cmd.getLabel() + this.whatToDoCG.getSelectedIndex());
	}

	public void commandAction(Command cmd, Item item) {
		showAlert("Cmd: " + cmd.getLabel() + ". Item: " + item.getLabel());
		if ( cmd == chooseCmd ) {
			if ( item == this.whatToDoCG ) {
				//showAlert(cmd.getLabel() + this.whatToDoCG.getSelectedIndex());
				switch ( this.whatToDoCG.getSelectedIndex() ) {
				case -1:
					showAlert("No Action");
					break;
				case 0:
					showAlert(cmd.getLabel() + this.whatToDoCG.getSelectedIndex());
					this.showRandomizedCards();
					break;
				case 1:
					this.showEditCards();
					break;
				default:
					showAlert(cmd.getLabel() + this.whatToDoCG.getSelectedIndex());
				}
			} else if ( item == this.quickGameRandomizerCG ) {
				showAlert( this.quickGameRandomizerCG.getString(1));
				boolean flags[] = new boolean[this.quickGameRandomizerCG.size()];
				this.quickGameRandomizerCG.getSelectedFlags(flags);
				for (int i = 0; i < this.quickGameRandomizerCG.size() - 1; i++)
		        	this.dominion.setExpansionPlayingState(this.quickGameRandomizerCG.getString(i), flags[i]);
			}
		} else
	    	this.commandAction(cmd, this.mainForm);
	}
	
	public void itemStateChanged(Item item) {
		if ( item == this.quickGameRandomizerCG ) {
			this.commandAction(chooseCmd, item);
		}
	}
	
	private void showRandomizedCards() {
		String temp = "";
		boolean flags[] = new boolean[this.quickGameRandomizerCG.size()];
		this.quickGameRandomizerCG.getSelectedFlags(flags);
		for (int i = 0; i < this.quickGameRandomizerCG.size() - 1; i++) {
        	this.dominion.setExpansionPlayingState(this.quickGameRandomizerCG.getString(i), flags[i]);
        	temp += flags[i];
		}
		showAlert(temp);
		ShowCardsForm scForm = new ShowCardsForm(this, "Randomized Cards");
		scForm.viewCards(dominion.getRandomizedCards());
		this.changeToScreen(scForm);
	}
	
	private void showEditCards() {
		EditCardsForm edForm = new EditCardsForm(this, "Edit Cards");
		edForm.setCards(dominion.getAllCards());
		this.changeToScreen(edForm);
	}
	
	private void showCardListTable() {
		TableCardForm ecForm = new TableCardForm(this, "Table of Cards");
		ecForm.viewCards(dominion.getAllCards());
		this.changeToScreen(ecForm);
	}
	
	private void changeToScreen(Form form) {
		this.display = Display.getDisplay(this);
		this.display.setCurrent(form);
	}
	
	public void returnToMainScreen() {
		this.changeToScreen(this.mainForm);
	}

	protected void startApp() throws MIDletStateChangeException {
		//#debug
		System.out.println("setting display.");
		this.changeToScreen(this.mainForm);
		//this.showRandomizedCards();
		//#debug
		System.out.println("sample application is up and running.");
	}

	protected void pauseApp() {
		DeviceControl.lightOff();
	}
	
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		// just quit
	}
	
	/**
	 * @param string
	 */
	public void showAlert(String message) {
		//#style messageAlert
		Alert alert = new Alert( "Alert", message, null, AlertType.INFO );
		alert.setTimeout( Alert.FOREVER );
		this.display.setCurrent( alert );
	}
	private void quit() {
		notifyDestroyed();
	}

	
}
