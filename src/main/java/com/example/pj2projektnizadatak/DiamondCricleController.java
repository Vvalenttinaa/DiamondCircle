package com.example.pj2projektnizadatak;

import com.example.pj2projektnizadatak.elementi.figure.*;
import com.example.pj2projektnizadatak.karte.*;
import com.example.pj2projektnizadatak.mapa.*;
import javafx.application.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import static com.example.pj2projektnizadatak.GameController.*;
import static com.example.pj2projektnizadatak.mapa.Putanja.*;

public class DiamondCricleController implements Initializable {

    public static int BROJ_IGRACA;
    public static int DIMENZIJA_MATRICE = Integer.valueOf(StartController.dimenzijeMape);
    private static long startVrijeme;

    private static final Pane[][] mapaGui = new Pane[DIMENZIJA_MATRICE][DIMENZIJA_MATRICE];
    private static final Pane[] figureGui = new Pane[16];
    private static final Pane[][] kvadratiGui = new Pane[4][2];

    private static final HashMap<Boja, Color> mapiraneBoje = new HashMap<>() {{
        put(Boja.CRVENA, Color.RED);
        put(Boja.PLAVA, Color.BLUE);
        put(Boja.ZELENA, Color.GREEN);
        put(Boja.ZUTA, Color.ORANGE);
    }};

    @FXML
    private Label idBrojIgara;

    @FXML
    private GridPane idGridPane;

    @FXML
    private GridPane idGridPane1;

    @FXML
    private GridPane idGridPaneKvadrati;

    @FXML
    private Label idIgrac1;

    @FXML
    private Label idIgrac2;

    @FXML
    private Label idIgrac3;

    @FXML
    private Label idIgrac4;

    @FXML
    private Label idOpisPoteza;

    @FXML
    private Label idOpisPozicije;

    @FXML
    private Label idTrenutnaKarta;

    @FXML
    private ImageView imageView;

    @FXML
    private Button idButtonPokreniZautsavi;

    @FXML
    private Button idRezultatiButton;

    @FXML
    private Label idTrajanje;

    final static ImageView slikaKarte = new ImageView();
    final static Label opisKarte = new Label();
    final static Label opisPoteza = new Label();
    final static Label opisPozicije = new Label();
    final static Label trajanje = new Label();

    public static void nacrtajSpecijalnuKartu(String specijalnaKarta) {
        obrisiKvadrate();
        Platform.runLater(() -> {
            try {
                slikaKarte.setImage(new Image(new FileInputStream(specijalnaKarta)));
                opisKarte.setText(("Specijalna karta."));
            } catch (FileNotFoundException e) {
                StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            }
        });
    }

