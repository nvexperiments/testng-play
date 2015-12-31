package edu.nitin.testng.issue112.reflect;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.NoInjection;
import org.testng.annotations.Optional;
import org.testng.xml.XmlTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Utility class to handle reflection.
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public final class ReflectionRecipes {

  private static final Map<Class, Class> PRIMITIVE_MAPPING = new HashMap<Class, Class>();

  static {
    PRIMITIVE_MAPPING.put(boolean.class, Boolean.class);
    PRIMITIVE_MAPPING.put(byte.class, Byte.class);
    PRIMITIVE_MAPPING.put(short.class, Short.class);
    PRIMITIVE_MAPPING.put(int.class, Integer.class);
    PRIMITIVE_MAPPING.put(long.class, Long.class);
    PRIMITIVE_MAPPING.put(float.class, Float.class);
    PRIMITIVE_MAPPING.put(double.class, Double.class);
    PRIMITIVE_MAPPING.put(char.class, Character.class);
    PRIMITIVE_MAPPING.put(void.class, Void.class);
  }

  private ReflectionRecipes() {
    throw new RuntimeException();
  }

  /**
   * Extracts method parameters.
   *
   * @param method any valid method.
   * @return extracted method parameters.
   */
  public static Parameter[] getMethodParameters(final Method method) {
    if (method != null) {
      final Class<?> parametersTypes[] = method.getParameterTypes();
      final Annotation[][] parametersAnnotations = method.getParameterAnnotations();
      final Parameter[] parameters = new Parameter[parametersTypes.length];
      for (int i = 0; i < parametersTypes.length; i++) {
        parameters[i] = new Parameter(i, parametersTypes[i], parametersAnnotations[i]);
      }
      return parameters;
    } else {
      return null;
    }
  }

  /**
   * Creates a map of parameter index, parameter.
   *
   * @param method  Method to analyse for optionals.
   * @param filters
   * @return Map of parameter index, parameter.
   */
  public static Map<Integer, Parameter> getOptionalParameters(
    final Method method,
    final Set<InjectableParameter> filters) {
    final boolean currentTestMethod = filters.contains(InjectableParameter.CURRENT_TEST_METHOD);
    final Map<Integer, Parameter> optionalParameters = new TreeMap<Integer, Parameter>();
    final Parameter[] parameters = getMethodParameters(method);
    boolean isFirstMethodSeen = false;
    for (int i = 0; i < parameters.length; i++) {
      final Parameter parameter = parameters[i];
      if (isOptional(parameter, filters, isFirstMethodSeen)) {
        optionalParameters.put(i, parameter);
      }
      if (currentTestMethod && !isFirstMethodSeen) {
        isFirstMethodSeen = canInject(parameter, InjectableParameter.CURRENT_TEST_METHOD);
      }

    }
    return optionalParameters;
  }

  /**
   * Checks if parameter is optional or not.
   *
   * @param parameter         parameter to be tested.
   * @param filters           which all injects to use.
   * @param isFirstMethodSeen is current test method injected for this method.
   * @return optional or not.
   */
  public static boolean isOptional(final Parameter parameter, final Set<InjectableParameter> filters,
                                   final boolean isFirstMethodSeen) {
    final boolean currentTestMethod = filters.contains(InjectableParameter.CURRENT_TEST_METHOD);
    final boolean iTestContext = filters.contains(InjectableParameter.ITEST_CONTEXT);
    final boolean iTestResult = filters.contains(InjectableParameter.ITEST_RESULT);
    final boolean xmlTest = filters.contains(InjectableParameter.XML_TEST);
    final boolean optional = parameter.isAnnotationPresent(Optional.class);
    boolean willInject = false;
    if (currentTestMethod && !isFirstMethodSeen) {
      willInject = canInject(parameter, InjectableParameter.CURRENT_TEST_METHOD);
    }
    if (!willInject && iTestContext) {
      willInject = canInject(parameter, InjectableParameter.ITEST_CONTEXT);
    }
    if (!willInject && iTestResult) {
      willInject = canInject(parameter, InjectableParameter.ITEST_RESULT);
    }
    if (!willInject && xmlTest) {
      willInject = canInject(parameter, InjectableParameter.XML_TEST);
    }
    return !willInject && optional;
  }

  private static boolean canInject(final Parameter parameter, final InjectableParameter injectableParameter) {
    boolean canInject = false;
    if (parameter != null && injectableParameter != null) {
      final boolean inject = !parameter.isAnnotationPresent(NoInjection.class);
      switch (injectableParameter) {
        case CURRENT_TEST_METHOD:
          final boolean isMethod = isOrExtends(Method.class, parameter.getType());
          canInject = inject && isMethod;
          break;
        case ITEST_CONTEXT:
          canInject = inject && isOrImplementsInterface(ITestContext.class, parameter.getType());
          break;
        case ITEST_RESULT:
          canInject = inject && isOrImplementsInterface(ITestResult.class, parameter.getType());
          break;
        case XML_TEST:
          canInject = inject && isOrExtends(XmlTest.class, parameter.getType());
          break;
        default:
          canInject = false;
          break;
      }
    }
    return canInject;
  }

  /**
   * @return matches or not
   * @see #matchArrayEnding(Class[], Object[])
   */
  public static boolean matchArrayEnding(final Parameter[] parameters, final Object[] param) {
    return matchArrayEnding(classesFromParameters(parameters), param);
  }

  /**
   * Matches an array of class instances to an array of instances having
   * last class instance an array.
   * <p>
   * Assuming upper case letters denote classes and corresponding low case it's instances.
   * Classes {A,B,C...}, instances {a,b,c1,c2} ==> check for {a,b,{c1,c2}} match
   * or
   * Classes {A,B,C[]}, instances {a,b,c1,c2} ==> check for {a,b,{c1,c2}} match
   * both of the above cases are equivalent.
   *
   * @param classes array of class instances to check against.
   * @param args    instances to be verified.
   * @return matches or not
   */
  public static boolean matchArrayEnding(final Class<?>[] classes, final Object[] args) {
    if (classes.length < 1) {
      return false;
    }
    if (!classes[classes.length - 1].isArray()) {
      return false;
    }
    boolean matching = true;
    int i = 0;
    if (classes.length <= args.length) {
      for (final Class<?> clazz : classes) {
        if (i >= classes.length - 1) {
          break;
        }
        matching = ReflectionRecipes.isInstanceOf(clazz, args[i]);
        i++;
        if (!matching) break;
      }
    } else {
      matching = false;
    }

    if (matching) {
      final Class<?> componentType = classes[classes.length - 1].getComponentType();
      for (; i < args.length; i++) {
        matching = ReflectionRecipes.isInstanceOf(componentType, args[i]);
        if (!matching) break;
      }
    }

    return matching;
  }

  /**
   * Extras class instances from parameters.
   *
   * @param parameters an array of parameters.
   * @return classes
   */
  public static Class<?>[] classesFromParameters(final Parameter[] parameters) {
    final Class<?>[] classes = new Class<?>[parameters.length];
    int i = 0;
    for (final Parameter parameter : parameters) {
      classes[i] = parameter.getType();
      i++;
    }
    return classes;
  }

  /**
   * Matches an array of class instances to an array of instances.
   *
   * @return matches or not
   * @see #exactMatch(Class[], Object[])
   */
  public static boolean exactMatch(final Parameter[] parameters, final Object[] args) {
    return exactMatch(classesFromParameters(parameters), args);
  }

  /**
   * Matches an array of class instances to an array of instances.
   *
   * @param classes array of class instances to check against.
   * @param args    instances to be verified.
   * @return matches or not
   */
  public static boolean exactMatch(final Class<?>[] classes, final Object[] args) {
    boolean matching = true;
    if (classes.length == args.length) {
      int i = 0;
      for (final Class<?> clazz : classes) {
        matching = ReflectionRecipes.isInstanceOf(clazz, args[i]);
        i++;
        if (!matching) break;
      }
    } else {
      matching = false;
    }
    return matching;
  }

  /**
   * Matches an array of class instances to an array of instances.
   *
   * @return matches or not
   * @see #lenientMatch(Class[], Object[])
   */
  public static boolean lenientMatch(final Parameter[] parameters, final Object[] args) {
    return lenientMatch(classesFromParameters(parameters), args);
  }

  /**
   * Matches an array of class instances to an array of instances.
   * Such that {int, boolean, float} matches {int, boolean}
   *
   * @param classes array of class instances to check against.
   * @param args    instances to be verified.
   * @return matches or not
   */
  public static boolean lenientMatch(final Class<?>[] classes, final Object[] args) {
    boolean matching = true;
    int i = 0;
    for (final Class<?> clazz : classes) {
      matching = ReflectionRecipes.isInstanceOf(clazz, args[i]);
      i++;
      if (!matching) break;
    }
    return matching;
  }

  /**
   * Check is an instance is an instance of the given class.
   *
   * @param clazz  reference class.
   * @param object instance to be tested.
   * @return is an instance-of or not
   */
  public static boolean isInstanceOf(final Class clazz, final Object object) {
    if (object == null) {
      return !clazz.isPrimitive();
    }
    boolean isInstanceOf = false;
    final boolean directInstnace = clazz.isInstance(object);
    if (!directInstnace && clazz.isPrimitive()) {
      isInstanceOf = PRIMITIVE_MAPPING.get(clazz).isInstance(object);
    } else {
      isInstanceOf = directInstnace;
    }
    return isInstanceOf;
  }

  /**
   * Omits
   * 1. org.testng.ITestContext or it's implementations from input array
   * 2. org.testng.ITestResult or it's implementations from input array
   * 3. org.testng.xml.XmlTest or it's implementations from input array
   * 4. First method
   * depending on filters.
   * <p>
   * An example would be
   * Input: {ITestContext.class, int.class, Boolean.class, TestContext.class}
   * Output: {int.class, Boolean.class}
   *
   * @param parameters array of parameter instances under question.
   * @param filters    filters to use.
   * @return Injects free array of class instances.
   */
  public static Parameter[] filter(final Parameter[] parameters, final Set<InjectableParameter> filters) {
    if (filters != null && !filters.isEmpty()) {
      final boolean currentTestMethod = filters.contains(InjectableParameter.CURRENT_TEST_METHOD);
      final boolean iTestContext = filters.contains(InjectableParameter.ITEST_CONTEXT);
      final boolean iTestResult = filters.contains(InjectableParameter.ITEST_RESULT);
      final boolean xmlTest = filters.contains(InjectableParameter.XML_TEST);
      boolean firstMethodFiltered = false;
      final List<Parameter> filterList = new ArrayList<Parameter>(parameters.length);
      for (final Parameter parameter : parameters) {
        boolean omit = false;
        if (currentTestMethod && !firstMethodFiltered) {
          if (canInject(parameter, InjectableParameter.CURRENT_TEST_METHOD)) {
            firstMethodFiltered = true;
            omit = true;
          }
        }
        if (!omit && iTestContext) {
          omit = canInject(parameter, InjectableParameter.ITEST_CONTEXT);
        }
        if (!omit && iTestResult) {
          omit = canInject(parameter, InjectableParameter.ITEST_RESULT);
        }
        if (!omit && xmlTest) {
          omit = canInject(parameter, InjectableParameter.XML_TEST);
        }

        if (!omit) {
          filterList.add(parameter);
        }
      }
      final Parameter[] filteredArray = new Parameter[filterList.size()];
      return filterList.toArray(filteredArray);
    } else {
      return parameters;
    }
  }

  /**
   * Injects appropriate arguments.
   *
   * @param parameters      array of parameter instances under question.
   * @param filters         filters to use.
   * @param args            user supplied arguments.
   * @param injectionMethod current test method.
   * @param context         current test context.
   * @param testResult      on going test results.
   * @return injected arguments.
   */
  public static Object[] inject(final Parameter[] parameters, final Set<InjectableParameter> filters,
                                final Object[] args,
                                final Method injectionMethod,
                                final ITestContext context,
                                final ITestResult testResult) {
    if (filters != null && !filters.isEmpty()) {
      final ArrayList<Object> arguments = new ArrayList<Object>(args.length);
      final Queue<ObjectHolder> queue = new ArrayDeque<ObjectHolder>(args.length);
      for (int i = 0; i < args.length; i++) {
        // to ensure fifo & hold nulls
        queue.add(new ObjectHolder(args[i]));
      }
      final boolean currentTestMethod = filters.contains(InjectableParameter.CURRENT_TEST_METHOD);
      final boolean iTestContext = filters.contains(InjectableParameter.ITEST_CONTEXT);
      final boolean iTestResult = filters.contains(InjectableParameter.ITEST_RESULT);
      final boolean xmlTest = filters.contains(InjectableParameter.XML_TEST);
      boolean firstMethodInjected = false;
      for (final Parameter parameter : parameters) {
        boolean injected = false;
        if (currentTestMethod && !firstMethodInjected) {
          if (canInject(parameter, InjectableParameter.CURRENT_TEST_METHOD)) {
            arguments.add(injectionMethod);
            firstMethodInjected = true;
            injected = true;
          }
        }

        if (!injected && iTestContext) {
          if (canInject(parameter, InjectableParameter.ITEST_CONTEXT)) {
            arguments.add(context);
            injected = true;
          }
        }
        if (!injected && iTestResult) {
          if (canInject(parameter, InjectableParameter.ITEST_RESULT)) {
            arguments.add(testResult);
            injected = true;
          }
        }
        if (!injected && xmlTest) {
          if (canInject(parameter, InjectableParameter.XML_TEST)) {
            arguments.add(context != null ? context.getCurrentXmlTest() : null);
            injected = true;
          }
        }

        if (!injected) {
          arguments.add(queue.poll().get());
        }
      }
      final Object[] injectedArray = new Object[arguments.size()];
      return arguments.toArray(injectedArray);
    } else {
      return args;
    }
  }

  /**
   * Checks a class instance for being the given interface or it's implementation.
   *
   * @param reference reference interface instance.
   * @param clazz     class instance to be tested.
   * @return would an instance of 'clazz' be an instance of reference interface.
   */
  public static boolean isOrImplementsInterface(final Class<?> reference, final Class<?> clazz) {
    boolean implementsInterface = false;
    if (reference.isInterface()) {
      if (reference.equals(clazz)) {
        implementsInterface = true;
      } else {
        final Class<?>[] interfaces = clazz.getInterfaces();
        for (final Class<?> interfaceClazz : interfaces) {
          implementsInterface = interfaceClazz.equals(reference);
          if (implementsInterface) break;
        }
      }
    }
    return implementsInterface;
  }

  /**
   * Checks a class instance for being the given class or it's sub-class.
   *
   * @param reference reference class instance.
   * @param clazz     class instance to be tested.
   * @return would an instance of 'clazz' be an instance of reference class.
   */
  public static boolean isOrExtends(final Class<?> reference, final Class<?> clazz) {
    boolean extendsGiven = false;
    if (clazz != null) {
      if (!reference.isInterface()) {
        if (reference.equals(clazz)) {
          extendsGiven = true;
        } else {
          extendsGiven = isOrExtends(reference, clazz.getSuperclass());
        }
      }
    }
    return extendsGiven;
  }

  public static class UniqueIndices implements Comparable<UniqueIndices> {
    final Set<Integer> indices = new TreeSet<Integer>();

    public boolean add(final int index) {
      return indices.add(index);
    }

    public boolean addAll(final Collection<Integer> collection) {
      return indices.addAll(collection);
    }

    public int size() {
      return indices.size();
    }

    public Iterator<Integer> iterator() {
      return indices.iterator();
    }

    @Override
    public int compareTo(final UniqueIndices other) {
      int compareTo = 0;
      if (other != null) {
        compareTo = Integer.compare(this.size(), other.size());
        if (compareTo == 0) {
          final Iterator<Integer> thisIterator = this.iterator();
          final Iterator<Integer> otherIterator = other.iterator();
          while (thisIterator.hasNext() && otherIterator.hasNext()) {
            final Integer thisNext = thisIterator.next();
            final Integer otherNext = otherIterator.next();
            if (thisNext == null && otherNext == null) {
              compareTo = 0;
            } else if (thisNext != null && otherNext == null) {
              compareTo = -1;
            } /*thisNext == null && otherNext != null*/ else if (thisNext == null) {
              compareTo = 1;
            } else {
              compareTo = thisNext.compareTo(otherNext);
            }
            if (compareTo != 0) {
              break;
            }
          }
        }
      } else {
        compareTo = -1;
      }
      return compareTo;
    }
  }

  private static class ObjectHolder {
    private final Object object;

    public ObjectHolder(Object object) {
      this.object = object;
    }

    public Object get() {
      return object;
    }
  }
}
