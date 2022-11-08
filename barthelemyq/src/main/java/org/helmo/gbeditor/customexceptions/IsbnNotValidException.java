package org.helmo.gbeditor.customexceptions;

/**
 * Exception ISBN invalide
 */
public class IsbnNotValidException extends Exception{

    /**
     *Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     */
    public IsbnNotValidException(String errorMessage) {
        super(errorMessage);
    }

}
