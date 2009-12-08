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

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
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
public class GameApp extends MIDlet {
	Displayable currentForm = null;
	Display display = null;
	private Dominion dominion = null;
	
	public GameApp() {
		super();
		//#debug info
		System.out.println("starting Dominion");
		this.dominion = new Dominion();
		this.currentForm = new QuickRandomizeForm(this, Locale.get("app.name"));
		//#debug info
		System.out.println("initialisation done.");
	}
	
	
	public boolean updateSelectedQuickRandom(boolean[] flags, boolean empty, boolean promo) {
		if ( promo ) {
			this.showAlert(Locale.get("alert.QuickSelectExpansions.OnlyPromoSelected"));
			return false;
		} else if ( empty ) {
			this.showAlert(Locale.get("alert.QuickSelectExpansions.NoneSelected"));
			return false;
		} else {
			this.dominion.setExpansionPlayingState(Locale.get("rms.base"), flags[0]);
			this.dominion.setExpansionPlayingState(Locale.get("rms.promo"), flags[1]);
			this.dominion.setExpansionPlayingState(Locale.get("rms.intrigue"), flags[2]);
			this.dominion.setExpansionPlayingState(Locale.get("rms.seaside"), flags[3]);
			return true;
		}
	}
	
	public void showRandomizedCards(boolean[] flags, boolean empty, boolean promo) {
		if ( updateSelectedQuickRandom(flags, empty, promo) ) {
			ShowCardsForm scForm = new ShowCardsForm(this, this.dominion, Locale.get("screen.RandomizedCards.title"));
			scForm.reRandomize();
			this.changeToScreen(scForm);
		} else
			this.showAlert("Flag: " + flags[0] + flags[1] + flags[2] + flags[3]);
		
	}
	
	public void showBlackMarketDeck(Form previousForm) {
		//updateSelectedQuickRandom();
		BlackMarketForm bmForm = new BlackMarketForm(this, previousForm, Locale.get("screen.BlackMarket.title"));
		bmForm.setBlackMarketDeck(this.dominion.getAllCards());
		this.changeToScreen(bmForm);
	}

	public void changeToScreen(Displayable displayable) {
		if ( displayable == null )
			this.changeToScreen(this.currentForm);
		else
			this.display.setCurrent(displayable);
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
	
	public void quit() {
		notifyDestroyed();
	}
}
