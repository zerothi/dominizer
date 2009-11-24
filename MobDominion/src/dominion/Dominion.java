package dominion;

import java.util.Random;
import java.util.Vector;

public class Dominion {
	
	private Vector cards = null;
	private int selectCards = 10;
	
	public Dominion() {
		readCards();
	}
	
	/**
	 * @return the selectCards
	 */
	public int getSelectCards() {
		return selectCards;
	}

	/**
	 * @param selectCards the selectCards to set
	 */
	public void setSelectCards(int selectCards) {
		this.selectCards = selectCards;
	}
	
	public Vector randomizeCards() {
		Vector selectedCards = new Vector(selectCards);
		int totalAvailable = cards.size();
		int selectedElement = 0;
		Random selector = new Random();
		for (int i = 0 ; i < selectCards ; i++ ) {
			selectedElement = (int) ((selector.nextFloat() * (totalAvailable - i)) % (totalAvailable - i));
			selectedCards.addElement(cards.elementAt(selectedElement));
			cards.removeElementAt(selectedElement);
		}
		return selectedCards;
	}
	
	private void readCards() {
		if ( cards == null )
			cards = new Vector();
		cards.ensureCapacity(25);
		cards.addElement(new Card("Adventurer","Base",6));
		cards.addElement(new Card("Bureaucrat","Base",4));
		cards.addElement(new Card("Cellar","Base",2));
		cards.addElement(new Card("Chancellor","Base",3));
		cards.addElement(new Card("Chapel","Base",2));
		cards.addElement(new Card("Council Room","Base",5));
		cards.addElement(new Card("Feast","Base",4));
		cards.addElement(new Card("Festival","Base",5));
		cards.addElement(new Card("Gardens","Base",4));
		cards.addElement(new Card("Laboratory","Base",5));
		cards.addElement(new Card("Library","Base",5));
		cards.addElement(new Card("Market","Base",5));
		cards.addElement(new Card("Militia","Base",4));
		cards.addElement(new Card("Mine","Base",5));
		cards.addElement(new Card("Moat","Base",2));
		cards.addElement(new Card("Moneylender","Base",4));
		cards.addElement(new Card("Remodel","Base",4));
		cards.addElement(new Card("Smithy","Base",4));
		cards.addElement(new Card("Spy","Base",4));
		cards.addElement(new Card("Thief","Base",4));
		cards.addElement(new Card("Throne Room","Base",4));
		cards.addElement(new Card("Village","Base",3));
		cards.addElement(new Card("Witch","Base",5));
		cards.addElement(new Card("Woodcutter","Base",3));
		cards.addElement(new Card("Workshop","Base",3));
	}
}
