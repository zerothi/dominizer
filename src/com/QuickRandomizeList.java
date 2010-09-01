package com;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import com.dominizer.GameApp;
import com.util.Dominion;
import com.util.DominionException;

import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.Locale;

/**
 * 
 */

/**
 * @author nick
 *
 */
public class QuickRandomizeList extends List implements CommandListener {

	private Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	private Command gaugeCmd = new Command( Locale.get("cmd.SetCards.Gauge"), Command.ITEM, 15);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.ITEM, 16);
	public boolean[] flags = new boolean[Dominion.USER+1];
	private int tmp = 0;
	
	public QuickRandomizeList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		for ( tmp = 0 ; tmp < Dominion.I().getExpansions() ; tmp++ ) {
			if ( Dominion.I().numberOfCardsFromExp[tmp] > 0 ) {
				//#style label
				append(Dominion.getExpansionName(tmp) + " " + Dominion.I().numberOfCardsFromExp[tmp], Dominion.getExpansionImage(tmp));
			} else {
				//#style label
				append(Dominion.getExpansionName(tmp), Dominion.getExpansionImage(tmp));
			}
			setSelectedIndex(tmp, Dominion.I().playingExpansions[tmp]);
		}
		addCommand(quickRandomizeCardsCmd);
		addCommand(gaugeCmd);
		addCommand(quitCmd);
		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable screen) {
		if ( cmd.equals(quickRandomizeCardsCmd) ) {
			getSelectedFlags(flags);
			Dominion.I().setExpansionPlayingState(flags);
			GameApp.instance().ecFL.updateCards(false);
			if ( Dominion.CURRENT_SET == 0 ) {
				try {
					Dominion.I().randomizeCards();
					ShowCardsForm.instance().addNewCards(Dominion.I().getCurrentlySelected(Dominion.CURRENT_SET));
					GameApp.instance().changeToScreen(ShowCardsForm.instance());
				} catch (DominionException e) {
					GameApp.instance().showAlert(e.toString());
				}
			} else 
				GameApp.instance().changeToScreen(ShowCardsForm.instance());
		} else if ( cmd.equals(gaugeCmd) ) {
			tmp = UiAccess.getFocusedIndex(this);
			String tmpS = Dominion.getExpansionName(tmp);
			if ( tmp == Dominion.PROMO )
				GaugeForm.instance().setGauge(Locale.get("gauge.expansion.setCards", tmpS), true, Dominion.expansions[Dominion.PROMO].size(), 0);
			else
				GaugeForm.instance().setGauge(Locale.get("gauge.expansion.setCards", tmpS), true, Dominion.I().getNumberOfRandomCards(), 0);
			GaugeForm.instance().setGaugeValue(Dominion.I().numberOfCardsFromExp[tmp]);
			GaugeForm.instance().setCommandListener(this);
			GameApp.instance().changeToScreen(GaugeForm.instance());
			tmpS = null;
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok")) ) {
			GameApp.instance().changeToScreen(null);
			setCardsFromExpansion(tmp, GaugeForm.instance().getGaugeValue());
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
			setCardsFromExpansion(keyCode - Canvas.KEY_NUM0);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}

	private void setCardsFromExpansion(int numberCards) {
		//#debug dominizer
		System.out.println("trying to set cards from expansion");
		setCardsFromExpansion(UiAccess.getFocusedIndex(this), numberCards);
		
	}

	private void setCardsFromExpansion(int exp, int numberOfCards) {
		if ( -1 < exp & exp < Dominion.I().getExpansions() ) {
			if ( Dominion.I().expansions[exp].size() < numberOfCards )
				GameApp.instance().showAlert(Locale.get("alert.CardsFromExpansion.Promo"));
			else {
				if ( numberOfCards > 0 ) {
					//#style label
					set(exp, Dominion.getExpansionName(exp) + " " + numberOfCards, getImage(exp));
					Dominion.I().setExpansionPlayingState(exp, true);
				} else {
					//#style label
					set(exp, Dominion.getExpansionName(exp), getImage(exp));
				}
				setSelectedIndex(exp, Dominion.I().playingExpansions[exp]);
				Dominion.I().numberOfCardsFromExp[exp] = numberOfCards;
			}
			if ( exp == 0 )
				UiAccess.setFocusedIndex(this, 1);
			else 
				UiAccess.setFocusedIndex(this, 0);
			UiAccess.setFocusedIndex(this, exp);
		}
	}
}
