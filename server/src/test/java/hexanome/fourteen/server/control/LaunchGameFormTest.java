package hexanome.fourteen.server.control;

import hexanome.fourteen.server.control.form.LaunchGameForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class LaunchGameFormTest {

  @Test
  public void testNoArgsConstructor() {
    LaunchGameForm expectedGame = new LaunchGameForm();

    Assertions.assertNull(expectedGame.creator());
    Assertions.assertNull(expectedGame.gameType());
    Assertions.assertNull(expectedGame.players());
    Assertions.assertNull(expectedGame.saveGame());
  }
}