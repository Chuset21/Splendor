module com.hexanome.fourteen {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.hexanome.fourteen.boards to javafx.fxml;
    opens com.hexanome.fourteen.login to javafx.fxml;

    exports com.hexanome.fourteen;
    exports com.hexanome.fourteen.boards;
    exports com.hexanome.fourteen.login;
}