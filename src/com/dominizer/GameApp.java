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
import com.ConditionList;
import com.EditCardsList;
import com.GaugeForm;
import com.PresetList;
import com.QuickRandomizeList;
import com.ShowCardsForm;
import com.TiltedPieChartForm;
import com.util.Cards;
import com.util.Dominion;
import com.util.DominionException;
import com.util.SettingsRecordStorage;

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
	public static final int TAB_CONDITION = 3;
	public static final int SHOWCARDS= 20;
	
	private static GameApp app = null;
	private Display display = null;
	private Alert alert = null;
	private TabbedPane tabbedPane = null;
	
	private int currentTab = 0;
	
	public QuickRandomizeList qrF = null;
	public EditCardsList ecFL = null;
	public PresetList pFL = null;
	public ConditionList cF = null;
	public BlackMarketForm bmF = null;
	

	public GameApp() {
		super();
		app = this;
		//SettingsRecordStorage.instance().deleteRecordStore(Locale.get("rms.file.preset"));
		//SettingsRecordStorage.instance().deleteRecordStore(Locale.get("rms.file.settings"));
		//SettingsRecordStorage.instance().deleteRecordStore(Locale.get("rms.file.condition"));
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
		GaugeForm.instance(true).setGaugeLabel(Locale.get("gauge.loading"));
		display.setCurrent(GaugeForm.instance());
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.cards"));
		Dominion.I().getExpansions();
		ShowCardsForm.instance();
		//int j = Dominion.CURRENT_SET;
		/*for ( int i = 1 ; i <= j ; i++ ) {
			try {
				//#debug dominizer
				System.out.println("adding card sets: " + i + "/" + j + " is null = " + ( Dominion.I().getCurrentlySelected(i) == null));
				Dominion.CURRENT_SET = i;
				ShowCardsForm.instance().addNewCards(Dominion.I().getCurrentlySelected(i));
			} catch (DominionException e) {
				//showAlert(e.toString());
			}	
		}*/
		GaugeForm.instance().setGaugeLabel(Locale.get("gauge.loading.gui"));
		//#style tabbedPane
		tabbedPane = new TabbedPane(null)
			/*{
			public void keyReleased(int keyCode) {
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
				} else {
					super.keyReleased(keyCode);
				}
		    }
		}*/
		;
		tabbedPane.addTabListener(this);
		tabbedPane.setTabbedFormListener(this);
		qrF = new QuickRandomizeList(null, List.MULTIPLE);
		//#style tabIcon
		tabbedPane.addTab(qrF, null, Locale.get("app.name"));
		ecFL = new EditCardsList(null, List.MULTIPLE);
		//#style tabIcon
		//#= tabbedPane.addTab(ecFL, null, Locale.get("tab.EditCards.title"));
		pFL = new PresetList(null, List.IMPLICIT);
		//#style tabIcon
		//#= tabbedPane.addTab(pFL, null, Locale.get("tab.Preset.title"));
		cF = new ConditionList(null, List.EXCLUSIVE);
		//#style tabIcon
		//#= tabbedPane.addTab(cF, null, Locale.get("tab.Condition.title"));
		// # style tabIcon
		//tabbedPane.addTab(new GameCalendarForm(null), null, Locale.get("screen.Calendar.title"));
		bmF = new BlackMarketForm(Locale.get("screen.BlackMarket.title"));
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
		//display.setCurrent(new CardsList("hej", List.MULTIPLE));
		//display.setCurrent(ConditionTableForm.instance());
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
	
	public void showCardInfo(int[][][] types, int[][][] adds) {
		TiltedPieChartForm fm = new TiltedPieChartForm(Locale.get("chart.title"), 2);
		fm.setChart(0, Locale.get("chart.types"), types);
		fm.setChart(1, Locale.get("chart.attributes"), adds);
		display.setCurrent(fm);
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
		changeToScreen(alert);
	}
	
	public void quit() {
		int i;
		try {
			if ( SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.settings")) ) {
				SettingsRecordStorage.instance().addData(Locale.get("rms.expansions"), Dominion.I().getExpansionPlayingStatesAsSave());
				SettingsRecordStorage.instance().addData(Locale.get("rms.expansions.usedcards"), Dominion.I().getCardsUsedForExpansionAsSave());
				SettingsRecordStorage.instance().addData(Locale.get("rms.available"), Dominion.I().getAvailableAsSave());
				SettingsRecordStorage.instance().addData(Locale.get("rms.percentage"), Dominion.I().getPercentagesAsSave());
				SettingsRecordStorage.instance().addData(Locale.get("rms.lasttab"), "" + currentTab);
				SettingsRecordStorage.instance().addData(Locale.get("rms.preferredsort"), "" + Cards.COMPARE_PREFERRED);
				int sets = 0;
				i = 0;
				//#debug dominizer
				System.out.println("WRITING OLD SETS");
				do {
					i++;
					SettingsRecordStorage.instance().deleteData("" + i);
					// the data will not be added if the data is null. So no need to test!
					SettingsRecordStorage.instance().addData("" + i, Dominion.I().getPlayingSetAsSave(i));
					if ( Dominion.I().getPlayingSetAsSave(i) != null )
						sets++;
				} while ( i < Dominion.MAX_SETS );
				//#debug dominizer
				System.out.println("DONE WRITING OLD SETS");
				// TODO when option allows for setting this variable move it up!
				SettingsRecordStorage.instance().addData(Locale.get("rms.randomized.saves"), "" + sets);
				SettingsRecordStorage.instance().writeData();
				SettingsRecordStorage.instance().closeRecord();
			}
		} catch (RecordStoreFullException e) {
		} catch (RecordStoreNotFoundException e) {
		} catch (RecordStoreException e) {
		}
		try {
			//SettingsRecordStorage.instance().deleteRecordStore(Locale.get("rms.file.condition"));
			if ( SettingsRecordStorage.instance().changeToRecordStore(Locale.get("rms.file.condition")) ) {
				//#debug dominizer
				System.out.println("initial " + Dominion.I().condition.getInitialConditions() + " preferred " + Dominion.I().condition.getPreferredCondition());
				//#debug dominizer
				System.out.println("reached condition writing");
				SettingsRecordStorage.instance().addData("preferred", "" + Dominion.I().condition.getPreferredCondition());
				//#debug dominizer
				System.out.println("added the preferred condition: " + Dominion.I().condition.getPreferredCondition());
				int userCreated = 0;
				for ( i = Dominion.I().condition.getInitialConditions() ; i < Dominion.I().condition.size() ; i++ ) {
					SettingsRecordStorage.instance().addData("" + userCreated, Dominion.I().condition.getCondition(i));
					SettingsRecordStorage.instance().addData("name" + userCreated, Dominion.I().condition.getNameAsSave(i));
					//#debug dominizer
					System.out.println("added condition: " + Dominion.I().condition.getNameAsSave(i) + " with condition: " + Dominion.I().condition.getCondition(i));
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
			//qrF.getSelectedFlags(qrF.flags);
			//Dominion.I().setExpansionPlayingState(qrF.flags);
			//#debug dominizer
			System.out.println("updating flags");
			break;
		case TAB_EDIT:
			//#debug dominizer
			System.out.println("updating cards internal");
			break;
		case TAB_CONDITION:
			//cF.updateCards(true, -1);
			//#debug dominizer
			System.out.println("updating conditions internal");
			break;
		}
		switch ( to ) {
		case TAB_EDIT:
			//ecFL.updateCards(-1);
			//#debug dominizer
			System.out.println("updating cards external");
			break;
		case TAB_CONDITION:
			//cF.updateCards(false, -1);
			//#debug dominizer
			System.out.println("updating conditions external");
			break;
		}
		return true;
	}
}
