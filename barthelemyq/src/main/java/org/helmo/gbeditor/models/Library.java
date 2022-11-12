package org.helmo.gbeditor.models;

import java.util.List;

/**
 * Classe bibliothéque de stockage de livres
 */
public class Library {

    private List<Book> books;

    /**
     * Constructeur de la bibliothéque
     * @param books (List<Book>) liste de livres
     */
    public Library(List<Book> books) {
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
     * Récupère un livre sur base de l'index donné
     * @param index (int)
     * @return (Book) livre
     */
    public Book getBookByIndex(int index) {
        return books.get(index);
    }

    /**
     * Ajoute un livre à la bibliothéque
     * @param book (Book) livre
     */
    public void addBook(Book book) {
        books.add(book);
    }
}
