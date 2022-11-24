package org.helmo.gbeditor.presenters.interfaces.views;

/**
 * Interface vue Principale
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public interface MainViewInterface extends ViewInterface{

    /**
     * Affiche la vue
     * @param authorInfos (String) nom prenom de l'auteur
     */
    void showView(String authorInfos);

    /**
     * Change de menu dans la vue principale
     * @param id (int) identifiant du panel
     */
    void switchPane(int id);

    /**
     * Rafraichis la sous-vue selon son identifiant
     * @param id (int)
     */
    void refreshSubView(int id);

    /**
     * Affiche un popup de demande de confirmation de suppression de page
     * @param text (String) texte du popup
     */
    boolean confirmDialog(String text);

}
