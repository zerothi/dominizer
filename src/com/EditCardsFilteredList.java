package com;



import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.dominizer.GameApp;

import de.enough.polish.ui.FilteredList;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.Locale;

public class EditCardsFilteredList extends FilteredList implements CommandListener {
	
	private Command randomizeCmd = new Command(Locale.get("cmd.Randomize.Show"), Command.BACK, 0);
	// # if polish.android
	private Command percentage05Cmd = new Command( Locale.get("cmd.Percentage.Main", "0-50%"), Command.SCREEN, 2);
	private Command per0Cmd = new Command( Locale.get("cmd.Percentage.Remove"), Command.ITEM, 1);
	private Command per1Cmd = new Command( Locale.get("cmd.Percentage.Use", "10"), Command.ITEM, 2);
	private Command per2Cmd = new Command( Locale.get("cmd.Percentage.Use", "20"), Command.ITEM, 3);
	private Command per3Cmd = new Command( Locale.get("cmd.Percentage.Use", "30"), Command.ITEM, 4);
	private Command per4Cmd = new Command( Locale.get("cmd.Percentage.Use", "40"), Command.ITEM, 5);
	private Command per5Cmd = new Command( Locale.get("cmd.Percentage.Use", "50"), Command.ITEM, 6);
	private Command percentage610Cmd = new Command( Locale.get("cmd.Percentage.Main", "60-100%"), Command.SCREEN, 3);
	private Command per6Cmd = new Command( Locale.get("cmd.Percentage.Use", "60"), Command.ITEM, 1);
	private Command per7Cmd = new Command( Locale.get("cmd.Percentage.Use", "70"), Command.ITEM, 2);
	private Command per8Cmd = new Command( Locale.get("cmd.Percentage.Use", "80"), Command.ITEM, 3);
	private Command per9Cmd = new Command( Locale.get("cmd.Percentage.Use", "90"), Command.ITEM, 4);
	private Command per10Cmd = new Command( Locale.get("cmd.Percentage.Use", "100"), Command.ITEM, 5);
	// # else
	private Command perGaugeCmd = new Command( Locale.get("cmd.Percentage.Gauge"), Command.SCREEN, 1);
	// # endif
	private Command quitCmd = new Command( Locale.get("cmd.Quit"), Command.SCREEN, 10);
	private int tmp;
	
