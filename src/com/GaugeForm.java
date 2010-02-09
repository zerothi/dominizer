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
	/**
	 * @param label
	 * @param interactive
	 * @param maxValue
	 * @param initialValue
	 */
	private GaugeForm(String label) {
		//#style formGauge
		super(label);
		//#style loadingGauge
		g = new Gauge(null, false, Gauge.INDEFINITE,Gauge.CONTINUOUS_RUNNING);
		this.append(g);
	}
	
	public static GaugeForm instance() {
		if ( domG == null )
			domG = new GaugeForm(null);
		return domG;
	}
	
	public Gauge getGauge() {
		return g;
	}
	
	public void setGauge(String label, boolean interactive, int maxValue, int initialValue) {
		this.deleteAll();
		if ( interactive ) {
			//#style inputGauge
			g = new Gauge(label, interactive, maxValue, initialValue);
			this.addCommand(okCmd);
			this.addCommand(cancelCmd);
		} else {
			//#style loadingGauge
			g = new Gauge(label, interactive, maxValue, initialValue);
		}
		this.append(g);
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
		this.setCommandListener(cmdListener);
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
			this.setGaugeValue(keyCode - Canvas.KEY_NUM0);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}
}
