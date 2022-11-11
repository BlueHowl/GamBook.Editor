package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.infrastructures.Mapper;
import org.helmo.gbeditor.infrastructures.dto.BookDTO;
import org.helmo.gbeditor.models.Choice;
import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.repositories.exceptions.*;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Isbn;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.repositories.DataInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.helmo.gbeditor.infrastructures.jdbc.SqlQueries.*;

/**
 * Classe SqlStorage
 * Crée et détruit le schéma de la Bd
 * Sauvegarde des livres
 */
public class SqlStorage implements AutoCloseable, DataInterface {
    private final Connection connection;

    /**
     * Constructeur SqlStorage
     * @param con (Connection) Connexion vers la base de donnée
     */
    public SqlStorage(Connection con) {
        this.connection = con;
    }

    /**
     * Mets en place les tables de la bd
     */
    public void setup() {
        try (Statement createStatement = connection.createStatement()) {
            createStatement.executeUpdate(CREATE_BOOK_TABLE_STMT);
            createStatement.executeUpdate(CREATE_PAGE_TABLE_STMT);
            createStatement.executeUpdate(CREATE_CHOICE_TABLE_STMT);
        } catch (SQLException e) {
            throw new UnableToSetupException(e);
        }
    }

    /**
     * Détruit les tables de la bd
     */
    public void tearDown() {
        try (Statement createStatement = connection.createStatement()) {
            createStatement.executeUpdate("DROP TABLE BOOK");
            createStatement.executeUpdate("DROP TABLE PAGE");
            createStatement.executeUpdate("DROP TABLE CHOICE");
        } catch (SQLException e) {
            throw new UnableToTearDownException(e);
        }
    }

