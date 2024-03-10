package com.example.pj2projektnizadatak.elementi.figure;

import com.example.pj2projektnizadatak.*;
import com.example.pj2projektnizadatak.elementi.*;
import com.example.pj2projektnizadatak.karte.*;
import com.example.pj2projektnizadatak.mapa.*;
import com.example.pj2projektnizadatak.utils.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import static com.example.pj2projektnizadatak.DiamondCricleController.*;
import static com.example.pj2projektnizadatak.GameController.*;
import static com.example.pj2projektnizadatak.mapa.Mapa.DIMENZIJA_MATRICE;
import static com.example.pj2projektnizadatak.mapa.Putanja.*;

public abstract class Figura extends Element implements Runnable {

    protected static final int SUPER_BRZINA = 2;

    protected static Properties pros = MyProperties.getProperties();
    protected static final String PATH_FIGURA = System.getProperty("user.dir") + File.separator + "slike/figure";

    protected Boja boja;
    protected static int id;
    protected int identifikator;
    protected int vrijemeKretanja;
    protected int pozicijaFigure;
    protected int predjiPolja;
    protected String vrsta;
    private boolean propala = false;
    private boolean krajFigure = false;
    public boolean pauzaNasljednik = false;

    private static long startVrijemeFigure;

    public Object lock = new Object();
    public Object lockFigure = new Object();
    public boolean pauzaFigure = false;

    public Figura(Boja boja, String vrsta) {
        id++;
        this.boja = boja;
        this.vrsta = vrsta;
    }

    public Boja getBoja() {
        return boja;
    }

    @Override
    public String toString() {
        return "Boja: " + boja.toString().toLowerCase();
    }

    public abstract void obrisiSliku(int pozicija);

    public abstract void prikaziSliku(int pozicija);

    public void setPozicijaFigure(int pozicijaFigure) {
        this.pozicijaFigure = pozicijaFigure;
    }

    public void setVrijemeKretanja(int vrijemeKretanja) {
        this.vrijemeKretanja = vrijemeKretanja;
    }

    public int getPozicijaFigure() {
        return this.pozicijaFigure;
    }

    public int getVrijemeKretanja() {
        return vrijemeKretanja;
    }

    public boolean isPropala() {
        return propala;
    }

    public void setPropala(boolean propala) {
        this.propala = propala;
    }

    public boolean isKrajFigure() {
        return krajFigure;
    }

    public void setKrajFigure(boolean krajFigure) {
        this.krajFigure = krajFigure;
    }

    public abstract String getNaziv();

    public int getId() {
        return this.identifikator;
    }

    public void setPredjiPolja(int predjiPolja) {
        this.predjiPolja = predjiPolja;
    }

    public int getPredjiPolja() {
        return predjiPolja;
    }

    public void setStartVrijemeFigure(){
        startVrijemeFigure=System.currentTimeMillis();
    }

    public long getVrijemeKretanjaFigure(){
        return (System.currentTimeMillis() - startVrijemeFigure)/1000;
    }

    protected void obrisiPolje(int pozicija) {
         synchronized (Mapa.mapa[getI(pozicija)][getJ(pozicija)]) {
        Mapa.mapa[getI(pozicija)][getJ(pozicija)] = new Polje();
          }
    }

    protected void postaviFiguruNaPolje(int pozicija) {
        synchronized (Mapa.mapa[getI(pozicija)][getJ(pozicija)]) {
            Mapa.mapa[getI(pozicija)][getJ(pozicija)].setElement(this);
        }
    }

