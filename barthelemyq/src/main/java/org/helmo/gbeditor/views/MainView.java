package org.helmo.gbeditor.views;

import  javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;

/**
 * Vue
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public class MainView implements MainViewInterface {
    private MainPresenterInterface presenter;

    private Stage primaryStage;
    private Scene scene;

    public MainView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private Label message = new Label(); {
        message.getStyleClass().add("wrap-text");
    }

    //Create Book section
    private BorderPane titleBox = new BorderPane(); {
        Label title = new Label("Créer un livre");

        titleBox.setCenter(title);

        titleBox.getStyleClass().add("title");
    }

    private TextField bookTitle = new TextField(); {
        bookTitle.setPrefWidth(250);

        int maxLength = 150;
        bookTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                bookTitle.setText(oldValue);
            }
        });
    }
    private BorderPane bookTitleBox = new BorderPane(); {
        Label text = new Label("Titre ");

        bookTitleBox.setLeft(text);
        bookTitleBox.setRight(bookTitle);

        bookTitleBox.getStyleClass().add("");
    }

    private Label isbnVerif = new Label(" ");
    private TextField isbn = new TextField(); {
        isbn.setPrefWidth(90);
        //isbn.setEditable(false);
        //isbn.setPromptText("*-******-**-*");

        isbn.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 11 || newValue.length() < 9 || !newValue.matches("\\d{1}-\\d{6}-\\d*")) {
                isbn.setText(oldValue);
            } else {
                isbnVerif.setText(presenter.getIsbnVerif(newValue));
            }
        });

    }

    private BorderPane isbnBox = new BorderPane(); {
        Label text = new Label("ISBN ");

        HBox field = new HBox(); {
            field.getChildren().addAll(isbn, isbnVerif);
        }

        isbnBox.setLeft(text);
        isbnBox.setRight(field);

        isbnBox.getStyleClass().add("");
    }

    private Label wordCount = new Label("Caractères : 0");
    private TextArea summary = new TextArea(); {
        summary.setPrefRowCount(8);
        summary.setPrefColumnCount(100);
        summary.setWrapText(true);
        summary.setPrefWidth(500);

        int maxLength = 500;
        summary.textProperty().addListener((observable, oldValue, newValue) -> {
            int wc = newValue.length();
            if (newValue.length() > maxLength) {
                summary.setText(oldValue);
                --wc;
            }
            wordCount.setText("Caractères : " + wc);
        });
    }
    private BorderPane summaryBox = new BorderPane(); {
        Label text = new Label("Résumé ");

        summaryBox.setLeft(text);
        summaryBox.setRight(summary);
        summaryBox.setBottom(wordCount);

        summaryBox.getStyleClass().add("");
    }

    private TextField author = new TextField(); {
        author.setEditable(false);
    }
    private BorderPane authorBox = new BorderPane(); {
        Label text = new Label("Auteur ");

        authorBox.setLeft(text);
        authorBox.setRight(author);

        authorBox.getStyleClass().add("");
    }

    private BorderPane createBtnBox = new BorderPane(); {
        Button loginButton = new Button("Créer le livre"); {
            loginButton.setOnAction(action -> presenter.createBook(bookTitle.getText(), summary.getText(), author.getText(), isbn.getText() + isbnVerif.getText()));

            loginButton.getStyleClass().add("");
        }

        createBtnBox.setCenter(loginButton);

        createBtnBox.getStyleClass().add("auth-box");
    }

    private GridPane createBookPane = new GridPane();{
        createBookPane.add(titleBox, 0, 0);
        createBookPane.add(bookTitleBox, 0, 1);
        createBookPane.add(isbnBox, 0, 2);
        createBookPane.add(summaryBox, 0, 3);
        createBookPane.add(authorBox, 0, 4);

        createBookPane.add(createBtnBox, 0, 5);

        createBookPane.add(message,  0, 6);
        GridPane.setColumnSpan(message, GridPane.REMAINING);

        createBookPane.setAlignment(Pos.CENTER);

        createBookPane.setVisible(false);
    }



    //description view Book section
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
            //TODO MODIFIER loginButton.setOnAction(action -> presenter.createBook(editableBookTitle.getText(), editableSummary.getText(), editableAuthor.getText(), editableIsbn.getText() + editableIsbnVerif.getText()));

            loginButton.getStyleClass().add("");
        }

        editableCreateBtnBox.setCenter(loginButton);

        editableCreateBtnBox.getStyleClass().add("auth-box");
    }

    //Book list

    private ListView<BookViewModel> list = new ListView<BookViewModel>();
    private ObservableList<BookViewModel> data = FXCollections.observableArrayList(
            new BookViewModel("titre", "description", "auteur aaa", "1-222222-55-x"),
            new BookViewModel("titre2", "description22", "auteur aaa22", "1-333333-11-x"),
            new BookViewModel("titre33", "description32", "auteur a3332", "1-444444-11-7")
    );

    private static class LabelCell extends ListCell<BookViewModel> {
        @Override
        public void updateItem(BookViewModel item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Label label = new Label("Titre : " + item.getTitle() + ", Auteur : " + item.getAuthor() + "Isbn : " + item.getIsbn());
                setGraphic(label);
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

        //bookDescriptionPane.add(message,  0, 6);
        //GridPane.setColumnSpan(message, GridPane.REMAINING);

        bookDescriptionPane.setAlignment(Pos.CENTER);

        //bookDescriptionPane.setVisible(false);
    }

    private BorderPane bookListPane = new BorderPane(); {
        bookListPane.setLeft(container);
        bookListPane.setCenter(bookDescriptionPane);
    }



    //Top section
    private Label userInfoText = new Label("");
    private BorderPane TopBox = new BorderPane(); {

        MenuItem menuItem1 = new MenuItem("Accueil"); { menuItem1.setOnAction(action -> switchPane(0)); }
        MenuItem menuItem2 = new MenuItem("Nouveau"); { menuItem2.setOnAction(action -> switchPane(1)); }
        MenuItem menuItem3 = new MenuItem("Quitter"); { menuItem3.setOnAction(action -> quit()); }

        MenuButton topMenu = new MenuButton("Menu", null, menuItem1, menuItem2, menuItem3);

        TopBox.setLeft(topMenu);
        TopBox.setRight(userInfoText);

        TopBox.getStyleClass().add("top-box");
        userInfoText.getStyleClass().add("user-info-text");
    }

    private BorderPane mainPane = new BorderPane();{
        mainPane.setTop(TopBox);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(createBookPane);
        stackPane.getChildren().add(bookListPane);

        mainPane.setCenter(stackPane);
    }

    /**
     * Affiche les informations du livre donné dans l'onglet de description et modification du livre
     * @param book (BookViewModel) livre à afficher
     */
    private void setBookDescription(BookViewModel book) {
        editableBookTitle.setText(book.getTitle().substring(0, Math.min(book.getTitle().length(), 14))); //limite 15 premiers caracteres
        editableSummary.setText(book.getSummary());
        editableAuthor.setText(book.getAuthor());

        String isbn = book.getIsbn();
        editableIsbn.setText(isbn.substring(0, isbn.length()-2));
        editableIsbnVerif.setText(isbn.substring(isbn.length()-2));
    }

    /**
     * Met en place les informations sur la vue
     */
    private void SetupView() {
        String userInfos = presenter.getUserInfos();
        userInfoText.setText(userInfos);
        author.setText(userInfos);
    }

    /**
     * Quitte l'application
     */
    private void quit() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Retourne le scenegraph
     * @return
     */
    public Parent getRoot() {
        return mainPane;
    }

    /**
     * Attribue la scene correspondant à la vue
     * @param scene (Scene)
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Affiche la vue
     */
    public void showView() {
        SetupView();
        primaryStage.setScene(scene);
    }

    /**
     * Change de menu dans la vue principale
     * @param id (int) identifiant du panel
     */
    @Override
    public void switchPane(int id) { //remplacer par liste de menu et activer sur base d'index ?
        createBookPane.setVisible(false);
        bookListPane.setVisible(false);

        switch (id) {
            case 0:
                bookListPane.setVisible(true);
                break;
            case 1:
                //faire fonction pour
                createBookPane.setVisible(true);

                bookTitle.clear();
                summary.clear();

                String isbnNumber = presenter.generateIsbn();
                isbn.setText(isbnNumber.substring(0, isbnNumber.length()-2));
                isbnVerif.setText(isbnNumber.substring(isbnNumber.length()-2));
                break;
            case 2:
                break;
        }
    }

    /**
     * Renseigne un presenter à la vue
     * @param presenter (MainPresenterInterface)
     */
    @Override
    public void setPresenter(MainPresenterInterface presenter) {
        this.presenter = presenter;
    }

    /**
     * Affiche un message
     * @param msg (String) message
     */
    @Override
    public void displayMessage(String msg) {
        message.setText(msg);
        message.getStyleClass().remove("error-message");
    }

    /**
     * Affiche un message d'erreur
     * @param msg (String) message d'erreur
     */
    @Override
    public void displayErrorMessage(String msg) {
        message.setText(msg);
        message.getStyleClass().add("error-message");
    }

}

