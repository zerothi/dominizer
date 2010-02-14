package com;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

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
	private Command gaugeCmd = new Command( Locale.get("cmd.SetCards.Gauge"), Command.SCREEN, 1);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 10);
	public boolean[] flags = new boolean[6];
	private int tmp = 0;
	
	public QuickRandomizeForm(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		for ( tmp = 0 ; tmp < Dominion.I().getExpansions() ; tmp++ ) {
			if ( Dominion.I().getNumberOfExpansionCards()[tmp] > 0 ) {
				//#style label
				this.append(Dominion.getExpansionName(tmp) + " " + Dominion.I().getNumberOfExpansionCards()[tmp], Dominion.getExpansionImage(tmp));
			} else {
				//#style label
				this.append(Dominion.getExpansionName(tmp), Dominion.getExpansionImage(tmp));
			}
			this.setSelectedIndex(tmp, Dominion.I().getExpansionPlayingStates()[tmp]);
		}
		this.addCommand(this.quickRandomizeCardsCmd);
		this.addCommand(this.gaugeCmd);
		this.addCommand(this.quitCmd);
		this.setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable screen) {
		if ( cmd.equals(this.quickRandomizeCardsCmd) ) {
			this.getSelectedFlags(flags);
			Dominion.I().setExpansionPlayingState(flags);
			GameApp.instance().ecFL.updateCards(false);
			GameApp.instance().showRandomizedCards();
		} else if ( cmd.equals(gaugeCmd) ) {
			tmp = UiAccess.getFocusedIndex(this);
			String tmpS = Dominion.getExpansionName(tmp);
			if ( tmp == Dominion.PROMO )
				GaugeForm.instance().setGauge(Locale.get("gauge.expansion.setCards", tmpS), true, 3, 0);
			else
				GaugeForm.instance().setGauge(Locale.get("gauge.expansion.setCards", tmpS), true, 10, 0);
			GaugeForm.instance().setGaugeValue(Dominion.I().getNumberOfExpansionCards()[tmp]);
			GaugeForm.instance().setCommandListener(this);
			GameApp.instance().changeToScreen(GaugeForm.instance());
			tmpS = null;
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok")) ) {
			GameApp.instance().changeToScreen(null);
			this.setCardsFromExpansion(tmp, GaugeForm.instance().getGaugeValue());
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel")) ) {
			GameApp.instance().changeToScreen(null);
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

	private void setCardsFromExpansion(int exp, int numberOfCards) {
		if ( -1 < exp & exp < Dominion.I().getExpansions() ) {
			if ( Dominion.I().getExpansion(exp).size() < numberOfCards )
				GameApp.instance().showAlert(Locale.get("alert.CardsFromExpansion.Promo"));
			else {
				if ( numberOfCards > 0 ) {
					//#style label
					this.set(exp, Dominion.getExpansionName(exp) + " " + numberOfCards, this.getImage(exp));
					Dominion.I().setExpansionPlayingState(exp, true);
				} else {
					//#style label
					this.set(exp, Dominion.getExpansionName(exp), this.getImage(exp));
				}
				this.setSelectedIndex(exp, Dominion.I().getExpansionPlayingStates()[exp]);
				Dominion.I().setCardsUsedForExpansion(exp, numberOfCards);
			}
			if ( exp == 0 )
				UiAccess.setFocusedIndex(this, 1);
			else 
				UiAccess.setFocusedIndex(this, 0);
			UiAccess.setFocusedIndex(this, exp);
		}
	}
}
