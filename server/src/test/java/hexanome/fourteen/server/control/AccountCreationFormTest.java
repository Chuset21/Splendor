package hexanome.fourteen.server.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
class AccountCreationFormTest {

  AccountCreationForm dummyAccount;

  @BeforeAll
  void setUpDummyAccount() {
    dummyAccount = new AccountCreationForm("Hex 14 Backend", "1234");
  }

  @Test
  void getName() {
    Assertions.assertEquals("Hex 14 Backend", dummyAccount.getName());
  }

  @Test
  void getPassword() {
    Assertions.assertEquals("1234", dummyAccount.getPassword());
  }

  @Test
  void getPreferredColour() {
    Assertions.assertEquals("01FFFF", dummyAccount.getPreferredColour());
  }

  @Test
  void getRole() {
    Assertions.assertEquals("ROLE_SERVICE", dummyAccount.getRole());
  }

  @Test
  void noArgsConstructor() {
    AccountCreationForm expectedAccount = new AccountCreationForm();

    Assertions.assertNull(expectedAccount.getName());
    Assertions.assertNull(expectedAccount.getRole());
    Assertions.assertNull(expectedAccount.getPassword());
    Assertions.assertNull(expectedAccount.getPreferredColour());
  }
}