package com;



import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
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
	private Command randomizeCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.OK, 0);
	private Command randomizeSetCmd = new Command( Locale.get("cmd.Randomize.Set"), Command.OK, 0);
	private Command blackMarketCmd = new Command( Locale.get("cmd.BlackMarket"), Command.SCREEN, 1);
	
	private Command sortCmd = new Command( Locale.get("cmd.Sort.Main"), Command.SCREEN, 5);
	private Command showInfoCmd = new Command( Locale.get("cmd.ShowChosenCardInfo"), Command.ITEM, 2);
	private Command sortExpNameCmd = new Command( Locale.get("cmd.Sort.ExpName"), Command.ITEM, 2);
	private Command sortExpCostCmd = new Command( Locale.get("cmd.Sort.ExpCost"), Command.ITEM, 3);
	private Command sortNameCmd = new Command( Locale.get("cmd.Sort.Name"), Command.ITEM, 4);
	private Command sortCostNameCmd = new Command( Locale.get("cmd.Sort.CostName"), Command.ITEM, 5);
	private Command sortCostExpCmd = new Command( Locale.get("cmd.Sort.CostExp"), Command.ITEM, 6);
	private Command anotherSetCmd = new Command( Locale.get("cmd.AnotherSet"), Command.ITEM, 6);
	private Command saveCmd = new Command( Locale.get("cmd.SaveAsPreset"), Command.ITEM, 7);
	
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.SCREEN, 10);
	//private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	
	private ShowCardsForm(String title) {
		//#style behindTitle
		super(title);
		//#debug info
		System.out.println("showing cards initialize");
		//#style defaultTable
		table = new TableItem();
		setCommandRandomize(true);
		addCommand(showInfoCmd);
		addCommand(anotherSetCmd);
		addCommand(sortCmd);
		UiAccess.addSubCommand( sortExpNameCmd, sortCmd, this );
		UiAccess.addSubCommand( sortExpCostCmd, sortCmd, this );
		UiAccess.addSubCommand( sortNameCmd, sortCmd, this );
		UiAccess.addSubCommand( sortCostExpCmd, sortCmd, this );
		UiAccess.addSubCommand( sortCostNameCmd, sortCmd, this );
		addCommand(saveCmd);
		addCommand(backCmd);
		table.setSelectionMode(TableItem.SELECTION_MODE_NONE);//SELECTION_MODE_CELL);
		append(table);
		setCommandListener(this);
	}
	
	public static ShowCardsForm instance() {
		if ( scF == null ) {
			scF = new ShowCardsForm(null);//Locale.get("screen.RandomizedCards.title")
		}
		return scF;
	}

	public void reRandomize() {
		try {
			Dominion.I().randomizeCards();
			viewCards(Dominion.I().getCurrentlySelected());
		} catch (DominionException e) {
			GameApp.instance().showAlert(e.toString());
		}
	}

	public void viewCards(Cards cards) {
		table.releaseResources();
		//#debug info
		System.out.println("adding card information");
		if ( cards == null ) {
			for (int cardNumber = 0 ; cardNumber < table.getNumberOfRows() - 1 ; cardNumber++ ) {
				//#style tableCell
				table.set(0, cardNumber + 1, "");
				//#style tableCell
				table.set(1, cardNumber + 1, "");
				//#style tableCell
				table.set(2, cardNumber + 1, "");
			}
			return;
		}
		table.setDimension(3, cards.size() + 1);
		//#debug info
		System.out.println("adding header");
		//#style tableHeading
		table.set(0, 0, Locale.get("table.heading.Name"));
		//#style tableHeading
		table.set(1, 0, Locale.get("table.heading.Expansion"));
		//#style tableHeading
		table.set(2, 0, Locale.get("table.heading.Cost"));
		if ( Dominion.I().hasBlackMarketPlaying() )
			addCommand(blackMarketCmd);
		else
			removeCommand(blackMarketCmd);
		for (int cardNumber = 0 ; cardNumber < cards.size() ; cardNumber++ ) {
			if ( Dominion.I().isHold(cards.getName(cardNumber)) ) {
				//#style tableCellHold
				table.set(0, cardNumber + 1, cards.getName(cardNumber));
			} else {
				//#style tableCell
				table.set(0, cardNumber + 1, cards.getName(cardNumber));
			}
			try {
				//#style tableCell
				table.set(1, cardNumber + 1, new ImageItem(null, 
						Image.createImage("/" +	Dominion.getExpansionImageName(cards.getExpansion(cardNumber)) 
								+ cards.getCardType(cardNumber) + ".png"), ImageItem.PLAIN, null));
			} catch (IOException e) {
				table.set(1, cardNumber + 1, Dominion.getExpansionName(cards.getExpansion(cardNumber)));
			}
			try {
				//#style tableCellCentered
				table.set(2, cardNumber + 1, new ImageItem(null, 
						Image.createImage("/trea" + new Integer(cards.getCost(cardNumber)).toString() + ".png"), ImageItem.PLAIN, null));
			} catch (IOException e) {
				//#style tableCellCentered
				table.set(2, cardNumber + 1, new Integer(cards.getCost(cardNumber)));
			}
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(backCmd) ) {
			Dominion.RANDOMIZE_COMPLETELY_NEW = true;
			Dominion.I().resetHoldCards();
			setCommandRandomize(true);
			GameApp.instance().changeToScreen(null);
		} else if ( cmd.equals(randomizeCmd) ) {
			Dominion.I().resetIsPlaying(true);
			reRandomize();
		} else if ( cmd.equals(randomizeSetCmd) ) {
			Dominion.I().resetIsPlaying(false);
			reRandomize();
		} else if ( cmd.equals(blackMarketCmd) )
			GameApp.instance().showBlackMarketDeck(GameApp.SHOWCARDS);
		else if ( cmd.equals(anotherSetCmd) ) {
			Dominion.RANDOMIZE_COMPLETELY_NEW = false;
			Dominion.I().resetHoldCards();
			reRandomize();
			setCommandRandomize(false);
		} else if ( cmd.equals(showInfoCmd) )
			GameApp.instance().showInfo(Dominion.I().getSelectedInfo(), Alert.FOREVER);
		else if ( cmd.equals(saveCmd) ) {
			GameApp.instance().showInputDialog(Locale.get("screen.RandomizedCards.InputMessage"), this);
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok"))) {
			if ( InputForm.instance().getInput().indexOf(SettingsRecordStorage.BIG_SPLITTER) > 0 )
				GameApp.instance().showAlert(Locale.get("alert.Randomization.Save.IllegalChar"));
			else if ( !InputForm.instance().getInput().equals("") ) {
				SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.preset"));
				SettingsRecordStorage.instance().addData(InputForm.instance().getInput(), Dominion.I().getCurrentAsPresetSave());
				try {
					SettingsRecordStorage.instance().writeData();
				} catch (RecordStoreFullException e) {
					// TODO Utilize GameApp.showAlert
				} catch (RecordStoreNotFoundException e) {
					// TODO Utilize GameApp.showAlert
				} catch (RecordStoreException e) {
					// TODO Utilize GameApp.showAlert
				}
			}
			GameApp.instance().changeToScreen(this);
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.clear"))) {
			InputForm.instance().clearInput();
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel"))) {
			GameApp.instance().changeToScreen(this);
		} else if ( cmd.equals(sortExpNameCmd) ) {
			try {
				viewCards(Dominion.I().getCurrentlySelected(Cards.COMPARE_EXPANSION_NAME));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_EXPANSION_NAME;
			} catch (DominionException e) {
				// this will never happen as you cannot come to the showscreen without randomizing
			}
		} else if ( cmd.equals(sortExpCostCmd) ) {
			try {
				viewCards(Dominion.I().getCurrentlySelected(Cards.COMPARE_EXPANSION_COST));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_EXPANSION_COST;
			} catch (DominionException e) {}
		} else if ( cmd.equals(sortNameCmd) ) {
			try {
				viewCards(Dominion.I().getCurrentlySelected(Cards.COMPARE_NAME));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_NAME;
			} catch (DominionException e) {}
		} else if ( cmd.equals(sortCostNameCmd) ) {
			try {
				viewCards(Dominion.I().getCurrentlySelected(Cards.COMPARE_COST_NAME));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_COST_NAME;
			} catch (DominionException e) {}
		} else if ( cmd.equals(sortCostExpCmd) ) {
			try {
				viewCards(Dominion.I().getCurrentlySelected(Cards.COMPARE_COST_EXPANSION));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_COST_EXPANSION;
			} catch (DominionException e) {}
		}
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
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
			holdCard(keyCode - Canvas.KEY_NUM0);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}
	
	public void holdCard(int card) {
		String cardName;
		if ( card == 0 )
			card = 10;
		cardName = ((StringItem) table.get(0, card)).getText();
		if ( Dominion.I().holdCard(cardName) ) {
			//#style tableCellHold
			table.set(0, card, cardName);
			//#debug info
			System.out.println("holding card: " + cardName);
		} else {
			//#style tableCell
			table.set(0, card, cardName);
			//#debug info
			System.out.println("deholding card: " + cardName);
		}
	}
	
	private void setCommandRandomize(boolean rand) {
		try {
			removeCommand(randomizeCmd);
		} catch (Exception e) {}/*
		try {
			table.removeCommand(randomizeCmd);
		} catch (Exception e) {}*/
		try {
			removeCommand(randomizeSetCmd);
		} catch (Exception e) {}/*
		try {
			table.removeCommand(randomizeSetCmd);
		} catch (Exception e) {}*/
		if ( rand ) {
			addCommand(randomizeCmd);
			/*table.addCommand(randomizeCmd);
			table.setDefaultCommand(randomizeCmd);*/
		} else {
			addCommand(randomizeSetCmd);
			/*table.addCommand(randomizeSetCmd);
			table.setDefaultCommand(randomizeSetCmd);*/
		}
	}
}	
