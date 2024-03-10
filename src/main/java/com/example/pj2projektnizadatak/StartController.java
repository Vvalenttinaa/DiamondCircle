package com.example.pj2projektnizadatak;

import com.example.pj2projektnizadatak.mapa.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

public class StartController implements Initializable {

    private String[] igraciBroj = {"1","2","3","4"};
    private static String[] igraci = new String[4];
    public static String[] getIgraci() {
        return igraci;
    }

    public boolean setIgraci() {
        switch (Integer.valueOf(brojIgraca)) {
            case 1:
                if (idigrac1.getText().length() > 0)
                {
                    igraci[0] = idigrac1.getText();
                    System.out.println("Ime igraca je " + igraci[0] + ".");
                    return true;
                }
                else {
                    System.out.println("falseeeeeeeeeeeeeeeeeeeeeeeeee");
                    return false;
                }
            case 2:
                if (idigrac1.getText().length() > 0 && idigrac2.getText().length() >0 && !idigrac1.getText().equals(idigrac2.getText()))
                {
                    igraci[0] = idigrac1.getText();
                    igraci[1] = idigrac2.getText();
                    return true;
                }
                return false;
            case 3:
                if (idigrac1.getText().length() > 0 && idigrac2.getText().length() > 0 && idigrac3.getText().length() > 0
                        && !idigrac1.getText().equals(idigrac2.getText()) && !idigrac1.getText().equals(idigrac3.getText())
                        && !idigrac2.getText().equals(idigrac3.getText()))
                {
                    igraci[0] = idigrac1.getText();
                    igraci[1] = idigrac2.getText();
                    igraci[2] = idigrac3.getText();
                    return true;
                }
                return false;
            case 4:
                if (idigrac1.getText().length() > 0 && idigrac2.getText().length() > 0 && idigrac3.getText().length() > 0 && idigrac4.getText().length() > 0
                        && !idigrac1.getText().equals(idigrac2) && !idigrac1.getText().equals(idigrac3) && !idigrac1.getText().equals(idigrac4)
                        && !idigrac2.getText().equals(idigrac3) && !idigrac2.getText().equals(idigrac4)
                        && !idigrac3.getText().equals(idigrac4))
                {
                    igraci[0] = idigrac1.getText();
                    igraci[1] = idigrac2.getText();
                    igraci[2] = idigrac3.getText();
                    igraci[3] = idigrac4.getText();
                    return true;
                }
                return false;
            default: return false;
        }
    }

    private String dimenzije[] = {"7x7", "8x8", "9x9", "10x10"};
    public static String brojIgraca;
    public static String dimenzijeMape;

    @FXML
    private ComboBox<String> idBrojIgraca;

    @FXML
    private ComboBox<String> idVelicinaMape;

    @FXML
    private Label idSelectedBrojIgraca;

    @FXML
    private Label idSelectedDimenzije;

    @FXML
    private Button idPokreniButton;

    @FXML
    private TextField idigrac1;

    @FXML
    private TextField idigrac2;

    @FXML
    private TextField idigrac3;

    @FXML
    private TextField idigrac4;

    @FXML
    void izaberiDimenzije(ActionEvent event) {
        String[] string = idVelicinaMape.getSelectionModel().getSelectedItem().toString().split("x", 2);
        dimenzijeMape = string[0];
    }

    @FXML
    void izaberiIgrace(ActionEvent event) {
        try {
            brojIgraca = idBrojIgraca.getSelectionModel().getSelectedItem().toString();
        } catch (NumberFormatException e) {
            StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }


    @FXML
    void pokreni(ActionEvent event) throws IOException {
        System.out.println("Pokreni igru...");
        if (brojIgraca != null && dimenzijeMape != null && setIgraci()) {
            DiamondCricleController.BROJ_IGRACA = Integer.parseInt(brojIgraca);
            DiamondCricleController.DIMENZIJA_MATRICE = Integer.parseInt(dimenzijeMape);
            Mapa.DIMENZIJA_MATRICE = Integer.parseInt(dimenzijeMape);

            System.out.println("Broj igraca je " + DiamondCricleController.BROJ_IGRACA);
            System.out.println("Dimenzije matrice su " + DiamondCricleController.DIMENZIJA_MATRICE + "==" + Mapa.DIMENZIJA_MATRICE);

            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("diamond-cricle.fxml"));
            primaryStage.setTitle("DIAMOND-CRICLE");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idBrojIgraca.getItems().addAll(igraciBroj);
        idVelicinaMape.getItems().addAll(dimenzije);
    }
}