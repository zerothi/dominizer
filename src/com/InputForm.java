/**
 * 
 */
package com;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class InputForm extends Form {
	private static InputForm iF = null;
	private static TextField tF = null;
	
	private InputForm(String title) {
		//#style mainPopupScreen
		super(title);
		this.addCommand(new Command(Locale.get("polish.command.ok"), Command.OK, 1));
		this.addCommand(new Command(Locale.get("polish.command.cancel"), Command.SCREEN, 10));
		//#style inputTextField
		tF = new TextField(Locale.get("screen.Input.inputname"), "", 15, TextField.ANY);
		this.append(tF);
	}
	
	public static InputForm instance() {
		return instance(Locale.get("screen.Input.title"), Locale.get("screen.Input.defaultmessage"), null);
	}
	
	public InputForm instance(String message, CommandListener cmdListener) {
		return instance(Locale.get("screen.Input.title"), message, cmdListener);
	}
	
	public static InputForm instance(String title, String message, CommandListener cmdListener) {
		if ( iF == null )
			iF = new InputForm(title);
		tF.setLabel(message);
		iF.setCommandListener(cmdListener);
		return iF;
	}
	
	public String getInput() {
		if ( tF.getString().trim().equals("") )
			return null;
		return tF.getString().trim();
	}
	
	public void clearInput() {
		tF.setString("");
	}
}
