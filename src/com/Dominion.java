package com;


//#if polish.android
//#= import android.content.res.Resources;
//#= import java.nio.charset.Charset;
//#endif
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Vector;

import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

import de.enough.polish.util.Locale;

public class Dominion {
	private static Dominion dom = null;
	public static final int TOTAL_CARDS = 78;
	public static final int BASE = 0;
	public static final int PROMO = 1;
	public static final int INTRIGUE = 2;
	public static final int SEASIDE = 3;
	public static final int SORT_EXPANSION = 0;
	public static final int SORT_NAME = 1;
	public static final int SORT_COST = 2;
	private Cards[] expansions = null;
	private Cards selectedCards = null;
	private CardPresets[] presets = null;
	private int numberOfRandomCards = 10;
	private boolean[] playingExpansions = new boolean[] {true, true, true, true};
	private int[] numberOfCardsFromExp = new int[] {0, 0, 0, 0};

	private Dominion() {
		expansions = new Cards[4];
		presets = new CardPresets[4]; // Promo cards doesn't have preset! The last is used when reading user presets!
		presets[0] = new CardPresets(5);
		presets[0].setPreset(0, Locale.get("preset.base.FirstGame"), new int[][] { 
			new int[] {0,  2}, new int[] {0, 11}, new int[] {0, 12}, new int[] {0, 13}, new int[] {0, 14}, 
			new int[] {0, 16}, new int[] {0, 17}, new int[] {0, 21}, new int[] {0, 23}, new int[] {0, 24} });
		presets[0].setPreset(1, Locale.get("preset.base.BigMoney"), new int[][] { 
			new int[] {0,  0}, new int[] {0,  1}, new int[] {0,  3}, new int[] {0,  4}, new int[] {0,  6}, 
			new int[] {0,  9}, new int[] {0, 11}, new int[] {0, 13}, new int[] {0, 15}, new int[] {0, 20} });
		presets[0].setPreset(2, Locale.get("preset.base.Interaction"), new int[][] { 
			new int[] {0,  1}, new int[] {0,  3}, new int[] {0,  5}, new int[] {0,  7}, new int[] {0, 10},
			new int[] {0, 12}, new int[] {0, 14}, new int[] {0, 18}, new int[] {0, 23}, new int[] {0, 24} });
		presets[0].setPreset(3, Locale.get("preset.base.SizeDistortion"), new int[][] {
			new int[] {0,  2}, new int[] {0,  4}, new int[] {0,  6}, new int[] {0,  8}, new int[] {0,  9},
			new int[] {0, 19}, new int[] {0, 21}, new int[] {0, 22}, new int[] {0, 23}, new int[] {0, 24} });
		presets[0].setPreset(4, Locale.get("preset.base.VillageSquare"), new int[][] { 
			new int[] {0,  1}, new int[] {0,  2}, new int[] {0,  7}, new int[] {0, 10}, new int[] {0, 11},
			new int[] {0, 16}, new int[] {0, 17}, new int[] {0, 20}, new int[] {0, 21}, new int[] {0, 23} });
		presets[1] = new CardPresets(6);
		presets[1].setPreset(0, Locale.get("preset.intrigue.VictoryDance"), new int[][] { 
			new int[] {2,  1}, new int[] {2,  5}, new int[] {2,  6}, new int[] {2,  7}, new int[] {2,  8},
			new int[] {2,  9}, new int[] {2, 12}, new int[] {2, 13}, new int[] {2, 15}, new int[] {2, 23} });
		presets[1].setPreset(1, Locale.get("preset.intrigue.SecretSchemes"), new int[][] { 
			new int[] {2,  2}, new int[] {2,  7}, new int[] {2,  8}, new int[] {2, 13},	new int[] {2, 14},
			new int[] {2, 17}, new int[] {2, 18}, new int[] {2, 20}, new int[] {2, 21}, new int[] {2, 22} });
		presets[1].setPreset(2, Locale.get("preset.intrigue.BestWishes"), new int[][] { 
			new int[] {2,  3}, new int[] {2,  4}, new int[] {2,  9}, new int[] {2, 15}, new int[] {2, 17}, 
			new int[] {2, 18}, new int[] {2, 20}, new int[] {2, 21}, new int[] {2, 23}, new int[] {2, 24} });
		presets[1].setPreset(3, Locale.get("preset.intrigue.Deconstruction"),  	new int[][] { 
			new int[] {0, 16}, new int[] {0, 18}, new int[] {0, 19}, new int[] {0, 20}, new int[] {2,  1},
			new int[] {2, 10}, new int[] {2, 14}, new int[] {2, 16}, new int[] {2, 19}, new int[] {2, 20} });
		presets[1].setPreset(4, Locale.get("preset.intrigue.HandMadness"), new int[][] { 
			new int[] {0,  1}, new int[] {0,  3}, new int[] {0,  5}, new int[] {0, 12}, new int[] {0, 13},
			new int[] {2,  4}, new int[] {2, 11}, new int[] {2, 12}, new int[] {2, 18}, new int[] {2, 20} });
		presets[1].setPreset(5, Locale.get("preset.intrigue.Underlings"), new int[][] {
			new int[] {0,  2}, new int[] {0,  7}, new int[] {0, 10}, new int[] {0, 22}, new int[] {2,  0},
			new int[] {2,  9}, new int[] {2, 11}, new int[] {2, 12}, new int[] {2, 13}, new int[] {2, 18} });
		presets[2] = new CardPresets(6);
		presets[2].setPreset(0, Locale.get("preset.seaside.HighSeas"), new int[][] {
			new int[] {3,  1}, new int[] {3,  2}, new int[] {3,  4}, new int[] {3,  5}, new int[] {3,  8},
			new int[] {3,  9}, new int[] {3, 11}, new int[] {3, 17}, new int[] {3, 20}, new int[] {3, 25} });
		presets[2].setPreset(1, Locale.get("preset.seaside.BuriedTreasure"), new int[][] {
			new int[] {3,  0}, new int[] {3,  3}, new int[] {3,  6}, new int[] {3, 10}, new int[] {3, 15},
			new int[] {3, 16}, new int[] {3, 21}, new int[] {3, 22}, new int[] {3, 24}, new int[] {3, 25} });
		presets[2].setPreset(2, Locale.get("preset.seaside.Shipwrecks"), new int[][] {
			new int[] {3,  7}, new int[] {3, 12}, new int[] {3, 13}, new int[] {3, 14}, new int[] {3, 16},
			new int[] {3, 18}, new int[] {3, 19}, new int[] {3, 20}, new int[] {3, 23}, new int[] {3, 24} });
		presets[2].setPreset(3, Locale.get("preset.seaside.ReachForTomorrow"), new int[][] {
			new int[] {0,  0}, new int[] {0,  2}, new int[] {0,  5}, new int[] {0, 21}, new int[] {0, 18},
			new int[] {3,  3}, new int[] {3,  7}, new int[] {3, 11}, new int[] {3, 19}, new int[] {3, 22} });
		presets[2].setPreset(4, Locale.get("preset.seaside.Repetition"), new int[][] {
			new int[] {0,  3}, new int[] {0,  7}, new int[] {0, 12}, new int[] {0, 24}, new int[] {3,  2},
			new int[] {3,  5}, new int[] {3, 15}, new int[] {3, 16}, new int[] {3, 17}, new int[] {3, 23} });
		presets[2].setPreset(5, Locale.get("preset.seaside.GiveAndTake"), new int[][] {
			new int[] {0, 10}, new int[] {0, 11}, new int[] {0, 15}, new int[] {0, 22}, new int[] {3,  0},
			new int[] {3,  6}, new int[] {3,  8}, new int[] {3,  9}, new int[] {3, 18}, new int[] {3, 20} });
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

	public boolean[] getPlayingStates() {
		return playingExpansions;
	}

	public void setExpansionPlayingState(int expansion, boolean isAvailable) {
		playingExpansions[expansion] = isAvailable;
		for (int i = 0 ; i < expansions[expansion].size() ; i++ ) {
			expansions[expansion].setAvailable(i, isAvailable);
			expansions[expansion].setBlackMarketAvailable(i, isAvailable);
		}
	}

	public void setExpansionPlayingState(boolean[] isAvailable) {
		for (int exp = 0 ; exp < isAvailable.length ; exp++ ) {
			//#debug info
			System.out.println("setting playing state: " + exp + ". =" + isAvailable[exp]);	
			setExpansionPlayingState(exp, isAvailable[exp]);
		}
	}

	public void randomizeCards(int sortMethod) {
		this.resetIsPlaying();
		this.selectedCards = new Cards(numberOfRandomCards, Cards.IS_NOT_SET);
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
						this.selectedCards.setCard(selected, expansions[i].getCard(selectedElement));
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
				this.selectedCards.setCard(selected, expansions[tmpSum].getCard(selectedElement));
				selected++;
			}
		}
		selector = null;
		sortCards(this.selectedCards, sortMethod);
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

