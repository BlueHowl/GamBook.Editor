package org.helmo.gbeditor.infrastructures.json;

import org.helmo.gbeditor.models.*;
import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.ChoiceNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.infrastructures.dto.BookDTO;
import org.helmo.gbeditor.infrastructures.dto.ChoiceDTO;
import org.helmo.gbeditor.infrastructures.dto.PageDTO;
import org.helmo.gbeditor.models.exceptions.PageNotValidException;

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
        return new BookDTO(book.getTitle(), book.getSummary(), book.getAuthor(), book.getIsbn(), book.isPublished());
    }

    /**
     * Converti l'objet DTO livre en objet livre
     * @param bookDto (BookDTO) livre dto
     * @return (Book) livre
     * @throws IsbnNotValidException si le format de l'isbn récupéré est incorrect
     * @throws BookNotValidException si les données du livre récupéré sont incorrectes
     */
    public static Book dtoToBook(BookDTO bookDto) throws IsbnNotValidException, BookNotValidException {
        return new Book(new Cover(bookDto.getTitle(), bookDto.getSummary(), bookDto.getAuthor(), new Isbn(bookDto.getIsbn())), bookDto.isPublished());
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

        return new PageDTO(page.getText(), choicesDTO);
    }

    /**
     * Converti l'objet DTOPage vers Page
     * @param pageDTO (PageDTO) objet à convertir
     * @return (Page) objet converti
     */
    public static Page dtoToPage(PageDTO pageDTO) throws PageNotValidException, ChoiceNotValidException {
        List<Choice> choices = new ArrayList<>();

        for (ChoiceDTO choice : pageDTO.getChoices()) {
            choices.add(dtoToChoice(choice));
        }

        return new Page(pageDTO.getText(), choices);
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
    public static Choice dtoToChoice(ChoiceDTO choiceDTO) throws ChoiceNotValidException {
        return new Choice(choiceDTO.getText(), choiceDTO.getRef());
    }

}
