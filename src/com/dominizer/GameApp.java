package com.dominizer;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.BlackMarketForm;
import com.Dominion;
import com.DominionException;
import com.EditCardsForm;
import com.GameCalendar;
import com.PresetFilteredList;
import com.QuickRandomizeForm;
import com.SettingsRecordStorage;
import com.ShowCardsForm;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.ui.Screen;
import de.enough.polish.ui.TabListener;
import de.enough.polish.ui.TabbedFormListener;
import de.enough.polish.ui.TabbedPane;
import de.enough.polish.util.DeviceControl;
import de.enough.polish.util.Locale;
/**
 * <p>The main app for Dominizer</p>
 *
 * <p>Copyright Nick Papior Andersen Software</p>

 * <pre>
 * history
 *        21-November-2009 - Nick - creation
 * </pre>
 * @author Nick Papior Andersen, nickpapior@gmail.com
 */
public class GameApp extends MIDlet implements TabListener, TabbedFormListener {
	private static GameApp app = null;
	private Display display = null;
	private Alert alert = null;
	private TabbedPane tabbedPane = null;
	private int currentTab = 0;

	public GameApp() {
		super();
		app = this;
		//#debug info
		System.out.println("initialisation done.");
	}
	
	public static GameApp instance() {
		if ( app == null )
			app = new GameApp();
		return app;
	}

	public void showRandomizedCards() {
		if ( !Dominion.instance().getPlayingStates()[0] && Dominion.instance().getPlayingStates()[1] && !Dominion.instance().getPlayingStates()[2] && !Dominion.instance().getPlayingStates()[3] )
			showAlert(Locale.get("alert.QuickSelectExpansions.OnlyPromoSelected"));
		else if ( !Dominion.instance().getPlayingStates()[0] && !Dominion.instance().getPlayingStates()[1] && !Dominion.instance().getPlayingStates()[2] && !Dominion.instance().getPlayingStates()[3] )
			showAlert(Locale.get("alert.QuickSelectExpansions.NoneSelected"));
		else {
			ShowCardsForm.instance().reRandomize();
			changeToScreen(ShowCardsForm.instance());
		}
	}

	public void showCurrentSelectedCards() {
		try {
			ShowCardsForm.instance().viewCards(Dominion.instance().getCurrentlySelected());		
		} catch (DominionException exp) {
			//#debug info
			System.out.println(exp.toString());
		}
	}


	public void showBlackMarketDeck(int previousScreen) {
		//updateSelectedQuickRandom();
		BlackMarketForm bmForm = new BlackMarketForm(Locale.get("screen.BlackMarket.title"));
		bmForm.setBlackMarketDeck(Dominion.instance().getBlackMarketDeck());
		changeToScreen(bmForm);
		currentTab = previousScreen;
	}

	public void changeToScreen(Displayable displayable) {
		if ( alert != null )
			alert = null;
		if ( displayable == null )
			changeToScreen(tabbedPane);
		else
			display.setCurrent(displayable);
	}

	public void returnToPreviousScreen() {
		if ( currentTab == -1 )
			changeToScreen(ShowCardsForm.instance());
		else
			changeToScreen(tabbedPane);
		//notifyTabChangeRequested(0, currentTab);
	}

	protected void startApp() throws MIDletStateChangeException {
		//#debug info
		System.out.println("setting display");
		display = Display.getDisplay(this);
		//#style tabbedPane
		tabbedPane = new TabbedPane(null);
		tabbedPane.addTabListener(this);
		tabbedPane.setTabbedFormListener(this);
		//#style tabIcon
		tabbedPane.addTab(new QuickRandomizeForm(Locale.get("tab.Quick.title")), null, Locale.get("app.name"));
		//#style tabIcon
		tabbedPane.addTab(new PresetFilteredList(Locale.get("tab.Preset.title"), FilteredList.IMPLICIT), null, Locale.get("screen.PresetCards.title"));
		//#style tabIcon
		tabbedPane.addTab(new EditCardsForm(Locale.get("tab.EditCards.title")), null, Locale.get("screen.EditSingleCards.title"));
		//#style tabIcon
		tabbedPane.addTab(new GameCalendar(Locale.get("tab.Calendar.title")), null, Locale.get("screen.Calendar.title"));
		changeToScreen(tabbedPane);
	}

	protected void pauseApp() {
		DeviceControl.lightOff();
	}

	protected void destroyApp(boolean sunconditional) throws MIDletStateChangeException {
		// just quit
	}

	/**
	 * @param string
	 */
	public void showAlert(String message) {
		//#style messageAlert
		alert = new Alert(Locale.get("alert.alert"), message, null, AlertType.WARNING);
		alert.setTimeout(Alert.FOREVER);
		display.setCurrent(alert);
	}

	/**
	 * @param string
	 */
	public void showInfo(String message, int timeOut) {
		//#style messageAlert
		alert = new Alert(Locale.get("alert.info"), message, null, AlertType.INFO);
		alert.setTimeout(timeOut);
		display.setCurrent(alert);
	}

	/**
	 * @param string
	 */
	public void showConfirmation(String message, CommandListener cmdListener) {
		//#style messageAlert
		alert = new Alert(Locale.get("alert.confirmation"), message, null, AlertType.CONFIRMATION);
		alert.addCommand(new Command(Locale.get("polish.command.ok"), Command.OK, 1));
		alert.addCommand(new Command(Locale.get("polish.command.cancel"), Command.CANCEL, 1));
		alert.setCommandListener(cmdListener);
		display.setCurrent(alert);
	}

	public void quit() {
		try {
			SettingsRecordStorage srs = new SettingsRecordStorage();
			srs.writeExpansions(Dominion.instance().getPlayingStates());
			srs.writeExpansionCards(Dominion.instance().getNumberOfExpansionCards());
			srs = null;
		} catch (RecordStoreFullException e) {
			// TODO Auto-generated catch block
		} catch (RecordStoreNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (RecordStoreException e) {
			// TODO Auto-generated catch block
		}
		notifyDestroyed();
	}

	public void tabChangeEvent(Screen arg0) {
		// TODO Auto-generated method stub		
	}

	public void notifyTabChangeCompleted(int from, int to) {
		currentTab = to;	
	}

	public boolean notifyTabChangeRequested(int from, int to) {
		return true;
	}
}
