package hexanome.fourteen.server.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;

@TestInstance(PER_CLASS)
public class AccountCreationFormTest {

  static AccountCreationForm dummyAccount;

  @BeforeAll
  public static void setUp() {
    dummyAccount = new AccountCreationForm("Hex 14 Backend", "1234");
  }

  @Test
  public void testGetName() {
    Assertions.assertEquals("Hex 14 Backend", dummyAccount.getName());
  }

  @Test
  public void testGetPassword() {
    Assertions.assertEquals("1234", dummyAccount.getPassword());
  }

  @Test
  public void testGetPreferredColour() {
    Assertions.assertEquals("01FFFF", dummyAccount.getPreferredColour());
  }

  @Test
  public void testGetRole() {
    Assertions.assertEquals("ROLE_SERVICE", dummyAccount.getRole());
  }

  @Test
  public void testNoArgConstructor() {
    AccountCreationForm expectedAccount = new AccountCreationForm();

    Assertions.assertNull(expectedAccount.getName());
    Assertions.assertNull(expectedAccount.getRole());
    Assertions.assertNull(expectedAccount.getPassword());
    Assertions.assertNull(expectedAccount.getPreferredColour());
  }
}