import de.enough.polish.util.Locale;
import java.util.Vector;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

public class SettingsRecordStorage {
	public static final char BIG_SPLITTER = ':';
	public static final char MEDIUM_SPLITTER = ';';
	public static final char SMALL_SPLITTER = '#';
	public static final char OPTION_SPLITTER = '=';

	private Vector settings = null;
	private RecordStore store = null;
	boolean[] expansionsSettings = new boolean[] {true, true, true, false};

	public SettingsRecordStorage() {
		super();
		settings = null;
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
				if ( settings.elementAt(i).toString().startsWith(Locale.get("rms.expansions")) ) {
					//#debug info
					System.out.println("SettingsRecordStorage: getExpansions : Element" + settings.elementAt(i).toString());
					//this.app.quickGameRandomizerCG.append(settings.elementAt(i).toString(), null);
					//settings.setElementAt(settings.elementAt(i).toString().substring(settings.elementAt(i).toString().indexOf(BIG_SPLITTER) + 1 ), i);
					//this.app.quickGameRandomizerCG.append(settings.elementAt(i).toString(), null);
					
					expansionsSettings[0] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 1, Locale.get("rms.expansions").length() + 3) == Locale.get("rms.base");
					expansionsSettings[1] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 3, Locale.get("rms.expansions").length() + 5) == Locale.get("rms.promo");
					expansionsSettings[2] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 5, Locale.get("rms.expansions").length() + 7) == Locale.get("rms.intrigue");
					expansionsSettings[3] = settings.elementAt(i).toString().substring(Locale.get("rms.expansions").length() + 7) == Locale.get("rms.seaside");
					i = settings.size();
				}
			}
		}
		return expansionsSettings;
	}

	public boolean writeExpansions(String expansions) throws RecordStoreFullException, RecordStoreNotFoundException,
	        RecordStoreException {
		settings = readData(Locale.get("rms.file.settings"));
		if ( settings != null ) {
			//#debug info
			System.out.println("Before loop");
			int i = 0;
			while ( i < settings.size() ) {
				if ( settings.elementAt(i).toString().startsWith(Locale.get("rms.expansions")) ) {
					//#debug info
					System.out.println("found, and will now write");
					try {
						store = RecordStore.openRecordStore(Locale.get("rms.file.settings"), true);
						store.deleteRecord(i + 1);
					} finally {
						store.closeRecordStore();
						i = settings.size();
					}
				}
				//#debug info
				System.out.println("Iterate");
				i++;
			}
		}
		return writeData(Locale.get("rms.file.settings"), Locale.get("rms.expansions") + BIG_SPLITTER + expansions);
	}

	public Vector readData(String recordStore) throws RecordStoreFullException, RecordStoreException {
		Vector data = null;
		try {
			//#debug info
			System.out.println("opening record store");
			store = RecordStore.openRecordStore(recordStore, true);
			byte[] record = null;
			//#debug info
			System.out.println("found " + store.getNumRecords());
			if ( store.getNumRecords() == 0 )
				throw new RecordStoreException();
			data = new Vector(store.getNumRecords());
			for (int i = 0; i < store.getNumRecords(); i++) {
				record = new byte[store.getRecordSize(i + 1)];
				store.getRecord(i + 1, record, 0);
				if ( record != null ) {
					//#debug info
					System.out.println("Found record: " + new String(record));
					data.addElement(new String(record));
				}
			}
		} catch (RecordStoreException rms) {
			data = null;
		} finally {
			store.closeRecordStore();
		}
		return data;
	}


	private boolean writeData(String recordStore, String record) throws RecordStoreFullException, RecordStoreNotFoundException,
	        RecordStoreException {
		boolean succes = true;
		//#debug info
		System.out.println("Writing record: " + record);
		try {
			store = RecordStore.openRecordStore(recordStore, true);
			store.addRecord(record.getBytes(), 0, record.getBytes().length);
			//#debug info
			System.out.println("Succes");
		} catch (Exception e) {
			succes = false;
		} finally {
			store.closeRecordStore();
		}
		return succes;
	}
}
