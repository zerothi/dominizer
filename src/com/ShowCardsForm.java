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
			cardSet[i] = new CardsList(null, List.MULTIPLE, i + 1);
			if ( Dominion.I().isSetPlaying(i + 1) ) {
				try {
					cardSet[i].setCards(Dominion.I().getCurrentlySelected(i + 1));
					Dominion.CURRENT_SET = i + 1;
					//#= String tmp = "" + Dominion.CURRENT_SET;
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
		if ( Dominion.CURRENT_SET > getCount() && Dominion.CURRENT_SET <= cardSet.length ) {
			cardSet[Dominion.CURRENT_SET - 1].setCards(cards);
			//#= String tmp = "" + Dominion.CURRENT_SET;
			//#style tabIconSet
			//#= addTab(cardSet[Dominion.CURRENT_SET-1], null, Locale.get("screen.RandomizedCards.title.tab", tmp));
			cardSet[Dominion.CURRENT_SET - 1].setBlackMarket(Dominion.I().isBlackMarketPlaying());
			setFocus(Dominion.CURRENT_SET - 1);
			for ( int i = 0 ; i < cardSet.length ; i++ )
				cardSet[i].setBlackMarket(Dominion.I().isBlackMarketPlaying());
		} else {
			throw new DominionException(Locale.get("alert.NoMoreSets"));
		}
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
	    // TODO Auto-generated method stub
	    return true;
    }
    
    public void updateTabs() {
    	int i = 0;
    	for ( i = this.size() - 1 ; i > -1 ; i-- )
    		removeTab(i);
    	Dominion.CURRENT_SET = 0;
    	for ( i = 1 ; i <= cardSet.length ; i++ ) {
    		if ( Dominion.I().isSetPlaying(i) ) {
    			try {
    				//#debug dominizer
    	    		System.out.println("adding tab for set " + i);
	    			cardSet[i - 1].setCards(Dominion.I().getCurrentlySelected(i));
	    			String tmp = "" + i;
	    			//#style tabIconSet
	    			addTab(cardSet[i - 1], null, Locale.get("screen.RandomizedCards.title.tab", tmp));
	    			cardSet[i - 1].setBlackMarket(Dominion.I().isBlackMarketPlaying());
	    			Dominion.CURRENT_SET = i;
    			} catch ( DominionException e ) {
    				GameApp.instance().showAlert(Locale.get("alert.adding.cards"));
    			}
    		} else {
    			//#debug dominizer
	    		System.out.println("resetting tab with no playing " + i);
    			cardSet[i - 1].setCards(null);
    		}
    	}
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
}	
