package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.PageNotValidException;
import org.helmo.gbeditor.models.utils.InputUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe page
 */
public class Page {

    private String text;

    private final List<Choice> choices;

    private Choice currentChoice;

    /**
     * Constructeur de page
     * @param text (String) texte de la page
     * @param choices (List<Choice>) liste de choix
     */
    public Page(String text, Collection<Choice> choices) throws PageNotValidException {
        checkPageParameters(text);

        this.text = text;
        this.choices = (choices == null) ? new ArrayList<>() : new ArrayList<>(choices); //copie la liste
    }

    private void checkPageParameters(String text) throws PageNotValidException {
        if(InputUtil.isEmptyOrBlank(text)) {
            throw new PageNotValidException("La page ne peut pas avoir un texte vide");
        }
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

    /**
     * Récupère un choix sur base de son index
     * @param index (int)
     * @return (Choice)
     */
    public Choice getChoiceByIndex(int index) {
        return choices.get(index);
    }

    /**
     * Défini le choix courant
     * @param selectedChoice (int)
     */
    public void setCurrentChoice(int selectedChoice) {
        currentChoice = getChoiceByIndex(selectedChoice);
    }

    /**
     * Récupère le choix courant
     * @return (Choice)
     */
    public Choice getCurrentChoice() {
        return currentChoice;
    }

    /**
     * Défini le texte de la page
     * @param text
     */
    public void setText(String text) throws PageNotValidException {
        checkPageParameters(text);

        this.text = text;
    }

    /**
     * Ajoute un choix à la liste des choix
     * @param choice (Choice)
     */
    public void addChoice(Choice choice) {
        choices.add(choice);
    }

    /**
     * Supprime le choix de la liste
     * @param choice (Choice)
     */
    public void removeChoice(Choice choice) {
        choices.remove(choice);
    }

}
