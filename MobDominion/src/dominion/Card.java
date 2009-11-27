package dominion;

public class Card {
	private String name = null;
	private String expansion = null;
	private boolean isPlaying = false;
	private boolean isSelected = true;
	private boolean isBlackMarketSelected = true;
	private int cost = 2;
	private boolean isAction = false;
	private boolean isVictory = false;
	private boolean isTreasure = false;
	private boolean isAttack = false;
	private boolean isReaction = false;
	private boolean isDuration = false;

	public Card() {
		super();
	}

	public Card(String name) {
		super();
		this.name = name;
	}

	public Card(String name, String expansion) {
		super();
		this.name = name;
		this.expansion = expansion;
	}

	public Card(String name, String expansion, int cost) {
		super();
		this.name = name;
		this.expansion = expansion;
		this.cost = cost;
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
	 * @return the selected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}

	/**
	 * @return the bmSelected
	 */
	public boolean isBlackMarketSelected() {
		return isBlackMarketSelected;
	}

	/**
	 * @param bmSelected
	 *            the bmSelected to set
	 */
	public void setBlackMarketSelected(boolean bmSelected) {
		this.isBlackMarketSelected = bmSelected;
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
	 * @return the isPlaying
	 */
	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * @param isPlaying
	 *            the isPlaying to set
	 */
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	/**
	 * @return the isVictory
	 */
	public boolean isVictory() {
		return isVictory;
	}

	/**
	 * @param isVictory
	 *            the isVictory to set
	 */
	public void setVictory(boolean isVictory) {
		this.isVictory = isVictory;
	}

	/**
	 * @return the isCoin
	 */
	public boolean isTreasure() {
		return isTreasure;
	}

	/**
	 * @param isCoin
	 *            the isCoin to set
	 */
	public void setTreasure(boolean isCoin) {
		this.isTreasure = isCoin;
	}

	/**
	 * @return the isReaction
	 */
	public boolean isReaction() {
		return isReaction;
	}

	/**
	 * @param isReaction
	 *            the isReaction to set
	 */
	public void setReaction(boolean isReaction) {
		this.isReaction = isReaction;
	}

	/**
	 * @return the isAction
	 */
	public boolean isAction() {
		return isAction;
	}

	/**
	 * @param isAction
	 *            the isAction to set
	 */
	public void setAction(boolean isAction) {
		this.isAction = isAction;
	}

	/**
	 * @param isAttack
	 *            the isAttack to set
	 */
	public void setAttack(boolean isAttack) {
		this.isAttack = isAttack;
	}

	/**
	 * @return the isAttack
	 */
	public boolean isAttack() {
		return isAttack;
	}

	/**
	 * @return the isDuration
	 */
	public boolean isDuration() {
		return isDuration;
	}

	/**
	 * @param isDuration
	 *            the isDuration to set
	 */
	public void setDuration(boolean isDuration) {
		this.isDuration = isDuration;
	}
}
