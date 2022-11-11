package org.helmo.gbeditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.helmo.gbeditor.infrastructures.JsonRepository;
import org.helmo.gbeditor.infrastructures.jdbc.SqlStorage;
import org.helmo.gbeditor.infrastructures.jdbc.SqlStorageFactory;
import org.helmo.gbeditor.presenters.LoginPresenter;
import org.helmo.gbeditor.presenters.MainPresenter;
import org.helmo.gbeditor.presenters.interfaces.presenters.LoginPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.repositories.exceptions.ConnectionFailedException;
import org.helmo.gbeditor.views.LoginView;
import org.helmo.gbeditor.views.MainView;

//@SuppressWarnings("PMD")

/**
 * Classe principale de l'application
 */
public class App extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    private static final SqlStorageFactory factory = new SqlStorageFactory(
            "com.mysql.cj.jdbc.Driver",
            "jdbc:mySQL://192.168.128.13:3306/in21b10043",
            "in21b10043",
            "0043"
    );

    @Override
    public void start(Stage primaryStage) {

        //Création du repository json
        /*
        DataInterface jsonRepository = null;
        try {
            jsonRepository = new JsonRepository();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur système fichiers", ButtonType.OK);
            alert.showAndWait();

            System.exit(-1);
        }
        */

        try {
            SqlStorage storage = factory.newStorageSession(); //TODO remonter exception afin d'afficher popup erreur


            //Main Presenter
            MainPresenterInterface mainPresenter = new MainPresenter(storage);
            //Login Presenter
            LoginPresenterInterface loginPresenter = new LoginPresenter(storage, mainPresenter); //prend le mainPresenter en param afin d'appeler le changement de Vue


            //LoginView : Crée une loginView, récupère son scenegraph, crée une scene avec le scenegraph et applique une feuille de style à la scene
            LoginView loginView = new LoginView(loginPresenter);
            Scene loginScene = new Scene(loginView.getRoot(), 600, 400);
            loginScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());


            //LoginView : Crée une MainView, récupère son scenegraph, crée une scene avec le scenegraph et applique une feuille de style à la scene
            MainView mainView = new MainView(primaryStage, mainPresenter);
            Scene mainScene = new Scene(mainView.getRoot(), 900, 600);
            mainScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            //Renseigne la scene, permet de changer de vue
            mainView.setScene(mainScene);


            //fenetre principale (1ere vue affichée LoginView)
            primaryStage.setTitle("GBEditor-2022 Barthélemy Quentin");
            primaryStage.setScene(loginScene);
            primaryStage.show();


            primaryStage.setOnCloseRequest(event -> {
                storage.close(); //ferme la connexion manuellement
            });

        } catch (ConnectionFailedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();

            System.exit(-1);
        }
    }

}