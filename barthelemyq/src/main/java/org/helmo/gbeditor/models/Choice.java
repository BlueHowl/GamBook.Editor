package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.ChoiceNotValidException;
import org.helmo.gbeditor.models.utils.InputUtil;

/**
 * Classe choix
 */
public class Choice {

    private String text;

    private Page ref;

    /**
     * Constructeur de la classe choix
     * @param text (String) texte du choix
     * @param ref (Page) page redirigée par le choix
     * @throws ChoiceNotValidException
     */
    public Choice(String text, Page ref) throws ChoiceNotValidException {
        checkChoiceParameters(text, ref);

        this.text = text;
        this.ref = ref;
    }

    private void checkChoiceParameters(String text, Page ref) throws ChoiceNotValidException {
        if(InputUtil.isEmptyOrBlank(text)) {
            throw new ChoiceNotValidException("Le choix ne peut pas avoir un texte vide");
        } else if (ref == null) {
            throw  new ChoiceNotValidException("Le choix doit renvoyer sur une page valide");
        }
    }

    /**
     * Récupère le texte du choix
     * @return (String) texte
     */
    public String getText() {
        return text;
    }

    /**
     * Récupère la page redirigée par le choix
     * @return (Page)
     */
    public Page getRef() {
        return ref;
    }

    /**
     * Modifie le choix
     * @param text (String) nouveau texte
     * @param ref (Page) page à rediriger
     */
    public void modifyChoice(String text, Page ref) throws ChoiceNotValidException {
        checkChoiceParameters(text, ref);

        this.text = text;
        this.ref = ref;
    }
}
