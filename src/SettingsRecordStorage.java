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
	private boolean[] expansions = new boolean[4];

	public SettingsRecordStorage() {
		super();
		settings = null;
	}

	public boolean[] getExpansions() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		readExpansions();
		return expansions;
	}

	public boolean writeExpansions(String expansions) throws RecordStoreFullException, RecordStoreNotFoundException,
	        RecordStoreException {
		if ( expansions != null ) {
			//#debug info
			System.out.println("Before loop");
			boolean deleted = false;
			int i = 0;
			while (i < settings.size() & !deleted) {
				if ( ((String)settings.elementAt(i)).startsWith(Locale.get("rms.expansions")) ) {
					//#debug info
					System.out.println("found");
					try {
						store = RecordStore.openRecordStore(Locale.get("rms.file.settings"), true);
						store.deleteRecord(i + 1);
						deleted = true;
					} finally {
						store.closeRecordStore();
					}
				}
				//#debug info
				System.out.println("Iterate");
				i++;
			}
		}
		return writeData(Locale.get("rms.file.settings"), expansions);
	}

	public void populateEvent(String event) {
		try {
			populateEvents();
			populatePersons();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
		ExpenseEvent.getInstance(true);
		String tmpData = null;
		int size = events.size();
		int i = 0;
		boolean readEvent = false;
		while( i < size & !readEvent) {
			tmpData = (String) events.elementAt(i);
			if (tmpData.startsWith(event + BIG_SPLITTER + SETTINGS)) {
				//#debug info
				System.out.println("Event found: " + event);
				readEvent = true;
				int[] splits = {
				        0, 0, 0, 0, 0, 0
				};
				splits[0] = tmpData.indexOf(BIG_SPLITTER);
				splits[1] = tmpData.indexOf(MEDIUM_SPLITTER);
				splits[2] = tmpData.indexOf(MEDIUM_SPLITTER, splits[1] + 1);
				splits[3] = tmpData.indexOf(MEDIUM_SPLITTER, splits[2] + 1);
				splits[4] = tmpData.indexOf(MEDIUM_SPLITTER, splits[3] + 1);
				splits[5] = tmpData.indexOf(BIG_SPLITTER, splits[4] + 1);
				//#debug info
				System.out.println("The splits: 1st=" + splits[0] + " 2nd=" + splits[1] +" 3rd=" +splits[2]+" 4th=" + splits[3] +" 5th=" + splits[4] +" 6th=" + splits[5]);
				ExpenseEvent.getInstance().setName(tmpData.substring(0, splits[0]));
				ExpenseEvent.getInstance().setStartDay(Integer.parseInt(tmpData.substring(splits[1] + 1, splits[2])));
				ExpenseEvent.getInstance().setDays(Integer.parseInt(tmpData.substring(splits[2] + 1, splits[3])));
				//#debug info
				System.out.println("\nEvent Reading: " + ExpenseEvent.getInstance().getName() + "\nStart day: "
				        + ExpenseEvent.getInstance().getStartDay() + "\nDays: " + ExpenseEvent.getInstance().getDays());
				String[] arrayTmp = null;
				if ((splits[3] + 1) < splits[4]) {
					arrayTmp = TextUtil.split(tmpData.substring(splits[3] + 1, splits[4]), OPTION_SPLITTER, 3);
					try {
	                    FoodExpense.setMorningNoonEvening(arrayTmp[0].equals("true"), arrayTmp[1].equals("true"), arrayTmp[2].equals("true"));
                    } catch (Exception e) {
	                    e.printStackTrace();
                    }
				}
				if ((splits[4] + 1) < splits[5]) {
					arrayTmp = TextUtil.split(tmpData.substring(splits[4] + 1, splits[5]), OPTION_SPLITTER, 3);
					try {
	                    FoodExpense.setPercentages(Double.parseDouble(arrayTmp[0]), Double.parseDouble(arrayTmp[1]), Double.parseDouble(arrayTmp[2]));
                    } catch (Exception e) {
	                    e.printStackTrace();
                    }
				}
				arrayTmp = TextUtil.split(tmpData.substring(splits[5] + 1), BIG_SPLITTER);
				int arraysize = arrayTmp.length;
				//#debug info
				System.out.println("Event Reading: Persons: " + arraysize + ". Full: " + arrayTmp[0]);
				String[] arrayPerson;
				for (int k = 0; k < arraysize; k++) {
					arrayPerson = TextUtil.split(arrayTmp[k], MEDIUM_SPLITTER, 4);
					//#debug info
					System.out.println("Event Reading: Person info: " + arrayPerson[0] + ". And " + arrayPerson[1] + ". And " + arrayPerson[2] + ". And " + arrayPerson[3] +".");
					Person tmpPerson = null;
					if (!isEmptyString(arrayPerson[0])) {
						tmpPerson = new Person(arrayPerson[0]);
						//#debug info
						System.out.println("Event Reading: Person name: " + tmpPerson.getName());
						String[] arrayExp = null;
						String[] arraySingleExp = null;
						int arrayExpsize = 0;
						if (!isEmptyString(arrayPerson[1])) {
							arrayExp = TextUtil.split(arrayPerson[1], SMALL_SPLITTER);
							arrayExpsize = arrayExp.length;
							//#debug info
							System.out.println("Event Reading: Expenses: " + arrayExpsize);
							for (int j = 0; j < arrayExpsize; j++) {
								arraySingleExp = TextUtil.split(arrayExp[j], OPTION_SPLITTER, 2);
								//#debug info
								System.out.println("Event Reading: Expense: " + arraySingleExp[0] + ". Value=" + arraySingleExp[1]);
								Expense expTmp = new Expense(Double.parseDouble(arraySingleExp[1]), arraySingleExp[0]);
								tmpPerson.addExpense(expTmp);
							}
						}
						if (!isEmptyString(arrayPerson[2])) {
							arrayExp = TextUtil.split(arrayPerson[2], SMALL_SPLITTER);
							arrayExpsize = arrayExp.length;
							//#debug info
							System.out.println("Event Reading: FoodExpenses: " + arrayExpsize);
							for (int j = 0; j < arrayExpsize; j++) {
								arraySingleExp = TextUtil.split(arrayExp[j], OPTION_SPLITTER, 2);
								//#debug info
								System.out.println("Event Reading: FoodExpense: " + arraySingleExp[0] + ". Value=" + arraySingleExp[1]);
								FoodExpense expTmp = new FoodExpense(Double.parseDouble(arraySingleExp[1]), arraySingleExp[0]);
								tmpPerson.addExpense(expTmp);
							}
						}
						if (!isEmptyString(arrayPerson[3])) {
							arrayExp = TextUtil.split(arrayPerson[3], OPTION_SPLITTER, 3);
							tmpPerson.setMornings(Integer.parseInt(arrayExp[0]));
							tmpPerson.setNoons(Integer.parseInt(arrayExp[1]));
							tmpPerson.setEvenings(Integer.parseInt(arrayExp[2]));
							//#debug info
							System.out.println("Event Reading: Days: " + tmpPerson.getMornings() + ", " + tmpPerson.getNoons() + ", " + tmpPerson.getEvenings() + ".");
						}
						ExpenseEvent.getInstance().addPerson(tmpPerson);
						//#debug info
						System.out.println("Event Reading: Person Added: " + tmpPerson.getName() + ".");
					}
				}
			}
			i++;
		}
		//#debug info
		System.out.println("Event Reading: Done parsing.");
	}

	private Vector readData(String recordStore) throws RecordStoreFullException, RecordStoreException {
		Vector data = null;
		try {
			store = RecordStore.openRecordStore(recordStore, true);
			byte[] record = null;
			int numRecords = store.getNumRecords();
			data = new Vector(numRecords);
			for (int i = 0; i < numRecords; i++) {
				record = new byte[store.getRecordSize(i + 1)];
				store.getRecord(i + 1, record, 0);
				data.addElement(new String(record));
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
		System.out.println("EventIO: Writing record: " + record);
		try {
			store = RecordStore.openRecordStore(recordStore, true);
			byte[] rec = record.getBytes();
			store.addRecord(rec, 0, rec.length);
			//#debug info
			System.out.println("EventIO: Succes");
		} catch (Exception e) {
			succes = false;
		} finally {
			store.closeRecordStore();
		}
		return succes;
	}

	private boolean isEmptyString(String tmp) {
		return tmp.trim().equals("");
	}
}
