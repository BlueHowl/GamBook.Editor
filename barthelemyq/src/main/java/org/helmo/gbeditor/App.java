package org.helmo.gbeditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.helmo.gbeditor.infrastructures.JsonRepository;
import org.helmo.gbeditor.presenters.LoginPresenter;
import org.helmo.gbeditor.presenters.MainPresenter;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.repositories.DataInterface;
import org.helmo.gbeditor.views.LoginView;
import org.helmo.gbeditor.views.MainView;

import java.io.IOException;

//@SuppressWarnings("PMD")

/**
 * Classe principale de l'application
 */
public class App extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //LoginView : Crée une loginView, récupère son scenegraph, crée une scene avec le scenegraph et applique une feuille de style à la scene
        LoginView loginView = new LoginView();
        Scene loginScene = new Scene(loginView.getRoot(), 600, 400);
        loginScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());


        //LoginView : Crée une MainView, récupère son scenegraph, crée une scene avec le scenegraph et applique une feuille de style à la scene
        MainView mainView = new MainView(primaryStage);
        Scene mainScene = new Scene(mainView.getRoot(), 900, 600);
        mainScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        //Renseigne la scene, permet de changer de vue
        mainView.setScene(mainScene);

        //Création du modéle métier principal
        //EditorInterface editor = new Editor();

        //Création du repository json
        DataInterface jsonRepository = null;
        try {
            jsonRepository = new JsonRepository();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur système fichiers", ButtonType.OK);
            alert.showAndWait();

            System.exit(-1);
        }

        //Main Presenter
        MainPresenterInterface mainPresenter = new MainPresenter(mainView, jsonRepository);
        //Login Presenter
        new LoginPresenter(loginView, jsonRepository, mainPresenter); //prend le mainPresenter en param afin d'appeler le changement de Vue

        //fenetre principale (1ere vue affichée LoginView)
        primaryStage.setTitle("GBEditor-2022 Barthélemy Quentin");
        primaryStage.setScene(loginScene);
        primaryStage.show();


    }

}