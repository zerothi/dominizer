package com;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.microedition.lcdui.Image;

import de.enough.polish.util.Locale;

public class Dominion {
	private static Dominion dom = null;
	public static final int TOTAL_CARDS = 25 + 25 + 26 + 3;
	public static final int BASE = 0; // has 25 cards
	public static final int INTRIGUE = 1; // has 25 cards
	public static final int SEASIDE = 2; // has 26 cards
	public static final int ALCHEMY = 3;
	public static final int PROSPERITY = 4;
	public static final int PROMO = 5; // has 3 cards
	public static final int USER = 10;
	public static boolean RANDOMIZE_COMPLETELY_NEW = true;
	
	private Cards[] expansions = null;
	private Cards selectedCards = null;
	private CardPresets[] presets = null;
	private int numberOfRandomCards = 10;
	private boolean[] playingExpansions = new boolean[] { true, true, true, true, true, true };
	private int[] numberOfCardsFromExp = new int[] { 0, 0, 0, 0, 0, 0 };
	private StringBuffer sb;
	private int loop = 0;

	
	public static Dominion I() {
		if (dom == null)
			dom = new Dominion();
		return dom;
	}
	
	private Dominion() {
		expansions = new Cards[6]; 
		presets = new CardPresets[4]; 
		presets[BASE] = new CardPresets(5);
		presets[BASE].setExpansion(BASE);
		presets[BASE].setPreset(0, Locale.get("preset.base.FirstGame"), new int[][] { 
			new int[] { 0,  2 }, new int[] { 0, 11 }, new int[] { 0, 12 }, new int[] { 0, 13 }, new int[] { 0, 14 }, 
			new int[] { 0, 16 }, new int[] { 0, 17 }, new int[] { 0, 21 }, new int[] { 0, 23 }, new int[] { 0, 24 } });
		presets[BASE].setPreset(1, Locale.get("preset.base.BigMoney"), new int[][] { 
			new int[] { 0, 0 }, new int[] { 0,  1 }, new int[] { 0,  3 }, new int[] { 0,  4 }, new int[] { 0,  6 }, 
			new int[] { 0, 9 }, new int[] { 0, 11 }, new int[] { 0, 13 }, new int[] { 0, 15 }, new int[] { 0, 20 } });
		presets[BASE].setPreset(2, Locale.get("preset.base.Interaction"), new int[][] { 
			new int[] { 0,  1 }, new int[] { 0,  3 }, new int[] { 0,  5 }, new int[] { 0,  7 }, new int[] { 0, 10 }, 
			new int[] { 0, 12 }, new int[] { 0, 14 }, new int[] { 0, 18 }, new int[] { 0, 23 }, new int[] { 0, 24 } });
		presets[BASE].setPreset(3, Locale.get("preset.base.SizeDistortion"), new int[][] {
			new int[] { 0,  2 }, new int[] { 0,  4 }, new int[] { 0,  6 }, new int[] { 0,  8 }, new int[] { 0,  9 }, 
			new int[] { 0, 19 }, new int[] { 0, 21 }, new int[] { 0, 22 }, new int[] { 0, 23 }, new int[] { 0, 24 } });
		presets[BASE].setPreset(4, Locale.get("preset.base.VillageSquare"), new int[][] {
			new int[] { 0,  1 }, new int[] { 0,  2 }, new int[] { 0,  7 }, new int[] { 0, 10 }, new int[] { 0, 11 }, 
			new int[] { 0, 16 }, new int[] { 0, 17 }, new int[] { 0, 20 }, new int[] { 0, 21 }, new int[] { 0, 23 } });
		presets[INTRIGUE] = new CardPresets(6);
		presets[INTRIGUE].setExpansion(INTRIGUE);
		presets[INTRIGUE].setPreset(0, Locale.get("preset.intrigue.VictoryDance"), new int[][] {
			new int[] { 1,  1 }, new int[] { 1,  5 }, new int[] { 1,  6 }, new int[] { 1,  7 }, new int[] { 1,  8 }, 
			new int[] { 1,  9 }, new int[] { 1, 12 }, new int[] { 1, 13 }, new int[] { 1, 15 }, new int[] { 1, 23 } });
		presets[INTRIGUE].setPreset(1, Locale.get("preset.intrigue.SecretSchemes"), new int[][] {
			new int[] { 1,  2 }, new int[] { 1,  7 }, new int[] { 1,  8 }, new int[] { 1, 13 }, new int[] { 1, 14 }, 
			new int[] { 1, 17 }, new int[] { 1, 18 }, new int[] { 1, 20 }, new int[] { 1, 21 }, new int[] { 1, 22 } });
		presets[INTRIGUE].setPreset(2, Locale.get("preset.intrigue.BestWishes"), new int[][] {
			new int[] { 1,  3 }, new int[] { 1,  4 }, new int[] { 1,  9 }, new int[] { 1, 15 }, new int[] { 1, 17 }, 
			new int[] { 1, 18 }, new int[] { 1, 20 }, new int[] { 1, 21 }, new int[] { 1, 23 }, new int[] { 1, 24 } });
		presets[INTRIGUE].setPreset(3, Locale.get("preset.intrigue.Deconstruction"), new int[][] {
			new int[] { 0, 16 }, new int[] { 0, 18 }, new int[] { 0, 19 }, new int[] { 0, 20 }, new int[] { 1,  1 }, 
			new int[] { 1, 10 }, new int[] { 1, 14 }, new int[] { 1, 16 }, new int[] { 1, 19 }, new int[] { 1, 20 } });
		presets[INTRIGUE].setPreset(4, Locale.get("preset.intrigue.HandMadness"), new int[][] {
			new int[] { 0,  1 }, new int[] { 0,  3 }, new int[] { 0,  5 }, new int[] { 0, 12 }, new int[] { 0, 13 }, 
			new int[] { 1,  4 }, new int[] { 1, 11 }, new int[] { 1, 12 }, new int[] { 1, 18 }, new int[] { 1, 20 } });
		presets[INTRIGUE].setPreset(5, Locale.get("preset.intrigue.Underlings"), new int[][] {
			new int[] { 0,  2 }, new int[] { 0,  7 }, new int[] { 0, 10 }, new int[] { 0, 22 }, new int[] { 1,  0 }, 
			new int[] { 1,  9 }, new int[] { 1, 11 }, new int[] { 1, 12 }, new int[] { 1, 13 }, new int[] { 1, 18 } });
		presets[SEASIDE] = new CardPresets(6);
		presets[SEASIDE].setExpansion(SEASIDE);
		presets[SEASIDE].setPreset(0, Locale.get("preset.seaside.HighSeas"), new int[][] {
			new int[] { 2,  1 }, new int[] { 2,  2 }, new int[] { 2,  4 }, new int[] { 2,  5 }, new int[] { 2,  8 }, 
			new int[] { 2,  9 }, new int[] { 2, 11 }, new int[] { 2, 17 }, new int[] { 2, 20 }, new int[] { 2, 25 } });
		presets[SEASIDE].setPreset(1, Locale.get("preset.seaside.BuriedTreasure"), new int[][] {
			new int[] { 2,  0 }, new int[] { 2,  3 }, new int[] { 2,  6 }, new int[] { 2, 10 }, new int[] { 2, 15 }, 
			new int[] { 2, 16 }, new int[] { 2, 21 }, new int[] { 2, 22 }, new int[] { 2, 24 }, new int[] { 2, 25 } });
		presets[SEASIDE].setPreset(2, Locale.get("preset.seaside.Shipwrecks"), new int[][] {
			new int[] { 2,  7 }, new int[] { 2, 12 }, new int[] { 2, 13 }, new int[] { 2, 14 }, new int[] { 2, 16 }, 
			new int[] { 2, 18 }, new int[] { 2, 19 }, new int[] { 2, 20 }, new int[] { 2, 23 }, new int[] { 2, 24 } });
		presets[SEASIDE].setPreset(3, Locale.get("preset.seaside.ReachForTomorrow"), new int[][] {
			new int[] { 0,  0 }, new int[] { 0,  2 }, new int[] { 0,  5 }, new int[] { 0, 21 }, new int[] { 0, 18 }, 
			new int[] { 2,  3 }, new int[] { 2,  7 }, new int[] { 2, 11 }, new int[] { 2, 19 }, new int[] { 2, 22 } });
		presets[SEASIDE].setPreset(4, Locale.get("preset.seaside.Repetition"), new int[][] {
			new int[] { 0,  3 }, new int[] { 0,  7 }, new int[] { 0, 12 }, new int[] { 0, 24 }, new int[] { 2,  2 }, 
			new int[] { 2,  5 }, new int[] { 2, 15 }, new int[] { 2, 16 }, new int[] { 2, 17 }, new int[] { 2, 23 } });
		presets[SEASIDE].setPreset(5, Locale.get("preset.seaside.GiveAndTake"), new int[][] {
			new int[] { 0, 10 }, new int[] { 0, 11 }, new int[] { 0, 15 }, new int[] { 0, 22 }, new int[] { 2,  0 }, 
			new int[] { 2,  6 }, new int[] { 2,  8 }, new int[] { 2,  9 }, new int[] { 2, 18 }, new int[] { 2, 20 } });
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("base"));
		//#debug dominizer
		System.out.println("reading base");
		readResource(BASE, "base", 25);
		//#debug dominizer
		System.out.println("size ba " + expansions[BASE].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("intrigue"));
		//#debug dominizer
		System.out.println("reading intrigue");
		readResource(INTRIGUE, "intrigue", 25);
		//#debug dominizer
		System.out.println("size intrigue: " + expansions[INTRIGUE].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("seaside"));
		//#debug dominizer
		System.out.println("reading seaside");
		readResource(SEASIDE, "seaside", 26);
		//#debug dominizer
		System.out.println("size seaside: " + expansions[SEASIDE].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("alchemy"));
		//#debug dominizer
		System.out.println("reading alchemy");
		readResource(ALCHEMY, "alchemy", 0); // TODO real 13
		//#debug dominizer
		System.out.println("size alchemy: " + expansions[ALCHEMY].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("prosperity"));
		//#debug dominizer
		System.out.println("reading prosperity");
		readResource(PROSPERITY, "prosperity", 0); // TODO real supposedly 25
		//#debug dominizer
		System.out.println("size prosperity: " + expansions[PROSPERITY].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("promo"));
		//#debug dominizer
		System.out.println("reading promo");
		readResource(PROMO, "promo", 3);
		//#debug dominizer
		System.out.println("size promo: " + expansions[PROMO].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.cards.settings"));
		//#debug dominizer
		System.out.println("reading settings cards");
		readSettings();
	}

