module hexanome.fourteen.server {
  requires spring.boot;
  requires spring.boot.autoconfigure;
  requires spring.web;

  exports hexanome.fourteen.server;
  exports hexanome.fourteen.server.control;
}