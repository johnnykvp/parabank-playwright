package utils;

public class TestDataGenerator {

  public static String uniqueUsername(String prefix) {
    return prefix + System.currentTimeMillis();
  }
}
