package edu.nitin.testng.issue112;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.DataProvider;
import org.testng.annotations.NoInjection;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;

/**
 * Created on 12/24/15
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public class CasesToFix {

  private static final Object[] A0 = new Object[]{"3", new String[]{"three", "four"}};
  private static final Object[] A1 = new Object[]{"3", new String[]{"three", "four"}};
  private static final Object[] A2 = new String[]{"3", "three", "four"};
  private static final Object[] B0 = new Object[]{3, true, new String[]{"three"}, "four"};
  private static final Object[] B1 = new Object[]{3, true, new String[]{"three"}, new String[]{"four"}};
  private static final Object[] C0 = new Object[]{
    getMethod(CasesToFix.class, "mixedArgs"),
    new XmlTestJustForTesting(),
    3,
    getMethod(CasesToFix.class, "badTestIssue122"),
    new TestContextJustForTesting(),
    true,
    new TestResultJustForTesting(),
    new String[]{"three"},
    new String[]{"four"}};

  private static final Object[] C1 = new Object[]{
    getMethod(CasesToFix.class, "mixedArgs"),
    new XmlTestJustForTesting(),
    3,
    getMethod(CasesToFix.class, "badTestIssue122"),
    new TestContextJustForTesting(),
    true,
    new TestResultJustForTesting(),
    new String[]{"three"},
    "four"};

  private static Method getMethod(final Class<?> clazz, final String methodName) {
    Method method = null;
    for (final Method m : clazz.getMethods()) {
      if (m.getName().equals(methodName)) {
        method = m;
      }
    }
    return method;
  }

  @DataProvider
  public Object[][] data() {
    return new Object[][]{A0, A1, A2};
  }

  @DataProvider
  public Object[][] data2() {
    return new Object[][]{B0, B1};
  }

  @DataProvider
  public Object[][] data3() {
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

  @Test(dataProvider = "data3")
  public void potpourri0(
    @NoInjection final Method myMethod1,
    @NoInjection final XmlTest myXmlTest,
    final Method currentTestMethod,
    final int i,
    final Method myMethod2,
    final ITestContext iTestContext,
    @NoInjection final ITestContext myTestContext,
    final Boolean b,
    @NoInjection final ITestResult myTestResult,
    final ITestResult iTestResult,
    final String[] s1,
    final XmlTest xmlTest,
    final String... strings
  ) {
    System.out.println("MyMethod1 is \"" + myMethod1 + "\"");
    System.out.println("MyMethod2 is \"" + myMethod2 + "\"");
    System.out.println("CurrentTestMethod is \"" + currentTestMethod + "\"");
    System.out.println("MyITestContext is \"" + myTestContext + "\"");
    System.out.println("ITestContext is \"" + iTestContext + "\"");
    System.out.println("ITestResult is \"" + iTestResult + "\"");
    System.out.println("MyTestResult is \"" + myTestResult + "\"");
    System.out.println("XmlTest is \"" + xmlTest + "\"");
    System.out.println("MyXmlTest is \"" + myXmlTest + "\"");
    for (String item : strings) {
      System.out.println("An item is \"" + item + "\"");
    }
    Assert.assertNotNull(myTestContext);
    Assert.assertTrue(myTestContext instanceof TestContextJustForTesting);
    Assert.assertNotNull(myTestResult);
    Assert.assertTrue(myTestResult instanceof TestResultJustForTesting);
    Assert.assertNotNull(myXmlTest);
    Assert.assertTrue(myXmlTest instanceof XmlTestJustForTesting);
    Assert.assertNotNull(currentTestMethod);
    Assert.assertEquals("potpourri0", currentTestMethod.getName());
    Assert.assertNotNull(myMethod1);
    Assert.assertEquals("mixedArgs", myMethod1.getName());
    Assert.assertNotNull(myMethod2);
    Assert.assertEquals("badTestIssue122", myMethod2.getName());
    Assert.assertEquals(i, 3);
    Assert.assertNotNull(b);
    Assert.assertTrue(b);
    Assert.assertNotNull(s1);
    Assert.assertEquals(s1.length, 1);
    Assert.assertEquals(s1[0], "three");
    Assert.assertNotNull(strings);
    Assert.assertEquals(strings.length, 1);
    Assert.assertEquals(strings[0], "four");
  }
}
