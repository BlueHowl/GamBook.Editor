package org.helmo.gbeditor.presenters.subpresenters;

import org.helmo.gbeditor.models.*;
import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.presenters.ViewModelMapper;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.BookListPInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.SubPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.helmo.gbeditor.repositories.DataInterface;
import org.helmo.gbeditor.repositories.StorageFactoryInterface;
import org.helmo.gbeditor.repositories.exceptions.NotRetrievedException;
import org.helmo.gbeditor.repositories.exceptions.NotSavedException;

import java.util.List;

/**
 * Classe sous présentateur BookListPresenter
 */
public class BookListPresenter implements SubPresenterInterface, BookListPInterface {

    private final StorageFactoryInterface factory;

    private final MainViewInterface view;

    private final Library library;

    private final Author author;

    /**
     * Constructeur de la classe BookListPresenter
     * @param factory (StorageFactoryInterface)
     * @param view (MainViewInterface)
     * @param library (Library)
     * @param author (Author)
     */
    public BookListPresenter(StorageFactoryInterface factory, MainViewInterface view, Library library, Author author) {
        this.factory = factory;
        this.view = view;
        this.library = library;
        this.author = author;
    }

    /**
     * Modifie le livre courant
     * @param title (String) titre
     * @param summary (String) description
     * @param isbn (String) numéro isbn
     */
    @Override
    public void modifyCurrentBook(String title, String summary, String isbn) {
        Book book = library.getCurrentBook();

        if(book == null) {
            view.displayMessage("Vous devez d'abord sélectionner un livre");
            return;
        }

        try(DataInterface repository = factory.newStorageSession()) {
            Cover cover = new Cover(title, summary, author.getAuthorInfos(), new Isbn(isbn));
            book.setCover(cover);
            repository.updateBook(book);
            view.refreshSubView(0);
        } catch (BookNotValidException | IsbnNotValidException e) {
            view.displayMessage(e.getMessage());
        } catch (NotSavedException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (Exception e) {
            view.displayErrorMessage("Un problème est survenu lors de la modification du livre");
        }
    }

    /**
     * Récupère les livres de l'auteur
     * @return (List<Book>) liste de livres
     */
    @Override
    public List<BookViewModel> getBooks() {
        if(library.getBooks() == null) {
            loadBooksInLibrary();
        }

        return ViewModelMapper.bookToViewModel(library.getBooks());
    }

    /**
     * Charge les livres dans la bibliothéque locale
     */
    private void loadBooksInLibrary() {
        try(DataInterface repository = factory.newStorageSession()) {
            library.setBooks(repository.retrieveBooks(author.getCode()));
        } catch (NotRetrievedException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (BookNotValidException | IsbnNotValidException e) {
            view.displayMessage("Impossible de récupèrer les livres : " + e.getMessage());
        } catch (Exception e) {
            view.displayErrorMessage("Un problème est survenu lors du chargements des livres");
        }
    }

    /**
     * Publie le livre
     */
    @Override
    public void publishBook() {
        Book book = library.getCurrentBook();

        if(book == null || book.getPages() == null || book.getPages().isEmpty()) {
            view.displayMessage("Vous devez sélectionner un livre avec une page au minimum");
            return;
        }

        try(DataInterface repository = factory.newStorageSession()) {
            repository.publishBook(book);
            book.setToPublished();
        } catch (Exception e) {
            view.displayErrorMessage("Un problème est survenu lors de la publication du livre");
        }

    }
}
