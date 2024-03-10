package com.example.pj2projektnizadatak;

import com.example.pj2projektnizadatak.elementi.figure.*;
import com.example.pj2projektnizadatak.mapa.*;

import java.util.*;

public class Igrac {

    private String ime;
    private Boja boja;
    private final List<Figura> figure = new ArrayList<>();
    private static Koordinate krajPutanje;
    private Figura trenutnaFigura = null;

    public Figura getTrenutnaFigura() {
        return trenutnaFigura;
    }

    static {
        setEnd();
    }

    public Igrac() {
    }

    public Igrac(String ime, Boja boja) {
        this.ime = ime;
        this.boja = boja;
        this.trenutnaFigura = null;

        for (int i = 0; i < 4; i++) {
            FiguraString figuraString = FiguraString.randomFigura();
        //    System.out.println(figuraString);
            if (FiguraString.ObicnaFigura.equals(figuraString)) {
                figure.add(i, new ObicnaFigura(boja));
            } else if (FiguraString.LebdecaFigura.equals(figuraString)) {
                figure.add(i, new LebdecaFigura(boja));
            } else {
                figure.add(i, new SuperBrzaFigura(boja));
            }
        }
        for(int i=0; i<3; i++){
            if(figure.get(i).getBoja() != figure.get(i+1).getBoja()){
                System.out.println("Boje figura nisu dobre");
                System.exit(-1);
            }
        }
        System.out.println("Kreiran je " + this);
    }

    public String getIme() {
        return ime;
    }

    public List<Figura> getFigure() {
        return figure;
    }

    public Boja getBoja() {
        return boja;
    }

    @Override
    public String toString() {
        String figureString = ime + ", " + boja + "\n";
        if (figure.size() > 0) {
            for (Figura f : figure
            ) {
                figureString += f.toString() + " ";
            }
            return figureString + "\n";
        }
        return figureString + " Sve figure zavrsile igru";
    }

    private static void setEnd() {
        Collection<Koordinate> putanja = Putanja.getPut().values();
        krajPutanje = putanja.stream().reduce((prev, next) -> next).orElse(null);
    }

    public static Koordinate getKrajPutanje() {
        return krajPutanje;
    }

    public synchronized void pokreniNovuFiguru() {
        if (figure.size() > 0) {
       //     System.out.println("===================   " + figure.size() + "  ============================");
            trenutnaFigura = figure.get(0);
            trenutnaFigura.setPozicijaFigure(1);
            trenutnaFigura.setPredjiPolja(0);
            trenutnaFigura.setVrijemeKretanja(0);
            trenutnaFigura.setPropala(false);
            trenutnaFigura.setKrajFigure(false);
            trenutnaFigura.setStartVrijemeFigure();

            figure.remove(0);

            Thread trenutnaFiguraThread = new Thread(trenutnaFigura);
            trenutnaFiguraThread.start();

        } else {
                System.out.println(this.getIme() + " nema vise figura");
        }
    }
    public boolean aktivanIgrac() {
        return figure.size() > 0 || !trenutnaFigura.isKrajFigure();
    }
}


