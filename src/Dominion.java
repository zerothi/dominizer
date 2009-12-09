import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Vector;

import de.enough.polish.util.Locale;

public class Dominion {
	
	private Vector cards = null;
	private int numberOfRandomCards = 10;
	private boolean[] playingExpansions = new boolean[] {true, true, true, true};
	private int[] numberOfCardsFromExp = new int[] {0, 0, 0, 0};
	
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
		this.resetIsPlaying();
		Vector selectedCards = new Vector(numberOfRandomCards);
		int totalAvailable = cards.size();
		int selectedElement = 0;
		Random selector = new Random(System.currentTimeMillis());
		int i = 0;
		if ( 0 < numberOfCardsFromExp[0] &&)
			//#debug info
			System.out.println("starting selecting for BASE");
			while ( i < numberOfCardsFromExp[0] ) {
				selectedElement = selector.nextInt(totalAvailable);
				//#debug info
				System.out.println("try selecting" + selectedElement);
				if ( ((Card)cards.elementAt(selectedElement)).isAvailable() && !selectedCards.contains(cards.elementAt(selectedElement)) 
						&& ((Card)cards.elementAt(selectedElement)).getExpansion().equals(Locale.get("base")) ) {
					((Card)cards.elementAt(selectedElement)).setPlaying(true);
					selectedCards.addElement(cards.elementAt(selectedElement));
					i++;
					//#debug info
					System.out.println("BASE game selection: " + i);
				}
			}
		if ( 0 < numberOfCardsFromExp[1] )
			//#debug info
			System.out.println("starting selecting for PROMO");
			while ( i < numberOfCardsFromExp[0] + numberOfCardsFromExp[1] ) {
				selectedElement = selector.nextInt(totalAvailable);
				if ( ((Card)cards.elementAt(selectedElement)).isAvailable() && !selectedCards.contains(cards.elementAt(selectedElement)) 
						&& ((Card)cards.elementAt(selectedElement)).getExpansion().equals(Locale.get("promo")) ) {
					((Card)cards.elementAt(selectedElement)).setPlaying(true);
					selectedCards.addElement(cards.elementAt(selectedElement));
					i++;
					//#debug info
					System.out.println("PROMO game selection: " + i);
				}
			}
		if ( 0 < numberOfCardsFromExp[2] )
			//#debug info
			System.out.println("starting selecting for INTRIGUE");
			while ( i < numberOfCardsFromExp[0] + numberOfCardsFromExp[1] + numberOfCardsFromExp[2] ) {
				selectedElement = selector.nextInt(totalAvailable);
				if ( ((Card)cards.elementAt(selectedElement)).isAvailable() && !selectedCards.contains(cards.elementAt(selectedElement)) 
						&& ((Card)cards.elementAt(selectedElement)).getExpansion().equals(Locale.get("intrigue")) ) {
					((Card)cards.elementAt(selectedElement)).setPlaying(true);
					selectedCards.addElement(cards.elementAt(selectedElement));
					i++;
					//#debug info
					System.out.println("INTRIGUE game selection: " + i);
				}
			}
		if ( 0 < numberOfCardsFromExp[3] )
			//#debug info
			System.out.println("starting selecting for SEASIDE");
			while ( i < numberOfCardsFromExp[0] + numberOfCardsFromExp[1] + numberOfCardsFromExp[2] + numberOfCardsFromExp[3] ) {
				selectedElement = selector.nextInt(totalAvailable);
				if ( ((Card)cards.elementAt(selectedElement)).isAvailable() && !selectedCards.contains(cards.elementAt(selectedElement)) 
						&& ((Card)cards.elementAt(selectedElement)).getExpansion().equals(Locale.get("seaside")) ) {
					((Card)cards.elementAt(selectedElement)).setPlaying(true);
					selectedCards.addElement(cards.elementAt(selectedElement));
					i++;
					//#debug info
					System.out.println("SEASIDE game selection: " + i);
				}
			}
		while ( i < numberOfRandomCards ) {
			selectedElement = selector.nextInt(totalAvailable);
			if ( ((Card)cards.elementAt(selectedElement)).isAvailable() && !selectedCards.contains(cards.elementAt(selectedElement)) ) {
				((Card)cards.elementAt(selectedElement)).setPlaying(true);
				selectedCards.addElement(cards.elementAt(selectedElement));
				i++;
			}
		}
		selectedCards.trimToSize();
		selector = null;
		return sortCards(selectedCards);
	}
	
	public void setCardsUsedForExpansion(int expansion, int numberOfCards) {
		if ( expansion == 0 && numberOfRandomCards >= getNumberOfCardsSum(0) + numberOfCards )
			numberOfCardsFromExp[expansion] = numberOfCards;
		else if ( expansion == 1 && numberOfRandomCards >= getNumberOfCardsSum(1) + numberOfCards )
			numberOfCardsFromExp[expansion] = numberOfCards;
		else if ( expansion == 2 && numberOfRandomCards >= getNumberOfCardsSum(2) + numberOfCards )
			numberOfCardsFromExp[expansion] = numberOfCards;
		else if ( expansion == 3 && numberOfRandomCards >= getNumberOfCardsSum(3) + numberOfCards )
			numberOfCardsFromExp[expansion] = numberOfCards;
	}
	
	public Vector sortCards(Vector cards) {
		Card tmp = null;
		for ( int j = 0; j < cards.size(); j++ ) { 
			for ( int i = j + 1; i < cards.size(); i++ ) { 
				if ( Card.compare((Card)cards.elementAt(i), (Card)cards.elementAt(j), Card.NAME) < 0) {
					tmp = (Card)cards.elementAt(j);
					//#debug info
					System.out.println("Switching : " + tmp.getName() + " og " + cards.elementAt(i));
					cards.setElementAt(cards.elementAt(i), j); 
					cards.setElementAt(tmp, i);
				}
			}
		}
		return cards;
	}
	
	public int[] getNumberOfExpansionCards() {
		return numberOfCardsFromExp;
	}
	
	private int getNumberOfCardsSum(int overlook) {
		int tmp = numberOfCardsFromExp[0];
		tmp += numberOfCardsFromExp[1];
		tmp += numberOfCardsFromExp[2];
		tmp += numberOfCardsFromExp[3];
		if ( overlook == -1 )
			return tmp;
		tmp -= numberOfCardsFromExp[overlook];
		return tmp;
	}
	
	public Vector getAllCards() {
		return cards;
	}
	
	public void resetIsPlaying() {
		for ( int i = 0 ; i < cards.size() ; i++ ) {
			((Card)cards.elementAt(i)).setPlaying(false);
		}
	}
	
	private void readCards() {
		//#debug
		System.out.println("reading all cards");
		readResource("c");
	}
	
	private Card processCardInformation(String information) {
		Card card = new Card();
		int start = 0;
		card.setName(information.substring(start, information.indexOf(":", start)).trim());
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
	
	private void readResource(String fileName) {
		if ( cards == null )
			cards = new Vector();
		cards.ensureCapacity(78);
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		InputStreamReader isr = null;
		try {
			is = this.getClass().getResourceAsStream(fileName);      
			if (is == null)
				throw new Exception("File Does Not Exist");
			isr = new InputStreamReader(is,"UTF8");
			int ch;
			while ( (ch = isr.read()) > -1 ) {
				sb.append((char)ch);
				if ( (char)ch == ';' ) {
					cards.addElement(processCardInformation(sb.toString()));
					sb.delete(0, sb.toString().length() - 1);
				} else if ( (char)ch == '\n' ) {
					sb.delete(0, sb.toString().length() - 1);
				}
			}
			if (isr != null)  
				isr.close();              
		} catch (Exception ex) {
			//#debug
			System.out.println(ex);
		}
	}
}
=======


