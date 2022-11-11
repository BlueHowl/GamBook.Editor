package org.helmo.gbeditor.models.exceptions;

/**
 * Exception Page invalide
 */
public class PageNotValidException extends Exception{
    /**
     *Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     */
    public PageNotValidException(String errorMessage) {
        super(errorMessage);
    }
}