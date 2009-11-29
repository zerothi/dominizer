

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import de.enough.polish.ui.TableItem;
import de.enough.polish.util.Locale;

public class TableCardForm extends Form implements CommandListener {
	
	private GameApp app = null;
	private TableItem table = null;
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.BACK, 8 );
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	
	public TableCardForm(GameApp app, String title) {
		//#style mainScreen
		super(title);
		this.app = app;
		//#style defaultTable
		table = new TableItem();
		this.addCommand(backCmd);
		this.addCommand(quitCmd);
		table.setSelectionMode(TableItem.SELECTION_MODE_CELL);
		this.append(table);
		this.setCommandListener(this);
	}
	
	public void viewCards(Vector cards) {
		this.table.setDimension(9, cards.size() + 1);
		//#debug
		System.out.println("adding header");
		//#style tableHeading
		table.set(0, 0, Locale.get("table.heading.Name"));
		//#style tableHeading
		table.set(1, 0, Locale.get("table.heading.Expansion"));
		//#style tableHeading
		table.set(2, 0, Locale.get("table.heading.Cost"));
		//#style tableHeading
		table.set(3, 0, Locale.get("table.heading.ActionCard"));
		//#style tableHeading
		table.set(4, 0, Locale.get("table.heading.ReactionCard"));
		//#style tableHeading
		table.set(5, 0, Locale.get("table.heading.TreasuryCard"));
		//#style tableHeading
		table.set(6, 0, Locale.get("table.heading.ReactionCard"));
		//#style tableHeading
		table.set(7, 0, Locale.get("table.heading.AttackCard"));
		//#style tableHeading
		table.set(8, 0, Locale.get("table.heading.DurationCard"));
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
		if ( cmd.equals(backCmd) )
			this.app.returnToMainScreen();
		else if ( cmd.equals(quitCmd) )
			this.app.notifyDestroyed();
	}

}
