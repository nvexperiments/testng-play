package edu.nitin.testng.issue112.reflect;

/**
 * An interface to valid conformance of input parameters to it's target method.
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public interface MethodMatcher {
  /**
   * Checks if the params conform to the method.
   *
   * @return conformance
   * @throws MethodMatcherException if any internal failure.
   */
  boolean conforms() throws MethodMatcherException;

  /**
   * If possible gives an array consumable by java method invoker.
   *
   * @return conforming argument array
   * @throws MethodMatcherException internal failure or non-conformance
   */
  Object[] getConformingArguments() throws MethodMatcherException;
}
