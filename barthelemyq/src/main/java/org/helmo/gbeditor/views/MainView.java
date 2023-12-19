package org.helmo.gbeditor.views;

import  javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.MainViewInterface;
import org.helmo.gbeditor.presenters.interfaces.views.subviews.SubViewInterface;
import org.helmo.gbeditor.views.subviews.BookListView;
import org.helmo.gbeditor.views.subviews.ChoicesGestionView;
import org.helmo.gbeditor.views.subviews.CreateBookView;
import org.helmo.gbeditor.views.subviews.PagesGestionView;

import java.util.Optional;


/**
 * Vue
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public class MainView implements MainViewInterface {
    private MainPresenterInterface presenter;

    private Stage primaryStage;
    private Scene scene;

    private Label message = new Label(); {
        message.getStyleClass().add("wrap-text");
    }

    //Top section
    private Label userInfoText = new Label("");
    private BorderPane TopBox = new BorderPane(); {

        MenuItem menuItem1 = new MenuItem("Accueil"); { menuItem1.setOnAction(action -> presenter.switchPane(0)); }
        MenuItem menuItem2 = new MenuItem("Nouveau"); { menuItem2.setOnAction(action -> presenter.switchPane(1)); }
        MenuItem menuItem3 = new MenuItem("Quitter"); { menuItem3.setOnAction(action -> quit()); }

        MenuButton topMenu = new MenuButton("Menu", null, menuItem1, menuItem2, menuItem3);

        TopBox.setLeft(topMenu);
        TopBox.setRight(userInfoText);

        TopBox.getStyleClass().add("top-box");
        userInfoText.getStyleClass().add("user-info-text");
    }

    private SubViewInterface[] subViews = new SubViewInterface[4];

    private StackPane viewStackPane = new StackPane();
    private BorderPane mainPane = new BorderPane();{
        mainPane.setTop(TopBox);

        mainPane.setCenter(viewStackPane);
        mainPane.setBottom(message);
    }

    public MainView(Stage primaryStage, MainPresenterInterface presenter) {
        this.primaryStage = primaryStage;
        this.presenter = presenter;

        presenter.setView(this);

        CreateBookView cbv = new CreateBookView(presenter);
        BookListView blv = new BookListView(presenter);

        ChoicesGestionView cgv = new ChoicesGestionView(presenter);
        PagesGestionView pgv = new PagesGestionView(presenter, cgv);

        subViews[0] = blv;
        subViews[1] = cbv;
        subViews[2] = pgv;
        subViews[3] = cgv;

        viewStackPane.getChildren().add(cbv);
        viewStackPane.getChildren().add(blv);
        viewStackPane.getChildren().add(pgv);
    }

    /**
     * Met en place les informations sur la vue
     */
    private void SetupView(String authoInfos) {
        userInfoText.setText("Auteur : " + authoInfos);

        switchPane(0);
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
    @Override
    public void showView(String authorInfos) {
        for(SubViewInterface sv : subViews) {
            sv.setPresenter();
        }

        SetupView(authorInfos);
        primaryStage.setScene(scene);
    }

    /**
     * Change de menu dans la vue principale
     * @param id (int) identifiant du panel
     */
    @Override
    public void switchPane(int id) {
        for(SubViewInterface subView : subViews) {
            subView.setVisibility(false);
        }

        subViews[id].setVisibility(true);
        refreshSubView(id);
    }

    /**
     * Rafraichis la sous-vue selon son identifiant
     * @param id (int)
     */
    @Override
    public void refreshSubView(int id) {
        subViews[id].refresh();
    }

    /**
     * Affiche un popup de demande de confirmation de suppression de page
     * @param text (String) texte du popup
     */
    @Override
    public boolean confirmDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text, ButtonType.CANCEL, ButtonType.APPLY);
        Optional<ButtonType> result =  alert.showAndWait();

        return result.get() == ButtonType.APPLY;
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

