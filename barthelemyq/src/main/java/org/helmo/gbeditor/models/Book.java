package org.helmo.gbeditor.models;

import org.helmo.gbeditor.customexceptions.BookNotValidException;
import org.helmo.gbeditor.utils.InputUtil;

import java.util.List;

/**
 * Classe Livre
 */
public class Book {

    private final String title;

    private final String summary;

    private final String author;

    private final Isbn isbn;

    private List<Page> pages;

    /**
     * Constructeur de la classe livre
     * @param title (String) titre du livre
     * @param summary (String) résumé du livre
     * @param author (String) nom et prénom de l'auteur
     * @param isbn (Isbn) numéro isbn unique du livre
     * @throws BookNotValidException si les données du livre récupéré sont incorrectes
     */
    public Book(String title, String summary, String author, Isbn isbn) throws BookNotValidException {
        if(InputUtil.isEmptyOrBlank(title) ||
            InputUtil.isEmptyOrBlank(summary) ||
            InputUtil.isEmptyOrBlank(author))
        {
            throw new BookNotValidException("Tous les champs doivent être remplis");
        }

        if(!InputUtil.isInBound(title, 0, 150)) {
            throw new BookNotValidException("Le titre ne peut pas faire plus que 150 caractères");
        }

        if(!InputUtil.isInBound(summary, 0, 500)) {
            throw new BookNotValidException("La description ne peut pas faire plus que 500 caractères");
        }

        this.title = title.trim();
        this.summary = summary.trim();
        this.author = author;
        this.isbn = isbn;
    }

    /**
     * Récupère le titre du livre
     * @return (String) titre
     */
    public String getTitle() {
        return title;
    }

    /**
     * Récupère le résumé du livre
     * @return (String) résumé
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Récupère le nom et prénom de l'auteur du livre
     * @return (String) nom et prénom de l'auteur
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Récupère le numéro isbn du livre
     * @return (String) numéro isbn du livre
     */
    public String getIsbn() {
        return isbn.getIsbn();
    }

}