	private void checkAvailability() throws DominionException {
		int tmp = 0, sum = 0;
		for ( loop = 0; loop < expansions.length; loop++) {
			tmp = 0;
			for (int j = 0; j < expansions[loop].size(); j++) {
				if (expansions[loop].isAvailable(j) & !expansions[loop].isPlaying(j))
					tmp++;
			}
			sum += tmp;
			if (tmp < numberOfCardsFromExp[loop] & playingExpansions[loop]) {
				String t = getExpansionName(loop);
				throw new DominionException(Locale.get("alert.Randomization.Availability", t));
			}
		}
		if (sum < numberOfRandomCards) {
			String t = "" + sum;
			throw new DominionException(Locale.get("alert.Randomization.TotalAvailability", t));
		}
	}

	public String getAvailableAsSave() {
		StringBuffer sb = new StringBuffer(TOTAL_CARDS);
		for ( loop = 0; loop < expansions.length; loop++)
			for (int j = 0; j < expansions[loop].size(); j++)
				sb.append("" + (expansions[loop].isAvailable(j) ? "1" : "0"));
		return sb.toString();
	}
	
	public String getPercentagesAsSave() {
		StringBuffer sb = new StringBuffer(TOTAL_CARDS);
		for ( loop = 0; loop < expansions.length; loop++)
			for (int j = 0; j < expansions[loop].size(); j++)
				sb.append( expansions[loop].getPercentage(j) == 10 ? "*" : "" + expansions[loop].getPercentage(j) );
		return sb.toString();
	}

