package com.util;

import java.io.IOException;

import javax.microedition.lcdui.Image;

import com.dominizer.GameApp;


/**
 * @author nick
 */
public class Cards {

	private String[] name = null;
	private short[] expansion = null;
	
	private short[] playing = null;
	/*
	 * 1 = Is Selected for availability
	 * 2 = Is Black Market available
	 * 4 = Is Bane Card
	 */
	private short[] isGamingRelated = null; 
	private short[] cost = null;
	/*
	 * #0 = Action
	 * #1 = Victory
	 * #2 = Treasury
	 * #3 = Attack
	 * #4 = Reaction
	 * #5 = Duration
	 */
	private short[] isSpecific = null;
	/*
	 * #0 = Adds # Cards
	 * #1 = Adds # Actions
	 * #2 = Adds # Buys
	 * #3 = Adds # Coins
	 * #4 = Adds # Trash
	 * #5 = Adds # Curse
	 * #6 = Adds # Potions
	 * #7 = Adds # Victory Points
	 */
	private short[][] addsInfo = null;
	private short[] percentage = null;

	public Cards(int size, int isSet) {
		if ( size > 0 ) {
			name = new String[size];
			playing = new short[size];
			isGamingRelated = new short[size];
			cost = new short[size];
			isSpecific = new short[size];
			addsInfo = new short[size][8];
			if ( isSet == IS_SET )
				expansion = new short[1];
			else
				expansion = new short[size];
			expansion[0] = -1;
			percentage = new short[size];
			for ( int i = 0 ; i < size ; i++ ) {
				name[i] = null;
				cost[i] = 0;
				playing[i] = 0;
				/*
				 * Adding 1 makes it selected for availability
				 * Adding 2 makes it selectable for the Black Market deck
				 * Adding 4 makes it a Bane card, so no need for that!
				 */
				isGamingRelated[i] = 1 + 2;
				for ( int k = 0 ; k < addsInfo[i].length ; k++ )
					addsInfo[i][k] = 0;
				if ( isSet == IS_NOT_SET )
					expansion[i] = -1;
				percentage[i] = 0;
			}
			
		}
	}

	/**
	 * @param index the index of the card in the list
	 * @return the name of the card
	 */
	public String getName(int index) {
		return name[index];
	}


	/**
	 * @param index the index of the card in the list
	 * @param name the name that should be given to the card
	 */
	public void setName(int index, String name) {
		this.name[index] = name;
	}

	
	/**
	 * @return the expansion number of the list determined from the first card
	 */
	public int getExpansion() {
		if ( expansion != null)
			return expansion[0];
		return -1;
	}
	

	/**
	 * @param index the index of the card in the list
	 * @return the expansion number of the card
	 */
	public int getExpansion(int index) {
		if ( expansion.length == 1 )
			return (int) expansion[0];
		return (int) expansion[index];
	}

	
	/**
	 * @param index the index of the card in the list
	 * @param expansion the expansion which should be assigned with the card
	 */
	public void setExpansion(int index, int expansion) {
		this.expansion[index] = (short) expansion;
	}
	

	/**
	 * @param index the index of the card in the list
	 * @return the percentage set on the card
	 */
	public int getPercentage(int index) {
		return (int) percentage[index];
	}

	
	/**
	 * @param index the index of the card in the list
	 * @param percentage the percentage which is to be assigned with the card
	 */
	public void setPercentage(int index, int percentage) {
		this.percentage[index] = (short) percentage;
	}

	
	/**
	 * @param expansion sets the default expansion number
	 */
	public void setExpansion(int expansion) {
		this.expansion[0] = (short) expansion;
	}

	/**
	 * @param index the index of the card in the list
	 * @return an integer which determines the state of the card
	 */
	public int isPlaying(int index) {
		return (int) playing[index];
	}
	
	
	/**
	 * @param index the index of the card in the list
	 * @param playingSet the playingset which needs to be checked
	 * @return true if the card is playing in the playingset, otherwise false
	 */
	public boolean isPlayingSet(int index, int playingSet) {
		if ( playing[index] > 100 )
			return (playing[index] - 100) == playingSet;
		return playing[index] == playingSet;
	}

	/**
	 * @param index the index of the card in the list
	 * @param playing the playing state of the card
	 */
	public void setPlaying(int index, int playing) {
		this.playing[index] = (short) playing;
		if ( playing == 0 )
			setBaneCard(index, false);
	}
	
