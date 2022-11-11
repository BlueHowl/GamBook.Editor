package org.helmo.gbeditor.repositories.exceptions;

import java.sql.SQLException;

public class UnableToRollbackException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnableToRollbackException(SQLException ex) {
        super(ex);
    }
}
