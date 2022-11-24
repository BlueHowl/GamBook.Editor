package org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters;

import org.helmo.gbeditor.presenters.viewmodels.ChoiceViewModel;

import java.util.List;

/**
 * Interface du présentateur de la gestion des choix
 */
public interface ChoiceGestionPInterface {

    /**
     * Récupère les choix de la page courante
     * @return (List<ChoiceViewModel>) liste de choix
     */
    List<ChoiceViewModel> getPageChoices();

    /**
     * Ajoute un choix à la page courante
     * @param text (String) texte du choix
     * @param refPageNum (String) numéro de la page liée
     */
    void addChoiceToCurrentPage(String text, String refPageNum);

    /**
     * Modifie le choix du livre à l'index donné
     * @param text (String)
     * @param refPageNum (String)
     */
    void modifyChoiceOfCurrentPage(String text, String refPageNum);

    /**
     * Supprime le choix courant
     */
    void removeCurrentChoice();
}
