package com.example.pj2projektnizadatak;

import com.example.pj2projektnizadatak.elementi.figure.*;
import com.example.pj2projektnizadatak.mapa.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.net.*;
import java.util.*;

import static com.example.pj2projektnizadatak.DiamondCricleController.DIMENZIJA_MATRICE;


public class PutanjaFigureController implements Initializable {

    private final static Pane[][] mapaGui = new Pane[DIMENZIJA_MATRICE][DIMENZIJA_MATRICE];
    private static Figura figura;

    @FXML
    private GridPane idGridPane;

    public static void setFigura(Figura figura) {
        PutanjaFigureController.figura = figura;
    }

    private void generisiPutanjuGui() {
        int polje = 1;

        for (int i = 0; i < DIMENZIJA_MATRICE; i++) {
            for (int j = 0; j < DIMENZIJA_MATRICE; j++) {
                Label labela = new Label(String.valueOf(polje));
                labela.setTextFill(Color.BLACK);
                labela.setStyle("-fx-font-size: 1.7em");
                mapaGui[i][j] = new Pane();
                mapaGui[i][j].setStyle("-fx-border-width:0.5");
                mapaGui[i][j].getChildren().add(labela);
                polje++;
            }
        }

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

    private void dodajPutanjuNaGui(){
        int brojPolja = figura.getPozicijaFigure();
     //   System.out.println(brojPolja);
        for( Map.Entry<Integer, Koordinate> entry : Putanja.put.entrySet() ) {
            if(entry.getKey() <= brojPolja){
                Koordinate k = entry.getValue();
         //       System.out.println(k);
                dodajPolje(k);
            }
        }
    }

    public static void dodajPolje(Koordinate k) {
        Circle circle = new Circle(20);
        circle.setCenterX(20);
        circle.setCenterY(20);
        circle.setFill(Color.YELLOW);

        Platform.runLater(() -> {
            mapaGui[k.i][k.j].getChildren().add(circle);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generisiPutanjuGui();
        dodajPutanjuNaGui();
    }
}