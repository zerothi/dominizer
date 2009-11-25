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

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import de.enough.polish.ui.Choice;
import de.enough.polish.util.DeviceControl;
import de.enough.polish.util.Locale;

import de.enough.polish.util.Debug;
import dominion.Card;
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
public class GameApp extends MIDlet implements CommandListener, ItemStateListener {
	
	ChoiceGroup group = null;
	Form mainForm = null;
	Command reAssignCardCmd = new Command( Locale.get( "cmd.ReAssign" ), Command.ITEM, 8 );
	Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	Command showLogCmd = new Command( Locale.get("cmd.ShowLog"), Command.ITEM, 9 );
	Display display = null;
	Dominion dominion = null;
	
	public GameApp() {
		super();
		// Initialize Dominion
		dominion = new Dominion();
		//#debug
		System.out.println("starting Dominion");
		//#style mainScreen
		this.mainForm = new Form("Dominion randomizer");
		//#style choiceGroup
		this.group = new ChoiceGroup("Card list", Choice.EXCLUSIVE);// doesn't work yet with multiple
		this.randomizeCards();
		this.mainForm.setCommandListener(this);
		this.mainForm.addCommand( this.reAssignCardCmd ); 
		this.mainForm.addCommand( this.showLogCmd );
		this.mainForm.addCommand( this.quitCmd );
		//#ifdef polish.debugEnabled
			this.mainForm.addCommand( this.showLogCmd );
		//#endif
		this.mainForm.append(this.group);
		this.mainForm.setItemStateListener(this);

		// You can also use further localization features like the following: 
		//System.out.println("Today is " + Locale.formatDate( System.currentTimeMillis() ));
		
		//#debug
		System.out.println("initialisation done.");
	}


	
	public void commandAction(Command cmd, Displayable screen) {		
		if (screen == this.mainForm) {
			if (cmd == this.showLogCmd ) {
				Debug.showLog(this.display);
				return;
			}
			if (cmd == this.showLogCmd) {
				int selectedItem = this.group.getSelectedIndex();
				if (selectedItem == 5) { //quit has been selected
					quit();
				} else {
					showAlert( this.group.getString(selectedItem) );
				}
			} else if (cmd == this.reAssignCardCmd) {
				randomizeCards();
			}
		} 
		if (cmd == this.quitCmd) {
			quit();
		}
	}
	
	public void itemStateChanged(Item item) {
		if (item == this.group)	{
			// TODO: do nothing yet
	    }
	}

	private void randomizeCards() {
		Vector tmp = dominion.randomizeCards();
		this.group.deleteAll();
		for (int i = 0; i < tmp.size() ; i++ ) {
			//#style choiceItem
			this.group.append( ((Card) tmp.elementAt(i)).getName(), null );			
		}
		tmp = null;
	}
	protected void startApp() throws MIDletStateChangeException {
		//#debug
		System.out.println("setting display.");
		this.display = Display.getDisplay(this);
		/*
		this.display.setCurrent( this.menuScreen );
		*/
		
		this.display.setCurrent( this.mainForm );
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
