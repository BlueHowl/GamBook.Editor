package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.PageNotValidException;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Livre
 */
public class Book {

    private Cover cover;

    private List<Page> pages;

    private Page currentPage;

    private boolean published;

    /**
     * Constructeur de la classe livre
     * @param cover (Cover) covuerture du livre
     */
    public Book(Cover cover, boolean published) {
        this.cover = cover;
        this.published = published;
    }

    /**
     * Récupère le titre du livre
     * @return (String) titre
     */
    public String getTitle() {
        return cover.getTitle();
    }

    /**
     * Récupère le résumé du livre
     * @return (String) résumé
     */
    public String getSummary() {
        return cover.getSummary();
    }

    /**
     * Récupère le nom et prénom de l'auteur du livre
     * @return (String) nom et prénom de l'auteur
     */
    public String getAuthor() {
        return cover.getAuthor();
    }

    /**
     * Récupère le numéro isbn du livre
     * @return (String) numéro isbn du livre
     */
    public String getIsbn() {
        return cover.getIsbn();
    }

    /**
     * Récupère les pages du livre
     * @return (List<Page>) liste de pages
     */
    public List<Page> getPages() {
        return pages;
    }

    /**
     * Récupère la page sur base de l'index
     * @return (Page)
     */
    public Page getPageByIndex(int index) throws PageNotValidException {
        try {
            return pages.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new PageNotValidException("La page au numéro donné n'existe pas");
        }
    }

    /**
     * Défini la page courante
     * @param selectedPage (int)
     */
    public void setCurrentPage(int selectedPage) throws PageNotValidException {
        currentPage = getPageByIndex(selectedPage);
    }

    /**
     * Récupère la page courante
     * @return (Page)
     */
    public Page getCurrentPage() {
        return currentPage;
    }

    /**
     * Défini la liste des pages du livre
     * @param pages (List<Page>) liste de pags
     */
    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    /**
     * Ajoute une page à l'index donné
     * Assigne la premiere page si pas encore de pages
     * @param index (int) index (numéro page)
     * @param page (Page)
     */
    public void setPageAtIndex(int index, Page page) {
        if(pages.isEmpty() || index > pages.size()) {
            pages.add(page);
        } else {
            pages.add(index, page);
        }
    }

    /**
     * modifie la page à l'index donné
     * @param index (int)
     * @param text (String)
     */
    public void modifyPageAtIndex(int index, String text) throws PageNotValidException {
        if(!pages.isEmpty()) {
            pages.get(index).setText(text);
        } else {
            throw new PageNotValidException("Impossible de modifier, aucune page sélectionnée");
        }
    }

    /**
     * Supprime la page de la liste de pages
     * @param page (Page)
     */
    public void removePage(Page page) {
        pages.remove(page);
    }

    /**
     * Récupère les choix de toutes les pages du livre
     * @return (List<Choice>) liste de choix
     */
    public List<Choice> getAllChoices() {
        List<Choice> choices = new ArrayList<>();
        for(Page page : getPages()) {
            choices.addAll(page.getChoices());
        }

        return choices;
    }

    /**
     * Modifie le livre
     * @param cover (Cover) nouvelle couverture
     */
    public void setCover(Cover cover) {
        this.cover = cover;
    }

    /**
     * Récupère la valeur de publication
     * @return (boolean)
     */
    public boolean isPublished() {
        return published;
    }

    /**
     * Assigne la valeur true de publication
     */
    public void setToPublished() {
        published = true;
    }
}