    @Override
    public void run() {
        while (!krajFigure) {
            synchronized (this.lockFigure) {
                checkKrajFigure();
                if (!krajFigure)
                    obradiKartu();
                //    System.out.println("u lock game " + this + "predji polja " + predjiPolja);
                for (int i = 0; i < this.predjiPolja && !krajFigure; i++) {
                    if (this.pauzaNasljednik) {
                        synchronized (this.lock) {
                            try {
             //                   System.out.println("ja sam " + this + " i zaustavio me pauza nasljednik");
                                this.lock.wait();
                            } catch (InterruptedException e) {
                                StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                            }
                        }
           //             System.out.println("ja sam " + this + " i izlazim iz pauza nasljednik");
                    }

                    pozicijaFigure++;
                    if (Mapa.mapa[getI(pozicijaFigure)][getJ(pozicijaFigure)].getElement() instanceof Dijamant) {
                        obrisiPolje(pozicijaFigure); //brise dijamant
                        obrisiPrethodno(pozicijaFigure - 1);
                        dodajNovo(pozicijaFigure);
                        ispisiBonusPoljeNaGui();
                        predjiPolja++;
                    } else if (Mapa.mapa[getI(pozicijaFigure)][getJ(pozicijaFigure)].getElement() instanceof Figura) {
                        obrisiPrethodno(pozicijaFigure - 1);
                        pozicijaFigure++;
                        ispisiPreskakanjeNaGui();
                        if (Mapa.mapa[getI(pozicijaFigure)][getJ(pozicijaFigure)].getElement() instanceof Figura) {
                            pozicijaFigure++;
                            ispisiPreskakanjeNaGui();
                            if (Mapa.mapa[getI(pozicijaFigure)][getJ(pozicijaFigure)].getElement() instanceof Figura) {
                                pozicijaFigure++;
                                ispisiPreskakanjeNaGui();
                            }
                        }
                        dodajNovo(pozicijaFigure);
                    } else {
                        obrisiPrethodno(pozicijaFigure - 1);
                        dodajNovo(pozicijaFigure);
                    }
                    ispisiTrenutnuPozicijuNaGui(this);
                    checkKrajFigure();
                }

                this.pauzaFigure = true;

                synchronized (velikiLockNastaviIgru) {
                    velikiLockNastaviIgru.notify();
              //      System.out.println("Ali sam prvo poslala notify");
                }

                try {
                    checkKrajFigure();
                    if (pauzaFigure) {
                        synchronized (this.lockFigure) {
              //              System.out.println("Ja sam  " + this + " i cekam jer jos nisam zavrsila igru");
                            this.lockFigure.wait();
              //              System.out.println("Ja sam  " + this + " i izlazim iz locka");
                        }
                    }
                } catch (InterruptedException e) {
                    StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                }
            }
        }
    }

    public synchronized void obradiKartu() {
        Karta trenutnaKarta = izvuciKartu();
        if (trenutnaKarta instanceof ObicnaKarta) {
            if (pozicijaFigure == 1) {
                dodajNovo(1);
            }
            if (this instanceof SuperBrzina) {
                setPredjiPolja(((ObicnaKarta) trenutnaKarta).getBrojPolja() * SUPER_BRZINA);
            } else {
                setPredjiPolja(((ObicnaKarta) trenutnaKarta).getBrojPolja());
            }
            //        System.out.println(trenutnaKarta.getSlika());
            //        System.out.println("****************************obradjujem kartu i postavljam na nju " + getPredjiPolja() + "a ja sam" + this);
            DiamondCricleController.nacrtajKartu(trenutnaKarta, this);
            ispisiPutanju(this);
        } else {
            //      System.out.println("special card");
            setPredjiPolja(0);
            GameController.obradiSpecijalnuKartu(trenutnaKarta.getSlika());
        }
    }

    public Koordinate getKoordinateFigure() {
        return getKoordinate(pozicijaFigure);
    }

    public String getVrsta() {
        return vrsta;
    }

    private void checkKrajFigure() {
        if (Putanja.getKoordinate(pozicijaFigure).equals(Igrac.getKrajPutanje())) {
            dodajRezultatUFajl();
             obrisiSliku(pozicijaFigure);
            obrisiPolje(pozicijaFigure);
            krajFigure = true;
        } else if (propala) {
            // dodajRezultatUFajl();
            krajFigure = true;
        }
    }

    private void obrisiPrethodno(int pozicija) {
        if (Mapa.mapa[getI(pozicija)][getJ(pozicija)] instanceof Polje) {
            synchronized (Mapa.mapa[getI(pozicija)][getJ(pozicija)]) {
                obrisiPolje(pozicija);
                obrisiSliku(pozicija);
            }
        }
    }

    public void dodajNovo(int pozicija) {
        if (Mapa.mapa[getI(pozicija)][getJ(pozicija)] instanceof Polje) {
            synchronized (Mapa.mapa[getI(pozicija)][getJ(pozicija)]) {
                postaviFiguruNaPolje(pozicija);
                prikaziSliku(pozicija);
            }
        }
    }

    public void dodajRezultatUFajl() {

        String nadji = "Figura " + this.identifikator + " (" + this.getVrsta() + ", " + this.getBoja() + ") - predjeni put: prazno";
        String dodaj = nadji.replace("prazno", " (");

        String putanja = "";

        for (int i = 1; i <= this.pozicijaFigure; i++) {
            int pom = getI(i) * DIMENZIJA_MATRICE + getJ(i) + 1;
     //       System.out.println(pom);
            putanja += pom + " ";
      //      System.out.println(putanja);
        }

        String cilj = " - stigla do cilja (";

        if (putanja.contains(String.valueOf(posljednjaPozicija))) {
            cilj = cilj.concat("da)");
        } else {
            cilj = cilj.concat("ne)");
        }

        String vrijeme = " Vrijeme kretanja: " + this.getVrijemeKretanjaFigure();

        putanja = putanja.concat(")");
        putanja = putanja.concat(cilj);

        putanja=putanja.concat(vrijeme);

        dodaj = dodaj.concat(putanja);

        FileUtils.replaceStringInFile(new File(rez), nadji, dodaj + "\n");
    }
}
