import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import de.enough.polish.util.Locale;

public class SettingsRecordStorage {
	public static final char BIG_SPLITTER = ':';
	public static final char MEDIUM_SPLITTER = ';';
	public static final char SMALL_SPLITTER = '#';
	public static final char OPTION_SPLITTER = '=';

	private Vector data = null;
	private RecordStore store = null;
	private byte[] tmpByte = null;

	public SettingsRecordStorage() {
		super();
		data = null;
		if ( store != null ) {
			try {
				store.closeRecordStore();
			} catch (RecordStoreNotOpenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RecordStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				store = null;
			}
		}
	}

	public boolean writeExpansions(boolean[] expansions) throws RecordStoreFullException, RecordStoreNotFoundException,
	        RecordStoreException {
		StringBuffer sb = new StringBuffer(4);
		sb.append(expansions[0] ? "1" : "0");
		sb.append(expansions[1] ? "1" : "0");
		sb.append(expansions[2] ? "1" : "0");
		sb.append(expansions[3] ? "1" : "0");
		return writeData(Locale.get("rms.file.settings"), Locale.get("rms.expansions"), sb.toString());
	}
	
	public boolean writeExpansionCards(int[] expansionCards) throws RecordStoreFullException, RecordStoreNotFoundException,
	RecordStoreException {
		StringBuffer sb = new StringBuffer(4);
		sb.append(expansionCards[0]);
		sb.append(expansionCards[1]);
		sb.append(expansionCards[2]);
		sb.append(expansionCards[3]);
		return writeData(Locale.get("rms.file.settings"), Locale.get("rms.expansions.usedcards"), sb.toString());
}

	public Vector readData(String recordStore) throws RecordStoreFullException, RecordStoreException {
		data = null;
		try {
			//#debug info
			System.out.println("opening record store");
			store = RecordStore.openRecordStore(recordStore, true);
			//#debug info
			System.out.println("found " + store.getNumRecords() + " records");
			if ( store.getNumRecords() == 0 ) {
				throw new RecordStoreException("there is no records to be found");
			}
			RecordEnumeration re = store.enumerateRecords(null, null, false);
			data = new Vector(store.getNumRecords());
			while ( re.hasNextElement() ) {
				//#debug info
				System.out.println("try reading record #= ");
				tmpByte = re.nextRecord();
				if ( tmpByte != null ) {
					//#debug info
					System.out.println("Found record: " + new String(tmpByte));
					data.addElement(new String(tmpByte));
				}
			}
		} catch (RecordStoreException rms) {
			//#debug info
			System.out.println("store exception happened. " + rms.toString());
			data = null;
		} finally {
			store.closeRecordStore();
			//#debug info
			System.out.println("succesfull close of store");
		}
		return data;
	}


	public boolean writeData(String recordStore, String key, String record) throws RecordStoreFullException, RecordStoreNotFoundException,
	        RecordStoreException {
		boolean succes = true;
		//#debug info
		System.out.println("Writing record: " + record);
		try {
			data = readData(recordStore);
		} catch (RecordStoreFullException rms) {
			//#debug info
			System.out.println("error read");
		} catch (RecordStoreException rms) {
			//#debug info
			System.out.println("error read");
		}
		this.deleteRecordStore(recordStore);
		try {
			store = RecordStore.openRecordStore(recordStore, true);
			if ( data != null )
				for ( int i = 0 ; i < data.size() ; i++ )
					if ( !data.elementAt(i).toString().startsWith(key) )
						store.addRecord(data.elementAt(i).toString().getBytes(), 0, data.elementAt(i).toString().getBytes().length);
			store.addRecord(new String(key + BIG_SPLITTER + record).getBytes(), 0, new String(key + BIG_SPLITTER + record).getBytes().length);
			//#debug info
			System.out.println("Succes");
		} catch (Exception e) {
			succes = false;
		} finally {
			store.closeRecordStore();
		}
		data = null;
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
