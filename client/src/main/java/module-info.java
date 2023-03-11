module com.hexanome.fourteen {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires com.google.gson;
  requires unirest.java;
  requires gson.extras;
  requires org.apache.commons.codec;

  opens com.hexanome.fourteen.boards to javafx.fxml;
  opens com.hexanome.fourteen.lobbyui to javafx.fxml;
  opens com.hexanome.fourteen.login to javafx.fxml, com.google.gson;
  opens com.hexanome.fourteen.form.lobbyservice to com.google.gson;
  opens com.hexanome.fourteen.form.server to com.google.gson;
  opens com.hexanome.fourteen.form.server.cardform to com.google.gson;
  opens com.hexanome.fourteen.form.server.payment to com.google.gson;
  opens com.hexanome.fourteen.form.server.tradingposts to com.google.gson;
  opens com.hexanome.fourteen to com.google.gson, gson.extras, org.junit.jupiter.api;

  exports com.hexanome.fourteen.form.server.tradingposts;
  exports com.hexanome.fourteen.form.server.payment;
  exports com.hexanome.fourteen;
  exports com.hexanome.fourteen.boards;
  exports com.hexanome.fourteen.login;
  exports com.hexanome.fourteen.lobbyui;
  exports com.hexanome.fourteen.form.lobbyservice;
  exports com.hexanome.fourteen.form.server;
  exports com.hexanome.fourteen.form.server.cardform;
}