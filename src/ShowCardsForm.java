

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;


import de.enough.polish.ui.TableItem;
import de.enough.polish.util.Locale;

public class ShowCardsForm extends Form  implements CommandListener {
	
	private GameApp app = null;
	private Dominion dominion = null;
	private TableItem table = null;
	private Command randomizeCmd = new Command( Locale.get( "cmd.Randomize" ), Command.ITEM, 5 );
	private Command backCmd = new Command( Locale.get( "cmd.Back" ), Command.BACK, 8 );
	//private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	
	
	public ShowCardsForm(GameApp app, Dominion dominion, String title) {
		//#style mainScreen
		super(title);
		this.app = app;
		this.dominion = dominion;
		//#debug
		System.out.println("showing cards initialize");
		//#style defaultTable
		table = new TableItem();
		this.addCommand(randomizeCmd);
		this.addCommand(backCmd);
		table.setSelectionMode(TableItem.SELECTION_MODE_CELL);
		this.append(table);
		this.setCommandListener(this);
	}
	
	public void reRandomize() {
		this.viewCards(this.dominion.getRandomizedCards());
	}

	public void viewCards(Vector cards) {
		this.table.setDimension(3, cards.size() + 1);
		//#debug
		System.out.println("adding header");
		//#style tableHeading
		table.set(0, 0, Locale.get("table.heading.Name"));
		//#style tableHeading
		table.set(1, 0, Locale.get("table.heading.Expansion"));
		//#style tableHeading
		table.set(2, 0, Locale.get("table.heading.Cost"));
		//#debug
		System.out.println("adding card information");
		for (int cardNumber = 0 ; cardNumber < cards.size() ; cardNumber++ ) {
			//#style tableCell
			table.set(0, cardNumber + 1, ( (Card) cards.elementAt(cardNumber)).getName() );
			try {
				//#style tableCell
				table.set(1, cardNumber + 1, new ImageItem(null, 
						Image.createImage("/" + ((Card) cards.elementAt(cardNumber)).getExpansion() + ".png"), ImageItem.PLAIN, null));
			} catch (IOException e) {
				table.set(1, cardNumber + 1, ((Card) cards.elementAt(cardNumber)).getExpansion());
			}
			//#style tableCellCentered
			table.set(2, cardNumber + 1, new Integer(( (Card) cards.elementAt(cardNumber) ).getCost()) );
		}
	}


	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(backCmd) )
			this.app.returnToMainScreen();
		else if ( cmd.equals(randomizeCmd) )
			this.reRandomize();
	}
}
