package com.example.pj2projektnizadatak.elementi.figure;

import com.example.pj2projektnizadatak.*;
import com.example.pj2projektnizadatak.mapa.*;

import java.io.*;

public class ObicnaFigura extends Figura {

    private static final String ZUTA_OBICNA ="zutaObicna.png";
    private static final String ZELENA_OBICNA ="zelenaObicna.png";
    private static final String CRVENA_OBICNA ="crvenaObicna.png";
    private static final String PLAVA_OBICNA ="plavaObicna.png";

    public ObicnaFigura(Boja boja) {
        super(boja, FiguraString.ObicnaFigura.toString());
        identifikator = Figura.id;
    }

    @Override
    public String toString() {
        return super.toString() + ", obicna figura, id=" + identifikator;
    }

    @Override
    public void obrisiSliku(int pozicija) {
        DiamondCricleController.removeImageFromMatrix(Putanja.put.get(pozicija), getSlikaObicnaFigura());
    }

    @Override
    public void prikaziSliku(int pozicija) {
        DiamondCricleController.pomjeri(getSlikaObicnaFigura(), Putanja.put.get(pozicija));
    }

    @Override
    public String getNaziv() {
        return "OBICNA FIGURA";
    }

    public File getSlikaObicnaFigura() {
        String path= PATH_FIGURA;
        switch (this.getBoja()) {
            case PLAVA: {
                path += File.separator + PLAVA_OBICNA;
                break;
            }
            case CRVENA: {
                path += File.separator + CRVENA_OBICNA;
                break;
            }
            case ZUTA: {
                path += File.separator + ZUTA_OBICNA;
                break;
            }
            case ZELENA: {
                path += File.separator + ZELENA_OBICNA;
                break;
            }
        }
        return new File(path);
    }
}