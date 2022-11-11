package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Isbn;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.repositories.exceptions.ElementNotFoundException;
import org.helmo.gbeditor.repositories.exceptions.UnableToSaveException;

import java.io.IOException;
import java.util.List;

/**
 * Interface repository
 */
public interface DataInterface { //TODO changer les ioException en exceptions custom

    /**
     * Sauvegarde un livre
     * @param book (Book) Livre à sauvegarder
     * @throws
     */
    void saveBook(Book book) throws UnableToSaveException;

    /**
     * Sauvegarde des pages d'un livre
     * @param pages (List<Page>) liste des pages
     */
    void savePages(List<Page> pages) throws UnableToSaveException;

    /**
     * Récupère la liste des livres de l'auteur
     * @return (List<Book>) liste de livre
     * @throws IOException
     */
    List<Book> retrieveBooks(String authorId) throws ElementNotFoundException, BookNotValidException, IsbnNotValidException;

    /**
     * Récupère la liste de pages du livre
     * @param isbn (Isbn) numéro isbn du livre
     * @return (List<Page>) liste de pages
     * @throws IOException
     */
    List<Page> getBookPages(String isbn) throws ElementNotFoundException, IsbnNotValidException;

    /**
     * Récupère le nombre de livres stockés
     * @return (int) nombre de livres
     */
    int getBookCount(String authorId) throws ElementNotFoundException;

}
