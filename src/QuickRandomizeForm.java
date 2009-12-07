import java.io.IOException;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;

import de.enough.polish.util.Locale;

/**
 * 
 */

/**
 * @author nick
 *
 */
public class QuickRandomizeForm extends Form implements CommandListener, ItemCommandListener{
	
	GameApp app = null;
	ChoiceGroup whatToDoCG = null;
	ChoiceGroup quickGameRandomizerCG = null;
	Command selectCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 0);
	Command quickRandomizeCardsCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.SCREEN, 0);
	//Command showCardsListCmd = new Command( Locale.get( "cmd.ShowCards" ), Command.ITEM, 8 );
	Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.BACK, 10);
	boolean[] flags = new boolean[] {true, true, true, false};
	
	public QuickRandomizeForm(GameApp app, String title) {
		//#style mainScreen
		super(title);
		this.app = app;
		//#style choiceGroup
		this.whatToDoCG = new ChoiceGroup(Locale.get("mainScreen.ChoiceWhatToDo"), ChoiceGroup.EXCLUSIVE);
		//#style choiceItem
		this.whatToDoCG.append(Locale.get("cmd.Randomize.Show"), null);
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
			this.quickGameRandomizerCG.append(Locale.get("base"), Image.createImage("/ba.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("promo"), Image.createImage("/pr.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("intrigue"), Image.createImage("/in.png"));
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("seaside"), Image.createImage("/se.png"));
		} catch (IOException e) {
			//style choiceItem 
			this.quickGameRandomizerCG.append(Locale.get("base"), null);
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("promo"), null);
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("intrigue"), null);
			//style choiceItem
			this.quickGameRandomizerCG.append(Locale.get("seaside"), null);
		}
		this.quickGameRandomizerCG.addCommand(this.quickRandomizeCardsCmd);
		this.quickGameRandomizerCG.setItemCommandListener(this);
		this.quickGameRandomizerCG.setSelectedFlags(flags);
		this.addCommand(this.quitCmd);
		this.setCommandListener(this);
		this.append(this.whatToDoCG);
		this.append(this.quickGameRandomizerCG);	
	}
	
	public boolean isEmptySelection() {
		return (0 == this.quickGameRandomizerCG.getSelectedFlags(flags));
	}
	
	public boolean isOnlyPromoSelection() {
		return (1 == this.quickGameRandomizerCG.getSelectedFlags(flags) && this.quickGameRandomizerCG.getSelectedIndex() == 1);
	}
	
	public boolean[] getExpansionFlags() {
		this.quickGameRandomizerCG.getSelectedFlags(flags);
		return flags;
	}
	
	public void commandAction(Command cmd, Displayable screen) {
		//showAlert("Cmd: " + cmd.getLabel() + ". Screen: " + screen.getTitle());
		if ( cmd == this.quickRandomizeCardsCmd ) {
			this.app.showRandomizedCards(getExpansionFlags(), isEmptySelection(), isOnlyPromoSelection());
		} else if ( cmd == this.quitCmd ) {
			this.app.quit();
		}
	}
	
	public void commandAction(Command cmd, Item item) {
		if ( cmd == this.quitCmd ) {
			this.app.quit();
		} else if ( item == this.whatToDoCG && cmd == this.selectCmd ) {
			switch ( this.whatToDoCG.getSelectedIndex() ) {
			case 0:
				this.app.showRandomizedCards(getExpansionFlags(), isEmptySelection(), isOnlyPromoSelection());
				break;
			case 1:
				this.app.showBlackMarketDeck(this);
				break;
			}
		} else if ( item == this.quickGameRandomizerCG )
			this.commandAction(cmd, this);	
	}
}
