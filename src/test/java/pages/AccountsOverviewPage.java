package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AccountsOverviewPage {

  private final Page page;
  private final Locator pageTitle;
  private final Locator firstAccountLink;
  private final Locator totalBalance;

  public AccountsOverviewPage(Page page) {
    this.page = page;
    // There are two title elements, one is hidden
    this.pageTitle = page.locator("h1.title").locator("visible=true");
    // tr[1] = first row, td[1] = first cell in row
    this.firstAccountLink = page.locator("xpath=//*[@id=\"accountTable\"]/tbody/tr[1]/td[1]/a");
    this.totalBalance = page.locator("#accountTable tfoot td").last();
  }
  //*[@id="accountTable"]/tbody/tr[1]/td[1]/a
  public Locator pageTitle() {
    return pageTitle;
  }

  public String firstAccountId() {
    //return accountLinks.first().textContent();
    return firstAccountLink.textContent();
  }

  public Locator totalBalance() {
    return totalBalance;
  }
}
