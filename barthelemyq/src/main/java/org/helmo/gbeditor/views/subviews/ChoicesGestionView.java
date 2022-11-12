package org.helmo.gbeditor.views.subviews;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.helmo.gbeditor.presenters.interfaces.presenters.MainPresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.views.subviews.SubViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.ChoiceViewModel;

//todo add to subviewinterface ?
public class ChoicesGestionView extends HBox implements SubViewInterface {

    private MainPresenterInterface presenter;

    private TextField pageNumber = new TextField(); {
        pageNumber.setPrefWidth(60);

        pageNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                pageNumber.setText(oldValue);
            }
        });

    }

    private HBox pageNumberBox = new HBox(); {
        Label text = new Label("Numéro de page à lier : ");

        pageNumberBox.getChildren().addAll(text, pageNumber);

        pageNumberBox.getStyleClass().add("");
    }

    //Page list

    private ListView<ChoiceViewModel> list = new ListView<ChoiceViewModel>();
    private ObservableList<ChoiceViewModel> data = FXCollections.observableArrayList();

    private static class LabelCell extends ListCell<ChoiceViewModel> {
        @Override
        public void updateItem(ChoiceViewModel item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {//limite titre 15 premiers caracteres
                Label title = new Label(item.getText().substring(0, Math.min(item.getText().length(), 25)) + "... -> " + item.getRefPageNumber());

                setGraphic(title);
            }
        }
    }

    private HBox container = new HBox(); {
        list.setItems(data);

        container.getChildren().addAll(list);

        list.setCellFactory(new Callback<ListView<ChoiceViewModel>,
            ListCell<ChoiceViewModel>>() {
                @Override
                public ListCell<ChoiceViewModel> call(ListView<ChoiceViewModel> list) {
                    return new ChoicesGestionView.LabelCell();
                }
            }
        );

        list.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<ChoiceViewModel>() {
                @Override
                public void changed(ObservableValue<? extends ChoiceViewModel> observable, ChoiceViewModel oldValue, ChoiceViewModel newValue) {
                    if(presenter != null && newValue != null) {
                        int index = data.indexOf(newValue);
                        presenter.setCurrentChoice(index);
                        setChoiceDetails(newValue);
                    }
                }
            });

        list.getSelectionModel().select(0);
    }

    private Label wordCount = new Label("Caractères : 0");
    private TextArea choiceText = new TextArea(); {
        choiceText.setPrefRowCount(4);
        choiceText.setPrefColumnCount(100);
        choiceText.setWrapText(true);
        choiceText.setPrefWidth(300);

        int maxLength = 250;
        choiceText.textProperty().addListener((observable, oldValue, newValue) -> {
            int wc = newValue.length();
            if (newValue.length() > maxLength) {
                choiceText.setText(oldValue);
                --wc;
            }
            wordCount.setText("Caractères : " + wc);
        });
    }

    private VBox pageTextBox = new VBox(); {
        Label text = new Label("Texte :");

        pageTextBox.getChildren().addAll(text, choiceText, wordCount);

        pageTextBox.getStyleClass().add("");
    }

    private VBox editPage = new VBox(); {
        BorderPane bottomPane = new BorderPane(); {
            Button btnModifyElement = new Button("Modifier le choix"); {
                btnModifyElement.setOnAction(action -> presenter.modifyChoiceOfCurrentPage(choiceText.getText(), Integer.parseInt(pageNumber.getText()) - 1));
                //TODO gerer exception parseint
            }

            bottomPane.setLeft(pageNumberBox);
            bottomPane.setRight(btnModifyElement);
        }

        editPage.getChildren().addAll(pageTextBox, bottomPane);
    }

    //toolBar

    private VBox toolBar = new VBox(); {
        ImageView addElementUnderLogo = new ImageView(getClass().getResource("/images/addElement.png").toExternalForm()); {
            addElementUnderLogo.setPreserveRatio(true);
            addElementUnderLogo.setFitWidth(16);
        }

        ImageView deleteElementLogo = new ImageView(getClass().getResource("/images/deleteSelected.png").toExternalForm()); {
            deleteElementLogo.setPreserveRatio(true);
            deleteElementLogo.setFitWidth(16);
        }


        Button btnAddElement = new Button(); {
            btnAddElement.setOnAction(action -> presenter.addChoiceToCurrentPage(choiceText.getText(), Integer.parseInt(pageNumber.getText())));
            //TODO gerer exception parseint

            btnAddElement.setGraphic(addElementUnderLogo);
        }

        Button btnDeleteElement = new Button(); {
            //btnDeleteElement.setOnAction(action -> );

            btnDeleteElement.setGraphic(deleteElementLogo);
        }

        toolBar.getChildren().add(btnAddElement);
        toolBar.getChildren().add(btnDeleteElement);
    }

    public ChoicesGestionView(MainPresenterInterface presenter) {
        this.presenter = presenter;

        this.getChildren().addAll(container, toolBar, editPage);
    }

    /**
     * Défini la liste de choix
     * affiche les choix de la page
     */
    @Override
    public void refresh() {
        data.setAll(presenter.getPageChoices());
    }

    /**
     * Fonction inutile à la vue des choix
     * @param b (boolean)
     */
    @Override
    public void setVisibility(boolean b) {}

    /**
     * Défini les détails du choix sélectionné
     * @param choiceViewModel (ChoiceViewModel)
     */
    private void setChoiceDetails(ChoiceViewModel choiceViewModel) {
        choiceText.setText(choiceViewModel.getText());
        pageNumber.setText(String.valueOf(choiceViewModel.getRefPageNumber()));
    }

}
