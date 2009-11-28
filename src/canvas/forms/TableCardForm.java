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

public class TableCardForm extends Form implements CommandListener {
	
	private GameApp app = null;
	private TableItem table = null;
	private Command goBackCmd = new Command( Locale.get( "cmd.GoBack" ), Command.BACK, 8 );
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	
	public TableCardForm(GameApp app, String title) {
		//#style mainScreen
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
		this.table.setDimension(9, cards.size() + 1);
		//#debug
		System.out.println("adding header");
		//#style tableHeading
		table.set(0, 0, "Name");
		//#style tableHeading
		table.set(1, 0, "Exp.");
		//#style tableHeading
		table.set(2, 0, "#");
		//#style tableHeading
		table.set(3, 0, "A");
		//#style tableHeading
		table.set(4, 0, "R");
		//#style tableHeading
		table.set(5, 0, "T");
		//#style tableHeading
		table.set(6, 0, "V");
		//#style tableHeading
		table.set(7, 0, "A");
		//#style tableHeading
		table.set(8, 0, "D");
		//#debug
		System.out.println("adding card information");
		for (int cardNumber = 0 ; cardNumber < cards.size() ; cardNumber++ ) {
			//#style tableCell
			table.set(0, cardNumber + 1, ( (Card) cards.elementAt(cardNumber)).getName() );
			//#style tableCell
			table.set(1, cardNumber + 1, ( (Card) cards.elementAt(cardNumber)).getExpansion() );
			//#style tableCellCentered
			table.set(2, cardNumber + 1, new Integer(( (Card) cards.elementAt(cardNumber) ).getCost()) );
			//#style tableCell
			table.set(3, cardNumber + 1, ( (Card) cards.elementAt(cardNumber) ).isAction() == true ? "Y" : "N" );
			//#style tableCell
			table.set(4, cardNumber + 1, ( (Card) cards.elementAt(cardNumber) ).isReaction() == true ? "Y" : "N" );
			//#style tableCell
			table.set(5, cardNumber + 1, ( (Card) cards.elementAt(cardNumber) ).isTreasure() == true ? "Y" : "N" );
			//#style tableCell
			table.set(6, cardNumber + 1, ( (Card) cards.elementAt(cardNumber) ).isVictory() == true ? "Y" : "N" );
			//#style tableCell
			table.set(7, cardNumber + 1, ( (Card) cards.elementAt(cardNumber) ).isAttack() == true ? "Y" : "N" );
			//#style tableCell
			table.set(8, cardNumber + 1, ( (Card) cards.elementAt(cardNumber) ).isDuration() == true ? "Y" : "N" );
		}
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(goBackCmd) )
			this.app.returnToMainScreen();
		else if ( cmd.equals(quitCmd) )
			this.app.notifyDestroyed();
	}

}
