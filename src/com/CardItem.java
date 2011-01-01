/**
 * 
 */
package com;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.enough.polish.ui.ChoiceItem;
import de.enough.polish.ui.List;
//#if polish.usePolishGui
	//#= import de.enough.polish.ui.Style;
//#endif
/**
 * @author nick
 *
 */
public class CardItem extends ChoiceItem {
	private Image lI = null;
	private Image rI = null;
	private boolean isBothSides = true;
	private int choiceType = 0;
	/**
	 * @param label
	 */
	public CardItem(String label, int listType) {
		super(label, null, listType);
		choiceType = listType;
	}
	
	//#if polish.usePolishGui
	//#= public CardItem( String label, int listType, Style style ) {
	//#=	super( label, null, listType, style );
	//#=    choiceType = listType;
	//#= }
	//#endif

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CustomItem#paint(javax.microedition.lcdui.Graphics, int, int)
	 */
	public void paintContent(int x, int y, int xBorder, int yBorder, Graphics g) {
		super.paintContent(x, y, xBorder, yBorder, g);
		if ( this.lI != null ) {
			if ( isBothSides ) {
				if ( choiceType == Choice.IMPLICIT ) {
					g.drawImage(this.lI, x + this.lI.getWidth() / 3, y, Graphics.TOP | Graphics.LEFT );
				} else {
					g.drawImage(this.lI, x + this.lI.getWidth() + this.lI.getWidth() / 3, y, Graphics.TOP | Graphics.LEFT );
				}
			} else {
				if ( this.rI != null )
					g.drawImage(this.lI, x + super.availContentWidth - this.rI.getWidth() - this.rI.getWidth() / 3 - this.lI.getWidth() / 3, y, Graphics.TOP | Graphics.RIGHT );
				else
					g.drawImage(this.lI, x + super.availContentWidth - this.lI.getWidth() / 3, y, Graphics.TOP | Graphics.RIGHT );
			}
		}
		if ( this.rI != null ) {
			g.drawImage( this.rI, x + super.availContentWidth - this.rI.getWidth() / 3, y, Graphics.TOP | Graphics.RIGHT);
		}
	}
	
	public void setLeftImage(Image img) {
		this.lI = img;
	}
	
	public void setRightImage(Image img) {
		this.rI = img;
	}
	
	public void setBothSides(boolean isBothSides) {
		this.isBothSides = isBothSides;
	}
}
