package org.helmo.gbeditor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.helmo.gbeditor.customexceptions.BookNotValidException;
import org.helmo.gbeditor.customexceptions.IsbnNotValidException;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Isbn;
import org.helmo.gbeditor.repositories.DataInterface;
import org.helmo.gbeditor.infrastructures.JsonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Classe de test du repository json
 */
public class JsonRepositoryTest {

    private static final Path TESTPATH = Path.of("src","test", "resources", "test.json").toAbsolutePath();

    private static DataInterface jsonRepository;

    /**
     * initialisation du repository
     */
    @BeforeAll
    public static void start() {
        jsonRepository = new JsonRepository(TESTPATH);
    }

    /**
     * setup de la ressource pour les tests
     */
    @BeforeEach
    public void setUp() {
        try {
            Files.write(TESTPATH, new byte[0], StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * suppression de la ressource apres tests
     */
    @AfterEach
    public void tearDown() {
        try {
            Files.deleteIfExists(TESTPATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * test exception 0 livres trouvés
     */
    @Test
    public void getBookCount0Test() {
        try {
            assertEquals(0, jsonRepository.getBookCount());
        } catch (IOException e) {
            assertEquals("Aucuns livres trouvés en sauvegarde, fichier créé", e.getMessage());
        }
    }

    /**
     * test sauvegarde livre et exceptions liées
     */
    @Test
    public void saveBookTest() {
        try {
            Book book = new Book("titre", "description", "auteur", new Isbn("2-111111-04-x"));

            jsonRepository.saveBook(book);

            assertEquals(1, jsonRepository.getBookCount());
        } catch (BookNotValidException|IsbnNotValidException ignored) {
        } catch (IOException e) {
            assertTrue(e.getMessage().equals("Impossible de sauvegarder les livres") || e.getMessage().equals("Aucuns livres trouvés en sauvegarde"));
        }
    }

}

