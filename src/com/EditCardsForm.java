package com;



import java.util.Vector;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;

import com.dominizer.GameApp;


import de.enough.polish.ui.Choice;
import de.enough.polish.util.Locale;

public class EditCardsForm extends Form implements CommandListener, ItemStateListener {
	
	private GameApp app = null;
	private Object[][] cards = null;
	private ChoiceGroup cardGroup= null;
	private Command backCmd = new Command( Locale.get( "cmd.Back" ), Command.BACK, 8 );
	//private Command switchCmd = new Command( Locale.get( "cmd.switch" ), Command.SCREEN, 8 );
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );

	public EditCardsForm(GameApp app, String title) {
		//#style mainScreen
		super(title);
		this.app = app;
		//#style choiceGroup
		this.cardGroup = new ChoiceGroup(Locale.get("screen.EditSingleCards.ChoiceCurrentlySelected"), Choice.EXCLUSIVE);
		//this.cardGroup.addCommand(switchCmd);
		//this.cardGroup.setDefaultCommand(switchCmd);
		this.addCommand(backCmd);
		this.addCommand(quitCmd);
		this.append(this.cardGroup);
		this.setCommandListener(this);
		this.setItemStateListener(this);
	}

	public void setCards(Vector cardsV) {
		this.cards = new Object[11][cardsV.size()];
		this.cardGroup.deleteAll();/*
		for ( int cardNumber = 0 ; cardNumber < cardsV.size() ; cardNumber++ ) {
			this.cards[0][cardNumber] = ( (Card)cardsV.elementAt(cardNumber)).getName();
			this.cardGroup.append((String)this.cards[0][cardNumber], null);
			this.cards[1][cardNumber] = ( (Card)cardsV.elementAt(cardNumber)).getExpansion();
			this.cards[2][cardNumber] = new Integer(( (Card)cardsV.elementAt(cardNumber)).getCost());
			this.cards[3][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isAction());
			this.cards[4][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isVictory());
			this.cards[5][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isTreasure());
			this.cards[6][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isAttack());
			this.cards[7][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isReaction());
			this.cards[8][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isDuration());
			this.cards[9][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isAvailable());
			this.cards[10][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isBlackMarketAvailable());
		}*/
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(backCmd) )
			this.app.changeToScreen(null);
		else if ( cmd.equals(quitCmd) )
			this.app.notifyDestroyed();
		/*else if ( cmd.equals(switchCmd) ) {
			int selIndex = this.cardGroup.getSelectedIndex();
			this.append((String)this.cards[9][selIndex]);
			this.cards[9][selIndex] = new Boolean(! ((Boolean)this.cards[9][selIndex]).booleanValue());
		}*/
	}

	public void itemStateChanged(Item item) {
		this.itemStateChanged(item, null);
	}
	
	public void itemStateChanged(Item item, Displayable disp) {
		// TODO Auto-generated method stub
		if ( item.equals(this.cardGroup) ) {
			//
		}
			
	}
}
