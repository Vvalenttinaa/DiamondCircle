package com.example.pj2projektnizadatak.mapa;

public class Koordinate {
    public int i;
    public int j;

    public Koordinate() {
    }

    public Koordinate(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public void setKoordinata(Koordinate k) {
        this.i = k.i;
        this.j = k.j;
    }

    public void setKoordinata(int i, int j){
        this.i=i;
        this.j=j;
    }
    @Override
    public String toString() {
        return " x je " + i + " y je " + j;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Koordinate)) {
            return false;
        }
        Koordinate k = (Koordinate) obj;
        return Integer.compare(k.i, this.i) == 0 && Integer.compare(k.j, this.j) == 0;
    }
}
