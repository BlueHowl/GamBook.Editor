package org.helmo.gbeditor.models.exceptions;

/**
 * Exception ISBN invalide
 */
public class IsbnNotValidException extends Exception {

    /**
     *Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     */
    public IsbnNotValidException(String errorMessage) {
        super(errorMessage);
    }

}
