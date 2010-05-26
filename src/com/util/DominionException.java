package com.util;


public class DominionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	private String err = "";
	
	public DominionException(String err) {
		this.err = err;
	}
	
	public String toString() {
		return err;
	}

}