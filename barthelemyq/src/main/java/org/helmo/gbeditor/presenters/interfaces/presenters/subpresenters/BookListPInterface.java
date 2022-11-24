package org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters;

import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;

import java.util.List;

/**
 * Interface du présentateur de la gestion de la liste de livres
 */
public interface BookListPInterface {

    /**
     * Modifie le livre courant
     * @param title (String) titre
     * @param summary (String) description
     * @param isbn (String) numéro isbn
     */
    void modifyCurrentBook(String title, String summary, String isbn);

    /**
     * Récupère les livres de l'auteur
     * @return (List<Book>) liste de livres
     * */
    List<BookViewModel> getBooks();

    /**
     * Publie le livre
     */
    void publishBook();
}
