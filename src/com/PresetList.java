package com;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.dominizer.GameApp;
import com.util.CardPresets;
import com.util.Dominion;
import com.util.DominionException;
import com.util.Rand;
import com.util.SettingsRecordStorage;

import de.enough.polish.ui.List;
import de.enough.polish.util.Locale;

public class PresetList extends List implements CommandListener {
	
	private Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	
	private Command infoCmd = new Command( Locale.get("cmd.Preset.ShowInfo"), Command.SCREEN, 2);
	private Command selectCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 6);
	private Command deleteCmd = new Command( Locale.get("cmd.Preset.DeletePreset"), Command.SCREEN, 7);
	private Command gotoCmd = new Command( Locale.get("cmd.Goto.RandomizeSets"), Command.SCREEN, 8);
	
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 10);
	
	public PresetList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		//#if !polish.android
			addCommand(selectCmd);
			//#= setSelectCommand(selectCmd);
		//#endif
		addCommand(infoCmd);
		addCommand(quickRandomizeCardsCmd);
		addCommand(quitCmd);
		addCommand(gotoCmd);
		setCommandListener(this);
		for ( int i = 0 ; i < Dominion.I().presetSize() ; i++ )
			addPresets(Dominion.I().getPreset(i));
	}

	public void addPresets(CardPresets cardPreset) {
		if ( cardPreset == null )
			return;
		for ( int i = 0 ; i < cardPreset.size() ; i++ ) {
			if ( cardPreset.getExpansion() > -1 ) { 
				//#style label
				append(cardPreset.getPresetName(i), Dominion.getExpansionImage(cardPreset.getExpansion()));
			} else {
				//#style label
				append(cardPreset.getPresetName(i), null);
				getItem(size() - 1).addCommand(deleteCmd);
			}
		}
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_STAR:
			if (getCurrentIndex() + 3 < size() )
				focus(getCurrentIndex() + 3);
			else
				focus(0);
			break;
		case Canvas.KEY_POUND:
			int tmp = 0;
			for ( int i = 0 ; i < Dominion.I().presetSize() - 1 ; i++ ) {
				tmp += Dominion.I().getPreset(i).size();
				if ( tmp > getCurrentIndex() && tmp < size() ) {
					focus(tmp);
					tmp = 10000;
					break;
				}
			}
			if ( getCurrentIndex() >= tmp | tmp == size() )
				focus(0);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(quickRandomizeCardsCmd) ) {
			Rand.resetSeed();
			focus(Rand.randomInt(size()));
		} else if ( cmd.equals(infoCmd) ) {
			/*if ( getFilterText().length() != 0 ) {
				GameApp.instance().showAlert(Locale.get("alert.Filter.Availability"));
				return;
			}*/
			int[] tmp = Dominion.I().getPresetLocation(getString(getCurrentIndex()));
			if ( tmp[0] > -1 ) 
				GameApp.instance().showInfo(Dominion.I().getPresetAsInfo(tmp[0], tmp[1]), Alert.FOREVER);
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.select")) ) {
			try {
				Dominion.I().selectPreset(-1, getString(getCurrentIndex()));
				ShowCardsForm.instance().addNewCards(Dominion.I().getSelectedCards(Dominion.I().getCurrentSet()));
				GameApp.instance().changeToScreen(ShowCardsForm.instance());
			} catch (DominionException e) {
				GameApp.instance().showAlert(e.toString());
			}
		} else if ( cmd.equals(gotoCmd) ) {
			if ( Dominion.I().getCurrentSet() > 0 )
				GameApp.instance().changeToScreen(ShowCardsForm.instance());
			else
				GameApp.instance().showInfo(Locale.get("info.randomized.Sets.NoneCreated"), Alert.FOREVER);
		} else if ( cmd.equals(quitCmd) ) {
			GameApp.instance().quit();
		} else if ( cmd.equals(deleteCmd) ) {
			SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.preset"));
			SettingsRecordStorage.instance().deleteData(getString(getCurrentIndex()));
			try {
				SettingsRecordStorage.instance().writeData();
			} catch (RecordStoreFullException e) {
				// TODO Add GameApp.showAlert!
			} catch (RecordStoreNotFoundException e) {
				// TODO Add GameApp.showAlert!
			} catch (RecordStoreException e) {
				// TODO Add GameApp.showAlert!
			}
			SettingsRecordStorage.instance().closeRecord();
			focus(getCurrentIndex() - 1);
			delete(getCurrentIndex() + 1);
			GameApp.instance().changeToTab(GameApp.TAB_QUICK);
			GameApp.instance().changeToTab(GameApp.TAB_PRESET);
		}
	}
}
