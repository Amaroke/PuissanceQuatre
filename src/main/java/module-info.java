module com.example.puissancequatre {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.puissancequatre to javafx.fxml;
    exports com.example.puissancequatre;
    exports old;
    opens old to javafx.fxml;
}