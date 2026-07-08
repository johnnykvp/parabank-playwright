package tests;

import base.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.AccountsOverviewPage;
import pages.RegistrationPage;
import pages.TransferFundsPage;
import utils.TestDataGenerator;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TransferFundsTest extends TestBase {

  @BeforeEach
  void registerAndLogIn() {
    RegistrationPage registrationPage = new RegistrationPage(page);
    String username = TestDataGenerator.uniqueUsername("qauser");
    String password = "TestPass123!";

    registrationPage.goTo(BASE_URL.replace("/index.htm", ""));
    registrationPage.register(
        "Joe", "Jollof", "123 Main St", "Seattle", "WA", "98101",
        "2065551234", "123456789", username, password
    );
    // ParaBank auto-logs-in after registration and creates a starter checking account,
    // so no separate login step is needed here.
  }

  @Test
  void transferBetweenOwnAccountsShowsConfirmation() {
    AccountsOverviewPage overviewPage = new AccountsOverviewPage(page);
    String accountId = overviewPage.firstAccountId();

    TransferFundsPage transferPage = new TransferFundsPage(page);
    transferPage.goTo(BASE_URL.replace("/index.htm", ""));
    transferPage.transfer("100", accountId, accountId);

    assertThat(transferPage.confirmationHeading()).hasText("Transfer Complete!");
    assertThat(transferPage.confirmationAmount()).hasText("$100.00");
  }
}
