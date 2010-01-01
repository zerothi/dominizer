package com;



import java.util.Vector;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;

import com.dominizer.GameApp;


import de.enough.polish.ui.Choice;
import de.enough.polish.util.Locale;

public class EditCardsForm extends Form implements CommandListener, ItemStateListener {
	
	private Object[][] cards = null;
	private ChoiceGroup expGroup= null;
	private ChoiceGroup cardGroup= null;
	private Command editCmd = new Command( Locale.get( "cmd.EditPopUp" ), Command.SCREEN, 2 );

	public EditCardsForm(String title) {
		//#style mainScreen
		super(title);
		this.expGroup = new ChoiceGroup(Locale.get("screen.EditSingleCards.ChoiceExp"), Choice.EXCLUSIVE);
		//#style choiceGroup
		this.cardGroup = new ChoiceGroup(Locale.get("screen.EditSingleCards.ChoiceCurrentlySelected"), Choice.MULTIPLE);
		this.cardGroup.addCommand(editCmd);
		this.cardGroup.setDefaultCommand(editCmd);
		
		this.append(this.expGroup);
		this.append(this.cardGroup);
		this.setCommandListener(this);
		this.setItemStateListener(this);
	}

	public void setCards(Cards cards) {
		this.cardGroup.deleteAll();
		for ( int cardNumber = 0 ; cardNumber < cards.size() ; cardNumber++ )
			this.cardGroup.append(cards.getName(cardNumber), null);
		
	}
	
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd == editCmd ) {
			GameApp.instance().showAlert("Hej");
		}
	}

	public void itemStateChanged(Item item) {
		this.itemStateChanged(item, null);
	}
	
	public void itemStateChanged(Item item, Displayable disp) {
		// TODO Auto-generated method stub
		if ( item.equals(this.expGroup) ) {
			//
		}
			
	}
}
