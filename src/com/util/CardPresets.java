/**
 * 
 */
package com.util;


/**
 * @author nick
 *presets[presetDeck].size()
 */
public class CardPresets {
	private int[][][] presets = null;
	private int[][] baneCard = null;
	private String[] presetNames = null;
	private int expansion = -1;

	public CardPresets(int size) {
		if ( 0 < size ) {
			presets = new int[size][10][2];
			baneCard = new int[size][2];
			presetNames = new String[size];
			for ( int i = 0 ; i < size ; i++ )
				baneCard[i] = new int[] { -1 , -1};
		} 
	}
	
	public int[] getBaneCard(int preset ) {
		return baneCard[preset];
	}

	public void setBaneCard(int preset, int[] baneCard) {
		this.baneCard[preset]= baneCard;
	}

	/**
	 * @return the expansion
	 */
	public int getExpansion() {
		return expansion;
	}

	/**
	 * @param expansion the expansion to set
	 */
	public void setExpansion(int expansion) {
		this.expansion = expansion;
	}
	
	public int[][] getPreset(int preset) {
		return presets[preset];
	}
	
	public int[] getPresetCard(int preset, int card) {
		return presets[preset][card];
	}
	
	public int getPresetCardExpansion(int preset, int card) {
		return presets[preset][card][0];
	}
	public int getPresetCardPlacement(int preset, int card) {
		return presets[preset][card][1];
	}
	
	public int[][] getPreset(String presetName) {
		for ( int i = 0 ; i < presetNames.length ; i++ )
			if ( presetNames[i].equals(presetName) )
				return presets[i];
		return null;
	}
	
	public String getPresetName(int index) {
		return presetNames[index];
	}
	
	public void setPreset(int index, String name, int[][] preset) {
		presetNames[index] = name;
		presets[index] = preset;
	}
	
	public int size() {
		if ( presetNames == null )
			return 0;
		return presetNames.length;
	}
	
	public int size(int i) {
		if ( presetNames == null )
			return 0;
		return presets[i].length;
	}
}
