package org.helmo.gbeditor.presenters.interfaces.views.subviews;

/**
 * Interface des sous vues
 */
public interface SubViewInterface {

    /**
     * Assigne le presentateur spécifique
     */
    void setPresenter();

    /**
     * Initialise la sous-vue
     */
    void refresh();

    /**
     * Modifie la visibilité de la vue
     * @param b (boolean)
     */
    void setVisibility(boolean b);
}
