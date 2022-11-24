package org.helmo.gbeditor.presenters.subpresenters;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Choice;
import org.helmo.gbeditor.models.Library;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.models.exceptions.ChoiceNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.models.exceptions.PageNotValidException;
import org.helmo.gbeditor.presenters.ViewModelMapper;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.PageGestionPInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.SubPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;
import org.helmo.gbeditor.repositories.DataInterface;
import org.helmo.gbeditor.repositories.StorageFactoryInterface;
import org.helmo.gbeditor.repositories.exceptions.NotRetrievedException;
import org.helmo.gbeditor.repositories.exceptions.NotSavedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe sous présentateur PageGestionPresenter
 */
public class PageGestionPresenter implements SubPresenterInterface, PageGestionPInterface {

    private final StorageFactoryInterface factory;

    private final MainViewInterface view;

    private final Library library;


    /**
     * Constructeur de la classe PageGestionPresenter
     * @param factory (StorageFactoryInterface)
     * @param view (MainViewInterface)
     * @param library (Library)
     */
    public PageGestionPresenter(StorageFactoryInterface factory, MainViewInterface view, Library library) {
        this.factory = factory;
        this.view = view;
        this.library = library;
    }

    /**
     * Sauvegarde les pages et choix du livre
     */
    @Override
    public void saveBookPages() {
        try(DataInterface repository = factory.newStorageSession()) {
            repository.saveChanges(library.getCurrentBook());
        } catch (NotSavedException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (Exception e) {
            view.displayErrorMessage("Un problème est survenu lors de la sauvegarde des pages");
        }
    }

    /**
     * Ajoute une page au livre courant
     * @param pageNum (String) numéro de la page
     * @param text (String) texte de la page
     * @param shift (int) décalage (-1) pour ajouter, 0 pour ajouter l'élement à la suite
     */
    @Override
    public void addPageToCurrentBook(String pageNum, String text, int shift) {
        try {
            library.getCurrentBook().setPageAtIndex(Integer.parseInt(pageNum) + shift, new Page(text, null)); //todo demeter?
            view.refreshSubView(2);
        } catch (PageNotValidException e) {
            view.displayMessage(e.getMessage());
        } catch (NumberFormatException e) {
            view.displayMessage("le numéro de page est invalide");
        }
    }

    /**
     * Modifie la page du livre à l'index donné
     * @param index (String) numéro de la page
     * @param text (String)
     */
    @Override
    public void modifyPageOfCurrentBook(String index, String text) {
        try {
            library.getCurrentBook().modifyPageAtIndex(Integer.parseInt(index) - 1, text); //todo demeter?
            view.refreshSubView(2);
        } catch (PageNotValidException e) {
            view.displayMessage(e.getMessage());
        } catch (NumberFormatException e) {
            view.displayMessage("le numéro de page est invalide");
        }
    }

    /**
     * Supprime la page courante
     */
    @Override
    public void safeRemoveCurrentPage() {
        if(library.getCurrentPage() == null) {
            view.displayMessage("Impossible de supprimer une page, aucune page sélectionnée");
            return;
        }

        Map<Choice, Page> involvedChoices = checkCurrentPageRefs();
        if(involvedChoices.size() > 0) {
            if(view.confirmDialog(involvedChoices.size() + " choix renvoient sur cette page, voulez vous les supprimer ?")) {
                RemoveCurrentPage(involvedChoices);
            }
        } else {
            RemoveCurrentPage(new HashMap<>());
        }
    }

    /**
     * Supprime la page courante et les choix impliqués
     * @param involvedChoices (Map<Choice, Page>)
     */
    private void RemoveCurrentPage(Map<Choice, Page> involvedChoices) {
        Page page = library.getCurrentPage();

        if(page == null) {
            view.displayMessage("Impossible de supprimer la page, aucune page sélectionnée");
            return;
        }

        library.getCurrentBook().removePage(page); //todo demeter?

        for (Map.Entry<Choice, Page> entry : involvedChoices.entrySet()) {
            removeInvolvedChoices(entry.getKey(), entry.getValue());
        }

        view.refreshSubView(2);
        view.refreshSubView(3);
    }

    /**
     * Supprime le choix donné à la page donnée
     * @param choice (Choice)
     * @param page (Page)
     */
    private void removeInvolvedChoices(Choice choice, Page page) {
        page.removeChoice(choice);
    }

    /**
     * Vérifie si des choix renvoient sur la page
     * @return (Map<Choice, Page>) liste des choix impliqués
     */
    private Map<Choice, Page> checkCurrentPageRefs() {
        Map<Choice, Page> choices = new HashMap<>();
        for(Page page : library.getCurrentBook().getPages()) { //todo demter?
            for(Choice choice : page.getChoices()) {
                if(library.getCurrentPage() == choice.getRef()) {
                    choices.put(choice, page);
                }
            }
        }

        return choices;
    }

    /**
     * Récupère les pages du livre courant
     * @return (List<PageViewModel>) liste de pages
     */
    @Override
    public List<PageViewModel> getBookPages() {
        List<Page> pages = library.getCurrentBook().getPages(); //todo demeter?
        if(pages == null) {
            pages = loadPagesInBook();
        }

        return ViewModelMapper.PageToViewModel(pages);
    }

    /**
     * Charge les pages dans le livre local
     * @return (List<Page>) liste de pages
     */
    private List<Page> loadPagesInBook() {
        try(DataInterface repository = factory.newStorageSession()) {
            Book book = library.getCurrentBook();
            return repository.getBookPages(book);
        } catch (NotRetrievedException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (IsbnNotValidException | PageNotValidException | ChoiceNotValidException e) {
            view.displayMessage("Impossible de récupèrer les livres : " + e.getMessage());
        } catch (Exception e) {
            view.displayErrorMessage("Un problème est survenu lors du chargements des pages");
        }

        return new ArrayList<>();
    }
}
