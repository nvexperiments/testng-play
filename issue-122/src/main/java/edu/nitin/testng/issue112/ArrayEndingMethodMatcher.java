package edu.nitin.testng.issue112;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Created by nitin.verma on 12/23/15.
 */
public class ArrayEndingMethodMatcher implements MethodMatcher {

    private final Method method;
    private final Object[] param;

    public ArrayEndingMethodMatcher(final Method method, final Object[] param) {
        this.method = method;
        this.param = param;
    }

    @Override
    public boolean isMatching() throws MethodMatcherException {
        boolean matching = true;
        final Class<?>[] classes = method.getParameterTypes();
        if (classes.length < 1) {
            return false;
        }
        if (!classes[classes.length - 1].isArray()) {
            return false;
        }
        int i = 0;
        if (classes.length <= param.length) {

            for (final Class<?> clazz : classes) {
                if (i >= classes.length - 1) {
                    break;
                }
                matching = clazz.isInstance(param[i]);
                i++;
                if (!matching) break;
            }
        } else {
            matching = false;
        }

        if (matching) {
            final Class<?> componentType = classes[classes.length - 1].getComponentType();
            for (; i < param.length; i++) {
                matching = componentType.isInstance(param[i]);
                if (!matching) break;
            }
        }

        return matching;
    }

    @Override
    public Object[] getMatchingInput() throws MethodMatcherException {
        if (!isMatching()) throw new MethodMatcherException("Not a array ending match");
        final Class<?>[] classes = method.getParameterTypes();
        final Object[] objects = new Object[classes.length];
        final Class<?> componentType = classes[classes.length - 1].getComponentType();
        final Object array = Array.newInstance(componentType, param.length - classes.length + 1);
        System.arraycopy(param, 0, objects, 0, classes.length - 1);
        int j = 0;
        for (int i = classes.length - 1; i < param.length; i++, j++) {
            Array.set(array, j, param[i]);
        }
        objects[classes.length - 1] = array;
        return objects;
    }
}
