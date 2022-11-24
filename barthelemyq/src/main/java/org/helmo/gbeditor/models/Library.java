package org.helmo.gbeditor.models;

import java.util.List;

/**
 * Classe bibliothéque de stockage de livres
 */
public class Library {

    private List<Book> books;

    private Book currentBook;

    /**
     * Assigne les livres à la bibliothéque
     * @param books (List<Book>)
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * Récupère la liste des livres de la bibliothéque
     * @return (List<Book>) liste de livres
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * Ajoute un livre à la bibliothéque
     * @param book (Book) livre
     */
    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * Défini le livre courant
     * @param selectedBook (int)
     */
    public void setCurrentBook(int selectedBook) {
        currentBook = books.get(selectedBook);
    }

    /**
     * Récupère le livre courant
     * @return (Book) livre
     */
    public Book getCurrentBook() {
        return currentBook;
    }

    /**
     * Récupère la page courante
     * @return (Page) page
     */
    public Page getCurrentPage() {
        return currentBook.getCurrentPage();
    }

    /**
     * Récupère le choix courant
     * @return (Choice) choix
     */
    public Choice getCurrentChoice() {
        return getCurrentPage().getCurrentChoice(); //todo demeter?
    }
}
