package org.helmo.gbeditor.repositories.exceptions;

/**
 * Exception de tearDown impossible à effectuer de Mr Ludewig
 */
public class UnableToTearDownException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Remonte l'erreur
	 * @param ex (Exception) déclencheur
	 */
	public UnableToTearDownException(Exception ex) {
        super("Error while tearing down",ex);
    }
}
