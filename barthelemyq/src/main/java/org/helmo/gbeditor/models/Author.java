package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.AuthorNotValidException;
import org.helmo.gbeditor.models.utils.InputUtil;

/**
 * Classe auteur
 */
public class Author {
    private final String code;
    private final String surname;
    private final String name;

    /**
     * Constructeur de la classe auteur
     * @param code (String) matricule de l'auteur
     * @param surname (String) prénom de l'auteur
     * @param name (String) nom de l'auteur
     * @throws AuthorNotValidException
     */
    public Author(String code, String surname, String name) throws AuthorNotValidException{
        checkAuthorParameters(code, surname, name);

        this.code = code;
        this.surname = surname.trim();
        this.name = name.trim();
    }

    /**
     * Vérifie les valeurs de l'auteur
     * @param code (String) matricule de l'auteur
     * @param surname (String) prénom de l'auteur
     * @param name (String) nom de l'auteur
     * @throws AuthorNotValidException
     */
    private void checkAuthorParameters(String code, String surname, String name) throws AuthorNotValidException {
        if(InputUtil.isEmptyOrBlank(code) ||
                InputUtil.isEmptyOrBlank(surname) ||
                InputUtil.isEmptyOrBlank(name))
        {
            throw new AuthorNotValidException("Attention tout les champs doivent être remplis");
        }

        if(!InputUtil.isInBound(code, 6, 6)) {
            throw new AuthorNotValidException("Le matricule doit obligatoirement faire 6 chiffres !");
        }
    }

    /**
     * Récupère l'id de l'auteur (matricule)
     * @return (String) id (matricule)
     */
    public String getCode() { return code; }

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

    /**
     * Récupère le nom et prénom de l'auteur
     * @return (String) prenom nom
     */
    public String getAuthorInfos() {
        return getSurname() + " " + getName();
    }
}
