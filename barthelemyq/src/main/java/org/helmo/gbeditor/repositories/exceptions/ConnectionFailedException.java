package org.helmo.gbeditor.repositories.exceptions;


/**
 * Exception de connexion de Mr Ludewig
 */
public class ConnectionFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Remonte l'erreur
	 * @param s (String) détails
	 * @param ex (Exception) déclencheur
	 */
	public ConnectionFailedException(String s, Exception ex) {
        super(s, ex);
    }
}
