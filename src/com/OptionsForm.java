/**
 * 
 */
package com;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import com.dominizer.GameApp;

import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class OptionsForm extends List implements CommandListener{
	
	private Command selectCmd = new Command( Locale.get("polish.command.select"), Command.BACK, 0);
	/**
	 * 
	 */
	public OptionsForm(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		//#style label
		append("Calender", null);
		addCommand(selectCmd);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(selectCmd) ) {
			GameApp.instance().changeToScreen(GameCalendarForm.I());
		}
	}
}
