package hexanome.fourteen.server.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.springframework.test.util.ReflectionTestUtils;

public class GsonInstanceTest {

  GsonInstance gsonInstance;

  @BeforeEach
  public void init() {
    gsonInstance = new GsonInstance();
  }

  @Test
  public void testNoArgsConstructor() {
    GsonInstance expectedGson = new GsonInstance();

    assertNull(expectedGson.gson);
  }

  @Test
  public void testInitGson() {
    ReflectionTestUtils.invokeMethod(gsonInstance, "initGson");

    assertNotNull(gsonInstance.gson);
  }
}