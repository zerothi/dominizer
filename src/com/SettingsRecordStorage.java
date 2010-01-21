package com;

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

	private static Vector data = null;
	private static RecordStore store = null;
	private static byte[] tmpByte = null;

	public SettingsRecordStorage() {
		super();
		data = null;
		if ( store != null ) {
			try {
				store.closeRecordStore();
			} catch (RecordStoreNotOpenException e) {
				// Already closed
			} catch (RecordStoreException e) {
				// This is an actual error
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

	public boolean savePreset(String presetName, String preset) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		StringBuffer sb = new StringBuffer(100);
		sb.append(presetName);
		sb.append(BIG_SPLITTER);
		sb.append(preset);
		sb.append(BIG_SPLITTER);
		return writeData(Locale.get("rms.file.preset"), null, sb.toString());
	}
	
	public String readKey(String recordStore, String key) {
		Vector data = null;
		try {
			data = readData(recordStore);
		} catch (RecordStoreFullException e) {
		} catch (RecordStoreException e) {
		}
		if ( data == null ) {
			return null;
		}
		for ( int i = 0 ; i < data.size() ; i++ ) {
			if ( data.elementAt(i).toString().startsWith(key) )
				return data.elementAt(i).toString().substring(key.length() + 1);
		}
		return null;
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
				for ( int i = 0 ; i < data.size() ; i++ ) {
					if ( key != null ) {
						if ( !data.elementAt(i).toString().startsWith(key) )
							store.addRecord(data.elementAt(i).toString().getBytes(), 0, data.elementAt(i).toString().getBytes().length);
					} else {
						if ( !data.elementAt(i).toString().startsWith(record) )
							store.addRecord(data.elementAt(i).toString().getBytes(), 0, data.elementAt(i).toString().getBytes().length);
					}
				}
			if ( key != null ) {
				store.addRecord(new String(key + BIG_SPLITTER + record).getBytes(), 0, new String(key + BIG_SPLITTER + record).getBytes().length);
			} else {
				store.addRecord(new String(record).getBytes(), 0, new String(record).getBytes().length);
			}
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
	
	public boolean deleteKey(String recordStore, String key) {
		Vector data = null;
		try {
			data = readData(recordStore);
		} catch (RecordStoreFullException e) {
		} catch (RecordStoreException e) {
		}
		if ( data == null ) {
			return true;
		}
		deleteRecordStore(recordStore);
		for ( int i = 0 ; i < data.size() ; i++ ) {
			if ( !data.elementAt(i).toString().startsWith(key) ) {
				try {
					writeData(recordStore, null, data.elementAt(i).toString());
				} catch (RecordStoreFullException e) {
					return false;
				} catch (RecordStoreNotFoundException e) {
					return false;
				} catch (RecordStoreException e) {
					return false;
				}
			}
		}
		return true;
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
