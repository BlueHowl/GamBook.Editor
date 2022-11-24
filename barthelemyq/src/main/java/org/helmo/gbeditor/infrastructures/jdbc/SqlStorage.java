package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.models.*;
import org.helmo.gbeditor.models.exceptions.BookNotValidException;
import org.helmo.gbeditor.models.exceptions.ChoiceNotValidException;
import org.helmo.gbeditor.models.exceptions.IsbnNotValidException;
import org.helmo.gbeditor.models.exceptions.PageNotValidException;
import org.helmo.gbeditor.repositories.exceptions.*;
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
public class SqlStorage implements DataInterface, AutoCloseable {
    private final Connection connection;

    private final IdKeeper idKeeper;

    /**
     * Constructeur SqlStorage
     * @param con (Connection) Connexion vers la base de donnée
     */
    public SqlStorage(Connection con, IdKeeper idKeeper) {
        this.connection = con;
        this.idKeeper = idKeeper;
    }

    /**
     * Mets en place les tables de la bd (pour les tests)
     */
    @Override
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
    @Override
    public void tearDown() {
        try (Statement createStatement = connection.createStatement()) {
            createStatement.executeUpdate("DROP TABLE CHOICE");
            createStatement.executeUpdate("DROP TABLE PAGE");
            createStatement.executeUpdate("DROP TABLE BOOK");
            createStatement.executeUpdate("DROP TABLE AUTHOR");
        } catch (SQLException e) {
            throw new UnableToTearDownException(e);
        }
    }