    /**
     * Sauvegarde le livre donné
     * @param book (Book) Livre à sauvegarder
     * @throws UnableToSaveException lorsque le livre n'a pas pû être sauvé
     */
    @Override //TODO DO THE UPDATE CASE
    public void saveBook(Book book) throws UnableToSaveException {
        BookDTO bookDTO = Mapper.bookToDto(book);

        try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_BOOK_STMT)) {
            insertStmt.setString(1,bookDTO.isbn);
            insertStmt.setString(2, bookDTO.title);
            insertStmt.setString(3, bookDTO.summary);
            insertStmt.setString(4, bookDTO.author);
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new UnableToSaveException("Impossible de sauvegarder le livre dans la base de donnée");
        }
    }

    /**
     * Sauvegarde des pages d'un livre
     * @param pages (List<Page>) liste des pages
     * @throws UnableToSaveException lorsque le livre n'a pas pû être sauvé
     */
    @Override //TODO DO THE UPDATE CASE
    public void savePages(List<Page> pages) throws UnableToSaveException{

        try (PreparedStatement insertPagesStmt = connection.prepareStatement(INSERT_PAGE_STMT)) {
            try (PreparedStatement insertChoicesStmt = connection.prepareStatement(INSERT_CHOICES_STMT)) {
                for(int i = 0; i < pages.size(); ++i) {
                    Page page = pages.get(i);
                    addPageToInsert(insertPagesStmt, page, i);
                    for (Choice choice : page.getChoices()) {
                        addChoiceToInsert(pages, insertChoicesStmt, choice, i);
                    }
                }

                insertPagesStmt.executeBatch();
                insertChoicesStmt.executeBatch();
            }
        } catch (SQLException e) {
            throw new UnableToSaveException("Impossible de sauvegarder le livre dans la base de donnée");
        }
    }

    /**
     * Insère une page dans le statement
     * @param insertPagesStmt (PreparedStatement) statement
     * @param e (Page) élement à insérer
     * @param index (int) Numéro de la page
     * @throws SQLException
     */
    private void addPageToInsert(PreparedStatement insertPagesStmt, Page e, int index) throws SQLException {
        insertPagesStmt.setString(1,e.getText());
        insertPagesStmt.setInt(2, index);
        insertPagesStmt.setString(3, e.getBookIsbn());
        insertPagesStmt.addBatch();
    }

    /**
     * Insère un choix dans le statement
     * @param pages (List<Page>) liste des pages
     * @param insertChoicesStmt (PreparedStatement) statement
     * @param e (Choice) élement à insérer
     * @param pageId (int) Numéro de la page qui contient le choix
     * @throws SQLException
     */
    private void addChoiceToInsert(List<Page> pages, PreparedStatement insertChoicesStmt, Choice e, int pageId) throws SQLException {
        insertChoicesStmt.setString(1,e.getText());
        insertChoicesStmt.setInt(2, pages.indexOf(e.getRef()));
        insertChoicesStmt.setInt(3, pageId);
        insertChoicesStmt.addBatch();
    }

    /**
     * Récupère la liste des livres de l'auteur
     * @return (List<Book>) liste de livre
     * @throws ElementNotFoundException
     * @throws BookNotValidException
     * @throws IsbnNotValidException
     */
    @Override
    public List<Book> retrieveBooks(String authorId) throws ElementNotFoundException, BookNotValidException, IsbnNotValidException {
        try (PreparedStatement selectStmt = connection.prepareStatement(SELECT_BOOKS_BY_AUTHOR_STMT)) {
            selectStmt.setString(1, "%" + authorId + "%");
            ResultSet rs = selectStmt.executeQuery();

            List<Book> books = new ArrayList<>();
            while(rs.next()) {
                books.add(new Book(rs.getString("title"), rs.getString("summary"), rs.getString("author"), new Isbn(rs.getString("isbn"))));
            }

            return books;
        } catch (SQLException e) {
            throw new ElementNotFoundException("Erreur lors de la récupèration des livres" + e.getMessage());
        }
    }

    /**
     * Récupère la liste de pages du livre
     * @param isbn (Isbn) numéro isbn du livre
     * @return (List<Page>) liste de pages
     * @throws ElementNotFoundException
     * @throws IsbnNotValidException
     */
    @Override
    public List<Page> getBookPages(String isbn) throws ElementNotFoundException, IsbnNotValidException {
        try (PreparedStatement selectStmt = connection.prepareStatement(SELECT_PAGES_BY_BOOK_STMT)) {
            selectStmt.setString(1, isbn);
            ResultSet rs = selectStmt.executeQuery();

            List<Page> pages = new ArrayList<>();
            while(rs.next()) {
                pages.add(new Page(rs.getString("text"), null, new Isbn(rs.getString("book_isbn"))));
            }

            getChoices(isbn, pages);

            return pages;
        } catch (SQLException e) {
            throw new ElementNotFoundException("Impossible de récupèrer les pages du livre" + e.getMessage());
        }
    }

    /**
     * Récupère les choix et les assigne aux pages respectives
     * @param isbn (Isbn) numéro isbn du livre
     * @param pages (List<Page>) liste des pages du livre
     * @throws ElementNotFoundException
     */
    private void getChoices(String  isbn, List<Page> pages) throws ElementNotFoundException {
        try (PreparedStatement selectStmt = connection.prepareStatement(SELECT_CHOICES_BY_BOOK_STMT)) {
            selectStmt.setString(1, isbn);
            ResultSet rs = selectStmt.executeQuery();

            while(rs.next()) {
                Page page = pages.get(rs.getInt("ownerPage"));
                page.addChoice(new Choice(rs.getString("text"), pages.get(rs.getInt("refPage"))));
            }
        } catch (SQLException e) {
            throw new ElementNotFoundException("Impossible de récupèrer les choix de la page" + e.getMessage());
        }
    }

    /**
     * Récupère le nombre de livres enregistrés dans la bd
     * @return (int) nombre de livres (0 si pas de livres)
     * @throws ElementNotFoundException
     */
    @Override //TODO regler prblm si livres supprimés
    public int getBookCount(String authorId) throws ElementNotFoundException {
        try (PreparedStatement selectStmt = connection.prepareStatement(SELECT_BOOK_COUNT_STMT)) {
            selectStmt.setString(1, "%" + authorId + "%");
            ResultSet rs = selectStmt.executeQuery();

            rs.next();

            return rs.getInt("bCount");

        } catch (SQLException e) {
            throw new ElementNotFoundException("Impossible de récupèrer le nombre de livres");
        }
    }

    /**
     * Ferme la connexion à la bd
     * @throws DeconnectionFailedException
     */
    @Override
    public void close() throws DeconnectionFailedException {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new DeconnectionFailedException(ex);
        }
    }
}
