package com.saucelabs.saucerest;

/**
 * Created by gavinmogan on 11/2/15.
 */
public class SauceException extends RuntimeException {
    /**
     * Created by gavinmogan on 11/2/15.
     */
    public static class NotAuthorized extends SauceException { }
    public static class TooManyRequests extends SauceException { }
}
