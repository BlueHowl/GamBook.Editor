package org.helmo.gbeditor.infrastructures.dto;

import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.utils.InputUtil;

public class ChoiceDTO {
    private String text;

    private Page ref;

    public ChoiceDTO(String text, Page ref) {
        if(InputUtil.isEmptyOrBlank(text)) {
            //TODO throw error
        } else if (ref == null) {
            //todo throw err
        }

        this.text = text;
        this.ref = ref;
    }

    public String getText() {
        return text;
    }

    public Page getRef() {
        return ref;
    }
}
