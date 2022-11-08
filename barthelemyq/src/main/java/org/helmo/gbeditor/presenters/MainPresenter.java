package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.customexceptions.BookNotValidException;
import org.helmo.gbeditor.customexceptions.IsbnNotValidException;
import org.helmo.gbeditor.models.*;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.repositories.DataInterface;

import java.io.IOException;

/**
 * Présentateur Principal
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public class MainPresenter implements MainPresenterInterface {

	//private EditorInterface editor;

	private final MainViewInterface view;

	private final DataInterface repository;

	//objet auteur contenant toutes les données de celui-ci
	private Author author;

	/**
	 * Constructeur du présentateur
	 * @param view (ViewInterface) Vue
	 */
	public MainPresenter(MainViewInterface view, DataInterface repository) {
		//this.editor = editor;
		this.view = view;
		this.repository = repository;

		view.setPresenter(this);
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
			count = repository.getBookCount();
		} catch (IOException e) {
			view.displayMessage("Attention, Aucuns livres trouvés, est-ce bien le premier ?");
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

	/*
	/**
	 * Appel la méthode de Editor qui :
	 * Génère le numéro ISBN, composé du code de langue 2, des 6 chiffres de l'id auteur (matricule), du numéro unique du livre et du code de vérification
	 * @return (String) base de numéro ISBN
	 */
	//public String getIsbn() { return book.getIsbn(); }

	/**
	 * Crée un livre et le sauvegarde
	 * @param title (String) titre
	 * @param summary (String) description
	 * @param author (String) auteur
	 * @param isbn (String) numéro isbn
	 */
	@Override
	public void createBook(String title, String summary, String author, String isbn) {
		try {
			Book book = new Book(title, summary, author, new Isbn(isbn), null);
			repository.saveBook(book);
			view.switchPane(0);
		} catch (BookNotValidException|IsbnNotValidException e) {
			view.displayErrorMessage(e.getMessage());
		} catch (IOException e) {
			view.displayMessage(e.getMessage());
		}
	}

	/**
	 * Affiche la vue
	 */
	@Override
	public void showView(Author author) {
		this.author = author;
		view.showView();
	}

}
