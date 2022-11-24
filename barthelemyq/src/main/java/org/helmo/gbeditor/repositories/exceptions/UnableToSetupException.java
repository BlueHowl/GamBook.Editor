package org.helmo.gbeditor.repositories.exceptions;

/**
 * Exception de setup impossible à effectuer de Mr Ludewig
 */
public class UnableToSetupException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Remonte l'erreur
	 * @param ex (Exception) déclencheur
	 */
	public UnableToSetupException(Exception ex) {
        super(ex);
    }
}
