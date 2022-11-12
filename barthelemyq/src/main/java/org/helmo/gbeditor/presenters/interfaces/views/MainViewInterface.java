package org.helmo.gbeditor.presenters.interfaces.views;

import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;

/**
 * Interface vue Principale
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public interface MainViewInterface extends ViewInterface{

    /**
     * Affiche la vue
     */
    void showView();

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

}
