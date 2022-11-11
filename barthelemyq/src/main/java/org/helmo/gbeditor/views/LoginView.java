package org.helmo.gbeditor.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.helmo.gbeditor.presenters.interfaces.presenters.LoginPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.ViewInterface;

/**
 * Vue
 * @author Quentin Barthélemy Q210043
 * @version 1.0
 */
public class LoginView implements ViewInterface {
    private LoginPresenterInterface presenter;

    private Label message = new Label(); {
        message.getStyleClass().add("wrap-text");
    }

    private VBox header = new VBox();{
        Label title = new Label("Connexion");
        header.getChildren().add(title);

        header.setAlignment(Pos.CENTER);

        title.getStyleClass().add("title");
    }

    private TextField surname = new TextField(); {
        surname.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Z '-]*$")) {
                surname.setText(oldValue);
            }
        });
    }

    private BorderPane surnameBox = new BorderPane(); {
        Label text = new Label("Prénom ");

        surnameBox.setLeft(text);
        surnameBox.setRight(surname);

        surnameBox.getStyleClass().add("login-boxes");
    }

    private TextField name = new TextField(); {
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Z '-]*$")) {
                name.setText(oldValue);
            }
        });
    }

    private BorderPane nameBox = new BorderPane(); {
        Label text = new Label("Nom ");

        nameBox.setLeft(text);
        nameBox.setRight(name);

        nameBox.getStyleClass().add("login-boxes");
    }

    private TextField id = new TextField(); {
        id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 6 || !newValue.matches("\\d*")) {
                id.setText(oldValue);
            }
        });
    }

    private BorderPane idBox = new BorderPane(); {
        Label text = new Label("Matricule ");

        idBox.setLeft(text);
        idBox.setRight(id);

        idBox.getStyleClass().add("login-boxes");
    }

    private BorderPane authBtnBox = new BorderPane(); {
        Button loginButton = new Button("S'authentifier"); {
            loginButton.setOnAction(action -> presenter.login(id.getText(), surname.getText(), name.getText()));

            loginButton.getStyleClass().add("auth-button");
        }

        authBtnBox.setCenter(loginButton);

        authBtnBox.getStyleClass().add("auth-box");
    }

    //Panneau de login
    private GridPane loginPane = new GridPane();{
        loginPane.add(surnameBox, 0, 0);
        loginPane.add(nameBox, 0, 1);
        loginPane.add(idBox, 0, 2);

        loginPane.add(authBtnBox, 0, 3);

        loginPane.add(message,  0, 4);
        GridPane.setColumnSpan(message, GridPane.REMAINING);

        loginPane.setAlignment(Pos.CENTER);
    }


    private BorderPane mainPane = new BorderPane();{
        mainPane.setTop(header);
        mainPane.setCenter(loginPane);
    }

    public LoginView(LoginPresenterInterface presenter) {
        this.presenter = presenter;

        presenter.setView(this);
    }


    /**
     * Retourne le scenegraph
     * @return
     */
    public Parent getRoot() {
        return mainPane;
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

