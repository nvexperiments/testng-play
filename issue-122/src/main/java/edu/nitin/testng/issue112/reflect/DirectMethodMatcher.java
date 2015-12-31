package edu.nitin.testng.issue112.reflect;

import org.testng.ITestContext;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static edu.nitin.testng.issue112.reflect.InjectableParameter.Assistant.ALL_INJECTS;
import static edu.nitin.testng.issue112.reflect.InjectableParameter.Assistant.NONE;

/**
 * Checks for method argument exactMatch with or without filtering injectables.
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public class DirectMethodMatcher implements MethodMatcher {
  private final Method method;
  private final Parameter[] methodParameter;
  private final ITestContext context;
  private final ITestResult testResult;
  private final Object[] userArguments;
  private Boolean conforms = null;
  private Parameter[] conformingParameters = null;
  private Set<InjectableParameter> conformingInjects = null;

  public DirectMethodMatcher(
    final Method method, final Object[] userArguments,
    final ITestContext context, ITestResult testResult) {
    this.method = method;
    this.methodParameter = ReflectionRecipes.getMethodParameters(method);
    this.userArguments = userArguments;
    this.context = context;
    this.testResult = testResult;
  }

  /**
   * {@inheritDoc}
   *
   * @see ReflectionRecipes#exactMatch(Class[], Object[])
   */
  @Override
  public boolean conforms() {
    boolean matching = false;
    try {
      final List<Set<InjectableParameter>> injectsOrder = new ArrayList<Set<InjectableParameter>>(2);
      injectsOrder.add(ALL_INJECTS);
      injectsOrder.add(NONE);
      for (final Set<InjectableParameter> injects : injectsOrder) {
        final Parameter[] parameters = ReflectionRecipes.filter(methodParameter, injects);
        matching = ReflectionRecipes.exactMatch(parameters, userArguments);
        if (matching) {
          conformingParameters = parameters;
          conformingInjects = injects;
          break;
        }
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
    if (conformingParameters == null) {
      throw new MethodMatcherException("Direct mismatch", method, userArguments);
    }
    return ReflectionRecipes.inject(
      methodParameter, InjectableParameter.Assistant.ALL_INJECTS, userArguments,
      method, context, testResult
    );
  }
}
