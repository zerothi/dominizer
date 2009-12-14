package com;


//#if polish.android
//#= import android.content.res.Resources;
//#= import java.nio.charset.Charset;
//#endif
import java.io.InputStreamReader;
import java.util.Random;

import com.dominizer.GameApp;

import de.enough.polish.util.Locale;

public class Dominion {
	private static Dominion dom = null;
	public static final int TOTAL_CARDS = 78;
	public static final int BASE = 0;
	public static final int PROMO = 1;
	public static final int INTRIGUE = 2;
	public static final int SEASIDE = 3;
	private Cards[] expansions = null;
	private CardPresets[] rulePresets = null;
	private int numberOfRandomCards = 10;
	private boolean[] playingExpansions = new boolean[] {true, true, true, true};
	private int[] numberOfCardsFromExp = new int[] {0, 0, 0, 0};
	
	private Dominion() {
		expansions = new Cards[4];
		rulePresets = new CardPresets[3]; // Promo cards doesn't have preset!
		rulePresets[0] = new CardPresets(5);
		rulePresets[0].setPreset(0, Locale.get("preset.base.FirstGame"), 
				new int[][] { 
					new int[] {0,  2},
					new int[] {0, 11},
					new int[] {0, 12},
					new int[] {0, 13},
					new int[] {0, 14},
					new int[] {0, 16},
					new int[] {0, 17},
					new int[] {0, 21},
					new int[] {0, 23},
					new int[] {0, 24}
		});
		rulePresets[0].setPreset(1, Locale.get("preset.base.BigMoney"), 
				new int[][] { 
					new int[] {0,  0},
					new int[] {0,  1},
					new int[] {0,  3},
					new int[] {0,  4},
					new int[] {0,  6},
					new int[] {0,  9},
					new int[] {0, 11},
					new int[] {0, 13},
					new int[] {0, 15},
					new int[] {0, 20}
		});
		rulePresets[0].setPreset(2, Locale.get("preset.base.Interaction"), 
				new int[][] { 
					new int[] {0,  1},
					new int[] {0,  3},
					new int[] {0,  5},
					new int[] {0,  7},
					new int[] {0, 10},
					new int[] {0, 12},
					new int[] {0, 14},
					new int[] {0, 18},
					new int[] {0, 23},
					new int[] {0, 24}
		});
		rulePresets[0].setPreset(3, Locale.get("preset.base.SizeDistortion"), 
				new int[][] { 
					new int[] {0,  2},
					new int[] {0,  4},
					new int[] {0,  6},
					new int[] {0,  8},
					new int[] {0,  9},
					new int[] {0, 19},
					new int[] {0, 21},
					new int[] {0, 22},
					new int[] {0, 23},
					new int[] {0, 24}
		});
		rulePresets[0].setPreset(4, Locale.get("preset.base.VillageSquare"), 
				new int[][] { 
					new int[] {0,  1},
					new int[] {0,  2},
					new int[] {0,  7},
					new int[] {0, 10},
					new int[] {0, 11},
					new int[] {0, 16},
					new int[] {0, 17},
					new int[] {0, 20},
					new int[] {0, 21},
					new int[] {0, 23}
		});
		rulePresets[1] = new CardPresets(6);
		rulePresets[1].setPreset(0, Locale.get("preset.intrigue.VictoryDance"), 
				new int[][] { 
					new int[] {2,  1},
					new int[] {2,  5},
					new int[] {2,  6},
					new int[] {2,  7},
					new int[] {2,  8},
					new int[] {2,  9},
					new int[] {2, 12},
					new int[] {2, 13},
					new int[] {2, 15},
					new int[] {2, 23}
		});
		rulePresets[1].setPreset(1, Locale.get("preset.intrigue.SecretSchemes"), 
				new int[][] { 
					new int[] {2,  2},
					new int[] {2,  7},
					new int[] {2,  8},
					new int[] {2, 13},
					new int[] {2, 14},
					new int[] {2, 17},
					new int[] {2, 18},
					new int[] {2, 20},
					new int[] {2, 21},
					new int[] {2, 22}
		})
		rulePresets[1].setPreset(2, Locale.get("preset.intrigue.BestWishes"), 
				new int[][] { 
					new int[] {2,  3},
					new int[] {2,  4},
					new int[] {2,  9},
					new int[] {2, 15},
					new int[] {2, 17},
					new int[] {2, 18},
					new int[] {2, 20},
					new int[] {2, 21},
					new int[] {2, 23},
					new int[] {2, 24}
		})
		rulePresets[1].setPreset(3, Locale.get("preset.intrigue.Deconstruction"), 
				new int[][] { 
					new int[] {0, 16},
					new int[] {0, 18},
					new int[] {0, 19},
					new int[] {0, 20},
					new int[] {2,  1},
					new int[] {2, 10},
					new int[] {2, 14},
					new int[] {2, 16},
					new int[] {2, 19},
					new int[] {2, 20}
		});
		rulePresets[1].setPreset(4, Locale.get("preset.intrigue.HandMadness"), 
				new int[][] { 
					new int[] {0,  1},
					new int[] {0,  3},
					new int[] {0,  5},
					new int[] {0, 12},
					new int[] {0, 13},
					new int[] {2,  4},
					new int[] {2, 11},
					new int[] {2, 12},
					new int[] {2, 18},
					new int[] {2, 20}
		});
		rulePresets[1].setPreset(5, Locale.get("preset.intrigue.Underlings"), 
				new int[][] { 
					new int[] {0,  2},
					new int[] {0,  7},
					new int[] {0, 10},
					new int[] {0, 22},
					new int[] {2,  0},
					new int[] {2,  9},
					new int[] {2, 11},
					new int[] {2, 12},
					new int[] {2, 13},
					new int[] {2, 18}
		});
		//#debug info
		System.out.println("reading preset card Village Square: " + rulePresets[0].getPresetName(4) + ". Card 3: " + rulePresets[0].getPreset(4)[3][0] + " and " + rulePresets[0].getPreset(4)[3][1]);
		//#debug info
		System.out.println("reading base");
		readResource(BASE, "base", 25);
		//#debug info
		System.out.println("size base: " + expansions[0].size());
		//#debug info
		System.out.println("reading promo");
		readResource(PROMO, "promo", 2);
		//#debug info
		System.out.println("size promo: " + expansions[1].size());
		//#debug info
		System.out.println("reading intrigue");
		readResource(INTRIGUE, "intrigue", 25);
		//#debug info
		System.out.println("size intrigue: " + expansions[2].size());
		//#debug info
		System.out.println("reading seaside");
		readResource(SEASIDE, "seaside", 26);
		//#debug info
		System.out.println("size seaside: " + expansions[3].size()+ ". Third name: " + expansions[3].getName(2));
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
	
	public Cards getPreset(int[][] preset) {
		Cards presetCards = new Cards(10, Cards.IS_NOT_SET);
		for ( int i = 0 ; i < preset.length ; i++ ) {
			presetCards.setCard(i, expansions[preset[i][0]].getCard(preset[i][1]));
		}
		return sortCards(presetCards);
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
	
	public void resetIsPlaying() {
		for ( int i = 0 ; i < expansions.length ; i++ )
			for ( int j = 0 ; j < expansions[i].size() ; j++ )
				expansions[i].setPlaying(j, false);
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
				//#= try {
				//#= if ( exp == 0 )
				//#= 	isr = new InputStreamReader(new Resources(null, null, null).openRawResource(com.dominizer.R.raw.base), Charset.forName("UTF-8"));
				//#= else if ( exp == 1 )
				//#= 	isr = new InputStreamReader(new Resources(null, null, null).openRawResource(com.dominizer.R.raw.promo), Charset.forName("UTF-8"));
				//#= else if ( exp == 2 )
				//#= 	isr = new InputStreamReader(new Resources(null, null, null).openRawResource(com.dominizer.R.raw.intrigue), Charset.forName("UTF-8"));
				//#= else if ( exp == 3 )
				//#= 	isr = new InputStreamReader(new Resources(null, null, null).openRawResource(com.dominizer.R.raw.seaside), Charset.forName("UTF-8"));
				//#= } catch (Resources.NotFoundException e) {
				//#= 	//#debug info
				//#=    System.out.println("Resource hasn't been found");
				//#= }
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
					expansions[exp].setAttack(cardRead, isTrue(sb.toString().substring(start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setReaction(cardRead, isTrue(sb.toString().substring(start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setDuration(cardRead, isTrue(sb.toString().substring(start, sb.toString().indexOf(";", start))));
					sb.delete(0, sb.toString().length());
					start = 0;
					/*
					//debug info
					System.out.println("expansions[" + exp + "].name(" + cardRead + "): " + expansions[exp].getName(cardRead) + ". Action? " + expansions[exp].isAction(cardRead)+
							". Attack? " + expansions[exp].isAttack(cardRead) +
							". Reaction? " + expansions[exp].isReaction(cardRead) +
							". Victory? " + expansions[exp].isVictory(cardRead) +
							". Treasury? " + expansions[exp].isTreasure(cardRead) +
							". Duration? " + expansions[exp].isDuration(cardRead)
							);
					*/
					cardRead++;
				} else if ( (char)ch == '\n' ) {
					sb.delete(0, sb.toString().length() - 1);
				}
			}
			if (isr != null)  
				isr.close();              
		} catch (Exception ex) {
			//#debug info
			System.out.println("exception on reading:" + ex);
		}
	}
	
	private boolean isTrue(String test) {
		return test.equals("0") ? false : true;
	}
}
