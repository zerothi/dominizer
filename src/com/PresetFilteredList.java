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
		this.addCommand(selectCmd);
		this.addCommand(infoCmd);
		this.addCommand(quickRandomizeCardsCmd);
		this.addCommand(quitCmd);
		this.setCommandListener(this);
		for ( int i = 0 ; i < Dominion.I().presetSize() ; i++ )
				this.addPresets(Dominion.I().getPreset(i), i == Dominion.I().presetSize() - 1);
	}

	public void addPresets(CardPresets cardPreset, boolean isUser) {
		if ( cardPreset == null )
			return;
		for ( int i = 0 ; i < cardPreset.size() ; i++ ) {
			//#style label
			this.append(cardPreset.getPresetName(i), null);
			if ( isUser )
				this.getItem(this.size() - 1).addCommand(deleteCmd);
		}
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_STAR:
			if (this.getCurrentIndex() + 3 < this.size() )
				this.focus(this.getCurrentIndex() + 3);
			else
				this.focus(0);
			break;
		case Canvas.KEY_POUND:
			tmp = Dominion.I().getPreset(0).size();
			if ( tmp - 1 < this.getCurrentIndex() )
				tmp += Dominion.I().getPreset(1).size();
			if ( tmp - 1 < this.getCurrentIndex() ) {
				tmp += Dominion.I().getPreset(2).size();
				if ( Dominion.I().getPreset(3) == null )
					tmp = 0;
			}
			if ( tmp <= this.getCurrentIndex() )
				tmp = 0;
			this.focus(tmp);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(quickRandomizeCardsCmd) ) {
			this.focus((new Random(System.currentTimeMillis())).nextInt(this.size()));
		} else if ( cmd.equals(infoCmd) ) {
			tmp = Dominion.I().getPreset(0).size();
			if ( this.getCurrentIndex() < tmp ) {
				GameApp.instance().showInfo(Dominion.I().getPresetAsInfo(0, this.getCurrentIndex()), Alert.FOREVER);
				return;
			}
			tmp += Dominion.I().getPreset(1).size();
			if ( this.getCurrentIndex() < tmp ) {
				tmp = Dominion.I().getPreset(0).size();
				GameApp.instance().showInfo(Dominion.I().getPresetAsInfo(1, this.getCurrentIndex() - tmp), Alert.FOREVER);
				return;
			}
			tmp += Dominion.I().getPreset(2).size();
			if ( this.getCurrentIndex() < tmp ) {
				tmp = Dominion.I().getPreset(1).size() + Dominion.I().getPreset(0).size();
				GameApp.instance().showInfo(Dominion.I().getPresetAsInfo(2, this.getCurrentIndex() - tmp), Alert.FOREVER);
				return;
			}
			tmp += Dominion.I().getPreset(3).size();
			if ( this.getCurrentIndex() < tmp ) {
				tmp = Dominion.I().getPreset(2).size() + Dominion.I().getPreset(1).size() + Dominion.I().getPreset(0).size();
				GameApp.instance().showInfo(Dominion.I().getPresetAsInfo(3, this.getCurrentIndex() - tmp), Alert.FOREVER);
				return;
			}
		} else if ( cmd.equals(selectCmd) ) {
			if ( Dominion.I().selectPreset(this.getString(this.getCurrentIndex())) )
				GameApp.instance().showCurrentSelectedCards();
		} else if ( cmd.equals(quitCmd) ) {
			GameApp.instance().quit();
		} else if ( cmd.equals(deleteCmd) ) {
			SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.preset"));
			SettingsRecordStorage.instance().deleteData(this.getString(this.getCurrentIndex()));
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
			this.focus(this.getCurrentIndex() - 1);
			this.delete(this.getCurrentIndex() + 1);
			GameApp.instance().changeToTab(GameApp.TAB_QUICK);
			GameApp.instance().changeToTab(GameApp.TAB_PRESET);
		}
	}
}
