package edu.nitin.testng.issue112.reflect;

import org.testng.ITestContext;
import org.testng.ITestResult;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import static edu.nitin.testng.issue112.reflect.InjectableParameter.Assistant.ALL_INJECTS;

/**
 * Checks for array ending method argument exactMatch with or without ITestContext filtering.
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 * @see ReflectionRecipes#matchArrayEnding(Class[], Object[])
 */
public class ArrayEndingMethodMatcher implements MethodMatcher {

  private final Method method;
  private final Parameter[] methodParameter;
  private final ITestContext context;
  private final ITestResult testResult;
  private final Object[] userArguments;
  private Boolean conforms = null;
  private Parameter[] conformingParameters = null;

  public ArrayEndingMethodMatcher(
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
   * @see ReflectionRecipes#matchArrayEnding(Class[], Object[])
   */
  @Override
  public boolean conforms() throws MethodMatcherException {
    boolean matching = false;
    Parameter[] parameters = methodParameter;
    try {
      parameters = ReflectionRecipes.filter(parameters, ALL_INJECTS);
      matching = ReflectionRecipes.matchArrayEnding(parameters, userArguments);
      if (matching) {
        conformingParameters = parameters;
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
      throw new MethodMatcherException("Array ending mismatch", method, userArguments);
    }

    final Class<?>[] classes = ReflectionRecipes.classesFromParameters(conformingParameters);
    final Object[] objects = new Object[classes.length];
    final Class<?> componentType = classes[classes.length - 1].getComponentType();
    final Object array = Array.newInstance(componentType, userArguments.length - classes.length + 1);
    System.arraycopy(userArguments, 0, objects, 0, classes.length - 1);
    int j = 0;
    for (int i = classes.length - 1; i < userArguments.length; i++, j++) {
      Array.set(array, j, userArguments[i]);
    }
    objects[classes.length - 1] = array;
    return ReflectionRecipes.inject(
      methodParameter, ALL_INJECTS, objects,
      method, context, testResult
    );
  }
}
