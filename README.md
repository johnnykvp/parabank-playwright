# Playwright Java Automation Framework — ParaBank

End-to-end test automation suite built with Playwright for Java, JUnit 5, and
Maven, targeting [ParaBank](https://parabank.parasoft.com), a public banking
demo application. Uses the Page Object Model (POM) to separate test logic
from page structure, mirroring the approach used professionally for
financial-services test automation at JPMorgan Chase.

## What this covers
- **Registration**: new user signup with a dynamically generated username
- **Login**: valid login, invalid password, unknown username
- **Fund transfer**: transferring between accounts and verifying the
  confirmation screen

## Why dynamic test data
ParaBank's demo environment doesn't provide fixed, guaranteed login
credentials — the underlying data resets periodically. Rather than hardcoding
a username that may stop working, each test registers its own unique user at
runtime (`TestDataGenerator.uniqueUsername`), which is a more realistic
pattern for testing against a live, shared environment anyway.

## Tech stack
- Playwright for Java
- JUnit 5
- Maven
- Page Object Model architecture
- GitHub Actions for CI/CD (nightly scheduled runs + on push)
- Trace capture on failure for debugging

## Project structure
```
src/test/java/
  base/       # TestBase — browser/context lifecycle shared across tests
  pages/      # Page Object classes (LoginPage, RegistrationPage, AccountsOverviewPage, TransferFundsPage)
  tests/      # Test classes organized by feature
  utils/      # Test data generation helpers
pom.xml
.github/workflows/nightly.yml
```

## Running locally
```bash
mvn test
```

First run downloads Playwright's browser binaries automatically via the
Maven dependency; if needed, install explicitly with:
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"
```

View screenshots through trace
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace traces/validUserCanLogInAndSeeAccountsOverview().zip"
```

## CI/CD
Tests run automatically nightly via GitHub Actions, and on every push to
`main`. Traces are uploaded as workflow artifacts on failure for debugging,
and Surefire test reports are uploaded on every run.

## Design decisions
- **POM pattern**: locators and page interactions live in page classes, not
  test classes.
- **Dynamic test data over hardcoded credentials**: avoids brittle tests tied
  to shared demo accounts that can be modified or reset outside your control.
- **Shared TestBase**: centralizes Playwright/browser/context setup so test
  classes stay focused on test logic.

## Next steps / possible extensions
- Add API-level tests against ParaBank's REST/SOAP services
- Add bill pay and account-opening flow coverage
- Add negative/boundary tests for transfer amounts (e.g., overdraft, zero, negative values)