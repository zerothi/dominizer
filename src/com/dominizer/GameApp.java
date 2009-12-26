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
import com.EditCardsForm;
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
	static Display display = null;
	static Alert alert = null;
	static TabbedPane tabbedPane = null;
	
	public GameApp() {
		super();
		//#debug info
		System.out.println("initialisation done.");
	}
	
	public static void showRandomizedCards() {
		if ( !Dominion.instance().getPlayingStates()[0] && Dominion.instance().getPlayingStates()[1] && !Dominion.instance().getPlayingStates()[2] && !Dominion.instance().getPlayingStates()[3] )
			showAlert(Locale.get("alert.QuickSelectExpansions.OnlyPromoSelected"));
		else if ( !Dominion.instance().getPlayingStates()[0] && !Dominion.instance().getPlayingStates()[1] && !Dominion.instance().getPlayingStates()[2] && !Dominion.instance().getPlayingStates()[3] )
			showAlert(Locale.get("alert.QuickSelectExpansions.NoneSelected"));
		else {
		    ShowCardsForm.instance().reRandomize();
		    changeToScreen(ShowCardsForm.instance());
		}
	}

	public static void showRandomizedCards() {
		if ( !Dominion.instance().getPlayingStates()[0] && Dominion.instance().getPlayingStates()[1] && !Dominion.instance().getPlayingStates()[2] && !Dominion.instance().getPlayingStates()[3] )
			showAlert(Locale.get("alert.QuickSelectExpansions.OnlyPromoSelected"));
		else if ( !Dominion.instance().getPlayingStates()[0] && !Dominion.instance().getPlayingStates()[1] && !Dominion.instance().getPlayingStates()[2] && !Dominion.instance().getPlayingStates()[3] )
			showAlert(Locale.get("alert.QuickSelectExpansions.NoneSelected"));
		else {
		    ShowCardsForm.instance().reRandomize();
		    changeToScreen(ShowCardsForm.instance());
		}
	}

    public static void showCurrentSelectedCards() {
	try {
	    Dominion.instance().getCurrentlySelected();
	} catch (DominionException exp) {
	    //#debug info
	    System.out.println(exp.toString());
	}
    }

	
	public static void showBlackMarketDeck(Form previousForm) {
		//updateSelectedQuickRandom();
		BlackMarketForm bmForm = new BlackMarketForm(previousForm, Locale.get("screen.BlackMarket.title"));
		bmForm.setBlackMarketDeck(Dominion.instance().getBlackMarketDeck());
		changeToScreen(bmForm);
	}

	public static void changeToScreen(Displayable displayable) {
		if ( alert != null )
			alert = null;
		if ( displayable == null )
			changeToScreen(tabbedPane);
		else
			GameApp.display.setCurrent(displayable);
	}

	protected void startApp() throws MIDletStateChangeException {
		//#debug info
		System.out.println("setting display");
		GameApp.display = Display.getDisplay(this);
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
	public static void showAlert(String message) {
		//#style messageAlert
		alert = new Alert(Locale.get("alert.alert"), message, null, AlertType.WARNING);
		alert.setTimeout(Alert.FOREVER);
		GameApp.display.setCurrent(alert);
	}
	
	/**
	 * @param string
	 */
	public static void showInfo(String message, int timeOut) {
		//#style messageAlert
		alert = new Alert(Locale.get("alert.info"), message, null, AlertType.INFO);
		alert.setTimeout(timeOut);
		GameApp.display.setCurrent(alert);
	}
	
	/**
	 * @param string
	 */
	public static void showConfirmation(String message, CommandListener cmdListener) {
		//#style messageAlert
		alert = new Alert(Locale.get("alert.confirmation"), message, null, AlertType.CONFIRMATION);
		alert.addCommand(new Command(Locale.get("polish.command.ok"), Command.OK, 1));
        alert.addCommand(new Command(Locale.get("polish.command.cancel"), Command.CANCEL, 1));
		alert.setCommandListener(cmdListener);
		GameApp.display.setCurrent(alert);
	}
	
	public static void quit() {
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
	}

	public void tabChangeEvent(Screen arg0) {
		// TODO Auto-generated method stub
		
	}

	public void notifyTabChangeCompleted(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public boolean notifyTabChangeRequested(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	

}
