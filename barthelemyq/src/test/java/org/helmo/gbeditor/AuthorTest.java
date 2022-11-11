package org.helmo.gbeditor;

import org.helmo.gbeditor.models.exceptions.AuthorNotValidException;
import org.helmo.gbeditor.models.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorTest {

    /**
     * Test les getters de la classe author
     */
    @Test
    public void gettersTest() {
        try {
            Author author = new Author("000000", "testSurname", "testName");
            assertEquals("000000", author.getId());
            assertEquals("testSurname", author.getSurname());
            assertEquals("testName", author.getName());
        } catch (AuthorNotValidException ignored) {}
    }

    /**
     * Test champs vides
     */
    @Test
    public void AuthorBlankParamsTest() {
        try {
            Author author = new Author("000000", "", "  ");
        } catch (AuthorNotValidException e) {
            assertEquals("Attention tout les champs doivent être remplis", e.getMessage());
        }
    }

    /**
     * Test matricule trop long
     */
    @Test
    public void AuthorIdTooLong() {
        try {
            Author author = new Author("000000999", "e", " e ");
        } catch (AuthorNotValidException e) {
            assertEquals("Le matricule doit obligatoirement faire 6 chiffres !", e.getMessage());
        }
    }

    /**
     * Test matricule trop court
     */
    @Test
    public void AuthorIdTooSmall() {
        try {
            Author author = new Author("00", "e", " e ");
        } catch (AuthorNotValidException e) {
            assertEquals("Le matricule doit obligatoirement faire 6 chiffres !", e.getMessage());
        }
    }
}
