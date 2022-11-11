package org.helmo.gbeditor.presenters.viewmodels;

import org.helmo.gbeditor.models.utils.InputUtil;

/**
 * Classe de stockage de données livre pour les vues
 */
public class ChoiceViewModel {

    public String text;

    public int refPageNumber;

    /**
     * Constructeur de page
     * @param text (String) texte de la page
     */
    public ChoiceViewModel(String text, int refPageNumber) {
        if(InputUtil.isEmptyOrBlank(text)) {
            //TODO throw error
        }

        this.text = text;
        this.refPageNumber = refPageNumber;
    }

    /**
     * Récupère le texte de la page
     * @return (String) texte de la page
     */
    public String getText() {
        return text;
    }

    /**
     * Récupère le numéro de la page
     * @return (int) numéro de page
     */
    public int getRefPageNumber() {
        return refPageNumber;
    }
}
