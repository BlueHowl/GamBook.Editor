package org.helmo.gbeditor.infrastructures.dto;

import com.google.gson.annotations.SerializedName;
import org.helmo.gbeditor.models.Choice;
import org.helmo.gbeditor.utils.InputUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PageDTO {

    @SerializedName("Text")
    public String text;

    @SerializedName("Choices")
    public List<ChoiceDTO> choices; //List ArrayList pour ajouter des elements au milieu

    public PageDTO(String text, Collection<ChoiceDTO> choices) {
        if(InputUtil.isEmptyOrBlank(text)) {
            //TODO throw error
        }

        this.text = text;
        this.choices = (choices == null || choices.isEmpty()) ? new ArrayList<>() : new ArrayList<>(choices); //copie la liste
    }

    public String getText() {
        return text;
    }

    public List<ChoiceDTO> getChoices() {
        return choices;
    }
}
