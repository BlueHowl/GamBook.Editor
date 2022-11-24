package org.helmo.gbeditor.presenters.subpresenters;

import org.helmo.gbeditor.models.*;
import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.CreateBookPInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.SubPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.repositories.DataInterface;
import org.helmo.gbeditor.repositories.StorageFactoryInterface;
import org.helmo.gbeditor.repositories.exceptions.NotRetrievedException;
import org.helmo.gbeditor.repositories.exceptions.NotSavedException;

/**
 * Classe sous présentateur CreateBookPresenter
 */
public class CreateBookPresenter implements SubPresenterInterface, CreateBookPInterface {

    private final StorageFactoryInterface factory;

    private final MainViewInterface view;

    private final Library library;

    private final Author author;

    /**
     * Constructeur de la classe CreateBookPresenter
     * @param factory (StorageFactoryInterface)
     * @param view (MainViewInterface)
     * @param library (Library)
     * @param author (Author)
     */
    public CreateBookPresenter(StorageFactoryInterface factory, MainViewInterface view, Library library, Author author) {
        this.factory = factory;
        this.view = view;
        this.library = library;
        this.author = author;
    }

    /**
     * Appel la méthode generateIsbn de IsbnUtil qui :
     * Génère le numéro ISBN, composé du code de langue 2, des 6 chiffres de l'id auteur (matricule), du numéro unique du livre et du code de vérification
     * @return (String) nouveau numéro ISBN
     */
    @Override
    public String generateIsbn() {
        int count = 0;

        try(DataInterface repository = factory.newStorageSession()) {
            count = repository.getBookCount(author.getCode());
        } catch (NotRetrievedException e) {
            view.displayMessage(e.getMessage());
        } catch (Exception e) {
            view.displayErrorMessage("Un problème est survenu lors de la création du livre");
        }

        return Isbn.generateIsbn(author.getCode(), count);
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
        try(DataInterface repository = factory.newStorageSession()) {
            Book book = new Book(new Cover(title, summary, author, new Isbn(isbn)), false);
            library.addBook(book);
            repository.saveBook(book);
            view.switchPane(0);
        } catch (BookNotValidException | IsbnNotValidException e) {
            view.displayMessage(e.getMessage());
        } catch (NotSavedException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (Exception e) {
            view.displayErrorMessage("Un problème est survenu lors de la création du livre");
        }
    }

}
