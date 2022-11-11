package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.infrastructures.Mapper;
import org.helmo.gbeditor.models.exceptions.BookNotValidException;
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

import java.util.List;

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
		this.currentPage = currentBook.getPageByIndex(selectedPage);
	}

	@Override
	public void setCurrentChoice(int selectedChoice) {
		this.currentChoice = currentPage.getChoiceByIndex(selectedChoice);
	}

	/**
	 * Crée un livre et le sauvegarde
	 * @param title (String) titre
	 * @param summary (String) description
	 * @param author (String) auteur
	 * @param isbn (String) numéro isbn
	 */
	@Override
	public void createUpdateBook(String title, String summary, String author, String isbn) {
		try {
			library.addBook(new Book(title, summary, author, new Isbn(isbn)));
			//repository.saveBook(book); TODO ne pas sauvegarder mtn ---> methodé sauvegarde lors d'appuis sur btn
			view.switchPane(0);
		} catch (BookNotValidException | IsbnNotValidException e) {
			view.displayMessage(e.getMessage());
		} /*catch (UnableToSaveException e) {
			view.displayErrorMessage(e.getMessage());
		}*/
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
	public List<PageViewModel> getBookPages() { //TODO do smth if no book selected
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
		} catch (IsbnNotValidException e) {
			view.displayMessage("Impossible de récupèrer les livres : " + e.getMessage());
		}
	}

	/**
	 * Récupère les choix de la page courante
	 * @return (List<ChoiceViewModel>) liste de choix
	 */
	public List<ChoiceViewModel> getPageChoices(int index) {
		Page page = currentBook.getPages().get(index);
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
			view.switchPane(2); //refresh
		} catch (IsbnNotValidException e) {
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
			view.switchPane(2); //refresh
		} catch (PageNotValidException e) {
			view.displayMessage(e.getMessage());
		}
	}

	/**
	 * Ajoute un choix à la page courante
	 * @param text (String) texte du choix
	 * @param refPageNum (int) numéro de la page liée
	 */
	@Override
	public void addChoiceToCurrentPage(String text, int refPageNum) {
		currentPage.addChoice(new Choice(text, currentBook.getPageByIndex(refPageNum))); //todo handle exceptions
		view.switchPane(2); //refresh
	}

	/**
	 * Modifie le choix du livre à l'index donné
	 * @param text (String)
	 * @param refPageNum (int)
	 */
	@Override
	public void modifyChoiceOfCurrentPage(String text, int refPageNum) {
		if(currentChoice != null) {
			currentChoice.setText(text);
			currentChoice.setRef(currentBook.getPages().get(refPageNum));
			view.switchPane(2); //refresh
		} else {
			// todo own exception throw new PageNotValidException("Impossible de modifier, aucune page sélectionnée");
		}
	}

	/**
	 * Change de vue en fonction de l'id
	 * @param id (int)
	 */
	@Override
	public void switchPane(int id) {
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
