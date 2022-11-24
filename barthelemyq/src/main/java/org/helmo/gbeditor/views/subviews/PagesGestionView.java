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
import org.helmo.gbeditor.presenters.interfaces.presenters.subpresenters.PageGestionPInterface;
import org.helmo.gbeditor.presenters.interfaces.views.subviews.SubViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;

import java.util.List;

public class PagesGestionView extends HBox implements SubViewInterface {

    private final MainPresenterInterface presenter;
    private PageGestionPInterface pageGestionPresenter;

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
            if (item != null && !empty) {//limite titre 15 premiers caracteres
                Label title = new Label(item.getPageNumber() + ". Résumé : " + item.getText().substring(0, Math.min(item.getText().length(), 30)) + "...");

                setGraphic(title);
            } else {
                setGraphic(null);
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

                            cgv.refresh();
                        }
                    }
                });

        list.getSelectionModel().select(0);
    }

    private Label wordCount = new Label("Caractères : 0");
    private TextArea pageText = new TextArea(); {
        pageText.setPrefRowCount(14);
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
            HBox hBtnBox = new HBox();

            Button btnAddElement = new Button("Ajouter la page"); {
                btnAddElement.setOnAction(action -> pageGestionPresenter.addPageToCurrentBook(pageNumber.getText(), pageText.getText(), -1));
            }

            Button btnModifyElement = new Button("Modifier la page"); {
                btnModifyElement.setOnAction(action -> pageGestionPresenter.modifyPageOfCurrentBook(pageNumber.getText(), pageText.getText()));
            }

            hBtnBox.getChildren().addAll(btnAddElement, btnModifyElement);

            bottomPane.setLeft(pageNumberBox);
            bottomPane.setRight(hBtnBox);
        }

        editPage.getChildren().addAll(pageTextBox, bottomPane);
    }

    private VBox editPageSection = new VBox(); {
        Button btnSavePages = new Button("Sauvegarder les pages"); {
            btnSavePages.setOnAction(action -> pageGestionPresenter.saveBookPages());
        }

        editPageSection.getChildren().addAll(editPage, btnSavePages);
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
            btnAddElementUnder.setOnAction(action -> pageGestionPresenter.addPageToCurrentBook(pageNumber.getText(), pageText.getText(), 0));

            btnAddElementUnder.setGraphic(addElementUnderLogo);
        }

        Button btnDeleteElement = new Button(); {
            btnDeleteElement.setOnAction(action -> pageGestionPresenter.safeRemoveCurrentPage());

            btnDeleteElement.setGraphic(deleteElementLogo);
        }

        toolBar.getChildren().add(btnAddElementUnder);
        toolBar.getChildren().add(btnDeleteElement);
    }

    /**
     * Constructeur de la sous vue
     * @param presenter (MainPresenterInterface)
     */
    public PagesGestionView(MainPresenterInterface presenter, ChoicesGestionView cgv) {
        this.presenter = presenter;

        editPage.getChildren().add(this.cgv = cgv);

        this.getChildren().addAll(container, toolBar, editPageSection);

        this.setVisible(false);
    }

    /**
     * Assigne le presentateur spécifique
     */
    public void setPresenter() {
        pageGestionPresenter = (PageGestionPInterface) presenter.getSubPresenters(2);
    }

    /**
     * Défini la liste de pages
     * affiche les livres de l'auteur
     */
    @Override
    public void refresh() {
        List<PageViewModel> pageViewModelList = pageGestionPresenter.getBookPages();

        data.setAll(pageViewModelList);
        list.refresh();

        setPageDetails(new PageViewModel("", 1), pageViewModelList.size()); //affiche de base une page vide avec le numéro de la prochaine page
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
     * Affiche les informations de la page donnée dans l'onglet de modification de page
     * @param pageViewModel (PageViewModel) page à afficher
     * @param pageNum (int) numéro de la page
     */
    private void setPageDetails(PageViewModel pageViewModel, int pageNum) {
        pageText.setText(pageViewModel.getText());
        pageNumber.setText(String.valueOf(pageNum+1));
    }

}
