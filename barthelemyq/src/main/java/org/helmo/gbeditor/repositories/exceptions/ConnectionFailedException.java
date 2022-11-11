package org.helmo.gbeditor.repositories.exceptions;


public class ConnectionFailedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionFailedException(String s, Exception ex) {
        super(s, ex);
    }
}
