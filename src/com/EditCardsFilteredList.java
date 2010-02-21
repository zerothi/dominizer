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
	
	private Command randomizeCmd = new Command(Locale.get("cmd.Randomize.Show"), Command.OK, 0);
	private Command perGaugeCmd = new Command( Locale.get("cmd.Percentage.Gauge"), Command.SCREEN, 1);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 10);
	private int[] tmp = new int[] { 0, 0};
	
	public EditCardsFilteredList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		for ( int i = 0 ; i < Dominion.I().getExpansions() ; i++ ){
			for ( int cardNumber = 0 ; cardNumber < Dominion.I().getExpansion(i).size() ; cardNumber++ ) {
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
			tmp[0] = 0;
			switch ( Dominion.I().getLinearExpansionIndex(getCurrentIndex()) ) {
			case 3:	tmp[0] = 0; break;
			case 2:	tmp[0] += Dominion.I().getExpansion(2).size();
			case 1:	tmp[0] += Dominion.I().getExpansion(1).size();
			case 0:	tmp[0] += Dominion.I().getExpansion(0).size();
			}
			focus(tmp[0]);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(randomizeCmd) ) {
			updateCards(true);
			GameApp.instance().showRandomizedCards();
		} else if ( cmd.equals(perGaugeCmd) ) {
			if ( getFilterText().length() != 0 ) {
				GameApp.instance().showAlert(Locale.get("alert.Filter.Availability"));
				return;
			}
			tmp[0] = Dominion.I().getLinearExpansionIndex(getCurrentIndex());
			tmp[1] = Dominion.I().getLinearCardIndex(getCurrentIndex());
			if ( tmp[0] == -1 ) return;
			String tmpS = Dominion.I().getExpansion(tmp[0]).getName(tmp[1]);
			GaugeForm.instance().setGauge(Locale.get("gauge.card.percentage", tmpS), true, 10, 0);
			GaugeForm.instance().setGaugeValue(Dominion.I().getExpansion(tmp[0]).getPercentage(tmp[1]));
			GaugeForm.instance().setCommandListener(this);
			GameApp.instance().changeToScreen(GaugeForm.instance());
			tmpS = null;
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok")) ) {
			GameApp.instance().changeToScreen(null);
			setPercentage(getCurrentIndex(), tmp[0], tmp[1], GaugeForm.instance().getGaugeValue());
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
	
	public void updateCards(boolean localUpdate) {
		if ( localUpdate ) {
			for ( int i = 0 ; i < size() ; i++ ) {
				changeCard(i, getItem(i).isSelected, false);
			}
		} else {
			for ( int i = 0 ; i < size() ; i++ ) {
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