	public Cards getBlackMarketDeck() {
		int total = 0;
		int j = 0;
		for ( loop = 0; loop < expansions.length; loop++)
			for ( j = 0; j < expansions[loop].size(); j++)
				if (expansions[loop].isBlackMarketAvailable(j)	&& 
						!expansions[loop].isPlaying(j))
					total++;
		Cards blackMarket = new Cards(total, Cards.IS_NOT_SET);
		total = 0;
		Rand.resetSeed();
		int randomize = Rand.randomInt(blackMarket.size());
		for ( loop = 0; loop < expansions.length; loop++)
			for ( j = 0; j < expansions[loop].size(); j++)
				if (expansions[loop].isBlackMarketAvailable(j) &&
						!expansions[loop].isPlaying(j)) {
					while (blackMarket.getName(randomize) != null)
						randomize = Rand.randomInt(blackMarket.size());
					blackMarket.setCard(randomize, expansions[loop].getCard(j));
				}
		return blackMarket;
	}

	
	public int[] getCardLocation(String cardName) {
		for ( int i = 0 ; i < expansions.length ; i++ )
			for ( int j = 0 ; j < expansions[i].size() ; j++ )
				if ( expansions[i].getName(j).equals(cardName) )
					return new int[] { i, j};
		return new int[] { -1, -1};
	}
	
	public int[] getPresetLocation(String presetName) {
		for ( int i = 0 ; i < presets.length ; i++ )
			for ( int j = 0 ; j < presets[i].size() ; j++ )
				if ( presets[i].getPresetName(j).equals(presetName) )
					return new int[] { i, j};
		return new int[] { -1, -1};
	}
	
	private int[] getCardInfo(String card) {
		//#debug dominizer
		System.out.println("the read card info: " + card);
		return new int[] {
				Integer.valueOf(card.substring(
						card.indexOf(SettingsRecordStorage.MEDIUM_SPLITTER) + 1,
						card.indexOf(SettingsRecordStorage.SMALL_SPLITTER))).intValue(),
				Integer.valueOf(card.substring(
						card.indexOf(SettingsRecordStorage.SMALL_SPLITTER) + 1)).intValue() };
	}

	public String getCardsUsedForExpansionAsSave() {
		sb = new StringBuffer(numberOfCardsFromExp.length);
		for ( loop = 0 ; loop < numberOfCardsFromExp.length ; loop++ )
			sb.append(numberOfCardsFromExp[loop]);
		return sb.toString();
	}

