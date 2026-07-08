package tests;

import base.TestBase;
import org.junit.jupiter.api.Test;
import pages.RegistrationPage;
import utils.TestDataGenerator;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegistrationTest extends TestBase {

  @Test
  void newUserCanRegisterSuccessfully() {
    RegistrationPage registrationPage = new RegistrationPage(page);
    String username = TestDataGenerator.uniqueUsername("qauser");

    registrationPage.goTo(BASE_URL.replace("/index.htm", ""));
    registrationPage.register(
        "Joe", "Jollof", "123 Main St", "Seattle", "WA", "98101",
        "2065551234", "123456789", username, "TestPass123!"
    );

    assertThat(registrationPage.welcomeMessage()).containsText("Welcome " + username);
    assertThat(registrationPage.successMessage()).containsText("Your account was created successfully. You are now logged in.");
  }
}
