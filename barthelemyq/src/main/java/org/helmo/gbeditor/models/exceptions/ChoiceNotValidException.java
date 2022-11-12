package org.helmo.gbeditor.models.exceptions;

/**
 * Exception Choix invalide
 */
public class ChoiceNotValidException extends Exception{
    /**
     *Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     */
    public ChoiceNotValidException(String errorMessage) {
        super(errorMessage);
    }
}
