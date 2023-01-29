module ui {
    requires javafx.controls;


    opens puissancequatre to javafx.fxml;
    exports puissancequatre;
    exports ui;
    opens ui to javafx.fxml;
}