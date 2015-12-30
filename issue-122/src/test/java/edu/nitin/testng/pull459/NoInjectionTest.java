package edu.nitin.testng.pull459;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.NoInjection;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Created on 12/29/15.
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public class NoInjectionTest {
  public void f() {
  }

  @DataProvider(name = "singleValueProvider")
  public Object[][] provide() throws Exception {
    return new Object[][]{{NoInjectionTest.class.getMethod("f")}};
  }

  @Test(dataProvider = "singleValueProvider")
  public void withoutInjection(@NoInjection Method m) {
    Assert.assertEquals(m.getName(), "f");
  }

  @Test(dataProvider = "singleValueProvider")
  public void withInjection(Method m, Method m2) {
    System.out.println("m = " + m + ", m2 = " + m2);
    Assert.assertEquals(m.getName(), "withInjection");
    Assert.assertEquals(m2.getName(), "f");
  }

  // Multi-injection test
  @DataProvider
  public Object[][] multiValuedProvider() throws NoSuchMethodException {
    return new Object[][]{
      {null, "some_data"}
    };
  }

  @Test(dataProvider = "multiValuedProvider")
  public void multiValuedTest1(@NoInjection Method providedMethod, Method thisMethod, String data) {
    Assert.assertNull(providedMethod);
    Assert.assertEquals(thisMethod.getName(), "multiValuedTest1");
    Assert.assertEquals(data, "some_data");
  }

  @Test(dataProvider = "multiValuedProvider")
  public void multiValuedTest2(Method thisMethod, @NoInjection Method providedMethod, String data) {
    System.out.println("thisMethod = " + thisMethod + ", providedMethod = " + providedMethod
      + ", data = " + data);
    Assert.assertNull(providedMethod);
    Assert.assertEquals(thisMethod.getName(), "multiValuedTest2");
    Assert.assertEquals(data, "some_data");
  }
}
