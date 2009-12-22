/**
 * 
 */
package com;

import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class CardPresets {
	private int[][][] presets;
	private String[] presetNames;
	
	public CardPresets(int size) {
		presets = new int[size][10][2];
		presetNames = new String[size];
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
		return presetNames.length;
	}
}
