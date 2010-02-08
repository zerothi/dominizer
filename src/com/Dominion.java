package com;

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
	public static final int ALCHEMY = 4;
	public static final int PROSPERITY = 5;
	public static final int USER = 10;
	public static boolean RANDOMIZE_COMPLETELY_NEW = true;
	private Random selector = null;
	
	private Cards[] expansions = null;
	private Cards selectedCards = null;
	private int[][] holdCards = new int[10][2];
	private CardPresets[] presets = null;
	private int numberOfRandomCards = 10;
	private boolean[] playingExpansions = new boolean[] { true, true, true, true };
	private int[] numberOfCardsFromExp = new int[] { 0, 0, 0, 0 };
	private StringBuffer sb;

	
	public static Dominion I() {
		if (dom == null)
			dom = new Dominion();
		return dom;
	}
	
	private Dominion() {
		selector = new Random(2005023);
		expansions = new Cards[4];
		for ( int i = 0 ; i < holdCards.length ; i++ ) {
			holdCards[i][0] = -1;
			holdCards[i][1] = -1;
		}
		/* Promo cards doesn't have preset! The
		 * last is used when reading user presets!
		 */ 
		presets = new CardPresets[4]; 
		presets[0] = new CardPresets(5);
		presets[0].setPreset(0, Locale.get("preset.base.FirstGame"), new int[][] { 
			new int[] { 0,  2 }, new int[] { 0, 11 }, new int[] { 0, 12 }, new int[] { 0, 13 }, new int[] { 0, 14 }, 
			new int[] { 0, 16 }, new int[] { 0, 17 }, new int[] { 0, 21 }, new int[] { 0, 23 }, new int[] { 0, 24 } });
		presets[0].setPreset(1, Locale.get("preset.base.BigMoney"), new int[][] { 
			new int[] { 0, 0 }, new int[] { 0,  1 }, new int[] { 0,  3 }, new int[] { 0,  4 }, new int[] { 0,  6 }, 
			new int[] { 0, 9 }, new int[] { 0, 11 }, new int[] { 0, 13 }, new int[] { 0, 15 }, new int[] { 0, 20 } });
		presets[0].setPreset(2, Locale.get("preset.base.Interaction"), new int[][] { 
			new int[] { 0,  1 }, new int[] { 0,  3 }, new int[] { 0,  5 }, new int[] { 0,  7 }, new int[] { 0, 10 }, 
			new int[] { 0, 12 }, new int[] { 0, 14 }, new int[] { 0, 18 }, new int[] { 0, 23 }, new int[] { 0, 24 } });
		presets[0].setPreset(3, Locale.get("preset.base.SizeDistortion"), new int[][] {
			new int[] { 0,  2 }, new int[] { 0,  4 }, new int[] { 0,  6 }, new int[] { 0,  8 }, new int[] { 0,  9 }, 
			new int[] { 0, 19 }, new int[] { 0, 21 }, new int[] { 0, 22 }, new int[] { 0, 23 }, new int[] { 0, 24 } });
		presets[0].setPreset(4, Locale.get("preset.base.VillageSquare"), new int[][] {
			new int[] { 0,  1 }, new int[] { 0,  2 }, new int[] { 0,  7 }, new int[] { 0, 10 }, new int[] { 0, 11 }, 
			new int[] { 0, 16 }, new int[] { 0, 17 }, new int[] { 0, 20 }, new int[] { 0, 21 }, new int[] { 0, 23 } });
		presets[1] = new CardPresets(6);
		presets[1].setPreset(0, Locale.get("preset.intrigue.VictoryDance"), new int[][] {
			new int[] { 2,  1 }, new int[] { 2,  5 }, new int[] { 2,  6 }, new int[] { 2,  7 }, new int[] { 2,  8 }, 
			new int[] { 2,  9 }, new int[] { 2, 12 }, new int[] { 2, 13 }, new int[] { 2, 15 }, new int[] { 2, 23 } });
		presets[1].setPreset(1, Locale.get("preset.intrigue.SecretSchemes"), new int[][] {
			new int[] { 2,  2 }, new int[] { 2,  7 }, new int[] { 2,  8 }, new int[] { 2, 13 }, new int[] { 2, 14 }, 
			new int[] { 2, 17 }, new int[] { 2, 18 }, new int[] { 2, 20 }, new int[] { 2, 21 }, new int[] { 2, 22 } });
		presets[1].setPreset(2, Locale.get("preset.intrigue.BestWishes"), new int[][] {
			new int[] { 2,  3 }, new int[] { 2,  4 }, new int[] { 2,  9 }, new int[] { 2, 15 }, new int[] { 2, 17 }, 
			new int[] { 2, 18 }, new int[] { 2, 20 }, new int[] { 2, 21 }, new int[] { 2, 23 }, new int[] { 2, 24 } });
		presets[1].setPreset(3, Locale.get("preset.intrigue.Deconstruction"), new int[][] {
			new int[] { 0, 16 }, new int[] { 0, 18 }, new int[] { 0, 19 }, new int[] { 0, 20 }, new int[] { 2,  1 }, 
			new int[] { 2, 10 }, new int[] { 2, 14 }, new int[] { 2, 16 }, new int[] { 2, 19 }, new int[] { 2, 20 } });
		presets[1].setPreset(4, Locale.get("preset.intrigue.HandMadness"), new int[][] {
			new int[] { 0,  1 }, new int[] { 0,  3 }, new int[] { 0,  5 }, new int[] { 0, 12 }, new int[] { 0, 13 }, 
			new int[] { 2,  4 }, new int[] { 2, 11 }, new int[] { 2, 12 }, new int[] { 2, 18 }, new int[] { 2, 20 } });
		presets[1].setPreset(5, Locale.get("preset.intrigue.Underlings"), new int[][] {
			new int[] { 0,  2 }, new int[] { 0,  7 }, new int[] { 0, 10 }, new int[] { 0, 22 }, new int[] { 2,  0 }, 
			new int[] { 2,  9 }, new int[] { 2, 11 }, new int[] { 2, 12 }, new int[] { 2, 13 }, new int[] { 2, 18 } });
		presets[2] = new CardPresets(6);
		presets[2].setPreset(0, Locale.get("preset.seaside.HighSeas"), new int[][] {
			new int[] { 3,  1 }, new int[] { 3,  2 }, new int[] { 3,  4 }, new int[] { 3,  5 }, new int[] { 3,  8 }, 
			new int[] { 3,  9 }, new int[] { 3, 11 }, new int[] { 3, 17 }, new int[] { 3, 20 }, new int[] { 3, 25 } });
		presets[2].setPreset(1, Locale.get("preset.seaside.BuriedTreasure"), new int[][] {
			new int[] { 3,  0 }, new int[] { 3,  3 }, new int[] { 3,  6 }, new int[] { 3, 10 }, new int[] { 3, 15 }, 
			new int[] { 3, 16 }, new int[] { 3, 21 }, new int[] { 3, 22 }, new int[] { 3, 24 }, new int[] { 3, 25 } });
		presets[2].setPreset(2, Locale.get("preset.seaside.Shipwrecks"), new int[][] {
			new int[] { 3,  7 }, new int[] { 3, 12 }, new int[] { 3, 13 }, new int[] { 3, 14 }, new int[] { 3, 16 }, 
			new int[] { 3, 18 }, new int[] { 3, 19 }, new int[] { 3, 20 }, new int[] { 3, 23 }, new int[] { 3, 24 } });
		presets[2].setPreset(3, Locale.get("preset.seaside.ReachForTomorrow"), new int[][] {
			new int[] { 0,  0 }, new int[] { 0,  2 }, new int[] { 0,  5 }, new int[] { 0, 21 }, new int[] { 0, 18 }, 
			new int[] { 3,  3 }, new int[] { 3,  7 }, new int[] { 3, 11 }, new int[] { 3, 19 }, new int[] { 3, 22 } });
		presets[2].setPreset(4, Locale.get("preset.seaside.Repetition"), new int[][] {
			new int[] { 0,  3 }, new int[] { 0,  7 }, new int[] { 0, 12 }, new int[] { 0, 24 }, new int[] { 3,  2 }, 
			new int[] { 3,  5 }, new int[] { 3, 15 }, new int[] { 3, 16 }, new int[] { 3, 17 }, new int[] { 3, 23 } });
		presets[2].setPreset(5, Locale.get("preset.seaside.GiveAndTake"), new int[][] {
			new int[] { 0, 10 }, new int[] { 0, 11 }, new int[] { 0, 15 }, new int[] { 0, 22 }, new int[] { 3,  0 }, 
			new int[] { 3,  6 }, new int[] { 3,  8 }, new int[] { 3,  9 }, new int[] { 3, 18 }, new int[] { 3, 20 } });
		// #debug info
		System.out.println("reading base");
		readResource(BASE, "base", 25);
		// #debug info
		System.out.println("size base: " + expansions[0].size());
		// #debug info
		System.out.println("reading promo");
		readResource(PROMO, "promo", 2);
		// #debug info
		System.out.println("size promo: " + expansions[1].size());
		// #debug info
		System.out.println("reading intrigue");
		readResource(INTRIGUE, "intrigue", 25);
		// #debug info
		System.out.println("size intrigue: " + expansions[2].size());
		// #debug info
		System.out.println("reading seaside");
		readResource(SEASIDE, "seaside", 26);
		// #debug info
		System.out.println("size seaside: " + expansions[3].size());
		// #debug info
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

	
	private int[] getCardLocation(String cardName) {
		for ( int i = 0 ; i < expansions.length ; i++ )
			for ( int j = 0 ; j < expansions[i].size() ; j++ )
				if ( expansions[i].getName(j).equals(cardName) )
					return new int[] { i, j};
		return new int[] { -1, -1};
	}
	
	private int[] getCardInfo(String card) {
		// #debug info
		System.out.println("the read card info: " + card);
		return new int[] {
				Integer.valueOf(card.substring(
						card.indexOf(SettingsRecordStorage.MEDIUM_SPLITTER) + 1,
						card.indexOf(SettingsRecordStorage.SMALL_SPLITTER))).intValue(),
				Integer.valueOf(card.substring(
						card.indexOf(SettingsRecordStorage.SMALL_SPLITTER) + 1)).intValue() };
	}

	public String getCardsUsedForExpansionAsSave() {
		sb = new StringBuffer(4);
		sb.append(numberOfCardsFromExp[0]);
		sb.append(numberOfCardsFromExp[1]);
		sb.append(numberOfCardsFromExp[2]);
		sb.append(numberOfCardsFromExp[3]);
		return sb.toString();
	}

	public String getCurrentAsPresetSave() {
		if (this.selectedCards == null)
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
		sb = new StringBuffer(4);
		sb.append(playingExpansions[0] ? "1" : "0");
		sb.append(playingExpansions[1] ? "1" : "0");
		sb.append(playingExpansions[2] ? "1" : "0");
		sb.append(playingExpansions[3] ? "1" : "0");
		return sb.toString();
	}

	public int getExpansions() {
		return expansions.length;
	}

	public int getLinearCardIndex(int i) {
		if (i < 0)
			return -1;
		if (i < expansions[0].size())
			return i;
		else if (i < expansions[0].size() + expansions[1].size())
			return i - expansions[0].size();
		else if (i < expansions[0].size() + expansions[1].size()
				+ expansions[2].size())
			return i - expansions[1].size() - expansions[0].size();
		else if (i < expansions[0].size() + expansions[1].size()
				+ expansions[2].size() + expansions[3].size())
			return i - expansions[2].size() - expansions[1].size()
					- expansions[0].size();
		else
			return -1;
	}

	public int getLinearExpansionIndex(int i) {
		if (i < 0)
			return -1;
		if (i < expansions[0].size())
			return 0;
		else if (i < expansions[0].size() + expansions[1].size())
			return 1;
		else if (i < expansions[0].size() + expansions[1].size()
				+ expansions[2].size())
			return 2;
		else if (i < expansions[0].size() + expansions[1].size()
				+ expansions[2].size() + expansions[3].size())
			return 3;
		else
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
		StringBuffer sb = new StringBuffer(140);
		for (int i = 0; i < presets[presetDeck].size(preset); i++) {
			sb.append("" + expansions[presets[presetDeck].getPreset(preset)[i][0]].getName(
					presets[presetDeck].getPreset(preset)[i][1]) + "\n");
		}
		return sb.toString();
	}

	public String getSelectedInfo() {
		if (this.selectedCards == null)
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
		sb.append("" + duration + " / " + selectedCards.size() + "\n");
		return sb.toString();
	}

	public boolean hasBlackMarketPlaying() {
		return expansions[1].isPlaying(0);
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
		// #debug info
		System.out.println("try: " + exp + " - " + exp);
		if ( expansions[exp].isAvailable(card) 
				& !selectedCards.contains(expansions[exp].getName(card))
				& !expansions[exp].isPlaying(card) ) {
			expansions[exp].setPlaying(card, true);
			if ( placement == -1 ) {
				for ( int i = 0 ; i < selectedCards.size() ; i++ )
					if ( selectedCards.getName(i) == null ) {
						this.selectedCards.setCard(i, expansions[exp].getCard(card));
						return true;
					}
			} else {
				this.selectedCards.setCard(placement, expansions[exp].getCard(card));
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
		this.selectedCards = new Cards(numberOfRandomCards, Cards.IS_NOT_SET);
		int selectedElement = 0;
		this.selector.setSeed(System.currentTimeMillis());
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
			if ( playingExpansions[i] & 0 < numberOfCardsFromExp[i] ) {
				while ( selectedCards.fromExpansion(i) < numberOfCardsFromExp[i] && selected < numberOfRandomCards ) {
					selectedElement = selector.nextInt(expansions[i].size());
					if ( selectCard(i, selectedElement, selected) ) {
						selected++;
						// #debug info
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
				// #debug info
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
			// #debug info
			System.out.println("reading " + fileName);
			isr = new InputStreamReader(this.getClass().getResourceAsStream("/" + fileName), "UTF8");
			int ch;
			while ((ch = isr.read()) > -1) {
				sb.append((char) ch);
				if ((char) ch == ';') {
					// #debug info
					System.out.println("processing " + sb.toString());
					expansions[exp].setName(cardRead, sb.toString().substring(
							start, sb.toString().indexOf(":", start)).trim());
					start = sb.toString().indexOf(":", start) + 1;
					expansions[exp].setExpansion(sb.toString().substring(
							start, sb.toString().indexOf(":", start)));
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
					// #debug info
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
			// #debug info
			System.out.println("exception on reading:" + ex);
		}
	}

	private void readSettings() {
		SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.preset"));
		if (SettingsRecordStorage.instance().data() == null) {
			// #debug info
			System.out.println("Read user settings: settings is null");
			presets[3] = null;
		} else {
			// #debug info
			System.out.println("Read user settings: settings is size=" + SettingsRecordStorage.instance().data().size());
			presets[3] = new CardPresets(SettingsRecordStorage.instance()
					.data().size());
			int[][] preset;
			int numberOfPresets = 0;
			int start;
			for (int i = 0; i < SettingsRecordStorage.instance().data().size(); i++) {
				// #debug info
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
		// #debug info
		System.out.println("finished reading presets successfully\nstart reading expansion states");
		SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.settings"));
		String tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.expansions"));
		// #debug info
		System.out.println("expansions: " + tmp);
		if (tmp != null) {
			playingExpansions[0] = tmp.substring(0, 1).equals("1");
			playingExpansions[1] = tmp.substring(1, 2).equals("1");
			playingExpansions[2] = tmp.substring(2, 3).equals("1");
			playingExpansions[3] = tmp.substring(3).equals("1");
		}
		// #debug info
		System.out.println("finished reading expansion states successfully\nstart reading number of cards");
		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.expansions.usedcards"));
		// #debug info
		System.out.println("usedcards: " + tmp);
		if (tmp != null) {
			setCardsUsedForExpansion(0, Integer.parseInt(tmp.substring(0, 1)));
			setCardsUsedForExpansion(1, Integer.parseInt(tmp.substring(1, 2)));
			setCardsUsedForExpansion(2, Integer.parseInt(tmp.substring(2, 3)));
			setCardsUsedForExpansion(3, Integer.parseInt(tmp.substring(3, 4)));
		}
		// #debug info
		System.out.println("finished reading number of cards succesfully\nstart reading available cards");

		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.available"));
		// #debug info
		System.out.println("available from " + tmp);
		if (tmp != null) {
			int k = 0;
			for (int i = 0; i < expansions.length; i++)
				for (int j = 0; j < expansions[i].size(); j++) {
					expansions[i].setAvailable(j, tmp.substring(k, k + 1).equals("1"));
					k++;
				}
		}
		// #debug info
		System.out.println("finished reading available cards\nread cards percentages");

		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.percentage"));
		// #debug info
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
		// #debug info
		System.out.println("finished reading cards percentages succesfully\nstart reading preferred sort");
		
		tmp = SettingsRecordStorage.instance().readKey(Locale.get("rms.preferredsort"));
		// #debug info
		System.out.println("sort: " + tmp);
		if (tmp != null) {
			Cards.COMPARE_PREFERRED = Integer.parseInt(tmp);
		}
		// #debug info
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
		// #debug info
		System.out.println("fetching preset: " + presetDeck + " and " + preset);
		this.selectedCards = new Cards(presets[presetDeck].size(preset),
				Cards.IS_NOT_SET);
		if (presetDeck > presets.length | preset >= presets[presetDeck].size())
			return false;
		for (int i = 0; i < presets[presetDeck].size(preset); i++) {
			// #debug info
			System.out.println("selecting expansion: " + presets[presetDeck].getPresetCardExpansion(preset, i) + " and card: " + presets[presetDeck].getPresetCardPlacement(preset, i));
			// #debug info
			System.out.println("card: " + expansions[presets[presetDeck].getPresetCardExpansion(preset, i)].getName(presets[presetDeck].getPresetCardPlacement(preset, i)));
			selectedCards.setCard(i, expansions[presets[presetDeck].getPresetCardExpansion(preset, i)].getCard(presets[presetDeck].getPresetCardPlacement(preset, i)));
		}
		return true;
	}

	public boolean selectPreset(String presetName) {
		for (int i = 0; i < presets.length; i++)
			for (int j = 0; j < presets[i].size(); j++)
				if (presets[i].getPresetName(j).equals(presetName))
					return this.selectPreset(i, j);
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
			// #debug info
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
}
