package com.example.pj2projektnizadatak.mapa;


import com.example.pj2projektnizadatak.elementi.*;

public class Polje {
    private Element element;

    public Polje() {
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    @Override
    public String toString() {
        try {
            return element.toString();
        } catch (NullPointerException e) {
            return "Prazno polje";
        }
    }
}
