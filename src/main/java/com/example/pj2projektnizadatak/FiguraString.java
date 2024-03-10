package com.example.pj2projektnizadatak;

import java.util.*;

public enum FiguraString {
    LebdecaFigura, ObicnaFigura, SuperBrzaFigura;

    public static final Random random = new Random();

    public static FiguraString randomFigura()  {
        FiguraString[] figure = values();
        return figure[random.nextInt(figure.length)];
    }
}
