package org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters;

/**
 * Interface du présentateur de la gestion de la création de livres
 */
public interface CreateBookPInterface {

    /**
     * Appel la méthode generateIsbn de IsbnUtil qui :
     * Génère le numéro ISBN, composé du code de langue 2, des 6 chiffres de l'id auteur (matricule), du numéro unique du livre et du code de vérification
     * @return (String) nouveau numéro ISBN
     */
    String generateIsbn();

    /**
     * Crée un livre
     * @param title (String) titre
     * @param summary (String) description
     * @param author (String) auteur
     * @param isbn (String) numéro isbn
     */
    void createBook(String title, String summary, String author, String isbn);
}
