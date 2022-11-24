package org.helmo.gbeditor.presenters.subpresenters;

import org.helmo.gbeditor.models.Choice;
import org.helmo.gbeditor.models.Library;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.models.exceptions.ChoiceNotValidException;
import org.helmo.gbeditor.models.exceptions.PageNotValidException;
import org.helmo.gbeditor.presenters.ViewModelMapper;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.ChoiceGestionPInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.SubPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.ChoiceViewModel;

import java.util.List;

/**
 * Classe sous présentateur ChoiceGestionPresenter
 */
public class ChoiceGestionPresenter implements SubPresenterInterface, ChoiceGestionPInterface {

    private final MainViewInterface view;

    private final Library library;


    /**
     * Constructeur de la classe ChoiceGestionPresenter
     * @param view (MainViewInterface)
     * @param library (Library)
     */
    public ChoiceGestionPresenter(MainViewInterface view, Library library) {
        this.view = view;
        this.library = library;
    }

    /**
     * Récupère les choix de la page courante
     * @return (List<ChoiceViewModel>) liste de choix
     */
    @Override
    public List<ChoiceViewModel> getPageChoices() {
        return ViewModelMapper.ChoiceToViewModel(library.getCurrentPage().getChoices(), library.getCurrentBook().getPages()); //todo demeter?
    }

    /**
     * Ajoute un choix à la page courante
     * @param text (String) texte du choix
     * @param refPageNum (String) numéro de la page liée
     */
    @Override
    public void addChoiceToCurrentPage(String text, String refPageNum) {
        Page page = library.getCurrentPage();

        if(page == null) {
            view.displayMessage("Impossible d'ajouter un choix, aucune page sélectionnée");
            return;
        }

        try {
            Page refPage = library.getCurrentBook().getPageByIndex(Integer.parseInt(refPageNum)-1); //todo demeter?
            if(page.equals(refPage)) { //compare reference
                view.displayMessage("Impossible d'ajouter un choix qui redirige sur sa propre page");
                return;
            }

            page.addChoice(new Choice(text, refPage));
            view.refreshSubView(3);
        } catch (PageNotValidException | ChoiceNotValidException e) {
            view.displayMessage(e.getMessage());
        } catch (NumberFormatException e) {
            view.displayMessage("le numéro de page à rediriger est invalide");
        }
    }

    /**
     * Modifie le choix du livre à l'index donné
     * @param text (String)
     * @param refPageNum (String)
     */
    @Override
    public void modifyChoiceOfCurrentPage(String text, String refPageNum) {
        Choice choice = library.getCurrentChoice();

        if(choice == null) {
            view.displayMessage("Impossible de modifier le choix, aucun choix sélectionné");
            return;
        }

        try {
            Page refPage = library.getCurrentBook().getPageByIndex(Integer.parseInt(refPageNum)-1); //todo demeter
            if(library.getCurrentPage().equals(refPage)) { //compare reference
                view.displayMessage("Impossible de modifier un choix qui redirige sur sa propre page");
                return;
            }

            choice.modifyChoice(text, refPage);
            view.refreshSubView(3);
        } catch (PageNotValidException | ChoiceNotValidException e) {
            view.displayMessage(e.getMessage());
        } catch (NumberFormatException e) {
            view.displayMessage("le numéro de page à rediriger est invalide");
        }
    }

    /**
     * Supprime le choix courant
     */
    @Override
    public void removeCurrentChoice() {
        Choice choice = library.getCurrentChoice();

        if(choice == null) {
            view.displayMessage("Impossible de supprimer le choix, aucun choix sélectionné");
            return;
        }

        library.getCurrentPage().removeChoice(choice); //todo demeter

        view.refreshSubView(3);
    }
}
