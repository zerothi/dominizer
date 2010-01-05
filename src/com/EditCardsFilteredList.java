package com;



import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.dominizer.GameApp;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.ui.List;
import de.enough.polish.util.Locale;

public class EditCardsFilteredList extends FilteredList implements CommandListener {
	
	private Command toggleCmd = new Command(Locale.get("cmd.EditCards.Toggle"), Command.BACK, 0);

	public EditCardsFilteredList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		for ( int i = 0 ; i < Dominion.instance().getExpansions() ; i++ ){
			for ( int cardNumber = 0 ; cardNumber < Dominion.instance().getExpansion(i).size() ; cardNumber++ ) {
				try {
					this.append(Dominion.instance().getExpansion(i).getName(cardNumber), Image.createImage("/" + Dominion.instance().getExpansion(i).getExpansion(cardNumber) + ".png"));
				} catch (IOException e) {
					this.append(Dominion.instance().getExpansion(i).getName(cardNumber), null);
				}
				this.setSelectedIndex(this.size() - 1, Dominion.instance().getExpansion(i).isAvailable(cardNumber));
			}
		}
		this.addCommand(toggleCmd);
		this.setSelectCommand((de.enough.polish.ui.Command) toggleCmd);
		this.setCommandListener(this);
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_POUND:
		case Canvas.KEY_STAR:
			if ( this.getCurrentIndex() < Dominion.instance().getExpansion(0).size() )
				this.focus(Dominion.instance().getExpansion(0).size());
			else if ( this.getCurrentIndex() < 
					Dominion.instance().getExpansion(0).size() + Dominion.instance().getExpansion(1).size() )
				this.focus(Dominion.instance().getExpansion(0).size() + Dominion.instance().getExpansion(1).size());
			else if ( this.getCurrentIndex() < 
					Dominion.instance().getExpansion(0).size() + Dominion.instance().getExpansion(1).size()
					+ Dominion.instance().getExpansion(2).size() )
				this.focus(Dominion.instance().getExpansion(0).size() + Dominion.instance().getExpansion(1).size() + Dominion.instance().getExpansion(2).size());
			else
				this.focus(0);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(toggleCmd) )
			GameApp.instance().showInfo("Selected0 index Card: " + this.getCurrentIndex(), 1000);
		else if ( cmd.equals(List.SELECT_COMMAND) )
			GameApp.instance().showInfo("Selected1 index Card: " + this.getCurrentIndex(), 1000);
	}
}
