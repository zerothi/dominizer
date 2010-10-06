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
import com.util.Cards;
import com.util.Dominion;
import com.util.DominionException;
import com.util.Rand;
import com.util.SettingsRecordStorage;

import de.enough.polish.ui.Alert;
import de.enough.polish.ui.List;
import de.enough.polish.ui.Style;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class CardsList extends List implements CommandListener {
	
	private static long timer = -1;
	private static int i = 0;
	private boolean hasBlackMarketCmd = false;
	private int listType = List.MULTIPLE;
	private int cardSet = 0;
	
	private static Command randomizeSetCmd = new Command( Locale.get("cmd.Randomize.Set"), Command.BACK, 0);
	
	private static Command anotherSetCmd = new Command( Locale.get("cmd.AnotherSet"), Command.ITEM, 1);
	
	private static Command blackMarketCmd = new Command( Locale.get("cmd.BlackMarket"), Command.ITEM, 15);
	
	private static Command randSetPreventCmd = new Command( Locale.get("cmd.Randomize.SetPrevent"), Command.ITEM, 4);
	private static Command randConditionCmd = new Command( Locale.get("cmd.Randomize.Condition"), Command.ITEM, 8);
	private static Command randPureCmd = new Command( Locale.get("cmd.Randomize.Pure"), Command.ITEM, 9);
	
	private static Command optionsCmd = new Command( Locale.get("cmd.Options.Main"), Command.ITEM, 10);
	private static Command showInfoCmd = new Command( Locale.get("cmd.ShowChosenCardInfo"), Command.ITEM, 20);
	private static Command deleteSetCmd = new Command( Locale.get("cmd.Set.Delete"), Command.ITEM, 25);
	private static Command deleteAllSetsCmd = new Command( Locale.get("cmd.Set.DeleteAll"), Command.ITEM, 26);
	private static Command saveCmd = new Command( Locale.get("cmd.SaveAsPreset"), Command.ITEM, 30);
		
	private static Command sortCmd = new Command( Locale.get("cmd.Sort.Main"), Command.SCREEN, 5);
	private static Command sortExpNameCmd = new Command( Locale.get("cmd.Sort.ExpName"), Command.ITEM, 35);
	private static Command sortExpCostCmd = new Command( Locale.get("cmd.Sort.ExpCost"), Command.ITEM, 36);
	private static Command sortNameCmd = new Command( Locale.get("cmd.Sort.Name"), Command.ITEM, 37);
	private static Command sortCostNameCmd = new Command( Locale.get("cmd.Sort.CostName"), Command.ITEM, 38);
	private static Command sortCostExpCmd = new Command( Locale.get("cmd.Sort.CostExp"), Command.ITEM, 39);
	
	private static Command prosperityDiceCmd = new Command( Locale.get("cmd.Prosperity.Dice"), Command.ITEM, 45);
	private static Command backCmd = new Command( Locale.get("cmd.Back"), Command.ITEM, 50);
	
	public CardsList(String title, int listType, int cardSet) {
		this(title, listType, cardSet, null);
	}
	
	public CardsList(String title, int listType, int cardSet, Style style) {
		super(title, listType, style);
		this.listType = listType;
		this.cardSet = cardSet;
		//setCommandRandomize(true);
		addCommand(randomizeSetCmd);
		//addCommand(blackMarketCmd);
		addCommand(anotherSetCmd);
		addCommand(randSetPreventCmd);
		addCommand(randPureCmd);
		addCommand(randConditionCmd);
		//#if !polish.android
			addCommand(optionsCmd);
			UiAccess.addSubCommand(showInfoCmd, optionsCmd, this);
			UiAccess.addSubCommand(deleteSetCmd, optionsCmd, this);
			UiAccess.addSubCommand(deleteAllSetsCmd, optionsCmd, this);
			UiAccess.addSubCommand(saveCmd, optionsCmd, this);
		//#else
			addCommand(showInfoCmd);
			addCommand(deleteSetCmd);
			addCommand(deleteAllSetsCmd);
			addCommand(saveCmd);
		//#endif
		//#if !polish.android
			UiAccess.addSubCommand(sortCmd, optionsCmd, this);
			UiAccess.addSubCommand(sortExpNameCmd, sortCmd, this);
			UiAccess.addSubCommand(sortExpCostCmd, sortCmd, this);
			UiAccess.addSubCommand(sortNameCmd, sortCmd, this);
			UiAccess.addSubCommand(sortCostExpCmd, sortCmd, this);
			UiAccess.addSubCommand(sortCostNameCmd, sortCmd, this);
		//#else
			addCommand(sortExpNameCmd);
			addCommand(sortExpCostCmd);
			addCommand(sortNameCmd);
			addCommand(sortCostExpCmd);
			addCommand(sortCostNameCmd);
		//#endif
		addCommand(backCmd);
		
		setCommandListener(this);
		//setItemStateListener(this);
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
			System.out.println("appending to list with card "+cards.getName(i) + " index "+i + " is hold: " +cards.isHold(i) + " on set " + cardSet);
		}
		if ( cards.fromExpansion(Dominion.PROSPERITY) > 0 )
			addCommand(prosperityDiceCmd);
		updateCards(false);
	}
	
	public void setBlackMarket(boolean isPlaying) {
		if ( isPlaying && !hasBlackMarketCmd ) {
		//#if !polish.android
			UiAccess.addSubCommand(blackMarketCmd, optionsCmd, this);
		//#else
			addCommand(blackMarketCmd);
		//#endif
			hasBlackMarketCmd = isPlaying;
		} else if ( !isPlaying && hasBlackMarketCmd ) {
		//#if !polish.android
			try {
				//#= UiAccess.removeSubCommand(blackMarketCmd, optionsCmd, this);
			} catch ( Exception e) {
				// Do nothing
			}
		//#else
			removeCommand(blackMarketCmd);
		//#endif
			hasBlackMarketCmd = isPlaying;
		}
	}
	
	public void updateCards(boolean formCorrect) {
		int[] cP = new int[2];
		for ( i = 0 ; i < this.size() ; i++ ){
			cP = Dominion.I().getCardLocation(getString(i).trim());
			if ( cP[0] > -1 ) {
				if ( formCorrect )
					Dominion.expansions[cP[0]].setHoldCard(cP[1], isSelected(i));
				else
					setSelectedIndex(i, Dominion.expansions[cP[0]].isHold(cP[1], cardSet));
			} else {
				//#debug dominizer
				System.out.println("couldn't find the card: " + getString(i));
			}
		}
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(backCmd) ) {
			GameApp.instance().changeToScreen(null);
		} else if ( cmd.equals(blackMarketCmd) )
			GameApp.instance().showBlackMarketDeck(GameApp.SHOWCARDS);
		else if ( cmd.equals(randomizeSetCmd) ) {
			updateCards(true);
			try {
				Dominion.I().randomizeCards(cardSet);
				setCards(Dominion.I().getSelectedCards(cardSet));
				ShowCardsForm.instance().updateBlackMarket();
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(randSetPreventCmd) ) {
			updateCards(true);
			try {
				Dominion.I().randomizeCards(cardSet, Dominion.RAND_EXPANSION_CARDS + Dominion.RAND_PERCENTAGE_CARDS + Dominion.RAND_HOLD + Dominion.RAND_PREVENT);
				setCards(Dominion.I().getSelectedCards(cardSet));
				ShowCardsForm.instance().updateBlackMarket();
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(randConditionCmd) ) {
			updateCards(true);
			try {
				Dominion.I().randomizeCards(cardSet, Dominion.RAND_HOLD + Dominion.RAND_CONDITION);
				setCards(Dominion.I().getSelectedCards(cardSet));		
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(randPureCmd) ) {
			updateCards(true);
			try {
				Dominion.I().randomizeCards(cardSet, Dominion.RAND_HOLD);
				setCards(Dominion.I().getSelectedCards(cardSet));
				ShowCardsForm.instance().updateBlackMarket();
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(anotherSetCmd) ) {
			try {
				Dominion.I().randomizeCards();
				ShowCardsForm.instance().addNewCards(Dominion.I().getSelectedCards(Dominion.I().getCurrentSet()));
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(deleteSetCmd) ) {
			Dominion.I().removePlayingSet(cardSet);
			setCards(null);
			ShowCardsForm.instance().updateTabs();
			String[] tmp = new String[] { "" + cardSet , "" + Dominion.I().getCurrentSet()};
			GameApp.instance().showInfo(Locale.get("info.delete.set", tmp), Alert.FOREVER);
		} else if ( cmd.equals(deleteAllSetsCmd) ) {
			Dominion.I().resetIsPlaying(-1);
			setCards(null);
			ShowCardsForm.instance().updateTabs();
		} else if ( cmd.equals(showInfoCmd) ) {
			GameApp.instance().showInfo(Dominion.I().getSelectedInfo(cardSet), Alert.FOREVER);
			/*
			try {
				GameApp.instance().showCardInfo(Dominion.I().getSelectedCards(cardSet).getTypeInfo(), Dominion.I().getSelectedCards(cardSet).getAddsInfo());
			} catch (DominionException e) {

			}*/
		} else if (cmd.equals(prosperityDiceCmd) ) {
			Rand.resetSeed();
			String[] tmp = new String[4];
			String t;
			int i;
			try {
				tmp[0] = "" + Dominion.I().getSelectedCards(cardSet).fromExpansion(Dominion.PROSPERITY);
				tmp[1] = Locale.get("expansion.prosperity");
				t = tmp[1];
				i = Rand.randomInt(Dominion.I().getSelectedCards(cardSet).size()) + 1;
				tmp[2] = "" + i;
				if ( i <= Dominion.I().getSelectedCards(cardSet).fromExpansion(Dominion.PROSPERITY) )
					tmp[3] = Locale.get("info.randomized.Prosperity.Succes", t);
				else
					tmp[3] = Locale.get("info.randomized.Prosperity.Failed", t);
				GameApp.instance().showInfo(Locale.get("info.randomized.Prosperity", tmp), Alert.FOREVER);
			} catch (DominionException e) {
				// TODO Code 1
				GameApp.instance().showAlert("Code 1\nIf this error occured contact Zerothi at BGG.");
			}
		} else if ( cmd.equals(saveCmd) ) {
			GameApp.instance().changeToScreen(InputForm.instance().instance(Locale.get("screen.RandomizedCards.InputMessage"), this));
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok"))) {
			if ( InputForm.instance().getInput().indexOf(SettingsRecordStorage.BIG_SPLITTER) > 0 )
				GameApp.instance().showAlert(Locale.get("alert.Randomization.Save.IllegalChar"));
			else if ( !InputForm.instance().getInput().equals("") ) {
				SettingsRecordStorage.instance().changeToRecordStore("presets");
				SettingsRecordStorage.instance().addData(InputForm.instance().getInput(), Dominion.I().getPlayingSetAsSave(cardSet));
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
			GameApp.instance().changeToScreen(ShowCardsForm.instance());
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.clear"))) {
			InputForm.instance().clearInput();
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel"))) {
			GameApp.instance().changeToScreen(this);
		} else if ( cmd.equals(sortExpNameCmd) ) {
			try {
				setCards(Dominion.I().getSelectedCards(cardSet, Cards.COMPARE_EXPANSION_NAME));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_EXPANSION_NAME;
			} catch (DominionException e) {
				// this will never happen as you cannot come to the showscreen without randomizing
			}
		} else if ( cmd.equals(sortExpCostCmd) ) {
			try {
				setCards(Dominion.I().getSelectedCards(cardSet, Cards.COMPARE_EXPANSION_COST));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_EXPANSION_COST;
			} catch (DominionException e) {}
		} else if ( cmd.equals(sortNameCmd) ) {
			try {
				setCards(Dominion.I().getSelectedCards(cardSet, Cards.COMPARE_NAME));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_NAME;
			} catch (DominionException e) {}
		} else if ( cmd.equals(sortCostNameCmd) ) {
			try {
				setCards(Dominion.I().getSelectedCards(cardSet, Cards.COMPARE_COST_NAME));
				Cards.COMPARE_PREFERRED = Cards.COMPARE_COST_NAME;
			} catch (DominionException e) {}
		} else if ( cmd.equals(sortCostExpCmd) ) {
			try {
				setCards(Dominion.I().getSelectedCards(cardSet, Cards.COMPARE_COST_EXPANSION));
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
		case Canvas.KEY_POUND:
		case Canvas.KEY_STAR:
			timer = System.currentTimeMillis();
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}
	
	
	public void keyReleased(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_STAR:
			if ( System.currentTimeMillis() - timer > 600 && timer > 0 ) {
				commandAction(deleteSetCmd, this);
				timer = -1;
				break;
			}
		case Canvas.KEY_POUND:
			commandAction(prosperityDiceCmd, this);
			timer = -1;
			break;
		default:
			//#= super.keyReleased(keyCode);
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