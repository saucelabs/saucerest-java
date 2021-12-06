package com.saucelabs.saucerest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Class providing a method to create a shareable test results link of a test executed on Sauce Labs.
 */
public class SauceShareableLink {
    /**
     * Based on the code from here: https://docs.saucelabs.com/test-results/sharing-test-results/index.html#example---java
     * @param username Sauce Labs username
     * @param accessKey Sauce Labs access key
     * @param sauceJobId Sauce Labs job id
     * @param server Sauce Labs data center endpoint
     * @return A url of the test result with an authentication token, so it can be accessed without Sauce Labs credentials.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String getShareableLink(String username, String accessKey, String sauceJobId, String server) throws NoSuchAlgorithmException, InvalidKeyException {
        String key = String.format("%s:%s", username , accessKey);
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(US_ASCII), "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(sks);
        byte[] result = mac.doFinal(sauceJobId.getBytes(US_ASCII));
        StringBuilder hash = new StringBuilder();
        for (byte b : result) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hash.append('0');
            }
            hash.append(hex);
        }
        String digest = hash.toString();
        return String.format("%s%s/%s?auth=%s", server, "tests", sauceJobId, digest);
    }
}
