package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 *
 */
public class RegistrationPage {

  private final Page page;

  public RegistrationPage(Page page) {
    this.page = page;
  }

  public void goTo(String baseUrl) {
    page.navigate(baseUrl + "/register.htm");
  }

  public void register(String firstName, String lastName, String address, String city,
                        String state, String zipCode, String phone, String ssn,
                        String username, String password) {
    page.locator("#customer\\.firstName").fill(firstName);
    page.locator("#customer\\.lastName").fill(lastName);
    page.locator("#customer\\.address\\.street").fill(address);
    page.locator("#customer\\.address\\.city").fill(city);
    page.locator("#customer\\.address\\.state").fill(state);
    page.locator("#customer\\.address\\.zipCode").fill(zipCode);
    page.locator("#customer\\.phoneNumber").fill(phone);
    page.locator("#customer\\.ssn").fill(ssn);
    page.locator("#customer\\.username").fill(username);
    page.locator("#customer\\.password").fill(password);
    page.locator("#repeatedPassword").fill(password);
    page.locator("input.button[value='Register']").click();
  }

  public Locator welcomeMessage() {
    return page.locator("#rightPanel h1");
  }

  public Locator successMessage() {
    return page.locator("#rightPanel p");
  }
}
