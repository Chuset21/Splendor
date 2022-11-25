module hexanome.fourteen.server {
  requires spring.boot;
  requires spring.boot.autoconfigure;
  requires spring.web;
  requires java.annotation;
  requires com.google.gson;

  exports hexanome.fourteen.server;
  exports hexanome.fourteen.server.control;
}