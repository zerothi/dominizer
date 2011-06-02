/**
 * 
 */
package com;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.enough.polish.ui.ChoiceItem;
import de.enough.polish.ui.Style;
//#if polish.usePolishGui
	//#= import de.enough.polish.ui.Style;
//#endif
/**
 * @author nick
 *
 */
public class CardItem extends ChoiceItem {
	private Image lI = null, rI = null;
	private boolean isBothSides = true;
	private int choiceType = 0;
	
	/**
	 * @param label
	 */
	public CardItem(String label, int listType) {
		this(label, null, null, listType);
	}
	
	/**
	 * @param label
	 */
	public CardItem(String label, Image lI, Image rI, int listType) {
		super(label, null, listType);
		this.lI = lI;
		this.rI = rI;
		choiceType = listType;
	}
	
	//#if polish.usePolishGui
	public CardItem( String label, int listType, Style style ) {
		this(label, null, null, listType, style);
	}

	public CardItem( String label, Image lI, Image rI, int listType, Style style ) {
		super( label, null, listType, style );
		this.lI = lI;
		this.rI = rI;
	    choiceType = listType;
	}
	//#endif

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CustomItem#paint(javax.microedition.lcdui.Graphics, int, int)
	 */
	public void paintContent(int x, int y, int xBorder, int yBorder, Graphics g) {
		if ( isBothSides ) {
			x -= super.availContentWidth / 2 - super.contentWidth / 2;
		}
		super.paintContent(x, y, xBorder, yBorder, g);
		
		if ( this.lI != null ) {
			if ( isBothSides ) {
				if ( choiceType == Choice.IMPLICIT ) {
					g.drawImage(this.lI, x + this.lI.getWidth() / 4, y, Graphics.TOP | Graphics.LEFT );
				} else {
					g.drawImage(this.lI, x + this.lI.getWidth() + this.lI.getWidth() / 4, y, Graphics.TOP | Graphics.LEFT );
				}
			} else {
				if ( this.rI != null )
					g.drawImage(this.lI, x + super.availContentWidth - this.rI.getWidth() - this.rI.getWidth() / 4 - this.lI.getWidth() / 4, y, Graphics.TOP | Graphics.RIGHT );
				else
					g.drawImage(this.lI, x + super.availContentWidth - this.lI.getWidth() / 4, y, Graphics.TOP | Graphics.RIGHT );
			}
		}
		if ( this.rI != null ) {
			g.drawImage( this.rI, x + super.availContentWidth - this.rI.getWidth() / 4, y, Graphics.TOP | Graphics.RIGHT);
		}
		
	}
	
	public void setBothSides(boolean isBothSides) {
		this.isBothSides = isBothSides;
	}
}
