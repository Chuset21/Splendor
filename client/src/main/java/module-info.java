module com.hexanome.fourteen {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.hexanome.fourteen to javafx.fxml;
    opens com.hexanome.fourteen.boards to javafx.fxml;
    exports com.hexanome.fourteen;
}