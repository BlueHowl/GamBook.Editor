package org.helmo.gbeditor.infrastructures;

import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.infrastructures.dto.BookDTO;
import org.helmo.gbeditor.infrastructures.dto.ChoiceDTO;
import org.helmo.gbeditor.infrastructures.dto.PageDTO;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Choice;
import org.helmo.gbeditor.models.Isbn;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.helmo.gbeditor.presenters.viewmodels.ChoiceViewModel;
import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de mapping DTO
 */
public class Mapper {

    //DTO
    //Book
    /**
     * Converti l'objet livre en objet DTO Livre
     * @param book (Book) livre
     * @return (BookDTO) livre dto
     */
    public static BookDTO bookToDto(Book book) {
        return new BookDTO(book.getTitle(), book.getSummary(), book.getAuthor(), book.getIsbn());
    }

    /**
     * Converti l'objet DTO livre en objet livre
     * @param bookDto (BookDTO) livre dto
     * @return (Book) livre
     * @throws IsbnNotValidException si le format de l'isbn récupéré est incorrect
     * @throws BookNotValidException si les données du livre récupéré sont incorrectes
     */
    public static Book dtoToBook(BookDTO bookDto) throws IsbnNotValidException, BookNotValidException {
        return new Book(bookDto.getTitle(), bookDto.getSummary(), bookDto.getAuthor(), new Isbn(bookDto.getIsbn()));
    }

    //Page
    /**
     * Converti l'objet Page vers DTOPage
     * @param page (Page) objet à convertir
     * @return (PageDTO) objet converti
     */
    public static PageDTO pageToDto(Page page) {
        List<ChoiceDTO> choicesDTO = new ArrayList<>();

        for (Choice choice : page.getChoices()) {
            choicesDTO.add(choiceToDto(choice));
        }

        return new PageDTO(page.getText(), choicesDTO, page.getBookIsbn());
    }

    /**
     * Converti l'objet DTOPage vers Page
     * @param pageDTO (PageDTO) objet à convertir
     * @return (Page) objet converti
     */
    public static Page dtoToPage(PageDTO pageDTO) throws IsbnNotValidException{
        List<Choice> choices = new ArrayList<>();

        for (ChoiceDTO choice : pageDTO.getChoices()) {
            choices.add(dtoToChoice(choice));
        }

        return new Page(pageDTO.getText(), choices, new Isbn(pageDTO.getBookIsbn()));
    }

    //Page
    /**
     * Converti l'objet Choice vers ChoiceDTO
     * @param choice (Choice) objet à convertir
     * @return (ChoiceDTO) objet converti
     */
    public static ChoiceDTO choiceToDto(Choice choice) {
        return new ChoiceDTO(choice.getText(), choice.getRef());
    }

    /**
     * Converti l'objet ChoiceDTO vers Choice
     * @param choiceDTO (ChoiceDTO) objet à convertir
     * @return (Choice) objet converti
     */
    public static Choice dtoToChoice(ChoiceDTO choiceDTO) {
        return new Choice(choiceDTO.getText(), choiceDTO.getRef());
    }


    //ViewModels

    /**
     * Conerti une liste de livre en modele de livre pour vue
     * @param books (List<Book>) liste de livres
     * @return (List<BookViewModel>) liste de livres pour vue
     */
    public static List<BookViewModel> bookToViewModel(List<Book> books) { //todo possible de convertir avec une interface fonctionnelle ?
        List<BookViewModel> viewModelBooks = new ArrayList<>();

        if(books != null) {
            for (Book book : books) {
                viewModelBooks.add(new BookViewModel(book.getTitle(), book.getSummary(), book.getAuthor(), book.getIsbn()));
            }
        }

        return viewModelBooks;
    }

    /**
     * Conerti une liste de page en modele de page pour vue
     * @param pages (List<Page>) liste de pages
     * @return (List<PageViewModel>) liste de pages pour vue
     */
    public static List<PageViewModel> PageToViewModel(List<Page> pages) { //todo possible de convertir avec une interface fonctionnelle ?
        List<PageViewModel> viewModelPage = new ArrayList<>();

        if(pages != null) {
            for (int i = 0; i < pages.size(); ++i) {
                viewModelPage.add(new PageViewModel(pages.get(i).getText(), i+1));
            }
        }

        return viewModelPage;
    }

    /**
     * Conerti une liste de choix en modele de choix pour vue
     * @param choices (List<Choice>) liste de choix
     * @return (List<ChoiceViewModel>) liste de choix pour vue
     */
    public static List<ChoiceViewModel> ChoiceToViewModel(List<Choice> choices, List<Page> pages) { //todo possible de convertir avec une interface fonctionnelle ?
        List<ChoiceViewModel> viewModelPage = new ArrayList<>();

        if(choices != null) {
            for (int i = 0; i < choices.size(); ++i) {
                Choice choice = choices.get(i);
                viewModelPage.add(new ChoiceViewModel(choice.getText(), pages.indexOf(choice.getRef())));
            }
        }

        return viewModelPage;
    }

}
