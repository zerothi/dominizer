package com;

import java.io.IOException;

import javax.microedition.lcdui.Image;


public class Cards {

	private String[] name = null;
	private int[] expansion = null;
	
	private int[] playing = null;
	/*
	 * #0 = Is Selected for availability
	 * #1 = Is Black Market available
	 */
	private boolean[][] isGamingRelated = null; 
	private int[] cost = null;
	/*
	 * #0 = Action
	 * #1 = Victory
	 * #2 = Treasury
	 * #3 = Attack
	 * #4 = Reaction
	 * #5 = Duration
	 * #6 = Potion
	 */
	private boolean[][] isSpecific = null;
	/*
	 * #0 = Adds # Cards
	 * #1 = Adds # Actions
	 * #2 = Adds # Buys
	 * #3 = Adds # Coins
	 * #4 = Adds # Trash
	 * #5 = Adds # Curse
	 * #6 = Adds # Potions
	 */
	private int[][] addsInfo = null;
	private int[] percentage = null;
	private static int i, k;

	public Cards(int size, int isSet) {
		if ( size > 0 ) {
			name = new String[size];
			playing = new int[size];
			isGamingRelated = new boolean[size][2];
			cost = new int[size];
			isSpecific = new boolean[size][6];
			addsInfo = new int[size][7];
			if ( isSet == IS_SET )
				expansion = new int[1];
			else
				expansion = new int[size];
			expansion[0] = -1;
			percentage = new int[size];
			for ( i = 0 ; i < size ; i++ ) {
				name[i] = null;
				cost[i] = 0;
				playing[i] = 0;
				isGamingRelated[i][0] = true;
				isGamingRelated[i][1] = true;
				for ( k = 0 ; k < addsInfo[i].length ; k++ )
					addsInfo[i][k] = 0;
				if ( isSet == IS_NOT_SET )
					expansion[i] = -1;
				percentage[i] = 0;
			}
			
		}
	}

