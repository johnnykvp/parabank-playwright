package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {

  private final Page page;
  private final Locator usernameInput;
  private final Locator passwordInput;
  private final Locator loginButton;
  private final Locator errorMessage;

  public LoginPage(Page page) {
    this.page = page;
    this.usernameInput = page.locator("input[name='username']");
    this.passwordInput = page.locator("input[name='password']");
    this.loginButton = page.locator("input.button[value='Log In']");
    this.errorMessage = page.locator(".error");
  }

  public void goTo(String baseUrl) {
    page.navigate(baseUrl);
  }

  public void login(String username, String password) {
    usernameInput.fill(username);
    passwordInput.fill(password);
    loginButton.click();
  }

  public String getErrorText() {
    return errorMessage.textContent();
  }

  public void logout() {
    page.locator("a[href='logout.htm']").click();
  }
}
