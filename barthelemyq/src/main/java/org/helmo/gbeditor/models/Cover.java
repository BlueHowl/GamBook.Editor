package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.utils.InputUtil;

/**
 * Classe couverture de livre
 */
public class Cover {

    private final String title;

    private final String summary;

    private final String author;

    private final Isbn isbn;


    /**
     * Constructeur de la couverture d'un livre
     * @param title (String)
     * @param summary (String)
     * @param author (String)
     * @param isbn (Isbn)
     * @throws BookNotValidException
     */
    public Cover(String title, String summary, String author, Isbn isbn) throws BookNotValidException {
        checkCoverParameters(title, summary, author);

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
    private void checkCoverParameters(String title, String summary, String author) throws BookNotValidException{
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
}
