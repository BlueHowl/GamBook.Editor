package org.helmo.gbeditor.repositories.exceptions;

import java.sql.SQLException;

/**
 * Exception de rollback impossible à effectuer de Mr Ludewig
 */
public class UnableToRollbackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Remonte l'erreur
	 * @param ex (SQLException) déclencheur
	 */
	public UnableToRollbackException(SQLException ex) {
        super(ex);
    }
}
