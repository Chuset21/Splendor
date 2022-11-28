package hexanome.fourteen.server.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
class RegisterGameServiceFormTest {

  @Test
  void noArgsConstructor() {
    RegisterGameServiceForm expectedRegisteredGame = new RegisterGameServiceForm();

    Assertions.assertNull(expectedRegisteredGame.location());
    Assertions.assertEquals(0, expectedRegisteredGame.maxSessionPlayers());
    Assertions.assertEquals(0, expectedRegisteredGame.minSessionPlayers());
    Assertions.assertNull(expectedRegisteredGame.name());
    Assertions.assertNull(expectedRegisteredGame.displayName());
    Assertions.assertNull(expectedRegisteredGame.webSupport());
  }

  void location() {
  }

  @Test
  void maxSessionPlayers() {
  }

  @Test
  void minSessionPlayers() {
  }

  @Test
  void name() {
  }

  @Test
  void displayName() {
  }

  @Test
  void webSupport() {
  }
}