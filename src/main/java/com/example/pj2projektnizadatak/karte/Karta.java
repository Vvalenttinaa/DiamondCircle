package com.example.pj2projektnizadatak.karte;

public abstract class Karta {

    protected String slikaPath;

    public Karta(String slika){
        this.slikaPath=slika;
    }

    public String getSlika() {
        return slikaPath;
    }

    @Override
    public String toString(){
        return this.getClass().toString();
    }
}
