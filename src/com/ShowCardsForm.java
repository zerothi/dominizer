package com;



import com.dominizer.GameApp;
import com.util.Cards;
import com.util.Dominion;
import com.util.DominionException;

import de.enough.polish.ui.List;
import de.enough.polish.ui.Screen;
import de.enough.polish.ui.TabListener;
import de.enough.polish.ui.TabbedFormListener;
import de.enough.polish.ui.TabbedPane;
import de.enough.polish.util.Locale;
//#= import de.enough.polish.util.Locale;

public class ShowCardsForm extends TabbedPane implements TabListener, TabbedFormListener {

	private static ShowCardsForm scF = null;
	
	private CardsList[] cardSet = null;
	
	private ShowCardsForm(String title) {
		//#style tabbedPane
		super(title);
		//#debug dominizer
		System.out.println("showing cards initialize");
		addTabListener(this);
		setTabbedFormListener(this);
		cardSet = new CardsList[Dominion.MAX_SETS];
		for ( int i = 0 ; i < cardSet.length ; i++ ) {
			//#style mainScreenSet
			cardSet[i] = new CardsList("#" + ( i + 1 ) , List.MULTIPLE, i + 1);
			if ( Dominion.I().isSetPlaying(i + 1) ) {
				try {
					cardSet[i].setCards(Dominion.I().getSelectedCards(i + 1));
					//#= String tmp = "" + (i + 1);
					//#style tabIconSet
					//#= addTab(cardSet[i], null, Locale.get("screen.RandomizedCards.title.tab", tmp));
				} catch ( DominionException e) {
					// Do nothing as there has been an error in reading. Highly unlikely
				}
			}
		}
	}
	
	public static ShowCardsForm instance() {
		if ( scF == null ) {
			scF = new ShowCardsForm(null);//Locale.get("screen.RandomizedCards.title")
		}
		return scF;
	}

	
	public void addNewCards(Cards cards) throws DominionException {
		if ( cards == null )
			return;
		//#debug dominizer
		System.out.println("adding new cards " + Dominion.I().getCurrentSet() + " getCount=" + getCount() + " s" +cards.isPlaying(1));
		if ( Dominion.I().getCurrentSet() > getCount() && Dominion.I().getCurrentSet() <= cardSet.length ) {
			cardSet[Dominion.I().getCurrentSet() - 1].setCards(cards);
			//#= String tmp = "" + Dominion.I().getCurrentSet();
			//#style tabIconSet
			//#= addTab(cardSet[Dominion.I().getCurrentSet()-1], null, Locale.get("screen.RandomizedCards.title.tab", tmp));
			setFocus(Dominion.I().getCurrentSet() - 1);
			updateBlackMarket();
		} else {
			throw new DominionException(Locale.get("alert.NoMoreSets"));
		}
	}
	
	public void updateBlackMarket() {
		for ( int i = 0 ; i < cardSet.length ; i++ )
			cardSet[i].setBlackMarket(Dominion.I().isBlackMarketPlaying());		
	}


	/* (non-Javadoc)
     * @see de.enough.polish.ui.TabListener#tabChangeEvent(de.enough.polish.ui.Screen)
     */
    public void tabChangeEvent(Screen tab) {}

	/* (non-Javadoc)
     * @see de.enough.polish.ui.TabbedFormListener#notifyTabChangeCompleted(int, int)
     */
    public void notifyTabChangeCompleted(int oldTabIndex, int newTabIndex) {
    	if ( -1 < oldTabIndex & oldTabIndex < cardSet.length) {
    		cardSet[oldTabIndex].updateCards(true);
    	}
    	if ( -1 < newTabIndex & newTabIndex < cardSet.length) {
    		cardSet[newTabIndex].updateCards(true);
    	}
    }

	/* (non-Javadoc)
     * @see de.enough.polish.ui.TabbedFormListener#notifyTabChangeRequested(int, int)
     */
    public boolean notifyTabChangeRequested(int oldTabIndex, int newTabIndex) {
    	//#debug dominizer
    	System.out.println("we have a change from " + oldTabIndex + " to " + newTabIndex);
	    return true;
    }
    
    public void updateTabs() {
    	int i = 0;
    	for ( i = this.size() - 1 ; i > -1 ; i-- )
    		removeTab(i);
    	for ( i = 1 ; i <= cardSet.length ; i++ ) {
    		if ( Dominion.I().isSetPlaying(i) ) {
    			try {
    				//#debug dominizer
    	    		System.out.println("adding tab for set " + i);
	    			cardSet[i - 1].setCards(Dominion.I().getSelectedCards(i));
	    			String tmp = "" + i;
	    			//#style tabIconSet
	    			addTab(cardSet[i - 1], null, Locale.get("screen.RandomizedCards.title.tab", tmp));
    			} catch ( DominionException e ) {
    				GameApp.instance().showAlert(Locale.get("alert.adding.cards"));
    			}
    		} else {
    			//#debug dominizer
	    		System.out.println("resetting tab with no playing " + i);
    			cardSet[i - 1].setCards(null);
    		}
    	}
    	updateBlackMarket();
    	if ( this.size() == 0 ) {
    		//#debug dominizer
    		System.out.println("form is empty");
    		GameApp.instance().returnToPreviousScreen();
    	} else {
    		//#debug dominizer
    		System.out.println("form has card lists");
    		setFocus(0);
    		repaint();
    	}
    }
    /*
    public void keyReleased(int keyCode) {
    	//#ifdef polish.hasPointerEvents
		if ( !hasPointerEvents() )
		{
		//#endif
		int gameAction = 0;
		try {
			gameAction = getGameAction(keyCode);
		} catch (Exception e) {
			// ignore
		}
    	if (gameAction == RIGHT && keyCode != KEY_NUM6 && getCurrentIndex() == size() - 1 ) {
			setFocus(0);
			return;
		} else if (gameAction == LEFT && keyCode != KEY_NUM4 && getCurrentIndex() == 0 ) {
			setFocus( size() - 1 );
			return;
		}
    	//#ifdef polish.hasPointerEvents
		}
		//#endif
		super.keyReleased(keyCode);
    }
    */
}	
