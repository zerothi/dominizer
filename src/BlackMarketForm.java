import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
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
public class BlackMarketForm extends Form implements CommandListener, ItemCommandListener {

	GameApp app = null;
	Form previousForm = null;
	Vector blackMarketDeck = null;
	ChoiceGroup chooseCard = null;
	Command drawCardsCmd = new Command( Locale.get("cmd.BlackMarket.Draw"), Command.SCREEN, 0);
	Command selectCardCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 1);
	Command backCmd = new Command( Locale.get("cmd.Back"), Command.BACK, 0);
	String randomizeCardHolder = null;
	int currentlyReachedCard = 0;
	int randomize = 0;
	
	/**
	 * @param title
	 */
	public BlackMarketForm(GameApp app, Form previousForm, String title) {
		//#style mainScreen
		super(title);
		this.app = app;
		this.previousForm = previousForm;
		//#style filterCards
		this.chooseCard = new ChoiceGroup(Locale.get("screen.BlackMarket.ChooseCards"), ChoiceGroup.EXCLUSIVE);
		this.chooseCard.addCommand(this.drawCardsCmd);
		this.chooseCard.setItemCommandListener(this);
		this.addCommand(this.backCmd);
		this.append(this.chooseCard);
		this.setCommandListener(this);
	}
	
	public void drawCards() {
		//#debug info
		System.out.println("new drawing");
		this.chooseCard.removeCommand(this.drawCardsCmd);
		this.chooseCard.addCommand(this.selectCardCmd);
		//style choiceItem
		this.chooseCard.append(Locale.get("screen.BlackMarket.ChooseNone"), null);
		this.addNextCard(3);
		if ( 1 < this.blackMarketDeck.size() )
			this.addNextCard(2);
		if ( 2 < this.blackMarketDeck.size() )
			this.addNextCard(1);
		this.randomizeDrawn();
		this.chooseCard.setSelectedIndex(0, true);
	}
		
	private void addNextCard(int remaining) {
		//#debug info
		System.out.println("add next card. remaining: " + remaining);
		if ( this.blackMarketDeck.size() == 0 ) {
			//style choiceItem
			this.chooseCard.deleteAll();
			this.chooseCard.append(Locale.get("screen.BlackMarket.DeckEmpty"), null);
		} else if ( this.blackMarketDeck.size() <= this.currentlyReachedCard ) {
			//#debug info
			System.out.println("size to large. remaining: " + remaining);
			this.currentlyReachedCard = 0;
			if ( remaining != 0 )
				addNextCard(0);
		} else if ( this.currentlyReachedCard < this.blackMarketDeck.size() ) {
			//#debug info
			System.out.println("just added. remaining: " + remaining);
			//style choiceItem
			this.chooseCard.append(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard)).toString(), null);
			this.currentlyReachedCard++;
		}
	}
	
	private void selectCard(int indexChosen) {
		for ( int i = 0 ; i < blackMarketDeck.size() ; i++ ) {
			if ( this.chooseCard.getString(this.chooseCard.getSelectedIndex()).equals(blackMarketDeck.elementAt(i)) ) {
				this.app.showInfo(Locale.get("screen.BlackMarket.InfoMessage") + "\n" + blackMarketDeck.elementAt(i).toString() + ".");
				this.blackMarketDeck.removeElementAt(i);
			}
		}
		this.chooseCard.deleteAll();
		this.chooseCard.removeCommand(this.selectCardCmd);
		this.chooseCard.addCommand(this.drawCardsCmd);
	}
	
	private void randomizeDrawn() {
		if ( this.blackMarketDeck.size() == 0 )
			return;
		switch ( new Random(System.currentTimeMillis()).nextInt(6) ) {
		case 0:
			// This means no switching
			break;
		case 1:
			// This means swap of first and second card
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 1));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3)), this.getIndexCard(this.currentlyReachedCard - 3 + 1));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3));
			break;
		case 2:
			// This means swap of first and third card
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3));
			break;
		case 3:
			// This means swap of second and third card
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 1)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3 + 1));
			break;
		case 4:
			// This means : Put third as first, the first as the second, and the second as third
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3));
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 1)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3 + 1));
			break;
		case 5:
			// This means : Put third as second, the first as the third, and the second as first
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 1)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3 + 1));
			this.randomizeCardHolder = (String) this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.blackMarketDeck.elementAt(this.getIndexCard(this.currentlyReachedCard - 3)), this.getIndexCard(this.currentlyReachedCard - 3 + 2));
			this.blackMarketDeck.setElementAt(this.randomizeCardHolder, this.getIndexCard(this.currentlyReachedCard - 3));
			break;
		}		
	}
	
	private int getIndexCard(int index) {
		if ( index < 0 ) {
			if ( this.blackMarketDeck.size() + index < 0 )
				return 0;
			return this.blackMarketDeck.size() + index;
		} else if ( index < this.blackMarketDeck.size() )
			return index;
		else
			return this.getIndexCard(index - this.blackMarketDeck.size());
	}
	
	public void setBlackMarketDeck(Cards blackMarketDeck) {
		for ( int i = 0 ; i < blackMarketDeck.size() ; i++ )
			this.blackMarketDeck.addElement(blackMarketDeck.getName(i));
		this.currentlyReachedCard = 0;
	}
	

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command cmd, Displayable screen) {
		if ( cmd == this.backCmd )
			this.app.changeToScreen(previousForm);
		else if ( cmd == this.drawCardsCmd )
			this.drawCards();
		else if ( cmd == this.selectCardCmd )
			this.selectCard(this.chooseCard.getSelectedIndex());
	}
	
	public void commandAction(Command cmd, Item item) {
		if ( cmd == this.backCmd )
			this.commandAction(this.backCmd, this);
		else if ( item == this.chooseCard && cmd == this.drawCardsCmd )
			this.drawCards();
		else if ( cmd == this.selectCardCmd )
			this.selectCard(this.chooseCard.getSelectedIndex());
	}
}