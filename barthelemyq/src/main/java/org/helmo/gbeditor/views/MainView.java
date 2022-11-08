package org.helmo.gbeditor.views;

import  javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;


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

    private CreateBookView cbv = new CreateBookView(presenter);
    private BookListView blv = new BookListView(presenter);

    private BorderPane mainPane = new BorderPane();{
        mainPane.setTop(TopBox);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(cbv);
        stackPane.getChildren().add(blv);

        mainPane.setCenter(stackPane);
        mainPane.setBottom(message);
    }

    /**
     * Met en place les informations sur la vue
     */
    private void SetupView() {
        String userInfos = presenter.getUserInfos();
        userInfoText.setText(userInfos);
        //cbv.author.setText(userInfos);
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
        cbv.setVisible(false);
        blv.setVisible(false);

        switch (id) {
            case 0:
                blv.setVisible(true);
                break;
            case 1:
                //faire fonction pour
                cbv.setVisible(true);

                /*bookTitle.clear();
                summary.clear();

                String isbnNumber = presenter.generateIsbn();
                isbn.setText(isbnNumber.substring(0, isbnNumber.length()-2));
                isbnVerif.setText(isbnNumber.substring(isbnNumber.length()-2));*/
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

