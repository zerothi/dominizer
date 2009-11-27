package canvas.forms;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import canvas.GameApp;
import de.enough.polish.ui.TableItem;
import de.enough.polish.util.Locale;
import dominion.Card;

public class EditCardRandomizationForm extends Form implements CommandListener {
	
	private GameApp app;
	private TableItem table = null;
	private Command goBackCmd = new Command( Locale.get( "cmd.GoBack" ), Command.BACK, 8 );
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	
	public EditCardRandomizationForm(GameApp app, String title) {
		super(title);
		this.app = app;
		//#style defaultTable
		table = new TableItem();
		table.addCommand(goBackCmd);
		table.addCommand(quitCmd);
		this.append(table);
		table.setSelectionMode(TableItem.SELECTION_MODE_CELL);
	}
	
	public void viewCards(Vector cards) {
		this.table.setDimension(7, cards.size() + 1);
		//#debug
		System.out.println("adding header");
		//#style tableHeading
		table.set(0, 0, "Name");
		//#style tableHeading
		table.set(1, 0, "Exp.");
		//#style tableHeading
		table.set(2, 0, "#");
		//#style tableHeading
		table.set(3, 0, "Action?");
		//#style tableHeading
		table.set(4, 0, "ReAction?");
		//#style tableHeading
		table.set(5, 0, "Treasure?");
		//#style tableHeading
		table.set(6, 0, "Victory?");
		//#debug
		System.out.println("adding card information");
		for (int cardNumber = 0 ; cardNumber < cards.size() ; cardNumber++ ) {
			//#style tableCell
			table.set(0, cardNumber + 1, ( (Card) cards.elementAt(cardNumber)).getName() );
			//#style tableCell
			table.set(1, cardNumber + 1, ( (Card) cards.elementAt(cardNumber)).getGame() );
			//#style tableCellCentered
			table.set(2, cardNumber + 1, new Integer(( (Card) cards.elementAt(cardNumber) ).getCost()) );
			//#style tableCell
			table.set(3, cardNumber + 1, new Boolean(( (Card) cards.elementAt(cardNumber) ).isAction()) );
			//#style tableCell
			table.set(4, cardNumber + 1, new Boolean(( (Card) cards.elementAt(cardNumber) ).isReaction()) );
			//#style tableCell
			table.set(5, cardNumber + 1, new Boolean(( (Card) cards.elementAt(cardNumber) ).isTreasure()) );
			//#style tableCell
			table.set(6, cardNumber + 1, new Boolean(( (Card) cards.elementAt(cardNumber) ).isVictory()) );
		}
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(goBackCmd) )
			this.app.returnToMainScreen();
		else if ( cmd.equals(quitCmd) )
			this.app.notifyDestroyed();
	}

}
