package com.example.pj2projektnizadatak.karte;

import java.io.*;
import java.util.*;

public class Spil {

    private final List<Karta> spilKarata = new ArrayList<>();

    private final static File SPECIJALNE_KARTE = new File(System.getProperty("user.dir") + File.separator + "slike/karte/specijalne/specialCard.png");
    private final static File OBICNE_KARTE = new File(System.getProperty("user.dir") + File.separator + "slike/karte/obicne");

    public Spil() {
        formirajSpil();
        Collections.shuffle(spilKarata);
    }

    public Karta getKartaIzSpila() {
        Karta karta = spilKarata.get(0);
        spilKarata.remove(0);
        spilKarata.add(spilKarata.size(), karta);
        return karta;
    }

    private void setSpecijalneKarte() {

        for (int i=0; i<12; i++){
            spilKarata.add(new SpecijalnaKarta(SPECIJALNE_KARTE.toString()));
        }
    }

    private void setObicneKarte() {

        for(int i = 0; i<10; i++){
            spilKarata.add(new ObicnaKarta(1, new File( OBICNE_KARTE + File.separator +  "one.png").getAbsolutePath()));
            spilKarata.add(new ObicnaKarta(2, new File( OBICNE_KARTE + File.separator + "two.png").getAbsolutePath()));
            spilKarata.add(new ObicnaKarta(3, new File(OBICNE_KARTE + File.separator +  "three.png").getAbsolutePath()));
            spilKarata.add(new ObicnaKarta(4, new File(OBICNE_KARTE + File.separator + "four.png").getAbsolutePath()));
        }
    }

    public void formirajSpil() {
        setObicneKarte();
        setSpecijalneKarte();
    }

    @Override
    public String toString() {
        String stringSpil = "";
        for (Karta k : spilKarata) {
            stringSpil += k.toString() + "\n";
        }
        return stringSpil;
    }
}
