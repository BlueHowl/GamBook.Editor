package org.helmo.gbeditor.models;

import java.util.List;

public class Library {

    private List<Book> books;

    public Library(List<Book> books) {
        this.books = books;
    }


    public List<Book> getBooks() {
        return books;
    }

    public Book getBookByIndex(int index) {
        return books.get(index);
    }

    public void addBook(Book book) {
        books.add(book);
    }
}