	public String getCurrentAsPresetSave(int playingSet) {
		if (selectedCards == null)
			return "";
		StringBuffer sb = new StringBuffer(50);
		for ( loop = 0; loop < expansions.length; loop++ )
			for (int j = 0; j < expansions[loop].size(); j++ )
				if ( expansions[loop].isPlayingSet(j, playingSet) )
					sb.append("" + SettingsRecordStorage.MEDIUM_SPLITTER
						+ loop + SettingsRecordStorage.SMALL_SPLITTER + j);
		return sb.toString();
	}

	public Cards getCurrentlySelected(int playingSet) throws DominionException {
		return getCurrentlySelected(playingSet, Cards.COMPARE_PREFERRED);
	}

	public Cards getCurrentlySelected(int playingSet, int sortMethod) throws DominionException {
		// TODO make selectedcards
		if (selectedCards == null | selectedCards.size() != numberOfRandomCards)
			throw new DominionException("No currently selected cards.");
		return sortCards(selectedCards, sortMethod);
	}

	public Cards getExpansion(int expansion) {
		return expansions[expansion];
	}

	public boolean[] getExpansionPlayingStates() {
		return playingExpansions;
	}

	public String getExpansionPlayingStatesAsSave() {
		sb = new StringBuffer(playingExpansions.length);
		for ( loop = 0 ; loop < numberOfCardsFromExp.length ; loop++ )
			sb.append(playingExpansions[loop] ? "1" : "0");
		return sb.toString();
	}

	public int getExpansions() {
		return expansions.length;
	}

	public int getLinearCardIndex(int i) {
		int tmp = 0;		
		if (i < 0)
			return -1;
		for ( int j = 0 ; j < expansions.length ; j++ ) {
			tmp += expansions[j].size();
			if ( i < tmp )
				return i - tmp + expansions[j].size();
		}
		return -1;
	}

	public int getLinearExpansionIndex(int i) {
		int tmp = 0;		
		if (i < 0)
			return -1;
		for ( int j = 0 ; j < expansions.length ; j++ ) {
			tmp += expansions[j].size();
			if ( i < tmp )
				return j;
		}
		return -1;
	}

	public int[] getNumberOfExpansionCards() {
		return numberOfCardsFromExp;
	}

	/**
	 * @return the numberOfRandomCards
	 */
	public int getNumberOfRandomCards() {
		return numberOfRandomCards;
	}

	public CardPresets getPreset(int preset) {
		return presets[preset];
	}

	public String getPresetAsInfo(int presetDeck, int preset) {
		StringBuffer sb = new StringBuffer(200);
		if ( presets[presetDeck].getExpansion() > -1 ) {
			sb.append(presets[presetDeck].getPresetName(preset) + ":\n");
		}
		for ( loop = 0 ; loop < presets[presetDeck].size(preset) ; loop++) {
			sb.append("" + expansions[presets[presetDeck].getPreset(preset)[loop][0]].getName(
					presets[presetDeck].getPreset(preset)[loop][1]) + "\n");
		}
		return sb.toString();
	}

	public String getSelectedInfo(int playingSet) {
		if (selectedCards == null)
			return "";
		StringBuffer sb = new StringBuffer(50);
		int action = 0, attack = 0, reaction = 0, treasury = 0, victory = 0, duration = 0;
		for ( int i = 0 ; i < expansions.length ; i++ )
			for ( loop = 0 ; loop < expansions[i].size() ; loop++ ) {
				if ( expansions[i].isPlayingSet(loop, playingSet) ) {
					if (expansions[i].isType(loop, Cards.TYPE_ACTION))
						action++;
					if (expansions[i].isType(loop, Cards.TYPE_ATTACK))
						attack++;
					if (expansions[i].isType(loop, Cards.TYPE_REACTION))
						reaction++;
					if (expansions[i].isType(loop, Cards.TYPE_TREASURY))
						treasury++;
					if (expansions[i].isType(loop, Cards.TYPE_VICTORY))
						victory++;
					if (expansions[i].isType(loop, Cards.TYPE_DURATION))
						duration++;
				}
			}
		sb.append("Action    : ");
		if (action < 10)
			sb.append(" ");
		sb.append("" + action + " / " + selectedCards.size() + "\n");
		sb.append("Attack    : ");
		if (attack < 10)
			sb.append(" ");
		sb.append("" + attack + " / " + selectedCards.size() + "\n");
		sb.append("Reaction  : ");
		if (reaction < 10)
			sb.append(" ");
		sb.append("" + reaction + " / " + selectedCards.size() + "\n");
		sb.append("Treasury  : ");
		if (treasury < 10)
			sb.append(" ");
		sb.append("" + treasury + " / " + selectedCards.size() + "\n");
		sb.append("Victory   : ");
		if (victory < 10)
			sb.append(" ");
		sb.append("" + victory + " / " + selectedCards.size() + "\n");
		sb.append("Duration  : ");
		if (duration < 10)
			sb.append(" ");
		sb.append("" + duration + " / " + selectedCards.size());
		return sb.toString();
	}

