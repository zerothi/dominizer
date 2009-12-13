package com;



import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;

import com.dominizer.GameApp;

import de.enough.polish.ui.TableItem;
import de.enough.polish.util.Locale;

public class ShowCardsForm extends Form  implements CommandListener {
	
	private GameApp app = null;
	private TableItem table = null;
	private Command randomizeCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.SCREEN, 1);
	private Command blackMarketCmd = new Command( Locale.get("cmd.BlackMarket"), Command.SCREEN, 2);
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.BACK, 0);
	//private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	
	
	public ShowCardsForm(GameApp app, String title) {
		//#style mainScreen
		super(title);
		this.app = app;
		//#debug
		System.out.println("showing cards initialize");
		//#style defaultTable
		this.table = new TableItem();
		this.addCommand(this.randomizeCmd);
		this.addCommand(this.backCmd);
		this.table.setSelectionMode(TableItem.SELECTION_MODE_NONE);//SELECTION_MODE_CELL);
		this.append(this.table);
		this.setCommandListener(this);
	}
	
	public void reRandomize() {
		this.viewCards(Dominion.instance().getRandomizedCards());
	}

	public void viewCards(Cards cards) {
		this.removeCommand(blackMarketCmd);
		this.table.setDimension(4, cards.size() + 1);
		//#debug
		System.out.println("adding header");
		//#style tableHeading
		this.table.set(0, 0, Locale.get("table.heading.Name"));
		//#style tableHeading
		this.table.set(1, 0, Locale.get("table.heading.Expansion"));
		//#style tableHeading
		this.table.set(2, 0, Locale.get("table.heading.Cost"));
		//#style tableHeading
		this.table.set(3, 0, Locale.get("table.heading.Type"));
		//#debug
		System.out.println("adding card information");
		for (int cardNumber = 0 ; cardNumber < cards.size() ; cardNumber++ ) {
			//#style tableCell
			this.table.set(0, cardNumber + 1, cards.getName(cardNumber));
			/*
			switch ( cards.getCardType(cardNumber) ) {
			case Cards.TYPE_ACTION:
				//#style cardTypeAction
				this.table.set(0, cardNumber + 1, cards.getName(cardNumber) );
				break;
			case Cards.TYPE_ACTION_ATTACK:
				//#style cardTypeActionAttack
				this.table.set(0, cardNumber + 1, cards.getName(cardNumber) );
				break;
			case Cards.TYPE_ACTION_REACTION:
				//#style cardTypeActionReaction
				this.table.set(0, cardNumber + 1, cards.getName(cardNumber) );
				break;
			case Cards.TYPE_VICTORY:
				//#style cardTypeVictory
				this.table.set(0, cardNumber + 1, cards.getName(cardNumber) );
				break;
			case Cards.TYPE_ACTION_TREASURY:
				//#style cardTypeActionTreasury
				this.table.set(0, cardNumber + 1, cards.getName(cardNumber) );
				break;
			case Cards.TYPE_ACTION_VICTORY:
				//#style cardTypeActionVictory
				this.table.set(0, cardNumber + 1, cards.getName(cardNumber) );
				break;
			case Cards.TYPE_TREASURY_VICTORY:
				//#style cardTypeTreasuryVictory
				this.table.set(0, cardNumber + 1, cards.getName(cardNumber) );
				break;
			case Cards.TYPE_ACTION_DURATION:
				//#style cardTypeActionDuration
				this.table.set(0, cardNumber + 1, cards.getName(cardNumber) );
				break;
			}
			*/
			if ( cards.getName(cardNumber).equals(Locale.get("card.BlackMarket")) )
				this.addCommand(blackMarketCmd);
			try {
				//#style tableCell
				this.table.set(1, cardNumber + 1, new ImageItem(null, 
						Image.createImage("/" + cards.getExpansion(cardNumber) + ".png"), ImageItem.PLAIN, null));
			} catch (IOException e) {
				this.table.set(1, cardNumber + 1, cards.getExpansion(cardNumber));
			}
			try {
				//#style tableCellCentered
				this.table.set(2, cardNumber + 1, new ImageItem(null, 
						Image.createImage("/trea" + new Integer(cards.getCost(cardNumber)).toString() + ".png"), ImageItem.PLAIN, null));
			} catch (IOException e) {
				//#style tableCellCentered
				this.table.set(2, cardNumber + 1, new Integer(cards.getCost(cardNumber)));
			}
			switch ( cards.getCardType(cardNumber) ) {
			case Cards.TYPE_ACTION:
				//#style cardTypeAction
				this.table.set(3, cardNumber + 1, " A ");
				break;
			case Cards.TYPE_ACTION_ATTACK:
				//#style cardTypeActionAttack
				this.table.set(3, cardNumber + 1, "A+A" );
				break;
			case Cards.TYPE_ACTION_REACTION:
				//#style cardTypeActionReaction
				this.table.set(3, cardNumber + 1, "A+R" );
				break;
			case Cards.TYPE_VICTORY:
				//#style cardTypeVictory
				this.table.set(3, cardNumber + 1, " V " );
				break;
			case Cards.TYPE_ACTION_TREASURY:
				//#style cardTypeActionTreasury
				this.table.set(3, cardNumber + 1, "A+T" );
				break;
			case Cards.TYPE_ACTION_VICTORY:
				//#style cardTypeActionVictory
				this.table.set(3, cardNumber + 1, "A+V" );
				break;
			case Cards.TYPE_TREASURY_VICTORY:
				//#style cardTypeTreasuryVictory
				this.table.set(3, cardNumber + 1, "T+V" );
				break;
			case Cards.TYPE_ACTION_DURATION:
				//#style cardTypeActionDuration
				this.table.set(3, cardNumber + 1, "A+D" );
				break;
			}
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(this.backCmd) )
			this.app.changeToScreen(null);
		else if ( cmd.equals(this.randomizeCmd) )
			this.reRandomize();
		else if ( cmd.equals(this.blackMarketCmd) )
			this.app.showBlackMarketDeck(this);
	}
}
