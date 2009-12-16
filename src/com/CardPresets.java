/**
 * 
 */
package com;

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
	
	public int[][] getPreset(int index) {
		return presets[index];
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
