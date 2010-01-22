package com;

import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Ticker;

import com.dominizer.GameApp;

import de.enough.polish.util.Locale;

/**
 * 
 */

/**
 * @author nick
 *
 */
public class BlackMarketForm extends List implements CommandListener {

	private Vector blackMarketDeck = null;
	private Vector drawnDeck = null;
	//private ChoiceGroup chooseCard = null;
	private Command cancelBuyCmd = new Command( Locale.get("cmd.BlackMarket.CancelBuy"), Command.SCREEN, 1);
	private Command drawCardsCmd = new Command( Locale.get("cmd.BlackMarket.Draw"), Command.OK, 0);
	private Command selectCardCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 2);
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.BACK, 0);
	private String randomizeCardHolder = null;
	private Ticker ticker = null;
	private String[] tickerArgs = null;
	int currentlyReachedCard = 0;
	int randomize = 0;
	
	/**
	 * @param title
	 */
	public BlackMarketForm(String title, int listMethod) {
		//#style behindScreen
		super(title, listMethod);
		///#style filterCards
		/*this.chooseCard = new ChoiceGroup(Locale.get("screen.BlackMarket.ChooseCards"), ChoiceGroup.EXCLUSIVE);
		this.chooseCard.addCommand(this.selectCardCmd);
		this.chooseCard.setItemCommandListener(this);*/
		//this.addCommand(this.selectCardCmd);
		this.addCommand(this.drawCardsCmd);
		this.setSelectCommand(this.drawCardsCmd);
		//this.setDefaultCommand(this.drawCardsCmd);
		this.addCommand(this.backCmd);
		this.setCommandListener(this);
		tickerArgs = new String[3];
		tickerArgs[0] = Locale.get("screen.BlackMarket.DrawTextInfo");
		tickerArgs[1] = "0";
		tickerArgs[2] = "0";
		//#style mainTicker
		ticker = new Ticker(Locale.get("screen.BlackMarket.Ticker", tickerArgs));
		this.setTicker(ticker);
	}
	
	public void drawCards() {
		//this.deleteAll();
		this.removeCommand(this.drawCardsCmd);
		this.removeCommand(this.cancelBuyCmd);
		//#debug info
		System.out.println("new drawing");
		//this.append(this.chooseCard);
		//this.chooseCard.removeCommand(this.drawCardsCmd);
		//style choiceItem
		this.append(Locale.get("screen.BlackMarket.ChooseNone"), null);
		this.addNextCard(3);
		if ( 1 < this.blackMarketDeck.size() )
			this.addNextCard(2);
		if ( 2 < this.blackMarketDeck.size() )
			this.addNextCard(1);
		this.randomizeDrawn();
		this.setSelectedIndex(0, true);
		//this.chooseCard.setSelectedIndex(0, true);
	}
		
	private void addNextCard(int remaining) {
		//#debug info
		System.out.println("add next card. remaining: " + remaining);
		if ( this.blackMarketDeck.size() == 0 ) {
			//style choiceItem
			//this.chooseCard.deleteAll();
			this.deleteAll();
		} else if ( this.blackMarketDeck.size() <= this.currentlyReachedCard ) {
			//#debug info
			System.out.println("size to large. remaining: " + remaining);
			this.currentlyReachedCard = 0;
			if ( remaining != 0 )
				addNextCard(0);
		} else if ( this.currentlyReachedCard < this.blackMarketDeck.size() ) {
			//#debug info
			System.out.println("just added. remaining: " + remaining);
			//style choiceItem
			//this.chooseCard.append(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard)).toString(), null);
			this.append(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard)).toString(), null);
			this.currentlyReachedCard++;
		}
	}
	
	private void selectCard() {
		if ( 0 < this.getSelectedIndex() ) {
			for ( int i = 0 ; i < blackMarketDeck.size() ; i++ ) {
				if ( this.getString(this.getSelectedIndex()).equals(blackMarketDeck.elementAt(i)) ) {
					GameApp.instance().showInfo(Locale.get("screen.BlackMarket.InfoMessage") + "\n" + blackMarketDeck.elementAt(i).toString() + ".", 2000);
					this.drawnDeck.addElement(this.blackMarketDeck.elementAt(i));
					this.blackMarketDeck.removeElementAt(i);
					i = 1000;
				}
			}
			/*if ( this.chooseCard.getString(this.chooseCard.getSelectedIndex()).equals(blackMarketDeck.elementAt(i)) ) {
				GameApp.instance().showInfo(Locale.get("screen.BlackMarket.InfoMessage") + "\n" + blackMarketDeck.elementAt(i).toString() + ".", 2000);
				this.drawnDeck.addElement(this.blackMarketDeck.elementAt(i));
				this.blackMarketDeck.removeElementAt(i);
				this.addCommand(cancelBuyCmd);
			}*/
		}
		this.deleteAll();
		//this.chooseCard.deleteAll();
		if ( this.blackMarketDeck.size() != 0 )
			tickerArgs[0] = Locale.get("screen.BlackMarket.DrawTextInfo");
		else
			tickerArgs[0] = Locale.get("screen.BlackMarket.DeckEmpty");
		tickerArgs[1] = "" + drawnDeck.size();
		tickerArgs[2] = "" + blackMarketDeck.size();
		ticker.setString(Locale.get("screen.BlackMarket.Ticker", tickerArgs));
		/*this.chooseCard.removeCommand(this.selectCardCmd);
		this.chooseCard.addCommand(this.drawCardsCmd);*/
		//this.setDefaultCommand(this.drawCardsCmd);
	}
	
	private void cancelBuy(boolean show) {
		if ( show )
			GameApp.instance().showConfirmation(Locale.get("screen.BlackMarket.CancelBuy") + this.drawnDeck.lastElement(), this);
		else {
			if ( this.blackMarketDeck.size() == 0 ) {
				this.blackMarketDeck.addElement(this.drawnDeck.lastElement());
			} else
				this.blackMarketDeck.insertElementAt(this.drawnDeck.lastElement(), getIndexCard(currentlyReachedCard - 1));
			this.drawnDeck.removeElementAt(this.drawnDeck.size() - 1);
		}
	}
	
	private void randomizeDrawn() {
		if ( this.blackMarketDeck.size() == 0 )
			return;
		switch ( new Random(System.currentTimeMillis()).nextInt(6) ) {
		case 0:
			// This means no switching when shuffling
			break;
		case 1:
			// This means swap of first and second card
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 1));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3)), this.getIndexCard(this.currentlyReachedCard - 3 + 1));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3));
			break;
		case 2:
			// This means swap of first and third card
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3));
			break;
		case 3:
			// This means swap of second and third card
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 1)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3 + 1));
			break;
		case 4:
			// This means : Put third as first, the first as the second, and the second as third
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3));
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 1)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3 + 1));
			break;
		case 5:
			// This means : Put third as second, the first as the third, and the second as first
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 1)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3 + 1));
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3));
			break;
		}		
	}
	
	private int getIndexCard(int index) {
		if ( index < 0 ) {
			if ( this.blackMarketDeck.size() + index < 0 )
				return 0;
			return this.blackMarketDeck.size() + index;
		} else if ( index < this.blackMarketDeck.size() )
			return index;
		else
			return this.getIndexCard(index - this.blackMarketDeck.size());
	}
	
	public void setBlackMarketDeck(Cards blackMarketDeck) {
		this.drawnDeck = new Vector(blackMarketDeck.size());
		this.blackMarketDeck = new Vector(blackMarketDeck.size());
		for ( int i = 0 ; i < blackMarketDeck.size() ; i++ )
			this.blackMarketDeck.addElement(blackMarketDeck.getName(i));
		this.currentlyReachedCard = 0;
		tickerArgs[0] = Locale.get("screen.BlackMarket.DrawTextInfo");
		tickerArgs[1] = "" + drawnDeck.size();
		tickerArgs[2] = "" + blackMarketDeck.size();
		ticker.setString(Locale.get("screen.BlackMarket.Ticker", tickerArgs));
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command cmd, Displayable screen) {
		if ( cmd.equals(this.backCmd) )
			GameApp.instance().returnToPreviousScreen();
		else if ( cmd.equals(this.drawCardsCmd) ) {
			this.drawCards();
			tickerArgs[0] = "";
			tickerArgs[1] = "" + drawnDeck.size();
			tickerArgs[2] = "" + blackMarketDeck.size();
			ticker.setString(Locale.get("screen.BlackMarket.Ticker", tickerArgs));
			this.removeCommand(drawCardsCmd);
		} else if ( cmd.equals(this.selectCardCmd) ) {
			this.selectCard();
			this.removeCommand(this.selectCardCmd);
		} else if ( cmd.equals(this.cancelBuyCmd) ) {
			this.cancelBuy(true);
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok")) ) {
			this.cancelBuy(false);
			this.addCommand(this.drawCardsCmd);
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
		if ( blackMarketDeck.size() == 0 )
			this.removeCommand(drawCardsCmd);
	}
	
	public void commandAction(Command cmd, Item item) {
		if ( cmd.equals(this.backCmd) )
			this.commandAction(this.backCmd, this);
		else if ( cmd.equals(this.selectCardCmd) )
			this.selectCard();
	}
}
