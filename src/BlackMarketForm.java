import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import de.enough.polish.util.Locale;

/**
 * 
 */

/**
 * @author nick
 *
 */
public class BlackMarketForm extends Form implements CommandListener {

	GameApp app = null;
	Vector blackMarketDeck = null;
	ChoiceGroup chooseCard = null;
	private Command drawCardsCmd = new Command( Locale.get("cmd.BlackMarket.Draw"), Command.SCREEN, 1);
	private Command selectCardCmd = new Command( Locale.get("polish.command.select"), Command.SCREEN, 2);
	private Command backCmd = new Command( Locale.get("cmd.Back"), Command.BACK, 0);
	int currentlyReachedCard = 0;
	
	/**
	 * @param title
	 */
	public BlackMarketForm(GameApp app, String title) {
		super(title);
		this.app = app;
		//#style choiceGroup
		this.chooseCard = new ChoiceGroup(Locale.get("screen.BlackMarket.ChooseCards"), ChoiceGroup.EXCLUSIVE);
		this.addCommand(this.drawCardsCmd);
		this.addCommand(this.backCmd);
		this.append(this.chooseCard);
		this.setCommandListener(this);
	}
	
	public void drawCards() {
		this.removeCommand(this.drawCardsCmd);
		this.addCommand(this.selectCardCmd);
		this.addNextCard();
		this.addNextCard();
		this.addNextCard();
		this.chooseCard.setSelectedIndex(0, true);
	}
		
	private void addNextCard() {
		if ( this.blackMarketDeck.size() == 0 ) {
			//#style choiceItem
			this.chooseCard.append(Locale.get("screen.BlackMarket.DeckEmpty"), null);
		} else if ( this.blackMarketDeck.size() <= this.currentlyReachedCard ) {
			this.currentlyReachedCard = 0;
			addNextCard();
		} else if ( this.currentlyReachedCard < this.blackMarketDeck.size() ) {
			//#style choiceItem
			this.chooseCard.append((String) this.blackMarketDeck.elementAt(this.currentlyReachedCard), null);
			this.currentlyReachedCard++;
		}
	}
	
	private void selectCard(int indexChosen) {
		for ( int i = 0 ; i < blackMarketDeck.size() ; i++ ) {
			if ( this.chooseCard.getString(this.chooseCard.getSelectedIndex()).equals((String) blackMarketDeck.elementAt(i)) ) {
				this.app.showInfo(Locale.get("screen.BlackMarket.InfoMessage") + blackMarketDeck.elementAt(i).toString() + ".");
				this.blackMarketDeck.removeElementAt(i);
			}
		}
		this.chooseCard.deleteAll();
		this.removeCommand(this.selectCardCmd);
		this.addCommand(this.drawCardsCmd);
	}
	
	public void setBlackMarketDeck(Vector cards) {
		this.currentlyReachedCard = 0;
		for ( int i = 0 ; i < cards.size() ; i++ ) 
			if ( !( (Card)cards.elementAt(i) ).isPlaying() &  ( (Card)cards.elementAt(i) ).isBlackMarketAvailable() )
				this.currentlyReachedCard++;
		if ( this.blackMarketDeck == null )
			this.blackMarketDeck = new Vector(this.currentlyReachedCard);
		else {
			this.blackMarketDeck.removeAllElements();
			this.blackMarketDeck.ensureCapacity(this.currentlyReachedCard);
		}
		for ( int i = 0 ; i < this.currentlyReachedCard ; i++ )
			this.blackMarketDeck.addElement(null);
		int randomized = 0;
		Random selector = new Random(System.currentTimeMillis());
		for ( int i = 0 ; i < cards.size() ; i++ )
			if ( !( (Card)cards.elementAt(i) ).isPlaying() &  ( (Card)cards.elementAt(i) ).isBlackMarketAvailable() ) {
				randomized = selector.nextInt(this.currentlyReachedCard);
				while ( this.blackMarketDeck.elementAt(randomized) != null )
					randomized = selector.nextInt(this.currentlyReachedCard);
				this.blackMarketDeck.setElementAt(((Card)cards.elementAt(i)).getName(), randomized);
			}
		selector = null;
		this.currentlyReachedCard = 0;
	}
	

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command cmd, Displayable screen) {
		if ( cmd.equals(this.backCmd) )
			this.app.returnToMainScreen();
		else if ( cmd.equals(this.drawCardsCmd) )
			this.drawCards();
		else if ( cmd.equals(this.selectCardCmd) )
			this.selectCard(this.chooseCard.getSelectedIndex());
	}
}
