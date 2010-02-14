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
				this.append(Dominion.I().getExpansion(i).getName(cardNumber), Dominion.I().getCardTypeImage(i, cardNumber));
				this.setSelectedIndex(this.size() - 1, Dominion.I().getExpansion(i).isAvailable(cardNumber));
				this.setPercentage(this.size() - 1, i, cardNumber, Dominion.I().getExpansion(i).getPercentage(cardNumber));
			}
		}
		this.addCommand(randomizeCmd);
		this.addCommand(perGaugeCmd);
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
			System.out.println("I AM TRULY CALLED");
			updateCards(true);
			GameApp.instance().showRandomizedCards();
		} else if ( cmd.equals(perGaugeCmd) ) {
			tmp = this.getCurrentIndex();
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
			this.setPercentage(tmp, Dominion.I().getLinearExpansionIndex(tmp),
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
			this.set(index, Dominion.I().getExpansion(exp).getName(card) + " " + deciPercentage * 10 + "%", this.getImage(index));
			Dominion.I().getExpansion(exp).setPercentage(card, deciPercentage);
		} else {
			//#style label
			this.set(index, Dominion.I().getExpansion(exp).getName(card), this.getImage(index));
			Dominion.I().getExpansion(exp).setPercentage(card, 0);
		}
		if ( index == 0 && this.size() > 1 )
			UiAccess.setFocusedIndex(this, 1);
		else 
			UiAccess.setFocusedIndex(this, 0);
		UiAccess.setFocusedIndex(this, index);
	}
	
	public void setPercentage(int exp, int card, int deciPercentage) {
		this.setPercentage(this.getCurrentIndex(), exp, card, deciPercentage);
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
		Dominion.I().getExpansion(Dominion.I().getLinearExpansionIndex(index)).setBlackMarketAvailable(
				Dominion.I().getLinearCardIndex(index), this.getItem(index).isSelected);
	}
}
