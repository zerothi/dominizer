package com;



import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.dominizer.GameApp;
import com.util.Dominion;
import com.util.DominionException;

import de.enough.polish.ui.List;
import de.enough.polish.util.Locale;

public class EditCardsList extends List implements CommandListener {
	
	private Command randomizeCmd = new Command(Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	private Command perGaugeCmd = new Command( Locale.get("cmd.Percentage.Gauge"), Command.SCREEN, 7);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 11);
	private int[] tmp = new int[] { 0, 0};
	
	public EditCardsList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		for ( int i = 0 ; i < Dominion.I().getExpansions() ; i++ ){
			for ( int cardNumber = 0 ; cardNumber < Dominion.expansions[i].size() ; cardNumber++ ) {
				appendCard(Dominion.expansions[i].getName(cardNumber),
						Dominion.expansions[i].getCardTypeImage(cardNumber),
						Dominion.expansions[i].getCostImage(cardNumber));
				/*//#style label
				append(Dominion.I().getExpansion(i).getName(cardNumber), Dominion.I().getExpansion(i).getCardTypeImage(cardNumber));*/
				setSelectedIndex(size() - 1, Dominion.expansions[i].isAvailable(cardNumber));
				setPercentage(size() - 1, i, cardNumber, Dominion.expansions[i].getPercentage(cardNumber));
			}
		}
		focus(0);
		addCommand(randomizeCmd);
		addCommand(perGaugeCmd);
		addCommand(quitCmd);
		setCommandListener(this);
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_STAR:
			if (getCurrentIndex() + 8 < size() )
				focus(getCurrentIndex() + 8);
			else
				focus(0);
			break;
		case Canvas.KEY_POUND:
			int tmp = 0;
			for ( int i = 0 ; i <= Dominion.I().getLinearExpansionIndex(getCurrentIndex()) ; i++ ) {
				tmp += Dominion.expansions[i].size();
			}
			if ( tmp == Dominion.TOTAL_CARDS )
				tmp = 0;
			focus(tmp);
			break;
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
			setPercentage(getCurrentIndex(), Dominion.I().getLinearExpansionIndex(getCurrentIndex()),
					Dominion.I().getLinearCardIndex(getCurrentIndex()), keyCode - Canvas.KEY_NUM0);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
		updateCards(true, getCurrentIndex());
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(randomizeCmd) ) {
			updateCards(true);
			try {
				Dominion.I().randomizeCards();
				ShowCardsForm.instance().addNewCards(Dominion.I().getCurrentlySelected(Dominion.CURRENT_SET));
				GameApp.instance().changeToScreen(ShowCardsForm.instance());
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
			
		} else if ( cmd.equals(perGaugeCmd) ) {
			/*if ( getFilterText().length() != 0 ) {
				GameApp.instance().showAlert(Locale.get("alert.Filter.Availability"));
				return;
			}*/
			tmp[0] = Dominion.I().getLinearExpansionIndex(getCurrentIndex());
			tmp[1] = Dominion.I().getLinearCardIndex(getCurrentIndex());
			if ( tmp[0] == -1 ) return;
			String tmpS = Dominion.expansions[tmp[0]].getName(tmp[1]);
			GaugeForm.instance().setGauge(Locale.get("gauge.card.percentage", tmpS), true, 10, 0);
			GaugeForm.instance().setGaugeValue(Dominion.expansions[tmp[0]].getPercentage(tmp[1]));
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
		CardItem ci = null;
		if ( deciPercentage > 0 ) {
			//#style label
			ci = new CardItem(Dominion.expansions[exp].getName(card) + " " + deciPercentage * 10 + "%", List.MULTIPLE);
			//set(index, Dominion.expansions[exp].getName(card) + " " + deciPercentage * 10 + "%", getImage(index));
			Dominion.expansions[exp].setPercentage(card, deciPercentage);
		} else {
			//#style label
			ci = new CardItem(Dominion.expansions[exp].getName(card), List.MULTIPLE);
			Dominion.expansions[exp].setPercentage(card, 0);
		}
		ci.setLeftImage(Dominion.expansions[exp].getCardTypeImage(card));
		ci.setRightImage(Dominion.expansions[exp].getCostImage(card));
		set(index, ci);
		setSelectedIndex(index, Dominion.expansions[exp].isAvailable(card));
		if ( index > 0 )
			focus(index - 1);
		if ( size() > index + 1)
			focus(index + 1);
		focus(index);
	}
	
	public void updateCards(boolean localUpdate) {
		updateCards(localUpdate, -1);
	}
	
	private void appendCard(String cardName, Image expImage, Image costImage) {
		//#style label
		CardItem ci = new CardItem(cardName, List.MULTIPLE);
		ci.setLeftImage(expImage);
		ci.setRightImage(costImage);
		append(ci);
	}
	
	public void updateCards(boolean localUpdate, int specific) {
		if ( localUpdate ) {
			if ( specific == -1 ) {
				for ( int i = 0 ; i < size() ; i++ ) {
					changeCard(i, getItem(i).isSelected);
				}
			} else {
				changeCard(specific, getItem(specific).isSelected);
			}
		} else {
			if ( specific == -1 ) {
				for ( int i = 0 ; i < size() ; i++ ) {
					/*
					//#debug dominizer
					System.out.println("changing: " + i + " to state: " + Dominion.expansions[
							Dominion.I().getLinearExpansionIndex(i)].isAvailable(
							Dominion.I().getLinearCardIndex(i)));
							*/
					changeCard(i, Dominion.expansions[Dominion.I().getLinearExpansionIndex(i)]
					                                      .isAvailable(Dominion.I().getLinearCardIndex(i)));
				}
			} else {
				changeCard(specific, Dominion.expansions[Dominion.I().getLinearExpansionIndex(specific)]
				                                             .isAvailable(Dominion.I().getLinearCardIndex(specific)));
			}
		}
	}
	
	private void changeCard(int index, boolean isAvailable) {
		if ( index < 0 ) 
			return;
		setSelectedIndex(index, isAvailable);
		Dominion.expansions[Dominion.I().getLinearExpansionIndex(index)].setAvailable(
				Dominion.I().getLinearCardIndex(index), isAvailable);
		Dominion.expansions[Dominion.I().getLinearExpansionIndex(index)].setBlackMarketAvailable(
				Dominion.I().getLinearCardIndex(index), isAvailable);
	}
}
