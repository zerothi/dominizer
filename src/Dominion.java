

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

public class Dominion {
	
	private Vector cards = null;
	private int numberOfRandomCards = 10;
	
	public Dominion() {
		readCards();
	}
	
	public void setExpansionPlayingState(String expansion, boolean isAvailable) {
		for (int i = 0 ; i < cards.size() ; i++ ) {
			if ( ( (Card)cards.elementAt(i)).getExpansion().equals(expansion) ) { 
				( (Card)cards.elementAt(i)).setAvailable(isAvailable);
				( (Card)cards.elementAt(i)).setBlackMarketAvailable(isAvailable);
			}
		}
	}
	/**
	 * @return the numberOfRandomCards
	 */
	public int getNumberOfRandomCards() {
		return numberOfRandomCards;
	}

	/**
	 * @param numberOfRandomCards the number of cards that should be randomized to set
	 */
	public void setNumberOfRandomCards(int numberOfRandomCards) {
		this.numberOfRandomCards = numberOfRandomCards;
	}
	
	public Vector getRandomizedCards() {
		Vector selectedCards = new Vector(numberOfRandomCards);
		int totalAvailable = cards.size();
		int selectedElement = 0;
		Random selector = new Random(System.currentTimeMillis());
		int i = 0;
		while ( i < numberOfRandomCards ) {
			selectedElement = selector.nextInt(totalAvailable - i);
			if ( ( (Card)cards.elementAt(selectedElement)).isAvailable() == true && !selectedCards.contains(cards.elementAt(selectedElement))) {
				((Card)cards.elementAt(selectedElement)).setPlaying(true);
				selectedCards.addElement(cards.elementAt(selectedElement));
				i++;
			}
		}
		selectedCards.trimToSize();
		selector = null;
		return selectedCards;
	}
	
	public Vector getAllCards() {
		return cards;
	}
	
	public void resetIsPlaying() {
		for ( int i = 0 ; i < cards.size() ; i++ )
			((Card)cards.elementAt(i)).setPlaying(false);
	}
	
	private void readCards() {
		//#debug
		System.out.println("reading all cards");
		readResource("c");
	}
	
	private Card processCardInformation(String information) {
		Card card = new Card();
		int start = 0;
		card.setName(information.substring(start, information.indexOf(":", start)));
		start = information.indexOf(":", start) + 1;
		card.setExpansion(information.substring(start, information.indexOf(":", start)));
		start = information.indexOf(":", start) + 1;
		card.setCost(Integer.parseInt(information.substring(start, information.indexOf(":", start))));
		start = information.indexOf(":", start) + 1;
		card.setAction(isTrue(information.substring(start, information.indexOf(":", start))));
		start = information.indexOf(":", start) + 1;
		card.setVictory(isTrue(information.substring(start, information.indexOf(":", start))));
		start = information.indexOf(":", start) + 1;
		card.setTreasure(isTrue(information.substring(start, information.indexOf(":", start))));
		start = information.indexOf(":", start) + 1;
		card.setAction(isTrue(information.substring(start, information.indexOf(":", start))));
		start = information.indexOf(":", start) + 1;
		card.setReaction(isTrue(information.substring(start, information.indexOf(":", start))));
		start = information.indexOf(":", start) + 1;
		card.setDuration(isTrue(information.substring(start, information.indexOf(";", start))));
		return card;
	}
	
	private boolean isTrue(String test) {
		return test.equals("0") ? false : true;
	}
	
	private void readResource(String file) {
		StringBuffer sb = new StringBuffer();
		try {
		InputStream is = this.getClass().getResourceAsStream(file);
		int len = 0;
		byte[] data = new byte[64];
		try {
			while ( ( len = is.read(data) ) != -1 ) {
				sb.append(new String(data,0,len));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//#debug
			System.out.println("Ioexception");
		}
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//#debug
			System.out.println("Ioexception");
		}
		} catch (IllegalArgumentException e) {
			//#debug
			System.out.println("IllegalArgument" + e.toString());
		}
		if ( cards == null )
			cards = new Vector();
		boolean foundEndLine = true;
		int start = 0;
		int end = -2;
		cards.ensureCapacity(78);
		while ( foundEndLine ) {
			if ( sb.toString().length() - 10 < end )
				foundEndLine = false;
			else {
				start = end + 2;
				end = sb.toString().indexOf(";", start);
				cards.addElement(processCardInformation(sb.toString().substring(start, end + 1)));
			}
		}
	}
}
