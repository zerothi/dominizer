package com;

import java.util.Random;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.dominizer.GameApp;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.util.Locale;

public class PresetFilteredList extends FilteredList implements CommandListener {
	private Command selectCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 0);
	private Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.BACK, 0);

	public PresetFilteredList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		this.addCommand(selectCmd);
		this.addCommand(quickRandomizeCardsCmd);
		this.setCommandListener(this);
		for ( int i = 0 ; i < Dominion.instance().presetSize() ; i++ )
			this.addPresets(Dominion.instance().getPreset(i));
	}

	public void addPresets(CardPresets cardPreset) {
		if ( cardPreset == null )
			return;
		for ( int i = 0 ; i < cardPreset.size() ; i++ )
			this.append(cardPreset.getPresetName(i), null);
	}

	public void setPresets(CardPresets cardPreset) {
		this.deleteAll();
		this.addPresets(cardPreset);
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_POUND:
		case Canvas.KEY_STAR:
			if ( this.getCurrentIndex() < Dominion.instance().getPreset(0).size() )
				this.focus(Dominion.instance().getPreset(0).size());
			else if ( this.getCurrentIndex() < 
					Dominion.instance().getPreset(0).size() + Dominion.instance().getPreset(1).size() )
				this.focus(Dominion.instance().getPreset(0).size() + Dominion.instance().getPreset(1).size());
			else if ( this.getCurrentIndex() < 
					Dominion.instance().getPreset(0).size() + Dominion.instance().getPreset(1).size()
					+ Dominion.instance().getPreset(2).size() )
				this.focus(Dominion.instance().getPreset(0).size() + Dominion.instance().getPreset(1).size() + Dominion.instance().getPreset(2).size());
			else
				this.focus(0);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd == quickRandomizeCardsCmd ) {
			this.focus((new Random(System.currentTimeMillis())).nextInt(this.size()));
		} else if ( cmd == selectCmd ) {
			ShowCardsForm.instance().viewCards(Dominion.instance().getPreset(this.getString(this.getCurrentIndex())));
			GameApp.instance().changeToScreen(ShowCardsForm.instance());
		}
	}
}
