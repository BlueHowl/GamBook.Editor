package org.helmo.gbeditor.infrastructures.dto;

import com.google.gson.annotations.SerializedName;
import org.helmo.gbeditor.models.Page;

/**
 * Classe Choice DTO
 */
public class ChoiceDTO {

    @SerializedName("Text")
    public final String text;

    @SerializedName("Ref")
    public final Page ref;

    /**
     * Constructeur de choix dto
     * @param text (String) texte du choix
     * @param ref (Page) page de redirection du choix
     */
    public ChoiceDTO(String text, Page ref) {
        this.text = text;
        this.ref = ref;
    }

    /**
     * Récupère le texte du choix
     * @return (String) texte
     */
    public String getText() {
        return text;
    }

    /**
     * Récupère la page de redirection u choix
     * @return (Page) page
     */
    public Page getRef() {
        return ref;
    }
}
