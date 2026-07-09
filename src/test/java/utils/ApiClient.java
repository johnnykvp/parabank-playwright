package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.BrowserContext;
import models.Account;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Thin wrapper around Playwright's request API. Reuses the BrowserContext's
 * cookies, so any API calls made through this client are authenticated as
 * whichever user is currently logged in via the UI — no separate token/login
 * handling needed for this project's read-only validation calls.
 */
public class ApiClient {

  private static final Type ACCOUNT_LIST_TYPE = new TypeToken<List<Account>>() {}.getType();

  private final APIRequestContext request;
  private final Gson gson = new Gson();

  public ApiClient(BrowserContext context) {
    this.request = context.request();
  }

  public APIResponse get(String url) {
    return request.get(url);
  }

  public String getBody(String url) {
    APIResponse response = get(url);
    return response.text();
  }

  public int getStatus(String url) {
    return get(url).status();
  }

  /** Fetches and deserializes the accounts list from the given accounts endpoint URL. */
  public List<Account> getAccounts(String accountsApiUrl) {
    APIResponse response = get(accountsApiUrl);
    return gson.fromJson(response.text(), ACCOUNT_LIST_TYPE);
  }

  /** Fetches the accounts list and returns the count. */
  public int getAccountCount(String accountsApiUrl) {
    return getAccounts(accountsApiUrl).size();
  }
}