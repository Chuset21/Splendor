package hexanome.fourteen.server.control;

import hexanome.fourteen.server.control.form.LoginForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class LoginFormTest {

  @Test
  void noArgsConstructor() {
    LoginForm expectedLoginForm = new LoginForm();

    Assertions.assertNull(expectedLoginForm.accessToken());
    Assertions.assertNull(expectedLoginForm.tokenType());
    Assertions.assertNull(expectedLoginForm.refreshToken());
    Assertions.assertEquals(0, expectedLoginForm.expiresIn());
    Assertions.assertNull(expectedLoginForm.scope());
  }
}