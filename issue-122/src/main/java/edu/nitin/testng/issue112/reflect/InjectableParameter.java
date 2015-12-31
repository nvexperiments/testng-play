package edu.nitin.testng.issue112.reflect;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 12/29/15.
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public enum InjectableParameter {
  CURRENT_TEST_METHOD, ITEST_CONTEXT, ITEST_RESULT, XML_TEST;

  /**
   * convenience means to add and remove injectables.
   */
  public static class Assistant {
    public static final Set<InjectableParameter> NONE = null;
    public static final Set<InjectableParameter> ALL_INJECTS = get(true, true, true, true);

    private static Set<InjectableParameter> get(
      final boolean firstMethod, final boolean iTestContext,
      final boolean iTestResult, final boolean xmlTest
    ) {
      final Set<InjectableParameter> filters = new HashSet<InjectableParameter>(4);
      if (firstMethod) {
        filters.add(CURRENT_TEST_METHOD);
      }
      if (iTestContext) {
        filters.add(ITEST_CONTEXT);
      }
      if (iTestResult) {
        filters.add(ITEST_RESULT);
      }
      if (xmlTest) {
        filters.add(XML_TEST);
      }
      return filters;
    }
  }
}
