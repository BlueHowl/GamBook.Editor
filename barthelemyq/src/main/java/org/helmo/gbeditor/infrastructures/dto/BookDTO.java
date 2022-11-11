package org.helmo.gbeditor.infrastructures.dto;

import com.google.gson.annotations.SerializedName;
import org.helmo.gbeditor.models.Page;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Constructeur livre DTO
     * @param title (String) titre
     * @param summary (String) description
     * @param author (String) auteur
     * @param isbn (String) numéro isbn
     */
    public BookDTO(String title, String summary, String author, String isbn) {
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.isbn = isbn;
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

}