import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Vector;

import de.enough.polish.util.Locale;

public class Dominion {
	private static Dominion dom = null;
	public static final int BASE = 0;
	public static final int PROMO = 1;
	public static final int INTRIGUE = 2;
	public static final int SEASIDE = 3;
	private Cards[] expansions = null;
	private int numberOfRandomCards = 10;
	private boolean[] playingExpansions = new boolean[] {true, true, true, true};
	private int[] numberOfCardsFromExp = new int[] {0, 0, 0, 0};
	
	private Dominion() {
		expansions = new Cards[4];
		//#debug info
		System.out.println("reading base");
		readResource(BASE, "base", 25);
		//#debug info
		System.out.println("reading promo");
		readResource(PROMO, "promo", 2);
		//#debug info
		System.out.println("reading intrigue");
		readResource(INTRIGUE, "intrigue", 25);
		//#debug info
		System.out.println("reading seaside");
		readResource(SEASIDE, "seaside", 26);
	}
	
	public static Dominion instance() {
		if ( dom == null )
			dom = new Dominion();
		return dom;
	}
	
	public void setExpansionPlayingState(int expansion, boolean isAvailable) {
		playingExpansions[expansion] = isAvailable;
		for (int i = 0 ; i < expansions[expansion].size() ; i++ ) {
			expansions[expansion].setAvailable(i, isAvailable);
			expansions[expansion].setBlackMarketAvailable(i, isAvailable);
		}
	}
	
