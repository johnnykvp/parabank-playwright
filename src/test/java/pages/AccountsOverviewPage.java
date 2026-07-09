package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountsOverviewPage {

  private final Page page;
  private final Locator pageTitle;
  private final Locator accountRows;
  private final Locator totalBalance;

  public AccountsOverviewPage(Page page) {
    this.page = page;
    // There are two title elements, one is hidden
    this.pageTitle = page.locator("h1.title").locator("visible=true");
    // tbody includes the Total Balance row at the end of the table
    this.accountRows = page.locator("#accountTable tbody tr td:first-child a");
    this.totalBalance = page.locator("#accountTable tfoot td").last();
  }

  public Locator pageTitle() {
    return pageTitle;
  }

  public int accountCount() {
    return accountRows.count();
  }

  public String firstAccountId() {
    return accountRows.first().textContent();
  }

  public Locator totalBalance() {
    return totalBalance;
  }

  /**
   * Navigates to the overview page while capturing the URL of the underlying
   * accounts API call the page itself makes. Only the URL is used — the body
   * of this navigation-triggered response is intentionally NOT read here.
   * Reading response bodies from page-navigation-triggered requests via CDP
   * can hit "No resource with given identifier found" if the browser's
   * network buffer clears the resource before/while you read it. Callers
   * should re-fetch this URL via ApiClient to reliably read the body.
   */
  public String captureAccountsApiUrl(Page page, String baseUrl) {
    Response response = page.waitForResponse(
            resp -> resp.url().contains("/services_proxy/bank/customers/")
                    && resp.url().endsWith("/accounts")
                    && resp.status() == 200,
            () -> page.navigate(baseUrl + "/overview.htm")
    );
    return response.url();
  }

  /** Extracts the customerId embedded in the accounts API URL, e.g. .../customers/12345/accounts */
  public static String extractCustomerId(String accountsApiUrl) {
    Matcher matcher = Pattern.compile("/customers/(\\d+)/accounts").matcher(accountsApiUrl);
    if (matcher.find()) {
      return matcher.group(1);
    }
    throw new IllegalArgumentException("Could not extract customerId from URL: " + accountsApiUrl);
  }

  /** Reads all account IDs shown in the UI table, for cross-checking against the API response. */
  public List<String> displayedAccountIds() {
    List<String> ids = new ArrayList<>();
    // Subtract - 1, as it will include the Total Balance row
    int count = accountRows.count();
    for (int i = 0; i < count; i++) {
      ids.add(accountRows.nth(i).textContent().trim());
    }
    return ids;
  }
}
