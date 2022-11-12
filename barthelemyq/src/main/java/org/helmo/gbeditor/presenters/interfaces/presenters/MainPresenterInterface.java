package org.helmo.gbeditor.presenters.interfaces.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.helmo.gbeditor.presenters.viewmodels.ChoiceViewModel;
import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;

import java.util.List;

public interface MainPresenterInterface {

    /**
     * Appel les méthodes getSurname et getName de User qui :
     * Récupère le nom et prénom concaténé de l'auteur
     * @return (String) nom prénom de l'auteur
     */
    String getUserInfos();

    /**
     * Appel la méthode generateIsbn de IsbnUtil qui :
     * Génère le numéro ISBN, composé du code de langue 2, des 6 chiffres de l'id auteur (matricule), du numéro unique du livre et du code de vérification
     * @return (String) nouveau numéro ISBN
     */
    String generateIsbn();

    /**
     * Retourne le code de vérification de l'isbn
     * @param isbnBegining (String)
     * @return (String) code de vérification isbn
     */
    String getIsbnVerif(String isbnBegining);

    /**
     * Défini le livre courant
     * @param selectedBook (int)
     */
    void setCurrentBook(int selectedBook);

    /**
     * Défini la page courante
     * @param selectedPage (int)
     */
    void setCurrentPage(int selectedPage);

    /**
     * Défini le choix courante
     * @param selectedChoice (int)
     */
    void setCurrentChoice(int selectedChoice);

    /**
     * Crée un livre
     * @param title (String) titre
     * @param summary (String) description
     * @param author (String) auteur
     * @param isbn (String) numéro isbn
     */
    void createBook(String title, String summary, String author, String isbn);

    /**
     * Modifie le livre courant
     * @param title (String) titre
     * @param summary (String) description
     * @param isbn (String) numéro isbn
     */
    void modifyCurrentBook(String title, String summary, String isbn);

    /**
     * Récupère les livres de l'auteur
     * @return (List<Book>) liste de livres
     * */
    List<BookViewModel> getBooks();

    /**
     * Récupère les pages du livre courant
     * @return (List<PageViewModel>) liste de pages
     */
    List<PageViewModel> getBookPages();

    /**
     * Récupère les choix de la page courante
     * @return (List<ChoiceViewModel>) liste de choix
     */
    List<ChoiceViewModel> getPageChoices();

    /**
     * Ajoute une page au livre courant
     * @param pageNum (int) numéro de la page
     * @param text (String) texte de la page
     */
    void addPageToCurrentBook(int pageNum, String text);

    /**
     * Modifie la page du livre à l'index donné
     * @param index (int) numéro de la page - 1
     * @param text (String)
     */
    void modifyPageOfCurrentBook(int index, String text);

    /**
     * Supprime la page courante
     */
    void removeCurrentPage();

    /**
     * Ajoute un choix à la page courante
     * @param text (String) texte du choix
     * @param refPageNum (int) numéro de la page liée
     */
    void addChoiceToCurrentPage(String text, int refPageNum);

    /**
     * Modifie le choix du livre à l'index donné
     * @param text (String)
     * @param refPageNum (int)
     */
    void modifyChoiceOfCurrentPage(String text, int refPageNum);

    /**
     * Supprime le choix courant
     */
    void removeCurrentChoice();

    /**
     * Change de vue en fonction de l'id
     * @param id (int)
     */
    void switchPane(int id);

    /**
     * Affiche la vue
     */
    void showView(Author author);

    /**
     * Renseigne une vue au presentateur
     * @param view (MainViewInterface)
     */
    void setView(MainViewInterface view);

}
