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
import com.CardsList;
import com.ConditionList;
import com.EditCardsList;
import com.GaugeForm;
import com.PresetList;
import com.QuickRandomizeList;
import com.TiltedPieChartForm;
import com.util.Cards;
import com.util.Dominion;
import com.util.SettingsRecordStorage;
//#if dominizer.calendar
//#= import tests.GameCalendarForm;
//#endif

import de.enough.polish.ui.List;
import de.enough.polish.ui.Screen;
import de.enough.polish.ui.TabListener;
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
public class GameApp extends MIDlet
//#if !polish.classes.ApplicationInitializer:defined
//	, ApplicationInitializer
//#endif
	{
	
	public static final int TAB_QUICK = 1;
	public static final int TAB_EDIT = 0;
	public static final int TAB_PRESET = 2;
	public static final int TAB_CONDITION = 3;
	public static final int SHOWCARDS= 20;
	
	private static GameApp app = null;
	private Display display = null;
	private Alert alert = null;
	private TabbedPane tabbedPane = null;
	
	private int currentTab = TAB_QUICK;
	
	public QuickRandomizeList qrF = null;
	public EditCardsList ecFL = null;
	public PresetList pFL = null;
	public ConditionList cF = null;
	public BlackMarketForm bmF = null;
	

	public GameApp() {
		super();
		app = this;
		//SettingsRecordStorage.instance().deleteRecordStore("presets");
		//SettingsRecordStorage.instance().deleteRecordStore("settings");
		//SettingsRecordStorage.instance().deleteRecordStore("condition");
		//#debug dominizer
		System.out.println("initialisation done.");
	}
	
	public static GameApp instance() {
		if ( app == null )
			app = new GameApp();
		return app;
	}

	public void showBlackMarketDeck(int previousScreen) {
		bmF.setBlackMarketDeck(Dominion.I().getBlackMarketDeck());
		changeToScreen(bmF);
		currentTab = previousScreen;
	}

	public void changeToScreen(Displayable displayable) {
		currentTab = getCurrentTab();
		if ( displayable == null )
			changeToScreen(tabbedPane);
		else {
			display.setCurrent(displayable);
		}
			
	}

	public void returnToPreviousScreen() {
		if ( currentTab == SHOWCARDS )
			changeToScreen(CardsList.instance());
		else
			changeToScreen(tabbedPane);
	}
	
	/*
	protected void startApp() throws MIDletStateChangeException {
		try {
			display = Display.getDisplay(this);
			Image splashImage = Image.createImage("/splash.png");
			InitializerSplashScreen splashScreen = new InitializerSplashScreen(display, splashImage, 0, null, 0, this);
			display.setCurrent(splashScreen);
		} catch (IOException e) {
			//#debug error
			System.out.println("Unable to load splash image." + e);
		}
	}*/

	protected void startApp() throws MIDletStateChangeException {
		display = Display.getDisplay(this);
		display.setCurrent(GaugeForm.instance(true));
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.cards"));
		Dominion.I().getExpansions();
		//ShowCardsForm.instance();
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.gui"));
		//#style tabbedPane
		tabbedPane = new TabbedPane(null);
		tabbedPane.addTabListener(new TabListener() {
			//#if polish.android
			//#= @Override
			//#endif
			public void tabChangeEvent(Screen tab) {
				//#debug dominizer
				System.out.println("We are in the tabChangeEvent: " + tab.getClass().getName());
				if ( tab instanceof EditCardsList ) {
					ecFL.loadCards();
					currentTab = TAB_EDIT;
				} else if ( tab instanceof QuickRandomizeList )
					currentTab = TAB_QUICK;
				else if ( tab instanceof PresetList )
					currentTab = TAB_PRESET;
				else if ( tab instanceof ConditionList)
					currentTab = TAB_CONDITION;
				//#debug dominizer
				System.out.println("We are done with the tabChangeEvent: " + tab.getClass().getName());
			}
		});
		ecFL = new EditCardsList(null, List.MULTIPLE);
		//#style tabIcon
		//#= tabbedPane.addTab(ecFL, null, Locale.get("tab.EditCards.title"));
		qrF = new QuickRandomizeList(null, List.MULTIPLE);
		//#style tabIcon
		//#= tabbedPane.addTab(qrF, null, Locale.get("app.name"));
		pFL = new PresetList(null, List.IMPLICIT);
		//#style tabIcon
		//#= tabbedPane.addTab(pFL, null, Locale.get("tab.Preset.title"));
		cF = new ConditionList(null, List.EXCLUSIVE);
		//#style tabIcon
		//#= tabbedPane.addTab(cF, null, Locale.get("tab.Condition.title"));
		// # style tabIcon
		//#if dominizer.calendar
		//#= tabbedPane.addTab(new GameCalendarForm(null), null, Locale.get("screen.Calendar.title"));
		//#endif
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.gui.settings"));
		SettingsRecordStorage.instance().changeToRecordStore("settings");
		if ( SettingsRecordStorage.instance().readKey("lasttab") != null )
			currentTab = Integer.parseInt(SettingsRecordStorage.instance().readKey("lasttab"));
		if ( currentTab != SHOWCARDS && currentTab > -1 )
			changeToTab(currentTab);
		//#debug dominizer
		System.out.println("setting display");
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.finished"));
		display.setCurrent(tabbedPane);
		GaugeForm.instance(false);
		bmF = new BlackMarketForm(Locale.get("screen.BlackMarket.title"));
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
		alert = null;
		//#style messageAlert
		alert = new Alert(Locale.get("alert.alert"), message, null, AlertType.WARNING);
		alert.setTimeout(Alert.FOREVER);
		changeToScreen(alert);
	}

	/**
	 * @param string
	 */
	public void showInfo(String message, int timeOut) {
		alert = null;
		//#style messageAlert
		alert = new Alert(Locale.get("alert.info"), message, null, AlertType.INFO);
		alert.setTimeout(timeOut);
		changeToScreen(alert);
	}
	
	public void showCardInfo(int[][][] types, int[][][] adds) {
		TiltedPieChartForm fm = new TiltedPieChartForm(Locale.get("chart.title"), 2);
		fm.setChart(0, Locale.get("chart.types"), types);
		fm.setChart(1, Locale.get("chart.attributes"), adds);
		changeToScreen(fm);
	}

	/**
	 * @param string
	 */
	public void showConfirmation(String message, CommandListener cmdListener) {
		alert = null;
		//#style messageAlert
		alert = new Alert(Locale.get("alert.confirmation"), message, null, AlertType.CONFIRMATION);
		alert.addCommand(new Command(Locale.get("polish.command.ok"), Command.OK, 1));
		alert.addCommand(new Command(Locale.get("polish.command.cancel"), Command.CANCEL, 1));
		alert.setCommandListener(cmdListener);
		changeToScreen(alert);
	}
	
	public void quit() {
		int i;
		try {
			if ( SettingsRecordStorage.instance().changeToRecordStore("settings") ) {
				SettingsRecordStorage.instance().addData("expansions", Dominion.I().getExpansionPlayingStatesAsSave());
				SettingsRecordStorage.instance().addData("expcards", Dominion.I().getCardsUsedForExpansionAsSave());
				SettingsRecordStorage.instance().addData("available", Dominion.I().getAvailableAsSave());
				SettingsRecordStorage.instance().addData("percentage", Dominion.I().getPercentagesAsSave());
				SettingsRecordStorage.instance().addData("lasttab", "" + currentTab);
				SettingsRecordStorage.instance().addData("sort", "" + Cards.COMPARE_PREFERRED);
				i = 0;
				//#debug dominizer
				System.out.println("WRITING OLD SETS");
				do {
					i++;
					SettingsRecordStorage.instance().deleteData("" + i);
					// the data will not be added if the data is null. So no need to test!
					SettingsRecordStorage.instance().addData("" + i, Dominion.I().getPlayingSetAsSave(i));
				} while ( i < 50 );
				//#debug dominizer
				System.out.println("DONE WRITING OLD SETS");
				SettingsRecordStorage.instance().writeData();
				SettingsRecordStorage.instance().closeRecord();
			}
		} catch (RecordStoreFullException e) {
		} catch (RecordStoreNotFoundException e) {
		} catch (RecordStoreException e) {
		}
		try {
			//SettingsRecordStorage.instance().deleteRecordStore(Locale.get("rms.file.condition"));
			if ( SettingsRecordStorage.instance().changeToRecordStore("condition") ) {
				Dominion.I();
				//#debug dominizer
				System.out.println("initial " + Dominion.condition.initialConditions + " preferred " + Dominion.condition.preferredCondition);
				//#debug dominizer
				System.out.println("reached condition writing");
				SettingsRecordStorage.instance().addData("preferred", "" + Dominion.condition.preferredCondition);
				int userCreated = 0;
				for ( i = Dominion.condition.initialConditions ; i < Dominion.condition.size() ; i++ ) {
					SettingsRecordStorage.instance().addData("" + userCreated, Dominion.condition.getCondition(i));
					SettingsRecordStorage.instance().addData("name" + userCreated, Dominion.condition.getNameAsSave(i));
					//#debug dominizer
					System.out.println("added condition: " + Dominion.condition.getNameAsSave(i) + " with condition: " + Dominion.condition.getCondition(i));
					userCreated++;
				}
				//#debug dominizer
				System.out.println("beginning to write conditions");
				SettingsRecordStorage.instance().writeData();
				SettingsRecordStorage.instance().closeRecord();
			}
		} catch (RecordStoreFullException e) {
		} catch (RecordStoreNotFoundException e) {
		} catch (RecordStoreException e) {
		}
		notifyDestroyed();
	}
		
	public void changeToTab(int tab) {
		if ( tab == TAB_EDIT )
			ecFL.loadCards();
		tabbedPane.setFocus(tab);
		currentTab = tab;
	}
}
