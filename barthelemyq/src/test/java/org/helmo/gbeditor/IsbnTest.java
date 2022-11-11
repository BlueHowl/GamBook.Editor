package org.helmo.gbeditor;

import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.models.Isbn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Classe de test isbn
 */
public class IsbnTest {

    /**
     * Test exception isbn syntaxe invalide
     */
    @Test
    public void invalidIsbnSyntax() {
        try {
            new Isbn("test");
        } catch (IsbnNotValidException e) {
            assertEquals("Le code isbn est invalide, veuillez respecter la syntaxe", e.getMessage());
        }
    }

    /**
     * Test exception isbn code de vérification invalide
     */
    @Test
    public void invalidIsbnVerifCode() {
        try {
            new Isbn("2-111111-04-0");
        } catch (IsbnNotValidException e) {
            assertEquals("Le code isbn est invalide, le code de vérification ne correspond pas", e.getMessage());
        }
    }

    /**
     * Test génération numéro isbn
     */
    @Test
    public void generateIsbnTest() {
        assertEquals("2-123456-00-4", Isbn.generateIsbn("123456", 0));

        String isbnNum = Isbn.generateIsbn("111111", 4);
        Isbn isbn = null;
        try {
            isbn = new Isbn(isbnNum);
        } catch (IsbnNotValidException ignored) {}
        assertEquals("2-111111-04-x", isbn.getIsbn());
    }

    /**
     * Test getter isbn
     */
    @Test
    public void getIsbn() {
        try {
            Isbn isbn = new Isbn("2-111111-04-x");
            assertEquals("2-111111-04-x", isbn.getIsbn());
        } catch (IsbnNotValidException ignored) {}

    }
}
