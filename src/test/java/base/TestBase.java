package base;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

/**
 * A shared setup/teardown that every Test class extends to avoid code duplication.
 */
public class TestBase {

  protected static Playwright playwright;
  protected static Browser browser;
  protected BrowserContext context;
  protected Page page;

  protected static final String BASE_URL = "https://parabank.parasoft.com/parabank/index.htm";

  @BeforeAll
  static void launchBrowser() {
    playwright = Playwright.create();
    boolean isCI = System.getenv("CI") != null;
    browser = playwright.chromium().launch(
        new BrowserType.LaunchOptions().setHeadless(isCI)
    );
  }

  @BeforeEach
  void createContextAndPage() {
    context = browser.newContext();
    context.tracing().start(new Tracing.StartOptions()
        .setScreenshots(true)
        .setSnapshots(true));
    page = context.newPage();
  }

  @AfterEach
  void closeContext(TestInfo testInfo) {
    context.tracing().stop(new Tracing.StopOptions()
        .setPath(java.nio.file.Paths.get(
            "traces/" + testInfo.getDisplayName().replaceAll("\\s+", "_") + ".zip")));
    context.close();
  }

  @AfterAll
  static void closeBrowser() {
    browser.close();
    playwright.close();
  }
}
