package hexanome.fourteen.server.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class UserTest {

  @Test
  public void testNoArgsConstructor() {
    User expectedUser = new User();

    Assertions.assertNull(expectedUser.name());
    Assertions.assertNull(expectedUser.preferredColour());
  }
}