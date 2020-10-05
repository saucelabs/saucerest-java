package com.saucelabs.saucerest;
/**
 *  TODO: 27/2/20 Lets have all these take a message, yeah?
 *  TODO: 27/2/20 And also, we should make these IOExceptions, not Runtime.
 */

/**
 * Created by gavinmogan on 11/2/15.
 */
public class SauceException extends RuntimeException {
    /**
     * Created by gavinmogan on 11/2/15.
     * {@inheritDoc}
     */
    public SauceException(String message) {
        super(message);
    }

    /**
     * Default case.
     */
    public SauceException() {}

    public static class NotAuthorized extends SauceException {

        public NotAuthorized(String message) {
            super(message);
        }

        public NotAuthorized(){

        }
    }
    public static class TooManyRequests extends SauceException { }
}
