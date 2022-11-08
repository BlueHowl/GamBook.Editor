package org.helmo.gbeditor.models;

import org.helmo.gbeditor.utils.InputUtil;

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

public class Page {

    private String text;

    private List<Choice> choices; //List ArrayList pour ajouter des elements au milieu

    public Page(String text, Collection<Choice> choices) {
        if(InputUtil.isEmptyOrBlank(text)) {
            //TODO throw error
        }

        this.text = text;
        this.choices = (choices == null || choices.isEmpty()) ? new ArrayList<>() : new ArrayList<>(choices); //copie la liste
    }

    public String getText() {
        return text;
    }

    public List<Choice> getChoices() {
        return choices;
    }
}
