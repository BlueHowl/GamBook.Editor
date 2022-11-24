package org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters;

import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;

import java.util.List;

/**
 * Interface du présentateur de gestion des pages
 */
public interface PageGestionPInterface {

    /**
     * Sauvegarde les pages et choix du livre
     */
    void saveBookPages();

    /**
     * Récupère les pages du livre courant
     * @return (List<PageViewModel>) liste de pages
     */
    List<PageViewModel> getBookPages();

    /**
     * Ajoute une page au livre courant
     * @param pageNum (String) numéro de la page
     * @param text (String) texte de la page
     * @param shift (int) décalage (-1) pour ajouter, 0 pour ajouter l'élement à la suite
     */
    void addPageToCurrentBook(String pageNum, String text, int shift);

    /**
     * Modifie la page du livre à l'index donné
     * @param index (String) numéro de la page
     * @param text (String)
     */
    void modifyPageOfCurrentBook(String index, String text);

    /**
     * Supprime la page courante
     */
    void safeRemoveCurrentPage();
}
