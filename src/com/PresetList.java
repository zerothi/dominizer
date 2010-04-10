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

import de.enough.polish.ui.List;
import de.enough.polish.util.Locale;

public class PresetList extends List implements CommandListener {
	
	private Command selectCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 6);
	private Command infoCmd = new Command( Locale.get("cmd.Preset.ShowInfo"), Command.SCREEN, 2);
	private Command deleteCmd = new Command( Locale.get("cmd.Preset.DeletePreset"), Command.SCREEN, 4);
	private Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 10);
	private int[] tmp = new int[] { 0, 0};
	
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
		setCommandListener(this);
		for ( tmp[0] = 0 ; tmp[0] < Dominion.I().presetSize() ; tmp[0]++ )
			addPresets(Dominion.I().getPreset(tmp[0]));
	}

	public void addPresets(CardPresets cardPreset) {
		if ( cardPreset == null )
			return;
		for ( tmp[1] = 0 ; tmp[1] < cardPreset.size() ; tmp[1]++ ) {
			if ( cardPreset.getExpansion() > -1 ) { 
				//#style label
				append(cardPreset.getPresetName(tmp[1]), Dominion.getExpansionImage(cardPreset.getExpansion()));
			} else {
				//#style label
				append(cardPreset.getPresetName(tmp[1]), null);
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
			tmp[0] = Dominion.I().getPreset(0).size();
			if ( tmp[0] - 1 < getCurrentIndex() )
				tmp[0] += Dominion.I().getPreset(1).size();
			if ( tmp[0] - 1 < getCurrentIndex() ) {
				tmp[0] += Dominion.I().getPreset(2).size();
				if ( Dominion.I().getPreset(3) == null )
					tmp[0] = 0;
			}
			if ( tmp[0] <= getCurrentIndex() )
				tmp[0] = 0;
			focus(tmp[0]);
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
			tmp = Dominion.I().getPresetLocation(getString(getCurrentIndex()));
			if ( tmp[0] > -1 ) 
				GameApp.instance().showInfo(Dominion.I().getPresetAsInfo(tmp[0], tmp[1]), Alert.FOREVER);
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.select")) ) {
			if ( Dominion.I().selectPreset(getString(getCurrentIndex())) ) {}
				// TODO add implementation of preset selection
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
