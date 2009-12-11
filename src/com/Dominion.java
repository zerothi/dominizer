package com;



import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import de.enough.polish.util.Locale;

public class Dominion {
	private static Dominion dom = null;
	public static final int TOTAL_CARDS = 78;
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
			selectedElement = selector.nextInt(TOTAL_CARDS);
			if ( selectedElement < expansions[0].size() )
				tmpSum = 0;
			else if ( selectedElement < (expansions[0].size() + expansions[1].size()) ) {
				selectedElement -= expansions[0].size();
				tmpSum = 1;
			} else if ( selectedElement < (expansions[0].size() + expansions[1].size() + expansions[2].size()) ) {
				selectedElement -= (expansions[0].size() + expansions[1].size());
				tmpSum = 2;
			} else if ( selectedElement < (expansions[0].size() + expansions[1].size() + expansions[2].size() + expansions[3].size()) ) {
				selectedElement -= (expansions[0].size() + expansions[1].size() + expansions[2].size());
				tmpSum = 3;
			}
			//#debug info
			System.out.println("choosing expansion: " + tmpSum + ". together with card: " + selectedElement);
			if ( playingExpansions[tmpSum] && expansions[tmpSum].isAvailable(selectedElement) && 
					!selectedCards.contains(expansions[tmpSum].getName(selectedElement)) ) {
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
		InputStreamReader isr = null;
		int start = 0;
		int cardRead = 0;
		try {
			//#if polish.android
				//#= isr = new InputStreamReader(getResources().openRawResource(fileName),"UTF8");
			//#else
			isr = new InputStreamReader(this.getClass().getResourceAsStream(fileName),"UTF8");
			//#endif
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
