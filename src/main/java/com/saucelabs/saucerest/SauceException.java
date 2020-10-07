package com.saucelabs.saucerest;
/**
 * TODO: 2020-02-27 Lets have all these take a message, yeah?
 * TODO: 2020-02-27 And also, we should make these IOExceptions, not Runtime.
 */

/**
 * Created by gavinmogan on 2015-02-11.
 */
public class SauceException extends RuntimeException {
    /**
     * Created by gavinmogan on 2015-02-11.
     * {@inheritDoc}
     */
    public SauceException(String message) {
        super(message);
    }

    /**
     * Default case.
     */
    public SauceException() {
    }

    public static class NotAuthorized extends SauceException {

        public NotAuthorized(String message) {
            super(message);
        }

        public NotAuthorized() {

        }
    }

    public static class TooManyRequests extends SauceException {
    }
}
