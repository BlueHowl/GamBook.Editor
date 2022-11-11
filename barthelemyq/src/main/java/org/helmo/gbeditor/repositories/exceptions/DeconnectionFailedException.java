package org.helmo.gbeditor.repositories.exceptions;

import java.sql.SQLException;

public class DeconnectionFailedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeconnectionFailedException(SQLException ex) {
    }
}
