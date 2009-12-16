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
import com.QuickRandomizeForm;
import com.SettingsRecordStorage;
import com.ShowCardsForm;

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
	Displayable currentForm = null;
	Display display = null;
	Alert alert = null;
	TabbedPane tabbedPane = null;
	
	public GameApp() {
		super();
		//#debug info
		System.out.println("initialisation done.");
	}

	public void showRandomizedCards() {
		if ( !Dominion.instance().getPlayingStates()[0] && Dominion.instance().getPlayingStates()[1] && !Dominion.instance().getPlayingStates()[2] && !Dominion.instance().getPlayingStates()[3] )
			this.showAlert(Locale.get("alert.QuickSelectExpansions.OnlyPromoSelected"));
		else if ( !Dominion.instance().getPlayingStates()[0] && !Dominion.instance().getPlayingStates()[1] && !Dominion.instance().getPlayingStates()[2] && !Dominion.instance().getPlayingStates()[3] )
			this.showAlert(Locale.get("alert.QuickSelectExpansions.NoneSelected"));
		else {
			ShowCardsForm scForm = new ShowCardsForm(this, Locale.get("screen.RandomizedCards.title"));
			scForm.reRandomize();
			this.changeToScreen(scForm);
		}
	}
	
	public void showBlackMarketDeck(Form previousForm) {
		//updateSelectedQuickRandom();
		BlackMarketForm bmForm = new BlackMarketForm(this, previousForm, Locale.get("screen.BlackMarket.title"));
		bmForm.setBlackMarketDeck(Dominion.instance().getBlackMarketDeck());
		this.changeToScreen(bmForm);
	}

	public void changeToScreen(Displayable displayable) {
		if ( alert != null )
			alert = null;
		if ( displayable == null )
			this.changeToScreen(this.tabbedPane);
		else
			this.display.setCurrent(displayable);
	}

	protected void startApp() throws MIDletStateChangeException {
		//#debug info
		System.out.println("setting display");
		//#style tabbedPane
		this.tabbedPane = new TabbedPane(null);//QuickRandomizeForm(this, Locale.get("app.name")), null, Locale.get("app.name")
		//#debug info
		System.out.println("setting display");
		this.tabbedPane.addTabListener(this);
   	 	this.tabbedPane.setTabbedFormListener(this);
   	 	//#debug info
		System.out.println("adding forms");
		//#style mainScreen
		Form tmp1 = new Form(null);
		tmp1.append("Hej1");
		//#style mainScreen
		Form tmp2 = new Form(null);
		tmp2.append("Hej2");
		//#debug info
		System.out.println("adding forms");
		//#style tabIcon
		this.tabbedPane.addTab(tmp1, null, "Hej");
		//#debug info
		System.out.println("adding forms2");
		//#style tabIcon
		this.tabbedPane.addTab(tmp2, null, "He2j");
		//#debug info
		System.out.println("setting focu");
		//tabbedPane.addTab(new ShowCardsForm(this, Locale.get("app.name")), null, Locale.get("app.name"));
		//this.currentForm = new QuickRandomizeForm(this, Locale.get("app.name"));
		this.display = Display.getDisplay(this);
		//#debug info
		System.out.println("setting display");
		this.display.setCurrent(this.tabbedPane);
		//this.changeToScreen(null);
	}

	protected void pauseApp() {
		DeviceControl.lightOff();
	}
	
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		// just quit
	}
	
	/**
	 * @param string
	 */
	public void showAlert(String message) {
		//#style messageAlert
		alert = new Alert(Locale.get("alert.alert"), message, null, AlertType.WARNING);
		alert.setTimeout(Alert.FOREVER);
		this.display.setCurrent(alert);
	}
	
	/**
	 * @param string
	 */
	public void showInfo(String message, int timeOut) {
		//#style messageAlert
		alert = new Alert(Locale.get("alert.info"), message, null, AlertType.INFO);
		alert.setTimeout(timeOut);
		this.display.setCurrent(alert);
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
		this.display.setCurrent(alert);
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

	public void notifyTabChangeCompleted(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public boolean notifyTabChangeRequested(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	

}
