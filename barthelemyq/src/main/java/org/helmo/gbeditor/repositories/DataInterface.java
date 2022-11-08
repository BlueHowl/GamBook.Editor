package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.models.Book;

import java.io.IOException;
import java.util.List;

/**
 * Interface repository
 */
public interface DataInterface {

    /**
     * Sauvegarde un livre
     * @param book (Book) Livre à sauvegarder
     * @throws
     */
    void saveBook(Book book) throws IOException;

    List<Book> getBooks() throws IOException;

    /**
     * Récupère le nombre de livres stockés
     * @return (int) nombre de livres
     */
    int getBookCount() throws IOException;

    /**
     * Défini l'id de l'utilisateur
     * @param id (String) id de l'utilisateur
     */
    void setUserId(String id);

}
