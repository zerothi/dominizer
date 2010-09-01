package com.util;

import java.io.IOException;

import javax.microedition.lcdui.Image;



/**
 * @author nick
 *
 */
/**
 * @author nick
 *
 */
/**
 * @author nick
 *
 */
public class Cards {

	private String[] name = null;
	private short[] expansion = null;
	
	private short[] playing = null;
	/*
	 * 1 = Is Selected for availability
	 * 2 = Is Black Market available
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
			playing = new short[size];
			isGamingRelated = new short[size];
			cost = new short[size];
			isSpecific = new boolean[size][6];
			addsInfo = new int[size][8];
			if ( isSet == IS_SET )
				expansion = new short[1];
			else
				expansion = new short[size];
			expansion[0] = -1;
			percentage = new int[size];
			for ( i = 0 ; i < size ; i++ ) {
				name[i] = null;
				cost[i] = 0;
				playing[i] = 0;
				/*
				 * Adding 1 makes it selected for availability
				 * Adding 2 makes it selectable for the Black Market deck
				 */
				isGamingRelated[i] = 1 + 2;
				for ( k = 0 ; k < addsInfo[i].length ; k++ )
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
			return expansion[0];
		return expansion[index];
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
		return percentage[index];
	}

	
	/**
	 * @param index the index of the card in the list
	 * @param percentage the percentage which is to be assigned with the card
	 */
	public void setPercentage(int index, int percentage) {
		this.percentage[index] = percentage;
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
		return playing[index];
	}
	
	
	/**
	 * @param index the index of the card in the list
	 * @param playingSet the playingset which needs to be checked
	 * @return true if the card is playing in the playingset, otherwise false
	 */
	public boolean isPlayingSet(int index, int playingSet) {
		if ( playing[index] > 100 )
			return playing[index] - 100 == playingSet;
		return playing[index] == playingSet;
	}

	/**
	 * @param index the index of the card in the list
	 * @param playing the playing state of the card
	 */
	public void setPlaying(int index, int playing) {
		this.playing[index] = (short) playing;
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
	 * @param playingSet the playing set to be checked
	 * @return true if the card is holded on the playingset, false otherwise
	 */
	public boolean isHold(int index, int playingSet) {
		return playing[index] == playingSet + 100;
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
		for ( i = 0 ; i < playing.length ; i++ )
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
			if ( 0 < playing[index] & playing[index] < 100 )
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
		return ((isGamingRelated[index] >> 1) & 1) > 0;
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
	 * @return the cobber cost of the card
	 */
	public int getCost(int index) {
		return (cost[index] & 31);
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
		isSpecific[index][whichType] = state;
	}
		
	
	/**
	 * @param index the index of the card in the list
	 * @param whichType the type to check for
	 * @return true if it is the type, false otherwise
	 */
	public boolean isType(int index, int whichType) {
		return isSpecific[index][whichType];
	}
	
	/**
	 * @param index the index of the card in the list
	 * @param whichType the type to check for
	 * @return true if the card is only that one type, false otherwise
	 */
	public boolean isOnlyType(int index, int whichType) {
		boolean tmp = false;
		for (int i = 0 ; i < isSpecific[index].length ; i++ )
			tmp = (i == whichType ? tmp : tmp | isSpecific[index][i]);
		return !(isSpecific[index][whichType] & tmp);
	}
		
	/**
	 * @param index
	 * @return
	 */
	public int getCardType(int index) {
		if ( isType(index, TYPE_ACTION) ) {
			if ( isType(index, TYPE_VICTORY) )
				return TYPE_ACTION_VICTORY;
			if ( isType(index, TYPE_TREASURY) )
				return TYPE_ACTION_TREASURY;
			if ( isType(index, TYPE_ATTACK) )
				return TYPE_ACTION_ATTACK;
			if ( isType(index, TYPE_DURATION) )
				return TYPE_ACTION_DURATION;
			if ( isType(index, TYPE_REACTION) ) // last because of Lighthouse
				return TYPE_ACTION_REACTION;
			// Default is action anyway!
		} else if ( isOnlyType(index, TYPE_VICTORY) ) {
			return TYPE_VICTORY;
		} else if ( isType(index, TYPE_TREASURY) ) {
			if ( isType(index, TYPE_VICTORY) )
				return TYPE_TREASURY_VICTORY;
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
			// TODO : what if it is only a ATTACK
		} else if ( isType(index, TYPE_REACTION) ) {
			if ( isType(index, TYPE_VICTORY) )
				return TYPE_REACTION_VICTORY;
			if ( isType(index, TYPE_TREASURY) )
				return TYPE_REACTION_TREASURY;
			if ( isType(index, TYPE_DURATION) )
				return TYPE_REACTION_DURATION;
			// TODO : what if it is only a REACTION
		} else if ( isType(index, TYPE_DURATION) ) {
			if ( isType(index, TYPE_VICTORY) )
				return TYPE_DURATION_VICTORY;
			if ( isType(index, TYPE_TREASURY) )
				return TYPE_DURATION_TREASURY;
			// TODO : what if it is only a DURATION
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
		Object[] tmp = new Object[20];
		tmp[0] = getName(index);
		tmp[1] = new Integer(getExpansion(index));
		tmp[2] = new Integer(getCost(index)+getPotionCost(index)*100);
		tmp[3] = new Boolean(isType(index, TYPE_ACTION));
		tmp[4] = new Boolean(isType(index, TYPE_VICTORY));
		tmp[5] = new Boolean(isType(index, TYPE_ATTACK));
		tmp[6] = new Boolean(isType(index, TYPE_TREASURY));
		tmp[7] = new Boolean(isType(index, TYPE_REACTION));
		tmp[8] = new Boolean(isType(index, TYPE_DURATION));
		tmp[9] = new Integer(isPlaying(index));
		tmp[10] = new Boolean(isAvailable(index));
		tmp[11] = new Boolean(isBlackMarketAvailable(index));
		tmp[12] = new Integer(getAddInfo(index, ADDS_CARDS));
		tmp[13] = new Integer(getAddInfo(index, ADDS_ACTIONS));
		tmp[14] = new Integer(getAddInfo(index, ADDS_BUYS));
		tmp[15] = new Integer(getAddInfo(index, ADDS_COINS));
		tmp[16] = new Integer(getAddInfo(index, ADDS_TRASH));
		tmp[17] = new Integer(getAddInfo(index, ADDS_CURSE));
		tmp[18] = new Integer(getAddInfo(index, ADDS_POTIONS));
		tmp[19] = new Integer(getAddInfo(index, ADDS_VICTORY_POINTS));
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
		this.cost[index] = (short)((Integer)cardInfo[2]).intValue(); // simply to bypass the potion cost.	
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
		setAddInfo(index, ADDS_VICTORY_POINTS, ((Integer)cardInfo[19]).intValue());
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
			/*
			 Image source = Image.createImage("/trea" + getCost(card) + ".png");
		    Image copy = Image.createImage(source.getWidth(), source.getHeight());
		    Graphics g = copy.getGraphics();
		    g.drawImage(source, 0, 0, Graphics.TOP | Graphics.LEFT);
			if ( copy.isMutable() ) {
				//#debug dominizer
				System.out.println("image is mutable");
				g.drawString(""+getCost(card), source.getWidth() / 2, source.getHeight() / 2, Graphics.HCENTER | Graphics.VCENTER);
			}*/
			if ( getPotionCost(card) > 0 ) {
				return Image.createImage("/t" + getCost(card) + "P.png");
			} else {
				return Image.createImage("/t" + getCost(card) + ".png");
			}
		} catch (IOException exp) {
			return null;
		}
	}
	
	public Image getCardTypeImage(int card) {
		try {
			if ( getExpansion(card) == Dominion.PROMO ) {
				if ( getName(card).equals(Dominion.expansions[Dominion.PROMO].getName(0)) )
					return Image.createImage("/" + Dominion.getExpansionImageName(Dominion.PROMO) + "0.png");
				else if ( getName(card).equals(Dominion.expansions[Dominion.PROMO].getName(1)) )
					return Image.createImage("/" + Dominion.getExpansionImageName(Dominion.PROMO) + "1.png");
				else if ( getName(card).equals(Dominion.expansions[Dominion.PROMO].getName(2)) )
					return Image.createImage("/" + Dominion.getExpansionImageName(Dominion.PROMO) + "2.png");
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
