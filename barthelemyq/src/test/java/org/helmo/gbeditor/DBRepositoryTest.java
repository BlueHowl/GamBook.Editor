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
                storage.setup();
            } catch(UnableToSetupException ex) {

            }
        }
    }

    @AfterEach
    public void teardown() throws Exception {
        try(DataInterface storage = factory.newStorageSession()) {
            storage.tearDown();
        } catch(UnableToTearDownException ex) {
            //Cette exception peut être lancée si le schéma ne contient pas les tables.
            //La méthode essaie alors de créer les tables.
        }
    }

    @Test
    public void saveBook() throws Exception {
        try(DataInterface storage = factory.newStorageSession()) {
            storage.saveBook(new Book(new Cover("titre", "description", "auteur", new Isbn("2-111111-04-x")), false));

            assertEquals(1, storage.getBookCount("111111"));
        }
    }

    /*
    @Test
    public void savesProject() throws Exception {
        try(SqlStorage storage = factory.newStorageSession()) {
            org.helmo.sd_java_jdbc.domain.Project newProject = new Project(0L,"A project","A description");
            newProject.add(new Phase(0L,"A phase 1","blabla", newProject));
            newProject.add(new Phase(0L,"A phase 2","blabla", newProject));

            storage.save(newProject);

            assertNotEquals(newProject.getId(), 0L);

            assertNotEquals(newProject.getPhases().get(0), 0L);
            assertNotEquals(newProject.getPhases().get(1), 0L);
        }
    }

    @Test
    public void updatesProject() throws Exception {
        try(SqlStorage storage = factory.newStorageSession()) {
            Project newProject = new Project(0L,"A project","A description");
            newProject.add(new Phase(0L,"A phase 1","blabla", newProject));
            newProject.add(new Phase(0L,"A phase 2","blabla", newProject));

            storage.save(newProject);

            assertNotEquals(newProject.getId(), 0L);

            assertNotEquals(newProject.getPhases().get(0), 0L);
            assertNotEquals(newProject.getPhases().get(1), 0L);

            newProject.add(new Task(0L, "A task","description", newProject.getPhases().get(0)));

            storage.save(newProject);
        }
    }

    @Test
    public void loadsProject() throws Exception {
        //Given : Un projet sauvegardé en BD
        try(SqlProjectStorage storage = factory.newStorageSession()) {
            Project newProject = new Project(0L, "A project", "A description");
            newProject.add(new Phase(0L, "A phase 1", "blabla", newProject));
            newProject.add(new Phase(0L, "A phase 2", "blabla", newProject));

            storage.save(newProject);
            //When : on essaie de le récupérer
            Project found = storage.load(newProject.getId());

            //Then : l'objet trouvé a le même nom que le projet sauvegardé
            assertEquals(newProject.getName(), found.getName());
            assertEquals(newProject.getDescription(), found.getDescription());
            assertNotSame(newProject, found);

            assertEquals("A phase 1", found.getPhases().get(0).getName());
            assertEquals("A phase 2", found.getPhases().get(1).getName());
        }
    }
    */

}
