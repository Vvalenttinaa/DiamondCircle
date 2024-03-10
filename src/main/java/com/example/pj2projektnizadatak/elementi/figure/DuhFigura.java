package com.example.pj2projektnizadatak.elementi.figure;

import com.example.pj2projektnizadatak.*;
import com.example.pj2projektnizadatak.elementi.*;
import com.example.pj2projektnizadatak.mapa.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import static com.example.pj2projektnizadatak.GameController.*;

public class DuhFigura extends Figura implements Runnable {
    private int brojDijamanata;
    private static Random random = new Random();
    private static final Collection<Koordinate> putanja = Putanja.getPut().values();
    private final List<Koordinate> trenutniBonusi = new ArrayList<>();
    private static final String BONUS_SLIKA = System.getProperty("user.dir") + File.separator + "slike/bonus.png";

    public DuhFigura() {
        super(Boja.BEZBOJNA, "DuhFigura");
    }

    private void generisiBrojDijamanata() {
        brojDijamanata = random.nextInt((Mapa.DIMENZIJA_MATRICE - 2) + 2);
    }

    private Object getRandomPolje() {
        int i = random.nextInt(putanja.size());
        return putanja.toArray()[i];
    }

    public void setDijamanti() {
        generisiBrojDijamanata();
   //     System.out.println("Generisacu " + brojDijamanata + " dijamanata");
        while (brojDijamanata > 0) {
            Koordinate koordinateDijamanta = (Koordinate) getRandomPolje();
            synchronized (Mapa.mapa[koordinateDijamanta.i][koordinateDijamanta.j]) {
                if ((Mapa.mapa[koordinateDijamanta.i][koordinateDijamanta.j].getElement() == null)) {
                    Mapa.mapa[koordinateDijamanta.i][koordinateDijamanta.j].setElement(new Dijamant());
                    DiamondCricleController.addToMatrix(koordinateDijamanta, new File(BONUS_SLIKA));
                    brojDijamanata--;
                    trenutniBonusi.add(koordinateDijamanta);
                }
            }
        }
    }

    public void deleteDijamanti() {
        for (int i = 0; i < trenutniBonusi.size(); i++) {
            Koordinate k = trenutniBonusi.get(i);
            synchronized (Mapa.mapa[k.i][k.j]) {
                if (Mapa.mapa[k.i][k.j].getElement() instanceof Dijamant) {
                    Mapa.mapa[k.i][k.j] = new Polje();
                    DiamondCricleController.removeImageFromMatrix(k, new File(BONUS_SLIKA));
                }
            }
        }
        trenutniBonusi.clear();
    }

    @Override
    public void run() {
        while (!kraj) {
            if (pauzaNasljednik) {
          //      System.out.println("==========================================duh provjerava pauzu " + pauza);
                synchronized (this.lock) {
                    try {
            //            System.out.println("===================================duh ceka");
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                    }
                }
            }
          //  System.out.println("====================================Duh van pauze " + pauza);
            setDijamanti();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            }
            deleteDijamanti();
        }
    }

    @Override
    public void obrisiSliku(int pozicija) {

    }

    @Override
    public void prikaziSliku(int pozicija) {

    }

    @Override
    public String getNaziv() {
        return "DUH FIGURA";
    }
}

