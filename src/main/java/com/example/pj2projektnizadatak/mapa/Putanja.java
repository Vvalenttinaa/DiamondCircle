package com.example.pj2projektnizadatak.mapa;

import com.example.pj2projektnizadatak.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Putanja {

    public static final HashMap<Integer, Koordinate> put = new HashMap<>();
    static final String fajlPutanja = System.getProperty("user.dir") + File.separator + "dimenzije/matrica" + Mapa.DIMENZIJA_MATRICE + "x" + Mapa.DIMENZIJA_MATRICE + ".TXT";
    public static int posljednjaPozicija;

    public Putanja() {
        ucitajPutanju();
    }

    public static HashMap<Integer, Koordinate> getPut() {
        return put;
    }

    public static void ucitajPutanju() {
        try {
            BufferedReader in = new BufferedReader((new FileReader(fajlPutanja)));
            String s;
            int i = 1;
            while ((s = in.readLine()) != null) {
                String[] polja = s.split(";");
                for (String string : polja
                ) {
                    //     System.out.println("Pozicija" + string);
                    String[] vrijednosti = string.split(",");
                    Koordinate koordinate = new Koordinate(Integer.parseInt(vrijednosti[0].substring(1)), Integer.parseInt(vrijednosti[1].substring(0, vrijednosti[1].length() - 1)));
                    put.put(i, koordinate);
                    i++;
                }
                posljednjaPozicija = i - 1;
            //    System.out.println("***********************************************************");
            //    System.out.println(posljednjaPozicija);
            }
            in.close();
        } catch (IOException e) {
            StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            System.out.println("Greska pri citanju iz fajla");
        }
    }

    public static int getI(int pozicija) {
        try {
            return pozicija < posljednjaPozicija ? Putanja.put.get(pozicija).i : Putanja.put.get(posljednjaPozicija).i;
        } catch (Exception e) {
       //     System.out.println("Greska na poziciji " + pozicija);
            StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            return -1;
        }
    }

    public static int getJ(int pozicija) {
        return pozicija < posljednjaPozicija ? Putanja.put.get(pozicija).j : Putanja.put.get(posljednjaPozicija).j;
    }

    public static int getPozicija(Koordinate k) {
        for (Map.Entry<Integer, Koordinate> entry : Putanja.getPut().entrySet()) {
            Integer key = entry.getKey();
            if (entry.getValue().equals(k)) {
                return key;
            }
        }
        return -1;
    }

    public static Koordinate getKoordinate(int pozicija) {
        return new Koordinate(getI(pozicija), getJ(pozicija));
    }

    @Override
    public String toString() {
        String putanja = "";
        for (Map.Entry<Integer, Koordinate> entry : put.entrySet()) {
            putanja += entry.getKey() + " = " + entry.getValue();
        }
        return putanja;
    }
}
