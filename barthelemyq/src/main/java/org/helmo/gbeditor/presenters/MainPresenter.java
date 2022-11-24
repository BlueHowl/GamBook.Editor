package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.*;
import org.helmo.gbeditor.models.exceptions.PageNotValidException;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.SubPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.presenters.subpresenters.*;
import org.helmo.gbeditor.repositories.StorageFactoryInterface;

/**
 * Présentateur Principal
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public class MainPresenter implements MainPresenterInterface {

	private MainViewInterface view;

	private final StorageFactoryInterface factory;

	private Author author;

	private final Library library = new Library();

	private final SubPresenterInterface[] subPresenters = new SubPresenterInterface[4];

	/**
	 * Constructeur du présentateur
	 * @param factory (StorageFactoryInterface)
	 */
	public MainPresenter(StorageFactoryInterface factory) {
		this.factory = factory;
	}

	/**
	 * Récupère le nom prenom de l'auteur
	 * @return (String)
	 */
	@Override
	public String getAuthorInfos() {
		return author.getAuthorInfos();
	}

	/**
	 * Retourne le code de vérification de l'isbn
	 * @param isbnBegining (String)
	 * @return (String) code de vérification isbn
	 */
	@Override
	public String getIsbnVerif(String isbnBegining) {
		return String.valueOf(Isbn.getIsbnVerification(isbnBegining));
	}

	/**
	 * Change de vue en fonction de l'id
	 * @param id (int)
	 */
	@Override
	public void switchPane(int id) {
		if(id == 2 && library.getCurrentBook() == null) {
			view.displayMessage("Vous devez d'abord sélectionner un livre");
			return;
		}

		view.switchPane(id);
	}

	/**
	 * Affiche la vue
	 */
	@Override
	public void showMainView(Author author) {
		subPresenters[0] = new BookListPresenter(factory, view, library, author);
		subPresenters[1] = new CreateBookPresenter(factory, view, library, author);
		subPresenters[2] = new PageGestionPresenter(factory, view, library);
		subPresenters[3] = new ChoiceGestionPresenter(view, library);

		this.author = author;
		view.showView(getAuthorInfos());
	}

	/**
	 * Renseigne une vue au presentateur
	 * @param view (MainViewInterface)
	 */
	@Override
	public void setView(MainViewInterface view) {
		this.view = view;
	}

	@Override
	public SubPresenterInterface getSubPresenters(int id) {
		return subPresenters[id];
	}

	/**
	 * Défini le livre courant
	 * @param selectedBook (int)
	 */
	@Override
	public void setCurrentBook(int selectedBook) {
		library.setCurrentBook(selectedBook);
	}

	@Override
	public void setCurrentPage(int selectedPage) {
		try {
			library.getCurrentBook().setCurrentPage(selectedPage);
		} catch (PageNotValidException e) {
			view.displayMessage(e.getMessage());
		}
	}

	/**
	 * Défini le choix courant
	 * @param selectedChoice (int)
	 */
	@Override
	public void setCurrentChoice(int selectedChoice) {
		library.getCurrentPage().setCurrentChoice(selectedChoice);
	}
}