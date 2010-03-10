/**
 * 
 */
package com;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import com.dominizer.GameApp;

import de.enough.polish.ui.ChoiceItem;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class TestList extends List implements CommandListener {

	private Command selectCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 6);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 10);

	public TestList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		//#if !polish.android
		addCommand(selectCmd);
		//#= setSelectCommand(selectCmd);
		//#endif
		addCommand(quitCmd);
		setCommandListener(this);
		//#style label
		CardItem cI = new CardItem("Hej", listType);
		cI.setLeftImage(Dominion.getExpansionImage(0));
		cI.setRightImage(Dominion.getExpansionImage(1));
		append(cI);
		//#style label
		CardItem cI1 = new CardItem("Hej2", listType);
		cI1.setLeftImage(Dominion.getExpansionImage(2));
		cI1.setRightImage(Dominion.getExpansionImage(3));
		append(cI1);
	}


	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(quitCmd) ) {
			GameApp.instance().quit();
		}
	}

}