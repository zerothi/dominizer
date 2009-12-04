

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
	private Command randomizeCmd = new Command( Locale.get("cmd.Randomize.Show"), Command.SCREEN, 1);
	private Command blackMarketCmd = new Command( Locale.get("cmd.BlackMarket"), Command.SCREEN, 2);
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.BACK, 0);
	//private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	
	
	public ShowCardsForm(GameApp app, Dominion dominion, String title) {
		//#style mainScreen
		super(title);
		this.app = app;
		this.dominion = dominion;
		//#debug
		System.out.println("showing cards initialize");
		//#style defaultTable
		this.table = new TableItem();
		this.addCommand(this.randomizeCmd);
		this.addCommand(this.backCmd);
		this.table.setSelectionMode(TableItem.SELECTION_MODE_CELL);
		this.append(this.table);
		this.setCommandListener(this);
	}
	
	public void reRandomize() {
		this.viewCards(this.dominion.getRandomizedCards());
	}

	public void viewCards(Vector cards) {
		this.removeCommand(blackMarketCmd);
		this.table.setDimension(3, cards.size() + 1);
		//#debug
		System.out.println("adding header");
		//#style tableHeading
		this.table.set(0, 0, Locale.get("table.heading.Name"));
		//#style tableHeading
		this.table.set(1, 0, Locale.get("table.heading.Expansion"));
		//#style tableHeading
		this.table.set(2, 0, Locale.get("table.heading.Cost"));
		//#debug
		System.out.println("adding card information");
		for (int cardNumber = 0 ; cardNumber < cards.size() ; cardNumber++ ) {
			//#style tableCell
			this.table.set(0, cardNumber + 1, ( (Card) cards.elementAt(cardNumber)).getName() );
			if ( ((Card) cards.elementAt(cardNumber)).getName().equals(Locale.get("card.BlackMarket")) )
				this.addCommand(blackMarketCmd);
			try {
				//#style tableCell
				this.table.set(1, cardNumber + 1, new ImageItem(null, 
						Image.createImage("/" + ((Card) cards.elementAt(cardNumber)).getExpansion() + ".png"), ImageItem.PLAIN, null));
			} catch (IOException e) {
				this.table.set(1, cardNumber + 1, ((Card) cards.elementAt(cardNumber)).getExpansion());
			}
			//#style tableCellCentered
			this.table.set(2, cardNumber + 1, new Integer(( (Card) cards.elementAt(cardNumber) ).getCost()) );
		}
	}

	public void keyReleased(int keyCode) {
		this.app.showAlert("Trykket " + keyCode);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(this.backCmd) )
			this.app.changeToScreen(null);
		else if ( cmd.equals(this.randomizeCmd) )
			this.reRandomize();
		else if ( cmd.equals(this.blackMarketCmd) )
			this.app.showBlackMarketDeck(this);
	}
}
