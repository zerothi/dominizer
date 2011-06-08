package com.util;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.microedition.lcdui.Image;

import com.GaugeForm;

import de.enough.polish.util.Locale;

public class Dominion {
	private static Dominion dom = null;
	public static int TOTAL_CARDS = 25 + // BASE 
									25 + // INTRIGUE
									26 + // SEASIDE
									12 + // ALCHEMY
									25 + // PROSPERITY
									13 + // CORNUCOPIA
									3; // PROMOS
	public static final int BASE = 0;       // has 25 cards
	public static final int INTRIGUE = 1;   // has 25 cards
	public static final int SEASIDE = 2;    // has 26 cards
	public static final int ALCHEMY = 3;    // has 12 cards (the 13th card Potion is not counted)
	public static final int PROSPERITY = 4; // has 25 cards
	public static final int CORNUCOPIA = 5; // has 13 cards
	public static final int PROMO = 6;      // has 3 cards
	public static final int USER = PROMO + 1;       // always have to be the highest number!
	public static final int RAND_EXPANSION_CARDS = 1;
	public static final int RAND_PERCENTAGE_CARDS = 2;
	public static final int RAND_CONDITION = 4;
	public static final int RAND_PREVENT = 8;
	public static final int RAND_HOLD = 16;
	public static boolean RANDOMIZE_COMPLETELY_NEW = true;
	public static int SETS_SAVE = 10;
	public static int CURRENT_SET = 0;
	
	public static Cards[] expansions = null;
	public static Cards selectedCards = null;
	public static int selected = 0;
	public static Condition condition = null;
	public static CardPresets[] presets = null;
	public static int numberOfRandomCards = 10;
	
	public static boolean[] playingExpansions = null;
	
	public static int[] numberOfCardsFromExp = null;
	
