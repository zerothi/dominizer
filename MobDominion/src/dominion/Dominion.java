package dominion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

public class Dominion {
	
	private Vector cards = null;
	private int selectCards = 10;
	
	public Dominion() {
		readCards();
	}
	
	/**
	 * @return the selectCards
	 */
	public int getSelectCards() {
		return selectCards;
	}

	/**
	 * @param selectCards the selectCards to set
	 */
	public void setSelectCards(int selectCards) {
		this.selectCards = selectCards;
	}
	
	public Vector getRandomizedCards() {
		Vector selectedCards = new Vector(selectCards);
		int totalAvailable = cards.size();
		int selectedElement = 0;
		Random selector = new Random(System.currentTimeMillis());
		for (int i = 0 ; i < selectCards ; i++ ) {
			selectedElement = selector.nextInt(totalAvailable - i);
			selectedCards.addElement(cards.elementAt(selectedElement));
			cards.removeElementAt(selectedElement);
		}
		selector = null;
		return selectedCards;
	}
	
	public Vector getAllCards() {
		return cards;
	}
	
	private void readCards() {
		readResource("base");
		readResource("intrigue");
	}
	
	private Card processCardInformation(String information) {
		Card card = new Card();
		int start = 0;
		int end = information.indexOf(":", start);
		card.setName(information.substring(start, end));
		start = end + 1;
		end = information.indexOf(":", start);
		card.setGame(information.substring(start, end));
		start = end + 1;
		end = information.indexOf(":", start);
		card.setCost(Integer.parseInt(information.substring(start, end)));
		start = end + 1;
		end = information.indexOf(":", start);
		card.setAction(information.substring(start, end) == "0" ? false : true);
		start = end + 1;
		end = information.indexOf(":", start);
		card.setGame(information.substring(start, end));
		card.setVictory(information.substring(start, end) == "0" ? false : true);
		start = end + 1;
		end = information.indexOf(":", start);
		card.setTreasure(information.substring(start, end) == "0" ? false : true);
		start = end + 1;
		end = information.indexOf(":", start);
		card.setAction(information.substring(start, end) == "0" ? false : true);
		start = end + 1;
		end = information.indexOf(";", start);
		card.setReaction(information.substring(start, end) == "0" ? false : true);
		return card;
	}
	
	private void readResource(String file) {
		StringBuffer sb = new StringBuffer();
		try {
		InputStream is = this.getClass().getResourceAsStream(file);
		int len = 0;
		byte[] data = new byte[64];
		try {
			while ( ( len = is.read(data) ) != -1 ) {
				sb.append(new String(data,0,len));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//#debug
			System.out.println("Ioexception");
		}
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//#debug
			System.out.println("Ioexception");
		}
		} catch (IllegalArgumentException e) {
			//#debug
			System.out.println("IllegalArgument" + e.toString());
		}
		if ( cards == null )
			cards = new Vector();
		String tmp = sb.toString();
		boolean foundEndLine = true;
		int start = 0;
		int end = -2;
		cards.ensureCapacity(50);
		while ( foundEndLine ) {
			if ( tmp.length() - 10 < end )
				foundEndLine = false;
			else {
				start = end + 2;
				end = tmp.indexOf(";", start);
				cards.addElement(processCardInformation(tmp.substring(start, end + 1)));
			}
		}
	}
}
