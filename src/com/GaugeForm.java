/**
 * 
 */
package com;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;

import de.enough.polish.ui.Command;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class GaugeForm extends Form {
	private static GaugeForm domG = null;
	private Gauge g = null;
	private Command okCmd = new Command(Locale.get("polish.command.ok"), Command.OK, 0);
	private Command cancelCmd = new Command(Locale.get("polish.command.cancel"), Command.BACK, 1);	

	private GaugeForm() {
		//#style loadForm
		super(null);
		//#style loadingGauge
		g = new Gauge(null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);
		append(g);
	}
	
	private GaugeForm(boolean is) {
		//#style inputGaugeForm
		super(null);
		//#style loadingGauge
		g = new Gauge(null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);
		append(g);
	}
	
	public static GaugeForm instance(boolean isLoadForm) {
		if ( domG != null ) {
			domG.deleteAll();
			domG = null;
		}
		if ( isLoadForm ) {
			domG = new GaugeForm();
		} else {
			domG = new GaugeForm(true);
		}
		return domG;
	}
	
	public static GaugeForm instance() {
		if ( domG != null )
			return domG;
		return instance(true);
	}
	
	public Gauge getGauge() {
		return g;
	}
	
	public void setGauge(String label, boolean interactive, int maxValue, int initialValue) {
		deleteAll();
		if ( interactive ) {
			//#style inputGauge
			g = new Gauge(label, interactive, maxValue, initialValue);
			addCommand(okCmd);
			addCommand(cancelCmd);
		} else {
			//#style loadingGauge
			g = new Gauge(label, interactive, maxValue, initialValue);
		}
		append(g);
	}
	
	public void setGaugeLabel(String label) {
		g.setLabel(label);
	}
	
	public void setGaugeValue(int value) {
		g.setValue(value);
	}
	
	public int getGaugeValue() {
		return g.getValue();
	}
	
	public void addCommandListener(CommandListener cmdListener) {
		setCommandListener(cmdListener);
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_NUM0:
		case Canvas.KEY_NUM1:
		case Canvas.KEY_NUM2:
		case Canvas.KEY_NUM3:
		case Canvas.KEY_NUM4:
		case Canvas.KEY_NUM5:
		case Canvas.KEY_NUM6:
		case Canvas.KEY_NUM7:
		case Canvas.KEY_NUM8:
		case Canvas.KEY_NUM9:
			setGaugeValue((keyCode - Canvas.KEY_NUM0));
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}
}
