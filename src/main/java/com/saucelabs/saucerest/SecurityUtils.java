package com.saucelabs.common;

import java.security.Key;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

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
            throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }

    protected static String byteArrayToHex(byte [] bytes) {
        int hn, ln, cx;
        String hexDigitChars = "0123456789abcdef";
        StringBuffer buf = new StringBuffer(a.length * 2);
        for(cx = 0; cx < bytes.length; cx++) {
            hn = ((int)(bytes[cx]) & 0x00ff) / 16;
            ln = ((int)(bytes[cx]) & 0x000f);
            buf.append(hexDigitChars.charAt(hn));
            buf.append(hexDigitChars.charAt(ln));
        }
        return buf.toString();
    }
}