	/**
	 * @return the name
	 */
	public String getName(int index) {
		return name[index];
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(int index, String name) {
		this.name[index] = name;
	}

	/**
	 * @return the game
	 */
	public int getExpansion() {
		if ( expansion != null)
			return expansion[0];
		return -1;
	}
	
	/**
	 * @return the game
	 */
	public int getExpansion(int index) {
		if ( expansion.length == 1 )
			return expansion[0];
		return expansion[index];
	}

	/**
	 * @param game
	 *            the game to set
	 */
	public void setExpansion(int index, int expansion) {
		this.expansion[index] = expansion;
	}
	
	/**
	 * @return the percentage
	 */
	public int getPercentage(int index) {
		return percentage[index];
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(int index, int percentage) {
		this.percentage[index] = percentage;
	}

	/**
	 * @param game
	 *            the game to set
	 */
	public void setExpansion(int expansion) {
		this.expansion[0] = expansion;
	}
	
	/**
	 * @return the isPlaying
	 */
	public int isPlaying(int index) {
		return playing[index];
	}
	
	/**
	 * @return the isPlaying
	 */
	public boolean isPlayingSet(int index, int playingSet) {
		if ( playing[index] > 100 )
			return playing[index] - 100 == playingSet;
		return playing[index] == playingSet;
	}

	/**
	 * @param isPlaying
	 *            the isPlaying to set
	 */
	public void setPlaying(int index, int playing) {
		this.playing[index] = playing;
	}
	
	/**
	 * @return the isPlaying
	 */
	public int getPlaying(int index) {
		if ( playing[index] > 100 )
			return playing[index] - 100;
		return playing[index];
	}
	
	public boolean isHold(int index, int playingSet) {
		return playing[index] == playingSet + 100;
	}
	
	public boolean isHold(int index) {
		return playing[index] > 100;
	}
	
	/**
	 * @param name
	 * @param hold 
	 */
	public void setHoldCard(String name, boolean hold) {
		for ( i = 0 ; i < playing.length ; i++ )
			if ( this.name[i].equals(name) ) {
				setHoldCard(i, hold);
				return;
			}
	}
	
	public void setHoldCard(int index, boolean hold) {
		if ( hold ) {
			if ( 0 < playing[index] & playing[index] < 100 )
				playing[index] = playing[index] + 100;
		} else {
			if ( playing[index] > 100 )
				playing[index] = playing[index] - 100;
		}
	}
	

	/**
	 * @return the selected
	 */
	public boolean isAvailable(int index) {
		return isGamingRelated[index][0];
	}

	/**
	 * @param available
	 *            the selected to set
	 */
	public void setAvailable(int index, boolean available) {
		isGamingRelated[index][0] = available;
	}

	/**
	 * @return the bmSelected
	 */
	public boolean isBlackMarketAvailable(int index) {
		return isGamingRelated[index][1];
	}

	/**
	 * @param bmSelected
	 *            the bmSelected to set
	 */
	public void setBlackMarketAvailable(int index, boolean bmAvailable) {
		isGamingRelated[index][1] = bmAvailable;
	}

	/**
	 * @return the cost
	 */
	public int getCost(int index) {
		return cost[index];
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(int index, int cost) {
		this.cost[index] = cost;
	}

	public void setType(int index, int whichType, boolean state) {
		isSpecific[index][whichType] = state;
	}
		
	
	public boolean isType(int index, int whichType) {
		return isSpecific[index][whichType];
	}
		
	public int getCardType(int index) {
		if ( isType(index, TYPE_ACTION) ) {
			if ( isType(index, TYPE_ATTACK) )
				return TYPE_ACTION_ATTACK;
			if ( isType(index, TYPE_REACTION) )
				return TYPE_ACTION_REACTION;
			if ( isType(index, TYPE_DURATION) )
				return TYPE_ACTION_DURATION;
			if ( isType(index, TYPE_TREASURY) )
				return TYPE_ACTION_TREASURY;
			if ( isType(index, TYPE_VICTORY) )
				return TYPE_ACTION_VICTORY;
			return TYPE_ACTION;
		} else if ( isType(index, TYPE_VICTORY) ) {
			if ( isType(index, TYPE_TREASURY) )
				return TYPE_VICTORY_TREASURY;
			return TYPE_VICTORY;
		}		
		return TYPE_ACTION;
	}
	
	public void setAddInfo(int index, int whichAddInfo, int info) {
		addsInfo[index][whichAddInfo] = info;
	}
	
	public int getAddInfo(int index, int whichAddInfo) {
		return addsInfo[index][whichAddInfo];
	}
	
	public Object[] getCard(int index) {
		Object[] tmp = new Object[19];
		tmp[0] = getName(index);
		tmp[1] = new Integer(getExpansion(index));
		tmp[2] = new Integer(getCost(index));
		tmp[3] = new Boolean(isType(index, TYPE_ACTION));
		tmp[4] = new Boolean(isType(index, TYPE_VICTORY));
		tmp[5] = new Boolean(isType(index, TYPE_ATTACK));
		tmp[6] = new Boolean(isType(index, TYPE_TREASURY));
		tmp[7] = new Boolean(isType(index, TYPE_REACTION));
		tmp[8] = new Boolean(isType(index, TYPE_DURATION));
		tmp[9] = new Integer(getPlaying(index));
		tmp[10] = new Boolean(isAvailable(index));
		tmp[11] = new Boolean(isBlackMarketAvailable(index));
		tmp[12] = new Integer(getAddInfo(index, ADDS_CARDS));
		tmp[13] = new Integer(getAddInfo(index, ADDS_ACTIONS));
		tmp[14] = new Integer(getAddInfo(index, ADDS_BUYS));
		tmp[15] = new Integer(getAddInfo(index, ADDS_COINS));
		tmp[16] = new Integer(getAddInfo(index, ADDS_TRASH));
		tmp[17] = new Integer(getAddInfo(index, ADDS_CURSE));
		tmp[18] = new Integer(getAddInfo(index, ADDS_POTIONS));
		return tmp;
	}
	
	public int fromExpansion(int exp) {
		int tmp = 0;
		for ( i = 0 ; i < size() ; i++ ) {
			if ( getExpansion(i) > -1 && getExpansion(i) == exp )
				tmp++;
		}
		return tmp;
	}
	
	public void setCard(int index, Object[] cardInfo) {
		if ( cardInfo.length == 0 ) 
			return;
		for ( i = 0 ; i < cardInfo.length ; i++ ) {
			if ( cardInfo[i] == null ) {
				//#debug dominizer 
				System.out.println("cardinfo " + i + " is null ");
			}
		}
		if ( cardInfo[0] == null ) {
			//#debug dominizer 
			System.out.println("cardinfo 0 is null ");
		}
		setName(index, cardInfo[0].toString());
		setExpansion(index, ((Integer)cardInfo[1]).intValue());
		setCost(index, ((Integer)cardInfo[2]).intValue());	
		setType(index, TYPE_ACTION, ((Boolean)cardInfo[3]).booleanValue());
		setType(index, TYPE_VICTORY, ((Boolean)cardInfo[4]).booleanValue());
		setType(index, TYPE_ATTACK, ((Boolean)cardInfo[5]).booleanValue());
		setType(index, TYPE_TREASURY, ((Boolean)cardInfo[6]).booleanValue());
		setType(index, TYPE_REACTION, ((Boolean)cardInfo[7]).booleanValue());
		setType(index, TYPE_DURATION, ((Boolean)cardInfo[8]).booleanValue());
		setPlaying(index, ((Integer)cardInfo[9]).intValue());
		setAvailable(index, ((Boolean)cardInfo[10]).booleanValue());
		setBlackMarketAvailable(index, ((Boolean)cardInfo[11]).booleanValue());
		setAddInfo(index, ADDS_CARDS, ((Integer)cardInfo[12]).intValue());
		setAddInfo(index, ADDS_ACTIONS, ((Integer)cardInfo[13]).intValue());
		setAddInfo(index, ADDS_BUYS, ((Integer)cardInfo[14]).intValue());
		setAddInfo(index, ADDS_COINS, ((Integer)cardInfo[15]).intValue());
		setAddInfo(index, ADDS_TRASH, ((Integer)cardInfo[16]).intValue());
		setAddInfo(index, ADDS_CURSE, ((Integer)cardInfo[17]).intValue());
		setAddInfo(index, ADDS_POTIONS, ((Integer)cardInfo[18]).intValue());
	}
	
	public static int compare(Object[] first, Object[] compareTo, int method) {
		switch ( method ) {
		case COMPARE_EXPANSION_NAME:
			if ( compare(first, compareTo, COMPARE_EXPANSION) == 0 )
				return compare(first, compareTo, COMPARE_NAME);
			else return compare(first, compareTo, COMPARE_EXPANSION);
		case COMPARE_EXPANSION_COST:
			if ( compare(first, compareTo, COMPARE_EXPANSION) != 0 )
				return compare(first, compareTo, COMPARE_EXPANSION);
			if ( compare(first, compareTo, COMPARE_COST) != 0 )
				return compare(first, compareTo, COMPARE_COST);
			return compare(first, compareTo, COMPARE_NAME);
		case COMPARE_COST_EXPANSION:
			if ( compare(first, compareTo, COMPARE_COST) != 0 )
				return compare(first, compareTo, COMPARE_COST);
			if ( compare(first, compareTo, COMPARE_EXPANSION) != 0 )
				return compare(first, compareTo, COMPARE_EXPANSION);
			return compare(first, compareTo, COMPARE_NAME);
		case COMPARE_COST_NAME:
			if ( compare(first, compareTo, COMPARE_COST) != 0 )
				return compare(first, compareTo, COMPARE_COST);
			return compare(first, compareTo, COMPARE_NAME);
		case COMPARE_EXPANSION:
			if ( ((Integer) first[1]).intValue() > ((Integer) compareTo[1]).intValue() )
				return 1;
			else if ( ((Integer) first[1]).intValue() < ((Integer) compareTo[1]).intValue() )
				return -1;
			else return 0;
		case COMPARE_COST:
			if ( ((Integer) first[2]).intValue() > ((Integer) compareTo[2]).intValue() )
				return 1;
			else if ( ((Integer) first[2]).intValue() < ((Integer) compareTo[2]).intValue() )
				return -1;
			else return 0;
		case COMPARE_NAME:
			return first[0].toString().compareTo(compareTo[0].toString());
		default: return compare(first, compareTo, COMPARE_EXPANSION_NAME);
		}
	}
	
	public boolean contains(String cardName) {
		for ( i = 0 ; i < size() ; i++ )
			if ( getName(i) != null )
				if ( getName(i).equals(cardName) )
					return true;
		return false;
	}
	
	public String toString(int index) {
		return getName(index);
	}
	
	public int size() {
		if ( name == null )
			return 0;
		for ( int i = 0 ; i < name.length ; i++ )
			if ( name[i] == null )
				return i + 1;
		return name.length;
	}
	
	public Image getCostImage(int card) {
		try {
			return Image.createImage("/trea" + getCost(card) + ".png");
		} catch (IOException exp) {
			return null;
		}
	}
	
	public Image getCardTypeImage(int card) {
		try {
			return Image.createImage("/" + Dominion.getExpansionImageName(getExpansion(card)) + 
					getCardType(card) + ".png");
		} catch (IOException expc) {
			return Dominion.getExpansionImage(getExpansion(card));
		}
	}
	
	public int getTypes(int type) {
		int sum = 0;
		for ( int i = 0 ; i < size() ; i++ )
			if ( isType(i, type) )
				sum++;
		return sum;
	}
	
	public int getAdds(int addInfo) {
		int sum = 0;
		for ( int i = 0 ; i < size() ; i++ )
			sum += getAddInfo(i, addInfo);
		return sum;
	}
	
	public static final int COMPARE_EXPANSION_NAME = 0;
	public static final int COMPARE_EXPANSION_COST = 1;
	public static final int COMPARE_NAME = 2;
	public static final int COMPARE_COST_NAME = 3;
	public static final int COMPARE_COST_EXPANSION = 4;
	
	public static final int COMPARE_COST = 10;
	public static final int COMPARE_EXPANSION = 11;
	
	public static int COMPARE_PREFERRED = COMPARE_EXPANSION_NAME;

	public static final int IS_SET = 1;
	public static final int IS_NOT_SET = 0;
	
	// TODO : When changing the below to non-consecutive numbers please update method "parseCondition" in Dominion
	public static final int TYPE_ACTION = 0;
	public static final int TYPE_VICTORY = 1;
	public static final int TYPE_TREASURY = 2;
	public static final int TYPE_ATTACK = 3;
	public static final int TYPE_REACTION = 4;
	public static final int TYPE_DURATION = 5;
	public static final int TYPE_POTION = 6;
	
	public static final int TYPE_ACTION_VICTORY = 10;
	public static final int TYPE_ACTION_TREASURY = 11;
	public static final int TYPE_ACTION_REACTION = 12;
	public static final int TYPE_ACTION_ATTACK = 13;
	public static final int TYPE_ACTION_DURATION = 14;
	public static final int TYPE_VICTORY_TREASURY = 15;
	
	// TODO : When changing the below to non-consecutive numbers please update method "parseCondition" in Dominion
	public static final int ADDS_CARDS = 0;
	public static final int ADDS_ACTIONS = 1;
	public static final int ADDS_BUYS = 2;
	public static final int ADDS_COINS = 3;
	public static final int ADDS_TRASH = 4;
	public static final int ADDS_CURSE = 5;
	public static final int ADDS_POTIONS = 6;
}
