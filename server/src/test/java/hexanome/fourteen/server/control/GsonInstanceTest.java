package hexanome.fourteen.server.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
public class GsonInstanceTest {

  static GsonInstance gsonInstance;

  @BeforeAll
  public static void setUp() {
    gsonInstance = new GsonInstance();
  }

  @Test
  public void testNoArgsConstructor() {
    GsonInstance expectedGson = new GsonInstance();

    Assertions.assertNull(expectedGson.gson);
  }
}