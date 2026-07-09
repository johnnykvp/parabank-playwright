package tests;

import base.TestBase;
import models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.AccountsOverviewPage;
import pages.LoginPage;
import pages.RegistrationPage;
import utils.ApiClient;
import utils.TestDataGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApiValidationTest extends TestBase {

  private String username;
  private final String password = "TestPass123!";
  private ApiClient apiClient;

  @BeforeEach
  void registerFreshUser() {
    RegistrationPage registrationPage = new RegistrationPage(page);
    username = TestDataGenerator.uniqueUsername("qauser");

    registrationPage.goTo(BASE_URL.replace("/index.htm", ""));
    registrationPage.register(
            "Joe", "Jollof", "123 Main St", "Seattle", "WA", "98101",
            "2065551234", "123456789", username, password
    );
    new LoginPage(page).logout();

    LoginPage loginPage = new LoginPage(page);
    loginPage.goTo(BASE_URL);
    loginPage.login(username, password);

    apiClient = new ApiClient(context);
  }

  @Test
  void accountsApiReturnsSuccessAndValidJsonStructure() {
    AccountsOverviewPage overviewPage = new AccountsOverviewPage(page);
    String baseUrl = BASE_URL.replace("/index.htm", "");

    // Only the URL comes from the page-navigation-triggered response.
    // The actual body is read via a fresh direct call (ApiClient) to avoid
    // CDP "No resource with given identifier found" issues.
    String accountsApiUrl = overviewPage.captureAccountsApiUrl(page, baseUrl);

    assertEquals(200, apiClient.getStatus(accountsApiUrl),
            "Accounts endpoint should return 200 OK after successful login");

    List<Account> accounts = apiClient.getAccounts(accountsApiUrl);

    assertFalse(accounts.isEmpty(), "Newly registered user should have at least one account");
    for (Account account : accounts) {
      assertTrue(account.id > 0, "Account id should be a positive number");
      assertNotNull(account.type, "Account type should not be null");
      assertTrue(account.balance >= 0, "Starter account balance should not be negative");
    }
  }

  @Test
  void accountsApiDataMatchesWhatUiDisplays() {
    AccountsOverviewPage overviewPage = new AccountsOverviewPage(page);
    String baseUrl = BASE_URL.replace("/index.htm", "");

    String accountsApiUrl = overviewPage.captureAccountsApiUrl(page, baseUrl);
    List<Account> accountsFromApi = apiClient.getAccounts(accountsApiUrl);

    List<String> accountIdsFromUi = overviewPage.displayedAccountIds();
    List<String> accountIdsFromApi = accountsFromApi.stream()
            .map(a -> String.valueOf(a.id))
            .toList();
    System.out.println("UI:" + accountIdsFromUi);
    System.out.println("API:" + accountIdsFromApi);

    assertEquals(accountIdsFromUi.size(), accountIdsFromApi.size(),
            "Number of accounts shown in the UI should match the number returned by the API");
    assertTrue(accountIdsFromApi.containsAll(accountIdsFromUi),
            "Every account ID shown in the UI should be present in the API response");
  }

  @Test
  void accountCountFromApiMatchesAccountCountInUi() {
    AccountsOverviewPage overviewPage = new AccountsOverviewPage(page);
    String baseUrl = BASE_URL.replace("/index.htm", "");

    String accountsApiUrl = overviewPage.captureAccountsApiUrl(page, baseUrl);

    int accountCountFromApi = apiClient.getAccountCount(accountsApiUrl);
    int accountCountFromUi = overviewPage.accountCount();

    assertTrue(accountCountFromApi > 0, "Newly registered user should have at least one account");
    assertEquals(accountCountFromUi, accountCountFromApi,
            "Account count shown in the UI should match the count returned by the API");
  }

  @Test
  void repeatedDirectCallsToAccountsEndpointReturnConsistentData() {
    AccountsOverviewPage overviewPage = new AccountsOverviewPage(page);
    String baseUrl = BASE_URL.replace("/index.htm", "");

    String accountsApiUrl = overviewPage.captureAccountsApiUrl(page, baseUrl);

    int firstCallCount = apiClient.getAccountCount(accountsApiUrl);
    int secondCallCount = apiClient.getAccountCount(accountsApiUrl);

    assertEquals(200, apiClient.getStatus(accountsApiUrl));
    assertEquals(firstCallCount, secondCallCount,
            "Calling the accounts endpoint twice in a row should return consistent data");
  }
}
