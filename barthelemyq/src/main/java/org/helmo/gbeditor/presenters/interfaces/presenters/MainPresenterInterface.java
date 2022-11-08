package org.helmo.gbeditor.presenters.interfaces.presenters;

import org.helmo.gbeditor.models.Author;

public interface MainPresenterInterface {

    /**
     * Appel les méthodes getSurname et getName de User qui :
     * Récupère le nom et prénom concaténé de l'auteur
     * @return (String) nom prénom de l'auteur
     */
    String getUserInfos();

    /**
     * Appel la méthode generateIsbn de IsbnUtil qui :
     * Génère le numéro ISBN, composé du code de langue 2, des 6 chiffres de l'id auteur (matricule), du numéro unique du livre et du code de vérification
     * @return (String) nouveau numéro ISBN
     */
    String generateIsbn();

    /**
     * Retourne le code de vérification de l'isbn
     * @param isbnBegining (String)
     * @return (String) code de vérification isbn
     */
    String getIsbnVerif(String isbnBegining);

    /**
     * Crée un livre et le sauvegarde
     * @param title (String) titre
     * @param summary (String) description
     * @param author (String) auteur
     * @param isbn (String) numéro isbn
     */
    void createBook(String title, String summary, String author, String isbn);

    /**
     * Affiche la vue
     */
    void showView(Author author);

}