	/**
	 * @param index the index of the card in the list
	 * @return how much the card costs in potions
	 */
	public int getPotionCost(int index) {
		return (cost[index] >>> 5);
	}
	
	/**
	 * @param index the index of the card in the list
	 * @return how much the card costs in potions
	 */
	public int getTotalCost(int index) {
		return getCost(index) + getPotionCost(index);
	}
	
	/**
	 * @param index the index of the card in the list
	 * @param playingSet the playing set to be checked
	 * @return true if the card is holded on the playingset, false otherwise
	 */
	public boolean isHold(int index, int playingSet) {
		return playing[index] == (playingSet + 100);
	}
	
	/**
	 * @param index the index of the card in the list
	 * @return true if the card is holded on any playingset, false otherwise
	 */
	public boolean isHold(int index) {
		return playing[index] > 100;
	}
	
	/**
	 * @param name the name of the card
	 * @param the new hold state of the card
	 */
	public void setHoldCard(String name, boolean hold) {
		for ( int i = 0 ; i < playing.length ; i++ )
			if ( this.name[i].equals(name) ) {
				setHoldCard(i, hold);
				return;
			}
	}
	
	/**
	 * @param index the index of the card in the list
	 * @param the new hold state of the card 
	 */
	public void setHoldCard(int index, boolean hold) {
		if ( hold ) {
			if ( 0 < playing[index] && playing[index] < 100 )
				playing[index] = (short) (playing[index] + 100);
		} else {
			if ( playing[index] > 100 )
				playing[index] = (short) (playing[index] - 100);
		}
	}
	

	/**
	 * @param index the index of the card in the list
	 * @return the availability of the card
	 */
	public boolean isAvailable(int index) {
		return (isGamingRelated[index] & 1) > 0;
	}

	
	/**
	 * @param index the index of the card in the list
	 * @param available the available state of the card
	 */
	public void setAvailable(int index, boolean available) {
		if ( available )
			isGamingRelated[index] = (short) (isGamingRelated[index] | 1);
		else
			isGamingRelated[index] -= (short) (isGamingRelated[index] & 1) ;
	}

	/**
	 * @param index the index of the card in the list
	 * @return true if the card is available for the Black Market
	 */
	public boolean isBlackMarketAvailable(int index) {
		return (isGamingRelated[index] & 2) > 0;
	}

	/**
	 * @param index the index of the card in the list
	 * @param bmAvailable the state of the Black Market availability
	 */
	public void setBlackMarketAvailable(int index, boolean bmAvailable) {
		if ( bmAvailable )
			isGamingRelated[index] = (short) (isGamingRelated[index] | 2);
		else
			isGamingRelated[index] -= (short) (isGamingRelated[index] & 2) ;
	}
	
	/**
	 * @param index the index of the card in the list
	 * @return whether the card is a Bane card
	 */
	public boolean isBaneCard(int index) {
		return (isGamingRelated[index] & 4) > 0;
	}
	
	/**
	 * @param index the index of the card in the list
	 * @param isBane the state of the Bane possibility
	 */
	public void setBaneCard(int index, boolean isBane) {
		if ( isBane )
			isGamingRelated[index] = (short) (isGamingRelated[index] | 4);
		else
			isGamingRelated[index] -= (short) (isGamingRelated[index] & 4) ;
	}

	/**
	 * @param index the index of the card in the list
	 * @return the cobber cost of the card
	 */
	public int getCost(int index) {
		return (int) (cost[index] & 31);
	}
	
	/**
	 * @param index the index of the card in the list
	 * @param cost the cost in cobber of the card
	 * @param potions the cost in potions of the card
	 */
	public void setCost(int index, int cost, int potions) {
		this.cost[index] = (short) ((potions << 5) | cost);
	}

	/**
	 * @param index the index of the card in the list
	 * @param whichType which type is the card
	 * @param state sets if it is the type of card or not
	 */
	public void setType(int index, int whichType, boolean state) {
		if ( state )
			isSpecific[index] = (short) (isSpecific[index] | (1 << whichType));
		else if ( (isSpecific[index] >>> whichType & 1) == 1 )
			isSpecific[index] = (short) (isSpecific[index] ^ (1 << whichType));
	}
		
	
	/**
	 * @param index the index of the card in the list
	 * @param whichType the type to check for
	 * @return true if it is the type, false otherwise
	 */
	public boolean isType(int index, int whichType) {
		return ((isSpecific[index] >>> whichType) & 1) == 1;
	}
	
