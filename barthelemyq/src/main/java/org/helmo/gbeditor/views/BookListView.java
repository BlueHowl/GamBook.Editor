package org.helmo.gbeditor.views;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;

/**
 * Vue Description Livre
 */
public class BookListView extends BorderPane {

    private MainPresenterInterface presenter;

    public BookListView(MainPresenterInterface presenter) {
        this.presenter = presenter;

        this.setLeft(container);
        this.setCenter(bookDescriptionPane);
    }

    private BorderPane editableTitleBox = new BorderPane(); {
        Label title = new Label("Modifier le livre");

        editableTitleBox.setCenter(title);

        editableTitleBox.getStyleClass().add("title");
    }

    private TextField editableBookTitle = new TextField(); {
        editableBookTitle.setPrefWidth(250);

        int maxLength = 150;
        editableBookTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                editableBookTitle.setText(oldValue);
            }
        });
    }

    private BorderPane editableBookTitleBox = new BorderPane(); {
        Label text = new Label("Titre ");

        editableBookTitleBox.setLeft(text);
        editableBookTitleBox.setRight(editableBookTitle);

        editableBookTitleBox.getStyleClass().add("");
    }

    private Label editableIsbnVerif = new Label("");
    private TextField editableIsbn = new TextField(); {
        editableIsbn.setPrefWidth(90);
        //isbn.setEditable(false);
        //isbn.setPromptText("*-******-**-*");

        editableIsbn.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 11 || newValue.length() < 9 || !newValue.matches("\\d{1}-\\d{6}-\\d*")) {
                editableIsbn.setText(oldValue);
            } else {

                if(presenter != null) { //TODO modifier ????
                    editableIsbnVerif.setText(presenter.getIsbnVerif(newValue));
                }
            }
        });

    }

    private BorderPane editableIsbnBox = new BorderPane(); {
        Label text = new Label("ISBN ");

        HBox field = new HBox(); {
            field.getChildren().addAll(editableIsbn, editableIsbnVerif);
        }

        editableIsbnBox.setLeft(text);
        editableIsbnBox.setRight(field);

        editableIsbnBox.getStyleClass().add("");
    }

    private Label editableWordCount = new Label("Caractères : 0");
    private TextArea editableSummary = new TextArea(); {
        editableSummary.setPrefRowCount(10);
        editableSummary.setPrefColumnCount(100);
        editableSummary.setWrapText(true);
        editableSummary.setPrefWidth(400);

        int maxLength = 500;
        editableSummary.textProperty().addListener((observable, oldValue, newValue) -> {
            int wc = newValue.length();
            if (newValue.length() > maxLength) {
                editableSummary.setText(oldValue);
                --wc;
            }
            editableWordCount.setText("Caractères : " + wc);
        });
    }
    private BorderPane editableSummaryBox = new BorderPane(); {
        Label text = new Label("Résumé ");

        editableSummaryBox.setLeft(text);
        editableSummaryBox.setRight(editableSummary);
        editableSummaryBox.setBottom(editableWordCount);

        editableSummaryBox.getStyleClass().add("");
    }

    private TextField editableAuthor = new TextField(); {
        editableAuthor.setEditable(false);
    }
    private BorderPane editableAuthorBox = new BorderPane(); {
        Label text = new Label("Auteur ");

        editableAuthorBox.setLeft(text);
        editableAuthorBox.setRight(editableAuthor);

        editableAuthorBox.getStyleClass().add("");
    }

    private BorderPane editableCreateBtnBox = new BorderPane(); {
        Button loginButton = new Button("Modifier le livre"); {
            loginButton.setOnAction(action -> presenter.createBook(editableBookTitle.getText(), editableSummary.getText(), editableAuthor.getText(), editableIsbn.getText() + editableIsbnVerif.getText()));

            loginButton.getStyleClass().add("");
        }

        editableCreateBtnBox.setCenter(loginButton);

        editableCreateBtnBox.getStyleClass().add("auth-box");
    }

    //Book list

    private ListView<BookViewModel> list = new ListView<BookViewModel>();
    private ObservableList<BookViewModel> data = FXCollections.observableArrayList(
            new BookViewModel("titrelongggggthgggggggggeeefrgerg", "description", "auteur aaa", "1-222222-55-x"),
            new BookViewModel("titre2", "description22", "auteur aaa22", "1-333333-11-x"),
            new BookViewModel("titre33", "description32", "auteur a3332", "1-444444-11-7")
    );

    private static class LabelCell extends ListCell<BookViewModel> {
        @Override
        public void updateItem(BookViewModel item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {//limite titre 15 premiers caracteres
                VBox box = new VBox(); {
                    Label title = new Label("Titre : " + item.getTitle().substring(0, Math.min(item.getTitle().length(), 15)));
                    Label infos = new Label("Auteur : " + item.getAuthor() + "Isbn : " + item.getIsbn());

                    box.getChildren().add(title);
                    box.getChildren().add(infos);
                }

                setGraphic(box);
            }
        }
    }

    private HBox container = new HBox(); {
        container.setMinWidth(400);
        list.setItems(data);

        container.getChildren().addAll(list);

        list.setCellFactory(new Callback<ListView<BookViewModel>,
            ListCell<BookViewModel>>() {
                @Override
                public ListCell<BookViewModel> call(ListView<BookViewModel> list) {
                    return new LabelCell();
                }
            }
        );

        list.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<BookViewModel>() {
            @Override
            public void changed(ObservableValue<? extends BookViewModel> observable, BookViewModel oldValue, BookViewModel newValue) {
                setBookDescription(newValue);
            }
        });

        list.getSelectionModel().select(0);
    }

    private GridPane bookDescriptionPane = new GridPane();{
        bookDescriptionPane.add(editableTitleBox, 0, 0);
        bookDescriptionPane.add(editableBookTitleBox, 0, 1);
        bookDescriptionPane.add(editableIsbnBox, 0, 2);
        bookDescriptionPane.add(editableSummaryBox, 0, 3);
        bookDescriptionPane.add(editableAuthorBox, 0, 4);

        bookDescriptionPane.add(editableCreateBtnBox, 0, 5);

        bookDescriptionPane.setAlignment(Pos.CENTER);
    }

    /**
     * Affiche les informations du livre donné dans l'onglet de description et modification du livre
     * @param book (BookViewModel) livre à afficher
     */
    public void setBookDescription(BookViewModel book) {
        editableBookTitle.setText(book.getTitle());
        editableSummary.setText(book.getSummary());
        editableAuthor.setText(book.getAuthor());

        String isbn = book.getIsbn();
        editableIsbn.setText(isbn.substring(0, isbn.length()-2));
        editableIsbnVerif.setText(isbn.substring(isbn.length()-2));
    }

}