	public boolean hasBlackMarketPlaying() {
		return expansions[PROMO].isPlaying(0);
	}
	
	public int presetSize() {
		return presets.length;
	}

	public void randomizeCards(int playingSet) throws DominionException {
		randomizeCards(playingSet, Cards.COMPARE_PREFERRED);
	}
	
	public boolean selectCard(int playingSet, int exp, int card, int placement) {
		//#debug dominizer
		System.out.println("try: " + exp + " - " + card);
		if ( expansions[exp].isAvailable(card) 
				& !selectedCards.contains(expansions[exp].getName(card))
				& !expansions[exp].isPlaying(card) ) {
			expansions[exp].setPlaying(card, playingSet);
			if ( placement == -1 ) {
				for ( int i = 0 ; i < selectedCards.size() ; i++ )
					if ( selectedCards.getName(i) == null ) {
						selectedCards.setCard(i, expansions[exp].getCard(card));
						return true;
					}
			} else {
				selectedCards.setCard(placement, expansions[exp].getCard(card));
				return true;
			}
		}
		return false;
	}
	
	public int ensurePercentageCards(int playingSet, int exp) {
		int selected = 0;
		Rand.resetSeed();
		for ( int i = 0 ; i < expansions[exp].size() ; i++ ) { 
			switch ( expansions[exp].getPercentage(i) ) {
			case 10:
				if ( selectCard(playingSet, exp, i, -1) )
					selected++;
			case 0:
				break;
			default:
				if ( Rand.randomInt(10) < expansions[exp].getPercentage(i) )
					if ( selectCard(playingSet, exp, i, -1) )
						selected++;
			}
		}
		//#debug dominizer
		System.out.println("used " + selected + " from expansion: " + exp);
		return selected;
	}

	public void randomizeCards(int playingSet, int sortMethod) throws DominionException {
		checkAvailability();
		Cards.COMPARE_PREFERRED = sortMethod;
		int i = 0, selectedElement = 0, selected = 0, tmpSum = 0;
		selectedCards = new Cards(numberOfRandomCards, Cards.IS_NOT_SET);
		Rand.resetSeed(); 
		//#debug dominizer
		System.out.println("using hold cards");
		selected += useHoldCards(playingSet);
		//#debug dominizer
		System.out.println("using percentage cards");
		for ( i = 0 ; i < expansions.length ; i++ )
			selected += ensurePercentageCards(playingSet, i);
		//#debug dominizer
		System.out.println("using minimum expansion cards, already selected: " + selected);
		for ( i = 0 ; i < expansions.length ; i++ ) {
			//#debug dominizer
			System.out.println("trying expansion: " + i + " - State: " + playingExpansions[i] + " - Number: " + numberOfCardsFromExp[i]);
			if ( playingExpansions[i] & 0 < numberOfCardsFromExp[i] & expansions[i].size() > 0 ) {
				//#debug dominizer
				System.out.println("selecting for expansion: " + i + " already chosen: "+ selectedCards.fromExpansion(i));
				while ( selectedCards.fromExpansion(i) < numberOfCardsFromExp[i] && selected < numberOfRandomCards ) {
					//#debug dominizer
					System.out.println("selecting for expansion12: " +i+ " already chosen: "+ selectedCards.fromExpansion(i));
					selectedElement = Rand.randomInt(expansions[i].size());
					if ( selectCard(playingSet, i, selectedElement, selected) ) {
						selected++;
						//#debug dominizer
						System.out.println("selected: " + i + " - " + selected);
					}
				}
			}
		}
		Rand.resetSeed();
		//#debug dominizer
		System.out.println("using randomizing cards");
		while ( selected < numberOfRandomCards ) {
			selectedElement = Rand.randomInt(TOTAL_CARDS);
			tmpSum = getLinearExpansionIndex(selectedElement);
			selectedElement = getLinearCardIndex(selectedElement);
			if ( selectCard(playingSet, tmpSum, selectedElement, selected) ) {
				//#debug dominizer
				System.out.println("choosing expansion: " + tmpSum + ". together with card: " + selectedElement);
				selected++;
			}
		}
	}

