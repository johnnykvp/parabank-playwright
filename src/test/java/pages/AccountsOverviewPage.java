package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AccountsOverviewPage {

  private final Page page;
  private final Locator pageTitle;
  private final Locator accountLinks;
  private final Locator totalBalance;

  public AccountsOverviewPage(Page page) {
    this.page = page;
    this.pageTitle = page.locator("h1.title");
    this.accountLinks = page.locator("#accountTable a.account");
    this.totalBalance = page.locator("#accountTable tfoot td").last();
  }

  public Locator pageTitle() {
    return pageTitle;
  }

  public int accountCount() {
    return accountLinks.count();
  }

  public String firstAccountId() {
    return accountLinks.first().textContent();
  }

  public Locator totalBalance() {
    return totalBalance;
  }
}
