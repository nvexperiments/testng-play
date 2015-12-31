package edu.nitin.testng.issue112.reflect;

import org.testng.ITestContext;
import org.testng.ITestResult;

import java.lang.reflect.Method;

/**
 * Checks the conformance as per data-provide specifications.
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public class DataProviderMethodMatcher implements MethodMatcher {

  private final Method method;
  private final Object[] userArguments;
  private final ITestContext context;
  private final ITestResult testResult;
  private final DirectMethodMatcher directMethodMatcher;
  private final ArrayEndingMethodMatcher arrayEndingMethodMatcher;
  private Boolean conforms = null;
  private MethodMatcher matchingMatcher = null;

  public DataProviderMethodMatcher(final Method method, final Object[] userArguments,
                                   final ITestContext context, final ITestResult testResult) {
    this.method = method;
    this.userArguments = userArguments;
    this.context = context;
    this.testResult = testResult;
    this.directMethodMatcher = new DirectMethodMatcher(method, userArguments, context, testResult);
    this.arrayEndingMethodMatcher = new ArrayEndingMethodMatcher(method, userArguments, context, testResult);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean conforms() throws MethodMatcherException {
    boolean matching = false;
    try {
      if (directMethodMatcher.conforms()) {
        matching = true;
        matchingMatcher = directMethodMatcher;
      } else if (arrayEndingMethodMatcher.conforms()) {
        matching = true;
        matchingMatcher = arrayEndingMethodMatcher;
      }
    } finally {
      conforms = matching;
    }
    return matching;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] getConformingArguments() throws MethodMatcherException {
    if (conforms == null) {
      conforms();
    }
    if (matchingMatcher != null) {
      return matchingMatcher.getConformingArguments();
    }
    throw new MethodMatcherException("Data provider mismatch", method, userArguments);
  }
}