	private Dominion() {
		//imgC = new ImageCreator();
		playingExpansions = new boolean[USER+1];
		numberOfCardsFromExp = new int[USER+1];
		for ( int i = 0 ; i < playingExpansions.length ; i++ ) {
			playingExpansions[i] = true;
			numberOfCardsFromExp[i] = 0;			
		}
		expansions = new Cards[USER+1];
		presets = new CardPresets[USER]; // as promo always is the last expansion!
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
		presets[ALCHEMY] = new CardPresets(6);
		presets[ALCHEMY].setExpansion(ALCHEMY);
		presets[ALCHEMY].setPreset(0, Locale.get("preset.alchemy.ForbiddenArts"), new int[][] {
			new int[] { 0,  2 }, new int[] { 0,  5 }, new int[] { 0,  8 }, new int[] { 0,  9 }, new int[] { 0, 19 }, 
			new int[] { 0, 20 }, new int[] { 3,  2 }, new int[] { 3,  4 }, new int[] { 3,  7 }, new int[] { 3, 10 } });
		presets[ALCHEMY].setPreset(1, Locale.get("preset.alchemy.PotionMixers"), new int[][] {
			new int[] { 0,  2 }, new int[] { 0,  3 }, new int[] { 0,  7 }, new int[] { 0, 12 }, new int[] { 0, 17 }, 
			new int[] { 3,  0 }, new int[] { 3,  1 }, new int[] { 3,  4 }, new int[] { 3,  5 }, new int[] { 3, 10 } });
		presets[ALCHEMY].setPreset(2, Locale.get("preset.alchemy.ChemistryLesson"), new int[][] {
			new int[] { 0,  1 }, new int[] { 0, 11 }, new int[] { 0, 14 }, new int[] { 0, 16 }, new int[] { 0, 22 }, 
			new int[] { 0, 23 }, new int[] { 3,  0 }, new int[] { 3,  4 }, new int[] { 3,  6 }, new int[] { 3, 10 } });
		presets[ALCHEMY].setPreset(3, Locale.get("preset.alchemy.Servants"), new int[][] {
			new int[] { 1,  2 }, new int[] { 1,  6 }, new int[] { 1, 11 }, new int[] { 1, 13 }, new int[] { 1, 18 }, 
			new int[] { 3,  4 }, new int[] { 3,  7 }, new int[] { 3,  8 }, new int[] { 3,  9 }, new int[] { 3, 11 } });
		presets[ALCHEMY].setPreset(4, Locale.get("preset.alchemy.SecretResearch"), new int[][] {
			new int[] { 1,  1 }, new int[] { 1,  9 }, new int[] { 1, 11 }, new int[] { 1, 12 }, new int[] { 1, 17 }, 
			new int[] { 1, 20 }, new int[] { 3,  3 }, new int[] { 3,  5 }, new int[] { 3,  7 }, new int[] { 3, 10 } });
		presets[ALCHEMY].setPreset(5, Locale.get("preset.alchemy.PoolsToolsFools"), new int[][] {
			new int[] { 1,  0 }, new int[] { 1,  3 }, new int[] { 1,  8 }, new int[] { 1, 12 }, new int[] { 1, 21 }, 
			new int[] { 1, 24 }, new int[] { 3,  1 }, new int[] { 3,  2 }, new int[] { 3,  4 }, new int[] { 3,  8 } });
		presets[PROSPERITY] = new CardPresets(9);
		presets[PROSPERITY].setExpansion(PROSPERITY);
		presets[PROSPERITY].setPreset(0, Locale.get("preset.prosperity.Beginners"), new int[][] {
			new int[] { 4,  0 }, new int[] { 4,  4 }, new int[] { 4,  5 }, new int[] { 4,  7 }, new int[] { 4, 13 }, 
			new int[] { 4, 17 }, new int[] { 4, 18 }, new int[] { 4, 22 }, new int[] { 4, 23 }, new int[] { 4, 24 } });
		presets[PROSPERITY].setPreset(1, Locale.get("preset.prosperity.FriendlyInteractive"), new int[][] {
			new int[] { 4,  1 }, new int[] { 4,  2 }, new int[] { 4,  3 }, new int[] { 4,  6 }, new int[] { 4,  9 }, 
			new int[] { 4, 15 }, new int[] { 4, 18 }, new int[] { 4, 20 }, new int[] { 4, 21 }, new int[] { 4, 24 } });
		presets[PROSPERITY].setPreset(2, Locale.get("preset.prosperity.BigActions"), new int[][] {
			new int[] { 4,  2 }, new int[] { 4,  5 }, new int[] { 4,  8 }, new int[] { 4, 10 }, new int[] { 4, 11 }, 
			new int[] { 4, 12 }, new int[] { 4, 16 }, new int[] { 4, 17 }, new int[] { 4, 19 }, new int[] { 4, 21 } });
		presets[PROSPERITY].setPreset(3, Locale.get("preset.prosperity.BiggestMoney"), new int[][] {
			new int[] { 0,  0 }, new int[] { 0,  9 }, new int[] { 0, 13 }, new int[] { 0, 15 }, new int[] { 0, 18 }, 
			new int[] { 4,  0 }, new int[] { 4,  8 }, new int[] { 4, 12 }, new int[] { 4, 18 }, new int[] { 4, 22 } });
		presets[PROSPERITY].setPreset(4, Locale.get("preset.prosperity.TheKingsArmy"), new int[][] {
			new int[] { 0,  1 }, new int[] { 0,  5 }, new int[] { 0, 14 }, new int[] { 0, 18 }, new int[] { 0, 21 }, 
			new int[] { 4,  5 }, new int[] { 4,  7 }, new int[] { 4, 10 }, new int[] { 4, 17 }, new int[] { 4, 21 } });
		presets[PROSPERITY].setPreset(5, Locale.get("preset.prosperity.TheGoodLife"), new int[][] {
			new int[] { 0,  1 }, new int[] { 0,  2 }, new int[] { 0,  3 }, new int[] { 0,  8 }, new int[] { 0, 21 }, 
			new int[] { 4,  3 }, new int[] { 4,  4 }, new int[] { 4,  9 }, new int[] { 4, 13 }, new int[] { 4, 14 } });
		presets[PROSPERITY].setPreset(6, Locale.get("preset.prosperity.PathsToVictory"), new int[][] {
			new int[] { 1,  0 }, new int[] { 1,  7 }, new int[] { 1, 13 }, new int[] { 1, 17 }, new int[] { 1, 23 }, 
			new int[] { 4,  1 }, new int[] { 4,  4 }, new int[] { 4,  7 }, new int[] { 4, 13 }, new int[] { 4, 15 } });
		presets[PROSPERITY].setPreset(7, Locale.get("preset.prosperity.AllAlongTheWatchtower"), new int[][] {
			new int[] { 1,  1 }, new int[] { 1,  6 }, new int[] { 1, 10 }, new int[] { 1, 13 }, new int[] { 1, 20 }, 
			new int[] { 4,  9 }, new int[] { 4, 19 }, new int[] { 4, 20 }, new int[] { 4, 21 }, new int[] { 4, 23 } });
		presets[PROSPERITY].setPreset(8, Locale.get("preset.prosperity.LuckySeven"), new int[][] {
			new int[] { 1,  2 }, new int[] { 1,  3 }, new int[] { 1, 19 }, new int[] { 1, 22 }, new int[] { 1, 24 }, 
			new int[] { 4,  0 }, new int[] { 4,  5 }, new int[] { 4,  6 }, new int[] { 4, 10 }, new int[] { 4, 21 } });
		presets[CORNUCOPIA] = new CardPresets(6);
		presets[CORNUCOPIA].setExpansion(CORNUCOPIA);
		presets[CORNUCOPIA].setPreset(0, Locale.get("preset.cornucopia.BountyoftheHunt"), new int[][] {
			new int[] { 5,  4 }, new int[] { 5,  5 }, new int[] { 5,  7 }, new int[] { 5,  9 }, new int[] { 5, 11 }, 
			new int[] { 0,  2 }, new int[] { 0,  7 }, new int[] { 0, 12 }, new int[] { 0, 15 }, new int[] { 0, 17 } });
		presets[CORNUCOPIA].setPreset(1, Locale.get("preset.cornucopia.BadOmens"), new int[][] {
			new int[] { 5,  2 }, new int[] { 5,  3 }, new int[] { 5,  5 }, new int[] { 5,  8 }, new int[] { 5, 10 }, 
			new int[] { 0,  0 }, new int[] { 0,  1 }, new int[] { 0,  9 }, new int[] { 0, 18 }, new int[] { 0, 20 } });
		presets[CORNUCOPIA].setPreset(2, Locale.get("preset.cornucopia.TheJestersWorkshop"), new int[][] {
			new int[] { 5,  0 }, new int[] { 5,  1 }, new int[] { 5,  6 }, new int[] { 5,  8 }, new int[] { 5, 12 }, 
			new int[] { 0,  6 }, new int[] { 0,  9 }, new int[] { 0, 11 }, new int[] { 0, 16 }, new int[] { 0, 24 } });
		presets[CORNUCOPIA].setBaneCard(2, new int[] { 0 , 3 });
		presets[CORNUCOPIA].setPreset(3, Locale.get("preset.cornucopia.LastLaughs"), new int[][] {
			new int[] { 5,  1 }, new int[] { 5,  4 }, new int[] { 5,  6 }, new int[] { 5,  7 }, new int[] { 5, 8 }, 
			new int[] { 1, 11 }, new int[] { 1, 12 }, new int[] { 1, 13 }, new int[] { 1, 18 }, new int[] { 1, 19 } });
		presets[CORNUCOPIA].setPreset(4, Locale.get("preset.cornucopia.TheSpiceofLife"), new int[][] {
			new int[] { 5,  0 }, new int[] { 5,  5 }, new int[] { 5, 10 }, new int[] { 5, 11 }, new int[] { 5, 12 }, 
			new int[] { 1,  3 }, new int[] { 1,  4 }, new int[] { 1,  6 }, new int[] { 1, 10 }, new int[] { 1, 22 } });
		presets[CORNUCOPIA].setBaneCard(4, new int[] { 1 , 25 });
		presets[CORNUCOPIA].setPreset(5, Locale.get("preset.cornucopia.SmallVictories"), new int[][] {
			new int[] { 5,  2 }, new int[] { 5,  3 }, new int[] { 5,  7 }, new int[] { 5, 10 }, new int[] { 5, 11 }, 
			new int[] { 1,  2 }, new int[] { 1,  5 }, new int[] { 1,  6 }, new int[] { 1,  7 }, new int[] { 1, 13 } });
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("expansion.base"));
		//#debug dominizer
		System.out.println("reading base");
		readResource(BASE, "base", 25);
		//#debug dominizer
		System.out.println("size ba " + expansions[BASE].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("expansion.intrigue"));
		//#debug dominizer
		System.out.println("reading intrigue");
		readResource(INTRIGUE, "intrigue", 25);
		//#debug dominizer
		System.out.println("size intrigue: " + expansions[INTRIGUE].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("expansion.seaside"));
		//#debug dominizer
		System.out.println("reading seaside");
		readResource(SEASIDE, "seaside", 26);
		//#debug dominizer
		System.out.println("size seaside: " + expansions[SEASIDE].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("expansion.alchemy"));
		//#debug dominizer
		System.out.println("reading alchemy");
		readResource(ALCHEMY, "alchemy", 12); // The 13th card is a Potion which is always available
		//#debug dominizer
		System.out.println("size alchemy: " + expansions[ALCHEMY].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("expansion.prosperity"));
		//#debug dominizer
		System.out.println("reading prosperity");
		readResource(PROSPERITY, "prosperity", 25);
		//#debug dominizer
		System.out.println("size prosperity: " + expansions[PROSPERITY].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("expansion.cornucopia"));
		//#debug dominizer
		System.out.println("reading cornucopia");
		readResource(CORNUCOPIA, "cornucopia", 13);
		//#debug dominizer
		System.out.println("size cornucopia: " + expansions[CORNUCOPIA].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading") + " " + Locale.get("expansion.promo"));
		//#debug dominizer
		System.out.println("reading promo");
		readResource(PROMO, "promo", 3);
		//#debug dominizer
		System.out.println("size promo: " + expansions[PROMO].size());
		//#debug dominizer
		System.out.println("reading user");
		readResource(USER, "user", -1);
		//#debug dominizer
		System.out.println("size user: " + expansions[USER].size());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.cards.settings"));
		//#debug dominizer
		System.out.println("reading settings cards");
		readSettings();
	}
	
	
	public static void checkAvailability() throws DominionException {
		int tmp = 0, sum = 0, expL = expansions.length;
		for ( int i = 0; i < expL; i++) {
			tmp = 0;
			int expCl = expansions[i].size();
			for (int j = 0; j < expCl ; j++) {
				if (expansions[i].isAvailable(j) && expansions[i].isPlaying(j) == 0 )
					tmp++;
			}
			sum += tmp;
			if (tmp < numberOfCardsFromExp[i] && playingExpansions[i]) {
				String t = getExpansionName(i);
				throw new DominionException(Locale.get("alert.Randomization.Availability", t));
			}
		}
		if (sum < numberOfRandomCards ) {
			String t = "" + sum;
			throw new DominionException(Locale.get("alert.Randomization.TotalAvailability", t));
		}
	}
	
