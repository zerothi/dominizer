

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;


import de.enough.polish.ui.TableItem;
import de.enough.polish.util.Locale;

public class ShowCardsForm extends Form  implements CommandListener {
	
	private GameApp app = null;
	private TableItem table = null;
	private Command goBackCmd = new Command( Locale.get( "cmd.GoBack" ), Command.BACK, 8 );
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );
	
	
	public ShowCardsForm(GameApp app, String title) {
		//#style mainScreen
		super(title);
		this.app = app;
		//#debug
		System.out.println("showing cards initialize");
		//#style defaultTable
		table = new TableItem();
		this.addCommand(goBackCmd);
		this.addCommand(quitCmd);
		table.setSelectionMode(TableItem.SELECTION_MODE_CELL);
		this.append(table);
		this.setCommandListener(this);
	}

	public void viewCards(Vector cards) {
		this.table.setDimension(3, cards.size() + 1);
		//#debug
		System.out.println("adding header");
		//#style tableHeading
		table.set(0, 0, "Name");
		//#style tableHeading
		table.set(1, 0, "Exp.");
		//#style tableHeading
		table.set(2, 0, "#");
		//#debug
		System.out.println("adding card information");
		for (int cardNumber = 0 ; cardNumber < cards.size() ; cardNumber++ ) {
			//#style tableCell
			table.set(0, cardNumber + 1, ( (Card) cards.elementAt(cardNumber)).getName() );
			//#style tableCell
			table.set(1, cardNumber + 1, ( (Card) cards.elementAt(cardNumber)).getExpansion() );
			//#style tableCellCentered
			table.set(2, cardNumber + 1, new Integer(( (Card) cards.elementAt(cardNumber) ).getCost()) );
		}
		//table.setSelectionMode( TableItem.SELECTION_MODE_ROW | TableItem.SELECTION_MODE_INTERACTIVE );
	}


	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(goBackCmd) )
			this.app.returnToMainScreen();
		else if ( cmd.equals(quitCmd) )
			this.app.notifyDestroyed();
	}
}