	public EditCardsFilteredList(String title, int listType) {
		//#style mainScreen
		super(title, listType);
		for ( int i = 0 ; i < Dominion.I().getExpansions() ; i++ ){
			for ( int cardNumber = 0 ; cardNumber < Dominion.I().getExpansion(i).size() ; cardNumber++ ) {
				try {
					//#debug info
					System.out.println("exp: " + Dominion.I().getExpansion(i).getExpansion(cardNumber));
					//#style label
					this.append(Dominion.I().getExpansion(i).getName(cardNumber), Image.createImage("/" + Dominion.I().getExpansion(i).getExpansion(cardNumber) + 
							Dominion.I().getExpansion(i).getCardType(cardNumber) + ".png"));
				} catch (IOException e) {
					//#style label
					this.append(Dominion.I().getExpansion(i).getName(cardNumber), null);
				}
				this.setSelectedIndex(this.size() - 1, Dominion.I().getExpansion(i).isAvailable(cardNumber));
				this.setPercentage(this.size() - 1, i, cardNumber, Dominion.I().getExpansion(i).getPercentage(cardNumber));
			}
		}
		this.addCommand(randomizeCmd);
		// # if polish.android
		this.addCommand(percentage05Cmd);
		UiAccess.addSubCommand( this.per0Cmd, this.percentage05Cmd, this );
		UiAccess.addSubCommand( this.per1Cmd, this.percentage05Cmd, this );
		UiAccess.addSubCommand( this.per2Cmd, this.percentage05Cmd, this );
		UiAccess.addSubCommand( this.per3Cmd, this.percentage05Cmd, this );
		UiAccess.addSubCommand( this.per4Cmd, this.percentage05Cmd, this );
		UiAccess.addSubCommand( this.per5Cmd, this.percentage05Cmd, this );
		this.addCommand(percentage610Cmd);
		UiAccess.addSubCommand( this.per6Cmd, this.percentage610Cmd, this );
		UiAccess.addSubCommand( this.per7Cmd, this.percentage610Cmd, this );
		UiAccess.addSubCommand( this.per8Cmd, this.percentage610Cmd, this );
		UiAccess.addSubCommand( this.per9Cmd, this.percentage610Cmd, this );
		UiAccess.addSubCommand( this.per10Cmd, this.percentage610Cmd, this );
		// # else
		this.addCommand(perGaugeCmd);
		// # endif
		this.addCommand(quitCmd);
		this.setCommandListener(this);
	}
	
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Canvas.KEY_STAR:
			if (this.getCurrentIndex() + 10 < this.size() )
				this.focus(this.getCurrentIndex() + 10);
			else
				this.focus(0);
			break;
		case Canvas.KEY_POUND:
			tmp = 0;
			switch ( Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()) ) {
			case 3:	tmp = 0; break;
			case 2:	tmp += Dominion.I().getExpansion(2).size();
			case 1:	tmp += Dominion.I().getExpansion(1).size();
			case 0:	tmp += Dominion.I().getExpansion(0).size();
			}
			this.focus(tmp);
			break;
		default:
			//#= super.keyPressed(keyCode);
		}
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd.equals(randomizeCmd) ) {
			//#debug info
			System.out.println("I AM TRULY CALLED");
			updateCards(true);
			GameApp.instance().showRandomizedCards();
		// # if polish.android
		} else if ( cmd.equals(per0Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 0);
		} else if ( cmd.equals(per1Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 1);
		} else if ( cmd.equals(per2Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 2);
		} else if ( cmd.equals(per3Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 3);
		} else if ( cmd.equals(per4Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 4);
		} else if ( cmd.equals(per5Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 5);
		} else if ( cmd.equals(per6Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 6);
		} else if ( cmd.equals(per7Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 7);
		} else if ( cmd.equals(per8Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 8);
		} else if ( cmd.equals(per9Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 9);
		} else if ( cmd.equals(per10Cmd) ) {
			this.setPercentage(Dominion.I().getLinearExpansionIndex(this.getCurrentIndex()),
					Dominion.I().getLinearCardIndex(this.getCurrentIndex()), 10);
		// # else
		} else if ( cmd.equals(perGaugeCmd) ) {
			tmp = this.getCurrentIndex();
			String tmpS = Dominion.I().getExpansion(
					Dominion.I().getLinearExpansionIndex(tmp)).getName(Dominion.I().getLinearCardIndex(tmp));
			GaugeForm.instance().setGauge(Locale.get("gauge.card.percentage", tmpS), true, 10, 0);
			GaugeForm.instance().setGaugeValue(
					Dominion.I().getExpansion(Dominion.I().getLinearExpansionIndex(tmp)).getPercentage(
							Dominion.I().getLinearCardIndex(tmp)));
			GaugeForm.instance().setCommandListener(this);
			GameApp.instance().changeToScreen(GaugeForm.instance());
			tmpS = null;
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.ok")) ) {
			GameApp.instance().changeToScreen(null);
			this.setPercentage(tmp, Dominion.I().getLinearExpansionIndex(tmp),
					Dominion.I().getLinearCardIndex(tmp), GaugeForm.instance().getGaugeValue());
		} else if ( cmd.getLabel().equals(Locale.get("polish.command.cancel")) ) {
			GameApp.instance().changeToScreen(null);
		// # endif
		} else if ( cmd.equals(quitCmd) ) {
			GameApp.instance().quit();
		}
	}
	
	public void setPercentage(int index, int exp, int card, int deciPercentage) {
		if ( deciPercentage > 0 ) {
			//#style label
			this.set(index, Dominion.I().getExpansion(exp).getName(card) + " " + deciPercentage * 10 + "%", this.getImage(index));
			Dominion.I().getExpansion(exp).setPercentage(card, deciPercentage);
		} else {
			//#style label
			this.set(index, Dominion.I().getExpansion(exp).getName(card), this.getImage(index));
			Dominion.I().getExpansion(exp).setPercentage(card, 0);
		}
		if ( index == 0 && this.size() > 1 )
			UiAccess.setFocusedIndex(this, 1);
		else 
			UiAccess.setFocusedIndex(this, 0);
		UiAccess.setFocusedIndex(this, index);
	}
	
	public void setPercentage(int exp, int card, int deciPercentage) {
		this.setPercentage(this.getCurrentIndex(), exp, card, deciPercentage);
	}
	
	public void updateCards(boolean localUpdate) {
		if ( localUpdate ) {
			for ( int i = 0 ; i < this.size() ; i++ ) {
				this.changeCard(i, this.getItem(i).isSelected, false);
			}
		} else {
			for ( int i = 0 ; i < this.size() ; i++ ) {
				/*///#debug info
				System.out.println("redoing: " + Dominion.I().getExpansion(
						Dominion.I().getLinearExpansionIndex(i)).isAvailable(
						Dominion.I().getLinearCardIndex(i)));*/
				this.changeCard(i, Dominion.I().getExpansion(
						Dominion.I().getLinearExpansionIndex(i)).isAvailable(
						Dominion.I().getLinearCardIndex(i))
						, false);
			}
		}
	}
	
	private void changeCard(int index, boolean isAvailable, boolean switchState) {
		if ( index < 0 ) 
			return;
		if ( switchState ) {
			this.setSelectedIndex(index, !Dominion.I().getExpansion(
				Dominion.I().getLinearExpansionIndex(this.getCurrentIndex())).isAvailable(
						Dominion.I().getLinearCardIndex(this.getCurrentIndex())));
		} else {
			this.setSelectedIndex(index, isAvailable);
		}
		Dominion.I().getExpansion(Dominion.I().getLinearExpansionIndex(index)).setAvailable(
				Dominion.I().getLinearCardIndex(index), this.getItem(index).isSelected);
		Dominion.I().getExpansion(Dominion.I().getLinearExpansionIndex(index)).setBlackMarketAvailable(
				Dominion.I().getLinearCardIndex(index), this.getItem(index).isSelected);
	}
}
