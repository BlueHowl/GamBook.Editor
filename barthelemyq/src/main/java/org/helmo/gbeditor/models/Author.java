package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.AuthorNotValidException;
import org.helmo.gbeditor.models.utils.InputUtil;

/**
 * Classe auteur
 */
public class Author {
    private final String id;
    private final String surname;
    private final String name;

    /**
     * Constructeur de la classe auteur
     * @param id (String) matricule de l'auteur
     * @param surname (String) prénom de l'auteur
     * @param name (String) nom de l'auteur
     */
    public Author(String id, String surname, String name) throws AuthorNotValidException{
        if(InputUtil.isEmptyOrBlank(id) ||
            InputUtil.isEmptyOrBlank(surname) ||
            InputUtil.isEmptyOrBlank(name))
        {
            throw new AuthorNotValidException("Attention tout les champs doivent être remplis");
        }

        if(!InputUtil.isInBound(id, 6, 6)) {
            throw new AuthorNotValidException("Le matricule doit obligatoirement faire 6 chiffres !");
        }

        this.id = id;
        this.surname = surname.trim();
        this.name = name.trim();
    }

    /**
     * Récupère l'id de l'auteur (matricule)
     * @return (String) id (matricule)
     */
    public String getId() { return id; }

    /**
     * Récupère le prénom de l'auteur
     * @return (String) prénom
     */
    public String getSurname() { return surname; }

    /**
     * Récupère le nom de l'auteur
     * @return (String) nom
     */
    public String getName() { return name; }
}
