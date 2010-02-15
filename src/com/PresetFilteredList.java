package com;

import java.util.Random;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.dominizer.GameApp;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.util.Locale;

public class PresetFilteredList extends FilteredList implements CommandListener {
	private Command selectCmd = new Command( Locale.get("polish.command.select"), Command.OK, 0);
	private Command infoCmd = new Command( Locale.get("cmd.Preset.ShowInfo"), Command.SCREEN, 1);
	private Command deleteCmd = new Command( Locale.get("cmd.Preset.DeletePreset"), Command.SCREEN, 9);
	private Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 10);
	private int tmp;
	
	public PresetFilteredList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		addCommand(selectCmd);
		addCommand(infoCmd);
		addCommand(quickRandomizeCardsCmd);
		addCommand(quitCmd);
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
			tmp = Dominion.I().getPreset(0).size();
			if ( tmp - 1 < getCurrentIndex() )
				tmp += Dominion.I().getPreset(1).size();
			if ( tmp - 1 < getCurrentIndex() ) {
				tmp += Dominion.I().getPreset(2).size();
				if ( Dominion.I().getPreset(3) == null )
					tmp = 0;
			}
			if ( tmp <= getCurrentIndex() )
				tmp = 0;
			focus(tmp);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(quickRandomizeCardsCmd) ) {
			focus((new Random(System.currentTimeMillis())).nextInt(size()));
		} else if ( cmd.equals(infoCmd) ) {
			tmp = Dominion.I().getPreset(0).size();
			if ( getCurrentIndex() < tmp ) {
				GameApp.instance().showInfo(Dominion.I().getPresetAsInfo(0, getCurrentIndex()), Alert.FOREVER);
				return;
			}
			tmp += Dominion.I().getPreset(1).size();
			if ( getCurrentIndex() < tmp ) {
				tmp = Dominion.I().getPreset(0).size();
				GameApp.instance().showInfo(Dominion.I().getPresetAsInfo(1, getCurrentIndex() - tmp), Alert.FOREVER);
				return;
			}
			tmp += Dominion.I().getPreset(2).size();
			if ( getCurrentIndex() < tmp ) {
				tmp = Dominion.I().getPreset(1).size() + Dominion.I().getPreset(0).size();
				GameApp.instance().showInfo(Dominion.I().getPresetAsInfo(2, getCurrentIndex() - tmp), Alert.FOREVER);
				return;
			}
			tmp += Dominion.I().getPreset(3).size();
			if ( getCurrentIndex() < tmp ) {
				tmp = Dominion.I().getPreset(2).size() + Dominion.I().getPreset(1).size() + Dominion.I().getPreset(0).size();
				GameApp.instance().showInfo(Dominion.I().getPresetAsInfo(3, getCurrentIndex() - tmp), Alert.FOREVER);
				return;
			}
		} else if ( cmd.equals(selectCmd) ) {
			if ( Dominion.I().selectPreset(getString(getCurrentIndex())) )
				GameApp.instance().showCurrentSelectedCards();
		} else if ( cmd.equals(quitCmd) ) {
			GameApp.instance().quit();
		} else if ( cmd.equals(deleteCmd) ) {
			SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.preset"));
			SettingsRecordStorage.instance().deleteData(getString(getCurrentIndex()));
			try {
				SettingsRecordStorage.instance().writeData();
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
			SettingsRecordStorage.instance().closeRecord();
			focus(getCurrentIndex() - 1);
			delete(getCurrentIndex() + 1);
			GameApp.instance().changeToTab(GameApp.TAB_QUICK);
			GameApp.instance().changeToTab(GameApp.TAB_PRESET);
		}
	}
}