    public static void nacrtajKartu(Karta karta, Figura figura) {
        obrisiKvadrate();
        Platform.runLater(() -> {
            try {
                slikaKarte.setImage(new Image(new FileInputStream(karta.getSlika())));
                int brojKvadrata = ((ObicnaKarta) karta).getBrojPolja();
                if (figura instanceof SuperBrzaFigura) {
                    brojKvadrata *= 2;
                }
                opisKarte.setText("Obicna karta, broj polja " + brojKvadrata);
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (brojKvadrata > 0) {
                            dodajKvadrat(i, j);
                            brojKvadrata--;
                        }
                    }
                }
              //  Thread.sleep(100);
                //       ispisiPotez(karta,figura);
            } catch (Exception e) {
                StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            }
        });
    }

    public static void ispisiPutanju(Figura figura) {
        Platform.runLater(() -> {
            String ciljnaPozicija = String.valueOf(pozicijaNaGuiju(figura.getPozicijaFigure() + figura.getPredjiPolja()));
            String trenutnaPozicija = String.valueOf(pozicijaNaGuiju(figura.getPozicijaFigure()));
            // System.out.println("Pocetna pozicija -" + trenutnaPozicija + ", naredna pozicija - " + narednaPozicija);
            opisPoteza.setText("Igrac: " + getIgracPoBoji(figura.getBoja()).getIme() + "\nFigura: " + figura.getVrsta() + "\nPocetna pozicija: " + trenutnaPozicija + "\nCiljna pozicija: " + ciljnaPozicija);
        });
    }

    public static void ispisiTrenutnuPozicijuNaGui(Figura figura){
        Platform.runLater(() ->{
            opisPozicije.setText("\nTrenutna pozicija " + pozicijaNaGuiju(figura.getPozicijaFigure()) + ", vrijeme kretanja " + figura.getVrijemeKretanjaFigure() + "s");
        });
    }

    public static void ispisiTrajanjeNaGui(){
        Platform.runLater(() ->{
            trajanje.setText(String.valueOf((int)(System.currentTimeMillis()-startVrijeme)/1000)+"s");
        });
    }

    public static String getTrajanje(){
        return trajanje.getText();
    }

    public static void ispisiBonusPoljeNaGui(){
        Platform.runLater(() -> {
            String pom = opisPoteza.getText() + " + 1 (BONUS)";
            opisPoteza.setText(pom);
        });
    }

    public static void ispisiPreskakanjeNaGui(){
        Platform.runLater(() -> {
            String pom = opisPoteza.getText() + "(SKOK)";
            opisPoteza.setText(pom);
        });
    }

    private static int pozicijaNaGuiju(int pozicija) {
        return getI(pozicija) * DIMENZIJA_MATRICE + getJ(pozicija) + 1;
        // return Math.min(pom, posljednjaPozicija);
    }

    public static void addToMatrix(Koordinate k, File file) {
        Image image1 = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image1);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        Platform.runLater(() -> {
            mapaGui[k.i][k.j].getChildren().add(imageView);
        });
    }

    public static void removeImageFromMatrix(Koordinate k, File file) {
        Platform.runLater(() -> {
            mapaGui[k.i][k.j].getChildren().removeIf(node -> node instanceof ImageView);
        });
    }

    public static void dodajKrug(int i, int j) {
        Circle circle = new Circle(20);
        circle.setCenterX(20);
        circle.setCenterY(20);

        Platform.runLater(() -> {
            mapaGui[i][j].getChildren().add(circle);
        });
    }

    public static void obrisiKrug(int row, int column) {
        Platform.runLater(() -> {
            mapaGui[row][column].getChildren().removeIf(node -> node instanceof Circle);
        });
    }

    public static void dodajKvadrat(int i, int j) {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(10);
        rectangle.setHeight(10);

        Platform.runLater(() -> {
            kvadratiGui[j][i].getChildren().add(rectangle);
        });
    }

    public static void obrisiKvadrate() {
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 4; column++) {
                int finalRow = row;
                int finalColumn = column;
                Platform.runLater(() -> {
                    kvadratiGui[finalColumn][finalRow].getChildren().removeIf(node -> node instanceof Rectangle);
                });
            }
        }
    }

    private void generisiKvadrateGui() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                kvadratiGui[j][i] = new StackPane();
                idGridPaneKvadrati.add(kvadratiGui[j][i], j, i);
            }
        }
    }

    public static void pokreni() {
        Platform.runLater(() -> {
            kontrolaNova();
        });
    }

    public static void pomjeri(File figura, Koordinate k) {
        try {
            addToMatrix(k, figura);
        } catch (Exception e) {
            StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }

    private void generisiMapuGui() {

        int polje = 1;

        for (int i = 0; i < DIMENZIJA_MATRICE; i++) {
            for (int j = 0; j < DIMENZIJA_MATRICE; j++) {
                Label labela = new Label(String.valueOf(polje));
                labela.setTextFill(Color.BLACK);
                labela.setAlignment(Pos.CENTER);
                labela.setStyle("-fx-font-size: 1.7em;" + "-fx-border-color: gray"  );
                mapaGui[i][j] = new Pane();
                mapaGui[i][j].setStyle("-fx-border-width: 8px 8px" + "align-content: center;");
                mapaGui[i][j].getChildren().add(labela);
                polje++;
            }
        }

        idGridPane.getRowConstraints().removeAll();
        final int brojKolona = DIMENZIJA_MATRICE;
        final int brojVrsta = DIMENZIJA_MATRICE;

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints.setPercentWidth(100d / brojKolona);

        for (int i = 0; i < brojKolona; i++) {
            idGridPane.getColumnConstraints().add(columnConstraints);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setFillHeight(true);
        rowConstraints.setVgrow(Priority.ALWAYS);
        rowConstraints.setPercentHeight(100d / brojVrsta);

        for (int i = 0; i < brojVrsta; i++) {
            idGridPane.getRowConstraints().add(rowConstraints);
        }
        for (int i = 0; i < DIMENZIJA_MATRICE; i++) {
            for (int j = 0; j < DIMENZIJA_MATRICE; j++) {
                idGridPane.add(mapaGui[i][j], j, i);
            }
        }
    }

    private void generisiFigureGui() {

        int k = 0;
        for (int i = 0; i < getIgraci().size(); i++) {
            Igrac igrac = getIgraci().get(i);
            for (int j = 0; j < 4; j++) {
                Figura f = igrac.getFigure().get(j);
                Label labela = new Label(String.valueOf(f.getNaziv()));
                labela.setTextFill(mapiraneBoje.get(igrac.getBoja()));
         //       labela.setStyle("-fx-font-size: 0.8em");
                //          labela.setAlignment(Pos.CENTER);
                figureGui[k] = new Pane();
                //           figureGui[k].setStyle("-fx-border-width:0.50");
                int finalJ = j;
                figureGui[k].setOnMouseClicked(e -> {
                    Platform.runLater(() ->
                    {
                    //    System.out.println("otvaram putanju figure " + f + "igraca " + igrac);
                    //    System.out.println("POZICIJA JE " + f.getPozicijaFigure());
                        try {
                            otvoriPutanjuFigure(f);
                        } catch (Exception ex) {
                            StartApp.logger.log(Level.SEVERE, ex.fillInStackTrace().toString());
                        }
                    });
                });
                figureGui[k].getChildren().add(labela);
                k++;
            }
        }

        ColumnConstraints columnConstraints = new ColumnConstraints();
        //     columnConstraints.setFillWidth(true);
        //    columnConstraints.setHgrow(Priority.ALWAYS);
        //    columnConstraints.setPercentWidth(100d);
        idGridPane1.getColumnConstraints().add(columnConstraints);

        RowConstraints rowConstraints = new RowConstraints();
        //     rowConstraints.setFillHeight(true);
        //  rowConstraints.setVgrow(Priority.ALWAYS);
        //   rowConstraints.setPercentHeight(100d / 16);

        for (int i = 0; i < 16; i++) {
            idGridPane1.getRowConstraints().add(rowConstraints);
        }
        for (int i = 0; i < 16; i++) {
            if (figureGui[i] != null)
                idGridPane1.add(figureGui[i], 1, i);
        }
        idGridPane1.setLayoutX(0);
        idGridPane1.setLayoutY(20);
    }

    private void generisiNaziveIgracaGui() {
        if (getIgraci().size() >= 1) {
            idIgrac1.setText("Igrac 1:\n" + getIgraci().get(0).getIme());
            idIgrac1.setTextFill(mapiraneBoje.get(getIgraci().get(0).getBoja()));
        //    System.out.println(getIgraci().get(0).getIme() + getIgraci().get(0).getBoja() + mapiraneBoje.get(getIgraci().get(0).getBoja()));

            if (getIgraci().size() >= 2) {
                idIgrac2.setText("Igrac 2:\n" + getIgraci().get(1).getIme());
                idIgrac2.setTextFill(mapiraneBoje.get(getIgraci().get(1).getBoja()));
           //     System.out.println(getIgraci().get(1).getIme() + getIgraci().get(1).getBoja() + mapiraneBoje.get(getIgraci().get(1).getBoja()));

                if (getIgraci().size() >= 3) {
                    idIgrac3.setText("Igrac 3:\n" + getIgraci().get(2).getIme());
                    idIgrac3.setTextFill(mapiraneBoje.get(getIgraci().get(2).getBoja()));
             //       System.out.println(getIgraci().get(2).getIme() + getIgraci().get(2).getBoja() + mapiraneBoje.get(getIgraci().get(2).getBoja()));

                    if (getIgraci().size() == 4) {
                        idIgrac4.setText("Igrac 4:\n" + getIgraci().get(3).getIme());
                        idIgrac4.setTextFill(mapiraneBoje.get(getIgraci().get(3).getBoja()));
               //         System.out.println(getIgraci().get(3).getIme() + getIgraci().get(3).getBoja() + mapiraneBoje.get(getIgraci().get(3).getBoja()));

                    }
                }
            }
        }
    }

    @FXML
    void pressPokreniZaustavi(ActionEvent event) {

        if (pauzaIgre == true) {
            pauzaIgre = false;
            for (int i = 0; i < getIgraci().size(); i++) {
                if(getIgraci().get(i).getTrenutnaFigura() != null) {
                    getIgraci().get(i).getTrenutnaFigura().pauzaNasljednik = false;
                }
            }
            getDuh().pauzaNasljednik = false;
            for (int i = 0; i < getIgraci().size(); i++) {
                if(getIgraci().get(i).getTrenutnaFigura() != null) {

                    synchronized (getIgraci().get(i).getTrenutnaFigura().lock) {
                        getIgraci().get(i).getTrenutnaFigura().lock.notify();
                    }
                }
            }
            synchronized (getDuh().lock) {
                getDuh().lock.notify();
            }
        } else {
            pauzaIgre = true;
            for (int i = 0; i < getIgraci().size(); i++) {
                if (getIgraci().get(i).getTrenutnaFigura() != null) {

                    getIgraci().get(i).getTrenutnaFigura().pauzaNasljednik = true;
                }
            }
            getDuh().pauzaNasljednik = true;
        }
        System.out.println("PAUZA:" + GameController.pauzaIgre);
    }

    private void otvoriPutanjuFigure(Figura figura) throws IOException {

        PutanjaFigureController.setFigura(figura);
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("putanjaFigure.fxml"));
        primaryStage.setTitle(getIgracPoBoji(figura.getBoja()).getIme() + " - " + figura.getNaziv());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    void onClickRezultati(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("rezultati.fxml"));
        primaryStage.setTitle("REZULTATI");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void ispisTrajanja(){
        Thread ispis = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!kraj) {
                    ispisiTrajanjeNaGui();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                    }
                }
            }
        });
        ispis.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GameController game = new GameController();
        generisiMapuGui();
        generisiFigureGui();
        generisiNaziveIgracaGui();
        generisiKvadrateGui();
        startVrijeme = System.currentTimeMillis();
        ispisTrajanja();

        idBrojIgara.setText(String.valueOf(game.getBrojIgre()));

        trajanje.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                idTrajanje.setText(trajanje.getText());
                idTrajanje.setTextFill(trajanje.getTextFill());
            }
        });

        slikaKarte.imageProperty().addListener(new ChangeListener<Image>() {
            @Override
            public void changed(ObservableValue<? extends Image> observableValue, Image image, Image t1) {
                imageView.setImage(slikaKarte.getImage());
            }
        });

        opisKarte.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                idTrenutnaKarta.setText(opisKarte.getText());
                idTrenutnaKarta.setTextFill(opisKarte.getTextFill());
            }
        });

        opisPoteza.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                idOpisPoteza.setText(opisPoteza.getText());
                idOpisPoteza.setTextFill(opisPoteza.getTextFill());
            }
        });

        opisPozicije.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                idOpisPozicije.setText(opisPozicije.getText());
                idOpisPozicije.setTextFill(opisPozicije.getTextFill());
            }
        });

        game.pokreniIgru();

    }
}

