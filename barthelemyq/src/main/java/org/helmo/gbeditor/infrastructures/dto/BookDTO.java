package org.helmo.gbeditor.infrastructures.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Classe DTO Book
 */
public class BookDTO {

    @SerializedName("Title")
    public final String title;

    @SerializedName("Summary")
    public final String summary;

    @SerializedName("Author")
    public final String author;

    @SerializedName("Isbn")
    public final String isbn;

    @SerializedName("Published")
    public final boolean published;

    /**
     * Constructeur livre DTO
     * @param title (String) titre
     * @param summary (String) description
     * @param author (String) auteur
     * @param isbn (String) numéro isbn
     */
    public BookDTO(String title, String summary, String author, String isbn, boolean published) {
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
     * Récupère la valeur de publication du livre
     * @return (boolean)
     */
    public boolean isPublished() {
        return published;
    }
}
