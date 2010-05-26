package com.util;


public class Condition {
	
	public static final int MAX_RUNS = 250;
	private String[] name = null;
	private String[] condition = null;
	private int[] percentage;
	
	public Condition(int size) {
		if ( size == 0 )
			return;
		name = new String[size];
		condition = new String[size];
		percentage = new int[size];
		for ( int i = 0 ; i < size ; i++ ) {
			name[i] = condition[i] = null;
			percentage[i] = 0;
		}
	}
	
	public String getName(int index) {
		if ( index < name.length )
			return name[index];
		return null;
	}
	public String getCondition(int index) {
		if ( index < condition.length )
			return condition[index];
		return null;
	}
	
	public int getPercentage(int index) {
		if ( percentage.length <= index )
			return -1;
		if ( percentage[index] >= 100 )
			return percentage[index] - 100;
		return percentage[index];
	}
	
	public boolean isAvailable(int index) {
		if ( percentage.length <= index ) {
			//#debug dominizer
			System.out.println("isAvailable: no check returning false");
			return false;
		}
		//#debug dominizer
		System.out.println("isAvailable: returning "+ (percentage[index] >= 100));
		return percentage[index] >= 100;
	}
	
	public void setAvailable(int index, boolean available) {
		if ( this.percentage.length <= index )
			return;
		if ( available ) {
			if ( this.percentage[index] < 100 ) {
				//#debug dominizer
				System.out.println("setAvailable: set to true");
				this.percentage[index] += 100;
			}
		} else {
			if ( this.percentage[index] >= 100 )
				this.percentage[index] -= 100;
		}
	}
	
	public void setPercentage(int index, int deciPercentage) {
		if ( this.percentage.length <= index )
			return;
		if ( this.percentage[index] >= 100 )
			this.percentage[index] = 100 + deciPercentage;
		else
			this.percentage[index] = deciPercentage;
	}
	
	public void setName(int index, String name) {
		if ( this.name == null )
			return;
		if ( this.name.length <= index )
			return;
		this.name[index] = name;
	}
	
	public void setCondition(int index, String condition) {
		if ( this.condition.length <= index )
			return;
		this.condition[index] = condition;
	}
	
	public String getNameAsSave(int index) {
		if ( name == null )
			return null;
		if ( name.length <= index )
			return null;
		StringBuffer sb = new StringBuffer(2 + name[index].length());
		if ( percentage[index] >= 100 ) {
			sb.append("1");
			if ( percentage[index] - 100 == 10 )
				sb.append("*");
			else
				sb.append("" + (percentage[index] - 100));
		} else {
			sb.append("0");
			if ( percentage[index] == 10 )
				sb.append("*");
			else
				sb.append("" + percentage[index]);
		}
		sb.append(name[index]);
		return sb.toString();
	}
	
	public int size() {
		if ( name == null )
			return 0;
		return name.length;
	}
	
	
	public boolean addCondition(String name, String condition) {
		if ( name == null | condition == null )
			return false;
		if ( name.trim().length() == 0 & condition.trim().length() == 0 )
			return false;
		if ( this.name == null ) {
			this.name = new String[1];
			this.condition = new String[1];
			this.percentage = new int[1];
			this.name[0] = name;
			this.condition[0] = condition;
			this.percentage[0] = 0;
			return true;
		}
		String[] tmp = new String[this.name.length + 1];
		int[] tmpI = new int[this.name.length + 1];
		int i;
		for ( i = 0 ; i < this.name.length ; i++ ) {
			tmp[i] = this.name[i];
			tmpI[i] = this.percentage[i];
		}
		tmp[this.name.length] = name;
		tmpI[this.name.length] = 100;
		this.name = tmp;
		this.percentage = tmpI;
		tmp = new String[this.condition.length + 1];
		for ( i = 0 ; i < this.condition.length ; i++ )
			tmp[i] = this.condition[i];
		tmp[this.condition.length] = condition;
		this.condition = tmp;
		return true;	
	}
	
	public void deleteCondition(String name) {
		for ( int i = 0 ; i < this.name.length ; i++ ) {
			if ( this.name[i].equals(name) ) {
				deleteCondition(i);
				return;
			}
		}
	}
	
	public void deleteCondition(int index) {
		int i;
		String[] tmp = new String[name.length - 1];
		int[] tmpI = new int[this.name.length - 1];
		for ( i = 0 ; i < name.length ; i++ ) {
			if ( i < index ) {
				tmp[i] = name[i];
				tmpI[i] = percentage[i];
			} else if ( index < i ) {
				tmp[i - 1] = name[i];
				tmpI[i - 1] = percentage[i];
			}
		}
		name = tmp;
		percentage = tmpI;
		tmp = new String[condition.length - 1];
		for ( i = 0 ; i < condition.length ; i++ ) {
			if ( i < index )
				tmp[i] = condition[i];
			else if ( index < i )
				tmp[i - 1] = condition[i];
		}
		condition = tmp;
	}
	
	public int chooseCondition() {
		boolean possible = false;
		for ( int i = 0 ; i < size() ; i++ )
			possible = possible | isAvailable(i);
		if ( possible ) {
			Rand.resetSeed();
			int index = -1;
			while ( possible ) {
				index = Rand.randomInt(size());
				if ( isAvailable(index) ) {
					if ( Rand.randomInt(10) < getPercentage(index) )
						return index;
					else if ( getPercentage(index) == 0 & Rand.randomInt(size()) == 0 )
						return index;
				}
			}
		}
		return -1;
	}
}
