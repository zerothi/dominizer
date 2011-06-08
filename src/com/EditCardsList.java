package com;


import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.dominizer.GameApp;
import com.util.Dominion;
import com.util.DominionException;

import de.enough.polish.ui.Alert;
import de.enough.polish.ui.Choice;
import de.enough.polish.ui.ChoiceItem;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.ItemStateListener;
import de.enough.polish.ui.List;
import de.enough.polish.util.Locale;

public class EditCardsList extends List implements CommandListener, ItemStateListener {
	
	private Command randomizeCmd = new Command(Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	private Command perGaugeCmd = new Command( Locale.get("cmd.Percentage.Gauge"), Command.ITEM, 7);
	private Command optionCmd = new Command( Locale.get("cmd.Options.Main"), Command.ITEM, 12);
	private Command gotoCmd = new Command( Locale.get("cmd.Goto.RandomizeSets"), Command.ITEM, 9);
	
	//#if debugDominizer
		private Command showInfoCmd = new Command("INFO", Command.ITEM, 10);
	//#endif
	
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.ITEM, 11);
	private int[] tmp = new int[] { 0, 0};
	private boolean isLoaded = false;
	
	
	public EditCardsList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		isLoaded = false;
		addCommand(randomizeCmd);
		addCommand(perGaugeCmd);
		//addCommand(optionCmd);
		addCommand(quitCmd);
		addCommand(gotoCmd);
		//#if debugDominizer
			addCommand(showInfoCmd);
		//#endif
		setCommandListener(this);
		setItemStateListener(this);
	}
	
	public void loadCards() {
		if ( isLoaded )
			return;
		//#debug dominizer
		System.out.println("we are loading the cards now. Initializing the loading gauge");
		isLoaded = true;
		int cardNumber;
		GaugeForm.instance().setGauge("Loading: ", false, Dominion.expansions.length, 0);
		GameApp.instance().changeToScreen(GaugeForm.instance());
		for ( int i = 0 ; i < Dominion.I().getExpansions() ; i++ ){
			GaugeForm.instance().setGaugeValue(i + 1); // Locale.get("gauge.loading.expansion")
			GaugeForm.instance().setGaugeLabel("Loading: " + Dominion.getExpansionName(i));
			for ( cardNumber = 0 ; cardNumber < Dominion.expansions[i].size() ; cardNumber++ ) {
				appendCard(Dominion.expansions[i].getName(cardNumber),
						Dominion.expansions[i].getCardTypeImage(cardNumber),
						Dominion.expansions[i].getCostImage(cardNumber));
				setSelectedIndex(size() - 1, Dominion.expansions[i].isAvailable(cardNumber));
				setPercentage(size() - 1, i, cardNumber, Dominion.expansions[i].getPercentage(cardNumber));
			}
		}
		GameApp.instance().changeToScreen(null);
		focus(0);
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
			if ( tmp >= Dominion.TOTAL_CARDS )
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
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(randomizeCmd) ) {
			try {
				Dominion.I().randomizeCards();
				CardsList.instance().setCards(Dominion.I().getSelectedCards(Dominion.I().getCurrentSet()), Dominion.I().getCurrentSet());
				GameApp.instance().changeToScreen(CardsList.instance());
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(gotoCmd) ) {
			if ( Dominion.I().getCurrentSet() > 0 )
				GameApp.instance().changeToScreen(CardsList.instance());
			else
				GameApp.instance().showInfo(Locale.get("info.randomized.Sets.NoneCreated"), Alert.FOREVER);
		} else if ( cmd.equals(perGaugeCmd) ) {
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
		} else if ( cmd.equals(this.optionCmd) ) {
			GameApp.instance().changeToScreen(new OptionForm(""));
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel")) ) {
			GameApp.instance().changeToScreen(null);
		} else if ( cmd.equals(quitCmd) ) {
			GameApp.instance().quit();
		}
		//#if debugDominizer
			else if ( cmd.equals(showInfoCmd) ) {
				GameApp.instance().showInfo("Current set: " + Dominion.I().getCurrentSet(), Alert.FOREVER);
			}
		//#endif
	}
	
	public void setPercentage(int index, int exp, int card, int deciPercentage) {
		if ( deciPercentage > 0 ) {
			Dominion.expansions[exp].setPercentage(card, deciPercentage);
		} else {
			Dominion.expansions[exp].setPercentage(card, 0);
		}
		set(index, getCardItem(Dominion.expansions[exp].getName(card) + ( deciPercentage > 0 ? " " + deciPercentage * 10 + "%" : ""), 
				Dominion.expansions[exp].getCardTypeImage(card), 
				Dominion.expansions[exp].getCostImage(card)));
		setSelectedIndex(index, Dominion.expansions[exp].isAvailable(card));
		if ( index > 0 )
			focus(index - 1);
		if ( size() > index + 1)
			focus(index + 1);
		focus(index);
	}
	
	private ChoiceItem getCardItem(String cardName, Image expImage, Image costImage) {
		//#style label
		CardItem ci = new CardItem(cardName, expImage, costImage, Choice.MULTIPLE);
		ci.setBothSides(true);
		return ci;
	}
	
	private void appendCard(String cardName, Image expImage, Image costImage) {
		append(getCardItem(cardName, expImage, costImage));
	}
	
	public void updateCards(int specific) {
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
	
	private void changeCard(int index, boolean isAvailable) {
		if ( index < 0 ) 
			return;
		setSelectedIndex(index, isAvailable);
		Dominion.expansions[Dominion.I().getLinearExpansionIndex(index)].setAvailable(
				Dominion.I().getLinearCardIndex(index), isAvailable);
		Dominion.expansions[Dominion.I().getLinearExpansionIndex(index)].setBlackMarketAvailable(
				Dominion.I().getLinearCardIndex(index), isAvailable);
	}
//#if polish.android
	//#= @Override
//#endif
	public void itemStateChanged(Item it) {
		changeCard(getCurrentIndex(), isSelected(getCurrentIndex()));
	}
}
