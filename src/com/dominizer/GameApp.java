package com.dominizer;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.BlackMarketForm;
import com.Cards;
import com.Dominion;
import com.DominionException;
import com.EditCardsList;
import com.GaugeForm;
import com.InputForm;
import com.PresetFilteredList;
import com.QuickRandomizeForm;
import com.Rand;
import com.SettingsRecordStorage;
import com.ShowCardsForm;
import com.TestList;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.ui.List;
import de.enough.polish.ui.Screen;
import de.enough.polish.ui.TabListener;
import de.enough.polish.ui.TabbedFormListener;
import de.enough.polish.ui.TabbedPane;
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
	public static final int SHOWCARDS= 20;
	
	private static GameApp app = null;
	private Display display = null;
	private Alert alert = null;
	private TabbedPane tabbedPane = null;
	private int currentTab = 0;
	
	public QuickRandomizeForm qrF = null;
	public EditCardsList ecFL = null;
	public PresetFilteredList pFL = null;
	public BlackMarketForm bmF = null;

	public GameApp() {
		super();
		app = this;
		//SettingsRecordStorage.instance().deleteRecordStore(Locale.get("rms.file.preset"));
		//SettingsRecordStorage.instance().deleteRecordStore(Locale.get("rms.file.settings"));
		//#debug dominizer
		System.out.println("initialisation done.");
	}
	
	public static GameApp instance() {
		if ( app == null )
			app = new GameApp();
		return app;
	}

	public void showRandomizedCards() {
		currentTab = getCurrentTab();
		Dominion.I().resetIsPlaying(true);
		try {
			Dominion.I().randomizeCards();
			ShowCardsForm.instance().viewCards(Dominion.I().getCurrentlySelected());
			changeToScreen(ShowCardsForm.instance());
		} catch (DominionException e) {
			GameApp.instance().showAlert(e.toString());
		}
	}

	public void showCurrentSelectedCards() {
		try {
			ShowCardsForm.instance().viewCards(Dominion.I().getCurrentlySelected());
			changeToScreen(ShowCardsForm.instance());
		} catch (DominionException exp) {
			showAlert(exp.toString());
		}
	}

	public void showBlackMarketDeck(int previousScreen) {
		bmF.setBlackMarketDeck(Dominion.I().getBlackMarketDeck());
		changeToScreen(bmF);
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
		if ( currentTab == SHOWCARDS )
			changeToScreen(ShowCardsForm.instance());
		else
			changeToScreen(tabbedPane);
	}

	protected void startApp() throws MIDletStateChangeException {
		display = Display.getDisplay(this);
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading"));
		display.setCurrent(GaugeForm.instance());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.cards"));
		Dominion.I().getExpansions();
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.gui"));
		//#style tabbedPane
		tabbedPane = new TabbedPane(null);
		tabbedPane.addTabListener(this);
		tabbedPane.setTabbedFormListener(this);
		//Locale.get("tab.Quick.title")
		qrF = new QuickRandomizeForm(null, List.MULTIPLE);
		//#style tabIcon
		tabbedPane.addTab(qrF, null, Locale.get("app.name"));
		//Locale.get("tab.EditCards.title")
		ecFL = new EditCardsList(null, FilteredList.MULTIPLE);
		//#style tabIcon
		tabbedPane.addTab(ecFL, null, Locale.get("tab.EditCards.title"));
		//Locale.get("tab.Preset.title")
		pFL = new PresetFilteredList(null, FilteredList.IMPLICIT);
		//#style tabIcon
		tabbedPane.addTab(pFL, null, Locale.get("tab.Preset.title"));
		//Locale.get("tab.Calendar.title")
		///#style tabIcon
		//tabbedPane.addTab(new GameCalendarForm(null), null, Locale.get("screen.Calendar.title"));
		bmF = new BlackMarketForm(Locale.get("screen.BlackMarket.title"), List.IMPLICIT);
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.gui.settings"));
		SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.settings"));
		if ( SettingsRecordStorage.instance().readKey(Locale.get("rms.lasttab")) != null )
			currentTab = Integer.parseInt(SettingsRecordStorage.instance().readKey(Locale.get("rms.lasttab")));
		if ( currentTab != SHOWCARDS & currentTab > -1 )
			tabbedPane.setFocus(currentTab);
		//#debug dominizer
		System.out.println("setting display");
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.finished"));
		display.setCurrent(tabbedPane);
		//display.setCurrent(new TestList("hej", List.MULTIPLE));
		GaugeForm.instance(false);
	}
	
	public int getCurrentTab() {
		return currentTab;
	}
	
	protected void pauseApp() {
		//DeviceControl.lightOff();
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		quit();
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
	
	/**
	 * @param string
	 */
	public void showInputDialog(String message, CommandListener cmdListener) {
		changeToScreen(InputForm.instance().instance(message, cmdListener));
	}

	public void quit() {
		try {
			if ( SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.settings")) ) {
				SettingsRecordStorage.instance().addData(Locale.get("rms.expansions"), Dominion.I().getExpansionPlayingStatesAsSave());
				SettingsRecordStorage.instance().addData(Locale.get("rms.expansions.usedcards"), Dominion.I().getCardsUsedForExpansionAsSave());
				SettingsRecordStorage.instance().addData(Locale.get("rms.available"), Dominion.I().getAvailableAsSave());
				SettingsRecordStorage.instance().addData(Locale.get("rms.percentage"), Dominion.I().getPercentagesAsSave());
				SettingsRecordStorage.instance().addData(Locale.get("rms.lasttab"), "" + currentTab);
				SettingsRecordStorage.instance().addData(Locale.get("rms.preferredsort"), "" + Cards.COMPARE_PREFERRED);
				SettingsRecordStorage.instance().writeData();
				SettingsRecordStorage.instance().closeRecord();
			}
		} catch (RecordStoreFullException e) {
		} catch (RecordStoreNotFoundException e) {
		} catch (RecordStoreException e) {
		}
		notifyDestroyed();
	}

	public void tabChangeEvent(Screen scr) {}
	
	public void changeToTab(int tab) {
		tabbedPane.setFocus(tab);
		currentTab = tab;
	}

	public void notifyTabChangeCompleted(int from, int to) {
		if ( to == SHOWCARDS )
			return;
		currentTab = to;
	}

	public boolean notifyTabChangeRequested(int from, int to) {
		switch ( from ) {
		case TAB_QUICK:
			qrF.getSelectedFlags(qrF.flags);
			Dominion.I().setExpansionPlayingState(qrF.flags);
			//#debug dominizer
			System.out.println("updating flags");
			break;
		case TAB_EDIT:
			ecFL.updateCards(true);
			//#debug dominizer
			System.out.println("updating cards internal");
			break;
		}
		switch ( to ) {
		case TAB_EDIT:
			ecFL.updateCards(false);
			//#debug dominizer
			System.out.println("updating cards external");
			break;
		}
		return true;
	}
}
