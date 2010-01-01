/**
 * 
 */
package com;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import de.enough.polish.calendar.CalendarItem;
import de.enough.polish.util.Locale;

/**
 * @author nick
 *
 */
public class GameCalendar extends Form implements CommandListener {

	private CalendarItem calendar = null;
	private Command addGameCmd = new Command(Locale.get("cmd.Calendar.AddGame"), Command.BACK, 0);
	
	public GameCalendar(String title) {
		//#style mainScreen
		super(title);
		//#style calendar
		calendar = new CalendarItem();
		this.append(calendar);
		this.addCommand(addGameCmd);
		this.setCommandListener(this);
	}
	
	/**
     * Adds event to list of events. 
     * Gets data for event from addEventForm controls.
     */
    private void addGame() {
    	/*
        try {
            // Get list of events.
            EventList eventList = (EventList)PIM.getInstance().openPIMList(
                    PIM.EVENT_LIST, PIM.READ_WRITE);            
 
            // Create new event.
            Event event = eventList.createEvent();
 
            // Get data from controls
            if(eventList.isSupportedField(Event.SUMMARY) == true) {
                String summary = summaryField.getString();
                event.addString(Event.SUMMARY, PIMItem.ATTR_NONE, summary);
            } else {
                // At least summary must be supported.
                eventList.close();
                throw new Exception("Summary field for event is not supported");
            }
 
            if(eventList.isSupportedField(Event.START) == true) {
                long startDate = startDateField.getDate().getTime();
                event.addDate(Event.START, PIMItem.ATTR_NONE, startDate);
            }
 
            if(eventList.isSupportedField(Event.END) == true) {
                long endDate = endDateField.getDate().getTime();
                event.addDate(Event.END, PIMItem.ATTR_NONE, endDate);
            }
 
            if(eventList.isSupportedField(Event.NOTE) == true) {
                String note = noteField.getString();
                event.addString(Event.NOTE, PIMItem.ATTR_NONE, note);
            }
 
            if(eventList.isSupportedField(Event.LOCATION) == true) {
                String location = locationField.getString();
                event.addString(Event.LOCATION, PIMItem.ATTR_NONE, location);
            }      
 
            // Commit event.
            event.commit();
            // Close list of events.
            eventList.close();
 
            // Notify user that event was added
            GameApp.instance().showInfo("Event was successfully added.", 2000);
 
        } catch(PIMException pimExc) {
            // TODO: Handle error on working with PIM.
            GameApp.instance().showAlert("PIMException" + pimExc.getMessage());
        }
        catch(SecurityException secExc) {
            // TODO: Handle error on access to PIM.
        	GameApp.instance().showAlert("SecurityException" + secExc.getMessage());
        }
        catch(Exception exc) {
            // TODO: Handle all other errors.
        	GameApp.instance().showAlert("Exception" + exc.getMessage());
        }
        */   
    }
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command cmd, Displayable disp) {
		if ( cmd == addGameCmd )
			addGame();

	}

}
