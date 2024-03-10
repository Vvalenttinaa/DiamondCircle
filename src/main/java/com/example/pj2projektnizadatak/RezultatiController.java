package com.example.pj2projektnizadatak;
import com.example.pj2projektnizadatak.utils.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;


public class RezultatiController implements Initializable{

    @FXML
    private TableView <FileResult> idRezultati;
    @FXML
    private TableColumn<String,String> idColumn;

    private static final String directoryPath="rezultati";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("fileName")
        );

        ObservableList<FileResult> listInstance = FXCollections.observableArrayList();
        File path = new File(directoryPath);
        File filesList[] = path.listFiles();
        for (File f: filesList
             ) {
            listInstance.add(new FileResult(f.getName(),f.getAbsolutePath()));
        }
        idRezultati.setItems(listInstance);

        idRezultati.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                if (idRezultati.getSelectionModel().getSelectedItem() != null) {
                    otvoriRezltate(idRezultati.getSelectionModel().getSelectedItem().getFilePath());
                }
            }
        });
    }

    private void otvoriRezltate(String fajl){
        File f = new File(fajl);
        ProcessBuilder pb = new ProcessBuilder("Notepad.exe", f.getAbsolutePath());
        try {
            pb.start();
        } catch (IOException e) {
            StartApp.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }
}
