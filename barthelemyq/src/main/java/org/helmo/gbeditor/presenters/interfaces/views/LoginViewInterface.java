package org.helmo.gbeditor.presenters.interfaces.views;

import org.helmo.gbeditor.presenters.interfaces.presenters.LoginPresenterInterface;

public interface LoginViewInterface extends ViewInterface{

    /**
     * Renseigne un presenter à la vue
     * @param presenter (LoginPresenterInterface)
     */
    void setPresenter(LoginPresenterInterface presenter);

}
