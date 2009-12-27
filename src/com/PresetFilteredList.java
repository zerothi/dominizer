package com;

import java.util.Random;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.dominizer.GameApp;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.util.Locale;

public class PresetFilteredList extends FilteredList implements CommandListener {
	private Command selectCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 0);
	private Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.SCREEN, 0);

	public PresetFilteredList(String title, int filterHeight) {
		//#style mainScreen
		super(title, filterHeight);
		this.addCommand(selectCmd);
		this.addCommand(quickRandomizeCardsCmd);
		for ( int i = 0 ; i < Dominion.instance().presetSize() ; i++ )
			this.addPresets(Dominion.instance().getPreset(i));
	}

	public void addPresets(CardPresets cardPreset) {
		for ( int i = 0 ; i < cardPreset.size() ; i++ )
			this.append(cardPreset.getPresetName(i), null);
	}

	public void setPresets(CardPresets cardPreset) {
		this.deleteAll();
		this.addPresets(cardPreset);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd == quickRandomizeCardsCmd ) {
			Random selector = new Random(System.currentTimeMillis());
			this.focus(selector.nextInt(this.size()));
		} else if ( cmd == selectCmd ) {
			ShowCardsForm.instance().viewCards(Dominion.instance().getPreset(this.getString(this.getCurrentIndex())));
			GameApp.changeToScreen(ShowCardsForm.instance());
		}
	}
}
