package com;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;

import de.enough.polish.ui.ChartItem;
import de.enough.polish.util.Locale;

public class TiltedPieChartForm extends Form {
	private ChartItem[] chart = null;
	public TiltedPieChartForm(String title, int numberOfCharts) {
		//#style mainPopupScreen
		super(title);
		addCommand(new Command(Locale.get("polish.command.ok"), Command.OK, 1));
		chart = new ChartItem[numberOfCharts];
	}
	//Locale.get("chart.types")
	public void setChart(int chartNumber, String chartTitle, int[][][] info) {
		//#style pieChart
		chart[chartNumber] = new ChartItem(chartTitle, info[0], info[1][0]);
		append(chart[chartNumber]);
		//#= repaint();
		
	}
	
	
	

}
