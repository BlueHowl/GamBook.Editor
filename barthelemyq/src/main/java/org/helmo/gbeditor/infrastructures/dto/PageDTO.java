package org.helmo.gbeditor.infrastructures.dto;

import com.google.gson.annotations.SerializedName;
import org.helmo.gbeditor.models.utils.InputUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe Page DTO
 */
public class PageDTO {

    @SerializedName("Text")
    public String text;

    @SerializedName("Choices")
    public List<ChoiceDTO> choices; //List ArrayList pour ajouter des elements au milieu

    @SerializedName("BookIsbn")
    public String bookIsbn;

    /**
     * Constructeur de page dto
     * @param text (String) texte de la page
     * @param choices (List<ChoiceDTO>) liste de choix dto
     * @param bookIsbn (String) id du livre qui contient la page
     */
    public PageDTO(String text, Collection<ChoiceDTO> choices, String bookIsbn) {
        if(InputUtil.isEmptyOrBlank(text)) {
            //TODO throw error
        }

        this.text = text;
        this.choices = (choices == null || choices.isEmpty()) ? new ArrayList<>() : new ArrayList<>(choices); //copie la liste
        this.bookIsbn = bookIsbn;
    }

    /**
     * Récupère le texte de la page
     * @return (String) texte de la page
     */
    public String getText() {
        return text;
    }

    /**
     * Récupère une liste des choix DTO de la page DTO
     * @return (List<ChoiceDTO>) liste de choix DTO
     */
    public List<ChoiceDTO> getChoices() {
        return choices;
    }

    /**
     * Récupère l'isbn du livre auquel appartient la page dto
     * @return (String) isbn
     */
    public String getBookIsbn() {
        return bookIsbn;
    }
}
