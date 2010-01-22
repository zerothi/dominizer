package com;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Ticker;

import com.dominizer.GameApp;

import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.Locale;

/**
 * 
 */

/**
 * @author nick
 *
 */
public class QuickRandomizeForm extends List implements CommandListener {

	private Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 10);
	public boolean[] flags = new boolean[4];
	
	public QuickRandomizeForm(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		try {
			//#style label
			this.append(Dominion.getExpansionName(0), Image.createImage("/ba.png"));
			//#style label
			this.append(Dominion.getExpansionName(1), Image.createImage("/pr.png"));
			//#style label
			this.append(Dominion.getExpansionName(2), Image.createImage("/in.png"));
			//#style label
			this.append(Dominion.getExpansionName(3), Image.createImage("/se.png"));
		} catch (IOException e) {
			//#style label
			this.append(Dominion.getExpansionName(0), null);
			//#style label
			this.append(Dominion.getExpansionName(1), null);
			//#style label
			this.append(Dominion.getExpansionName(2), null);
			//#style label
			this.append(Dominion.getExpansionName(3), null);
		}
		for ( int i = 0 ; i < Dominion.I().getExpansions() ; i++ ) {
			if ( Dominion.I().getNumberOfExpansionCards()[i] > 0 ) {
				//#style label
				this.set(i, Dominion.getExpansionName(i) + " " + Dominion.I().getNumberOfExpansionCards()[i], this.getImage(i));
			} else {
				//#style label
				this.set(i, Dominion.getExpansionName(i), this.getImage(i));
			}
			this.setSelectedIndex(i, Dominion.I().getExpansionPlayingStates()[i]);
		}
		this.addCommand(this.quickRandomizeCardsCmd);
		this.addCommand(this.quitCmd);
		this.setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable screen) {
		if ( cmd.equals(this.quickRandomizeCardsCmd) ) {
			this.getSelectedFlags(flags);
			Dominion.I().setExpansionPlayingState(flags);
			GameApp.instance().ecFL.updateCards(false);
			GameApp.instance().showRandomizedCards();
		} else if ( cmd.equals(this.quitCmd) )
			GameApp.instance().quit();
	}

	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_NUM0:
		case Canvas.KEY_NUM1:
		case Canvas.KEY_NUM2:
		case Canvas.KEY_NUM3:
		case Canvas.KEY_NUM4:
		case Canvas.KEY_NUM5:
		case Canvas.KEY_NUM6:
		case Canvas.KEY_NUM7:
		case Canvas.KEY_NUM8:
		case Canvas.KEY_NUM9:
			this.setCardsFromExpansion(keyCode - Canvas.KEY_NUM0);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}

	private void setCardsFromExpansion(int numberCards) {
		//#debug info
		System.out.println("trying to set cards from expansion");
		this.setCardsFromExpansion(UiAccess.getFocusedIndex(this), numberCards);
		
	}

	private void setCardsFromExpansion(int expansion, int numberOfCards) {
		if ( -1 < expansion ) {
			if ( expansion == 1 && numberOfCards > 2 )
				GameApp.instance().showAlert(Locale.get("alert.CardsFromExpansion.Promo"));
			else {
				if ( numberOfCards > 0 ) {
					//#style label
					this.set(expansion, Dominion.getExpansionName(expansion) + " " + numberOfCards, this.getImage(expansion));
					Dominion.I().setExpansionPlayingState(expansion, true);
				} else {
					//#style label
					this.set(expansion, Dominion.getExpansionName(expansion), this.getImage(expansion));
				}
				this.setSelectedIndex(expansion, Dominion.I().getExpansionPlayingStates()[expansion]);
				Dominion.I().setCardsUsedForExpansion(expansion, numberOfCards);
			}
			if ( expansion == 0 )
				UiAccess.setFocusedIndex(this, 1);
			else 
				UiAccess.setFocusedIndex(this, 0);
			UiAccess.setFocusedIndex(this, expansion);
		}
	}
}
