package org.helmo.gbeditor;

import org.helmo.gbeditor.infrastructures.jdbc.SqlStorage;
import org.helmo.gbeditor.infrastructures.jdbc.SqlStorageFactory;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Cover;
import org.helmo.gbeditor.models.Isbn;
import org.helmo.gbeditor.repositories.DataInterface;
import org.helmo.gbeditor.repositories.exceptions.UnableToSetupException;
import org.helmo.gbeditor.repositories.exceptions.UnableToTearDownException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.helmo.gbeditor.infrastructures.jdbc.SqlQueries.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DBRepositoryTest {

    private static final SqlStorageFactory factory = new SqlStorageFactory(
            "org.apache.derby.jdbc.EmbeddedDriver",
            "jdbc:derby:derbyTest;create=true",
            "",
            ""
    );

    @BeforeEach
    public void setup() throws Exception {
        try(DataInterface storage = factory.newStorageSession()) {
            try {
                //storage.setup();
            } catch(UnableToSetupException ex) {

            }
        }
    }

    @AfterEach
    public void teardown() throws Exception {
        try(DataInterface storage = factory.newStorageSession()) {
            //storage.tearDown();
        } catch(UnableToTearDownException ex) {
            //Cette exception peut être lancée si le schéma ne contient pas les tables.
            //La méthode essaie alors de créer les tables.
        }
    }

    /*
    /**
     * Mets en place les tables de la bd (pour les tests)
     */
    /*
    public void setup() {
        try (Statement createStatement = connection.createStatement()) {
            createStatement.executeUpdate(CREATE_AUTHOR_TABLE_STMT);
            createStatement.executeUpdate(CREATE_BOOK_TABLE_STMT);
            createStatement.executeUpdate(CREATE_PAGE_TABLE_STMT);
            createStatement.executeUpdate(CREATE_CHOICE_TABLE_STMT);
        } catch (SQLException e) {
            throw new UnableToSetupException(e);
        }
    }

    /**
     * Détruit les tables de la bd (pour les tests)
     */
    /*
    public void tearDown() {
     /*
        try (Statement createStatement = connection.createStatement()) {
            createStatement.executeUpdate("DROP TABLE CHOICE");
            createStatement.executeUpdate("DROP TABLE PAGE");
            createStatement.executeUpdate("DROP TABLE BOOK");
            createStatement.executeUpdate("DROP TABLE AUTHOR");
        } catch (SQLException e) {
            throw new UnableToTearDownException(e);
        }
    }*/

    /*
    @Test
    public void saveBook() throws Exception {
        try(DataInterface storage = factory.newStorageSession()) {
            storage.saveBook(new Book(new Cover("titre", "description", "auteur", new Isbn("2-111111-04-x")), false));

            assertEquals(1, storage.getBookCount("111111"));
        }
    }*/

}
