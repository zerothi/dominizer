package com;

import java.io.IOException;

import javax.microedition.lcdui.Image;


public class Cards {

	private String[] name = null;
	private int[] expansion = null;
	/*
	 * #0 = Is Playing
	 * #1 = Is Selected for availability
	 * #2 = Is Black Market available
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
	 */
	private boolean[][] isSpecific = null;
	private int[] percentage = null;

	public Cards(int size, int isSet) {
		if ( size > 0 ) {
			name = new String[size];
			isGamingRelated = new boolean[size][3];
			cost = new int[size];
			isSpecific = new boolean[size][6];
			if ( isSet == IS_SET )
				expansion = new int[1];
			else
				expansion = new int[size];
			expansion[0] = -1;
			percentage = new int[size];
			for ( int i = 0 ; i < size ; i++ ) {
				name[i] = null;
				cost[i] = 0;
				isGamingRelated[i][0] = false;
				isGamingRelated[i][1] = true;
				isGamingRelated[i][2] = true;
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
	public boolean isPlaying(int index) {
		return isGamingRelated[index][0];
	}

	/**
	 * @param isPlaying
	 *            the isPlaying to set
	 */
	public void setPlaying(int index, boolean isPlaying) {
		isGamingRelated[index][0] = isPlaying;
	}


	/**
	 * @return the selected
	 */
	public boolean isAvailable(int index) {
		return isGamingRelated[index][1];
	}

	/**
	 * @param available
	 *            the selected to set
	 */
	public void setAvailable(int index, boolean available) {
		isGamingRelated[index][1] = available;
	}

	/**
	 * @return the bmSelected
	 */
	public boolean isBlackMarketAvailable(int index) {
		return isGamingRelated[index][2];
	}

	/**
	 * @param bmSelected
	 *            the bmSelected to set
	 */
	public void setBlackMarketAvailable(int index, boolean bmAvailable) {
		isGamingRelated[index][2] = bmAvailable;
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

	
	/**
	 * @return the isAction
	 */
	public boolean isAction(int index) {
		return isSpecific[index][0];
	}

	/**
	 * @param isAction
	 *            the isAction to set
	 */
	public void setAction(int index, boolean isAction) {
		isSpecific[index][0] = isAction;
	}
	/**
	 * @return the isVictory
	 */
	public boolean isVictory(int index) {
		return isSpecific[index][1];
	}

	/**
	 * @param isVictory
	 *            the isVictory to set
	 */
	public void setVictory(int index, boolean isVictory) {
		isSpecific[index][1] = isVictory;
	}

	/**
	 * @return the isCoin
	 */
	public boolean isTreasure(int index) {
		return isSpecific[index][2];
	}

	/**
	 * @param isCoin
	 *            the isCoin to set
	 */
	public void setTreasure(int index, boolean isCoin) {
		isSpecific[index][2] = isCoin;
	}

	/**
	 * @param isAttack
	 *            the isAttack to set
	 */
	public void setAttack(int index, boolean isAttack) {
		isSpecific[index][3] = isAttack;
	}

	/**
	 * @return the isAttack
	 */
	public boolean isAttack(int index) {
		return isSpecific[index][3];
	}

	/**
	 * @return the isReaction
	 */
	public boolean isReaction(int index) {
		return isSpecific[index][4];
	}

	/**
	 * @param isReaction
	 *            the isReaction to set
	 */
	public void setReaction(int index, boolean isReaction) {
		isSpecific[index][4] = isReaction;
	}
	/**
	 * @return the isDuration
	 */
	public boolean isDuration(int index) {
		return isSpecific[index][5];
	}

	/**
	 * @param isDuration
	 *            the isDuration to set
	 */
	public void setDuration(int index, boolean isDuration) {
		isSpecific[index][5] = isDuration;
	}
	
	public int getCardType(int index) {
		if ( isAction(index) && !isAttack(index) && !isReaction(index) && !isVictory(index) && !isTreasure(index) && !isDuration(index) )
			return TYPE_ACTION;
		else if ( isAction(index) && isAttack(index) && !isReaction(index) && !isVictory(index) && !isTreasure(index) && !isDuration(index) )
			return TYPE_ACTION_ATTACK;
		else if ( isAction(index) && !isAttack(index) && isReaction(index) && !isVictory(index) && !isTreasure(index) && !isDuration(index) )
			return TYPE_ACTION_REACTION;
		else if ( !isAction(index) && !isAttack(index) && !isReaction(index) && isVictory(index) && !isTreasure(index) && !isDuration(index) )
			return TYPE_VICTORY;
		else if ( isAction(index) && !isAttack(index) && !isReaction(index) && !isVictory(index) && isTreasure(index) && !isDuration(index) )
			return TYPE_ACTION_TREASURY;
		else if ( isAction(index) && !isAttack(index) && !isReaction(index) && isVictory(index) && !isTreasure(index) && !isDuration(index) )
			return TYPE_ACTION_VICTORY;
		else if ( !isAction(index) && !isAttack(index) && !isReaction(index) && isVictory(index) && isTreasure(index) && !isDuration(index) )
			return TYPE_TREASURY_VICTORY;
		else if ( isAction(index) && !isAttack(index) && !isReaction(index) && !isVictory(index) && !isTreasure(index) && isDuration(index) )
			return TYPE_ACTION_DURATION;
		return TYPE_ACTION;
	}
	
	public Object[] getCard(int index) {
		Object[] tmp = new Object[12];
		tmp[0] = getName(index);
		tmp[1] = new Integer(getExpansion(index));
		tmp[2] = new Integer(getCost(index));
		tmp[3] = new Boolean(isAction(index));
		tmp[4] = new Boolean(isVictory(index));
		tmp[5] = new Boolean(isTreasure(index));
		tmp[6] = new Boolean(isAttack(index));
		tmp[7] = new Boolean(isReaction(index));
		tmp[8] = new Boolean(isDuration(index));
		tmp[9] = new Boolean(isPlaying(index));
		tmp[10] = new Boolean(isAvailable(index));
		tmp[11] = new Boolean(isBlackMarketAvailable(index));
		return tmp;
	}
	
	public int fromExpansion(int exp) {
		int tmp = 0;
		for ( int i = 0 ; i < size() ; i++ ) {
			if ( getExpansion(i) > -1 && getExpansion(i) == exp )
				tmp++;
		}
		return tmp;
	}
	
	public void setCard(int index, Object[] cardInfo) {
		for ( int i = 0 ; i < 12 ; i++ ) {
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
		setAction(index, ((Boolean)cardInfo[3]).booleanValue());
		setVictory(index, ((Boolean)cardInfo[4]).booleanValue());
		setTreasure(index, ((Boolean)cardInfo[5]).booleanValue());
		setAttack(index, ((Boolean)cardInfo[6]).booleanValue());
		setReaction(index, ((Boolean)cardInfo[7]).booleanValue());
		setDuration(index, ((Boolean)cardInfo[8]).booleanValue());
		setPlaying(index, ((Boolean)cardInfo[9]).booleanValue());
		setAvailable(index, ((Boolean)cardInfo[10]).booleanValue());
		setBlackMarketAvailable(index, ((Boolean)cardInfo[11]).booleanValue());
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
		for ( int i = 0 ; i < size() ; i++ )
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
	
	public static Image convert2ImageType(Image img, int cardType) {
		int[] greenAddition = new int[2];
		int[] blueAddition = new int[2];
		int[] redAddition = new int[2];
		greenAddition[0] = -2;
		blueAddition[0] = -2;
		redAddition[0] = -2;
		switch ( cardType ) {
		case TYPE_ACTION:
			greenAddition[1] = -2;
			blueAddition[1] = -2;
			redAddition[1] = -2;
			break;
		case TYPE_ACTION_ATTACK:
			greenAddition[1] = 0;
			blueAddition[1] = 0;
			redAddition[1] = 2;
			break;
		case TYPE_ACTION_REACTION:
			greenAddition[1] = 0;
			blueAddition[1] = 2;
			redAddition[1] = 0;
			break;
		case TYPE_ACTION_VICTORY:
			greenAddition[1] = 2;
			blueAddition[1] = 0;
			redAddition[1] = 0;
			break;
		case TYPE_ACTION_TREASURY:
			greenAddition[1] = 2;
			blueAddition[1] = 1;
			redAddition[1] = 3;
			break;
		case TYPE_TREASURY_VICTORY:
			greenAddition[0] = 2;
			blueAddition[0] = 1;
			redAddition[0] = 3;
			greenAddition[1] = 2;
			blueAddition[1] = 0;
			redAddition[1] = 3;
			break;
		case TYPE_ACTION_DURATION:
			greenAddition[1] = 2;
			blueAddition[1] = 0;
			redAddition[1] = 2;
			break;
		case TYPE_VICTORY:
			greenAddition[0] = 2;
			blueAddition[0] = 0;
			redAddition[0] = 0;
			greenAddition[1] = 2;
			blueAddition[1] = 0;
			redAddition[1] = 0;
			break;
		}
		return null;
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
	public static final int TYPE_ACTION = 0;
	public static final int TYPE_ACTION_ATTACK = 1;
	public static final int TYPE_ACTION_REACTION = 2;
	public static final int TYPE_VICTORY = 3;
	public static final int TYPE_ACTION_TREASURY = 4;
	public static final int TYPE_ACTION_VICTORY = 5;
	public static final int TYPE_TREASURY_VICTORY = 6;
	public static final int TYPE_ACTION_DURATION = 7;
	public static final int TYPE_POTION = 8;

}
