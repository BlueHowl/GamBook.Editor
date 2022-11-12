package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.models.exceptions.PageNotValidException;
import org.helmo.gbeditor.models.utils.InputUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Livre
 */
public class Book {

    private String title;

    private String summary;

    private final String author;

    private Isbn isbn;

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
        checkBookParameters(title, summary, author);

        this.title = title.trim();
        this.summary = summary.trim();
        this.author = author;
        this.isbn = isbn;
    }

    /**
     * Vérifie les valeurs du livre
     * @param title (String) titre
     * @param summary (String) description
     * @param author (String) nom et prenom de l'auteur
     * @throws BookNotValidException si les données du livre récupéré sont incorrectes
     */
    private void checkBookParameters(String title, String summary, String author) throws BookNotValidException{
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

    /**
     * Récupère les pages du livre
     * @return (List<Page>) liste de pages
     */
    public List<Page> getPages() {
        return pages;
    }

    /**
     * Récupère la page sur base de l'index
     * @return (Page)
     */
    public Page getPageByIndex(int index) throws PageNotValidException {
        try {
            return pages.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new PageNotValidException("La page au numéro donné n'existe pas");
        }
    }

    /**
     * Défini la liste des pages du livre
     * @param pages (List<Page>) liste de pags
     */
    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    /**
     * Ajoute une page à l'index donné
     * Assigne la premiere page si pas encore de pages
     * @param index (int) index (numéro page - 1)
     * @param page (Page)
     */
    public void setPageAtIndex(int index, Page page) {
        if(pages.isEmpty()) {
            pages.add(page);
        } else {
            pages.add(index, page);
        }
    }

    /**
     * modifie la page à l'index donné
     * @param index (int)
     * @param text (String)
     */
    public void modifyPageAtIndex(int index, String text) throws PageNotValidException {
        if(!pages.isEmpty()) {
            pages.get(index).setText(text);
        } else {
            throw new PageNotValidException("Impossible de modifier, aucune page sélectionnée");
        }
    }

    public void removePage(Page page) {
        pages.remove(page);
    }

    /**
     * Modifie le livre
     * @param title (String) titre
     * @param summary (String) description
     * @param isbn (String) numéro isbn
     * @throws BookNotValidException
     * @throws IsbnNotValidException
     */
    public void modifyBook(String title, String summary, String isbn) throws BookNotValidException, IsbnNotValidException {
        checkBookParameters(title, summary, this.getAuthor());

        this.isbn = new Isbn(isbn);
        this.title = title.trim();
        this.summary = summary.trim();
    }
}
