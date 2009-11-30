

public class Card {
	private String name = null;
	private String expansion = null;
	/*
	 * #1 = Is Playing
	 * #2 = Is Selected for availability
	 * #3 = Is Black Market available
	 */
	private boolean[] isGamingRelated = new boolean[3]; 
	private int cost = 2;
	/*
	 * #1 = Action
	 * #2 = Victory
	 * #3 = Treasury
	 * #4 = Attack
	 * #5 = Reaction
	 * #6 = Duration
	 */
	private boolean[] isSpecific = new boolean[6];

	public Card() {
		super();
		this.setAvailable(true);
		this.setBlackMarketAvailable(true);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the game
	 */
	public String getExpansion() {
		return expansion;
	}

	/**
	 * @param game
	 *            the game to set
	 */
	public void setExpansion(String game) {
		this.expansion = game;
	}
	
	/**
	 * @return the isPlaying
	 */
	public boolean isPlaying() {
		return isGamingRelated[0];
	}

	/**
	 * @param isPlaying
	 *            the isPlaying to set
	 */
	public void setPlaying(boolean isPlaying) {
		this.isGamingRelated[0] = isPlaying;
	}


	/**
	 * @return the selected
	 */
	public boolean isAvailable() {
		return isGamingRelated[1];
	}

	/**
	 * @param available
	 *            the selected to set
	 */
	public void setAvailable(boolean available) {
		this.isGamingRelated[1] = available;
	}

	/**
	 * @return the bmSelected
	 */
	public boolean isBlackMarketAvailable() {
		return isGamingRelated[2];
	}

	/**
	 * @param bmSelected
	 *            the bmSelected to set
	 */
	public void setBlackMarketAvailable(boolean bmAvailable) {
		this.isGamingRelated[2] = bmAvailable;
	}

	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	
	/**
	 * @return the isAction
	 */
	public boolean isAction() {
		return isSpecific[0];
	}

	/**
	 * @param isAction
	 *            the isAction to set
	 */
	public void setAction(boolean isAction) {
		this.isSpecific[0] = isAction;
	}
	/**
	 * @return the isVictory
	 */
	public boolean isVictory() {
		return isSpecific[1];
	}

	/**
	 * @param isVictory
	 *            the isVictory to set
	 */
	public void setVictory(boolean isVictory) {
		this.isSpecific[1] = isVictory;
	}

	/**
	 * @return the isCoin
	 */
	public boolean isTreasure() {
		return isSpecific[2];
	}

	/**
	 * @param isCoin
	 *            the isCoin to set
	 */
	public void setTreasure(boolean isCoin) {
		this.isSpecific[2] = isCoin;
	}

	/**
	 * @param isAttack
	 *            the isAttack to set
	 */
	public void setAttack(boolean isAttack) {
		this.isSpecific[3] = isAttack;
	}

	/**
	 * @return the isAttack
	 */
	public boolean isAttack() {
		return isSpecific[3];
	}

	/**
	 * @return the isReaction
	 */
	public boolean isReaction() {
		return isSpecific[4];
	}

	/**
	 * @param isReaction
	 *            the isReaction to set
	 */
	public void setReaction(boolean isReaction) {
		this.isSpecific[4] = isReaction;
	}
	/**
	 * @return the isDuration
	 */
	public boolean isDuration() {
		return isSpecific[5];
	}

	/**
	 * @param isDuration
	 *            the isDuration to set
	 */
	public void setDuration(boolean isDuration) {
		this.isSpecific[5] = isDuration;
	}
}
