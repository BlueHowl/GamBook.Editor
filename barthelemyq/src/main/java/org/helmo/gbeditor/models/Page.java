package org.helmo.gbeditor.models;

import java.util.Collection;
import java.util.List;

public class Page {

    private String text;

    private int pageNumber;

    private List<Choice> choices; //TODO choisir type de liste

    public Page(String text, int pageNumber, Collection<Choice> choices) {
        this.text = text;
        this.pageNumber = pageNumber;
        this.choices = List.copyOf(choices); //copie la liste
    }
}
