package com;

import java.util.Vector;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;

import com.dominizer.GameApp;
import com.util.Cards;
import com.util.Dominion;
import com.util.Rand;

import de.enough.polish.ui.ChoiceGroup;
import de.enough.polish.ui.FramedForm;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class BlackMarketForm extends FramedForm implements CommandListener {

	private Vector
	//#if polish.android
		//#= <String>
	//#endif
		blackMarketDeck = null;
	private Vector
	//#if polish.android
    	//#= <String>
	//#endif	
		boughtDeck = null;
	private Command cancelBuyCmd = new Command( Locale.get("cmd.BlackMarket.CancelBuy"), Command.SCREEN, 1);
	private Command drawCardsCmd = new Command( Locale.get("cmd.BlackMarket.Draw"), Command.BACK, 0);
	private Command selectCardCmd = new Command( Locale.get("polish.command.select"), Command.BACK, 1);
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.SCREEN, 3);
	private String randomizeCardHolder = null;
	private Item[] infoItem = null;
	private ChoiceGroup cardChoice = null;
		//private Ticker ticker = null;
		//private String[] tickerArgs = null;
	private int currentlyReachedCard = 0;
	
	/**
	 * @param title
	 */
	public BlackMarketForm(String title) {
		//#style behindScreen
		super(title);
		addCommand(backCmd);
		setCommandListener(this);
		infoItem = new Item[3];
		//TODO add styles to these items
		String tmp = "0";
		infoItem[0] = new StringItem(Locale.get("screen.BlackMarket.Cards.Bought", tmp), null);
		infoItem[1] = new StringItem(Locale.get("screen.BlackMarket.Cards.Left", tmp), null);
		infoItem[2] = new StringItem(Locale.get("screen.BlackMarket.DrawTextInfo"), null);
		append(Graphics.TOP, infoItem[0]);
		append(Graphics.TOP, infoItem[1]);
		append(Graphics.TOP, infoItem[2]);
		cardChoice = new ChoiceGroup(Locale.get("screen.BlackMarket.Cards.Select"), Choice.EXCLUSIVE);
		cardChoice.addCommand(selectCardCmd);
		append(Graphics.BOTTOM, cardChoice);
	}
	
	public void drawCards() {
		cardChoice.deleteAll();
		//#debug dominizer
		System.out.println("new drawing");
		//#style label
		CardItem cI = new CardItem(Locale.get("screen.BlackMarket.ChooseNone"), Choice.EXCLUSIVE);
		cI.setBothSides(true);
		cardChoice.add(cI);
		addNextCard(1);
		addNextCard(2);
		addNextCard(3);
		randomizeDrawn();
	}
		
	private void addNextCard(int number) {
		//#debug dominizer
		System.out.println("add next card. remaining: " + number);
		if ( blackMarketDeck.size() == 0 )
			cardChoice.deleteAll();
		else if ( blackMarketDeck.size() < number )
			return; // this check is what number is for, otherwise not needed!
		else if ( currentlyReachedCard < blackMarketDeck.size() ) {
			//#debug dominizer
			System.out.println("just added. remaining: " + number + " name "+blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard)).toString());
			int[] tmp = Dominion.I().getCardLocation(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard)).toString());
			if ( tmp[0] > -1 ) { 
				//#style label
				CardItem cI = new CardItem(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard)).toString(), Choice.IMPLICIT);
				cI.setBothSides(true);
				cI.setLeftImage(Dominion.expansions[tmp[0]].getCardTypeImage(tmp[1]));
				cI.setRightImage(Dominion.expansions[tmp[0]].getCostImage(tmp[1]));
				cardChoice.add(cI);
				currentlyReachedCard++;
			}
		} else if ( blackMarketDeck.size() <= currentlyReachedCard ) {
			//#debug dominizer
			System.out.println("size to large. remaining: " + number);
			currentlyReachedCard = 0;
			if ( number != 0 )
				addNextCard(0);
		}
	}
	
	private void selectCard() {
		if ( 0 < cardChoice.getSelectedIndex() ) {
			for ( int i = 0 ; i < blackMarketDeck.size() ; i++ ) {
				if ( cardChoice.getString(cardChoice.getSelectedIndex()).equals(blackMarketDeck.elementAt(i)) ) {
					GameApp.instance().showInfo(Locale.get("screen.BlackMarket.InfoMessage") + "\n" + blackMarketDeck.elementAt(i).toString() + ".", 2000);
					boughtDeck.addElement(blackMarketDeck.elementAt(i));
					blackMarketDeck.removeElementAt(i);
					i = blackMarketDeck.size() + 1;
				}
			}
		}
		cardChoice.deleteAll();
		if ( blackMarketDeck.size() != 0 )
			infoItem[2].setLabel(Locale.get("screen.BlackMarket.DrawTextInfo"));
		else
			infoItem[2].setLabel(Locale.get("screen.BlackMarket.DeckEmpty"));
		String tmp = "" + boughtDeck.size();
		infoItem[0].setLabel(Locale.get("screen.BlackMarket.Cards.Bought", tmp));
		tmp = "" + blackMarketDeck.size();
		infoItem[1].setLabel(Locale.get("screen.BlackMarket.Cards.Left", tmp));
	}
	
	private void cancelBuy(boolean show) {
		if ( show )
			GameApp.instance().showConfirmation(Locale.get("screen.BlackMarket.CancelBuy") + boughtDeck.lastElement(), this);
		else {
			if ( blackMarketDeck.size() == 0 ) {
				blackMarketDeck.addElement(boughtDeck.lastElement());
			} else
				blackMarketDeck.insertElementAt(boughtDeck.lastElement(), getIndexCard(currentlyReachedCard - 1));
			boughtDeck.removeElementAt(boughtDeck.size() - 1);
		}
	}
	
	private void randomizeDrawn() {
		randomizeDrawn(-1);
	}
	
	private void randomizeDrawn(int test) {
		if ( blackMarketDeck.size() == 0 )
			return;
		if ( test == -1 ) {
			Rand.resetSeed();
			test = Rand.randomInt(6);
		}
		switch ( test ) {
		case 0:
			// This means no switching when shuffling
			break;
		case 1:
			// This means swap of first and second card
			randomizeCardHolder = (String) blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3 + 1));
			blackMarketDeck.setElementAt(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3)), getIndexCard(currentlyReachedCard - 3 + 1));
			blackMarketDeck.setElementAt(randomizeCardHolder, getIndexCard(currentlyReachedCard - 3));
			break;
		case 2:
			// This means swap of first and third card
			randomizeCardHolder = (String) blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3)), getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(randomizeCardHolder, getIndexCard(currentlyReachedCard - 3));
			break;
		case 3:
			// This means swap of second and third card
			randomizeCardHolder = (String) blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3 + 1)), getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(randomizeCardHolder, getIndexCard(currentlyReachedCard - 3 + 1));
			break;
		case 4:
			// This means : Put third as first, the first as the second, and the second as third
			randomizeDrawn(3);
			randomizeDrawn(1);
			break;
		case 5:
			// This means : Put third as second, the first as the third, and the second as first
			randomizeDrawn(2);
			randomizeDrawn(1);
			break;
		}		
	}
	
	private int getIndexCard(int index) {
		if ( index < 0 ) {
			if ( blackMarketDeck.size() + index < 0 )
				return 0;
			return blackMarketDeck.size() + index;
		} else if ( index < blackMarketDeck.size() )
			return index;
		else
			return getIndexCard(index - blackMarketDeck.size());
	}
	
	public void setBlackMarketDeck(Cards blackMarketDeck) {
		cardChoice.deleteAll();
		boughtDeck = new Vector
		//#if polish.android
			//#= <String>
		//#endif
			(blackMarketDeck.size());
		this.blackMarketDeck = new Vector
		//#if polish.android
			//#= <String>
		//#endif
			(blackMarketDeck.size());
		for ( int i = 0 ; i < blackMarketDeck.size() ; i++ )
			this.blackMarketDeck.addElement(blackMarketDeck.getName(i));
		currentlyReachedCard = 0;
		String tmp = "" + boughtDeck.size();
		infoItem[0].setLabel(Locale.get("screen.BlackMarket.Cards.Bought", tmp));
		tmp = "" + blackMarketDeck.size();
		infoItem[1].setLabel(Locale.get("screen.BlackMarket.Cards.Left", tmp));
		infoItem[2].setLabel(Locale.get("screen.BlackMarket.DrawTextInfo"));
		updateCommands();
	}
	
	private void updateCommands() {
		if ( 0 < size() ) {
			//#debug dominizer
			System.out.println("adding commands for selecting");
			removeCommand(drawCardsCmd);
			removeCommand(cancelBuyCmd);
			cardChoice.addCommand(selectCardCmd);
		} else {
			//#debug dominizer
			System.out.println("adding commands for drawing");
			removeCommand(drawCardsCmd);
			cardChoice.removeCommand(selectCardCmd);
			if ( 0 < blackMarketDeck.size() ) {
				//#debug dominizer
				System.out.println("adding commands for drawing2");
				addCommand(drawCardsCmd);
				// setSelectCommand(drawCardsCmd);
			} else {
				removeCommand(drawCardsCmd);
			}
			if ( 0 < boughtDeck.size() ) {
				addCommand(cancelBuyCmd);
			} else {
				removeCommand(cancelBuyCmd);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command cmd, Displayable screen) {
		String tmp = "" + boughtDeck.size();
		if ( cmd.equals(drawCardsCmd) ) {
			drawCards();
			infoItem[0].setLabel(Locale.get("screen.BlackMarket.Cards.Bought", tmp));
			tmp = "" + blackMarketDeck.size();
			infoItem[1].setLabel(Locale.get("screen.BlackMarket.Cards.Left", tmp));
			infoItem[2].setLabel("");
		} else if ( cmd.equals(selectCardCmd) ) {
			selectCard();
		} else if ( cmd.equals(cancelBuyCmd) ) {
			cancelBuy(true);
		} else if ( cmd.equals(backCmd) ) {
			GameApp.instance().returnToPreviousScreen();
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok")) ) {
			cancelBuy(false);
			GameApp.instance().changeToScreen(this);
			infoItem[0].setLabel(Locale.get("screen.BlackMarket.Cards.Bought", tmp));
			tmp = "" + blackMarketDeck.size();
			infoItem[1].setLabel(Locale.get("screen.BlackMarket.Cards.Left", tmp));
			infoItem[2].setLabel(Locale.get("screen.BlackMarket.DrawTextInfo"));
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel")) ) {
			GameApp.instance().changeToScreen(this);
			infoItem[0].setLabel(Locale.get("screen.BlackMarket.Cards.Bought", tmp));
			tmp = "" + blackMarketDeck.size();
			infoItem[1].setLabel(Locale.get("screen.BlackMarket.Cards.Left", tmp));
			infoItem[2].setLabel(Locale.get("screen.BlackMarket.DrawTextInfo"));
		} 
		updateCommands();
	}
}
