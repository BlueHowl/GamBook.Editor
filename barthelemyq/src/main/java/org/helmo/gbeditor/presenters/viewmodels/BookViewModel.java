package org.helmo.gbeditor.presenters.viewmodels;

/**
 * Classe de stockage de données livre pour les vues
 */
public class BookViewModel {

    public final String title;

    public final String summary;

    public final String author;

    public final String isbn;

    public final boolean published;

    /**
     * Constructeur livre pour les vues
     * @param title (String) titre
     * @param summary (String) description
     * @param author (String) auteur
     * @param isbn (String) numéro isbn
     */
    public BookViewModel(String title, String summary, String author, String isbn, boolean published) {
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.isbn = isbn;
        this.published = published;
    }

    /**
     * Récupère le titre
     * @return (String) titre
     */
    public String getTitle() {
        return title;
    }

    /**
     * Récupère la description
     * @return (String) description
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Récupère l'auteur
     * @return (String) auteur
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Récupère le numéro isbn
     * @return (String) numéro isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Récupère la valeur de publication
     * @return (boolean)
     */
    public boolean isPublished() {
        return published;
    }
}