	public String getSelectedInfo() {
		if ( this.selectedCards == null )
			return "";
		StringBuffer sb = new StringBuffer(50);
		double action = 0.00, attack = 0.00, reaction = 0.00, treasury = 0.00, victory = 0.00, duration = 0.00;
		for ( int i = 0 ; i < selectedCards.size() ; i++ ) {
			action += selectedCards.isAction(i) ? 1 / selectedCards.size() : 0;
			attack += selectedCards.isAttack(i) ? 1 / selectedCards.size() : 0;
			reaction += selectedCards.isReaction(i) ? 1 / selectedCards.size() : 0;
			treasury += selectedCards.isTreasure(i) ? 1 / selectedCards.size() : 0;
			victory += selectedCards.isVictory(i) ? 1 / selectedCards.size() : 0;
			duration += selectedCards.isDuration(i) ? 1 / selectedCards.size() : 0;
		}
		sb.append("Action: \t" + action + ".\n");
		sb.append("Attack: \t" + attack + ".\n");
		sb.append("Reaction: \t" + reaction + ".\n");
		sb.append("Treasury: \t" + treasury + ".\n");
		sb.append("Victory: \t" + victory + ".\n");
		sb.append("Duration: \t" + duration + ".");
		return sb.toString();
	}

	public Cards sortCards(Cards cards, int sortMethod) {
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

	public int presetSize() {
		return presets.length;
	}

	public Cards getPreset(int presetDeck, int preset) {
		//#debug info
		System.out.println("fetching preset: " + presetDeck + " and " + preset);
		this.selectedCards = new Cards(10, Cards.IS_NOT_SET);
		for ( int i = 0 ; i < presets[presetDeck].size() ; i++ ) {
			//#debug info
			System.out.println("selecting expansion: " + presets[presetDeck].getPresetCardExpansion(preset, i) + " and card: " + presets[presetDeck].getPresetCardPlacement(preset, i));
			selectedCards.setCard(i, expansions[presets[presetDeck].getPresetCardExpansion(preset, i)].getCard(presets[presetDeck].getPresetCardPlacement(preset, i)));
		}
		return sortCards(selectedCards, SORT_EXPANSION);
	}

	public Cards getPreset(String presetName) {
		for ( int i = 0 ; i < presets.length ; i++ )
			for ( int j = 0 ; j < presets[i].size() ; j++ )
				if ( presets[i].getPresetName(j).equals(presetName) )
					return this.getPreset(i, j);
		return null;

	}

	public CardPresets getPreset(int preset) {
		return presets[preset];
	}

	public void resetIsPlaying() {
		for ( int i = 0 ; i < expansions.length ; i++ )
			for ( int j = 0 ; j < expansions[i].size() ; j++ )
				expansions[i].setPlaying(j, false);
	}

	public String getCurrentAsPresetSave() {
		if ( this.selectedCards == null )
			return "";
		StringBuffer sb = new StringBuffer(50);
		for ( int i = 0 ; i < expansions.length ; i++ )
			for ( int j = 0 ; j < expansions[i].size() ; j++ )
				for ( int k = 0 ; k < selectedCards.size() ; k++ )
					if ( selectedCards.getName(k).equals(expansions[i].getName(j)) )
						sb.append(SettingsRecordStorage.MEDIUM_SPLITTER + i + SettingsRecordStorage.SMALL_SPLITTER + j);
		return sb.toString();
	}

	public static String getExpansionName(int expansion) {
		switch ( expansion ) {
		case BASE:
			return Locale.get("base");
		case PROMO:
			return Locale.get("promo");
		case INTRIGUE:
			return Locale.get("intrigue");
		case SEASIDE:
			return Locale.get("seaside");
		default:
			return "";
		}
	}

	public Cards getCurrentlySelected() throws DominionException {
		if ( selectedCards == null | selectedCards.size() != numberOfRandomCards ) 
			throw new DominionException("No currently selected cards.");
		return this.selectedCards;
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
			isr = new InputStreamReader(this.getClass().getResourceAsStream("/" + fileName),"UTF8");
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

	private void readUserPresets() {
		Vector settings = null;
		try {
			settings = new SettingsRecordStorage().readData(Locale.get("rms.file.preset"));
		} catch (RecordStoreFullException e) {
			settings = null;
		} catch (RecordStoreException e) {
			settings = null;
		}
		if ( settings == null ) {
			//#debug info
			System.out.println("Read user settings: settings is null");
			return;
		}
		presets[3] = new CardPresets(settings.size());
		int[][] preset = new int[10][2];
		int start = 0;
		for ( int i = 0 ; i < settings.size() ; i++ ) {
			if ( settings.elementAt(i).toString().startsWith(Locale.get("rms.preset")) ) {
				//#debug info
				System.out.println("getExpansions: " + settings.elementAt(i).toString());
				for (int k = 0 ; k < 10 ; k++ ) {
					start = settings.elementAt(i).toString().indexOf(SettingsRecordStorage.MEDIUM_SPLITTER, start);
					if ( k == 9 )
						preset[k] = getCardInfo(settings.elementAt(i).toString().substring(start));
					else
						preset[k] = getCardInfo(settings.elementAt(i).toString().substring(start, 
								settings.elementAt(i).toString().indexOf(SettingsRecordStorage.MEDIUM_SPLITTER)));
				}
			}
		}
	}

	private int[] getCardInfo(String card) {
		return new int[] {
				Integer.valueOf(card.substring(card.indexOf(SettingsRecordStorage.MEDIUM_SPLITTER) + 1, card.indexOf(SettingsRecordStorage.SMALL_SPLITTER))).intValue(), 
				Integer.valueOf(card.substring(card.indexOf(SettingsRecordStorage.SMALL_SPLITTER) + 1 )).intValue()
		}; 
	}

	private boolean isTrue(String test) {
		return test.equals("0") ? false : true;
	}
}