	/**
	 * @param index the index of the card in the list
	 * @param whichType the type to check for
	 * @return true if the card is only that one type, false otherwise
	 */
	public boolean isOnlyType(int index, int whichType) {
		return (isSpecific[index] | ( 1 << whichType )) == ( 1 << whichType );  
	}
		
	/**
	 * @param index
	 * @return
	 */
	public int getCardType(int index) {
		if ( isOnlyType(index, TYPE_VICTORY) ) {
			return TYPE_VICTORY;
		} else if ( isType(index, TYPE_TREASURY) ) {
			if ( isType(index, TYPE_VICTORY) )
				return TYPE_TREASURY_VICTORY;
			if ( isType(index, TYPE_ACTION) )
				return TYPE_ACTION_TREASURY;
			return TYPE_TREASURY;
		} else if ( isType(index, TYPE_ATTACK) ) {
			if ( isType(index, TYPE_VICTORY) )
				return TYPE_ATTACK_VICTORY;
			if ( isType(index, TYPE_TREASURY) )
				return TYPE_ATTACK_TREASURY;
			if ( isType(index, TYPE_REACTION) )
				return TYPE_ATTACK_REACTION;
			if ( isType(index, TYPE_DURATION) )
				return TYPE_ATTACK_DURATION;
			if ( isType(index, TYPE_ACTION) )
				return TYPE_ACTION_ATTACK;
			// TODO : what if it is only a ATTACK
		} else if ( isType(index, TYPE_DURATION) ) {
			if ( isType(index, TYPE_VICTORY) )
				return TYPE_DURATION_VICTORY;
			if ( isType(index, TYPE_TREASURY) )
				return TYPE_DURATION_TREASURY;
			if ( isType(index, TYPE_ACTION) )
				return TYPE_ACTION_DURATION;
			// TODO : what if it is only a DURATION
		} else if ( isType(index, TYPE_REACTION) ) {
			if ( isType(index, TYPE_VICTORY) )
				return TYPE_REACTION_VICTORY;
			if ( isType(index, TYPE_TREASURY) )
				return TYPE_REACTION_TREASURY;
			if ( isType(index, TYPE_DURATION) )
				return TYPE_REACTION_DURATION;
			if ( isType(index, TYPE_ACTION) ) // last because of Lighthouse
				return TYPE_ACTION_REACTION;
			// TODO : what if it is only a REACTION
		} else if ( isType(index, TYPE_ACTION) ) {
			if ( isType(index, TYPE_VICTORY) )
				return TYPE_ACTION_VICTORY;
			
			// Default is action anyway!
		}
		return TYPE_ACTION;
	}
	
	public void setAddInfo(int index, int whichAddInfo, int info) {
		addsInfo[index][whichAddInfo] = (short) info;
	}
	
	public int getAddInfo(int index, int whichAddInfo) {
		return addsInfo[index][whichAddInfo];
	}
	
