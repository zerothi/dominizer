/**
 * 
 */
package com;

import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
//#if polish.usePolishGui
//#= import de.enough.polish.ui.Style;
//#endif
/**
 * @author nick
 *
 */
public class CardItem extends CustomItem {

	/**
	 * @param label
	 */
	public CardItem(String label) {
		super(label);
		// TODO Auto-generated constructor stub
	}
	
	//#if polish.usePolishGui
	//#= public CardItem( String label, Style style ) {
	//#=	super( label, style );
	//#= }
	//#endif

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CustomItem#getMinContentHeight()
	 */
	protected int getMinContentHeight() {
		// TODO Auto-generated method stub
		return 30;
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CustomItem#getMinContentWidth()
	 */
	protected int getMinContentWidth() {
		// TODO Auto-generated method stub
		return 30;
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CustomItem#getPrefContentHeight(int)
	 */
	protected int getPrefContentHeight(int arg0) {
		// TODO Auto-generated method stub
		return 30;
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CustomItem#getPrefContentWidth(int)
	 */
	protected int getPrefContentWidth(int arg0) {
		// TODO Auto-generated method stub
		return 30;
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CustomItem#paint(javax.microedition.lcdui.Graphics, int, int)
	 */
	protected void paint(Graphics arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	public void setLeftImage(Image img) {
		
	}

}
