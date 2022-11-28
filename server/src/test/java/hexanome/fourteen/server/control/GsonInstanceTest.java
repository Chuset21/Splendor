package hexanome.fourteen.server.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
class GsonInstanceTest {

  GsonInstance gsonInstance;

  @BeforeAll
  void setUpGsonInstance() {
    gsonInstance = new GsonInstance();
  }

  @Test
  void noArgsConstructor() {
    GsonInstance expectedGson = new GsonInstance();

    Assertions.assertNull(expectedGson.gson);
  }
}