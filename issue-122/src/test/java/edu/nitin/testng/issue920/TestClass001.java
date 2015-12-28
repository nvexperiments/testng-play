package edu.nitin.testng.issue920;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * Created on 12/29/15.
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public class TestClass001 {
  private static final Object[] S1 = new String[]{"001", "002"};
  private static final Object[] S2 = new String[]{"001", "002", "003", "004", "005"};
  private static final Object[] O1 = new Object[]{920, "002", "003", "004", "005"};
  private static final Object[] A1 = new Object[]{new String[]{"0011", "0012"}, "002", "003", "004", "005"};

  public static void main(String[] args) {
    test("001", "002", "003", "004");
    // Execute as Java application.
    // The Length is 1
    // The Length is 3
  }

  @DataProvider(name = "testData")
  public static Object[][] testDataProvider() {
    return new Object[][]{
      S1, S2
    };
  }

  @DataProvider(name = "testData2")
  public static Object[][] testDataProvider2() {
    return new Object[][]{
      S1, S2, O1, A1
    };
  }

  @DataProvider(name = "testData3")
  public static Object[][] testDataProvider3() {
    return new Object[][]{
      O1
    };
  }

  @DataProvider(name = "testData4")
  public static Object[][] testDataProvider4() {
    return new Object[][]{
      A1
    };
  }

  @Test(dataProvider = "testData")
  public static void test(String string, String... str002) {
    System.out.println("The String " + string);
    System.out.println("The Length is " + str002.length);

  }

  @Test(dataProvider = "testData2")
  public static void test2(Object object, String... str002) {
    System.out.println("The Object " + object + "/" + object.getClass());
    System.out.println("The Length is " + str002.length);
  }

  @Test(dataProvider = "testData3")
  public static void test3(int i, String... str002) {
    System.out.println("The int " + i);
    System.out.println("The Length is " + str002.length);
  }

  @Test(dataProvider = "testData4")
  public static void test4(String[] array, String... str002) {
    System.out.println("The array " + Arrays.asList(array));
    System.out.println("The Length is " + str002.length);
  }
}
