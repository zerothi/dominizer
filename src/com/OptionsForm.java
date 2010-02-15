/**
 * 
 */
package com;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * @author nick
 *
 */
public class OptionsForm extends List implements CommandListener{
	
	/**
	 * 
	 */
	public OptionsForm(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		//#style label
		append("Attack Cards", null);
		//#style label
		append("Victory Cards", null);
		//#style label
		append("Treasure Cards", null);
		//#style label
		append("Reaction Cards", null);
		//#style label
		append("Duration Cards", null);
		//#style label
		append("Trash Cards", null);
		//#style label
		append("Reaction if Attack", null);
	}

	public void commandAction(Command cmd, Displayable disp) {
		
	}

}
