package com.example.pj2projektnizadatak.mapa;

import com.example.pj2projektnizadatak.*;

import java.util.*;

public class Mapa {

    public static int DIMENZIJA_MATRICE;
    public static Polje[][] mapa;

    public Mapa() {
        DIMENZIJA_MATRICE = DiamondCricleController.DIMENZIJA_MATRICE;
        mapa = new Polje[DIMENZIJA_MATRICE][DIMENZIJA_MATRICE];
        for (Map.Entry<Integer, Koordinate> entry : Putanja.getPut().entrySet()) {
            Koordinate koordinate = entry.getValue();
            mapa[koordinate.i][koordinate.j] = new Polje();
     //       System.out.println("Stavljam na mapu " + entry.getValue());
        }
    }

    @Override
    public String toString() {
        for (int i = 0; i < DIMENZIJA_MATRICE; i++) {
            for (int j = 0; j < DIMENZIJA_MATRICE; j++) {
           //     if ((Mapa.mapa[i][j]) instanceof Polje && Mapa.mapa[i][j].getElement() != null) {
            //        System.out.println(Mapa.mapa[i][j]);
           //     }
            }
        }
        return "";
    }
}
