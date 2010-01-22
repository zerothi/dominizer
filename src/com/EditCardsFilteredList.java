package com;



import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.dominizer.GameApp;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.util.Locale;

public class EditCardsFilteredList extends FilteredList implements CommandListener {
	
	private Command randomizeCmd = new Command(Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 10);
	private int tmp;
	
	public EditCardsFilteredList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		for ( int i = 0 ; i < Dominion.I().getExpansions() ; i++ ){
			for ( int cardNumber = 0 ; cardNumber < Dominion.I().getExpansion(i).size() ; cardNumber++ ) {
				try {
					//#style label
					this.append(Dominion.I().getExpansion(i).getName(cardNumber), Image.createImage("/" + Dominion.I().getExpansion(i).getExpansion(cardNumber) + 
							Dominion.I().getExpansion(i).getCardType(cardNumber) + ".png"));
				} catch (IOException e) {
					//#style label
					this.append(Dominion.I().getExpansion(i).getName(cardNumber), null);
				}
				this.setSelectedIndex(this.size() - 1, Dominion.I().getExpansion(i).isAvailable(cardNumber));
			}
		}
		this.addCommand(randomizeCmd);
		this.addCommand(quitCmd);
		this.setCommandListener(this);
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_STAR:
			if (this.getCurrentIndex() + 10 < this.size() )
				this.focus(this.getCurrentIndex() + 10);
			else
				this.focus(0);
			break;
		case Canvas.KEY_POUND:
			tmp = 0;
			switch ( Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()) ) {
			case 3:	tmp = 0; break;
			case 2:	tmp += Dominion.I().getExpansion(2).size();
			case 1:	tmp += Dominion.I().getExpansion(1).size();
			case 0:	tmp += Dominion.I().getExpansion(0).size();
			}
			this.focus(tmp);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(randomizeCmd) ) {
			//#debug info
			System.out.println("IAA M TRULY CALLED");
			updateCards(true);
			Dominion.I().randomizeCards(Cards.COMPARE_EXPANSION_NAME);
			GameApp.instance().showCurrentSelectedCards();
		} else if ( cmd.equals(quitCmd) ) {
			GameApp.instance().quit();
		}
	}
	
	public void updateCards(boolean localUpdate) {
		if ( localUpdate ) {
			for ( int i = 0 ; i < this.size() ; i++ ) {
				this.changeCard(i, this.getItem(i).isSelected, false);
			}
		} else {
			for ( int i = 0 ; i < this.size() ; i++ ) {
				/*///#debug info
				System.out.println("redoing: " + Dominion.I().getExpansion(
						Dominion.I().getLinearExpansionIndex(i)).isAvailable(
						Dominion.I().getLinearCardIndex(i)));*/
				this.changeCard(i, Dominion.I().getExpansion(
						Dominion.I().getLinearExpansionIndex(i)).isAvailable(
						Dominion.I().getLinearCardIndex(i))
						, false);
			}
		}
	}
	
	private void changeCard(int index, boolean isAvailable, boolean switchState) {
		if ( index < 0 ) 
			return;
		if ( switchState ) {
			this.setSelectedIndex(index, !Dominion.I().getExpansion(
				Dominion.I().getLinearExpansionIndex(this.getCurrentIndex())).isAvailable(
						Dominion.I().getLinearCardIndex(this.getCurrentIndex())));
		} else {
			this.setSelectedIndex(index, isAvailable);
		}
		Dominion.I().getExpansion(Dominion.I().getLinearExpansionIndex(index)).setAvailable(
				Dominion.I().getLinearCardIndex(index), this.getItem(index).isSelected);
	}
}
