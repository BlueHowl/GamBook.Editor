package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Choice;
import org.helmo.gbeditor.models.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de sauvegarde des identifiants bd
 */
public class IdKeeper {

    private final Map<Book, Integer> bookAssociation = new HashMap<>();

    private final Map<Page, Integer> pageAssociation = new HashMap<>();

    private final Map<Choice, Integer> choiceAssociation = new HashMap<>();

    //Books

    /**
     * Ajoute un livre à la map des livres
     * @param book (Book)
     * @param id (int) id du livre dans la bd
     */
    public void addBook(Book book, int id) {
        bookAssociation.put(book, id);
    }

    /**
     * Récupère l'id bd du livre
     * @param book (Book)
     * @return (int) id bd
     */
    public int getBookId(Book book) {
        return bookAssociation.get(book);
    }

    //Pages
    /**
     * Ajoute une page à la map de pages
     * @param page (Page)
     * @param id (int) id de la page dans la bd
     */
    public void addPage(Page page, int id) {
        pageAssociation.put(page, id);
    }

    /**
     * Vérifie si la page existe déjà dans la liste de la bd
     * @param page (Page)
     * @return (Boolean)
     */
    public boolean doPageAlreadyExist(Page page) {
        return pageAssociation.containsKey(page);
    }

    /**
     * Récupère l'id bd de la page
     * @param page (Page)
     * @return (int)
     */
    public int getPageId(Page page) {
       return pageAssociation.get(page);
    }

    /**
     * Récupère la page dont l'id bd est égale a page_id
     * Retourne null si la page a été supprimé ou qu'aucune page ne possède l'id
     * @param page_id (int)
     * @return (Page)
     */
    public Page getPage(int page_id) {
        for(Map.Entry<Page, Integer> entry : pageAssociation.entrySet()) {
            if(entry.getValue() == page_id) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * récupère les pages supprimées sur base d'une comparaison avec les anciennes pages
     * @param pages (List<Page>) liste de pages à sauvegarder
     * @return (List<Integer>)
     */
    public List<Integer> getRemovedPages(List<Page> pages) {
        List<Integer> ids = new ArrayList<>();

        for(Map.Entry<Page, Integer> entry : pageAssociation.entrySet()) {
            if(!pages.contains(entry.getKey())) {
                ids.add(entry.getValue());
            }
        }

        return ids;
    }

    //Choices
    /**
     * Ajoute un choix à la map de choix
     * @param choice (Choice)
     * @param id (int) id du choix dans la bd
     */
    public void addChoice(Choice choice, int id) {
        choiceAssociation.put(choice, id);
    }

    /**
     * Vérifie si le choix existe déjà dans la liste de la bd
     * @param choice (Choice)
     * @return (Boolean)
     */
    public boolean doChoiceAlreadyExist(Choice choice) {
        return choiceAssociation.containsKey(choice);
    }

    /**
     * Récupère l'id bd du choix
     * @param choice (Choice)
     * @return (int)
     */
    public int getChoiceId(Choice choice) {
        return choiceAssociation.get(choice);
    }

    /**
     * récupère les choix supprimés sur base d'une comparaison avec les anciens choix
     * @param choices (List<Choice>)
     * @return (List<Integer>)
     */
    public List<Integer> getRemovedChoices(List<Choice> choices) {
        List<Integer> ids = new ArrayList<>();

        for(Map.Entry<Choice, Integer> entry : choiceAssociation.entrySet()) {
            if(!choices.contains(entry.getKey())) {
                ids.add(entry.getValue());
            }
        }

        return ids;
    }

}
