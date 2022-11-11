package org.helmo.gbeditor;

import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Isbn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Classe de test livre
 */
public class BookTest {

    /**
     * Test les getters
     */
    @Test
    public void gettersTest() {
        try {
            Book book = new Book("titleTest", "summaryTest", "authorTest", new Isbn("2-111111-04-x"));

            assertEquals("titleTest", book.getTitle());
            assertEquals("summaryTest", book.getSummary());
            assertEquals("authorTest", book.getAuthor());
            assertEquals("2-111111-04-x", book.getIsbn());
        } catch (BookNotValidException|IsbnNotValidException ignored) {}
    }

    /**
     * Test champs vides
     */
    @Test
    public void BookBlankParamsTest() {
        try {
            Book book = new Book("", "   ", "", new Isbn("2-111111-04-x"));
        } catch (BookNotValidException|IsbnNotValidException e) {
            assertEquals("Tous les champs doivent être remplis", e.getMessage());
        }
    }

    /**
     * Test titre trop long
     */
    @Test
    public void BookTitleTooLongTest() {
        try {
            new Book("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "s", "a", new Isbn("2-111111-04-x"));

        } catch (BookNotValidException|IsbnNotValidException e) {
            assertEquals("Le titre ne peut pas faire plus que 150 caractères", e.getMessage());
        }
    }

    /**
     * Test description trop longue
     */
    @Test
    public void BookSummaryTooLongTest() {
        String summary500Plus = "lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll+++++++++";

        try {
            new Book("a", summary500Plus, "a", new Isbn("2-111111-04-x"));

        } catch (BookNotValidException|IsbnNotValidException e) {
            assertEquals("La description ne peut pas faire plus que 500 caractères", e.getMessage());
        }
    }
}
