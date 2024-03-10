module com.example.pj2projektnizadatak {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;


    opens com.example.pj2projektnizadatak to javafx.fxml;
    opens com.example.pj2projektnizadatak.utils to javafx.fxml;

    exports com.example.pj2projektnizadatak;
    exports com.example.pj2projektnizadatak.utils;
}