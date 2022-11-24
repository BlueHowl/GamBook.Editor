package org.helmo.gbeditor.infrastructures.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe Page DTO
 */
public class PageDTO {

    @SerializedName("Text")
    public final String text;

    @SerializedName("Choices")
    public final List<ChoiceDTO> choices;

    /**
     * Constructeur de page dto
     * @param text (String) texte de la page
     * @param choices (List<ChoiceDTO>) liste de choix dto
     */
    public PageDTO(String text, Collection<ChoiceDTO> choices) {
        this.text = text;
        this.choices = (choices == null || choices.isEmpty()) ? new ArrayList<>() : new ArrayList<>(choices); //copie la liste
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

}