	public void setExpansionPlayingState(boolean[] isAvailable) {
		for (int exp = 0 ; exp < isAvailable.length ; exp++ )
			setExpansionPlayingState(exp, isAvailable[exp]);
	}
	
	public boolean[] getPlayingStates() {
		return playingExpansions;
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
	
	public Cards getRandomizedCards() {
		this.resetIsPlaying();
		Cards selectedCards = new Cards(numberOfRandomCards, Cards.IS_NOT_SET);
		int selectedElement = 0;
		Random selector = new Random(System.currentTimeMillis());
		int selected = 0;
		int tmpSum = 0;
		for ( int i = 0 ; i < expansions.length ; i++ ) {
			if ( playingExpansions[i] & 0 < numberOfCardsFromExp[i] ) {
				//#debug info
				System.out.println("starting selecting for expansion: " + i);
				tmpSum += numberOfCardsFromExp[i];
				while ( selected < tmpSum && selected < numberOfRandomCards ) {
					selectedElement = selector.nextInt(expansions[i].size());
					//#debug info
					System.out.println("try selecting" + selectedElement);
					if ( expansions[i].isAvailable(selectedElement) && !selectedCards.contains(expansions[i].getName(selectedElement)) ) {
						expansions[i].setPlaying(selectedElement, true);
						selectedCards.setCard(selected, expansions[i].getCard(selectedElement));
						selected++;
						//#debug info
						System.out.println("expansion: " + i + ". selected: " + selected);
					}
				}
			}
		}
		while ( selected < numberOfRandomCards ) {
			tmpSum = selector.nextInt(expansions.length);
			selectedElement = selector.nextInt(expansions[tmpSum].size());
			if ( expansions[tmpSum].isAvailable(selectedElement) && !selectedCards.contains(expansions[tmpSum].getName(selectedElement)) ) {
				expansions[tmpSum].setPlaying(selectedElement, true);
				selectedCards.setCard(selected, expansions[tmpSum].getCard(selectedElement));
				selected++;
			}
		}
		selector = null;
		return sortCards(selectedCards);
	}
	
	public Cards getBlackMarketDeck() {
		int total = 0;
		for ( int i = 0; i < expansions.length ; i++ ) 
			for ( int j = 0; j < expansions[i].size(); j++ ) 
				if ( expansions[i].isBlackMarketAvailable(j) && ! expansions[i].isPlaying(j) )
					total++;
		Cards blackMarket = new Cards(total, Cards.IS_NOT_SET);
		total = 0;
		Random selector = new Random(System.currentTimeMillis());
		int randomize = selector.nextInt(blackMarket.size());
		for ( int i = 0; i < expansions.length ; i++ )
			for ( int j = 0; j < expansions[i].size(); j++ ) 
				if ( expansions[i].isBlackMarketAvailable(j) && ! expansions[i].isPlaying(j) ) {
					while ( blackMarket.getName(randomize) != null )
						randomize = selector.nextInt(blackMarket.size());
					blackMarket.setCard(randomize, expansions[i].getCard(j) );
				}
		return blackMarket;
	}
	
	public void setCardsUsedForExpansion(int expansion, int numberOfCards) {
		numberOfCardsFromExp[expansion] = numberOfCards;
	}
	
	public Cards sortCards(Cards cards) {
		Object[] tmp = null;
		for ( int j = 0; j < cards.size(); j++ ) { 
			for ( int i = j + 1; i < cards.size(); i++ ) { 
				if ( Cards.compare( cards.getCard(i), cards.getCard(j), Cards.NAME) < 0) {
					tmp = (Object[]) cards.getCard(j);
					/*//#debug info
					System.out.println("Switching : " + tmp[0] + " og " + cards.getName(i));*/
					cards.setCard(j, cards.getCard(i)); 
					cards.setCard(i, tmp);
				}
			}
		}
		return cards;
	}
	
	public int[] getNumberOfExpansionCards() {
		return numberOfCardsFromExp;
	}
	
	private int getNumberOfCardsSum(int overlook) {
		int tmp = 0;
		for ( int i = 0 ; i < numberOfCardsFromExp.length ; i++ )
			if ( overlook != i )
				tmp += numberOfCardsFromExp[i];
		return tmp;
	}
	
	public void resetIsPlaying() {
		for ( int i = 0 ; i < expansions.length ; i++ )
			for ( int j = 0 ; j < expansions[i].size() ; j++ )
				expansions[i].setPlaying(j, false);
	}
	
	private boolean isTrue(String test) {
		return test.equals("0") ? false : true;
	}
	
	public String getExpansionName(int expansion) {
		switch ( expansion ) {
		case BASE:
			return Locale.get("base");
		case PROMO:
			return Locale.get("promo");
		case INTRIGUE:
			return Locale.get("intrigue");
		case SEASIDE:
			return Locale.get("seaside");
		}
		return "";
	}
	
	private void readResource(int exp, String fileName, int totalCards) {
		expansions[exp] = new Cards(totalCards, Cards.IS_SET);
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		InputStreamReader isr = null;
		int start = 0;
		int cardRead = 0;
		try {
			is = this.getClass().getResourceAsStream(fileName);      
			if (is == null)
				throw new Exception("File Does Not Exist");
			isr = new InputStreamReader(is,"UTF8");
			int ch;
			while ( (ch = isr.read()) > -1 ) {
				sb.append((char)ch);
				if ( (char)ch == ';' ) {
					expansions[exp].setName(cardRead, sb.toString().substring(start, sb.toString().indexOf(":", start)).trim());
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setExpansion(sb.toString().substring(start, sb.toString().indexOf(":", start)));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setCost(cardRead, Integer.parseInt(sb.toString().substring(start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setAction(cardRead, isTrue(sb.toString().substring(start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setVictory(cardRead, isTrue(sb.toString().substring(start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setTreasure(cardRead, isTrue(sb.toString().substring(start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setAction(cardRead, isTrue(sb.toString().substring(start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setReaction(cardRead, isTrue(sb.toString().substring(start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setDuration(cardRead, isTrue(sb.toString().substring(start, sb.toString().indexOf(";", start))));
					sb.delete(0, sb.toString().length() - 1);
					start = 0;
					//#debug info
					System.out.println("expansions[" + exp + "].name(" + cardRead + "): " + expansions[exp].getName(cardRead));
					cardRead++;
				} else if ( (char)ch == '\n' ) {
					sb.delete(0, sb.toString().length() - 1);
				}
			}
			if (isr != null)  
				isr.close();              
		} catch (Exception ex) {
			//#debug
			System.out.println(ex);
		}
	}
}
