package dominion;

public class Card {
	private String name = null;
	private String game = null;
	private boolean isPlaying = false;
	private boolean isSelected = true;
	private boolean isBMSelected = true;
	private int cost = 2;
	private boolean isAction = false;
	private boolean isVictory = false;
	private boolean isCoin = false;
	private boolean isAttack = false;
	private boolean isReaction = false;
	
	public Card() {
		super();
	}
	
	public Card(String name) {
		super();
		this.name = name;
	}
	
	public Card(String name, String game) {
		super();
		this.name = name;
		this.game = game;
	}
	
	public Card(String name, String game, int cost) {
		super();
		this.name = name;
		this.game = game;
		this.cost = cost;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the game
	 */
	public String getGame() {
		return game;
	}
	/**
	 * @param game the game to set
	 */
	public void setGame(String game) {
		this.game = game;
	}
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return isSelected;
	}
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}
	/**
	 * @return the bmSelected
	 */
	public boolean isBlackMarketSelected() {
		return isBMSelected;
	}
	/**
	 * @param bmSelected the bmSelected to set
	 */
	public void setBlackMarketSelected(boolean bmSelected) {
		this.isBMSelected = bmSelected;
	}
	
	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
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
	 * @param isPlaying the isPlaying to set
	 */
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	/**
	 * @return the isBMSelected
	 */
	public boolean isBMSelected() {
		return isBMSelected;
	}

	/**
	 * @param isBMSelected the isBMSelected to set
	 */
	public void setBMSelected(boolean isBMSelected) {
		this.isBMSelected = isBMSelected;
	}

	/**
	 * @return the isVictory
	 */
	public boolean isVictory() {
		return isVictory;
	}

	/**
	 * @param isVictory the isVictory to set
	 */
	public void setVictory(boolean isVictory) {
		this.isVictory = isVictory;
	}

	/**
	 * @return the isCoin
	 */
	public boolean isCoin() {
		return isCoin;
	}

	/**
	 * @param isCoin the isCoin to set
	 */
	public void setCoin(boolean isCoin) {
		this.isCoin = isCoin;
	}

	/**
	 * @return the isReaction
	 */
	public boolean isReaction() {
		return isReaction;
	}

	/**
	 * @param isReaction the isReaction to set
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
	 * @param isAction the isAction to set
	 */
	public void setAction(boolean isAction) {
		this.isAction = isAction;
	}

	/**
	 * @param isAttack the isAttack to set
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
}