    /**
     * Sauvegarde le livre donné
     * @param book (Book) Livre à sauvegarder
     * @throws NotSavedException lorsque le livre n'a pas pû être sauvé
     */
    @Override
    public void saveBook(Book book) throws NotSavedException {
        try (PreparedStatement insertBookStmt = connection.prepareStatement(INSERT_BOOK_STMT, Statement.RETURN_GENERATED_KEYS)) {
            insertBookStmt.setString(1, book.getTitle());
            insertBookStmt.setString(2, book.getSummary());
            insertBookStmt.setString(3, book.getIsbn());
            insertBookStmt.executeUpdate();

            try (ResultSet keys = insertBookStmt.getGeneratedKeys()) {
                keys.next();
                idKeeper.addBook(book, keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new NotSavedException("Impossible de sauvegarder le livre dans la base de donnee", e);
        }
    }

    /**
     * Mise à jour du livre
     * @param book (Book) Livre à mettre à jour
     * @throws NotSavedException
     */
    @Override
    public void updateBook(Book book) throws NotSavedException {
        try (PreparedStatement updateStmt = connection.prepareStatement(UPDATE_BOOK_STMT)) {
            updateStmt.setString(1, book.getTitle());
            updateStmt.setString(2, book.getSummary());
            updateStmt.setString(3, book.getIsbn());
            updateStmt.setInt(4, idKeeper.getBookId(book));
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw new NotSavedException("Impossible de modifier le livre dans la base de donnée",e);
        }
    }

    /**
     * Publie le livre
     * @param book (Book) Livre à publier
     * @throws NotSavedException
     */
    @Override
    public void publishBook(Book book) throws NotSavedException {
        try (PreparedStatement updateStmt = connection.prepareStatement(UPDATE_PUBLISH_BOOK_STMT)) {
            updateStmt.setInt(1, idKeeper.getBookId(book));
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw new NotSavedException("Impossible de publier le livre",e);
        }
    }

    /**
     * Sauvegarde des pages d'un livre
     * @param book (Book) livre
     * @throws NotSavedException lorsque le livre n'a pas pû être sauvé
     */
    @Override
    public void saveChanges(Book book) throws NotSavedException { //todo remonter exception transaction fail
        Transaction
            .from(connection)
            .commit((con) -> {
                removeElements(idKeeper.getRemovedPages(book.getPages()), DELETE_PAGE_STMT);

                removeElements(idKeeper.getRemovedChoices(book.getAllChoices()), DELETE_CHOICE_STMT);

                savePageList(book);

                saveChoiceList(book.getPages());
            })
            .onRollback((e) -> { throw new UnableToSetupException(e);})
            .execute();

    }

    /**
     * Supprime les elements selon la liste d'id bd
     * @param ids (List<Integer>) liste d'id bd
     * @param query (String) requête sql
     * @throws SQLException
     */
    public void removeElements(List<Integer> ids, String query) throws SQLException {
        try (PreparedStatement deleteStmt = connection.prepareStatement(query)) {
            for(int id : ids) {
                deleteStmt.setInt(1, id);
                deleteStmt.addBatch();
            }

            deleteStmt.executeBatch();
        }
    }

    /**
     * Sauvegarde les pages données en bd et ajoute les nouveaux liens id-pages créés
     * @param book (Book) livre
     * @throws SQLException
     */
    private void savePageList(Book book) throws SQLException {
        try (PreparedStatement insertPagesStmt = connection.prepareStatement(INSERT_PAGE_STMT, Statement.RETURN_GENERATED_KEYS)) {
            try (PreparedStatement updatePagesStmt = connection.prepareStatement(UPDATE_PAGE_STMT)) {
                List<Page> newPages = new ArrayList<>();

                if(dispatchPages(book, newPages, insertPagesStmt, updatePagesStmt)) {
                    updatePagesStmt.executeUpdate();
                }

                insertPagesStmt.executeBatch();

                //ajoute les nouvelles pages au idKeeper
                registerNewPageIds(newPages, insertPagesStmt.getGeneratedKeys());
            }
        }
    }

    /**
     * Attribue les pages aux requêtes d'insertion et de mise à jour
     * @param book (Book) livre
     * @param newPages (List<Pages>) liste dans laquel ajouter les nouvelles pages
     * @param insertPagesStmt (PreparedStatement) requête d'insertion
     * @param updatePagesStmt (PreparedStatement) requête de mise à jour
     * @return (boolean) vrai si au moins 1 page à mettre à jour sinon false
     * @throws SQLException
     */
    private boolean dispatchPages(Book book, List<Page> newPages, PreparedStatement insertPagesStmt, PreparedStatement updatePagesStmt) throws SQLException {
        boolean update = false;

        for (int i = 0; i < book.getPages().size(); ++i) {
            Page page = book.getPages().get(i);

            if (idKeeper.doPageAlreadyExist(page)) {
                addPageToUpdate(updatePagesStmt, page, i, idKeeper.getPageId(page));
                update = true;
            } else {
                addPageToInsert(insertPagesStmt, page, i, idKeeper.getBookId(book));
                newPages.add(page);
            }
        }

        return update;
    }

    /**
     * Enregistre les nouvelles combinaisons Page-Id db
     * @param newPages (List<Page>) liste des nouvelles pages
     * @param keys (ResultSet) resulset des nouveaux id db
     * @throws SQLException
     */
    private void registerNewPageIds(List<Page> newPages, ResultSet keys) throws SQLException {
        //ajoute les nouvelles pages au idKeeper
        int i = 0;
        while (keys.next()) {
            idKeeper.addPage(newPages.get(i++), keys.getInt(1));
        }

        keys.close();
    }

    /**
     * Sauvegarde les choix donnés en bd et ajoute les nouveaux liens id-choix créés
     * @param pages (List<Choice>) liste des choix
     * @throws SQLException
     */
    private void saveChoiceList(List<Page> pages) throws SQLException {
        try (PreparedStatement insertChoicesStmt = connection.prepareStatement(INSERT_CHOICES_STMT, Statement.RETURN_GENERATED_KEYS)) {
            try (PreparedStatement updateChoicesStmt = connection.prepareStatement(UPDATE_CHOICE_STMT)) {
                List<Choice> newChoices = new ArrayList<>();

                if(dispatchChoices(pages, newChoices, insertChoicesStmt, updateChoicesStmt)) {
                    updateChoicesStmt.executeUpdate();
                }

                insertChoicesStmt.executeBatch();

                //ajoute les nouveaux choix au idKeeper
                registerNewChoiceIds(newChoices, insertChoicesStmt.getGeneratedKeys());
            }
        }
    }

    /**
     * Attribue les choix aux requêtes d'insertion et de mise à jour
     * @param pages (List<Page>) liste des pages
     * @param newChoices (List<Choice>) liste dans laquel ajouter les nouveaux choix
     * @param insertChoicesStmt (PreparedStatement) requête d'insertion
     * @param updateChoicesStmt (PreparedStatement) requête de mise à jour
     * @return (boolean) vrai si au moins 1 choix à mettre à jour sinon false
     * @throws SQLException
     */
    private boolean dispatchChoices(List<Page> pages, List<Choice> newChoices, PreparedStatement insertChoicesStmt, PreparedStatement updateChoicesStmt) throws SQLException {
        boolean update = false;
        for(Page page : pages) {
            for (Choice choice : page.getChoices()) {
                if (idKeeper.doChoiceAlreadyExist(choice)) {
                    addChoiceToUpdate(pages, updateChoicesStmt, choice, idKeeper.getPageId(page));
                    update = true;
                } else {
                    addChoiceToInsert(pages, insertChoicesStmt, choice, idKeeper.getPageId(page));
                    newChoices.add(choice);
                }
            }
        }

        return update;
    }

    /**
     * Enregistre les nouvelles combinaisons Choix-Id db
     * @param newChoices (List<Choice>) liste des nouvelles pages
     * @param keys (ResultSet) resulset des nouveaux id db
     * @throws SQLException
     */
    private void registerNewChoiceIds(List<Choice> newChoices, ResultSet keys) throws SQLException {
        int i = 0;
        while (keys.next()) {
            idKeeper.addChoice(newChoices.get(i++), keys.getInt(1));
        }

        keys.close();
    }

    /**
     * Insère une page dans le statement
     * @param insertPagesStmt (PreparedStatement) statement
     * @param p (Page) élement à insérer
     * @param index (int) Numéro de la page
     * @param id (int) id bd du livre
     * @throws SQLException
     */
    private void addPageToInsert(PreparedStatement insertPagesStmt, Page p, int index, int id) throws SQLException {
        insertPagesStmt.setString(1, p.getText());
        insertPagesStmt.setInt(2, index);
        insertPagesStmt.setInt(3, id);
        insertPagesStmt.addBatch();
    }

    /**
     * Insère une page dans le statement
     * @param updatePageStmt (PreparedStatement) statement
     * @param p (Page) élement à insérer
     * @param index (int) Numéro de la page
     * @param id (int) id bd de la page
     * @throws SQLException
     */
    private void addPageToUpdate(PreparedStatement updatePageStmt, Page p, int index, int id) throws SQLException {
        updatePageStmt.setString(1, p.getText());
        updatePageStmt.setInt(2, index);
        updatePageStmt.setInt(3, id);
        updatePageStmt.addBatch();
    }

    /**
     * Insère un choix dans le statement
     * @param pages (List<Page>) liste des pages
     * @param insertChoicesStmt (PreparedStatement) statement
     * @param c (Choice) élement à insérer
     * @param pageId (int) Numéro de la page qui contient le choix
     * @throws SQLException
     */
    private void addChoiceToInsert(List<Page> pages, PreparedStatement insertChoicesStmt, Choice c, int pageId) throws SQLException {
        insertChoicesStmt.setString(1, c.getText());
        insertChoicesStmt.setInt(2, pages.indexOf(c.getRef()));
        insertChoicesStmt.setInt(3, pageId);
        insertChoicesStmt.addBatch();
    }

    /**
     * Insère un choix dans le statement
     * @param pages (List<Page>) liste des pages
     * @param updateChoicesStmt (PreparedStatement) statement
     * @param c (Choice) élement à insérer
     * @param pageId (int) Numéro de la page qui contient le choix
     * @throws SQLException
     */
    private void addChoiceToUpdate(List<Page> pages, PreparedStatement updateChoicesStmt, Choice c, int pageId) throws SQLException {
        updateChoicesStmt.setString(1, c.getText());
        updateChoicesStmt.setInt(2, pages.indexOf(c.getRef()));
        updateChoicesStmt.setInt(3, pageId);
        updateChoicesStmt.setInt(4, idKeeper.getChoiceId(c));
        updateChoicesStmt.addBatch();
    }

    /**
     * Récupère la liste des livres de l'auteur
     * @param authorCode (String)
     * @return (List<Book>) liste de livre
     * @throws NotRetrievedException
     * @throws BookNotValidException
     * @throws IsbnNotValidException
     */
    @Override
    public List<Book> retrieveBooks(String authorCode) throws NotRetrievedException, BookNotValidException, IsbnNotValidException {
        try (PreparedStatement selectStmt = connection.prepareStatement(SELECT_BOOKS_BY_AUTHOR_STMT)) {
            selectStmt.setString(1, "%" + authorCode + "%");

            List<Book> books = new ArrayList<>();
            try(ResultSet rs = selectStmt.executeQuery()) {
                while(rs.next()) {
                    String authorInfos = rs.getString("surname") + " " + rs.getString("name");
                    Cover cover = new Cover(rs.getString("title"), rs.getString("summary"), authorInfos, new Isbn(rs.getString("isbn")));
                    Book book = new Book(cover, rs.getBoolean("published"));
                    books.add(book);
                    idKeeper.addBook(book, rs.getInt("id"));
                }
            }

            return books;
        } catch (SQLException e) {
            throw new NotRetrievedException("Erreur lors de la récupèration des livres", e);
        }
    }

    /**
     * Récupère et assigne la liste de pages du livre
     * @param book (Book) Livre
     * @throws NotRetrievedException
     * @throws PageNotValidException
     * @throws ChoiceNotValidException
     */
    @Override
    public List<Page> getBookPages(Book book) throws NotRetrievedException, PageNotValidException, ChoiceNotValidException {
        try (PreparedStatement selectStmt = connection.prepareStatement(SELECT_PAGES_BY_BOOK_STMT)) {
            selectStmt.setInt(1, idKeeper.getBookId(book));

            List<Page> pages = new ArrayList<>();
            try(ResultSet rs = selectStmt.executeQuery()) {
                while(rs.next()) {
                    Page page = new Page(rs.getString("text"), null);

                    pages.add(page);
                    idKeeper.addPage(page, rs.getInt("id"));
                }
            }

            book.setPages(pages);

            loadChoices(book);

            return book.getPages();
        } catch (SQLException e) {
            throw new NotRetrievedException("Impossible de récupèrer les pages du livre", e);
        }
    }

    /**
     * Récupère les choix et les assigne aux pages respectives
     * @param book (Book) Livre
     * @throws NotRetrievedException
     * @throws ChoiceNotValidException
     */
    private void loadChoices(Book book) throws NotRetrievedException, ChoiceNotValidException {
        try (PreparedStatement selectStmt = connection.prepareStatement(SELECT_CHOICES_BY_BOOK_STMT)) {
            selectStmt.setInt(1, idKeeper.getBookId(book));

            try(ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    Page page = idKeeper.getPage(rs.getInt("ownerPage"));
                    Choice choice = new Choice(rs.getString("text"), book.getPages().get(rs.getInt("refPage")));

                    page.addChoice(choice);
                    idKeeper.addChoice(choice, rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            throw new NotRetrievedException("Impossible de récupèrer les choix de la page", e);
        }
    }

    /**
     * Récupère le nombre de livres enregistrés dans la bd
     * @return (int) nombre de livres (0 si pas de livres)
     * @throws NotRetrievedException
     */
    @Override
    public int getBookCount(String authorCode) throws NotRetrievedException {
        try (PreparedStatement selectStmt = connection.prepareStatement(SELECT_BOOK_COUNT_STMT)) {
            selectStmt.setString(1, "%" + authorCode + "%");

            try(ResultSet rs = selectStmt.executeQuery()) {
                rs.next();
                return rs.getInt("bCount");
            }
        } catch (SQLException e) {
            throw new NotRetrievedException("Impossible de récupèrer le nombre de livres", e);
        }
    }

    /**
     * vérifie l'auteur et le crée si pas existant
     * @param author (Author)
     * @throws NotRetrievedException
     * @throws NotSavedException
     */
    @Override
    public void connectAuthor(Author author) throws NotRetrievedException, NotSavedException {
        try (PreparedStatement selectStmt = connection.prepareStatement(SELECT_AUTHOR_BY_CODE_STMT)) {
            selectStmt.setString(1, author.getCode());

            try(ResultSet rs = selectStmt.executeQuery()) {
                if(!rs.next()) {
                    registerAuthor(author);
                }
            }
        } catch (SQLException e) {
            throw new NotRetrievedException("Impossible de récupèrer l'auteur" + e.getMessage(), e);
        }
    }

    /**
     * Enregistre l'auteur dans la bd
     * @param author (Author)
     * @throws NotSavedException
     */
    private void registerAuthor(Author author) throws  NotSavedException {
        try (PreparedStatement insertBookStmt = connection.prepareStatement(INSERT_AUTHOR_STMT)) {
            insertBookStmt.setString(1, author.getCode());
            insertBookStmt.setString(2, author.getSurname());
            insertBookStmt.setString(3, author.getName());
            insertBookStmt.executeUpdate();
        } catch (SQLException e) {
            throw new NotSavedException("Impossible d'enregistrer l'auteur", e);
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
