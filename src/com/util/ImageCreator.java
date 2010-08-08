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
			g.setColor(0, 0, 0);
			while ((ch = isr.read()) > -1) {
				sb.append((char) ch);
				if ( !readDir && (char) ch == ';' ) {
					s = sb.toString().trim();
					dirDown = s.substring(0, s.length() - 1).equals("d");
					sb.delete(0, 10000);
				} else if ( readDir & start == 0 & (char) ch == ';' ) {
					start = Integer.parseInt(sb.toString().trim().substring(0, sb.toString().trim().indexOf(";") - 1));
					sb.delete(0, 10000);
				} else if ( readDir & start >= 0 & (char) ch == ';' ) {
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
					exp++;
					g = expansions[exp].getGraphics();
					g.setColor(0, 0, 0);
				}
			}
			if (isr != null)
				isr.close();
		} catch (Exception ex) {
			//#debug dominizer
			System.out.println("exception on reading" + ex.toString());
		}
	}
}

