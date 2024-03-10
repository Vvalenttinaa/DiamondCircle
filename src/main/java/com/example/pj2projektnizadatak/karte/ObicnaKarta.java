package com.example.pj2projektnizadatak.karte;

public class ObicnaKarta extends Karta{
    private int brojPolja;

    public ObicnaKarta(int brojPolja, String slika){
        super(slika);
        this.brojPolja=brojPolja;
    }

    public int getBrojPolja() {
        return brojPolja;
    }

    @Override
    public String toString(){
        return this.getClass().toString() + " " + brojPolja;
    }
}
