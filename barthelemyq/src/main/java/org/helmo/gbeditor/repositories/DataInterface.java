package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.ChoiceNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.models.exceptions.PageNotValidException;
import org.helmo.gbeditor.repositories.exceptions.NotRetrievedException;
import org.helmo.gbeditor.repositories.exceptions.NotSavedException;

import java.util.List;

/**
 * Interface repository
 */
public interface DataInterface extends AutoCloseable{

    /**
     * Initialise le stockage
     */
    void setup();

    /**
     * Nettoie le stockage
     */
    void tearDown();

    /**
     * Sauvegarde un livre
     * @param book (Book) Livre à sauvegarder
     * @throws NotSavedException
     */
    void saveBook(Book book) throws NotSavedException;

    /**
     * Met à jour le livre
     * @param book (Book) Livre à mettre à jour
     * @throws NotSavedException
     */
    void updateBook(Book book) throws NotSavedException;

    /**
     * Publie le livre
     * @param book (Book)
     * @throws NotSavedException
     */
    void publishBook(Book book) throws NotSavedException;

    /**
     * Sauvegarde des pages d'un livre
     * @param book (Book) livre
     * @throws NotSavedException
     */
    void saveChanges(Book book) throws NotSavedException;

    /**
     * Récupère la liste des livres de l'auteur
     * @return (List<Book>) liste de livre
     * @throws NotRetrievedException
     * @throws BookNotValidException
     * @throws IsbnNotValidException
     */
    List<Book> retrieveBooks(String authorId) throws NotRetrievedException, BookNotValidException, IsbnNotValidException;

    /**
     * Récupère et assigne la liste de pages du livre
     * @param book (Book) Livre
     * @return (List<Page>) liste de pages
     * @throws NotRetrievedException
     * @throws IsbnNotValidException
     * @throws PageNotValidException
     * @throws ChoiceNotValidException
     */
    List<Page> getBookPages(Book book) throws NotRetrievedException, PageNotValidException, ChoiceNotValidException;

    /**
     * Récupère le nombre de livres stockés
     * @param authorCode (String) code de l'auteur
     * @return (int) nombre de livres
     * @throws NotRetrievedException
     */
    int getBookCount(String authorCode) throws NotRetrievedException;

    /**
     * vérifie l'auteur et le crée si pas existant
     * @param author (Author)
     */
    void connectAuthor(Author author) throws NotRetrievedException, NotSavedException;
}
