package org.helmo.gbeditor.presenters.interfaces.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.SubPresenterInterface;

/**
 * Interface du presentateur principal
 */
public interface MainPresenterInterface {

    /**
     * Récupère le nom et prénom de l'auteur
     * @return (String) nom prénom
     */
    String getAuthorInfos();

    /**
     * Retourne le code de vérification de l'isbn
     * @param isbnBegining (String)
     * @return (String) code de vérification isbn
     */
    String getIsbnVerif(String isbnBegining);

    /**
     * Change de vue en fonction de l'id
     * @param id (int)
     */
    void switchPane(int id);

    /**
     * Affiche la vue
     */
    void showMainView(Author author);

    /**
     * Renseigne une vue au presentateur
     * @param view (MainViewInterface)
     */
    void setView(MainViewInterface view);

    /**
     * Récupère l'interface générique du sous presenter à l'id donné
     * @param id (int)
     * @return (SubPresenterInterface) sub
     */
    SubPresenterInterface getSubPresenters(int id);

    /**
     * Défini le livre courant
     * @param selectedBook (int)
     */
     void setCurrentBook(int selectedBook);

    /**
     * Défini la page courante
     * @param selectedPage (int)
     */
    void setCurrentPage(int selectedPage);

    /**
     * Défini le choix courant
     * @param selectedChoice (int)
     */
    void setCurrentChoice(int selectedChoice);
}
