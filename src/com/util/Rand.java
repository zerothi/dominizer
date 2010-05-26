/**
 * 
 */
package com.util;

import java.util.Random;

/**
 * @author nick
 *
 */
public class Rand {
	private static Random rnd = null;
	private static int i = 0;
	

	public static void resetSeed() {
		if ( rnd == null )
			rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
	}
	public static void resetSeed(long seed) {
		if ( rnd == null ) 
			rnd = new Random();
		rnd.setSeed(seed);
	}
	
	public static int randomInt(int max) {
		if ( rnd == null )
			rnd = new Random();
		i = rnd.nextInt() % max;
		if ( i < 0 )
			return -i;
		return i;
	}
}