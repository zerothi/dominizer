package com;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import javax.microedition.lcdui.Image;

import de.enough.polish.util.Locale;

public class Dominion {
	private static Dominion dom = null;
	public static final int TOTAL_CARDS = 79;
	public static final int BASE = 0; // has 25 cards
	public static final int INTRIGUE = 1; // has 25 cards
	public static final int SEASIDE = 2; // has 26 cards
	public static final int ALCHEMY = 3;
	public static final int PROSPERITY = 4;
	public static final int PROMO = 5; // has 3 cards
	public static final int USER = 10;
	public static boolean RANDOMIZE_COMPLETELY_NEW = true;
	private Random selector = null;
	
	private Cards[] expansions = null;
	private Cards selectedCards = null;
	private int[][] holdCards = new int[10][2];
	private CardPresets[] presets = null;
	private int numberOfRandomCards = 10;
	private boolean[] playingExpansions = new boolean[] { true, true, true, true, true, true };
	private int[] numberOfCardsFromExp = new int[] { 0, 0, 0, 0, 0, 0 };
	private StringBuffer sb;

	
	public static Dominion I() {
		if (dom == null)
			dom = new Dominion();
		return dom;
	}
	
	private Dominion() {
		selector = new Random(2005023);
		expansions = new Cards[6];
		for ( int i = 0 ; i < holdCards.length ; i++ ) {
			holdCards[i][0] = -1;
			holdCards[i][1] = -1;
		}
		/* Promo cards doesn't have preset! The
		 * last is used when reading user presets!
		 */ 
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
		//#debug info
		System.out.println("reading base");
		readResource(BASE, "base", 25);
		//#debug info
		System.out.println("size base: " + expansions[BASE].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("intrigue"));
		//#debug info
		System.out.println("reading intrigue");
		readResource(INTRIGUE, "intrigue", 25);
		//#debug info
		System.out.println("size intrigue: " + expansions[INTRIGUE].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("seaside"));
		//#debug info
		System.out.println("reading seaside");
		readResource(SEASIDE, "seaside", 26);
		//#debug info
		System.out.println("size seaside: " + expansions[SEASIDE].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("alchemy"));
		//#debug info
		System.out.println("reading alchemy");
		readResource(ALCHEMY, "alchemy", 0); // real 13
		//#debug info
		System.out.println("size alchemy: " + expansions[ALCHEMY].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("prosperity"));
		//#debug info
		System.out.println("reading prosperity");
		readResource(PROSPERITY, "prosperity", 0); // real supposedly 25
		//#debug info
		System.out.println("size prosperity: " + expansions[PROSPERITY].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("promo"));
		//#debug info
		System.out.println("reading promo");
		readResource(PROMO, "promo", 3);
		//#debug info
		System.out.println("size promo: " + expansions[PROMO].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.cards.settings"));
		//#debug info
		System.out.println("reading settings cards");
		readSettings();
	}

	private void checkAvailability() throws DominionException {
		int tmp, sum;
		sum = 0;
		tmp = 0;
		for (int i = 0; i < expansions.length; i++) {
			sum += tmp;
			tmp = 0;
			for (int j = 0; j < expansions[i].size(); j++) {
				if (expansions[i].isAvailable(j) & !expansions[i].isPlaying(j))
					tmp++;
			}
			if (tmp < numberOfCardsFromExp[i] & playingExpansions[i]) {
				String t = getExpansionName(i);
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
		for (int i = 0; i < expansions.length; i++)
			for (int j = 0; j < expansions[i].size(); j++)
				sb.append("" + (expansions[i].isAvailable(j) ? "1" : "0"));
		return sb.toString();
	}
	
	public String getPercentagesAsSave() {
		StringBuffer sb = new StringBuffer(TOTAL_CARDS);
		for (int i = 0; i < expansions.length; i++)
			for (int j = 0; j < expansions[i].size(); j++)
				sb.append( expansions[i].getPercentage(j) == 10 ? "*" : "" + expansions[i].getPercentage(j) );
		return sb.toString();
	}

	public Cards getBlackMarketDeck() {
		int total = 0;
		for (int i = 0; i < expansions.length; i++)
			for (int j = 0; j < expansions[i].size(); j++)
				if (expansions[i].isBlackMarketAvailable(j)	&& 
						!expansions[i].isPlaying(j))
					total++;
		Cards blackMarket = new Cards(total, Cards.IS_NOT_SET);
		total = 0;
		selector.setSeed(System.currentTimeMillis());
		int randomize = selector.nextInt(blackMarket.size());
		for (int i = 0; i < expansions.length; i++)
			for (int j = 0; j < expansions[i].size(); j++)
				if (expansions[i].isBlackMarketAvailable(j) &&
						!expansions[i].isPlaying(j)) {
					while (blackMarket.getName(randomize) != null)
						randomize = selector.nextInt(blackMarket.size());
					blackMarket.setCard(randomize, expansions[i].getCard(j));
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
		//#debug info
		System.out.println("the read card info: " + card);
		return new int[] {
				Integer.valueOf(card.substring(
						card.indexOf(SettingsRecordStorage.MEDIUM_SPLITTER) + 1,
						card.indexOf(SettingsRecordStorage.SMALL_SPLITTER))).intValue(),
				Integer.valueOf(card.substring(
						card.indexOf(SettingsRecordStorage.SMALL_SPLITTER) + 1)).intValue() };
	}

	public String getCardsUsedForExpansionAsSave() {
		sb = new StringBuffer(6);
		sb.append(numberOfCardsFromExp[0]);
		sb.append(numberOfCardsFromExp[1]);
		sb.append(numberOfCardsFromExp[2]);
		sb.append(numberOfCardsFromExp[3]);
		sb.append(numberOfCardsFromExp[4]);
		sb.append(numberOfCardsFromExp[5]);
		return sb.toString();
	}

	public String getCurrentAsPresetSave() {
		if (selectedCards == null)
			return "";
		StringBuffer sb = new StringBuffer(50);
		for (int i = 0; i < expansions.length; i++)
			for (int j = 0; j < expansions[i].size(); j++)
				for (int k = 0; k < selectedCards.size(); k++)
					if (selectedCards.getName(k).equals(expansions[i].getName(j)))
						sb.append("" + SettingsRecordStorage.MEDIUM_SPLITTER
								+ i + SettingsRecordStorage.SMALL_SPLITTER + j);
		return sb.toString();
	}

	public Cards getCurrentlySelected() throws DominionException {
		return getCurrentlySelected(Cards.COMPARE_PREFERRED);
	}

	public Cards getCurrentlySelected(int sortMethod) throws DominionException {
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
		sb = new StringBuffer(6);
		sb.append(playingExpansions[0] ? "1" : "0");
		sb.append(playingExpansions[1] ? "1" : "0");
		sb.append(playingExpansions[2] ? "1" : "0");
		sb.append(playingExpansions[3] ? "1" : "0");
		sb.append(playingExpansions[4] ? "1" : "0");
		sb.append(playingExpansions[5] ? "1" : "0");
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
			if ( i < tmp ) {
				//#debug info
				System.out.println("the returned statement is Card: " + (i - tmp + expansions[j].size()));
				return i - tmp + expansions[j].size();
			}
		}
		return -1;
	}

	public int getLinearExpansionIndex(int i) {
		int tmp = 0;		
		if (i < 0)
			return -1;
		for ( int j = 0 ; j < expansions.length ; j++ ) {
			tmp += expansions[j].size();
			if ( i < tmp ) {
				//#debug info
				System.out.println("the returned statement is Exp: " + j);
				return j;
			}
				
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
		for (int i = 0; i < presets[presetDeck].size(preset); i++) {
			sb.append("" + expansions[presets[presetDeck].getPreset(preset)[i][0]].getName(
					presets[presetDeck].getPreset(preset)[i][1]) + "\n");
		}
		return sb.toString();
	}

	public String getSelectedInfo() {
		if (selectedCards == null)
			return "";
		StringBuffer sb = new StringBuffer(50);
		int action = 0, attack = 0, reaction = 0, treasury = 0, victory = 0, duration = 0;
		for (int i = 0; i < selectedCards.size(); i++) {
			if (selectedCards.isAction(i))
				action++;
			if (selectedCards.isAttack(i))
				attack++;
			if (selectedCards.isReaction(i))
				reaction++;
			if (selectedCards.isTreasure(i))
				treasury++;
			if (selectedCards.isVictory(i))
				victory++;
			if (selectedCards.isDuration(i))
				duration++;
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
	
	public boolean isHold(String cardName) {
		int[] tmp = getCardLocation(cardName);
		for ( int i = 0 ; i < holdCards.length ; i++ )
			if ( holdCards[i][0] == tmp[0] && holdCards[i][1] == tmp[1] )
				return true;
		return false;
	}
	
	public boolean holdCard(int exp, int card) {
		int i = 0;
		for ( i = 0 ; i < holdCards.length ; i++ ) {
			if ( holdCards[i][0] == exp & holdCards[i][1] == card ) {
				holdCards[i][0] = -1;
				holdCards[i][1] = -1;
				return false;
			}
		}
		for ( i = 0 ; i < holdCards.length ; i++ ) {
			if ( holdCards[i][0] == -1 ) {
				holdCards[i][0] = exp;
				holdCards[i][1] = card;
				return true;
			}
		}
		return false;
	}
	
	public boolean holdCard(String cardName) {
		int[] tmp = getCardLocation(cardName);
		return holdCard(tmp[0], tmp[1]);
	}

	private boolean isTrue(String test) {
		return test.equals("0") ? false : true;
	}

	public int presetSize() {
		return presets.length;
	}

	public void randomizeCards() throws DominionException {
		randomizeCards(Cards.COMPARE_PREFERRED);
	}
	
	public boolean selectCard(int exp, int card, int placement) {
		//#debug info
		System.out.println("try: " + exp + " - " + card);
		if ( expansions[exp].isAvailable(card) 
				& !selectedCards.contains(expansions[exp].getName(card))
				& !expansions[exp].isPlaying(card) ) {
			expansions[exp].setPlaying(card, true);
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
	
	public int ensurePercentageCards(int exp) {
		int selected = 0;
		selector.setSeed(System.currentTimeMillis());
		for ( int i = 0 ; i < expansions[exp].size() ; i++ ) { 
			switch ( expansions[exp].getPercentage(i) ) {
			case 10:
				if ( selectCard(exp, i, -1) )
					selected++;
			case 0:
				break;
			default:
				if ( selector.nextInt(10) < expansions[exp].getPercentage(i) )
					if ( selectCard(exp, i, -1) )
						selected++;
			}
		}
		//#debug info
		System.out.println("used " + selected + " from expansion: " + exp);
		return selected;
	}

	public void randomizeCards(int sortMethod) throws DominionException {
		checkAvailability();
		Cards.COMPARE_PREFERRED = sortMethod;
		int i = 0;
		selectedCards = new Cards(numberOfRandomCards, Cards.IS_NOT_SET);
		int selectedElement = 0;
		selector.setSeed(System.currentTimeMillis());
		int selected = 0;
		int tmpSum = 0;
		//#debug info
		System.out.println("using hold cards");
		for ( i = 0 ; i < expansions.length ; i++ )
			selected += useHoldCards(i);
		//#debug info
		System.out.println("using percentage cards");
		for ( i = 0 ; i < expansions.length ; i++ )
			selected += ensurePercentageCards(i);
		selector.setSeed(System.currentTimeMillis());
		//#debug info
		System.out.println("using minimum expansion cards");
		for ( i = 0 ; i < expansions.length ; i++ ) {
			if ( playingExpansions[i] & 0 < numberOfCardsFromExp[i] & expansions[i].size() > 0) {
				while ( selectedCards.fromExpansion(i) < numberOfCardsFromExp[i] && selected < numberOfRandomCards ) {
					selectedElement = selector.nextInt(expansions[i].size());
					if ( selectCard(i, selectedElement, selected) ) {
						selected++;
						//#debug info
						System.out.println("selected: " + i + " - " + selected);
					}
				}
			}
		}
		//#debug info
		System.out.println("using randomizing cards");
		while ( selected < numberOfRandomCards ) {
			selectedElement = selector.nextInt(TOTAL_CARDS);
			tmpSum = getLinearExpansionIndex(selectedElement);
			selectedElement = getLinearCardIndex(selectedElement);
			if ( selectCard(tmpSum, selectedElement, selected) ) {
				//#debug info
				System.out.println("choosing expansion: " + tmpSum + ". together with card: " + selectedElement);
				selected++;
			}
		}
	}

	private void readResource(int exp, String fileName, int totalCards) {
		expansions[exp] = new Cards(totalCards, Cards.IS_SET);
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = null;
		int start = 0;
		int cardRead = 0;
		try {
			//#debug info
			System.out.println("reading " + fileName);
			isr = new InputStreamReader(getClass().getResourceAsStream("/" + fileName), "UTF8");
			int ch;
			while ((ch = isr.read()) > -1) {
				sb.append((char) ch);
				if ((char) ch == ';') {
					//#debug info
					System.out.println("processing " + sb.toString());
					expansions[exp].setName(cardRead, sb.toString().substring(
							start, sb.toString().indexOf(":", start)).trim());
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setExpansion(exp); // have removed the need for the expansion names! Consider removing them!
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setCost(cardRead, 
							Integer.parseInt(sb.toString().substring(
									start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setAction(cardRead,
							isTrue(sb.toString().substring(
									start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setVictory(cardRead,
							isTrue(sb.toString().substring(
									start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setTreasure(cardRead,
							isTrue(sb.toString().substring(
									start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setAttack(cardRead,
							isTrue(sb.toString().substring(
									start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setReaction(cardRead,
							isTrue(sb.toString().substring(
									start, sb.toString().indexOf(":", start))));
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setDuration(cardRead,
							isTrue(sb.toString().substring(
									start, sb.toString().indexOf(";", start))));
					sb.delete(0, sb.toString().length());
					start = 0;
					//#debug info
					System.out.println("expansions[" + exp + "].name("
							+ cardRead + "): "
							+ expansions[exp].getName(cardRead) + ". Action? "
							+ expansions[exp].isAction(cardRead) + ". Attack? "
							+ expansions[exp].isAttack(cardRead)
							+ ". Reaction? "
							+ expansions[exp].isReaction(cardRead)
							+ ". Victory? "
							+ expansions[exp].isVictory(cardRead)
							+ ". Treasury? "
							+ expansions[exp].isTreasure(cardRead)
							+ ". Duration? "
							+ expansions[exp].isDuration(cardRead));
					cardRead++;
				} else if ((char) ch == '\n') {
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

	private void readSettings() {
		SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.preset"));
		if (SettingsRecordStorage.instance().data() == null) {
			//#debug info
			System.out.println("Read user settings: settings is null");
			presets[3] = null;
		} else {
			//#debug info
			System.out.println("Read user settings: settings is size=" + SettingsRecordStorage.instance().data().size());
			presets[3] = new CardPresets(SettingsRecordStorage.instance()
					.data().size());
			int[][] preset;
			int numberOfPresets = 0;
			int start;
			for (int i = 0; i < SettingsRecordStorage.instance().data().size(); i++) {
				//#debug info
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
		//#debug info
		System.out.println("finished reading presets successfully\nstart reading expansion states");
		SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.settings"));
		String tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.expansions"));
		//#debug info
		System.out.println("expansions: " + tmp);
		if (tmp != null) {
			playingExpansions[0] = tmp.substring(0, 1).equals("1");
			playingExpansions[1] = tmp.substring(1, 2).equals("1");
			playingExpansions[2] = tmp.substring(2, 3).equals("1");
			playingExpansions[3] = tmp.substring(3, 4).equals("1");
			playingExpansions[4] = tmp.substring(4, 5).equals("1");
			playingExpansions[5] = tmp.substring(5).equals("1");
		}
		//#debug info
		System.out.println("finished reading expansion states successfully\nstart reading number of cards");
		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.expansions.usedcards"));
		//#debug info
		System.out.println("usedcards: " + tmp);
		if (tmp != null) {
			setCardsUsedForExpansion(0, Integer.parseInt(tmp.substring(0, 1)));
			setCardsUsedForExpansion(1, Integer.parseInt(tmp.substring(1, 2)));
			setCardsUsedForExpansion(2, Integer.parseInt(tmp.substring(2, 3)));
			setCardsUsedForExpansion(3, Integer.parseInt(tmp.substring(3, 4)));
			setCardsUsedForExpansion(4, Integer.parseInt(tmp.substring(4, 5)));
			setCardsUsedForExpansion(5, Integer.parseInt(tmp.substring(5, 6)));
		}
		//#debug info
		System.out.println("finished reading number of cards succesfully\nstart reading available cards");

		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.available"));
		//#debug info
		System.out.println("available from " + tmp);
		if (tmp != null) {
			int k = 0;
			for (int i = 0; i < expansions.length; i++)
				for (int j = 0; j < expansions[i].size(); j++) {
					expansions[i].setAvailable(j, tmp.substring(k, k + 1).equals("1"));
					k++;
				}
		}
		//#debug info
		System.out.println("finished reading available cards\nread cards percentages");

		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.percentage"));
		//#debug info
		System.out.println("percentages from " + tmp);
		if (tmp != null) {
			int k = 0;
			for (int i = 0; i < expansions.length; i++)
				for (int j = 0; j < expansions[i].size(); j++) {
					if ( tmp.substring(k, k + 1).equals("*") )
						expansions[i].setPercentage(j, 10);
					else
						expansions[i].setPercentage(j, Integer.parseInt(tmp.substring(k, k + 1)));
					k++;
				}
		}
		//#debug info
		System.out.println("finished reading cards percentages succesfully\nstart reading preferred sort");
		
		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.preferredsort"));
		//#debug info
		System.out.println("sort: " + tmp);
		if (tmp != null) {
			Cards.COMPARE_PREFERRED = Integer.parseInt(tmp);
		}
		//#debug info
		System.out.println("finished reading preferred sort succesfully");
	}
	
	public void resetHoldCards() {
		for ( int i = 0 ; i < holdCards.length ; i++ ) {
			holdCards[i][0] = -1;
			holdCards[i][1] = -1;
		}
	}

	public void resetIsPlaying(boolean resetAll) {
		for (int i = 0; i < expansions.length; i++)
			for (int j = 0; j < expansions[i].size(); j++)
				if (expansions[i].isPlaying(j))
					if (RANDOMIZE_COMPLETELY_NEW | selectedCards == null | resetAll)
						expansions[i].setPlaying(j, false);
					else
						for (int k = 0; k < selectedCards.size(); k++)
							if (expansions[i].getName(j).equals(selectedCards.getName(k)))
								expansions[i].setPlaying(j, false);

	}

	public boolean selectPreset(int presetDeck, int preset) {
		//#debug info
		System.out.println("fetching preset: " + presetDeck + " and " + preset);
		selectedCards = new Cards(presets[presetDeck].size(preset),
				Cards.IS_NOT_SET);
		if (presetDeck > presets.length | preset >= presets[presetDeck].size())
			return false;
		for (int i = 0; i < presets[presetDeck].size(preset); i++) {
			//#debug info
			System.out.println("selecting expansion: " + presets[presetDeck].getPresetCardExpansion(preset, i) + " and card: " + presets[presetDeck].getPresetCardPlacement(preset, i));
			//#debug info
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
		for (int i = 0; i < playingExpansions.length; i++)
			setExpansionPlayingState(i, isAvailable[i]);
	}

	public void setExpansionPlayingState(int exp, boolean isAvailable) {
		if (playingExpansions[exp] != isAvailable) {
			//#debug info
			System.out.println("setting playing state: " + exp + " = " + isAvailable);
			playingExpansions[exp] = isAvailable;
			for (int i = 0; i < expansions[exp].size(); i++) {
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
	
	public int useHoldCards(int expansion) {
		if ( selectedCards == null )
			return 0;
		int card = 0;
		for ( int i = 0 ; i < holdCards.length ; i++ ) {
			if ( holdCards[i][0] == expansion ) {
				for ( int j = 0 ; j < selectedCards.size() ; j++ )
					if ( selectedCards.getName(j) == null ) {
						selectCard(holdCards[i][0], holdCards[i][1], j);
						j = selectedCards.size();
					}
				card++;
			}
		}
		//#debug info
		System.out.println("used " + card + " from expansion " + expansion);
		return card;
	}
	
	public Image getCardTypeImage(int exp, int card) {
		try {
			return Image.createImage("/" + Dominion.getExpansionImageName(exp) + 
					Dominion.I().getExpansion(exp).getCardType(card) + ".png");
		} catch (IOException expc) {
			try {
				return Image.createImage("/" + Dominion.getExpansionImageName(exp) + ".png");
			} catch (IOException e) {
				return null;
			}
		}
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
