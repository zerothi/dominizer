package com;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.util.Locale;

public class PresetFilteredList extends FilteredList implements CommandListener {
	Command selectCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 0);
	Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.SCREEN, 0);
	
	public PresetFilteredList(String title, int filterHeight) {
		//#style mainScreen
		super(title, filterHeight);
		this.addCommand(selectCmd);
		this.addCommand(quickRandomizeCardsCmd);
		for ( int i = 0 ; i < Dominion.instance().presetSize() ; i++ ) {
			this.addPresets(Dominion.instance().getPreset(i));
		}
	}
	
	public void addPresets(CardPresets cardPreset) {
		for ( int i = 0 ; i < cardPreset.size() ; i++ ) {
			this.append(cardPreset.getPresetName(i), null);
		}
	}
	public void setPresets(CardPresets cardPreset) {
		this.deleteAll();
		this.addPresets(cardPreset);
	}
	
	public void commandAction(Command cmd, Displayable disp) {
	    if ( cmd == quickRandomizeCardsCmd ) {
		Random selector = new Random(System.currentTimeMillis());
		//TODO set selected cursor! Probably UIaccess
		//this.setCursor(selector.nextInt(this.getSize());
	    } else if ( cmd == selectCmd ) {
		ShowCardsForm.instance().viewCards(Dominion.instance().getPreset(this.getSelected()));
		GameApp.changeToScreen(ShowCardsForm.instance());
	    }
	}
}