	public String getAvailableAsSave() {
		StringBuffer sb = new StringBuffer(TOTAL_CARDS);
		for ( int i = 0; i < expansions.length; i++)
			for (int j = 0; j < expansions[i].size(); j++)
				sb.append("" + (expansions[i].isAvailable(j) ? "1" : "0"));
		return sb.toString();
	}
	
	public Cards getBlackMarketDeck() {
		int total = 0;
		int j, i;
		for ( i = 0; i < expansions.length; i++)
			for ( j = 0; j < expansions[i].size(); j++)
				if (expansions[i].isBlackMarketAvailable(j)	&& 
						expansions[i].isPlaying(j) == 0 )
					total++;
		Cards blackMarket = new Cards(total, Cards.IS_NOT_SET);
		Rand.resetSeed();
		int randomize;
		for ( i = 0; i < expansions.length; i++)
			for ( j = 0; j < expansions[i].size(); j++)
				if (expansions[i].isBlackMarketAvailable(j) &&
						expansions[i].isPlaying(j) == 0 ) {
					do {
						randomize = Rand.randomInt(total);
					} while (blackMarket.getName(randomize) != null);
					blackMarket.setCard(randomize, expansions[i].getCard(j));
				}
		return blackMarket;
	}
	
	private static int[] getCardInfo(String card) {
		//#debug dominizer
		System.out.println("the read card info: " + card);
		if ( card.indexOf(SettingsRecordStorage.MEDIUM_SPLITTER, 1) > 0 ) {
			return new int[] {
					Integer.valueOf(card.substring(
							card.indexOf(SettingsRecordStorage.MEDIUM_SPLITTER) + 1,
							card.indexOf(SettingsRecordStorage.SMALL_SPLITTER)).trim()).intValue(),
					Integer.valueOf(card.substring(
							card.indexOf(SettingsRecordStorage.SMALL_SPLITTER) + 1,
							card.indexOf(SettingsRecordStorage.MEDIUM_SPLITTER, card.indexOf(SettingsRecordStorage.MEDIUM_SPLITTER) + 1)).trim()).intValue() };
		} else {
			return new int[] {
					Integer.valueOf(card.substring(
							card.indexOf(SettingsRecordStorage.MEDIUM_SPLITTER) + 1,
							card.indexOf(SettingsRecordStorage.SMALL_SPLITTER)).trim()).intValue(),
					Integer.valueOf(card.substring(
							card.indexOf(SettingsRecordStorage.SMALL_SPLITTER) + 1).trim()).intValue() };
		}
	}

	public int[] getCardLocation(String cardName) {
		int j;
		for ( int i = 0 ; i < expansions.length ; i++ )
			for ( j = 0 ; j < expansions[i].size() ; j++ )
				if ( cardName.equals(expansions[i].getName(j)) )
					return new int[] { i, j};
		return new int[] { -1, -1};
	}

	public String getCardsUsedForExpansionAsSave() {
		StringBuffer sb = new StringBuffer(numberOfCardsFromExp.length);
		for ( int i = 0 ; i < numberOfCardsFromExp.length ; i++ )
			sb.append(numberOfCardsFromExp[i]);
		return sb.toString();
	}

	public String getPlayingSetAsSave(int playingSet) {
		StringBuffer sb = new StringBuffer(66);
		int cards = 0;
		for ( int i = 0; i < expansions.length; i++ )
			for ( int j = 0; j < expansions[i].size(); j++ )
				if ( expansions[i].isPlayingSet(j, playingSet) ) {
					sb.append("" + SettingsRecordStorage.MEDIUM_SPLITTER + ( expansions[i].isHold(j) ? "H" : "" ) + ( expansions[i].isBaneCard(j) ? "B" : "" ) + i + SettingsRecordStorage.SMALL_SPLITTER + j);
					cards++;
				}
		sb.append(SettingsRecordStorage.MEDIUM_SPLITTER);
		if ( sb.toString().trim().length() < 7 )
			return null;
		return sb.toString().trim();
	}

	public Cards getSelectedCards(int playingSet) throws DominionException {
		return getSelectedCards(playingSet, Cards.COMPARE_PREFERRED);
	}
	public int getSelectedCardsSize(int playingSet) {
		int card = 0;
		for ( int i = 0 ; i < expansions.length ; i++ ) {
			for ( int j = 0 ; j < expansions[i].size() ; j++ ) {
				if ( expansions[i].isPlayingSet(j, playingSet) )
					card++;
			}
		}
		return card;
	}

	public Cards getSelectedCards(int playingSet, int sortMethod) throws DominionException {
		selectedCards = new Cards(getSelectedCardsSize(playingSet), Cards.IS_NOT_SET);
		int card = 0;
		for ( int i = 0 ; i < expansions.length ; i++ ) {
			for ( int j = 0 ; j < expansions[i].size() ; j++ ) {
				if ( expansions[i].isPlayingSet(j, playingSet) )
					selectedCards.setCard(card++, expansions[i].getCard(j) );
			}
		}
		if ( card < numberOfRandomCards ) {
			removePlayingSet(playingSet);
			// TODO fix exception to handle correct!	
			throw new DominionException(Locale.get("alert.NotEnoughSelectedCards"));
		}
		return sortCards(selectedCards, sortMethod);
	}

	public String getExpansionPlayingStatesAsSave() {
		StringBuffer sb = new StringBuffer(playingExpansions.length);
		for ( int i = 0 ; i < numberOfCardsFromExp.length ; i++ )
			sb.append(playingExpansions[i] ? "1" : "0");
		return sb.toString();
	}
	
