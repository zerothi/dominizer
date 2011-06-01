package com.util;

import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class SettingsRecordStorage {

	private static SettingsRecordStorage srs = null;
	public static final char BIG_SPLITTER = ':';
	public static final char MEDIUM_SPLITTER = ';';
	public static final char SMALL_SPLITTER = '#';
	public static final char OPTION_SPLITTER = '=';

	private static String currentStore = null;
	private static Vector
	//#if polish.android
		//#= <String>
	//#endif
	data = null;
	private static RecordStore store = null;
	private static byte[] tmpByte = null;


	private SettingsRecordStorage() {
		super();
	}

	public static SettingsRecordStorage instance() {
		if ( srs == null )
			srs = new SettingsRecordStorage();
		return srs;
	}

	public boolean changeToRecordStore(String recordStore) {
		try {
			if ( store != null && !store.getName().equals(recordStore) ) {
				data = null;
				store.closeRecordStore();
			}
		} catch (RecordStoreNotOpenException e) {
			// Already closed
		} catch (RecordStoreException e) {
			// TODO Use GameApp.showAlert in this section!
		}
		try {
			//#debug dominizer
			System.out.println("opening record store '" + recordStore + "'");
			store = RecordStore.openRecordStore(recordStore, true);
			currentStore = recordStore;
			data = readData();
			store.closeRecordStore();
			return true;
		} catch (RecordStoreFullException e) {
			store = null;
			//#debug dominizer
			System.out.println("error read");
		} catch (RecordStoreNotFoundException e) {
			store = null;
			//#debug dominizer
			System.out.println("error read");
		} catch (RecordStoreException e) {
			store = null;
			//#debug dominizer
			System.out.println("error read");
		}
		return false;
	}

	public String readKey(String key) {
		if ( data == null )
			return null;
		for ( int i = 0 ; i < data.size() ; i++ )
			if ( data.elementAt(i).toString().startsWith(key + BIG_SPLITTER) )
				return data.elementAt(i).toString().substring(key.length() + 1);
		return null;
	}

	public Vector
	//#if polish.android
		//#= <String>
	//#endif
	data() {
		return data;
	}

	private Vector
	//#if polish.android
		//#= <String>
	//#endif
	readData() throws RecordStoreFullException, RecordStoreException {
		data = null;
		if ( store == null )
			return data;
		try {
			//#debug dominizer
			System.out.println("found " + store.getNumRecords() + " records in recordstore '" + store.getName() + "'");
			if ( store.getNumRecords() == 0 ) {
				throw new RecordStoreException("there is no records to be found");
			}
			RecordEnumeration re = store.enumerateRecords(null, null, false);
			data = new Vector
			//#if polish.android
				//#= <String>
			//#endif
			(store.getNumRecords());
			while ( re.hasNextElement() ) {
				tmpByte = re.nextRecord();
				if ( tmpByte != null ) {
					//#debug dominizer
					System.out.println("found record: " + new String(tmpByte));
					data.addElement(new String(tmpByte));
				}
			}
		} catch (RecordStoreException rms) {
			//#debug dominizer
			System.out.println("store exception happened");
			data = null;
		}
		return data;
	}

	public void addData(String key, String record) {
		if ( record == null )
			return;
		if ( data == null ) {
			data = new Vector
			//#if polish.android
				//#= <String>
			//#endif
			(1);
		}
		for ( int i = 0 ; i < data.size() ; i++ ) {
			if ( key != null ) {
				if ( data.elementAt(i).toString().startsWith(key) ) {
					//#debug dominizer
					System.out.println("overwriting data. key= " + key + " record= " + record);
					data.setElementAt(key + BIG_SPLITTER + record, i);
					return;
				}
			} else {
				if ( data.elementAt(i).toString().startsWith(record) ) {
					//#debug dominizer
					System.out.println("overwriting data. record= " + record);
					data.setElementAt(record, i);
					return;
				}
			}
		}
		if ( key != null ) {
			//#debug dominizer
			System.out.println("adding data. key= " + key + " record= " + record);
			data.addElement(key + BIG_SPLITTER + record);
		} else {
			//#debug dominizer
			System.out.println("adding data. record= " + record);
			data.addElement(record);
		}
	}

	public boolean writeData() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		boolean succes = true;
		deleteRecordStore(currentStore);
		store = RecordStore.openRecordStore(currentStore, true);
		try {
			if ( data != null )
				for ( int i = 0 ; i < data.size() ; i++ ) {
					store.addRecord(data.elementAt(i).toString().getBytes(), 0, data.elementAt(i).toString().getBytes().length);
				}
			//#debug dominizer
			System.out.println("Succes writing recordstore '" + store.getName() + "'");
		} catch (Exception e) {
			succes = false;
		}
		return succes;
	}

	public void closeRecord() {
		if ( store != null ) {
			data = null;
			try {
				store.closeRecordStore();
			} catch (RecordStoreNotOpenException e) {
				// TODO Utilize GameApp.showAlert
			} catch (RecordStoreException e) {
				// TODO Utilize GameApp.showAlert
			}
		}
	}

	public boolean deleteData(String key) {
		if ( data == null )
			return false;
		for ( int i = 0 ; i < data.size() ; i++ ) {
			if ( data.elementAt(i).toString().startsWith(key) ) {
				data.removeElementAt(i);
				//#debug dominizer
				System.out.println("deleted the key=" + key);
				return true;
			}
		}
		return false;
	}

	public void deleteRecordStore(String recordStore) {
		try {
			RecordStore.deleteRecordStore(recordStore);
		} catch (RecordStoreFullException e) {
			//#debug dominizer
			System.out.println("deletion failed");
		} catch (RecordStoreNotFoundException e) {
			//#debug dominizer
			System.out.println("deletion failed");
		} catch (RecordStoreException e) {
			//#debug dominizer
			System.out.println("deletion failed");
		}
	}
}
