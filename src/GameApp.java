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
// Following package is needed for Android Compilation

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
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
	Command selectCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 0);
	Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.SCREEN, 0);
	//Command showCardsListCmd = new Command( Locale.get( "cmd.ShowCards" ), Command.ITEM, 8 );
	Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.BACK, 10);
	Display display = null;
	private Dominion dominion = null;
	
	public GameApp() {
		super();
		//#debug info
		System.out.println("starting Dominion");
		this.dominion = new Dominion();
		// Setting up the ChoiceGroup of what to do
		//#style choiceGroup
		this.whatToDoCG = new ChoiceGroup(Locale.get("mainScreen.ChoiceWhatToDo"), ChoiceGroup.EXCLUSIVE);
		//#style choiceItem
		this.whatToDoCG.append(Locale.get("cmd.Randomize.Show"), null);
		//#style choiceItem
		this.whatToDoCG.append(Locale.get("cmd.BlackMarket.Show"), null);
		/*//style choiceItem
		this.whatToDoCG.append(Locale.get("cmd.EditCards.Show"), null);*/
		this.whatToDoCG.addCommand(this.selectCmd);
		this.whatToDoCG.setItemCommandListener(this);
		// Setting up the QuickGame Randomizer
		//#style filterCards
		this.quickGameRandomizerCG = new ChoiceGroup(Locale.get("mainScreen.QuickSelectExpansions"), ChoiceGroup.MULTIPLE);
		try {
			//style choiceItem 
			this.quickGameRandomizerCG.append(Locale.get("base"), Image.createImage("/ba.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("promo"), Image.createImage("/pr.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("intrigue"), Image.createImage("/in.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("seaside"), Image.createImage("/se.png"));
		} catch (IOException e) {
			//style choiceItem 
			this.quickGameRandomizerCG.append(Locale.get("base"), null);
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("promo"), null);
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("intrigue"), null);
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("seaside"), null);
		}
		this.quickGameRandomizerCG.addCommand(this.quickRandomizeCardsCmd);
		this.quickGameRandomizerCG.setItemCommandListener(this);
		this.quickGameRandomizerCG.setSelectedFlags(new boolean[] {true, true, true, false});
		//#style mainScreen
		this.mainForm = new Form(Locale.get("app.name"));
		this.mainForm.addCommand(this.quitCmd);
		this.mainForm.setCommandListener(this);
		this.mainForm.append(this.whatToDoCG);
		this.mainForm.append(this.quickGameRandomizerCG);
		//#debug info
		System.out.println("initialisation done.");
	}
	
	public void commandAction(Command cmd, Displayable screen) {
		//showAlert("Cmd: " + cmd.getLabel() + ". Screen: " + screen.getTitle());
		if ( cmd == this.quickRandomizeCardsCmd ) {
			this.showRandomizedCards();
		} else if ( cmd == this.quitCmd ) {
			this.quit();
		}
	}
	
	public void commandAction(Command cmd, Item item) {
		if ( cmd == this.quitCmd ) {
			this.quit();
		} else if ( item == this.whatToDoCG && cmd == this.selectCmd ) {
			switch ( this.whatToDoCG.getSelectedIndex() ) {
			case 0:
				this.showRandomizedCards();
				break;
			case 1:
				this.showBlackMarketDeck(this.mainForm);
				break;
			}
		} else if ( item == this.quickGameRandomizerCG )
			this.commandAction(cmd, this.mainForm);	
	}

	
	public boolean updateSelectedQuickRandom() {
		boolean flags[] = new boolean[this.quickGameRandomizerCG.size()];
		if ( this.quickGameRandomizerCG.getSelectedFlags(flags) == 1 && this.quickGameRandomizerCG.isSelected(1) ) {
			showAlert(Locale.get("alert.QuickSelectExpansions.OnlyPromoSelected"));
			flags = null;
			return false;
		} else if ( this.quickGameRandomizerCG.getSelectedFlags(flags) == 0 ) {
			showAlert(Locale.get("alert.QuickSelectExpansions.NoneSelected"));
			flags = null;
			return false;
		} else {
			this.dominion.setExpansionPlayingState(Locale.get("rms.base"), flags[0]);
			this.dominion.setExpansionPlayingState(Locale.get("rms.promo"), flags[1]);
			this.dominion.setExpansionPlayingState(Locale.get("rms.intrigue"), flags[2]);
			this.dominion.setExpansionPlayingState(Locale.get("rms.seaside"), flags[3]);
		}
		flags = null;
		return true;
	}
	
	public void showRandomizedCards() {
		if ( updateSelectedQuickRandom() ) {
			ShowCardsForm scForm = new ShowCardsForm(this, this.dominion, Locale.get("screen.RandomizedCards.title"));
			scForm.reRandomize();
			this.changeToScreen(scForm);
		}
		
	}
	
	public void showBlackMarketDeck(Form previousForm) {
		updateSelectedQuickRandom();
		BlackMarketForm bmForm = new BlackMarketForm(this, previousForm, Locale.get("screen.BlackMarket.title"));
		bmForm.setBlackMarketDeck(this.dominion.getAllCards());
		this.changeToScreen(bmForm);
	}

	public void changeToScreen(Form form) {
		if ( form == null )
			this.changeToScreen(this.mainForm);
		else
			this.display.setCurrent(form);
	}

	protected void startApp() throws MIDletStateChangeException {
		//#debug info
		System.out.println("setting display");
		this.display = Display.getDisplay(this);
		this.changeToScreen(null);
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
		Alert alert = new Alert(Locale.get("alert.alert"), message, null, AlertType.WARNING);
		alert.setTimeout(Alert.FOREVER);
		this.display.setCurrent(alert);
	}
	
	/**
	 * @param string
	 */
	public void showInfo(String message) {
		//#style messageAlert
		Alert alert = new Alert(Locale.get("alert.info"), message, null, AlertType.INFO);
		alert.setTimeout(Alert.FOREVER);
		this.display.setCurrent(alert);
	}
	
	private void quit() {
		notifyDestroyed();
	}
}
