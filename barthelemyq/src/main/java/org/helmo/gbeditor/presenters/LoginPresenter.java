package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.customexceptions.AuthorNotValidException;
import org.helmo.gbeditor.models.*;
import org.helmo.gbeditor.presenters.interfaces.presenters.LoginPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.LoginViewInterface;
import org.helmo.gbeditor.repositories.DataInterface;
import org.helmo.gbeditor.utils.InputUtil;

/**
 * Présentateur Login
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public class LoginPresenter implements LoginPresenterInterface {

    private LoginViewInterface view;

    private MainPresenterInterface mainPresenter;

    private final DataInterface repository;

    /**
     * Constructeur du présentateur
     * @param view (ViewInterface) Vue
     */
    public LoginPresenter(LoginViewInterface view, DataInterface repository, MainPresenterInterface mainPresenter) {
        //this.editor = editor;
        this.view = view;
        view.setPresenter(this);

        this.repository = repository;
        this.mainPresenter = mainPresenter;
    }

    /**
     * Méthode d'enregistrement d'auteur : crée un auteur et affiche l'espace de travail si les champs ont bien été remplis
     * @param id (matricule)
     * @param surname (prénom)
     * @param name (nom)
     */
    @Override
    public void login(String id, String surname, String name) {
        try {
            Author author = new Author(id, surname, name);

            repository.setFileName(author.getId());
            mainPresenter.showView(author);
        } catch (AuthorNotValidException e) {
            view.displayMessage(e.getMessage());
        }

    }

}

