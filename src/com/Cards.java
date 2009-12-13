package com;

import de.enough.polish.util.Locale;


public class Cards {
	public static final int IS_SET = 1;
	public static final int IS_NOT_SET = 0;
	public static final int NAME = 0;
	public static final int COST = 1;
	public static final int TYPE_ACTION = 0;
	public static final int TYPE_ACTION_ATTACK = 1;
	public static final int TYPE_ACTION_REACTION = 2;
	public static final int TYPE_VICTORY = 3;
	public static final int TYPE_ACTION_TREASURY = 4;
	public static final int TYPE_ACTION_VICTORY = 5;
	public static final int TYPE_TREASURY_VICTORY = 6;
	public static final int TYPE_ACTION_DURATION = 7;
	
	
	private String[] name = null;
	private String[] expansion = null;
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

	public Cards(int size, int isSet) {
		name = new String[size];
		isGamingRelated = new boolean[size][3];
		cost = new int[size];
		isSpecific = new boolean[size][6];
		for ( int i = 0 ; i < size ; i++ ) {
			isGamingRelated[i][1] = true;
			isGamingRelated[i][2] = true;
		}
		if ( isSet == IS_SET )
			expansion = new String[1];
		else
			expansion = new String[size];
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
	public String getExpansion() {
		return expansion[0];
	}
	
	/**
	 * @return the game
	 */
	public String getExpansion(int index) {
		if ( expansion.length == 1 )
			return expansion[0];
		return expansion[index];
	}

	/**
	 * @param game
	 *            the game to set
	 */
	public void setExpansion(int index, String expansion) {
		this.expansion[index] = expansion;
	}
	
	/**
	 * @param game
	 *            the game to set
	 */
	public void setExpansion(String expansion) {
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
		this.isGamingRelated[index][0] = isPlaying;
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
		this.isGamingRelated[index][1] = available;
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
		this.isGamingRelated[index][2] = bmAvailable;
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
		this.isSpecific[index][0] = isAction;
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
		this.isSpecific[index][1] = isVictory;
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
		this.isSpecific[index][2] = isCoin;
	}

	/**
	 * @param isAttack
	 *            the isAttack to set
	 */
	public void setAttack(int index, boolean isAttack) {
		this.isSpecific[index][3] = isAttack;
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
		this.isSpecific[index][4] = isReaction;
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
		this.isSpecific[index][5] = isDuration;
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
		tmp[1] = getExpansion(index);
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
	
	public void setCard(int index, Object[] cardInfo) {
		if ( cardInfo[0] == null ) {
			//#debug info 
			System.out.println("cardinfo 0 is null ");
		}
		this.setName(index, cardInfo[0].toString());
		this.setExpansion(index, cardInfo[1].toString());
		this.setCost(index, ((Integer)cardInfo[2]).intValue());	
		this.setAction(index, ((Boolean)cardInfo[3]).booleanValue());
		this.setVictory(index, ((Boolean)cardInfo[4]).booleanValue());
		this.setTreasure(index, ((Boolean)cardInfo[5]).booleanValue());
		this.setAttack(index, ((Boolean)cardInfo[6]).booleanValue());
		this.setReaction(index, ((Boolean)cardInfo[7]).booleanValue());
		this.setDuration(index, ((Boolean)cardInfo[8]).booleanValue());
		this.setPlaying(index, ((Boolean)cardInfo[9]).booleanValue());
		this.setAvailable(index, ((Boolean)cardInfo[10]).booleanValue());
		this.setBlackMarketAvailable(index, ((Boolean)cardInfo[11]).booleanValue());
	}
	
	public static int compare(Object[] first, Object[] compareTo, int method) {
		if ( first[1].toString().equals(compareTo[1].toString()) )
			return first[0].toString().compareTo(compareTo[0].toString());
		else if ( first[1].toString().equals(Locale.get("rms.base")) )
			return -1;
		else if ( first[1].toString().equals(Locale.get("rms.promo")) )
			if ( compareTo[1].toString().equals(Locale.get("rms.base")) )
				return 1;
			else
				return -1;
		else if ( first[1].toString().equals(Locale.get("rms.intrigue")) )
			if ( compareTo[1].toString().equals(Locale.get("rms.base")) || compareTo[1].toString().equals(Locale.get("rms.promo")) )
				return 1;
			else
				return -1;
		else if ( first[1].toString().equals(Locale.get("rms.seaside")) )
			return 1;
		return 0;
	}
	
	public boolean contains(String cardName) {
		for ( int i = 0 ; i < this.size() ; i++ )
			if ( this.getName(i) != null )
				if ( this.getName(i).equals(cardName) )
					return true;
		return false;
	}
	
	public String toString(int index) {
		return this.getName(index);
	}
	
	public int size() {
		return name.length;
	}
}
