package com.example.pj2projektnizadatak.karte;

public class SpecijalnaKarta extends Karta{
    public SpecijalnaKarta(String slika){
        super(slika);
    }
    @Override
    public String toString(){
        return this.getClass().toString();
    }
}
