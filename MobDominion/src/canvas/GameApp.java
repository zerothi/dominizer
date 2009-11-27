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
package canvas;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import canvas.forms.EditCardsForm;
import canvas.forms.TableCardForm;
import canvas.forms.ShowCardsForm;
import de.enough.polish.ui.Choice;
import de.enough.polish.util.Debug;
import de.enough.polish.util.DeviceControl;
import de.enough.polish.util.Locale;
import dominion.Dominion;
/**
 * <p>Shows a demonstration of the possibilities of J2ME Polish.</p>
 *
 * <p>Copyright Enough Software 2007</p>

 * <pre>
 * history
 *        26-June-2007 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class GameApp extends MIDlet implements CommandListener {
	
	ChoiceGroup whatToDoCG = null;
	Form mainForm = null;
	Command chooseCmd = new Command("Choose", Command.SCREEN, 2);  
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
		initialize();
		//#debug
		System.out.println("initialisation done.");
	}
	
	private void initialize() {
		//#style mainScreen
		this.mainForm = new Form("Dominion randomizer");
		//#style choiceGroup
		this.whatToDoCG = new ChoiceGroup("What to do?", Choice.EXCLUSIVE);// doesn't work yet with multiple
		//style choiceItem
		this.whatToDoCG.append("Randomize", null);
		this.whatToDoCG.append("Settings", null);
		this.mainForm.setCommandListener(this);
		this.mainForm.addCommand(this.showRandomizedCardsCmd);
		this.mainForm.addCommand(this.showEditCardsCmd);
		this.mainForm.addCommand(this.showCardsListCmd);
		this.mainForm.addCommand(this.showLogCmd);
		this.mainForm.addCommand(this.quitCmd);
		//#ifdef polish.debugEnabled
			this.mainForm.addCommand(this.showLogCmd);
		//#endif
		this.mainForm.append(this.whatToDoCG);
		// You can also use further localization features like the following: 
		//System.out.println("Today is " + Locale.formatDate( System.currentTimeMillis() ));
	}


	
	public void commandAction(Command cmd, Displayable screen) {		
		if (screen == this.mainForm) {
			if (cmd == this.showLogCmd ) {
				Debug.showLog(this.display);
				return;
			}
			if ( cmd == this.chooseCmd ) {
				int selectedItem = this.whatToDoCG.getSelectedIndex();
				switch ( selectedItem ) {
				case 0:
					this.showRandomizedCards();
					break;
				case 1:
					showAlert( this.whatToDoCG.getString(1));
					break;
				}
			} else if (cmd == this.showRandomizedCardsCmd) {
				this.showRandomizedCards();
			} else if (cmd == this.showEditCardsCmd) {
				this.showEditCards();
			} else if (cmd == this.showCardsListCmd) {
				this.showCardListTable();
			}
		} 
		if (cmd == this.quitCmd) {
			quit();
		}
	}
	
	private void showRandomizedCards() {
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
	
	public void changeToScreen(Form form) {
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
		this.showEditCards();
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
	private void showAlert(String message) {
		//#style messageAlert
		Alert alert = new Alert( "Alert", message, null, AlertType.INFO );
		alert.setTimeout( Alert.FOREVER );
		this.display.setCurrent( alert );
	}
	private void quit() {
		notifyDestroyed();
	}	
}
