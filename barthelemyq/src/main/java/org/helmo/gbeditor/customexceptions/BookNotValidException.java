package org.helmo.gbeditor.customexceptions;

/**
 * Exception livre invalide
 */
public class BookNotValidException extends Exception{

    /**
     *Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     */
    public BookNotValidException(String errorMessage) {
        super(errorMessage);
    }
}
