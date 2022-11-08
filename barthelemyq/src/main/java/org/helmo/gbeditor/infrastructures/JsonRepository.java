package org.helmo.gbeditor.infrastructures;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.helmo.gbeditor.infrastructures.dto.BookDTO;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.repositories.DataInterface;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Repository Json
 * Gère la sauvegarde et la récupération des données sous format json
 */
public class JsonRepository implements DataInterface {

    private final Gson gson = new Gson();

    private String jsonBooksPath;

    /**
     * Constructeur du repository (crée un dossier ue36 si pas existant)
     */
    public JsonRepository() throws IOException {
        Files.createDirectories(Path.of(System.getProperty("user.home"), "ue36")); //exception remontée
    }

    /**
     * Constructeur de test repository
     * @param jsonBooksPath (Path) Chemin d'accès du fichier de sauvegarde
     */
    public JsonRepository(Path jsonBooksPath) {
        this.jsonBooksPath = jsonBooksPath.toString();
    }


    /**
     * Défini le nom du fichier de sauvegarde (ici : matricule auteur)
     * @param id (String) nom du fichier
     */
    @Override
    public void setUserId(String id) {
        jsonBooksPath = Path.of(System.getProperty("user.home"), "ue36", id + ".json").toAbsolutePath().toString();
    }

    /**
     * Sauvegarde le livre donné en l'ajoutant aux autres
     * @param book (Book) Objet Livre
     * @throws IOException Impossible de sauvegarder les livres
     */
    @Override
    public void saveBook(Book book) throws IOException {
        List<BookDTO> books;
        books = getAllBooks();

        if(books != null) {
            books.add(Mapper.BookToDto(book));
        } else {
            books = new ArrayList<>();
            books.add(Mapper.BookToDto(book));
        }

        setAllBooks(books);
    }

    @Override
    public List<Book> getBooks() throws IOException {
        return null;
    }

    /**
     * Récupère le nombre de livres stockés
     * @return (int) nombre de livres
     */
    @Override
    public int getBookCount() throws IOException {
        List<BookDTO> books;
        books = getAllBooks();

        if(books == null) { throw new IOException("Aucuns livres trouvés en sauvegarde, fichier créé"); }

        return books.size();
    }


    /**
     * Récupère tous les livres stockés
     * @return (List<BookDTO>) Liste d'objet DTO Book
     */
    private List<BookDTO> getAllBooks() {//throws IOException {
        List<BookDTO> books = new ArrayList<>();

        try(Reader reader = new BufferedReader(new FileReader(jsonBooksPath))) {
            Type BookDtoList = new TypeToken<ArrayList<BookDTO>>(){}.getType();
            books = gson.fromJson(reader, BookDtoList);
        } catch (IOException e) {
            //throw new IOException("Aucuns livres trouvés en sauvegarde, fichier créé");
        }

        return books;
    }

    /**
     * Sauvegarde tous les livres donnés
     * @param books (List<BookDTO>) Liste de tous les livres en Objet DTO
     * @throws IOException Impossible de sauvegarder les livres
     */
    private void setAllBooks(List<BookDTO> books) throws IOException {
        try(FileWriter fw = new FileWriter(jsonBooksPath, StandardCharsets.UTF_8)) {
            gson.toJson(books, fw);
        } catch (JsonIOException | IOException e) {
            throw new IOException("Impossible de sauvegarder les livres");
        }
    }
}
