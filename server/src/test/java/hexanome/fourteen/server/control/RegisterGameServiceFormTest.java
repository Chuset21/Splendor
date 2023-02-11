package hexanome.fourteen.server.control;

import hexanome.fourteen.server.control.form.RegisterGameServiceForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
public class RegisterGameServiceFormTest {

  static RegisterGameServiceForm testRegisterGamePartial;
  static RegisterGameServiceForm testRegisterGameFull;

  @BeforeAll
  public static void setUp() {
    testRegisterGamePartial = new RegisterGameServiceForm("dummy location", "dummy",
        "dummy");

    testRegisterGameFull = new RegisterGameServiceForm("proper dummy location",
        3, 2, "proper dummy", "proper dummy",
        "enabled");
  }

  @Test
  public void testNoArgsConstructor() {
    RegisterGameServiceForm expectedRegisteredGame = new RegisterGameServiceForm();

    Assertions.assertNull(expectedRegisteredGame.location());
    Assertions.assertEquals(0, expectedRegisteredGame.maxSessionPlayers());
    Assertions.assertEquals(0, expectedRegisteredGame.minSessionPlayers());
    Assertions.assertNull(expectedRegisteredGame.name());
    Assertions.assertNull(expectedRegisteredGame.displayName());
    Assertions.assertNull(expectedRegisteredGame.webSupport());
  }

  @Test
  public void testLocation() {
    Assertions.assertEquals("dummy location", testRegisterGamePartial.location());
    Assertions.assertEquals("proper dummy location", testRegisterGameFull.location());
  }

  @Test
  public void testMaxSessionPlayers() {
    Assertions.assertEquals(4, testRegisterGamePartial.maxSessionPlayers());
    Assertions.assertEquals(3, testRegisterGameFull.maxSessionPlayers());
  }

  @Test
  public void testMinSessionPlayers() {
    Assertions.assertEquals(2, testRegisterGamePartial.minSessionPlayers());
    Assertions.assertEquals(2, testRegisterGameFull.minSessionPlayers());
  }

  @Test
  public void testName() {
    Assertions.assertEquals("dummy", testRegisterGamePartial.name());
    Assertions.assertEquals("proper dummy", testRegisterGameFull.name());
  }

  @Test
  public void testDisplayName() {
    Assertions.assertEquals("dummy", testRegisterGamePartial.displayName());
    Assertions.assertEquals("proper dummy", testRegisterGameFull.displayName());
  }

  @Test
  public void testWebSupport() {
    Assertions.assertEquals("true", testRegisterGamePartial.webSupport());
    Assertions.assertEquals("enabled", testRegisterGameFull.webSupport());
  }
}