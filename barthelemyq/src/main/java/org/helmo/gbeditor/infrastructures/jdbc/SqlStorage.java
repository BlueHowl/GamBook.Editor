package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.infrastructures.Mapper;
import org.helmo.gbeditor.infrastructures.dto.BookDTO;
import org.helmo.gbeditor.infrastructures.jdbc.exceptions.DeconnectionFailedException;
import org.helmo.gbeditor.infrastructures.jdbc.exceptions.UnableToTearDownException;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.repositories.DataInterface;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.helmo.gbeditor.infrastructures.jdbc.SqlQueries.*;

/**
 * Crée et détruit le schéma de la Bd
 * Sauvegarde des livres
 */
public class SqlStorage implements AutoCloseable, DataInterface {
    private final Connection connection;

    public SqlStorage(Connection con) {
        this.connection = con;
    }

    public void setup() {
        try (Statement createStatement = connection.createStatement()) {
            createStatement.executeUpdate(CREATE_BOOK_TABLE_STMT);
            createStatement.executeUpdate(CREATE_PAGE_TABLE_STMT);
            createStatement.executeUpdate(CREATE_CHOICE_TABLE_STMT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void tearDown() {
        try (Statement createStatement = connection.createStatement()) {
            createStatement.executeUpdate("DROP TABLE BOOK");
            createStatement.executeUpdate("DROP TABLE PAGE");
            createStatement.executeUpdate("DROP TABLE CHOICE");
        } catch (SQLException e) {
            throw new UnableToTearDownException(e);
        }
    }
/*
    public void save(BookDTO p) {
        try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_TABLE_STMT, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1,p.getName());
            insertStmt.setString(2, p.getDescription());
            insertStmt.setString(3, "Project");
            insertStmt.executeUpdate();
            addToInsert(insertStmt, p);

            p.getTasks().forEachRemaining(t -> addToInsert(insertStmt, t));
            p.getPhases().forEach(t -> addToInsert(insertStmt, t));

            insertStmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addToInsert(PreparedStatement insertStmt, ProjectElement e) {
        try {
            insertStmt.setString(1,e.getName());
            insertStmt.setString(2, e.getDescription());
            insertStmt.setString(3, e.getClass().getName());
            insertStmt.setInt(4, (int) e.getParentId());
            insertStmt.addBatch();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
*/

    @Override
    public void saveBook(Book book) throws IOException {
        BookDTO bookDTO = Mapper.BookToDto(book);


    }

    @Override
    public List<Book> getBooks() throws IOException {
        return null;
    }

    @Override
    public int getBookCount() throws IOException {
        return 0;
    }

    @Override
    public void setUserId(String id) {

    }

    @Override
    public void close() throws Exception {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new DeconnectionFailedException(ex);
        }
    }
}
