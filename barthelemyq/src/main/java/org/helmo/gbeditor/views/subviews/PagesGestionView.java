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
import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;

import java.util.List;

public class PagesGestionView extends HBox {

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
        Label text = new Label("Numéro de page : ");

        pageNumberBox.getChildren().addAll(text, pageNumber);

        pageNumberBox.getStyleClass().add("");
    }

    //Page list

    private ListView<PageViewModel> list = new ListView<PageViewModel>();
    private ObservableList<PageViewModel> data = FXCollections.observableArrayList();

    private static class LabelCell extends ListCell<PageViewModel> {
        @Override
        public void updateItem(PageViewModel item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {//limite titre 15 premiers caracteres
                Label title = new Label(item.getPageNumber() + ". Résumé : " + item.getText().substring(0, Math.min(item.getText().length(), 30)) + "...");

                setGraphic(title);
            }
        }
    }

    private HBox container = new HBox(); {
        list.setItems(data);

        container.getChildren().addAll(list);

        list.setCellFactory(new Callback<ListView<PageViewModel>,
            ListCell<PageViewModel>>() {
                @Override
                public ListCell<PageViewModel> call(ListView<PageViewModel> list) {
                    return new PagesGestionView.LabelCell();
                }
            }
        );

        list.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<PageViewModel>() {
                    @Override
                    public void changed(ObservableValue<? extends PageViewModel> observable, PageViewModel oldValue, PageViewModel newValue) {
                        if(presenter != null && newValue != null) {
                            int index = data.indexOf(newValue);
                            presenter.setCurrentPage(index);
                            setPageDetails(newValue, index);

                            cgv.setChoiceListView(presenter.getPageChoices(index));
                        }
                    }
                });

        list.getSelectionModel().select(0);
    }

    private Label wordCount = new Label("Caractères : 0");
    private TextArea pageText = new TextArea(); {
        pageText.setPrefRowCount(12);
        pageText.setPrefColumnCount(100);
        pageText.setWrapText(true);
        pageText.setPrefWidth(600);

        int maxLength = 3000;
        pageText.textProperty().addListener((observable, oldValue, newValue) -> {
            int wc = newValue.length();
            if (newValue.length() > maxLength) {
                pageText.setText(oldValue);
                --wc;
            }
            wordCount.setText("Caractères : " + wc);
        });
    }

    private VBox pageTextBox = new VBox(); {
        Label text = new Label("Texte :");

        pageTextBox.getChildren().addAll(text, pageText, wordCount);

        pageTextBox.getStyleClass().add("");
    }

    private ChoicesGestionView cgv;

    private VBox editPage = new VBox(); {
        BorderPane bottomPane = new BorderPane(); {
            Button btnModifyElement = new Button("Modifier la page"); {
                btnModifyElement.setOnAction(action -> presenter.modifyPageOfCurrentBook(Integer.parseInt(pageNumber.getText()) - 1, pageText.getText())); //TODO gerer exception parseint
            }

            bottomPane.setLeft(pageNumberBox);
            bottomPane.setRight(btnModifyElement);
        }

        editPage.getChildren().addAll(pageTextBox, bottomPane);
    }

    //toolBar

    private VBox toolBar = new VBox(); {
        ImageView addElementUnderLogo = new ImageView(getClass().getResource("/images/addElementUnder.png").toExternalForm()); {
            addElementUnderLogo.setPreserveRatio(true);
            addElementUnderLogo.setFitWidth(16);
        }

        ImageView deleteElementLogo = new ImageView(getClass().getResource("/images/deleteSelected.png").toExternalForm()); {
            deleteElementLogo.setPreserveRatio(true);
            deleteElementLogo.setFitWidth(16);
        }


        Button btnAddElementUnder = new Button(); {
            btnAddElementUnder.setOnAction(action -> presenter.addPageToCurrentBook(Integer.parseInt(pageNumber.getText()), pageText.getText())); //TODO gerer exception parseint

            btnAddElementUnder.setGraphic(addElementUnderLogo);
        }

        Button btnDeleteElement = new Button(); {
            //btnDeleteElement.setOnAction(action -> );

            btnDeleteElement.setGraphic(deleteElementLogo);
        }

        toolBar.getChildren().add(btnAddElementUnder);
        toolBar.getChildren().add(btnDeleteElement);
    }

    /**
     * Constructeur de la sous vue
     * @param presenter (MainPresenterInterface)
     */
    public PagesGestionView(MainPresenterInterface presenter) {
        this.presenter = presenter;

        editPage.getChildren().add(cgv = new ChoicesGestionView(presenter));

        this.getChildren().addAll(container, toolBar, editPage);

        this.setVisible(false);
    }

    /**
     * Défini la liste de pages
     * affiche les livres de l'auteur
     * @param pageViewModelList (List<PageViewModel>) liste de pages à afficher
     */
    public void setPageListView(List<PageViewModel> pageViewModelList) {
        data.setAll(pageViewModelList);
        setPageDetails(new PageViewModel("", 1), pageViewModelList.size()); //affiche de base une page vide avec le numéro de la prochaine page
    }

    /**
     * Affiche les informations de la page donnée dans l'onglet de modification de page
     * @param pageViewModel (PageViewModel) page à afficher
     * @param pageNum (int) numéro de la page
     */
    private void setPageDetails(PageViewModel pageViewModel, int pageNum) {
        pageText.setText(pageViewModel.getText());
        pageNumber.setText(String.valueOf(pageNum+1));
    }

}
