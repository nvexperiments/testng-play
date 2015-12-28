package edu.nitin.testng.issue112;

import java.lang.reflect.Method;

/**
 * Created by nitin.verma on 12/23/15.
 */
public class DataProviderMethodMatcher implements MethodMatcher {

    private final Method method;
    private final Object[] param;
    private final DirectMethodMatcher directMethodMatcher;
    private final ArrayEndingMethodMatcher arrayEndingMethodMatcher;

    public DataProviderMethodMatcher(final Method method, final Object[] param) {
        this.method = method;
        this.param = param;
        this.directMethodMatcher = new DirectMethodMatcher(method, param);
        this.arrayEndingMethodMatcher = new ArrayEndingMethodMatcher(method, param);
    }

    @Override
    public boolean isMatching() throws MethodMatcherException {
        return directMethodMatcher.isMatching() || arrayEndingMethodMatcher.isMatching();
    }

    @Override
    public Object[] getMatchingInput() throws MethodMatcherException {
        if (directMethodMatcher.isMatching()) {
            return directMethodMatcher.getMatchingInput();
        }
        if (arrayEndingMethodMatcher.isMatching()) {
            return arrayEndingMethodMatcher.getMatchingInput();
        }
        throw new MethodMatcherException("Not a data provider match");
    }
}
