package com.example.pj2projektnizadatak.elementi;

import com.example.pj2projektnizadatak.*;
import com.example.pj2projektnizadatak.elementi.figure.*;
import com.example.pj2projektnizadatak.mapa.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Rupa extends Element {
    private static final String brojCrnihRupa = System.getProperty("user.dir") + File.separator + "brojRupa.TXT";
    private static Collection<Koordinate> putanja = Putanja.getPut().values();
    private static int maxBrojRupa;
    private static Random random = new Random();

    public Rupa() {
        super();
        ucitajMaxBrojRupa();
        crneRupe();
    }

    private int generisiBrojRupa() {
        return random.nextInt(maxBrojRupa);
    }

    private Object getRandomPolje() {
        int i = random.nextInt(putanja.size());
        return putanja.toArray()[i];
    }

    private void ucitajMaxBrojRupa() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(brojCrnihRupa));
            String s;
            while ((s = in.readLine()) != null) {
                maxBrojRupa = Integer.valueOf(s);
            }
            in.close();
        } catch (IOException e) {
            StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            System.out.println("GRESKA PRI CITANJU MAKSIMALNOG BROJA RUPA");
        }
    }

    public static void iscrtajKrug(int row, int column) {
        DiamondCricleController.dodajKrug(row, column);
    }

    public static void obrisiKrug(int row, int column) {
        DiamondCricleController.obrisiKrug(row, column);
    }

    public void crneRupe() {
        int broj = generisiBrojRupa();
      //  System.out.println("Broj rupa je " + broj);
        List<Koordinate> rupe = new ArrayList<>();
        while (broj-- > 0) {
            Koordinate rupa = (Koordinate) getRandomPolje();
            rupe.add(rupa);
        }
        for (Koordinate rupa : rupe
        ) {
            Rupa.iscrtajKrug(rupa.i, rupa.j);
            if (Mapa.mapa[rupa.i][rupa.j].getElement() instanceof Figura && !(Mapa.mapa[rupa.i][rupa.j].getElement() instanceof LebdecaFigura)) {
                System.out.println("\nOtvara se rupa ispod figure " + ((Figura) Mapa.mapa[rupa.i][rupa.j].getElement()).getNaziv());
                figuraPropadaUCrnuRupu(rupa);
            }
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }
        for (Koordinate k : rupe
        ) {
            obrisiKrug(k.i, k.j);
        }
        rupe.clear();
    }

    private void figuraPropadaUCrnuRupu(Koordinate k) {
        synchronized (Mapa.mapa[k.i][k.j]) {
            System.out.println( Mapa.mapa[k.i][k.j].getElement() + " propala u crnu rupu\n");
            ((Figura) Mapa.mapa[k.i][k.j].getElement()).dodajRezultatUFajl();
            Igrac i = GameController.getIgracPoBoji(((Figura) Mapa.mapa[k.i][k.j].getElement()).getBoja());

            i.getTrenutnaFigura().setPropala(true);
            i.getTrenutnaFigura().setKrajFigure(true);
            i.getTrenutnaFigura().setPredjiPolja(0);

            ((Figura) Mapa.mapa[k.i][k.j].getElement()).obrisiSliku(Putanja.getPozicija(k));
            Mapa.mapa[k.i][k.j] = new Polje();
        }
    }
}
