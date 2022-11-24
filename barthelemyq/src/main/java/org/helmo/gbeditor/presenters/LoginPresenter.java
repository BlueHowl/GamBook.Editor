package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.exceptions.AuthorNotValidException;
import org.helmo.gbeditor.models.*;
import org.helmo.gbeditor.presenters.interfaces.presenters.LoginPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.ViewInterface;
import org.helmo.gbeditor.repositories.DataInterface;
import org.helmo.gbeditor.repositories.StorageFactoryInterface;
import org.helmo.gbeditor.repositories.exceptions.NotRetrievedException;
import org.helmo.gbeditor.repositories.exceptions.NotSavedException;

/**
 * Présentateur Login
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public class LoginPresenter implements LoginPresenterInterface {

    private ViewInterface view;

    private final MainPresenterInterface mainPresenter;

    private final StorageFactoryInterface factory;

    /**
     * Constructeur du présentateur
     * @param factory (StorageFactoryInterface)
     * @param mainPresenter (MainPresenterInterface) presentateur principal
     */
    public LoginPresenter(StorageFactoryInterface factory, MainPresenterInterface mainPresenter) {
        this.factory = factory;
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
        try(DataInterface repository = factory.newStorageSession()) {
            Author author = new Author(id, surname, name);

            repository.connectAuthor(author);

            mainPresenter.showMainView(author);
        } catch (AuthorNotValidException | NotRetrievedException | NotSavedException e) {
            view.displayMessage(e.getMessage());
        } catch (Exception e) {
            view.displayErrorMessage("Impossible de se connecter");
        }

    }

    /**
     * Renseigne une vue au presentateur
     * @param view (LoginViewInterface)
     */
    @Override
    public void setView(ViewInterface view) {
        this.view = view;
    }

}

