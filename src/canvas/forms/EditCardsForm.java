package canvas.forms;

import java.util.Vector;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;

import canvas.GameApp;
import de.enough.polish.ui.Choice;
import de.enough.polish.util.Locale;
import dominion.Card;

public class EditCardsForm extends Form implements CommandListener, ItemStateListener {
	
	private GameApp app = null;
	private Object[][] cards = null;
	private ChoiceGroup cardGroup= null;
	private Command goBackCmd = new Command( Locale.get( "cmd.GoBack" ), Command.BACK, 8 );
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.EXIT, 10 );

	public EditCardsForm(GameApp app, String title) {
		//#style mainScreen
		super(title);
		this.app = app;
		//#style choiceGroup
		this.cardGroup = new ChoiceGroup("Card:", Choice.EXCLUSIVE);// doesn't work yet with multiple
		this.addCommand(goBackCmd);
		this.addCommand(quitCmd);
		this.setCommandListener(this);
		this.setItemStateListener(this);
	}

	public void setCards(Vector cardsV) {
		this.cards = new Object[11][cardsV.size()];
		this.cardGroup.deleteAll();
		for ( int cardNumber = 0 ; cardNumber < cardsV.size() ; cardNumber++ ) {
			this.cards[0][cardNumber] = ( (Card)cardsV.elementAt(cardNumber)).getName();
			// TODO : set the style of this according to the expansion. Thus setting an image for each!!!
			this.cardGroup.append((String)this.cards[0][cardNumber], null);
			this.cards[1][cardNumber] = ( (Card)cardsV.elementAt(cardNumber)).getExpansion();
			this.cards[2][cardNumber] = new Integer(( (Card)cardsV.elementAt(cardNumber)).getCost());
			this.cards[3][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isAction());
			this.cards[4][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isVictory());
			this.cards[5][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isTreasure());
			this.cards[6][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isAttack());
			this.cards[7][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isReaction());
			this.cards[8][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isDuration());
			this.cards[9][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isSelected());
			this.cards[10][cardNumber] = new Boolean(( (Card)cardsV.elementAt(cardNumber)).isBlackMarketSelected());
		}
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(goBackCmd) )
			this.app.returnToMainScreen();
		else if ( cmd.equals(quitCmd) )
			this.app.notifyDestroyed();

	}

	public void itemStateChanged(Item item) {
		this.itemStateChanged(item, null);
	}
	
	public void itemStateChanged(Item item, Displayable disp) {
		// TODO Auto-generated method stub
		if ( item.equals(this.cardGroup) ) {
			this.delete(1);
			this.delete(2);
			this.append((String)this.cards[0][0]);
		}
			
	}

	

}
