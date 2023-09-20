package com.saucelabs.saucerest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Class providing a method to create a shareable test results link of a test executed on Sauce Labs.
 */
public class SauceShareableLink {
    private SauceShareableLink() {
        throw new IllegalStateException("Utility class");
    }

    public static String getJobAuthDigest(String username, String accessKey, String sauceJobId) {
        try {
            String key = String.format("%s:%s", username, accessKey);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.US_ASCII), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(secretKeySpec);
            byte[] result = mac.doFinal(sauceJobId.getBytes(StandardCharsets.US_ASCII));
            return bytesToHex(result).toLowerCase();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Error calculating job auth digest", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(hexStringBuilder);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return hexStringBuilder.toString();
    }

    /**
     * This method will use username and access key from the environment variables SAUCE_USERNAME and SAUCE_ACCESS_KEY.
     *
     * @param sauceJobId Sauce Labs job id
     * @param dataCenter Sauce Labs data center endpoint
     * @return A url of the test result with an authentication token, so it can be accessed without Sauce Labs credentials.
     */
    public static String getShareableLink(String sauceJobId, DataCenter dataCenter) {
        return getShareableLink(null, null, sauceJobId, dataCenter);
    }

    /**
     * Based on the code from <a href="https://docs.saucelabs.com/test-results/sharing-test-results/index.html#example---java">here</a>
     *
     * @param username   Sauce Labs username
     * @param accessKey  Sauce Labs access key
     * @param sauceJobId Sauce Labs job id
     * @param dataCenter Sauce Labs data center endpoint
     * @return A url of the test result with an authentication token, so it can be accessed without Sauce Labs credentials.
     */
    public static String getShareableLink(String username, String accessKey, String sauceJobId, DataCenter dataCenter) {
        String defaultUsername = System.getenv("SAUCE_USERNAME");
        String defaultAccessKey = System.getenv("SAUCE_ACCESS_KEY");

        if (username == null || username.isEmpty()) {
            if (defaultUsername == null || defaultUsername.isEmpty()) {
                throw new IllegalArgumentException("Sauce Labs username cannot be null or empty");
            } else {
                username = defaultUsername;
            }
        }

        if (accessKey == null || accessKey.isEmpty()) {
            if (defaultAccessKey == null || defaultAccessKey.isEmpty()) {
                throw new IllegalArgumentException("Sauce Labs access key cannot be null or empty");
            } else {
                accessKey = defaultAccessKey;
            }
        }

        if (sauceJobId == null || sauceJobId.isEmpty()) {
            throw new IllegalArgumentException("Sauce Labs job ID cannot be null or empty");
        }

        if (dataCenter == null) {
            throw new IllegalArgumentException("Sauce Labs data center endpoint cannot be null");
        }

        String digest = getJobAuthDigest(username, accessKey, sauceJobId);
        return dataCenter.appServer + "tests/" + sauceJobId + "?auth=" + digest;
    }
}