package org.helmo.gbeditor.infrastructures;

import org.helmo.gbeditor.customexceptions.BookNotValidException;
import org.helmo.gbeditor.customexceptions.IsbnNotValidException;
import org.helmo.gbeditor.infrastructures.dto.BookDTO;
import org.helmo.gbeditor.infrastructures.dto.ChoiceDTO;
import org.helmo.gbeditor.infrastructures.dto.PageDTO;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Choice;
import org.helmo.gbeditor.models.Isbn;
import org.helmo.gbeditor.models.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de mapping DTO
 */
public class Mapper {

    //Book
    /**
     * Converti l'objet livre en objet DTO Livre
     * @param book (Book) livre
     * @return (BookDTO) livre dto
     */
    public static BookDTO BookToDto(Book book) {
        List<PageDTO> pagesDTO = new ArrayList<>();

        for (Page page : book.getPages()) {
            pagesDTO.add(PageToDto(page));
        }

        return new BookDTO(book.getTitle(), book.getSummary(), book.getAuthor(), book.getIsbn(), pagesDTO);
    }

    /**
     * Converti l'objet DTO livre en objet livre
     * @param bookDto (BookDTO) livre dto
     * @return (Book) livre
     * @throws IsbnNotValidException si le format de l'isbn récupéré est incorrect
     * @throws BookNotValidException si les données du livre récupéré sont incorrectes
     */
    public static Book DtoToBook(BookDTO bookDto) throws IsbnNotValidException, BookNotValidException {
        List<Page> pages = new ArrayList<>();

        for (PageDTO page : bookDto.getPages()) {
            pages.add(DtoToPage(page));
        }

        return new Book(bookDto.getTitle(), bookDto.getSummary(), bookDto.getAuthor(), new Isbn(bookDto.getIsbn()), pages);
    }

    //Page
    /**
     * Converti l'objet Page vers DTOPage
     * @param page (Page) objet à convertir
     * @return (PageDTO) objet converti
     */
    public static PageDTO PageToDto(Page page) {
        List<ChoiceDTO> choicesDTO = new ArrayList<>();

        for (Choice choice : page.getChoices()) {
            choicesDTO.add(ChoiceToDto(choice));
        }

        return new PageDTO(page.getText(), choicesDTO);
    }

    /**
     * Converti l'objet DTOPage vers Page
     * @param pageDTO (PageDTO) objet à convertir
     * @return (Page) objet converti
     */
    public static Page DtoToPage(PageDTO pageDTO) {
        List<Choice> choices = new ArrayList<>();

        for (ChoiceDTO choice : pageDTO.getChoices()) {
            choices.add(DtoToChoice(choice));
        }

        return new Page(pageDTO.getText(), choices);
    }

    //Page
    /**
     * Converti l'objet Choice vers ChoiceDTO
     * @param choice (Choice) objet à convertir
     * @return (ChoiceDTO) objet converti
     */
    public static ChoiceDTO ChoiceToDto(Choice choice) {
        return new ChoiceDTO(choice.getText(), choice.getRef());
    }

    /**
     * Converti l'objet ChoiceDTO vers Choice
     * @param choiceDTO (ChoiceDTO) objet à convertir
     * @return (Choice) objet converti
     */
    public static Choice DtoToChoice(ChoiceDTO choiceDTO) {
        return new Choice(choiceDTO.getText(), choiceDTO.getRef());
    }
}
