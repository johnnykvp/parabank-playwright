package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TransferFundsPage {

  private final Page page;
  private final Locator amountInput;
  private final Locator fromAccountSelect;
  private final Locator toAccountSelect;
  private final Locator transferButton;
  private final Locator confirmationHeading;
  private final Locator confirmationAmount;

  public TransferFundsPage(Page page) {
    this.page = page;
    this.amountInput = page.locator("#amount");
    this.fromAccountSelect = page.locator("#fromAccountId");
    this.toAccountSelect = page.locator("#toAccountId");
    this.transferButton = page.locator("input[value='Transfer']");
    this.confirmationHeading = page.locator("h1.title");
    this.confirmationAmount = page.locator("#amountResult");
  }

  public void goTo(String baseUrl) {
    page.navigate(baseUrl + "/transfer.htm");
  }

  public void transfer(String amount, String fromAccountId, String toAccountId) {
    amountInput.fill(amount);
    fromAccountSelect.selectOption(fromAccountId);
    toAccountSelect.selectOption(toAccountId);
    transferButton.click();
  }

  public Locator confirmationHeading() {
    return confirmationHeading;
  }

  public Locator confirmationAmount() {
    return confirmationAmount;
  }
}
