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
			while ((ch = isr.read()) > -1) {
				Graphics g = expansions[exp].getGraphics();
				g.setColor(255, 255, 255);
				sb.append((char) ch);
				if ( !readDir && (char) ch == ';' ) {
					s = sb.toString().trim();
					dirDown = s.substring(0, s.length() - 1).equals("d");
					sb.delete(0, 10000);
				} else if ( readDir & start == 0 & (char) ch == ';' ) {
					start = Integer.parseInt(sb.toString().trim().substring(0, sb.toString().trim().indexOf(";") - 1));
					sb.delete(0, 10000);
				} else if ( readDir & start >= 0 & (char) ch == ';' ) {
					expansions[exp] = scaleImage(expansions[exp], 15, 15);
					exp++;
					readDir = false;
					start = -1;
					sb.delete(0, 10000);
				} else if ((char) ch == ':' ) {
					s = sb.toString().trim().substring(0, sb.toString().trim().length() - 1);
					//#debug dominizer
					System.out.println("processing " + s);
					oldComma = 0;
					do {
						comma = s.indexOf(",", oldComma);
						lineStart = Integer.parseInt(s.substring(oldComma, s.indexOf("+")));
						if ( dirDown ) 
							g.drawLine(start, lineStart, start, lineStart + Integer.parseInt(s.substring(s.indexOf("+") + 1)));
						else
							g.drawLine(lineStart, start, lineStart + Integer.parseInt(s.substring(s.indexOf("+") + 1)), start);	
						oldComma = comma + 1; 
					} while ( comma >= 0 );
				}
			}
			if (isr != null)
				isr.close();
		} catch (Exception ex) {
			//#debug dominizer
			System.out.println("exception on reading" + ex.toString());
		}
	}
	
	private Image scaleImage(Image sourceImage, int newWidth, int newHeight) {
        //Remember image size.
        int oldWidth = sourceImage.getWidth();
        int oldHeight = sourceImage.getHeight();
 
        //Create buffer for input image.
        int[] inputData = new int[oldWidth * oldHeight];
        //Fill it with image data.
        sourceImage.getRGB(inputData, 0, oldWidth, 0, 0, oldWidth, oldHeight);
 
        //Create buffer for output image.
        int[] outputData = new int[newWidth * newHeight];
 
        int YD = (oldHeight / newHeight - 1) * oldWidth;
        int YR = oldHeight % newHeight;
        int XD = oldWidth / newWidth;
        int XR = oldWidth % newWidth;
        //New image buffer offset.
        int outOffset = 0;
        //Source image buffer offset.
        int inOffset = 0;
 
        for (int y = newHeight, YE = 0; y > 0; y--) {
            for (int x = newWidth, XE = 0; x > 0; x--) {
                //Copying pixel from old image to new.
                outputData[outOffset++] = inputData[inOffset];
                inOffset += XD;
                //Calculations for "smooth" scaling in x dimension.
                XE += XR;
                if (XE >= newWidth) {
                    XE -= newWidth;
                    inOffset++;
                }
            }
            inOffset += YD;
            //Calculations for "smooth" scaling in y dimension.
            YE += YR;
            if (YE >= newHeight) {
                YE -= newHeight;
                inOffset += oldWidth;
            }
        }
        //Create image from output buffer.
        return Image.createRGBImage(outputData, newWidth, newHeight, false);
    }
}

