package com;



import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.dominizer.GameApp;

import de.enough.polish.ui.Alert;
import de.enough.polish.ui.List;
import de.enough.polish.ui.Screen;
import de.enough.polish.ui.TabListener;
import de.enough.polish.ui.TabbedFormListener;
import de.enough.polish.ui.TabbedPane;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.Locale;

public class ShowCardsForm extends TabbedPane implements TabListener, TabbedFormListener {

	private static ShowCardsForm scF = null;
	
	private CardsList[] cardSet = null;
	private int currentSet = 0;
	
	private ShowCardsForm(String title) {
		//#style tabbedPane
		super(title);
		//#debug dominizer
		System.out.println("showing cards initialize");
		
		addTabListener(this);
		setTabbedFormListener(this);
		cardSet = new CardsList[Dominion.TOTAL_CARDS / 10];
		
		for ( int i = 0 ; i < cardSet.length ; i++ )
			cardSet[i] = new CardsList(null, List.MULTIPLE, i+1);
	}
	
	public static ShowCardsForm instance() {
		if ( scF == null ) {
			scF = new ShowCardsForm(null);//Locale.get("screen.RandomizedCards.title")
		}
		return scF;
	}
	
	public int getCurrentSet() {
		return currentSet;
	}
	
	public void randomizeNewSet() throws DominionException {
		if ( currentSet < cardSet.length ) {
			currentSet++;
			String tmp = "" + currentSet;
			cardSet[currentSet-1].reRandomize();
			//#style tabIconSet
			addTab(cardSet[currentSet-1], null, Locale.get("screen.RandomizedCards.title2", tmp));
			setFocus(currentSet - 1);
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
}	
