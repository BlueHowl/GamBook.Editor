package org.helmo.gbeditor.infrastructures;

import org.helmo.gbeditor.customexceptions.BookNotValidException;
import org.helmo.gbeditor.customexceptions.IsbnNotValidException;
import org.helmo.gbeditor.infrastructures.dto.BookDTO;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Isbn;

/**
 * Classe de mapping DTO
 */
public class Mapper {

    /**
     * Converti l'objet livre en objet DTO Livre
     * @param book (Book) livre
     * @return (BookDTO) livre dto
     */
    public static BookDTO BookToDto(Book book) {
        return new BookDTO(book.getTitle(), book.getSummary(), book.getAuthor(), book.getIsbn());
    }

    /**
     * Converti l'objet DTO livre en objet livre
     * @param bookDto (BookDTO) livre dto
     * @return (Book) livre
     * @throws IsbnNotValidException si le format de l'isbn récupéré est incorrect
     * @throws BookNotValidException si les données du livre récupéré sont incorrectes
     */
    public static Book DtoToBook(BookDTO bookDto) throws IsbnNotValidException, BookNotValidException {
        return new Book(bookDto.getTitle(), bookDto.getSummary(), bookDto.getAuthor(), new Isbn(bookDto.getIsbn()));
    }
}
