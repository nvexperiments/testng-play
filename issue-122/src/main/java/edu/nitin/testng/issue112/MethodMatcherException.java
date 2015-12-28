package edu.nitin.testng.issue112;

/**
 * Created by nitin.verma on 12/23/15.
 */
public class MethodMatcherException extends Exception {
    public MethodMatcherException() {
    }

    public MethodMatcherException(String message) {
        super(message);
    }

    public MethodMatcherException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodMatcherException(Throwable cause) {
        super(cause);
    }
}
