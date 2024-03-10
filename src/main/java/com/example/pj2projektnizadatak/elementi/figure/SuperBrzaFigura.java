package com.example.pj2projektnizadatak.elementi.figure;

import com.example.pj2projektnizadatak.*;
import com.example.pj2projektnizadatak.mapa.*;

import java.io.*;

public class SuperBrzaFigura extends Figura implements SuperBrzina {

    private static final String ZELENA_SUPER_BRZA ="zelenaSuperBrza.gif";
    private static final String CRVENA_SUPER_BRZA ="crvenaSuperBrza.gif";
    private static final String ZUTA_SUPER_BRZA ="zutaSuperBrza.gif";
    private static final String PLAVA_SUPER_BRZA ="plavaSuperBrza.gif";

    public SuperBrzaFigura(Boja boja) {
        super(boja, FiguraString.ObicnaFigura.toString());
        identifikator = Figura.id;
    }

    @Override
    public String toString() {
        return super.toString() + ", super brza figura, id=" + identifikator;
    }

    @Override
    public void obrisiSliku(int pozicija) {
        DiamondCricleController.removeImageFromMatrix(Putanja.put.get(pozicija),getSlikaSuperBrzaFigura());
    }

    @Override
    public void prikaziSliku(int pozicija) {
        DiamondCricleController.pomjeri(getSlikaSuperBrzaFigura(), Putanja.put.get(pozicija));
    }

    @Override
    public String getNaziv() {
        return "SUPER BRZA FIGURA";
    }

    private File getSlikaSuperBrzaFigura() {
        String path= PATH_FIGURA;
        switch (this.getBoja()) {
            case PLAVA: {
                path += File.separator + PLAVA_SUPER_BRZA;
                break;
            }
            case CRVENA: {
                path += File.separator + CRVENA_SUPER_BRZA;
                break;
            }
            case ZUTA: {
                path += File.separator + ZUTA_SUPER_BRZA;
                break;
            }case ZELENA:{
                path += File.separator + ZELENA_SUPER_BRZA;
                break;
            }
        }
        return new File(path);
    }

}
