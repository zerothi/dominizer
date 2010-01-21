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
import com.Cards;
import com.Dominion;
import com.DominionException;
import com.EditCardsFilteredList;
import com.GameCalendarForm;
import com.InputForm;
import com.PresetFilteredList;
import com.QuickRandomizeForm;
import com.SettingsRecordStorage;
import com.ShowCardsForm;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.ui.List;
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
	
	
	public static final int TAB_QUICK = 0;
	public static final int TAB_EDIT = 1;
	public static final int TAB_PRESET = 2;
	
	private static GameApp app = null;
	private Display display = null;
	private Alert alert = null;
	private TabbedPane tabbedPane = null;
	private int currentTab = 0;
	
	public QuickRandomizeForm qrF = null;
	public EditCardsFilteredList ecFL = null;
	public PresetFilteredList pFL = null;

	public GameApp() {
		super();
		app = this;
		//new SettingsRecordStorage().deleteRecordStore(Locale.get("rms.file.preset"));
		//#debug info
		System.out.println("initialisation done.");
	}
	
	public static GameApp instance() {
		if ( app == null )
			app = new GameApp();
		return app;
	}

	public void showRandomizedCards() {
		if ( !Dominion.I().getExpansionPlayingStates()[0] && Dominion.I().getExpansionPlayingStates()[1] && !Dominion.I().getExpansionPlayingStates()[2] && !Dominion.I().getExpansionPlayingStates()[3] )
			showAlert(Locale.get("alert.QuickSelectExpansions.OnlyPromoSelected"));
		else if ( !Dominion.I().getExpansionPlayingStates()[0] && !Dominion.I().getExpansionPlayingStates()[1] && !Dominion.I().getExpansionPlayingStates()[2] && !Dominion.I().getExpansionPlayingStates()[3] )
			showAlert(Locale.get("alert.QuickSelectExpansions.NoneSelected"));
		else {
			ShowCardsForm.instance().reRandomize();
			changeToScreen(ShowCardsForm.instance());
		}
	}

	public void showCurrentSelectedCards() {
		try {
			ShowCardsForm.instance().viewCards(Dominion.I().getCurrentlySelected());
			changeToScreen(ShowCardsForm.instance());
		} catch (DominionException exp) {
			//#debug info
			System.out.println(exp.toString());
		}
	}


	public void showBlackMarketDeck(int previousScreen) {
		//updateSelectedQuickRandom();
		BlackMarketForm bmForm = new BlackMarketForm(Locale.get("screen.BlackMarket.title"));
		bmForm.setBlackMarketDeck(Dominion.I().getBlackMarketDeck());
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
		this.tabbedPane = new TabbedPane(null);
		this.tabbedPane.addTabListener(this);
		this.tabbedPane.setTabbedFormListener(this);
		//Locale.get("tab.Quick.title")
		qrF = new QuickRandomizeForm(null, List.MULTIPLE);
		//#style tabIcon
		this.tabbedPane.addTab(qrF, null, Locale.get("app.name"));
		//Locale.get("tab.EditCards.title")
		ecFL = new EditCardsFilteredList(null, FilteredList.MULTIPLE);
		//#style tabIcon
		this.tabbedPane.addTab(ecFL, null, Locale.get("screen.EditCards.title"));
		//Locale.get("tab.Preset.title")
		pFL = new PresetFilteredList(null, FilteredList.IMPLICIT);
		//#style tabIcon
		this.tabbedPane.addTab(pFL, null, Locale.get("screen.PresetCards.title"));
		//Locale.get("tab.Calendar.title")
		///#style tabIcon
		//this.tabbedPane.addTab(new GameCalendarForm(null), null, Locale.get("screen.Calendar.title"));
		String tmp = null;
		tmp = new SettingsRecordStorage().readKey(Locale.get("rms.file.settings"), Locale.get("rms.lasttab"));
		if ( tmp != null )
			currentTab = Integer.parseInt(tmp);
		tabbedPane.setFocus(currentTab);
		changeToScreen(null);
	}
	
	public int getCurrentTab() {
		return currentTab;
	}
	
	protected void pauseApp() {
		DeviceControl.lightOff();
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {}

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
	
	/**
	 * @param string
	 */
	public void showInputDialog(String message, CommandListener cmdListener) {
		changeToScreen(InputForm.instance().instance(message, cmdListener));
	}

	public void quit() {
		SettingsRecordStorage srs = new SettingsRecordStorage();
		try {
			srs.writeExpansions(Dominion.I().getExpansionPlayingStates());
			srs.writeExpansionCards(Dominion.I().getNumberOfExpansionCards());
			//#debug info 
			System.out.println(Dominion.I().getAvailableAsSave());
			srs.writeData(Locale.get("rms.file.settings"), Locale.get("rms.available"), Dominion.I().getAvailableAsSave());
			//#debug info 
			System.out.println("current tab: " + currentTab);
			srs.writeData(Locale.get("rms.file.settings"), Locale.get("rms.lasttab"), "" + currentTab);
			//#debug info 
			System.out.println("preferred sort: " + Cards.COMPARE_PREFERED);
			srs.writeData(Locale.get("rms.file.settings"), Locale.get("rms.preferredsort"), "" + Cards.COMPARE_PREFERED);
		} catch (RecordStoreFullException e) {
		} catch (RecordStoreNotFoundException e) {
		} catch (RecordStoreException e) {
		} finally {
			srs = null;
		}
		notifyDestroyed();
	}

	public void tabChangeEvent(Screen scr) {}
	
	public void changeToTab(int tab) {
		this.tabbedPane.setCurrentTab(tab);
		this.tabbedPane.setFocus(tab);
		currentTab = tab;
	}

	public void notifyTabChangeCompleted(int from, int to) {
		if ( to == -1 )
			return;
		currentTab = to;
	}

	public boolean notifyTabChangeRequested(int from, int to) {
		if ( to == TAB_EDIT ) {
			ecFL.updateCards(false);
			//#debug info
			System.out.println("Updating cards");
		}
		return true;
	}
}