	public Object[] getCard(int index) {
		Object[] tmp = new Object[14];
		tmp[0] = getName(index);
		tmp[1] = new Short((short) getExpansion(index));
		tmp[2] = new Short((short) ((getPotionCost(index) << 5) + getCost(index)));
		tmp[3] = new Short(isSpecific[index]);
		tmp[4] = new Short((short) isPlaying(index));
		tmp[5] = new Short(isGamingRelated[index]);
		tmp[6] = new Integer(getAddInfo(index, ADDS_CARDS));
		tmp[7] = new Integer(getAddInfo(index, ADDS_ACTIONS));
		tmp[8] = new Integer(getAddInfo(index, ADDS_BUYS));
		tmp[9] = new Integer(getAddInfo(index, ADDS_COINS));
		tmp[10] = new Integer(getAddInfo(index, ADDS_TRASH));
		tmp[11] = new Integer(getAddInfo(index, ADDS_CURSE));
		tmp[12] = new Integer(getAddInfo(index, ADDS_POTIONS));
		tmp[13] = new Integer(getAddInfo(index, ADDS_VICTORY_POINTS));
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
		if ( cardInfo.length == 0 ) 
			return;
		for ( int i = 0 ; i < cardInfo.length ; i++ ) {
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
		setExpansion(index, ((Short) cardInfo[1]).shortValue());
		this.cost[index] = ((Short) cardInfo[2]).shortValue(); // simply to bypass the potion cost.
		isSpecific[index] = ((Short) cardInfo[3]).shortValue();
		setPlaying(index, ((Short) cardInfo[4]).shortValue());
		isGamingRelated[index] = ((Short) cardInfo[5]).shortValue();
		setAddInfo(index, ADDS_CARDS, ((Integer)cardInfo[6]).intValue());
		setAddInfo(index, ADDS_ACTIONS, ((Integer)cardInfo[7]).intValue());
		setAddInfo(index, ADDS_BUYS, ((Integer)cardInfo[8]).intValue());
		setAddInfo(index, ADDS_COINS, ((Integer)cardInfo[9]).intValue());
		setAddInfo(index, ADDS_TRASH, ((Integer)cardInfo[10]).intValue());
		setAddInfo(index, ADDS_CURSE, ((Integer)cardInfo[11]).intValue());
		setAddInfo(index, ADDS_POTIONS, ((Integer)cardInfo[12]).intValue());
		setAddInfo(index, ADDS_VICTORY_POINTS, ((Integer)cardInfo[13]).intValue());
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
			if ( ((Short) first[1]).shortValue() > ((Short) compareTo[1]).shortValue() )
				return 1;
			else if ( ((Short) first[1]).shortValue() < ((Short) compareTo[1]).shortValue() )
				return -1;
			else return 0;
		case COMPARE_COST:
			if ( ((Short) first[2]).shortValue() > ((Short) compareTo[2]).shortValue() )
				return 1;
			else if ( ((Short) first[2]).shortValue() < ((Short) compareTo[2]).shortValue() )
				return -1;
			else return 0;
		case COMPARE_NAME:
			return first[0].toString().compareTo(compareTo[0].toString());
		default: return compare(first, compareTo, COMPARE_EXPANSION_NAME);
		}
	}
	
