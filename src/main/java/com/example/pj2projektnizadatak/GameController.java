package com.example.pj2projektnizadatak;

import com.example.pj2projektnizadatak.elementi.*;
import com.example.pj2projektnizadatak.elementi.figure.*;
import com.example.pj2projektnizadatak.karte.*;
import com.example.pj2projektnizadatak.mapa.*;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.logging.*;

import static com.example.pj2projektnizadatak.utils.FileUtils.appendStrToFile;
import static com.example.pj2projektnizadatak.utils.FileUtils.readBrojIgre;

public class GameController {

    public int brojIgre;
    private static Rupa rupa;
    public static boolean kraj = false;

    private static List<Igrac> igraci = new ArrayList<>();
    private static Spil spil = new Spil();
    private static List<Boja> boje = new LinkedList<>(Arrays.asList(Boja.values()));
    public static boolean pauzaIgre = false;
    private static DuhFigura duh;
    public static Object velikiLockNastaviIgru = new Object();
    public static String rez;


    public GameController() {
        brojIgre = readBrojIgre();
        Putanja p = new Putanja();
        Mapa mapa = new Mapa();
        generisiIgrace();
        kreirajRezultate();
        kontrolaKraja();
    }

    private static Boja randBoja() {
        boje.remove(Boja.BEZBOJNA);
        Collections.shuffle(boje);
        Boja b = boje.stream().findFirst().get();
        boje.remove(b);
        return b;
    }

    private static void generisiIgrace() {
        String[] imena = StartController.getIgraci();
        for (int k = 0; k < DiamondCricleController.BROJ_IGRACA; k++) {
            igraci.add(new Igrac(imena[k], randBoja()));
        }
    //    System.out.println(igraci);
        Collections.shuffle(igraci);
    //    System.out.println(igraci);
    }

    public static List<Igrac> getIgraci() {
        return igraci;
    }

    public static DuhFigura getDuh() {
        return duh;
    }

    public static Igrac getIgracPoBoji(Boja boja) {
        for (Igrac i : igraci
        ) {
            if (i.getBoja().equals(boja))
                return i;
        }
        return null;
    }

    public static Karta izvuciKartu() {
        return spil.getKartaIzSpila();
    }

    public static void obradiSpecijalnuKartu(String specijalnaKarta) {
        DiamondCricleController.nacrtajSpecijalnuKartu(specijalnaKarta);
        rupa = new Rupa();
    }

    public void pokreniIgru() {
        pokretanje();
    }

    private void pokretanje() {

        duh = new DuhFigura();
        Thread duhFigura = new Thread(duh);
        duhFigura.start();

        DiamondCricleController.pokreni();

    }

    public static void kontrolaNova() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!kraj) {
                    for (int i = 0; i < igraci.size(); i++) {
                        synchronized (velikiLockNastaviIgru) {
                            if (igraci.get(i).getFigure().size() > 0 ||
                                    (igraci.get(i).getTrenutnaFigura() != null && !igraci.get(i).getTrenutnaFigura().isKrajFigure())) {
                                if (igraci.get(i).getFigure().size() == 4) {
                                    igraci.get(i).pokreniNovuFiguru();
                                    try {
                                        velikiLockNastaviIgru.wait();
                          //              System.out.println("prvo pokretanje zavrsilo rundu");
                                    } catch (InterruptedException e) {
                                        StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                                    }
                                } else {

                                    if (igraci.get(i).getTrenutnaFigura().isKrajFigure() && igraci.get(i).getFigure().size() > 0) {
                             //           System.out.println("-----------------Pokrece novu za " + igraci.get(i));
                                        igraci.get(i).pokreniNovuFiguru();

                                    } else if (igraci.get(i).getTrenutnaFigura() != null) {
                           //             System.out.println("tu gdje treba" + igraci.get(i).getTrenutnaFigura());
                                        igraci.get(i).getTrenutnaFigura().pauzaFigure = false;
                                        synchronized (igraci.get(i).getTrenutnaFigura().lockFigure) {
                           //                 System.out.println("Saljem notify igracu " + igraci.get(i).getIme());
                                            try {
                                                igraci.get(i).getTrenutnaFigura().lockFigure.notify();
                                            } catch (Exception e) {
                                                StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                                            }
                           //                 System.out.println("Sad ce ispis PRVOG");
                                        }
                                    }
                                    try {
                         //               System.out.println("Zaglavi u wait nastaviIgru" + i + 1);
                                        velikiLockNastaviIgru.wait();
                         //               System.out.println("Izadje iz waita nastaviIgru" + i + 1);
                                    } catch (InterruptedException e) {
                                        StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        t.start();
    }

    private void kontrolaKraja() {
        Thread kontrolaKraja = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!kraj) {
                    int n = 0;
                    for (Igrac i : igraci
                    ) {
                        if (i.aktivanIgrac()) {
                            // System.out.println("Aktivan je " + i.getIme());
                            n++;
                        }
                    }
                    if (n == 0) {

                        appendStrToFile(rez, "\nUkupno vrijeme trajanja igre: " + DiamondCricleController.getTrajanje());
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                        LocalDateTime now = LocalDateTime.now();
                  //      System.out.println(dtf.format(now));

                        File renamed = new File(String.format(String.format(String.format(System.getProperty("user.dir") + File.separator + "rezultati/IGRA_" +
                                dtf.format(now)))) + ".TXT");

                        File rezultati = new File(rez);

                        boolean flag = rezultati.renameTo(renamed);
/*
                            if(flag){
                                System.out.println("rez full");
                            }else {
                                System.out.println("rez not full");
                            }
                             */
                        kraj = true;
                        System.out.println("Kraj");
                        //       System.exit(0);
                    }
                }
            }
        });
        kontrolaKraja.start();
    }

    public int getBrojIgre() {
        return brojIgre;
    }

    public void kreirajRezultate() {

        rez = System.getProperty("user.dir") + File.separator + "rezultati/rezultati" + brojIgre + ".txt";
        int br = 0;
        for (Igrac i : igraci
        ) {
            appendStrToFile(rez, "Igrac " + ++br + " - " + i.getIme() + "\n");
            int brFig = 0;
            for (Figura f : i.getFigure()
            ) {
                appendStrToFile(rez, "\tFigura " + i.getFigure().get(brFig++).getId() + " (" + f.getVrsta() + ", " + f.getBoja() + ") - predjeni put: prazno\n");
            }
        }
    }
}
