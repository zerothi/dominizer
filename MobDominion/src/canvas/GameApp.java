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
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import de.enough.polish.util.DeviceControl;
import de.enough.polish.util.Locale;

//#ifdef polish.debugEnabled
import de.enough.polish.util.Debug;
import dominion.Card;
import dominion.Dominion;
//#endif
	
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
	
	ChoiceGroup group = null;
	Form mainForm = null;
	Command startGameCmd = new Command( Locale.get( "cmd.StartGame" ), Command.ITEM, 8 );
	Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	//#ifdef polish.debugEnabled
		Command showLogCmd = new Command( Locale.get("cmd.ShowLog"), Command.ITEM, 9 );
	//#endif
	Display display;
	Dominion dominion = null;
	
	public GameApp() {
		super();
		//#debug
		System.out.println("starting Dominion");
		//#ifdef title:defined
			//#= String title = "${ title }";
		//#else
			String title = "J2ME Polish";
		//#endif
		this.mainForm = new Form(title);
		//#style horizontalChoice
		this.group = new ChoiceGroup("Limit search to:", ChoiceGroup.POPUP, null );
		dominion = new Dominion();
		Vector tmp = dominion.randomizeCards();
		for (int i = 0; i < tmp.size() ; i++ ) {
			//#style choiceItem
			this.group.append( ((Card) tmp.elementAt(i)).getName(), null );			
		}
		//#style choiceItem
		this.group.append( "All", null );
		//#style choiceItem
		this.group.append( "Events", null );
		//#style choiceItem
		this.group.append( "Concerts", null );
		//#style choiceItem
		this.group.append( "Cinema", null );
		//#style choiceItem
		this.group.append( "Plays", null );
		this.mainForm.setCommandListener(this);
		this.mainForm.addCommand( this.startGameCmd ); 
		this.mainForm.addCommand( this.quitCmd );
		//#ifdef polish.debugEnabled
			this.mainForm.addCommand( this.showLogCmd );
		//#endif
		this.mainForm.append(this.group); 
		/*
		//#style mainScreen
		this.menuScreen = new List(title, List.IMPLICIT);
		//#style mainCommand
		this.menuScreen.append("Chat", null);
		//#style mainCommand
		this.menuScreen.append("Mail", null);
		//#style mainCommand
		this.menuScreen.append("Settings", null);
		//#style mainCommand
		this.menuScreen.append("Applications", null);
		//#style mainCommand
		this.menuScreen.append("Help", null);
		//#style mainCommand
		this.menuScreen.append("Quit", null);
		//#style mainCommand
		this.menuScreen.append("Documents", null);
		
		this.menuScreen.setCommandListener(this);
		this.menuScreen.addCommand( this.startGameCmd ); 
		this.menuScreen.addCommand( this.quitCmd );
		//#ifdef polish.debugEnabled
			this.menuScreen.addCommand( this.showLogCmd );
		//#endif
		*/
		// You can also use further localization features like the following: 
		//System.out.println("Today is " + Locale.formatDate( System.currentTimeMillis() ));
		
		//#debug
		System.out.println("initialisation done.");
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
	
	public void commandAction(Command cmd, Displayable screen) {		
		if (screen == this.mainForm) {
			//#ifdef polish.debugEnabled
				if (cmd == this.showLogCmd ) {
					Debug.showLog(this.display);
					return;
				}
			//#endif
			if (cmd == ChoiceGroup.MARK_COMMAND) {
				int selectedItem = this.group.getSelectedIndex();
				if (selectedItem == 5) { //quit has been selected
					quit();
				} else {
					showAlert( this.group.getString(selectedItem) );
				}
			} else if (cmd == this.startGameCmd) {
				startGame();
			}
		} 
		if (cmd == this.quitCmd) {
			quit();
		}
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

	private void startGame() {
		Alert alert = null;
		//#style messageAlert
		//#= alert = new Alert( "Welcome", Locale.get( "messages.welcome", "${user.name}" ), null, AlertType.INFO );
		alert.setTimeout( Alert.FOREVER );
		this.display.setCurrent( alert, this.mainForm );
	}
	
	private void quit() {
		notifyDestroyed();
	}
	
	
}
