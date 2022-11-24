package org.helmo.gbeditor.presenters.interfaces.views;

/**
 * Interface mère des vue
 */
public interface ViewInterface {

    /**
     * Affiche un message
     * @param msg (String) message
     */
    void displayMessage(String msg);

    /**
     * Affiche un message d'erreur
     * @param msg (String) message
     */
    void displayErrorMessage(String msg);
}
