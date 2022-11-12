package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.ChoiceNotValidException;
import org.helmo.gbeditor.models.utils.InputUtil;

public class Choice {

    private String text;

    private Page ref;

    public Choice(String text, Page ref) throws ChoiceNotValidException {
        if(InputUtil.isEmptyOrBlank(text)) {
            throw new ChoiceNotValidException("Le choix ne peut pas avoir un texte vide");
        } else if (ref == null) {
            throw  new ChoiceNotValidException("Le choix doit renvoyer sur une page valide");
        }

        this.text = text;
        this.ref = ref;
    }

    public String getText() {
        return text;
    }

    public Page getRef() {
        return ref;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRef(Page ref) {
        this.ref = ref;
    }
}
