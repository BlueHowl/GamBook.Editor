package org.helmo.gbeditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.helmo.gbeditor.infrastructures.jdbc.SqlStorageFactory;
import org.helmo.gbeditor.presenters.LoginPresenter;
import org.helmo.gbeditor.presenters.MainPresenter;
import org.helmo.gbeditor.presenters.interfaces.presenters.LoginPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.repositories.StorageFactoryInterface;
import org.helmo.gbeditor.views.LoginView;
import org.helmo.gbeditor.views.MainView;

/**
 * Classe principale de l'application
 */
public class App extends Application {
    /**
     * Factory de SqlStorage
     */
    private static final StorageFactoryInterface FACTORY = new SqlStorageFactory(
            "com.mysql.cj.jdbc.Driver",
            "jdbc:mySQL://IP:PORT",
            "USERNAME",
            "PASSWORD"
    );

    /**
     * Méthode de démarrage du programme
     * @param args (String[])
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Méthode start
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        //Permet d'éviter l'exception de getBooleanProperty dans PropertyHelper lors du debug
        System.setProperty("javafx.sg.warn", "true"); //TODO prblm code javafx ?


        //Main Presenter
        MainPresenterInterface mainPresenter = new MainPresenter(FACTORY);
        //Login Presenter
        LoginPresenterInterface loginPresenter = new LoginPresenter(FACTORY, mainPresenter); //prend le mainPresenter en param afin d'appeler le changement de Vue


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

    }

}