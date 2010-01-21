package com;



import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.dominizer.GameApp;

import de.enough.polish.ui.Alert;
import de.enough.polish.ui.TableItem;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.Locale;

public class ShowCardsForm extends Form implements CommandListener {

	private static ShowCardsForm scF = null;
	private TableItem table = null;
	private Command randomizeCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	private Command blackMarketCmd = new Command( Locale.get("cmd.BlackMarket"), Command.OK, 1);
	private Command sortCmd = new Command( Locale.get("cmd.Sort.Main"), Command.SCREEN, 5);
	private Command showInfoCmd = new Command( Locale.get("cmd.ShowChosenCardInfo"), Command.ITEM, 2);
	private Command sortExpNameCmd = new Command( Locale.get("cmd.Sort.ExpName"), Command.ITEM, 2);
	private Command sortExpCostCmd = new Command( Locale.get("cmd.Sort.ExpCost"), Command.ITEM, 3);
	private Command sortNameCmd = new Command( Locale.get("cmd.Sort.Name"), Command.ITEM, 4);
	private Command sortCostNameCmd = new Command( Locale.get("cmd.Sort.CostName"), Command.ITEM, 5);
	private Command sortCostExpCmd = new Command( Locale.get("cmd.Sort.CostExp"), Command.ITEM, 6);
	private Command saveCmd = new Command( Locale.get("cmd.SaveAsPreset"), Command.ITEM, 7);
	
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.SCREEN, 10);
	//private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );


	private ShowCardsForm(String title) {
		//#style behindScreen
		super(title);
		//#debug
		System.out.println("showing cards initialize");
		//#style defaultTable
		this.table = new TableItem();
		this.addCommand(this.randomizeCmd);
		this.addCommand(this.showInfoCmd);
		this.addCommand(this.sortCmd);
		UiAccess.addSubCommand( this.sortExpNameCmd, this.sortCmd, this );
		UiAccess.addSubCommand( this.sortExpCostCmd, this.sortCmd, this );
		UiAccess.addSubCommand( this.sortNameCmd, this.sortCmd, this );
		UiAccess.addSubCommand( this.sortCostExpCmd, this.sortCmd, this );
		UiAccess.addSubCommand( this.sortCostNameCmd, this.sortCmd, this );
		//this.addCommand(this.saveCmd);
		this.addCommand(this.backCmd);
		this.table.setSelectionMode(TableItem.SELECTION_MODE_NONE);//SELECTION_MODE_CELL);
		this.append(this.table);
		this.setCommandListener(this);
	}
	
	public static ShowCardsForm instance() {
		if ( scF == null )
			scF = new ShowCardsForm(Locale.get("screen.RandomizedCards.title"));
		return scF;
	}

	public void reRandomize() {
		Dominion.I().randomizeCards(Cards.COMPARE_EXPANSION_NAME);
		try {
			this.viewCards(Dominion.I().getCurrentlySelected());
		} catch (DominionException e) {
			GameApp.instance().showAlert(e.toString());
		}
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
		if ( GameApp.instance().getCurrentTab() == GameApp.TAB_PRESET )
			this.removeCommand(saveCmd);
		else
			this.addCommand(saveCmd);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(this.backCmd) )
			GameApp.instance().changeToScreen(null);
		else if ( cmd.equals(this.randomizeCmd) )
			this.reRandomize();
		else if ( cmd.equals(this.blackMarketCmd) )
			GameApp.instance().showBlackMarketDeck(-1);
		else if ( cmd.equals(this.showInfoCmd) )
			GameApp.instance().showInfo(Dominion.I().getSelectedInfo(), Alert.FOREVER);
		else if ( cmd.equals(this.saveCmd) ) {
			GameApp.instance().showInputDialog(Locale.get("screen.RandomizedCards.InputMessage"), this);
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok"))) {
			if ( InputForm.instance().getInput() != null ) {
				try {
					new SettingsRecordStorage().savePreset(InputForm.instance().getInput() , Dominion.I().getCurrentAsPresetSave());
				} catch (RecordStoreFullException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RecordStoreNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RecordStoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			GameApp.instance().changeToScreen(this);
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel"))) {
			GameApp.instance().changeToScreen(this);
		} else if ( cmd.equals(this.sortExpNameCmd) ) {
			try {
				this.viewCards(Dominion.I().getCurrentlySelected(Cards.COMPARE_EXPANSION_NAME));
				Cards.COMPARE_PREFERED = Cards.COMPARE_EXPANSION_NAME;
			} catch (DominionException e) {
				// this will never happen as you cannot come to the showscreen without randomizing
			}
		} else if ( cmd.equals(this.sortExpCostCmd) ) {
			try {
				this.viewCards(Dominion.I().getCurrentlySelected(Cards.COMPARE_EXPANSION_COST));
				Cards.COMPARE_PREFERED = Cards.COMPARE_EXPANSION_COST;
			} catch (DominionException e) {}
		} else if ( cmd.equals(this.sortNameCmd) ) {
			try {
				this.viewCards(Dominion.I().getCurrentlySelected(Cards.COMPARE_NAME));
				Cards.COMPARE_PREFERED = Cards.COMPARE_NAME;
			} catch (DominionException e) {}
		} else if ( cmd.equals(this.sortCostNameCmd) ) {
			try {
				this.viewCards(Dominion.I().getCurrentlySelected(Cards.COMPARE_COST_NAME));
				Cards.COMPARE_PREFERED = Cards.COMPARE_COST_NAME;
			} catch (DominionException e) {}
		} else if ( cmd.equals(this.sortCostExpCmd) ) {
			try {
				this.viewCards(Dominion.I().getCurrentlySelected(Cards.COMPARE_COST_EXPANSION));
				Cards.COMPARE_PREFERED = Cards.COMPARE_COST_EXPANSION;
			} catch (DominionException e) {}
		}
	}
}
