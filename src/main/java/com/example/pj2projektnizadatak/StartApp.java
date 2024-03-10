package com.example.pj2projektnizadatak;

import com.example.pj2projektnizadatak.utils.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;
import java.util.logging.*;

public class StartApp extends Application {
    public static Handler handler;
    public static Logger logger;

    static {
        try {
            handler = new FileHandler("DiamondCircleGame.log", true);
            logger = Logger.getLogger(StartApp.class.getName());
            logger.addHandler(handler);
         //   handler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApp.class.getResource("pocetak.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 429, 235);
        stage.setTitle("DIAMOND CRICLE GAME");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        MyProperties properties = new MyProperties();
        launch();
    }
}
