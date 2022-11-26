module com.hexanome.fourteen {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires unirest.java;

    opens com.hexanome.fourteen.boards to javafx.fxml;
    opens com.hexanome.fourteen.lobbyui to javafx.fxml;
    opens com.hexanome.fourteen.login to javafx.fxml, com.google.gson;

    exports com.hexanome.fourteen;
    exports com.hexanome.fourteen.boards;
    exports com.hexanome.fourteen.login;
    exports com.hexanome.fourteen.lobbyui;
}