	public static int getFreeCards() {
		int tmp = 0;
		int expL = expansions.length;
		for ( int i = 0 ; i < expL ; i++ ) {
			int expCl = expansions[i].size();
			for ( int j = 0 ; j < expCl ; j++ ) {
				if ( expansions[i].isPlaying(j) > 0 )
					tmp++;
			}
		}
		return tmp;
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

	public String getPercentagesAsSave() {
		StringBuffer sb = new StringBuffer(TOTAL_CARDS);
		for ( int i = 0; i < expansions.length; i++)
			for (int j = 0; j < expansions[i].size(); j++)
				sb.append( expansions[i].getPercentage(j) == 10 ? "*" : "" + expansions[i].getPercentage(j) );
		return sb.toString();
	}

	public CardPresets getPreset(int preset) {
		return presets[preset];
	}

	public String getPresetAsInfo(int presetDeck, int preset) {
		StringBuffer sb = new StringBuffer(200);
		if ( presets[presetDeck].getExpansion() > -1 ) {
			sb.append(presets[presetDeck].getPresetName(preset) + ":\n");
		}
		for ( int i = 0 ; i < presets[presetDeck].size(preset) ; i++) {
			sb.append("" + expansions[presets[presetDeck].getPreset(preset)[i][0]].getName(
					presets[presetDeck].getPreset(preset)[i][1]) + "\n");
		}
		if ( presets[presetDeck].getBaneCard(preset)[0] != -1 )
			sb.append("" + expansions[presets[presetDeck].getBaneCard(preset)[0]].getName(presets[presetDeck].getBaneCard(preset)[1]));
		return sb.toString();
	}
	
	public int[] getPresetLocation(String presetName) {
		for ( int i = 0 ; i < presets.length ; i++ )
			for ( int j = 0 ; j < presets[i].size() ; j++ )
				if ( presets[i].getPresetName(j).equals(presetName) )
					return new int[] { i, j};
		return new int[] { -1, -1};
	}
	
	public String getSelectedInfo(int playingSet) {
		if (selectedCards == null)
			return "";
		StringBuffer sb = new StringBuffer(50);
		try {
			getSelectedCards(playingSet);
		} catch (DominionException e) {
			// TODO is it necessary with a check here? do nothing
		}
		sb.append("Action    : " + selectedCards.getTypes(Cards.TYPE_ACTION) + " / " + selectedCards.size() + "\n");
		sb.append("Attack    : " + selectedCards.getTypes(Cards.TYPE_ATTACK) + " / " + selectedCards.size() + "\n");
		sb.append("Reaction  : " + selectedCards.getTypes(Cards.TYPE_REACTION) + " / " + selectedCards.size() + "\n");
		sb.append("Treasury  : " + selectedCards.getTypes(Cards.TYPE_TREASURY) + " / " + selectedCards.size() + "\n");
		sb.append("Victory   : " + selectedCards.getTypes(Cards.TYPE_VICTORY) + " / " + selectedCards.size() + "\n");
		sb.append("Duration  : " + selectedCards.getTypes(Cards.TYPE_DURATION) + " / " + selectedCards.size() + "\n");
		sb.append("+ Draws   :  " + selectedCards.getAdds(Cards.ADDS_CARDS) + "\n");
		sb.append("+ Action  :  " + selectedCards.getAdds(Cards.ADDS_ACTIONS) + "\n");
		sb.append("+ Buys    :  " + selectedCards.getAdds(Cards.ADDS_BUYS) + "\n");
		sb.append("+ Coins   :  " + selectedCards.getAdds(Cards.ADDS_COINS) + "\n");
		sb.append("+ Trash   :  " + selectedCards.getAdds(Cards.ADDS_TRASH) + "\n");
		sb.append("+ Curse   :  " + selectedCards.getAdds(Cards.ADDS_CURSE) + "\n");
		sb.append("+ Victory :  " + selectedCards.getAdds(Cards.ADDS_VICTORY_POINTS) + "\n");
		sb.append("+ Potions :  " + selectedCards.getAdds(Cards.ADDS_POTIONS));
		return sb.toString();
	}
	
	public boolean isBlackMarketPlaying() {
		return expansions[PROMO].isPlaying(0) > 0;
	}
	
	public boolean isSetPlaying(int playingSet) {
		for ( int i = 0 ; i < expansions.length ; i++ ) 
			for ( int j = 0 ; j < expansions[i].size() ; j++ ) {
				if ( expansions[i].isPlayingSet(j, playingSet) ) {
					//#debug dominizer
					System.out.println("checking playingset: " + playingSet + " and returning true");
					return true;
				}
			}
		//#debug dominizer
		System.out.println("checking playingset: " + playingSet + " and returning false");
		return false;
	}
	
	private int parseInformation(String information, int whichInfo) {
		try {
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
			else if ( whichInfo == Cards.ADDS_VICTORY_POINTS && -1 < information.indexOf("v") )
				return parseInt(information.substring(information.indexOf("v")));
			else if ( whichInfo == Cards.COST_POTIONS && -1 < information.indexOf("P") )
				return parseInt(information.substring(information.indexOf("P")));
		} catch (Exception e) {return 1;}
		return 0;
	}
	
	private int parseInt(String info) throws Exception {
		if ( info.length() > 2 ) {
			try {
				return Integer.parseInt(info.substring(1,3));
			} catch (Exception e) {
				return Integer.parseInt(info.substring(1,2));
			}
		} else if ( info.length() > 1 ) {
			return Integer.parseInt(info.substring(1,2));
		}
		return 1;
	}
	
	private boolean parseCondition(String option) {
		boolean fulfilled = true;
		//#debug dominizer
		System.out.println("condition parsing: " + option);
		for ( int i = 0 ; i < option.length() ; i++ ) {
			if ( option.substring(i, i + 1).equals("(") ) {
				int p = 0;
				for ( int j = i + 1 ; j < option.length() ; j++ ) {
					if ( option.charAt(j) == ')' && p == 0 ) {
						p = j;
						j = option.length();
					} else if ( option.charAt(j) == ')' ) {
						p--;
					} else if ( option.charAt(j) == '(' ) {
						p++;
					}
				}
				fulfilled = parseCondition(option.substring(i+1, p));
				i = p;
			} else if ( option.substring(i, i + 1).equals(")") ) {
				// do nothing. Has been parsed. Shouldn't happen either
			} else if ( option.substring(i, i + 1).equals("&") ) {
				fulfilled = fulfilled && parseCondition(option.substring(i+1));
				i = option.length();
			} else if ( option.substring(i, i + 1).equals("|") ) {
				fulfilled = fulfilled || parseCondition(option.substring(i+1));
				i = option.length();
			} else {
				// checking if it is an type condition
				for ( int j = 0 ; j < 7 ; j++ ) { // TODO update when cards are changed!
					if ( parseType(option.substring(i,i+1), j) ) {
						fulfilled = parseCardsOption(0, option.substring(i), j);
						//#debug dominizer
						System.out.println("parsing expansion condition0: " + option.substring(i) + " returned " + fulfilled);
						break;
					}
				}
				// checking if it is an adds condition
				for ( int j = 0 ; j < 9 ; j++ ) { 
					if ( parseInformation(option.substring(i,i+1), j) > 0 ) {
						fulfilled = parseCardsOption(1, option.substring(i), j);
						//#debug dominizer
						System.out.println("parsing expansion condition1: " + option.substring(i) + " returned " + fulfilled);
						break;
					}
				}
				// checking if it is an expansion condition
				try {
					int tmp = parseInt(" "+option.substring(0,3));
					// #debug dominizer
					//System.out.println("parsing expansion condition: " + ( tmp > 9 ? option.substring(1) : option ));
					fulfilled = parseCardsOption(2, ( tmp > 9 ? option.substring(1) : option ), tmp);
					//#debug dominizer
					System.out.println("parsing expansion condition2: " + ( tmp > 9 ? option.substring(1) : option ) + " returned " + fulfilled);
				} catch (Exception e) {}//do nothing as it is a check
				// to not jump over things that are already processed!
				i += 2;
			}
		}
		return fulfilled;
	}
	
	/*
	 * Remember that this also has meaning for ADDS!
	 */
	private boolean parseCardsOption(int typeOption, String option, int typeInfo) {
		try {
		if ( option.substring(1,2).equals("=") ) {
			if ( typeOption == 0 )
				return selectedCards.getTypes(typeInfo) == parseInt(option.substring(1));
			else if ( typeOption == 1 )
				return selectedCards.getAdds(typeInfo) == parseInt(option.substring(1));
			else if ( typeOption == 2 )
				return selectedCards.fromExpansion(typeInfo) == parseInt(option.substring(1));
		} else if ( option.substring(1,2).equals(">") ) {
			if ( typeOption == 0 )
				return selectedCards.getTypes(typeInfo) > parseInt(option.substring(1));
			else if ( typeOption == 1 )
				return selectedCards.getAdds(typeInfo) > parseInt(option.substring(1));
			else if ( typeOption == 2 )
				return selectedCards.fromExpansion(typeInfo) > parseInt(option.substring(1));
		} else if ( option.substring(1,2).equals("<") ) {
			if ( typeOption == 0 )
				return selectedCards.getTypes(typeInfo) < parseInt(option.substring(1));
			else if ( typeOption == 1 )
				return selectedCards.getAdds(typeInfo) < parseInt(option.substring(1));
			else if ( typeOption == 2 )
				return selectedCards.fromExpansion(typeInfo) < parseInt(option.substring(1));
		}
		} catch (Exception e) {}
		return false;
	}

	private boolean parseType(String type, int whichInfo) {
		if ( whichInfo == Cards.TYPE_ACTION && -1 < type.indexOf("C") )
			return true;
		else if ( whichInfo == Cards.TYPE_VICTORY && -1 < type.indexOf("V") )
			return true;
		else if ( whichInfo == Cards.TYPE_TREASURY && -1 < type.indexOf("T") )
			return true;
		else if ( whichInfo == Cards.TYPE_ATTACK && -1 < type.indexOf("A") )
			return true;
		else if ( whichInfo == Cards.TYPE_REACTION && -1 < type.indexOf("R") )
			return true;
		else if ( whichInfo == Cards.TYPE_DURATION && -1 < type.indexOf("D") )
			return true;
		return false;
	}

	public int presetSize() {
		return presets.length;
	}
	
	public void randomizeCards() throws DominionException {
		// Hold doesn't make a difference here, but still included! At the max some extra computation time
		randomizeCards(-1, RAND_EXPANSION_CARDS + RAND_PERCENTAGE_CARDS + RAND_HOLD, condition.getCurrentCondition());
	}
	
	public void randomizeCards(int playingSet) throws DominionException {
		randomizeCards(playingSet, RAND_EXPANSION_CARDS + RAND_PERCENTAGE_CARDS + RAND_HOLD, condition.getCurrentCondition());
	}
	
	public void randomizeCards(int playingSet, int randomizeMethod) throws DominionException {
		randomizeCards(playingSet, randomizeMethod, condition.getCurrentCondition());
	}
	
	public void randomizeCards(int playingSet, int randomizeMethod, int condition) throws DominionException {
		//#debug dominizer
		System.out.println("trying to randomize with " + playingSet + ". With method " + randomizeMethod + " and condition= " + condition);
		if ( (randomizeMethod & RAND_CONDITION) > 0 && ( condition < 0 || condition >= Dominion.condition.size() ) )
			throw new DominionException(Locale.get("alert.Condition.NoSelection"));
		int selectedElement = 0, tmpExp = 0, tmpPlayingSet = 0;
		
		if ( (randomizeMethod & RAND_PREVENT) > 0 ) {
			tmpPlayingSet = playingSet;
			playingSet = 90;
		} else if ( playingSet > 0 ) {
			resetIsPlaying(playingSet);
		}
		checkAvailability();
		selectedCards = new Cards(numberOfRandomCards, Cards.IS_NOT_SET);
		selected = 0;
		Rand.resetSeed();
		if ( playingSet < 0 )
			playingSet = getCurrentSet() + 1;
		
		/**
		 * RandomizeMethod contains a bit number 0 + 1 + 2 + 4 + 8...
		 * We compare them by using the bit wise AND operator
		 */
		if ( (randomizeMethod & RAND_HOLD) > 0 )
			useHoldCards(playingSet, tmpPlayingSet);
		if ( (randomizeMethod & RAND_EXPANSION_CARDS) > 0 )
			useMinimumExpansionCards(playingSet);
		if ( (randomizeMethod & RAND_PERCENTAGE_CARDS) > 0 )
			usePercentageCards(playingSet);
		if ( (randomizeMethod & RAND_CONDITION) > 0 ) {
			int i = 0;
			for ( i = 0 ; i < Condition.MAX_RUNS ; i++ ) {
				//#debug dominizer
				System.out.println("condition tried for the '"+i+"' time.");
				while ( selected < numberOfRandomCards ) {
					selectedElement = Rand.randomInt(TOTAL_CARDS);
					tmpExp = getLinearExpansionIndex(selectedElement);
					selectedElement = getLinearCardIndex(selectedElement);
					if ( selectCard(playingSet, tmpExp, selectedElement, selected) ) {
						selected++;
					}
				}
				if ( parseCondition(Dominion.condition.getCondition(condition)) ) {
					//#debug dominizer
					System.out.println("condition succeded");
					i = Condition.MAX_RUNS + Condition.MAX_RUNS;
				} else {
					resetIsPlaying(playingSet);
					selected = 0;
					if ( (randomizeMethod & RAND_HOLD) > 0 )
						useHoldCards(playingSet, tmpPlayingSet);
				}
			}
			if ( i < Condition.MAX_RUNS + Condition.MAX_RUNS ) {
				//#debug dominizer
				System.out.println("condition failed");
				throw new DominionException(Locale.get("randomize.condition.failed"));
			}
			//#debug dominizer
			System.out.println("condition succeded with condition"+condition+" with name: "+ Dominion.condition.getCondition(condition));
		} else {
			//#debug dominizer
			System.out.println("using randomizing cards with total cards " + TOTAL_CARDS + " selected: " + selected);
			while ( selected < numberOfRandomCards ) {
				selectedElement = Rand.randomInt(TOTAL_CARDS);
				tmpExp = getLinearExpansionIndex(selectedElement);
				selectedElement = getLinearCardIndex(selectedElement);
				if ( selectCard(playingSet, tmpExp, selectedElement, selected) ) {
					selected++;
				}
			}
		}
		if ( (randomizeMethod & RAND_PREVENT) > 0 ) {
			for ( int i = 0 ; i < expansions.length ; i++ )
				for ( int j = 0 ; j < expansions[i].size() ; j++ ) {
					if ( expansions[i].isPlayingSet(j, tmpPlayingSet) )
						expansions[i].setPlaying(j, 0);
					if ( expansions[i].isHold(j, playingSet) ) {
						expansions[i].setPlaying(j, tmpPlayingSet);
						expansions[i].setHoldCard(j, true);
					} else if ( expansions[i].isPlayingSet(j, playingSet) )
						expansions[i].setPlaying(j, tmpPlayingSet);
				}
			playingSet = tmpPlayingSet;
		}
		if ( selected < numberOfRandomCards ) {
			removePlayingSet(playingSet);
			throw new DominionException(Locale.get("alert.NotEnoughSelectedCards"));
		}
		randomizeBaneDeck(playingSet);
		//#debug dominizer
		System.out.println("exiting randomize method. found " + selected + " cards");
	}
	
	private void randomizeBaneDeck(int playingSet) throws DominionException {
		if ( expansions[CORNUCOPIA].isPlayingSet(12, playingSet) || ( expansions[PROMO].isPlayingSet(0, playingSet) && expansions[CORNUCOPIA].isBlackMarketAvailable(12) ) ) {
			//#debug dominizer
			System.out.println("adding a Bane Kingdom pile for set" + playingSet);
			int possible = 0;
			for ( int i = 0 ; i < expansions.length ; i++ ) {
				for ( int j = 0 ; j < expansions[i].size() ; j++ ) {
					if ( expansions[i].isAvailable(j) && ( expansions[i].getTotalCost(j) == 2 || expansions[i].getTotalCost(j) == 3 ) )
						possible++;
				}
			}
			if ( possible < 1 )
				throw new DominionException(Locale.get("alert.NoCardsForBanePile"));
			else {
				selectedCards.increaseSize(1);
				int selectedElement, tmpExp;
				while ( selected < numberOfRandomCards + 1 ) {
					selectedElement = Rand.randomInt(TOTAL_CARDS);
					tmpExp = getLinearExpansionIndex(selectedElement);
					selectedElement = getLinearCardIndex(selectedElement);
					// TODO decide whether this is correct way of doing it
					if ( selectedCards.fromExpansion(ALCHEMY) > 1 && ( expansions[tmpExp].getTotalCost(selectedElement) == 1 || expansions[tmpExp].getTotalCost(selectedElement) == 2 ) ) {
						if ( selectBaneCard(playingSet, tmpExp, selectedElement, selected) )
							selected++;
					} else if ( expansions[tmpExp].getPotionCost(selectedElement) == 0 && ( expansions[tmpExp].getCost(selectedElement) == 2 || expansions[tmpExp].getCost(selectedElement) == 3 ) ) {
						if ( selectBaneCard(playingSet, tmpExp, selectedElement, selected) )
							selected++;
					}
				}
			}
		}
	}
	
	private void readResource(int exp, String fileName, int totalCards) {
		if ( exp == USER ) {
			if ( totalCards > -1 )
				expansions[exp] = new Cards(totalCards, Cards.IS_SET);
		} else {
			expansions[exp] = new Cards(totalCards, Cards.IS_SET);
		}
		if ( totalCards == 0 )
			return;
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
				if ( exp == USER && totalCards == -1 && (char) ch == ';' ) {
					if (isr != null)
						isr.close();
					//#debug dominizer
					System.out.println("processing " + sb.toString() + ":" + sb.toString().substring(0, sb.toString().length() - 1));
					readResource(exp, fileName, Integer.parseInt(sb.toString().substring(0, sb.toString().length() - 1)));
					return;
				} else if ( exp == USER && cardRead == 0 && start == 0 && (char) ch == ';' ) {
					start = 1; // this ensures that it is not encountered twice!
					TOTAL_CARDS += totalCards;
				} else if ((char) ch == ';' ) {
					// # debug dominizer
					//System.out.println("processing " + sb.toString());
					expansions[exp].setName(cardRead, sb.toString().substring(
							start, sb.toString().indexOf(":", start)).trim());
					expansions[exp].setExpansion(exp);
					start = sb.toString().indexOf(":", start) + 1;
					tmp = sb.toString().substring(start-1, sb.toString().indexOf(":", start));
					try {
						expansions[exp].setCost(cardRead, 
								parseInt(tmp),
								parseInformation(tmp, Cards.COST_POTIONS));
					} catch (Exception e) {
						expansions[exp].setCost(cardRead, 
								0,
								parseInformation(tmp, Cards.COST_POTIONS));
					}
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
					/*
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
					 */
					cardRead++;
				} else if ((char) ch == '\n') {
					sb.delete(0, sb.toString().length() - 1);
				}
			}
			if (isr != null)
				isr.close();
		} catch (Exception ex) {
			//#debug dominizer
			System.out.println("exception on reading" + ex.toString());
		}
	}

	private void readSettings() {
		SettingsRecordStorage.instance().changeToRecordStore("presets");
		int start = 0, i = 0;
		int numberSaves = 0;
		if (SettingsRecordStorage.instance().data() == null) {
			//#debug dominizer
			System.out.println("Read user presets: presets is null");
			presets[PROMO] = null;
		} else {
			//#debug dominizer
			System.out.println("Read user presets: presets is size=" + SettingsRecordStorage.instance().data().size());
			presets[PROMO] = new CardPresets(SettingsRecordStorage.instance().data().size());
			int[][] preset;
			int k;
			for ( i = 0; i < SettingsRecordStorage.instance().data().size(); i++) {
				//#debug dominizer
				System.out.println("presets: " + SettingsRecordStorage.instance().data().elementAt(i).toString());
				start = 0;
				preset = new int[10][2];
				for ( k = 0 ; k < 10 ; k++ ) {
					start = SettingsRecordStorage.instance().data().elementAt(i).toString().indexOf(
									SettingsRecordStorage.MEDIUM_SPLITTER,
									start + 1);
					if (k == 9)
						preset[k] = getCardInfo(SettingsRecordStorage.instance().data().elementAt(i).toString().substring(start));
					else
						preset[k] = getCardInfo(SettingsRecordStorage.instance().data().elementAt(i).toString().substring(start,
								SettingsRecordStorage.instance().data().elementAt(i).toString().indexOf(SettingsRecordStorage.MEDIUM_SPLITTER, start + 1)));
				}
				presets[PROMO].setPreset(numberSaves,
						SettingsRecordStorage.instance().data().elementAt(i).toString().substring(0,
								SettingsRecordStorage.instance().data().elementAt(i).toString().indexOf(SettingsRecordStorage.BIG_SPLITTER)),
								preset);
				numberSaves++;
			}
		}
		//#debug dominizer
		System.out.println("finished reading presets successfully");
		
		//#debug dominizer
		System.out.println("start reading expansion states");
		SettingsRecordStorage.instance().changeToRecordStore("settings");
		String tmp = SettingsRecordStorage.instance().readKey("expansions");
		//#debug dominizer
		System.out.println("expansions: " + tmp);
		if (tmp != null) {
			for ( i = 0 ; i < tmp.length() ; i++ ) 
				playingExpansions[i] = tmp.substring(i, i + 1).equals("1");
		}
		//#debug dominizer
		System.out.println("finished reading expansion states successfully");
		
		//#debug dominizer
		System.out.println("start reading number of cards");
		tmp = SettingsRecordStorage.instance().readKey("expcards");
		//#debug dominizer
		System.out.println("usedcards: " + tmp);
		if (tmp != null) {
			for ( i = 0 ; i < tmp.length() ; i++ ) 
				numberOfCardsFromExp[i] = Integer.parseInt(tmp.substring(i, i + 1));
		}
		//#debug dominizer
		System.out.println("finished reading number of cards succesfully\nstart reading available cards");

		//#debug dominizer
		System.out.println("start reading available cards");
		tmp = SettingsRecordStorage.instance().readKey("available");
		//#debug dominizer
		System.out.println("available from " + tmp);
		if (tmp != null) {
			for ( i = 0 ; i < tmp.length() ; i++ )
				expansions[getLinearExpansionIndex(i)].setAvailable(
						getLinearCardIndex(i), tmp.substring(i, i + 1).equals("1"));
		}
		//#debug dominizer
		System.out.println("finished reading available cards");

		//#debug dominizer
		System.out.println("read cards percentages");
		tmp = SettingsRecordStorage.instance().readKey("percentage");
		//#debug dominizer
		System.out.println("percentages from " + tmp);
		if (tmp != null) {
			for ( i = 0; i < tmp.length() ; i++)
				if ( tmp.substring(i, i + 1).equals("*") )
					expansions[getLinearExpansionIndex(i)].setPercentage(getLinearCardIndex(i), 10);
				else
					expansions[getLinearExpansionIndex(i)].setPercentage(getLinearCardIndex(i), 
							Integer.parseInt(tmp.substring(i, i + 1)));
		}
		//#debug dominizer
		System.out.println("finished reading cards percentages succesfully");
		
		//#debug dominizer
		System.out.println("reading randomized saves");
		tmp = SettingsRecordStorage.instance().readKey("randsave");
		if ( tmp != null )
			SETS_SAVE = Integer.parseInt(tmp);
		//#debug dominizer
		System.out.println("finished reading randomized saves");
		
		//#debug dominizer
		System.out.println("start reading old randomized cards");
		int[] card; 
		numberSaves = 1;
		while ( SettingsRecordStorage.instance().readKey("" + numberSaves ) != null && numberSaves <= SETS_SAVE ) {
			//#debug dominizer
			System.out.println("reading from: " + numberSaves + ":" + SettingsRecordStorage.instance().readKey("" + numberSaves));
			start = 0;
			String save = SettingsRecordStorage.instance().readKey("" + numberSaves ).trim();
			boolean isHold, isBane;
			while ( start >= 0 && start < save.length() - 1 ) {//for ( i = 0 ; i < numberOfCards ; i++ ) {
				String cardInfo= save.substring(start, save.indexOf(SettingsRecordStorage.MEDIUM_SPLITTER, start + 1));
				isHold = parseSaveSetting(cardInfo, 'H');
				isBane = parseSaveSetting(cardInfo, 'B');
				cardInfo = cardInfo.replace('H', ' ');
				cardInfo = cardInfo.replace('B', ' ');
				//#debug dominizer
				System.out.println("SEE HERE: " + cardInfo);
				card = getCardInfo(cardInfo);
				if ( expansions[card[0]].isPlaying(card[1]) > 0 ) {
					// Should never happen...
					//#debug dominizer
					System.out.println("READING AN ALREADY SET CARD: " + card[0] + " and " + card[1]);
				} else {
					expansions[card[0]].setPlaying(card[1], numberSaves);
					expansions[card[0]].setHoldCard(card[1], isHold);
					expansions[card[0]].setBaneCard(card[1], isBane);
				}
				start = SettingsRecordStorage.instance().readKey("" + numberSaves).indexOf(
					SettingsRecordStorage.MEDIUM_SPLITTER, start + 1);
			}
			numberSaves++;
		}
		//#debug dominizer
		System.out.println("finished reading old randomized cards");
		
		//#debug dominizer
		System.out.println("start reading preferred sort");
		tmp = SettingsRecordStorage.instance().readKey("sort");
		//#debug dominizer
		System.out.println("sort: " + tmp);
		if (tmp != null) {
			Cards.COMPARE_PREFERRED = Integer.parseInt(tmp);
		}
		//#debug dominizer
		System.out.println("finished reading preferred sort succesfully");
		
		//#debug dominizer
		System.out.println("start reading local conditions");
		StringBuffer sb = new StringBuffer();
		tmp = "      ";
		InputStreamReader isr = null;
		i = 0;
		start = 0;
		if ( SettingsRecordStorage.instance().changeToRecordStore("condition") )
			while ( SettingsRecordStorage.instance().readKey("name" + start) != null )
				start++;
		try {
			isr = new InputStreamReader(getClass().getResourceAsStream("/condition"), "UTF8");
			int ch;
			while ((ch = isr.read()) > -1) {
				if ( (char) ch == ';' )
					i++;
			}
			isr.close();
			condition = new Condition(i + start);
			//#debug dominizer
			System.out.println("readed conditions: "+condition.size());
			condition.initialConditions = i;
			start = 0;
			isr = new InputStreamReader(getClass().getResourceAsStream("/condition"), "UTF8");
			while ((ch = isr.read()) > -1) {
				sb.append((char) ch);
				if ( (char) ch == ';' ) {
					//#debug dominizer
					System.out.println("condition processing " + sb.toString().trim().substring(0, sb.toString().trim().length() - 1));
					condition.setName(start, sb.toString().trim().substring(0, sb.toString().trim().indexOf(':')));
					condition.setAvailable(start, true);
					condition.setPercentage(start, 0);
					condition.setCondition(start, sb.toString().substring(sb.toString().indexOf(':')+1, sb.toString().length() - 1).trim());
					sb.delete(0, sb.toString().length());
					start++;
				} else if ((char) ch == '\n') {
					sb.delete(0, sb.toString().length() - 1);
				}
			}
			if (isr != null)
				isr.close();
		} catch (Exception ex) {
			//#debug dominizer
			System.out.println("exception on reading" + ex.toString());
		}
		//#debug dominizer
		System.out.println("start reading user conditions");
		if ( SettingsRecordStorage.instance().changeToRecordStore("condition") ) {
			i = 0;
			while ( SettingsRecordStorage.instance().readKey("name" + i) != null ) {
				condition.setName(start, SettingsRecordStorage.instance().readKey("name" + i).substring(2));
				condition.setAvailable(start, SettingsRecordStorage.instance().readKey("name" + i).substring(0,1).equals("1"));
				if ( SettingsRecordStorage.instance().readKey("name" + i).substring(1,2).equals("*") )
					condition.setPercentage(start, 10);
				else
					condition.setPercentage(start, Integer.parseInt(SettingsRecordStorage.instance().readKey("name" + i).substring(1,2)));
				condition.setCondition(start, SettingsRecordStorage.instance().readKey("" + i));				
				i++;
				start++;
			}
			tmp = SettingsRecordStorage.instance().readKey("preferred");
			//#debug dominizer
			System.out.println("preferred condition: " + tmp);
			if (tmp != null) {
				condition.preferredCondition = Integer.parseInt(tmp);
			}
		}
		//#debug dominizer
		System.out.println("finished reading conditions succesfully");
	}

	private boolean parseSaveSetting(String cardSave, char in) {
		return cardSave.indexOf(in) >= 0;
	}


	public void removePlayingSet(int playingSet) {
		if ( playingSet <= 0 )
			return;
		for ( int i = 0 ; i < expansions.length ; i++ ) {
			for ( int j = 0 ; j < expansions[i].size() ; j++ ) {
				if ( expansions[i].isPlayingSet(j, playingSet) ) {
					//#debug dominizer
					System.out.println("resetting card [" + i +"][" + j + "] with " + expansions[i].isPlaying(j));
					expansions[i].setPlaying(j, 0);
				} else if ( expansions[i].isPlaying(j) > 100 && expansions[i].isPlaying(j) - 100 > playingSet ) {
					//#debug dominizer
					System.out.println("setting card [" + i +"][" + j + "] with " + (expansions[i].isPlaying(j) - 1) + " instead of " + expansions[i].isPlaying(j));
					expansions[i].setPlaying(j, expansions[i].isPlaying(j) - 1);
				} else if ( expansions[i].isPlaying(j) < 100 && expansions[i].isPlaying(j) > playingSet ) {
					//#debug dominizer
					System.out.println("setting card [" + i +"][" + j + "] with " + (expansions[i].isPlaying(j) - 1) + " instead of " + expansions[i].isPlaying(j));
					expansions[i].setPlaying(j, expansions[i].isPlaying(j) - 1);
				}
			}
		}
		if ( playingSet == CURRENT_SET )
			CURRENT_SET--;
	}

	public void resetIsPlaying(int playingSet) {
		for (int i = 0; i < expansions.length; i++)
			for (int j = 0; j < expansions[i].size(); j++)
				if ( playingSet < 0 )
					expansions[i].setPlaying(j, 0);
				else if ( expansions[i].isPlayingSet(j, playingSet) && !expansions[i].isHold(j, playingSet) )
					expansions[i].setPlaying(j, 0);
	}
	
	public boolean selectCard(int playingSet, int exp, int card, int placement) {
		// # debug dominizer
		//System.out.println("try: " + exp + " - " + card);
		if ( expansions[exp].isAvailable(card) 
				&& !selectedCards.contains(expansions[exp].getName(card))
				&& ( expansions[exp].isPlaying(card) == 0 || expansions[exp].isHold(card, playingSet) ) ) {
			if ( !expansions[exp].isHold(card, playingSet) )
				expansions[exp].setPlaying(card, playingSet);
			// # debug dominizer
			//System.out.println("accepted: " + exp + " - " + card + " with playingset " + expansions[exp].isPlaying(card) + " #:" + (selected + 1));
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
	
	public boolean selectBaneCard(int playingSet, int exp, int card, int placement) {
		// # debug dominizer
		//System.out.println("try: " + exp + " - " + card);
		if ( exp == -1 )
			return false;
		if ( selectCard(playingSet, exp, card, placement ) ) {
			expansions[exp].setBaneCard(card, true);
			selectedCards.setBaneCard(placement, true);
			return true;
		}
		return false;
	}
	
	public int getCurrentSet() {
		int tmp = 0;
		for ( int i = 0 ; i < expansions.length ; i++ ) {
			for ( int j = 0 ; j < expansions[i].size() ; j++ ) {
				if ( expansions[i].isPlaying(j) > 100 && expansions[i].isPlaying(j) - 100 > tmp )
					tmp = expansions[i].isPlaying(j) - 100;
				else if ( expansions[i].isPlaying(j) < 100 && expansions[i].isPlaying(j) > tmp )
					tmp = expansions[i].isPlaying(j);
			}
		}
		return tmp;
	}
	
	public void selectPreset(int playingSet, int presetDeck, int preset) throws DominionException {
		//#debug dominizer
		System.out.println("fetching preset: " + presetDeck + " and " + preset);
		if ( playingSet > 0 )
			resetIsPlaying(playingSet);
		else
			playingSet = getCurrentSet() + 1;
		checkAvailability();
		if ( presetDeck >= presets.length )
			throw new DominionException(Locale.get("alert.preset.WrongInput"));
		if ( preset >= presets[presetDeck].size() )
			throw new DominionException(Locale.get("alert.preset.WrongInput"));
		selectedCards = new Cards(presets[presetDeck].size(preset), Cards.IS_NOT_SET);
		selected = 0;
		
		for (int i = 0; i < presets[presetDeck].size(preset); i++) {
			//#debug dominizer
			System.out.println("selecting expansion: " + presets[presetDeck].getPresetCardExpansion(preset, i) + " and card: " + presets[presetDeck].getPresetCardPlacement(preset, i));
			//#debug dominizer
			System.out.println("card: " + expansions[presets[presetDeck].getPresetCardExpansion(preset, i)].getName(presets[presetDeck].getPresetCardPlacement(preset, i)));
			if ( selectCard(playingSet, presets[presetDeck].getPresetCardExpansion(preset, i), presets[presetDeck].getPresetCardPlacement(preset, i), -1) )
				selected++;
		}
		// This is necessary as cards could have been chosen in other set!		
		if ( selected < 10 ) {
			removePlayingSet(playingSet);
			//#debug dominizer
			System.out.println("selecting selected: " + selected);
			throw new DominionException(Locale.get("alert.preset.CardAlreadyUsed") + " Cur: " + getCurrentSet() + " Playing: " + playingSet);
		}
		if ( presets[presetDeck].getBaneCard(preset)[0] >= 0 ) {
			selectedCards.increaseSize(1);
			if ( selectBaneCard(playingSet, presets[presetDeck].getBaneCard(preset)[0], presets[presetDeck].getBaneCard(preset)[1], -1) ) {
				selected++;
			}
		}
	}

	public void selectPreset(int playingSet, String presetName) throws DominionException {
		for (int i = 0; i < presets.length; i++)
			for (int j = 0; j < presets[i].size(); j++)
				if (presets[i].getPresetName(j).equals(presetName)) {
					selectPreset(playingSet, i, j);
					return;
				}
	}

	public void setExpansionPlayingState(boolean[] isAvailable) {
		for ( int i = 0; i < playingExpansions.length; i++)
			setExpansionPlayingState(i, isAvailable[i]);
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
	
	public void useHoldCards(int playingSet, int tmpPlayingSet) {
		//#debug dominizer
		System.out.println("using hold cards");
		for ( int i = 0 ; i < expansions.length ; i++ ) {
			for ( int j = 0 ; j < expansions[i].size() ; j++ ) {
				if ( expansions[i].isHold(j, playingSet) && tmpPlayingSet <= 0 ) {
					//#debug dominizer
					System.out.println("using the regular HOLD");
					selectCard(playingSet, i, j, -1);
					selected++;
				} else if ( expansions[i].isHold(j, tmpPlayingSet) && tmpPlayingSet > 0 ) {
					//#debug dominizer
					System.out.println("using the prevent HOLD");
					expansions[i].setPlaying(j, playingSet);
					expansions[i].setHoldCard(j, true);
					selectCard(playingSet, i, j, -1);
					selected++;
				}
			}
		}
	}
	
	public void useMinimumExpansionCards(int playingSet) {
		//#debug dominizer
		System.out.println("using minimum expansion cards, already selected: " + selected);
		for ( int i = 0, j = 0 ; i < expansions.length ; i++ ) {
			//#debug dominizer
			System.out.println("trying expansion: " + i + " - State: " + playingExpansions[i] + " - Number: " + numberOfCardsFromExp[i]);
			if ( playingExpansions[i] && 0 < numberOfCardsFromExp[i] && expansions[i].size() > 0 ) {
				//#debug dominizer
				System.out.println("selecting for expansion: " + i + " already chosen: "+ selectedCards.fromExpansion(i));
				while ( selectedCards.fromExpansion(i) < numberOfCardsFromExp[i] && selected < numberOfRandomCards ) {
					//#debug dominizer
					System.out.println("selecting for expansion1: " +i+ " already chosen: "+ selectedCards.fromExpansion(i));
					j = Rand.randomInt(expansions[i].size());
					if ( selectCard(playingSet, i, j, selected) ) {
						selected++;
						//#debug dominizer
						System.out.println("selected: " + j + " - " + selected);
					}
				}
			}
		}
	}
	
	public void usePercentageCards(int playingSet) {
		//#debug dominizer
		System.out.println("using percentage cards");
		Rand.resetSeed();
		for ( int i = 0 ; i < expansions.length ; i++ ) {
			int tmpSel = 0;
			for ( int j = 0 ; j < expansions[i].size() ; j++ ) { 
				switch ( expansions[i].getPercentage(j) ) {
				case 10:
					if ( selectCard(playingSet, i, j, -1) )
						tmpSel++;
				case 0:
					break;
				default:
					if ( Rand.randomInt(10) < expansions[i].getPercentage(j) )
						if ( selectCard(playingSet, i, j, -1) )
							tmpSel++;
				}
			}
			//#debug dominizer
			System.out.println("used " + tmpSel + " from expansion: " + i);
			selected += tmpSel;
		}
	}
		
	public static Image getExpansionImage(int expansion) {
		try {
			if ( Dominion.getExpansionImageName(expansion) == null )
				return null;
			return Image.createImage("/" + Dominion.getExpansionImageName(expansion) + "0.png");
		} catch (IOException exp) {
			return null;
		}
	}
	
	public static String getExpansionImageName(int expansion) {
		switch (expansion) {
		case BASE:
			return "ba";
		case INTRIGUE:
			return "in";
		case SEASIDE:
			return "se";
		case ALCHEMY:
			return "al";
		case CORNUCOPIA:
			return "co";
		case PROSPERITY:
			return "pr";
		case PROMO:
			return "p";
		case USER:
			return "us";
		default:
			return null;
		}
	}
	
	public static String getExpansionName(int expansion) {
		switch (expansion) {
		case BASE:
			return Locale.get("expansion.base");
		case PROMO:
			return Locale.get("expansion.promo");
		case INTRIGUE:
			return Locale.get("expansion.intrigue");
		case SEASIDE:
			return Locale.get("expansion.seaside");
		case ALCHEMY:
			return Locale.get("expansion.alchemy");
		case CORNUCOPIA:
			return Locale.get("expansion.cornucopia");
		case PROSPERITY:
			return Locale.get("expansion.prosperity");
		case USER:
			return Locale.get("expansion.user");
		default:
			return "";
		}
	}

	public static Dominion I() {
		if (dom == null)
			dom = new Dominion();
		return dom;
	}	
}
