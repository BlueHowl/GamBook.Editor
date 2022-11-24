package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Choice;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.helmo.gbeditor.presenters.viewmodels.ChoiceViewModel;
import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe mapper pour modèle de vue
 */
public class ViewModelMapper {

    /**
     * Conerti une liste de livre en modele de livre pour vue
     * @param books (List<Book>) liste de livres
     * @return (List<BookViewModel>) liste de livres pour vue
     */
    public static List<BookViewModel> bookToViewModel(List<Book> books) {
        List<BookViewModel> viewModelBooks = new ArrayList<>();

        for (Book book : books) {
            viewModelBooks.add(new BookViewModel(book.getTitle(), book.getSummary(), book.getAuthor(), book.getIsbn(), book.isPublished()));
        }

        return viewModelBooks;
    }

    /**
     * Conerti une liste de page en modele de page pour vue
     * @param pages (List<Page>) liste de pages
     * @return (List<PageViewModel>) liste de pages pour vue
     */
    public static List<PageViewModel> PageToViewModel(List<Page> pages) {
        List<PageViewModel> viewModelPage = new ArrayList<>();

        if(pages != null) { //pages possiblement null
            for (int i = 0; i < pages.size(); ++i) {
                viewModelPage.add(new PageViewModel(pages.get(i).getText(), i+1));
            }
        }

        return viewModelPage;
    }

    /**
     * Conerti une liste de choix en modele de choix pour vue
     * @param choices (List<Choice>) liste de choix
     * @return (List<ChoiceViewModel>) liste de choix pour vue
     */
    public static List<ChoiceViewModel> ChoiceToViewModel(List<Choice> choices, List<Page> pages) {
        List<ChoiceViewModel> viewModelPage = new ArrayList<>();

        for (Choice choice :  choices) {
            viewModelPage.add(new ChoiceViewModel(choice.getText(), pages.indexOf(choice.getRef())+1));
        }

        return viewModelPage;
    }

}
