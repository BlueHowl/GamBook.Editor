package org.helmo.gbeditor.presenters;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.helmo.gbeditor.infrastructures.Mapper;
import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.ChoiceNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.models.*;
import org.helmo.gbeditor.models.exceptions.PageNotValidException;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.helmo.gbeditor.presenters.viewmodels.ChoiceViewModel;
import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;
import org.helmo.gbeditor.repositories.DataInterface;
import org.helmo.gbeditor.repositories.exceptions.ElementNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Présentateur Principal
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public class MainPresenter implements MainPresenterInterface {

	private MainViewInterface view;

	private final DataInterface repository;

	//objet auteur contenant toutes les données de celui-ci
	private Author author;

	private Library library;

	private Book currentBook;
	private Page currentPage;
	private Choice currentChoice;

	/**
	 * Constructeur du présentateur
	 * @param repository (DataInterface)
	 */
	public MainPresenter(DataInterface repository) {
		this.repository = repository;
	}

	/**
	 * Appel les méthodes getSurname et getName de User qui :
	 * Récupère le nom et prénom concaténé de l'auteur
	 * @return (String) nom prénom de l'auteur
	 */
	@Override
	public String getUserInfos() {
		return author.getSurname() + " " + author.getName();
	}

	/**
	 * Appel la méthode generateIsbn de IsbnUtil qui :
	 * Génère le numéro ISBN, composé du code de langue 2, des 6 chiffres de l'id auteur (matricule), du numéro unique du livre et du code de vérification
	 * @return (String) nouveau numéro ISBN
	 */
	@Override
	public String generateIsbn() {
		int count = 0;
		try {
			count = repository.getBookCount(author.getId());
		} catch (ElementNotFoundException e) {
			view.displayMessage(e.getMessage());
		}

		return Isbn.generateIsbn(author.getId(), count);
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
	 * Défini le livre courant
	 * @param selectedBook (BookViewModel)
	 */
	@Override
	public void setCurrentBook(int selectedBook) {
		this.currentBook = library.getBookByIndex(selectedBook);
	}

	/**
	 * Défini la page courante
	 * @param selectedPage (int)
	 */
	@Override
	public void setCurrentPage(int selectedPage) {
		try {
			this.currentPage = currentBook.getPageByIndex(selectedPage);
		} catch (PageNotValidException e) {
			view.displayErrorMessage(e.getMessage());
		}
	}

	@Override
	public void setCurrentChoice(int selectedChoice) {
		this.currentChoice = currentPage.getChoiceByIndex(selectedChoice);
	}

	/**
	 * Crée un livre
	 * @param title (String) titre
	 * @param summary (String) description
	 * @param author (String) auteur
	 * @param isbn (String) numéro isbn
	 */
	@Override
	public void createBook(String title, String summary, String author, String isbn) {
		try {
			library.addBook(new Book(title, summary, author, new Isbn(isbn)));
			view.refreshSubView(0);
		} catch (BookNotValidException | IsbnNotValidException e) {
			view.displayMessage(e.getMessage());
		}
	}

	/**
	 * Modifie le livre courant
	 * @param title (String) titre
	 * @param summary (String) description
	 * @param isbn (String) numéro isbn
	 */
	public void modifyCurrentBook(String title, String summary, String isbn) {
		if(currentBook == null) {
			view.displayMessage("Vous devez d'abord sélectionner un livre");
			return;
		}

		try {
			currentBook.modifyBook(title, summary, isbn);
			view.refreshSubView(0);
		} catch (BookNotValidException | IsbnNotValidException e) {
			view.displayMessage(e.getMessage());
		}
	}

	/**
	 * Récupère les livres de l'auteur
	 * @return (List<Book>) liste de livres
	 */
	@Override
	public List<BookViewModel> getBooks() {
		if(library == null) {
			loadBooksInLibrary();
		}

		return Mapper.bookToViewModel(library.getBooks());
	}

	/**
	 * Charge les livres dans la bibliothéque locale
	 */
	private void loadBooksInLibrary() {
		try {
			library = new Library(repository.retrieveBooks(author.getId()));
		} catch (ElementNotFoundException e) {
			view.displayErrorMessage(e.getMessage());
		} catch (BookNotValidException | IsbnNotValidException e) {
			view.displayMessage("Impossible de récupèrer les livres : " + e.getMessage());
		}
	}

	/**
	 * Récupère les pages du livre courant
	 * @return (List<PageViewModel>) liste de pages
	 */
	@Override
	public List<PageViewModel> getBookPages() {
		if(currentBook.getPages() == null) {
			loadPagesInBook();
		}

		return Mapper.PageToViewModel(currentBook.getPages());
	}

	/**
	 * Charge les pages dans le livre local
	 */
	private void loadPagesInBook() {
		try {
			currentBook.setPages(repository.getBookPages(currentBook.getIsbn()));
		} catch (ElementNotFoundException e) {
			view.displayErrorMessage(e.getMessage());
		} catch (IsbnNotValidException | PageNotValidException | ChoiceNotValidException e) {
			view.displayMessage("Impossible de récupèrer les livres : " + e.getMessage());
		}
	}

	/**
	 * Récupère les choix de la page courante
	 * @return (List<ChoiceViewModel>) liste de choix
	 */
	@Override
	public List<ChoiceViewModel> getPageChoices() {
		return Mapper.ChoiceToViewModel(currentPage.getChoices(), currentBook.getPages());
	}

	/**
	 *
	 * @param page
	 * @return
	 */
	private List<ChoiceViewModel> getPageChoices(Page page) {
		return Mapper.ChoiceToViewModel(page.getChoices(), currentBook.getPages());
	}

	/**
	 * Ajoute une page au livre courant
	 * @param pageNum (int) numéro de la page
	 * @param text (String) texte de la page
	 */
	@Override
	public void addPageToCurrentBook(int pageNum, String text) {
		try {
			currentBook.setPageAtIndex(pageNum, new Page(text, null, new Isbn(currentBook.getIsbn())));
			view.refreshSubView(2);
		} catch (IsbnNotValidException | PageNotValidException e) {
			view.displayMessage(e.getMessage());
		}
	}

	/**
	 * Modifie la page du livre à l'index donné
	 * @param index (int) numéro de la page - 1
	 * @param text (String)
	 */
	@Override
	public void modifyPageOfCurrentBook(int index, String text) {
		try {
			currentBook.modifyPageAtIndex(index, text);
			view.refreshSubView(2);
		} catch (PageNotValidException e) {
			view.displayMessage(e.getMessage());
		}
	}

	/**
	 * Supprime la pge courante
	 */
	@Override
	public void removeCurrentPage() {
		if(currentPage == null) {
			view.displayMessage("Impossible de supprimer une page, aucune page sélectionnée");
			return;
		}

		List<Choice> involvedChoices = checkCurrentPageRefs();
		if(involvedChoices.size() > 0) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, involvedChoices.size() + " choix renvoient sur cette page, voulez vous les supprimer ?", ButtonType.APPLY, ButtonType.CANCEL);
			Optional<ButtonType> result =  alert.showAndWait();

			if(result.get() == ButtonType.APPLY)
				currentBook.removePage(currentPage);
		}
	}

	private List<Choice> checkCurrentPageRefs() {
		List<Choice> choices = new ArrayList<>();
		for(Page page : currentBook.getPages()) {
			for(Choice choice : page.getChoices()) {
				if(currentPage == choice.getRef()) //comparaison de références
					choices.add(choice);

			}
		}

		return choices;
	}

	/**
	 * Ajoute un choix à la page courante
	 * @param text (String) texte du choix
	 * @param refPageNum (int) numéro de la page liée
	 */
	@Override
	public void addChoiceToCurrentPage(String text, int refPageNum) {
		if(currentPage == null) {
			view.displayMessage("Impossible d'ajouter un choix, aucune page sélectionnée");
			return;
		}

		try {
			currentPage.addChoice(new Choice(text, currentBook.getPageByIndex(refPageNum)));
			view.refreshSubView(3);
		} catch (PageNotValidException | ChoiceNotValidException e) {
			view.displayMessage(e.getMessage());
		}
	}

	/**
	 * Modifie le choix du livre à l'index donné
	 * @param text (String)
	 * @param refPageNum (int)
	 */
	@Override
	public void modifyChoiceOfCurrentPage(String text, int refPageNum) {
		if(currentChoice == null) {
			view.displayMessage("Impossible de modifier le choix, aucun choix sélectionné");
			return;
		}

		currentChoice.setText(text);
		currentChoice.setRef(currentBook.getPages().get(refPageNum));
		view.refreshSubView(3);
	}

	/**
	 * Supprime le choix courant
	 */
	@Override
	public void removeCurrentChoice() {
		if(currentChoice == null) {
			view.displayMessage("Impossible de supprimer le choix, aucun choix sélectionné");
			return;
		}

		currentPage.removeChoice(currentChoice);
	}

	/**
	 * Change de vue en fonction de l'id
	 * @param id (int)
	 */
	@Override
	public void switchPane(int id) {
		if(id == 2 && currentBook == null) {
			view.displayMessage("Vous devez d'abord sélectionner un livre");
			return;
		}

		view.switchPane(id);
	}

	/**
	 * Affiche la vue
	 */
	@Override
	public void showView(Author author) {
		this.author = author;
		view.showView();
	}

	/**
	 * Renseigne une vue au presentateur
	 * @param view (MainViewInterface)
	 */
	@Override
	public void setView(MainViewInterface view) {
		this.view = view;
	}

}
