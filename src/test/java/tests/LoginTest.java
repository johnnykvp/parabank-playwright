package tests;

import base.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.AccountsOverviewPage;
import pages.LoginPage;
import pages.RegistrationPage;
import utils.TestDataGenerator;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends TestBase {

  private String username;
  private final String password = "TestPass123!";

  @BeforeEach
  void registerFreshUser() {
    // ParaBank resets shared demo data periodically, so each test run registers
    // its own user rather than relying on hardcoded credentials.
    RegistrationPage registrationPage = new RegistrationPage(page);
    username = TestDataGenerator.uniqueUsername("qauser");

    registrationPage.goTo(BASE_URL.replace("/index.htm", ""));
    registrationPage.register(
        "Joe", "Jollof", "123 Main St", "Seattle", "WA", "98101",
        "2065551234", "123456789", username, password
    );

    new LoginPage(page).logout();
  }

  @Test
  void validUserCanLogInAndSeeAccountsOverview() {
    LoginPage loginPage = new LoginPage(page);
    AccountsOverviewPage overviewPage = new AccountsOverviewPage(page);

    loginPage.goTo(BASE_URL);
    loginPage.login(username, password);

    assertThat(overviewPage.pageTitle()).hasText("Accounts Overview");
    assertTrue(overviewPage.totalBalance().isVisible());
  }

  @Test
  void invalidPasswordShowsErrorMessage() {
    LoginPage loginPage = new LoginPage(page);

    loginPage.goTo(BASE_URL);
    loginPage.login(username, "WrongPassword!");

    assertFalse(loginPage.getErrorText().isEmpty());
  }

  @Test
  void unknownUsernameShowsErrorMessage() {
    LoginPage loginPage = new LoginPage(page);

    loginPage.goTo(BASE_URL);
    loginPage.login("nonexistent_user_12345", password);

    assertFalse(loginPage.getErrorText().isEmpty());
  }
}
