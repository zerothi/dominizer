package com;



import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.dominizer.GameApp;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.Locale;

public class EditCardsFilteredList extends FilteredList implements CommandListener {
	
	private Command randomizeCmd = new Command(Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	private Command perGaugeCmd = new Command( Locale.get("cmd.Percentage.Gauge"), Command.SCREEN, 1);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 10);
	private int tmp;
	
	public EditCardsFilteredList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		for ( int i = 0 ; i < Dominion.I().getExpansions() ; i++ ){
			for ( int cardNumber = 0 ; cardNumber < Dominion.I().getExpansion(i).size() ; cardNumber++ ) {
				//#debug info
				System.out.println("exp: " + Dominion.I().getExpansion(i).getExpansion(cardNumber));
				//#style label
				append(Dominion.I().getExpansion(i).getName(cardNumber), Dominion.I().getCardTypeImage(i, cardNumber));
				setSelectedIndex(size() - 1, Dominion.I().getExpansion(i).isAvailable(cardNumber));
				setPercentage(size() - 1, i, cardNumber, Dominion.I().getExpansion(i).getPercentage(cardNumber));
			}
		}
		addCommand(randomizeCmd);
		addCommand(perGaugeCmd);
		addCommand(quitCmd);
		setCommandListener(this);
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_STAR:
			if (getCurrentIndex() + 10 < size() )
				focus(getCurrentIndex() + 10);
			else
				focus(0);
			break;
		case Canvas.KEY_POUND:
			tmp = 0;
			switch ( Dominion.I().getLinearExpansionIndex(getCurrentIndex()) ) {
			case 3:	tmp = 0; break;
			case 2:	tmp += Dominion.I().getExpansion(2).size();
			case 1:	tmp += Dominion.I().getExpansion(1).size();
			case 0:	tmp += Dominion.I().getExpansion(0).size();
			}
			focus(tmp);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(randomizeCmd) ) {
			//#debug info
			System.out.println("I AM TRULY CALLED");
			updateCards(true);
			GameApp.instance().showRandomizedCards();
		} else if ( cmd.equals(perGaugeCmd) ) {
			tmp = getCurrentIndex();
			String tmpS = Dominion.I().getExpansion(
					Dominion.I().getLinearExpansionIndex(tmp)).getName(Dominion.I().getLinearCardIndex(tmp));
			GaugeForm.instance().setGauge(Locale.get("gauge.card.percentage", tmpS), true, 10, 0);
			GaugeForm.instance().setGaugeValue(
					Dominion.I().getExpansion(Dominion.I().getLinearExpansionIndex(tmp)).getPercentage(
							Dominion.I().getLinearCardIndex(tmp)));
			GaugeForm.instance().setCommandListener(this);
			GameApp.instance().changeToScreen(GaugeForm.instance());
			tmpS = null;
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok")) ) {
			GameApp.instance().changeToScreen(null);
			setPercentage(tmp, Dominion.I().getLinearExpansionIndex(tmp),
					Dominion.I().getLinearCardIndex(tmp), GaugeForm.instance().getGaugeValue());
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel")) ) {
			GameApp.instance().changeToScreen(null);
		} else if ( cmd.equals(quitCmd) ) {
			GameApp.instance().quit();
		}
	}
	
	public void setPercentage(int index, int exp, int card, int deciPercentage) {
		if ( deciPercentage > 0 ) {
			//#style label
			set(index, Dominion.I().getExpansion(exp).getName(card) + " " + deciPercentage * 10 + "%", getImage(index));
			Dominion.I().getExpansion(exp).setPercentage(card, deciPercentage);
		} else {
			//#style label
			set(index, Dominion.I().getExpansion(exp).getName(card), getImage(index));
			Dominion.I().getExpansion(exp).setPercentage(card, 0);
		}
		if ( index == 0 && size() > 1 )
			UiAccess.setFocusedIndex(this, 1);
		else 
			UiAccess.setFocusedIndex(this, 0);
		UiAccess.setFocusedIndex(this, index);
	}
	
	public void setPercentage(int exp, int card, int deciPercentage) {
		setPercentage(getCurrentIndex(), exp, card, deciPercentage);
	}
	
	public void updateCards(boolean localUpdate) {
		if ( localUpdate ) {
			for ( int i = 0 ; i < size() ; i++ ) {
				changeCard(i, getItem(i).isSelected, false);
			}
		} else {
			for ( int i = 0 ; i < size() ; i++ ) {
				/*///#debug info
				System.out.println("redoing: " + Dominion.I().getExpansion(
						Dominion.I().getLinearExpansionIndex(i)).isAvailable(
						Dominion.I().getLinearCardIndex(i)));*/
				changeCard(i, Dominion.I().getExpansion(
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
			setSelectedIndex(index, !Dominion.I().getExpansion(
				Dominion.I().getLinearExpansionIndex(getCurrentIndex())).isAvailable(
						Dominion.I().getLinearCardIndex(getCurrentIndex())));
		} else {
			setSelectedIndex(index, isAvailable);
		}
		Dominion.I().getExpansion(Dominion.I().getLinearExpansionIndex(index)).setAvailable(
				Dominion.I().getLinearCardIndex(index), getItem(index).isSelected);
		Dominion.I().getExpansion(Dominion.I().getLinearExpansionIndex(index)).setBlackMarketAvailable(
				Dominion.I().getLinearCardIndex(index), getItem(index).isSelected);
	}
}
