package org.helmo.gbeditor.repositories.exceptions;

/**
 * Exception de driver non trouvés de Mr Ludewig
 */
public class JdbcDriverNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Remonte l'erreur
	 * @param driverName (String) nom du driver
	 */
	public JdbcDriverNotFoundException(String driverName) {
        super("Unable to load driver "+driverName+". Is it available from the class path?");
    }
}
