package edu.nitin.testnp.issue112;

/**
 * Created by nitin.verma on 12/23/15.
 */
public interface MethodMatcher {
    boolean isMatching() throws MethodMatcherException;
    Object[] getMatchingInput() throws MethodMatcherException;
}
