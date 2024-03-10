package com.example.pj2projektnizadatak.elementi.figure;

import com.example.pj2projektnizadatak.*;
import com.example.pj2projektnizadatak.mapa.*;
import java.io.*;

public class LebdecaFigura extends Figura implements Lebdenje {

    private static final String LEBDECA_ZELENA ="GreenBird.png";
    private static final String LEBDECA_CRVENA ="RedBird.png";
    private static final String LEBDECA_PLAVA ="BlueBird.png";
    private static final String LEBDECA_ZUTA ="YellowBird.png";

    public LebdecaFigura(Boja boja) {
        super(boja, FiguraString.LebdecaFigura.toString());
        identifikator = Figura.id;
    }

    @Override
    public String toString() {
        return super.toString() + ", lebdeca figura, id=" + identifikator;
    }

    @Override
    public void obrisiSliku(int pozicija) {
        DiamondCricleController.removeImageFromMatrix(Putanja.put.get(pozicija), this.getSlikaLebdecaFigura());
    }

    @Override
    public void prikaziSliku(int pozicija) {
        DiamondCricleController.pomjeri(getSlikaLebdecaFigura(), Putanja.put.get(pozicija));
    }

    @Override
    public String getNaziv() {
        return "LEBDECA FIGURA";
    }

    private File getSlikaLebdecaFigura() {
        String path= PATH_FIGURA;
        switch (this.getBoja()) {
            case PLAVA: {
                path += File.separator + LEBDECA_PLAVA;
                break;
            }
            case CRVENA: {
                path += File.separator + LEBDECA_CRVENA;
                break;
            }
            case ZUTA: {
                path += File.separator + LEBDECA_ZUTA;
                break;
            }case ZELENA:{
                path += File.separator + LEBDECA_ZELENA;
                break;
            }
        }
        return new File(path);
    }
}