	public boolean contains(String cardName) {
		if ( cardName == null )
			return false;
		for ( int i = 0 ; i < size() ; i++ )
			if ( cardName.equals(getName(i)) )
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
			return Image.createImage("/t" + ( isBaneCard(card) ? "B" : "" ) + getCost(card) + ( getPotionCost(card) > 0 ? "P" : "" ) + ".png");
		} catch (IOException exp) {
			return null;
		}
	}
	
	public Image getCardTypeImage(int card) {
		try {
			if ( getExpansion(card) == Dominion.PROMO ) {
				for ( int i = 0 ; i < Dominion.expansions[Dominion.PROMO].size() ; i++ ) {
					if ( getName(card).equals(Dominion.expansions[Dominion.PROMO].getName(i)) )
						return Image.createImage("/" + Dominion.getExpansionImageName(Dominion.PROMO) + i + ".png");
				}
				return null;
			} else {
				return Image.createImage("/" + Dominion.getExpansionImageName(getExpansion(card)) + 
					getCardType(card) + ".png");
			}
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
	
	public void increaseSize(int bySize) {
		name = cloneArray(name, bySize);
		playing = cloneArray(playing, bySize);
		isGamingRelated = cloneArray(isGamingRelated, bySize);
		cost = cloneArray(cost, bySize);
		isSpecific = cloneArray(isSpecific, bySize);
		addsInfo = cloneArray(addsInfo, bySize);
		if ( expansion.length > 1 )
			expansion = cloneArray(expansion, bySize);
		percentage = cloneArray(percentage, bySize);
	}
	
	private static int[] cloneArray(int[] array, int bySize) {
		int[] tmp = new int[array.length + bySize];
		for ( int i = 0 ; i < array.length ; i++ ) {
			tmp[i] = array[i];
		}
		return tmp;
	}
	private static String[] cloneArray(String[] array, int bySize) {
		String[] tmp = new String[array.length + bySize];
		for ( int i = 0 ; i < array.length ; i++ ) {
			tmp[i] = array[i];
		}
		return tmp;
	}
	private static short[] cloneArray(short[] array, int bySize) {
		short[] tmp = new short[array.length + bySize];
		for ( int i = 0 ; i < array.length ; i++ ) {
			tmp[i] = array[i];
		}
		return tmp;
	}
	private static short[][] cloneArray(short[][] array, int bySize) {
		short[][] tmp = new short[array.length + bySize][array[0].length];
		for ( int i = 0 ; i < array.length ; i++ ) {
			for ( int j = 0 ; j < array[i].length ; j++ )
				tmp[i][j] = array[i][j];
		}
		return tmp;
	}
	
	public int[][][] getTypeInfo() {
		if ( size() == 0 )
			return null;
		int[][][] tmp = new int[2][1][6];
		tmp[0][0][0] = getTypes(TYPE_ACTION);
		tmp[0][0][1] = getTypes(TYPE_ATTACK);
		tmp[0][0][2] = getTypes(TYPE_REACTION);
		tmp[0][0][3] = getTypes(TYPE_TREASURY);
		tmp[0][0][4] = getTypes(TYPE_VICTORY);
		tmp[0][0][5] = getTypes(TYPE_DURATION);
		
		tmp[1][0][0] = 234 << 16 | 227 << 8 | 227;
		tmp[1][0][1] = 209 << 16 |  25 << 8 |  25;
		tmp[1][0][2] =  48 << 16 |  63 << 8 | 218;
		tmp[1][0][3] =   8 << 16 | 176 << 8 |  18;
		tmp[1][0][4] = 234 << 16 | 227 << 8 | 227;
		tmp[1][0][5] = 228 << 16 | 126 << 8 |  13;
		
		return tmp;
	}
	
	public int[][][] getAddsInfo() {
		if ( size() == 0 )
			return null;
		int[][][] tmp = new int[2][1][8];
		tmp[0][0][0] = getTypes(ADDS_CARDS);
		tmp[0][0][1] = getTypes(ADDS_ACTIONS);
		tmp[0][0][2] = getTypes(ADDS_BUYS);
		tmp[0][0][3] = getTypes(ADDS_COINS);
		tmp[0][0][4] = getTypes(ADDS_TRASH);
		tmp[0][0][5] = getTypes(ADDS_CURSE);
		tmp[0][0][6] = getTypes(ADDS_VICTORY_POINTS);
		tmp[0][0][7] = getTypes(ADDS_POTIONS);
		
		tmp[1][0][0] =  40 << 16 |  86 << 8 | 141;
		tmp[1][0][1] = 198 << 16 | 211 << 8 | 227;
		tmp[1][0][2] = 230 << 16 | 255 << 8 | 25;
		tmp[1][0][3] =   8 << 16 | 176 << 8 |  18;
		tmp[1][0][4] =   0 << 16 |   0 << 8 |   0;
		tmp[1][0][5] = 202 << 16 |   6 << 8 | 193;
		tmp[1][0][6] = 234 << 16 | 227 << 8 | 227;
		tmp[1][0][7] =  25 << 16 | 131 << 8 | 255;
		return tmp;
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
	// The bitwise operations need the types to be consecutive! Do NOT change!
	public static final int TYPE_ACTION = 0;
	public static final int TYPE_VICTORY = 1;
	public static final int TYPE_TREASURY = 2;
	public static final int TYPE_ATTACK = 3;
	public static final int TYPE_REACTION = 4;
	public static final int TYPE_DURATION = 5;
	
	public static final int TYPE_ACTION_VICTORY = 21;
	public static final int TYPE_ACTION_TREASURY = 22;
	public static final int TYPE_ACTION_ATTACK = 23;
	public static final int TYPE_ACTION_REACTION = 24;
	public static final int TYPE_ACTION_DURATION = 25;
	
	public static final int TYPE_TREASURY_VICTORY = 31;
	
	public static final int TYPE_ATTACK_VICTORY = 41;
	public static final int TYPE_ATTACK_TREASURY = 42;
	public static final int TYPE_ATTACK_REACTION = 44;
	public static final int TYPE_ATTACK_DURATION = 45;
	
	public static final int TYPE_REACTION_VICTORY = 51;
	public static final int TYPE_REACTION_TREASURY = 52;
	public static final int TYPE_REACTION_DURATION = 55;
	
	public static final int TYPE_DURATION_VICTORY = 61;
	public static final int TYPE_DURATION_TREASURY = 62;
	
		
	// TODO : When changing the below to non-consecutive numbers please update method "parseCondition" in Dominion
	public static final int ADDS_CARDS = 0;
	public static final int ADDS_ACTIONS = 1;
	public static final int ADDS_BUYS = 2;
	public static final int ADDS_COINS = 3;
	public static final int ADDS_TRASH = 4;
	public static final int ADDS_CURSE = 5;
	public static final int ADDS_VICTORY_POINTS = 6;
	public static final int ADDS_POTIONS = 7;
	
	public static final int COST_POTIONS = 100;
	
}
