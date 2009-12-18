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
	private Command randomizeCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.BACK, 1);
	private Command blackMarketCmd = new Command( Locale.get("cmd.BlackMarket"), Command.SCREEN, 1);
	private Command showInfoCmd = new Command( Locale.get("cmd.ShowChosenCardInfo"), Command.SCREEN, 2);
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.SCREEN, 10);
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
		this.addCommand(showInfoCmd);
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
		this.table.setDimension(3, cards.size() + 1);
		//#debug
		System.out.println("adding header");
		//#style tableHeading
		this.table.set(0, 0, Locale.get("table.heading.Name"));
		//#style tableHeading
		this.table.set(1, 0, Locale.get("table.heading.Expansion"));
		//#style tableHeading
		this.table.set(2, 0, Locale.get("table.heading.Cost"));
		//#debug
		System.out.println("adding card information");
		for (int cardNumber = 0 ; cardNumber < cards.size() ; cardNumber++ ) {
			//#style tableCell
			this.table.set(0, cardNumber + 1, cards.getName(cardNumber));
			if ( cards.getName(cardNumber).equals(Locale.get("card.BlackMarket")) )
				this.addCommand(blackMarketCmd);
			try {
				//#style tableCell
				this.table.set(1, cardNumber + 1, new ImageItem(null, 
						Image.createImage("/" + cards.getExpansion(cardNumber) + cards.getCardType(cardNumber) + ".png"), ImageItem.PLAIN, null));
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
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd == this.backCmd )
			this.app.changeToScreen(null);
		else if ( cmd == this.randomizeCmd )
			this.reRandomize();
		else if ( cmd == this.blackMarketCmd )
			this.app.showBlackMarketDeck(this);
		else if ( cmd == this.showInfoCmd )
			this.app.showBlackMarketDeck(this);
	}
}
