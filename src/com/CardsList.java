/**
 * 
 */
package com;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.dominizer.GameApp;

import de.enough.polish.ui.Alert;
import de.enough.polish.ui.List;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class CardsList extends List implements CommandListener {
	
	public static boolean BLACK_MARKET_CMD = false;

	private int listType = List.MULTIPLE;
	
	private int cardSet = 0;
	
	private static int i = 0;
	
	private static Command randomizeSetCmd = new Command( Locale.get("cmd.Randomize.Set"), Command.BACK, 0);
	
	private static Command blackMarketCmd = new Command( Locale.get("cmd.BlackMarket"), Command.SCREEN, 1);
	
	private static Command anotherSetCmd = new Command( Locale.get("cmd.AnotherSet"), Command.ITEM, 2);
	private static Command randSetPreventCmd = new Command( Locale.get("cmd.Randomize.SetPrevent"), Command.ITEM, 4);
	
	private static Command sortCmd = new Command( Locale.get("cmd.Sort.Main"), Command.SCREEN, 5);
	private static Command sortExpNameCmd = new Command( Locale.get("cmd.Sort.ExpName"), Command.ITEM, 4);
	private static Command sortExpCostCmd = new Command( Locale.get("cmd.Sort.ExpCost"), Command.ITEM, 5);
	private static Command sortNameCmd = new Command( Locale.get("cmd.Sort.Name"), Command.ITEM, 6);
	private static Command sortCostNameCmd = new Command( Locale.get("cmd.Sort.CostName"), Command.ITEM, 7);
	private static Command sortCostExpCmd = new Command( Locale.get("cmd.Sort.CostExp"), Command.ITEM, 8);
	
	
	private static Command optionsCmd = new Command( Locale.get("cmd.Options.Main"), Command.ITEM, 10);
	private static Command showInfoCmd = new Command( Locale.get("cmd.ShowChosenCardInfo"), Command.ITEM, 12);
	private static Command deleteSetCmd = new Command( Locale.get("cmd.Set.Delete"), Command.ITEM, 13);
	private static Command saveCmd = new Command( Locale.get("cmd.SaveAsPreset"), Command.ITEM, 14);
	
	private static Command backCmd = new Command( Locale.get("cmd.Back"), Command.SCREEN, 50);
	//private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	
	public CardsList(String title, int listType, int cardSet) {
		//#style mainScreenSet
		super(title, listType);
		this.listType = listType;
		this.cardSet = cardSet;
		//setCommandRandomize(true);
		addCommand(randomizeSetCmd);
		//addCommand(blackMarketCmd);
		addCommand(anotherSetCmd);
		addCommand(randSetPreventCmd);
		//#if !polish.android
			addCommand(sortCmd);
			UiAccess.addSubCommand(sortExpNameCmd, sortCmd, this);
			UiAccess.addSubCommand(sortExpCostCmd, sortCmd, this);
			UiAccess.addSubCommand(sortNameCmd, sortCmd, this);
			UiAccess.addSubCommand(sortCostExpCmd, sortCmd, this);
			UiAccess.addSubCommand(sortCostNameCmd, sortCmd, this);
			addCommand(optionsCmd);
			UiAccess.addSubCommand(showInfoCmd, optionsCmd, this);
			UiAccess.addSubCommand(deleteSetCmd, optionsCmd, this);
			UiAccess.addSubCommand(saveCmd, optionsCmd, this);
		//#else
			addCommand(sortExpNameCmd);
			addCommand(sortExpCostCmd);
			addCommand(sortNameCmd);
			addCommand(sortCostExpCmd);
			addCommand(sortCostNameCmd);
			addCommand(showInfoCmd);
			addCommand(deleteSetCmd);
			addCommand(saveCmd);
		//#endif
		addCommand(backCmd);
		
		setCommandListener(this);
	}

	public void setCards(Cards cards) {
		this.release();
		if ( cards == null )
			return;
		for ( int i = 0 ; i < cards.size() ; i++ ) {
			//#style labelCard
			CardItem cI = new CardItem(" " + cards.getName(i), listType);
			cI.setLeftImage(cards.getCardTypeImage(i));
			cI.setRightImage(cards.getCostImage(i));
			cI.setBothSides(false);
			append(cI);
			//#debug dominizer
			System.out.println("appending to list with card "+cards.getName(i) + " index "+i + " is hold: " +cards.isHold(i));
		}
		updateCards(false);
	}
	
	public void setBlackMarket(boolean isPlaying) {
		if ( isPlaying ) {
			if ( !BLACK_MARKET_CMD ) {
			//#if !polish.android
				UiAccess.addSubCommand(blackMarketCmd, optionsCmd, this);
			//#else
				addCommand(blackMarketCmd);
			//#endif
				BLACK_MARKET_CMD = isPlaying;
			}
		} else {
			if ( BLACK_MARKET_CMD ) {
			//#if !polish.android
				try {
					UiAccess.removeSubCommand(blackMarketCmd, optionsCmd, this);
				} catch ( Exception e) {
					// Do nothing
				}
			//#else
				removeCommand(blackMarketCmd);
			//#endif
				BLACK_MARKET_CMD = isPlaying;
			}
		}
	}
	
	public void updateCards(boolean formCorrect) {
		int[] cP = new int[2];
		for ( i = 0 ; i < this.size() ; i++ ){
			cP = Dominion.I().getCardLocation(getString(i).trim());
			if ( cP[0] > -1 ) {
				if ( formCorrect )
					Dominion.I().expansions[cP[0]].setHoldCard(cP[1], isSelected(i));
				else
					setSelectedIndex(i, Dominion.I().expansions[cP[0]].isHold(cP[1], cardSet));
			} else {
				//#debug dominizer
				System.out.println("couldn't find the card: " + getString(i));
			}
		}
	}
	
	public void reRandomize() throws DominionException {
		Dominion.I().resetIsPlaying(cardSet);
		Dominion.I().randomizeCards(cardSet);
		setCards(Dominion.I().getCurrentlySelected(cardSet));
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(backCmd) ) {
			GameApp.instance().changeToScreen(null);
		} else if ( cmd.equals(blackMarketCmd) )
			GameApp.instance().showBlackMarketDeck(GameApp.SHOWCARDS);
		else if ( cmd.equals(randomizeSetCmd) ) {
			updateCards(true);
			try {
				reRandomize();
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(anotherSetCmd) ) {
			try {
				ShowCardsForm.instance().randomizeNewSet();
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(randSetPreventCmd) ) {
			try {
				Dominion.I().randomizeCardsPrevent(cardSet);
				setCards(Dominion.I().getCurrentlySelected(cardSet));
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(deleteSetCmd) ) {
			Dominion.I().removePlayingSet(cardSet);
			setCards(null);
			ShowCardsForm.instance().updateTabs();
		} else if ( cmd.equals(showInfoCmd) )
			GameApp.instance().showInfo(Dominion.I().getSelectedInfo(cardSet), Alert.FOREVER);
		else if ( cmd.equals(saveCmd) ) {
			GameApp.instance().changeToScreen(InputForm.instance().instance(Locale.get("screen.RandomizedCards.InputMessage"), this));
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok"))) {
			if ( InputForm.instance().getInput().indexOf(SettingsRecordStorage.BIG_SPLITTER) > 0 )
				GameApp.instance().showAlert(Locale.get("alert.Randomization.Save.IllegalChar"));
			else if ( !InputForm.instance().getInput().equals("") ) {
				SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.preset"));
				SettingsRecordStorage.instance().addData(InputForm.instance().getInput(), Dominion.I().getCurrentAsSave(cardSet));
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
				setCards(Dominion.I().getCurrentlySelected(cardSet, Cards.COMPARE_EXPANSION_NAME));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_EXPANSION_NAME;
			} catch (DominionException e) {
				// this will never happen as you cannot come to the showscreen without randomizing
			}
		} else if ( cmd.equals(sortExpCostCmd) ) {
			try {
				setCards(Dominion.I().getCurrentlySelected(cardSet, Cards.COMPARE_EXPANSION_COST));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_EXPANSION_COST;
			} catch (DominionException e) {}
		} else if ( cmd.equals(sortNameCmd) ) {
			try {
				setCards(Dominion.I().getCurrentlySelected(cardSet, Cards.COMPARE_NAME));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_NAME;
			} catch (DominionException e) {}
		} else if ( cmd.equals(sortCostNameCmd) ) {
			try {
				setCards(Dominion.I().getCurrentlySelected(cardSet, Cards.COMPARE_COST_NAME));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_COST_NAME;
			} catch (DominionException e) {}
		} else if ( cmd.equals(sortCostExpCmd) ) {
			try {
				setCards(Dominion.I().getCurrentlySelected(cardSet, Cards.COMPARE_COST_EXPANSION));
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
			if ( keyCode - Canvas.KEY_NUM0 < this.size() ) {
				if ( keyCode - Canvas.KEY_NUM0 == 0 )
					setSelectedIndex(this.size() - 1, !isSelected(this.size() - 1));
				else
					setSelectedIndex(keyCode - Canvas.KEY_NUM0 - 1, !isSelected(keyCode - Canvas.KEY_NUM0 - 1));
			}
			repaint();
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}
	
	/*
	private void setCommandRandomize(boolean rand) {
		try {
			removeCommand(randomizeCmd);
		} catch (Exception e) {}
		try {
			removeCommand(randomizeSetCmd);
		} catch (Exception e) {}
		if ( rand ) {
			addCommand(randomizeCmd);
		} else {
			addCommand(randomizeSetCmd);
		}
	}*/

	public void release() {
		this.deleteAll();
	}
}