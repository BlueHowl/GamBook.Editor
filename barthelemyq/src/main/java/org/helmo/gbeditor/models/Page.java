package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.PageNotValidException;
import org.helmo.gbeditor.models.utils.InputUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


// Page contient des choix
// Numéro de page basé sur l'index dans la liste de page de book
// Choix contient une ref vers l'objet page auquel il renvoi
//
// Lors de la sauvegarde enregistrer book, liste de page avec fk bookid
// enregistrer choix avec fk pageid + convertir la ref vers l'objet en position dans la liste(numPage)
// (chercher la page dans la liste grace à la ref du choix et retourner l'index)
//
//Lors du load :
//attribuer les ref des pages correspondants à leur numéros pour les choix

/**
 * Classe page
 */
public class Page {

    private String text;

    private List<Choice> choices;

    private Isbn bookIsbn;

    /**
     * Constructeur de page
     * @param text (String) texte de la page
     * @param choices (List<Choice>) liste de choix
     * @param bookIsbn (Isbn) isbn du livre qui contient la page
     */
    public Page(String text, Collection<Choice> choices, Isbn bookIsbn) {
        if(InputUtil.isEmptyOrBlank(text)) {
            //TODO throw error
        }

        this.text = text;
        this.choices = (choices == null) ? new ArrayList<>() : new ArrayList<>(choices); //copie la liste
        this.bookIsbn = bookIsbn;
    }

    /**
     * Récupère le texte de la page
     * @return (String) texte de la page
     */
    public String getText() {
        return text;
    }

    /**
     * Récupère une liste des choix de la page
     * @return (List<Choice>) liste de choix
     */
    public List<Choice> getChoices() {
        return choices;
    }

    public Choice getChoiceByIndex(int index) {
        return choices.get(index);
    }

    /**
     * Récupère l'isbn du livre auquel appartient la page
     * @return (String) isbn
     */
    public String getBookIsbn() {
        return bookIsbn.getIsbn();
    }

    /**
     * Défini le texte de la page
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Ajoute un choix à la liste des choix
     * @param choice (Choice)
     */
    public void addChoice(Choice choice) {
        choices.add(choice);
    }

}
