package edu.nitin.testnp.issue112;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nitin.verma on 12/22/15.
 */
public class Main {
    public static void main(final String[] arg)
            throws MethodMatcherException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = "method1";
        if (arg.length > 0) {
            methodName = arg[0];
        }
        Method method = null;
        for (final Method m : Main.class.getMethods()) {
            if (m.getName().equals(methodName)) {
                method = m;
            }
        }

        if (method == null) {
            throw new NoSuchMethodException();
        }

        final Object[][] data = {
                {new String[]{"Hi!!"}},
                {},
                {"Yo!", new String[]{"Hi!!"}},
                new String[] {"Yo!", "Hi!!", "Bro"},
                {1, "hello", 7L},
        };

        for (final Object[] input : data) {
            final MethodMatcher methodMatcher = new DataProviderMethodMatcher(method, input);
            if (methodMatcher.isMatching()) {
                method.invoke(new Main(), methodMatcher.getMatchingInput());
            }
        }
    }

    public void method1(final String[] objects) {
        System.out.println("m1 " + objects);
    }

    public void method2(final String... objects) {
        System.out.println("m2 " + objects);
    }

    public void method3(final String s, final String[] objects) {
        System.out.println("m3 " + s + "," + objects);
    }

    public void method4(final String s, final String... objects) {
        System.out.println("m4 " + s + "," + objects);
    }

    public void method5(final Object object) {
        System.out.println("m5 " + object);
    }

    public void method6(final Integer i, final String s, final Long l) {
        System.out.println("m5 " + i + " " + s + " " + l);
    }


}
