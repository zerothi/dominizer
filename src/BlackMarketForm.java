import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;

/**
 * 
 */

/**
 * @author nick
 *
 */
public class BlackMarketForm extends Form implements CommandListener {

	GameApp app = null;
	Vector blackMarketDeck = null;
	
	/**
	 * @param title
	 */
	public BlackMarketForm(GameApp app, String title) {
		super(title);
		this.app = app;
	}
	
	public void setBlackMarketDeck(Vector cards) {
		blackMarketDeck = new Vector(cards.size() - 10);
		for ( int i = 0 ; i < cards.size() ; i++ ) {
			if ( !( (Cards)cards.elementAt(i) ).isSelected() )
				blackMarketDeck.addElement(((Cards)cards.elementAt(i)).getName());
		}
	}
	

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command arg0, Displayable arg1) {
		// TODO Auto-generated method stub

	}

}
