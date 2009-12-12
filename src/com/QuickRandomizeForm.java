package com;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

import com.dominizer.GameApp;


import de.enough.polish.ui.ChoiceItem;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.Locale;

/**
 * 
 */

/**
 * @author nick
 *
 */
public class QuickRandomizeForm extends Form implements CommandListener, ItemCommandListener {
	
	GameApp app = null;
	ChoiceGroup whatToDoCG = null;
	ChoiceGroup quickGameRandomizerCG = null;
	Command selectCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 0);
	Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.SCREEN, 0);
	//Command showCardsListCmd = new Command( Locale.get( "cmd.ShowCards" ), Command.ITEM, 8 );
	Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.BACK, 10);
	
	public QuickRandomizeForm(GameApp app, String title) {
		//#style mainScreen
		super(title);
		this.app = app;
		//#style choiceGroup
		this.whatToDoCG = new ChoiceGroup(Locale.get("mainScreen.ChoiceWhatToDo"), ChoiceGroup.EXCLUSIVE);
		// #style choiceItem
		//this.whatToDoCG.append(Locale.get("cmd.Randomize.Show"), null);
		//#style choiceItem
		this.whatToDoCG.append(Locale.get("cmd.BlackMarket.Show"), null);
		/*//style choiceItem
		this.whatToDoCG.append(Locale.get("cmd.EditCards.Show"), null);*/
		this.whatToDoCG.addCommand(this.selectCmd);
		this.whatToDoCG.setItemCommandListener(this);
		// Setting up the QuickGame Randomizer
		//#style filterCards
		this.quickGameRandomizerCG = new ChoiceGroup(Locale.get("mainScreen.QuickSelectExpansions"), ChoiceGroup.MULTIPLE);
		try {
			//style choiceItem 
			this.quickGameRandomizerCG.append(Dominion.instance().getExpansionName(0), Image.createImage("/ba.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Dominion.instance().getExpansionName(1), Image.createImage("/pr.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Dominion.instance().getExpansionName(2), Image.createImage("/in.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Dominion.instance().getExpansionName(3), Image.createImage("/se.png"));
		} catch (IOException e) {
			//style choiceItem 
			this.quickGameRandomizerCG.append(Dominion.instance().getExpansionName(0), null);
			//style choiceItem
			this.quickGameRandomizerCG.append(Dominion.instance().getExpansionName(1), null);
			//style choiceItem
			this.quickGameRandomizerCG.append(Dominion.instance().getExpansionName(2), null);
			//style choiceItem
			this.quickGameRandomizerCG.append(Dominion.instance().getExpansionName(3), null);
		}
		this.quickGameRandomizerCG.addCommand(this.quickRandomizeCardsCmd);
		this.quickGameRandomizerCG.setItemCommandListener(this);
		this.readExpansionSettings();
		this.addCommand(this.quitCmd);
		this.setCommandListener(this);
		this.append(this.quickGameRandomizerCG);
		this.append(this.whatToDoCG);
	}
	
	public void readExpansionSettings() {
		Vector settings = null;
		try {
			settings = new SettingsRecordStorage().readData(Locale.get("rms.file.settings"));
		} catch (RecordStoreFullException e) {
			settings = null;
		} catch (RecordStoreException e) {
			settings = null;
		}
		if ( settings == null ) {
			//#debug info
			System.out.println("SettingsRecordStorage: getExpansions : settings is null");
		} else {
			for ( int i = 0 ; i < settings.size() ; i++ ) {
				if ( settings.elementAt(i).toString().startsWith(Locale.get("rms.expansions")) ) {
					//#debug info
					System.out.println("getExpansions: " + settings.elementAt(i).toString() + " first: " + settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 1, Locale.get("rms.expansions").length() + 2));
					Dominion.instance().getPlayingStates()[0] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 1, Locale.get("rms.expansions").length() + 2).equals("1");
					Dominion.instance().getPlayingStates()[1] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 2, Locale.get("rms.expansions").length() + 3).equals("1");
					Dominion.instance().getPlayingStates()[2] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 3, Locale.get("rms.expansions").length() + 4).equals("1");
					Dominion.instance().getPlayingStates()[3] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 4).equals("1");
				} else if ( settings.elementAt(i).toString().startsWith(Locale.get("rms.expansions.usedcards")) ) {
					Dominion.instance().setCardsUsedForExpansion(0, parseInt(settings.elementAt(i).toString().substring(Locale.get("rms.expansions.usedcards").length() + 1, Locale.get("rms.expansions.usedcards").length() + 2)));
					Dominion.instance().setCardsUsedForExpansion(1, parseInt(settings.elementAt(i).toString().substring(Locale.get("rms.expansions.usedcards").length() + 2, Locale.get("rms.expansions.usedcards").length() + 3)));
					Dominion.instance().setCardsUsedForExpansion(2, parseInt(settings.elementAt(i).toString().substring(Locale.get("rms.expansions.usedcards").length() + 3, Locale.get("rms.expansions.usedcards").length() + 4)));
					Dominion.instance().setCardsUsedForExpansion(3, parseInt(settings.elementAt(i).toString().substring(Locale.get("rms.expansions.usedcards").length() + 4)));
				}
			}
		}
		this.quickGameRandomizerCG.setSelectedFlags(Dominion.instance().getPlayingStates());
		this.setCardsFromExpansion(0, Dominion.instance().getNumberOfExpansionCards()[0]);
		this.setCardsFromExpansion(1, Dominion.instance().getNumberOfExpansionCards()[1]);
		this.setCardsFromExpansion(2, Dominion.instance().getNumberOfExpansionCards()[2]);
		this.setCardsFromExpansion(3, Dominion.instance().getNumberOfExpansionCards()[3]);
	}
	
	public void commandAction(Command cmd, Displayable screen) {
		if ( cmd == this.quickRandomizeCardsCmd ) {
			boolean[] flags = new boolean[4];
			this.quickGameRandomizerCG.getSelectedFlags(flags);
			Dominion.instance().setExpansionPlayingState(flags);
			this.app.showRandomizedCards();
		} else if ( cmd == this.quitCmd ) {
			this.app.quit();
		}
	}
	
	public void commandAction(Command cmd, Item item) {
		if ( cmd == this.quitCmd ) {
			this.commandAction(cmd, this);
		} else if ( item == this.whatToDoCG && cmd == this.selectCmd ) {
			switch ( this.whatToDoCG.getSelectedIndex() ) {
			case 1:
				boolean[] flags = new boolean[4];
				this.quickGameRandomizerCG.getSelectedFlags(flags);
				Dominion.instance().setExpansionPlayingState(flags);
				this.app.showRandomizedCards();
				break;
			case 0:
				this.app.showBlackMarketDeck(this);
				break;
			}
		} else if ( item == this.quickGameRandomizerCG )
			this.commandAction(cmd, this);	
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_NUM0:
			this.setCardsFromExpansion(0);
			break;
		case Canvas.KEY_NUM1:
			this.setCardsFromExpansion(1);
			break;
		case Canvas.KEY_NUM2:
			this.setCardsFromExpansion(2);
			break;
		case Canvas.KEY_NUM3:
			this.setCardsFromExpansion(3);
			break;
		case Canvas.KEY_NUM4:
			this.setCardsFromExpansion(4);
			break;
		case Canvas.KEY_NUM5:
			this.setCardsFromExpansion(5);
			break;
		case Canvas.KEY_NUM6:
			this.setCardsFromExpansion(6);
			break;
		case Canvas.KEY_NUM7:
			this.setCardsFromExpansion(7);
			break;
		case Canvas.KEY_NUM8:
			this.setCardsFromExpansion(8);
			break;
		case Canvas.KEY_NUM9:
			this.setCardsFromExpansion(9);
			break;
		default:
			super.keyPressed(keyCode);
		}
	}
	
	private void setCardsFromExpansion(int numberCards) {
		//#debug info
		System.out.println("trying to set cards from expansion");
		this.setCardsFromExpansion(UiAccess.getFocusedIndex(this.quickGameRandomizerCG), numberCards);
	}
	
	private void setCardsFromExpansion(int expansion, int numberOfCards) {
		if ( -1 < expansion ) {
			//#debug info
			System.out.println("expansion found " + expansion);
			if ( expansion == 1 && numberOfCards > 2 )
				this.app.showAlert(Locale.get("alert.CardsFromExpansion.Promo"));
			else {
				if ( numberOfCards > 0 )
					this.quickGameRandomizerCG.set(expansion, Dominion.instance().getExpansionName(expansion) + " " + numberOfCards, this.quickGameRandomizerCG.getImage(expansion));
				else
					this.quickGameRandomizerCG.set(expansion, Dominion.instance().getExpansionName(expansion), this.quickGameRandomizerCG.getImage(expansion));
				Dominion.instance().setCardsUsedForExpansion(expansion, numberOfCards);
			}
		}
	}
	
	private static int parseInt(String value) {
		return Integer.parseInt(value);		
	}
}
