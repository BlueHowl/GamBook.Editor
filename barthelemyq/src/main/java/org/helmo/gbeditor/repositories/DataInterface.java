package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.models.Book;

import java.io.IOException;

/**
 * Interface repository
 */
public interface DataInterface {

    /**
     * Sauvegarde un livre
     * @param book (Book) Livre à sauvegarder
     * @throws
     */
    void saveBook(Book book) throws IOException;

    /**
     * Récupère le nombre de livres stockés
     * @return (int) nombre de livres
     */
    int getBookCount() throws IOException;

    /**
     * Défini le nom du fichier de sauvegarde (ici : matricule auteur)
     * @param name (String) nom du fichier
     */
    void setFileName(String name);

}
