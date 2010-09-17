package com;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Ticker;

import com.dominizer.GameApp;
import com.util.Cards;
import com.util.Dominion;
import com.util.Rand;

import de.enough.polish.ui.List;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class BlackMarketForm extends List implements CommandListener {

	private Vector
	//#if polish.android
		//#= <String>
	//#endif
		blackMarketDeck = null;
	private Vector
	//#if polish.android
    	//#= <String>
	//#endif	
		drawnDeck = null;
	private Command cancelBuyCmd = new Command( Locale.get("cmd.BlackMarket.CancelBuy"), Command.SCREEN, 1);
	private Command drawCardsCmd = new Command( Locale.get("cmd.BlackMarket.Draw"), Command.BACK, 0);
	private Command selectCardCmd = new Command( Locale.get("polish.command.select"), Command.BACK, 1);
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.SCREEN, 3);
	private String randomizeCardHolder = null;
		private Ticker ticker = null;
		private String[] tickerArgs = null;
	private int currentlyReachedCard = 0;
	
	/**
	 * @param title
	 */
	public BlackMarketForm(String title) {
		//#style behindScreen
		super(title, List.IMPLICIT);
		addCommand(backCmd);
		setCommandListener(this);
		tickerArgs = new String[3];
		tickerArgs[0] = Locale.get("screen.BlackMarket.DrawTextInfo");
		tickerArgs[1] = "0";
		tickerArgs[2] = "0";
		//#style mainTicker
		ticker = new Ticker(Locale.get("screen.BlackMarket.Ticker", tickerArgs));
		setTicker(ticker);
	}
	
	public void drawCards() {
		deleteAll();
		//#debug dominizer
		System.out.println("new drawing");
		//#style label
		CardItem cI = new CardItem(Locale.get("screen.BlackMarket.ChooseNone"), List.IMPLICIT);
		cI.setBothSides(true);
		append(cI);
		addNextCard(1);
		addNextCard(2);
		addNextCard(3);
		randomizeDrawn();
	}
		
	private void addNextCard(int number) {
		//#debug dominizer
		System.out.println("add next card. remaining: " + number);
		if ( blackMarketDeck.size() == 0 )
			deleteAll();
		else if ( blackMarketDeck.size() < number )
			return;
		else if ( currentlyReachedCard < blackMarketDeck.size() ) {
			//#debug dominizer
			System.out.println("just added. remaining: " + number + " name "+blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard)).toString());
			int[] tmp = Dominion.I().getCardLocation(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard)).toString());
			if ( tmp[0] > -1 ) { 
				//#style label
				CardItem cI = new CardItem(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard)).toString(), List.IMPLICIT);
				cI.setBothSides(true);
				cI.setLeftImage(Dominion.expansions[tmp[0]].getCardTypeImage(tmp[1]));
				cI.setRightImage(Dominion.expansions[tmp[0]].getCostImage(tmp[1]));
				append(cI);
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
		if ( 0 < getSelectedIndex() ) {
			for ( int i = 0 ; i < blackMarketDeck.size() ; i++ ) {
				if ( getString(getSelectedIndex()).equals(blackMarketDeck.elementAt(i)) ) {
					GameApp.instance().showInfo(Locale.get("screen.BlackMarket.InfoMessage") + "\n" + blackMarketDeck.elementAt(i).toString() + ".", 2000);
					drawnDeck.addElement(blackMarketDeck.elementAt(i));
					blackMarketDeck.removeElementAt(i);
					i = blackMarketDeck.size() + 1;
				}
			}
		}
		deleteAll();
		if ( blackMarketDeck.size() != 0 )
			tickerArgs[0] = Locale.get("screen.BlackMarket.DrawTextInfo");
		else
			tickerArgs[0] = Locale.get("screen.BlackMarket.DeckEmpty");
		tickerArgs[1] = "" + drawnDeck.size();
		tickerArgs[2] = "" + blackMarketDeck.size();
		ticker.setString(Locale.get("screen.BlackMarket.Ticker", tickerArgs));
	}
	
	private void cancelBuy(boolean show) {
		if ( show )
			GameApp.instance().showConfirmation(Locale.get("screen.BlackMarket.CancelBuy") + drawnDeck.lastElement(), this);
		else {
			if ( blackMarketDeck.size() == 0 ) {
				blackMarketDeck.addElement(drawnDeck.lastElement());
			} else
				blackMarketDeck.insertElementAt(drawnDeck.lastElement(), getIndexCard(currentlyReachedCard - 1));
			drawnDeck.removeElementAt(drawnDeck.size() - 1);
		}
	}
	
	private void randomizeDrawn() {
		if ( blackMarketDeck.size() == 0 )
			return;
		Rand.resetSeed();
		switch ( Rand.randomInt(6) ) {
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
			randomizeCardHolder = (String) blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3)), getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(randomizeCardHolder, getIndexCard(currentlyReachedCard - 3));
			randomizeCardHolder = (String) blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3 + 1)), getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(randomizeCardHolder, getIndexCard(currentlyReachedCard - 3 + 1));
			break;
		case 5:
			// This means : Put third as second, the first as the third, and the second as first
			randomizeCardHolder = (String) blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3 + 1)), getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(randomizeCardHolder, getIndexCard(currentlyReachedCard - 3 + 1));
			randomizeCardHolder = (String) blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(blackMarketDeck.elementAt(getIndexCard(currentlyReachedCard - 3)), getIndexCard(currentlyReachedCard - 3 + 2));
			blackMarketDeck.setElementAt(randomizeCardHolder, getIndexCard(currentlyReachedCard - 3));
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
		deleteAll();
		drawnDeck = new Vector
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
		tickerArgs[0] = Locale.get("screen.BlackMarket.DrawTextInfo");
		tickerArgs[1] = "" + drawnDeck.size();
		tickerArgs[2] = "" + blackMarketDeck.size();
		ticker.setString(Locale.get("screen.BlackMarket.Ticker", tickerArgs));
		updateCommands();
	}
	
	private void updateCommands() {
		if ( 0 < size() ) {
			//#debug dominizer
			System.out.println("adding commands for selecting");
			removeCommand(drawCardsCmd);
			removeCommand(cancelBuyCmd);
			addCommand(selectCardCmd);
			//#= setSelectCommand(selectCardCmd);
		} else {
			//#debug dominizer
			System.out.println("adding commands for drawing");
			removeCommand(drawCardsCmd);
			removeCommand(selectCardCmd);
			if ( 0 < blackMarketDeck.size() ) {
				//#debug dominizer
				System.out.println("adding commands for drawing2");
				addCommand(drawCardsCmd);
				//#= setSelectCommand(drawCardsCmd);
			} else {
				removeCommand(drawCardsCmd);
			}
			if ( 0 < drawnDeck.size() ) {
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
		if ( cmd.equals(drawCardsCmd) ) {
			drawCards();
			tickerArgs[0] = "";
			tickerArgs[1] = "" + drawnDeck.size();
			tickerArgs[2] = "" + blackMarketDeck.size();
			ticker.setString(Locale.get("screen.BlackMarket.Ticker", tickerArgs));
		} else if ( cmd.equals(selectCardCmd) ) {
			selectCard();
		} else if ( cmd.equals(cancelBuyCmd) ) {
			cancelBuy(true);
		} else if ( cmd.equals(backCmd) ) {
			GameApp.instance().returnToPreviousScreen();
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok")) ) {
			cancelBuy(false);
			GameApp.instance().changeToScreen(this);
			tickerArgs[0] = Locale.get("screen.BlackMarket.DrawTextInfo");
			tickerArgs[1] = "" + drawnDeck.size();
			tickerArgs[2] = "" + blackMarketDeck.size();
			ticker.setString(Locale.get("screen.BlackMarket.Ticker", tickerArgs));
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel")) ) {
			GameApp.instance().changeToScreen(this);
			tickerArgs[0] = Locale.get("screen.BlackMarket.DrawTextInfo");
			tickerArgs[1] = "" + drawnDeck.size();
			tickerArgs[2] = "" + blackMarketDeck.size();
			ticker.setString(Locale.get("screen.BlackMarket.Ticker", tickerArgs));
		} 
		updateCommands();
	}
}
