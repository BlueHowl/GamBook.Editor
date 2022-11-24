package org.helmo.gbeditor.views.subviews;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.BookListPInterface;
import org.helmo.gbeditor.presenters.interfaces.views.subviews.SubViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;

/**
 * Vue Description Livre
 */
public class BookListView extends BorderPane implements SubViewInterface {

    private MainPresenterInterface presenter;

    private BookListPInterface bookListPresenter;

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
                if(presenter != null) {
                    editableIsbnVerif.setText(presenter.getIsbnVerif(newValue));
                }
            }
        });

    }

    //private Label isbnLabel = new Label();

    private BorderPane editableIsbnBox = new BorderPane(); {
        Label text = new Label("ISBN ");

        HBox field = new HBox(); {
            field.getChildren().addAll(editableIsbn, editableIsbnVerif);
        }

        editableIsbnBox.setLeft(text);
        editableIsbnBox.setRight(field);
        //editableIsbnBox.setRight(isbnLabel);

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

    private HBox editableBtnBox = new HBox(); {
        Button publishButton = new Button("Publier le livre"); {
            publishButton.setOnAction(action -> bookListPresenter.publishBook());

            publishButton.getStyleClass().add("");
        }

        Button modifyButton = new Button("Modifier le livre"); {
            modifyButton.setOnAction(action -> bookListPresenter.modifyCurrentBook(editableBookTitle.getText(), editableSummary.getText(), editableIsbn.getText() + editableIsbnVerif.getText()));

            modifyButton.getStyleClass().add("");
        }

        Button gestionButton = new Button("Gérer les pages et choix"); {
            gestionButton.setOnAction(action -> presenter.switchPane(2));

            gestionButton.getStyleClass().add("");
        }

        editableBtnBox.getChildren().addAll(publishButton, modifyButton, gestionButton);

        editableBtnBox.getStyleClass().add("auth-box");
    }

    //Book list

    private ListView<BookViewModel> list = new ListView<BookViewModel>();
    private ObservableList<BookViewModel> data = FXCollections.observableArrayList();

    private static class LabelCell extends ListCell<BookViewModel> {
        @Override
        public void updateItem(BookViewModel item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {//limite titre 15 premiers caracteres
                VBox box = new VBox(); {
                    String titleString = item.getTitle();
                    Label title = new Label("Titre : " + titleString.substring(0, Math.min(titleString.length(), 15)) + ((titleString.length() >= 15) ? "..." : ""));

                    HBox hBox = new HBox();
                    Label infos = new Label("Auteur : " + item.getAuthor() + ", Isbn : " + item.getIsbn());
                    ImageView deleteElementLogo = new ImageView(getClass().getResource("/images/published.png").toExternalForm()); {
                        deleteElementLogo.setPreserveRatio(true);
                        deleteElementLogo.setFitWidth(16);
                        deleteElementLogo.setVisible(item.isPublished());
                    }
                    hBox.getChildren().addAll(infos, deleteElementLogo);

                    box.getChildren().addAll(title, hBox);

                }

                setGraphic(box);
            } else {
                setGraphic(null);
            }
        }
    }

    private HBox container = new HBox(); {
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
                if(presenter != null && newValue != null) {
                    if(newValue.isPublished()) {
                        bookDescriptionPane.setVisible(false);
                        published.setVisible(true);
                    } else {
                        published.setVisible(false);
                        bookDescriptionPane.setVisible(true);
                        setBookDetails(newValue);
                        presenter.setCurrentBook(data.indexOf(newValue));
                    }
                }
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

        bookDescriptionPane.add(editableBtnBox, 0, 5);

        bookDescriptionPane.setAlignment(Pos.CENTER);
    }

    private Label published = new Label("Livre publié");{
        published.setVisible(false);
    }

    private StackPane mainPane = new StackPane();{
        mainPane.getChildren().addAll(bookDescriptionPane, published);
    }

    /**
     * Constructeur de la sous vue
     * @param presenter (MainPresenterInterface)
     */
    public BookListView(MainPresenterInterface presenter) {
        this.presenter = presenter;

        this.setLeft(container);
        this.setCenter(mainPane);

        this.setVisible(false);
    }

    /**
     * Assigne le presentateur spécifique
     */
    public void setPresenter() {
        this.bookListPresenter = (BookListPInterface) presenter.getSubPresenters(0);
    }

    /**
     * Défini la liste de livres
     * affiche les livres de l'auteur
     */
    @Override
    public void refresh() {
        data.setAll(bookListPresenter.getBooks());
    }

    /**
     * Modifie la visibilité de la vue
     * @param b (boolean)
     */
    @Override
    public void setVisibility(boolean b) {
        this.setVisible(b);
    }

    /**
     * Affiche les informations du livre donné dans l'onglet de description et modification du livre
     * @param book (BookViewModel) livre à afficher
     */
    public void setBookDetails(BookViewModel book) {
        editableBookTitle.setText(book.getTitle());
        editableSummary.setText(book.getSummary());
        editableAuthor.setText(book.getAuthor());

        //isbnLabel.setText(book.getIsbn());

        String isbn = book.getIsbn();
        editableIsbn.setText(isbn.substring(0, isbn.length()-2));
        editableIsbnVerif.setText(isbn.substring(isbn.length()-2));

    }

}
