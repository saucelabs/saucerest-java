package com.saucelabs.saucerest;

import java.security.Key;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

/*
 * Modelled after code from {@linkhttp://www.tomred.net/tutorials/tomred-java-generate-hmac-md5-sha1.html}
*/
public class SecurityUtils {
    public static String hmacEncode(String algorithm, String input, String privateKey)
        throws IllegalArgumentException {
        try {
            byte[] keyBytes = privateKey.getBytes();
            Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(key);
            return byteArrayToHex(mac.doFinal(input.getBytes()));
        } catch(NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        } catch(InvalidKeyException ex) {
            throw new IllegalArgumentException("Illegal key: " + privateKey);
        }
    }

    protected static String byteArrayToHex(byte [] bytes) {
        int hn, ln, cx;
        String hexDigitChars = "0123456789abcdef";
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for(cx = 0; cx < bytes.length; cx++) {
            hn = ((int)(bytes[cx]) & 0x00ff) / 16;
            ln = ((int)(bytes[cx]) & 0x000f);
            buf.append(hexDigitChars.charAt(hn));
            buf.append(hexDigitChars.charAt(ln));
        }
        return buf.toString();
    }
}
