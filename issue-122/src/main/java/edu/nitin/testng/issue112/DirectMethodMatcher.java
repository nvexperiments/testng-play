package edu.nitin.testng.issue112;

import java.lang.reflect.Method;

/**
 * Created by nitin.verma on 12/23/15.
 */
public class DirectMethodMatcher implements MethodMatcher {
    private final Method method;
    private final Object[] param;

    public DirectMethodMatcher(final Method method, final Object[] param) {
        this.method = method;
        this.param = param;
    }

    @Override
    public boolean isMatching() {
        boolean matching = true;
        final Class<?>[] classes = method.getParameterTypes();
        if (classes.length == param.length) {
            int i = 0;
            for (final Class<?> clazz : classes) {
                matching = clazz.isInstance(param[i]);
                i++;
                if (!matching) break;
            }
        } else {
            matching = false;
        }
        return matching;

    }

    @Override
    public Object[] getMatchingInput() throws MethodMatcherException {
        if (!isMatching()) throw new MethodMatcherException("Not a direct match");
        return param;
    }
}
