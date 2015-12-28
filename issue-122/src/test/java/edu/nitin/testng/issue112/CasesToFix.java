package edu.nitin.testng.issue112;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created on 12/24/15
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public class CasesToFix {

  private static final Object[] S0 = new Object[]{"3", new String[]{"three", "four"}};
  private static final Object[] S1 = new Object[]{"3", new String[]{"three", "four"}};
  private static final Object[] S2 = new String[]{"3", "three", "four"};
  private static final Object[] C0 = new Object[]{3, true, new String[]{"three"}, "four"};
  private static final Object[] C1 = new Object[]{3, true, new String[]{"three"}, new String[]{"four"}};

  @DataProvider
  public Object[][] data() {
    return new Object[][]{S0, S1, S2};
  }

  @DataProvider
  public Object[][] data2() {
    return new Object[][]{C0, C1};
  }

  @Test(dataProvider = "data")
  public void goodTestIssue122(String s, String[] strings) {
    for (String item : strings) {
      System.out.println("An item is \"" + item + "\"");
    }
  }

  @Test(dataProvider = "data")
  public void badTestIssue122(String s, String... strings) {
    for (String item : strings) {
      System.out.println("An item is \"" + item + "\"");
    }
  }

  @Test(dataProvider = "data2")
  public void mixedArgs(
    final int i, final Boolean b,
    final String[] s1, final String... s2
  ) {
    for (String item : s2) {
      System.out.println("An item is \"" + item + "\"");
    }
  }
}
