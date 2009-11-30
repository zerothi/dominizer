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


import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import de.enough.polish.ui.ImageItem;
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
public class GameApp extends MIDlet implements CommandListener, ItemCommandListener {
	
	ChoiceGroup whatToDoCG = null;
	ChoiceGroup quickGameRandomizerCG = null;
	Form mainForm = null;
	Command selectCmd = new Command( Locale.get("polish.command.select"), Command.OK, 1);
	Command showRandomizedCardsCmd = new Command( Locale.get("cmd.Randomize"), Command.ITEM, 5 );
	Command showEditCardsCmd = new Command( Locale.get("cmd.EditCards"), Command.ITEM, 6 );
	//Command showCardsListCmd = new Command( Locale.get( "cmd.ShowCards" ), Command.ITEM, 8 );
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
		this.mainForm = new Form("Dominizer");
		//#style choiceGroup
		this.whatToDoCG = new ChoiceGroup(Locale.get("mainScreen.ChoiceWhatToDo"), ChoiceGroup.EXCLUSIVE);// doesn't work yet with multiple
		//style choiceItem
		this.whatToDoCG.append(Locale.get("cmd.Randomize"), null);
		/*//style choiceItem
		this.whatToDoCG.append(Locale.get("cmd.EditCards"), null);*/
		this.whatToDoCG.setDefaultCommand(selectCmd);
		this.whatToDoCG.setItemCommandListener(this);
		//#style filterCards
		this.quickGameRandomizerCG = new ChoiceGroup(Locale.get("mainScreen.QuickSelectExpansions"), ChoiceGroup.MULTIPLE);
		try {
			//style choiceItem 
			this.quickGameRandomizerCG.append(Locale.get("base"), Image.createImage("/base.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("promo"), Image.createImage("/promo.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("intrigue"), Image.createImage("/intrigue.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("seaside"), Image.createImage("/seaside.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for ( int i = 0 ; i < 3 ; i ++ )
			this.quickGameRandomizerCG.setSelectedIndex(i, true);
		//this.commandAction(chooseCmd, (Item) this.quickGameRandomizerCG);
		//this.mainForm.addCommand(chooseCmd);
		this.mainForm.addCommand(this.showRandomizedCardsCmd);
		this.mainForm.addCommand(this.showEditCardsCmd);
		//this.mainForm.addCommand(this.showCardsListCmd);
		this.mainForm.addCommand(this.quitCmd);
		//#ifdef polish.debugEnabled
			this.mainForm.addCommand(this.showLogCmd);
		//#endif
		this.mainForm.setCommandListener(this);
		
		this.mainForm.append(this.whatToDoCG);
		this.mainForm.append(this.quickGameRandomizerCG);
		// You can also use further localization features like the following: 
		//System.out.println("Today is " + Locale.formatDate( System.currentTimeMillis() ));
		//#debug
		System.out.println("initialisation done.");
	}
	
	public void commandAction(Command cmd, Displayable screen) {
		//showAlert("Cmd: " + cmd.getLabel() + ". Screen: " + screen.getTitle());
		if ( cmd == this.showLogCmd ) {
			Debug.showLog(this.display);
			return;
		}
		if ( cmd == this.showRandomizedCardsCmd ) {
			this.showRandomizedCards();
		} else if ( cmd == this.showEditCardsCmd ) {
			this.showEditCards();
		/*
		} else if ( cmd == this.showCardsListCmd ) {
			this.showCardListTable();
		*/
		} else if ( cmd == this.selectCmd ){
			switch ( this.whatToDoCG.getSelectedIndex() ) {
			case 0:
				this.showRandomizedCards();
				break;
			case 1:
				this.showEditCards();
				break;
			}
		}
		if ( cmd == this.quitCmd ) {
			quit();
		}
	}
	
	public void commandAction(Command cmd, Item item) {
		if ( item == this.whatToDoCG ) {
			switch ( this.whatToDoCG.getSelectedIndex() ) {
			case 0:
				this.showRandomizedCards();
				break;
			case 1:
				this.showEditCards();
				break;
			}
		}
		
	}
	
	private void showRandomizedCards() {
		boolean flags[] = new boolean[this.quickGameRandomizerCG.size()];
		if ( this.quickGameRandomizerCG.getSelectedFlags(flags) == 1 && this.quickGameRandomizerCG.isSelected(1) ) {
			showAlert(Locale.get("alert.QuickSelectExpansions.OnlyPromoSelected"));
		} else if ( this.quickGameRandomizerCG.getSelectedFlags(flags) == 0 ) {
			showAlert(Locale.get("alert.QuickSelectExpansions.NoneSelected"));
		} else {
			this.dominion.setExpansionPlayingState("Base", flags[0]);
			this.dominion.setExpansionPlayingState("Promo", flags[1]);
			this.dominion.setExpansionPlayingState("Intrigue", flags[2]);
			this.dominion.setExpansionPlayingState("Seaside", flags[3]);
			ShowCardsForm scForm = new ShowCardsForm(this, dominion, Locale.get("screen.RandomizedCards.title"));
			scForm.reRandomize();
			this.changeToScreen(scForm);
		}
		flags = null;
	}
	
	private void showEditCards() {
		EditCardsForm edForm = new EditCardsForm(this, Locale.get("screen.EditCards.title"));
		edForm.setCards(dominion.getAllCards());
		this.changeToScreen(edForm);
	}
	/*	
	private void showCardListTable() {
		TableCardForm ecForm = new TableCardForm(this, "Table of Cards");
		ecForm.viewCards(dominion.getAllCards());
		this.changeToScreen(ecForm);
	}
	*/
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
		Alert alert = new Alert( Locale.get("alert"), message, null, AlertType.INFO );
		alert.setTimeout( Alert.FOREVER );
		this.display.setCurrent( alert );
	}
	private void quit() {
		notifyDestroyed();
	}
}