	private void readResource(int exp, String fileName, int totalCards) {
		expansions[exp] = new Cards(totalCards, Cards.IS_SET);
		StringBuffer sb = new StringBuffer();
		String tmp = "      ";
		InputStreamReader isr = null;
		int start = 0;
		int cardRead = 0;
		try {
			isr = new InputStreamReader(getClass().getResourceAsStream("/" + fileName), "UTF8");
			int ch;
			while ((ch = isr.read()) > -1) {
				sb.append((char) ch);
				if ((char) ch == ';') {
					// #debug info
					//System.out.println("processing " + sb.toString());
					expansions[exp].setName(cardRead, sb.toString().substring(
							start, sb.toString().indexOf(":", start)).trim());
					expansions[exp].setExpansion(exp);
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setCost(cardRead, 
							Integer.parseInt(sb.toString().substring(
									start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					tmp = sb.toString().substring(start, sb.toString().indexOf(":", start));
					expansions[exp].setType(cardRead, Cards.TYPE_ACTION, parseType(tmp, Cards.TYPE_ACTION));
					expansions[exp].setType(cardRead, Cards.TYPE_VICTORY, parseType(tmp, Cards.TYPE_VICTORY));
					expansions[exp].setType(cardRead, Cards.TYPE_ATTACK, parseType(tmp, Cards.TYPE_ATTACK));
					expansions[exp].setType(cardRead, Cards.TYPE_TREASURY, parseType(tmp, Cards.TYPE_TREASURY));
					expansions[exp].setType(cardRead, Cards.TYPE_REACTION, parseType(tmp, Cards.TYPE_REACTION));
					expansions[exp].setType(cardRead, Cards.TYPE_DURATION, parseType(tmp, Cards.TYPE_DURATION));
					start = sb.toString().indexOf(":", start) + 1;
					tmp = sb.toString().substring(start, sb.toString().indexOf(";", start));
					expansions[exp].setAddInfo(cardRead, Cards.ADDS_CARDS, parseInformation(tmp , Cards.ADDS_CARDS));
					expansions[exp].setAddInfo(cardRead, Cards.ADDS_ACTIONS, parseInformation(tmp , Cards.ADDS_ACTIONS));
					expansions[exp].setAddInfo(cardRead, Cards.ADDS_BUYS, parseInformation(tmp , Cards.ADDS_BUYS));
					expansions[exp].setAddInfo(cardRead, Cards.ADDS_COINS, parseInformation(tmp , Cards.ADDS_COINS));
					expansions[exp].setAddInfo(cardRead, Cards.ADDS_TRASH, parseInformation(tmp , Cards.ADDS_TRASH));
					expansions[exp].setAddInfo(cardRead, Cards.ADDS_CURSE, parseInformation(tmp , Cards.ADDS_CURSE));
					expansions[exp].setAddInfo(cardRead, Cards.ADDS_POTIONS, parseInformation(tmp , Cards.ADDS_POTIONS));
					sb.delete(0, sb.toString().length());
					start = 0;
					//#debug dominizer
					System.out.println("expansions[" + exp + "].name("
							+ cardRead + "): "
							+ expansions[exp].getName(cardRead) + ". Action? "
							+ expansions[exp].isType(cardRead, Cards.TYPE_ACTION) + ". Attack? "
							+ expansions[exp].isType(cardRead, Cards.TYPE_ATTACK)
							+ ". Reaction? "
							+ expansions[exp].isType(cardRead, Cards.TYPE_REACTION)
							+ ". Victory? "
							+ expansions[exp].isType(cardRead, Cards.TYPE_VICTORY)
							+ ". Treasury? "
							+ expansions[exp].isType(cardRead, Cards.TYPE_TREASURY)
							+ ". Duration? "
							+ expansions[exp].isType(cardRead, Cards.TYPE_DURATION));
					cardRead++;
				} else if ((char) ch == '\n') {
					sb.delete(0, sb.toString().length() - 1);
				}
			}
			if (isr != null)
				isr.close();
		} catch (Exception ex) {
			//#debug dominizer
			System.out.println("exception on reading");
		}
	}
	
	private boolean parseType(String type, int whichInfo) {
		if ( whichInfo ==  Cards.TYPE_ACTION && -1 < type.indexOf("c") )
			return true;
		else if ( whichInfo == Cards.TYPE_VICTORY && -1 < type.indexOf("v") )
			return true;
		else if ( whichInfo == Cards.TYPE_TREASURY && -1 < type.indexOf("t") )
			return true;
		else if ( whichInfo == Cards.TYPE_ATTACK && -1 < type.indexOf("a") )
			return true;
		else if ( whichInfo == Cards.TYPE_REACTION && -1 < type.indexOf("r") )
			return true;
		else if ( whichInfo == Cards.TYPE_DURATION && -1 < type.indexOf("d") )
			return true;
		else if ( whichInfo == Cards.TYPE_POTION && -1 < type.indexOf("p") )
			return true;
		return false;
	}
	
	private int parseInformation(String information, int whichInfo) {
		if ( whichInfo == Cards.ADDS_CARDS && -1 < information.indexOf("d") )
			return parseInt(information.substring(information.indexOf("d")));
		else if ( whichInfo == Cards.ADDS_ACTIONS && -1 < information.indexOf("a") )
			return parseInt(information.substring(information.indexOf("a")));
		else if ( whichInfo == Cards.ADDS_COINS && -1 < information.indexOf("c") )
			return parseInt(information.substring(information.indexOf("c")));
		else if ( whichInfo == Cards.ADDS_BUYS && -1 < information.indexOf("b") )
			return parseInt(information.substring(information.indexOf("b")));
		else if ( whichInfo == Cards.ADDS_TRASH && -1 < information.indexOf("t") )
			return parseInt(information.substring(information.indexOf("t")));
		else if ( whichInfo == Cards.ADDS_CURSE && -1 < information.indexOf("u") )
			return parseInt(information.substring(information.indexOf("u")));
		else if ( whichInfo == Cards.ADDS_POTIONS && -1 < information.indexOf("p") )
			return parseInt(information.substring(information.indexOf("p")));
		return 0;
	}
	
	private int parseInt(String info) {
		if ( info.length() > 1 ) {
			try {
				return Integer.parseInt(info.substring(1,2));
			} catch (Exception e) {
				return 1;
			}
		}
		return 1;
	}

	private void readSettings() {
		SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.preset"));
		if (SettingsRecordStorage.instance().data() == null) {
			//#debug dominizer
			System.out.println("Read user settings: settings is null");
			presets[3] = null;
		} else {
			//#debug dominizer
			System.out.println("Read user settings: settings is size=" + SettingsRecordStorage.instance().data().size());
			presets[3] = new CardPresets(SettingsRecordStorage.instance().data().size());
			int[][] preset;
			int numberOfPresets = 0;
			int start;
			for (int i = 0; i < SettingsRecordStorage.instance().data().size(); i++) {
				//#debug dominizer
				System.out.println("presets: " + SettingsRecordStorage.instance().data().elementAt(i).toString());
				start = 0;
				preset = new int[10][2];
				for (int k = 0; k < 10; k++) {
					start = SettingsRecordStorage.instance().data().elementAt(i).toString().indexOf(
									SettingsRecordStorage.MEDIUM_SPLITTER,
									start + 1);
					if (k == 9)
						preset[k] = getCardInfo(SettingsRecordStorage.instance().data().elementAt(i).toString().substring(start));
					else
						preset[k] = getCardInfo(SettingsRecordStorage.instance().data().elementAt(i).toString().substring(start,
								SettingsRecordStorage.instance().data().elementAt(i).toString().indexOf(SettingsRecordStorage.MEDIUM_SPLITTER, start + 1)));
				}
				presets[3].setPreset(numberOfPresets,
						SettingsRecordStorage.instance().data().elementAt(i).toString().substring(0,
								SettingsRecordStorage.instance().data().elementAt(i).toString().indexOf(SettingsRecordStorage.BIG_SPLITTER)),
								preset);
				numberOfPresets++;
			}
		}
		//#debug dominizer
		System.out.println("finished reading presets successfully\nstart reading expansion states");
		SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.settings"));
		String tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.expansions"));
		//#debug dominizer
		System.out.println("expansions: " + tmp);
		if (tmp != null) {
			for ( loop = 0 ; loop < tmp.length() ; loop++ ) 
				playingExpansions[loop] = tmp.substring(loop, loop + 1).equals("1");
		}
		//#debug dominizer
		System.out.println("finished reading expansion states successfully\nstart reading number of cards");
		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.expansions.usedcards"));
		//#debug dominizer
		System.out.println("usedcards: " + tmp);
		if (tmp != null) {
			for ( loop = 0 ; loop < tmp.length() ; loop++ ) 
				setCardsUsedForExpansion(loop, Integer.parseInt(tmp.substring(loop, loop + 1)));
		}
		//#debug dominizer
		System.out.println("finished reading number of cards succesfully\nstart reading available cards");

		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.available"));
		//#debug dominizer
		System.out.println("available from " + tmp);
		if (tmp != null) {
			for ( loop = 0 ; loop < tmp.length() ; loop++ )
				expansions[getLinearExpansionIndex(loop)].setAvailable(
						getLinearCardIndex(loop), tmp.substring(loop, loop + 1).equals("1"));
		}
		//#debug dominizer
		System.out.println("finished reading available cards\nread cards percentages");

		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.percentage"));
		//#debug dominizer
		System.out.println("percentages from " + tmp);
		if (tmp != null) {
			for ( loop = 0; loop < tmp.length() ; loop++)
				if ( tmp.substring(loop, loop + 1).equals("*") )
					expansions[getLinearExpansionIndex(loop)].setPercentage(getLinearCardIndex(loop), 10);
				else
					expansions[getLinearExpansionIndex(loop)].setPercentage(getLinearCardIndex(loop), 
							Integer.parseInt(tmp.substring(loop, loop + 1)));
		}
		//#debug dominizer
		System.out.println("finished reading cards percentages succesfully\nstart reading preferred sort");
		
		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.preferredsort"));
		//#debug dominizer
		System.out.println("sort: " + tmp);
		if (tmp != null) {
			Cards.COMPARE_PREFERRED = Integer.parseInt(tmp);
		}
		//#debug dominizer
		System.out.println("finished reading preferred sort succesfully");
	}

	public void resetIsPlaying(int playingSet) {
		for (int i = 0; i < expansions.length; i++)
			for (int j = 0; j < expansions[i].size(); j++)
				if ( playingSet == 0 )
					expansions[i].setPlaying(j, 0);
				else if ( expansions[i].isPlayingSet(j, playingSet) & !expansions[i].isHold(j, playingSet))
					expansions[i].setPlaying(j, 0);
	}

	public boolean selectPreset(int presetDeck, int preset) {
		resetIsPlaying(0);
		//#debug dominizer
		System.out.println("fetching preset: " + presetDeck + " and " + preset);
		selectedCards = new Cards(presets[presetDeck].size(preset),
				Cards.IS_NOT_SET);
		if (presetDeck > presets.length | preset >= presets[presetDeck].size())
			return false;
		for (int i = 0; i < presets[presetDeck].size(preset); i++) {
			//#debug dominizer
			System.out.println("selecting expansion: " + presets[presetDeck].getPresetCardExpansion(preset, i) + " and card: " + presets[presetDeck].getPresetCardPlacement(preset, i));
			//#debug dominizer
			System.out.println("card: " + expansions[presets[presetDeck].getPresetCardExpansion(preset, i)].getName(presets[presetDeck].getPresetCardPlacement(preset, i)));
			selectedCards.setCard(i, expansions[presets[presetDeck].getPresetCardExpansion(preset, i)].getCard(presets[presetDeck].getPresetCardPlacement(preset, i)));
		}
		return true;
	}

	public boolean selectPreset(String presetName) {
		for (int i = 0; i < presets.length; i++)
			for (int j = 0; j < presets[i].size(); j++)
				if (presets[i].getPresetName(j).equals(presetName))
					return selectPreset(i, j);
		return false;
	}

	public void setCardsUsedForExpansion(int expansion, int numberOfCards) {
		numberOfCardsFromExp[expansion] = numberOfCards;
	}

	public void setExpansionPlayingState(boolean[] isAvailable) {
		for ( loop = 0; loop < playingExpansions.length; loop++)
			setExpansionPlayingState(loop, isAvailable[loop]);
	}

	public void setExpansionPlayingState(int exp, boolean isAvailable) {
		if (playingExpansions[exp] != isAvailable) {
			//#debug dominizer
			System.out.println("setting playing state: " + exp + " = " + isAvailable);
			playingExpansions[exp] = isAvailable;
			for ( int i = 0; i < expansions[exp].size() ; i++) {
				expansions[exp].setAvailable(i, isAvailable);
				expansions[exp].setBlackMarketAvailable(i, isAvailable);
			}
		}
	}

	/**
	 * @param numberOfRandomCards
	 *            the number of cards that should be randomized to set
	 */
	public void setNumberOfRandomCards(int numberOfRandomCards) {
		this.numberOfRandomCards = numberOfRandomCards;
	}

	public Cards sortCards(Cards cards, int sortMethod) {
		Object[] tmp = null;
		for (int j = 0; j < cards.size(); j++) {
			for (int i = j + 1; i < cards.size(); i++) {
				if (Cards.compare(cards.getCard(i), cards.getCard(j), sortMethod) < 0) {
					tmp = (Object[]) cards.getCard(j);
					cards.setCard(j, cards.getCard(i));
					cards.setCard(i, tmp);
				}
			}
		}
		return cards;
	}
	
	public int useHoldCards(int playingSet) {
		int card = 0;
		for ( int i = 0 ; i < expansions.length ; i++ ) {
			for ( int j = 0 ; j < expansions[i].size() ; j++ ) {
				if ( expansions[i].isHold(j, playingSet) ) {
					selectCard(playingSet, i, j, -1);
					card++;
				}
			}
			//#debug dominizer
			System.out.println("used " + card + " from expansion " + i);
		}
		return card;
	}
	
	public static String getExpansionImageName(int expansion) {
		switch (expansion) {
		case Dominion.BASE:
			return "ba";
		case Dominion.PROMO:
			return "pr";
		case Dominion.INTRIGUE:
			return "in";
		case Dominion.SEASIDE:
			return "se";
		case Dominion.ALCHEMY:
			return "al";
		case Dominion.PROSPERITY:
			return "po";
		default:
			return null;
		}
	}
	
	public static String getExpansionName(int expansion) {
		switch (expansion) {
		case BASE:
			return Locale.get("base");
		case PROMO:
			return Locale.get("promo");
		case INTRIGUE:
			return Locale.get("intrigue");
		case SEASIDE:
			return Locale.get("seaside");
		case ALCHEMY:
			return Locale.get("alchemy");
		case PROSPERITY:
			return Locale.get("prosperity");
		default:
			return "";
		}
	}
	
	public static Image getExpansionImage(int expansion) {
		try {
			return Image.createImage("/" + Dominion.getExpansionImageName(expansion) + ".png");
		} catch (IOException exp) {
			return null;
		}
	}
	
	
}
