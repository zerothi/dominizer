package com.util;

import java.io.InputStreamReader;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class ImageCreator {
	private Image[] cross = new Image[2];
	
	private Image[] expansions = new Image[5];
	private Image[] base = new Image[4];
	private Image[] intrigue = new Image[6];
	private Image[] seaside = new Image[5];
	private Image[] alchemy = new Image[3];
	private Image[] prosperity = new Image[3];
	private Image[] promo = new Image[3];
	private Image[] user = null;
	
	public ImageCreator() {
		// TODO read from text files the images and set user!
		int i;
		for ( i = 0 ; i < 5 ; i++ ) {
			expansions[i] = Image.createImage(200, 200);
		}
		createImages();
	}
	public void setCross(Image[] cross) {
		this.cross = cross;
	}
	public Image getCross(int i) {
		return cross[i];
	}
	
	public Image getCardImage(int exp, int type) {
		return expansions[exp];
//		switch (exp) {
//		case Dominion.BASE:
//			return base[type];
//		case Dominion.INTRIGUE:
//			return intrigue[type];
//		case Dominion.SEASIDE:
//			return seaside[type];
//		case Dominion.ALCHEMY:
//			return alchemy[type];
//		case Dominion.PROSPERITY:
//			return prosperity[type];
//		case Dominion.PROMO:
//			return promo[type];
//		case Dominion.USER:
//			return user[type];
//		}
//		return cross[0];
	}
	
	
	private void createImages() {
		InputStreamReader isr = null;
		StringBuffer sb = new StringBuffer(50);
		String s = "";
		int start = -1, exp = 0, comma, oldComma, lineStart;
		boolean readDir = false, dirDown = false;
		try {
			isr = new InputStreamReader(getClass().getResourceAsStream("/icons"), "UTF8");
			int ch;
			Graphics g = expansions[exp].getGraphics();
			while ((ch = isr.read()) > -1) {
				sb.append((char) ch);
				if ( !readDir && (char) ch == ';' ) {
					g = expansions[exp].getGraphics();
					g.setColor(255, 255, 255);
					g.fillRect(0, 0, 200, 200);
					g.setColor(0, 0, 0);
					s = sb.toString().trim();
					dirDown = s.substring(0, s.length() - 1).equals("d");
					sb.delete(0, 10000);
					readDir = true;
				} else if ( readDir & start == -1 & (char) ch == ';' ) {
					s = sb.toString().trim();
					//#debug dominizer
					System.out.println("processing start: " + s);
					start = Integer.parseInt(s.substring(0, s.indexOf(";")));
					sb.delete(0, 10000);
					//#debug dominizer
					System.out.println("processing with options: " + dirDown + " and start: " + start);
				} else if ( readDir & start >= 0 & (char) ch == ';' ) {
					expansions[exp] = scaleImage(expansions[exp], 15, 15);
					exp++;
					readDir = false;
					start = -1;
					sb.delete(0, 10000);
				} else if ((char) ch == ':' ) {
					if ( !sb.toString().trim().startsWith(":") ) {
						s = sb.toString().trim().substring(0, sb.toString().trim().length() - 1);
						//#debug dominizer
						System.out.println("processing " + s); 
						oldComma = -1;
						do {
							comma = s.indexOf(",", oldComma + 1);
							//#debug dominizer
							System.out.println("processing " + s + " at " + ( oldComma+1) + " and " + s.indexOf("+", oldComma + 1));
							lineStart = Integer.parseInt(s.substring(oldComma + 1, s.indexOf("+", oldComma + 1)));
							if ( dirDown ) 
								g.drawLine(start, lineStart, start, lineStart + Integer.parseInt(s.substring(s.indexOf("+", oldComma + 1) + 1, comma < 0 ? s.length() : comma)));	
							else
								g.drawLine(lineStart, start, lineStart + Integer.parseInt(s.substring(s.indexOf("+", oldComma + 1) + 1, comma < 0 ? s.length() : comma)), start);
							oldComma = comma;
						} while ( comma >= 0 );
					}
					sb.delete(0, 10000);
					start++;
				}
			}
			if (isr != null)
				isr.close();
		} catch (Exception ex) {
			//#debug dominizer
			System.out.println("exception on reading" + ex.toString());
		}
	}
	
	private Image scaleImage(Image orgImage, int newWidth, int newHeight) {
		int orgWidth = orgImage.getWidth();
		int orgHeight = orgImage.getHeight();
		int orgLength = orgWidth * orgHeight;
		int orgMax = orgLength - 1;
 
		int[] rawInput = new int[orgLength];
		orgImage.getRGB(rawInput, 0, orgWidth, 0, 0, orgWidth, orgHeight);
 
		int newLength = newWidth * newHeight;
 
		int[] rawOutput = new int[newLength];
 
		int yd = (orgHeight / newHeight - 1) * orgWidth;
		int yr = orgHeight % newHeight;
		int xd = orgWidth / newWidth;
		int xr = orgWidth % newWidth;
		int outOffset = 0;
		int inOffset = 0;
		
		// Whole pile of non array variables for the loop.
		int pixelA, pixelB, pixelC, pixelD;
		int xo, yo;
		int weightA, weightB, weightC, weightD;
		int redA, redB, redC, redD;
		int greenA, greenB, greenC, greenD;
		int blueA, blueB, blueC, blueD;
		int red, green, blue;
		
		for (int y = newHeight, ye = 0; y > 0; y--) {
			for (int x = newWidth, xe = 0; x > 0; x--) {
 
				// Set source pixels.
				pixelA = inOffset;
				pixelB = pixelA + 1;
				pixelC = pixelA + orgWidth;
				pixelD = pixelC + 1;
 
				// Get pixel values from array for speed, avoiding overflow.
				pixelA = rawInput[pixelA];
				pixelB = pixelB > orgMax ? pixelA : rawInput[pixelB];
				pixelC = pixelC > orgMax ? pixelA : rawInput[pixelC];
				pixelD = pixelD > orgMax ? pixelB : rawInput[pixelD];
				
				// Calculate pixel weights from error values xe & ye.
				xo = (xe << 8) / newWidth;
				yo = (ye << 8) / newHeight;
				weightD = xo * yo;
				weightC = (yo << 8) - weightD;
				weightB = (xo << 8) - weightD;
				weightA = 0x10000 - weightB - weightC - weightD;
				
				// Isolate colour channels.
				redA = pixelA >> 16;
				redB = pixelB >> 16;
				redC = pixelC >> 16;
				redD = pixelD >> 16;
				greenA = pixelA & 0x00FF00;
				greenB = pixelB & 0x00FF00;
				greenC = pixelC & 0x00FF00;
				greenD = pixelD & 0x00FF00;
				blueA = pixelA & 0x0000FF;
				blueB = pixelB & 0x0000FF;
				blueC = pixelC & 0x0000FF;
				blueD = pixelD & 0x0000FF;
 
				// Calculate new pixels colour and mask.
				red = 0x00FF0000 & (redA * weightA + redB * weightB + redC * weightC + redD * weightD);
				green = 0xFF000000 & (greenA * weightA + greenB * weightB + greenC * weightC + greenD * weightD);
				blue = 0x00FF0000 & (blueA * weightA + blueB * weightB + blueC * weightC + blueD * weightD);
 
				// Store pixel in output buffer and increment offset.
				rawOutput[outOffset++] = red + (((green | blue) >> 16));
				
				// Increment input by x delta.
				inOffset += xd;
				
				// Correct if we have a roll over error.
				xe += xr;
				if (xe >= newWidth) {
					xe -= newWidth;
					inOffset++;
				}
			}
			
			// Increment input by y delta.
			inOffset += yd;
			
			// Correct if we have a roll over error.
			ye += yr;
			if (ye >= newHeight) {
				ye -= newHeight;
				inOffset += orgWidth;
			}
		}
        //Create image from output buffer.
        return Image.createRGBImage(rawOutput, newWidth, newHeight, false);
    }
}

