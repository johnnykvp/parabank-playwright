package models;

public class Account {
  public long id;
  public long customerId;
  public String type;
  public double balance;

  @Override
  public String toString() {
    return "Account{id=" + id + ", customerId=" + customerId
        + ", type='" + type + "', balance=" + balance + "}";
  }
}
