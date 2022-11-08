package org.helmo.gbeditor.infrastructures.jdbc;


public class ConnectionFailedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionFailedException(String s, Exception ex) {
        super(s, ex);
    }
}
