package org.helmo.gbeditor.repositories.exceptions;

import java.sql.SQLException;

/**
 * Exception de déconnexion de Mr Ludewig
 */
public class DeconnectionFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Remonte l'erreur
	 * @param ex (SQLException) déclencheur
	 */
	public DeconnectionFailedException(SQLException ex) {
		super(ex);
    }
}
