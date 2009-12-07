import de.enough.polish.util.Locale;
import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class SettingsRecordStorage {
	public static final char BIG_SPLITTER = ':';
	public static final char MEDIUM_SPLITTER = ';';
	public static final char SMALL_SPLITTER = '#';
	public static final char OPTION_SPLITTER = '=';

	private Vector settings = null;
	private RecordStore store = null;
	private byte[] tmpByte = null;
	boolean[] expansionsSettings = new boolean[] {true, true, true, false};

	public SettingsRecordStorage() {
		super();
		settings = null;
		if ( store != null ) {
			try {
				store.closeRecordStore();
			} catch (RecordStoreNotOpenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RecordStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}

	public boolean[] getExpansions() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		//#debug info
		System.out.println("reading file.");
		settings = readData(Locale.get("rms.file.settings"));
		if ( settings == null ) {
			//#debug info
			System.out.println("SettingsRecordStorage: getExpansions : settings is null");
		} else {
			for ( int i = 0 ; i < settings.size() ; i++ ) {
				//#debug info
				System.out.println("found Element: " + settings.elementAt(i).toString());
				if ( settings.elementAt(i).toString().startsWith(Locale.get("rms.expansions")) ) {
					//#debug info
					System.out.println("SettingsRecordStorage: getExpansions : Element" + settings.elementAt(i).toString());
					expansionsSettings[0] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 2, Locale.get("rms.expansions").length() + 3).equals("1");
					expansionsSettings[1] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 3, Locale.get("rms.expansions").length() + 4).equals("1");
					expansionsSettings[2] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 4, Locale.get("rms.expansions").length() + 5).equals("1");
					expansionsSettings[3] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 5).equals("1");
					i = settings.size();
				}
			}
		}
		return expansionsSettings;
	}

	public boolean writeExpansions(boolean[] expansions) throws RecordStoreFullException, RecordStoreNotFoundException,
	        RecordStoreException {
		StringBuffer sb = new StringBuffer();
		sb.append(expansions[0] ? "1" : "0");
		sb.append(expansions[1] ? "1" : "0");
		sb.append(expansions[2] ? "1" : "0");
		sb.append(expansions[3] ? "1" : "0");
		return writeData(Locale.get("rms.file.settings"), Locale.get("rms.expansions"), sb.toString());
	}

	public Vector readData(String recordStore) throws RecordStoreFullException, RecordStoreException {
		Vector data = null;
		try {
			//#debug info
			System.out.println("opening record store");
			store = RecordStore.openRecordStore(recordStore, true);
			//#debug info
			System.out.println("found " + store.getNumRecords() + " records");
			if ( store.getNumRecords() == 0 ) {
				throw new RecordStoreException("there is no records to be founds");
			}
			data = new Vector(store.getNumRecords());
			for (int i = 0; i < store.getNumRecords(); i++) {
				//record = new byte[store.getRecordSize(i + 1)];
				//#debug info
				System.out.println("try reading record #= " + i);
				tmpByte = store.getRecord(i + 1);
				if ( tmpByte != null ) {
					//#debug info
					System.out.println("Found record: " + new String(tmpByte));
					data.addElement(new String(tmpByte));
				}
			}
			return data;
		} catch (RecordStoreException rms) {
			//#debug info
			System.out.println("store exception happened. " + rms.toString());
			data = null;
		} finally {
			store.closeRecordStore();
		}
		return data;
	}


	public boolean writeData(String recordStore, String key, String record) throws RecordStoreFullException, RecordStoreNotFoundException,
	        RecordStoreException {
		boolean succes = true;
		//#debug info
		System.out.println("Writing record: " + record);
		try {
			settings = readData(recordStore);
			this.deleteRecordStore(recordStore);
			store = RecordStore.openRecordStore(recordStore, true);
			for ( int i = 0 ; i < settings.size() ; i++ )
				if ( settings.elementAt(i).toString().startsWith(key) )
					settings.removeElementAt(i);
			for ( int i = 0 ; i < settings.size() ; i++ )
				if ( settings.elementAt(i).toString().startsWith(key) )
					store.addRecord(settings.elementAt(i).toString().getBytes(), 0, settings.elementAt(i).toString().getBytes().length);
			//tmpByte = record.getBytes();
			store.addRecord(new String(key + BIG_SPLITTER + record).getBytes(), 0, new String(key + BIG_SPLITTER + record).getBytes().length);
			//#debug info
			System.out.println("Succes");
		} catch (Exception e) {
			succes = false;
		} finally {
			store.closeRecordStore();
		}
		return succes;
	}
	
	public void deleteRecordStore(String recordStore) {
		try {
			RecordStore.deleteRecordStore(recordStore);
		} catch (RecordStoreFullException e) {
			//#debug info
			System.out.println("deletion failed");
		} catch (RecordStoreNotFoundException e) {
			//#debug info
			System.out.println("deletion failed");
		} catch (RecordStoreException e) {
			//#debug info
			System.out.println("deletion failed");
		}
	}
}
