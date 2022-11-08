package org.helmo.gbeditor.presenters.interfaces.views;

import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;

/**
 * Interface vue Principale
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public interface MainViewInterface extends ViewInterface{

    /**
     * Renseigne un presenter à la vue
     * @param presenter (MainPresenterInterface)
     */
    void setPresenter(MainPresenterInterface presenter);

    /**
     * Affiche la vue
     */
    void showView();

    /**
     * Change de menu dans la vue principale
     * @param id (int) identifiant du panel
     */
    void switchPane(int id);

}
