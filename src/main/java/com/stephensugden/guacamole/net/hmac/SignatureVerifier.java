package com.stephensugden.guacamole.net.hmac;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SignatureVerifier {
    private final SecretKeySpec secretKey;

    private Logger logger = LoggerFactory.getLogger(SignatureVerifier.class);

    public SignatureVerifier(String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
    }

    public boolean verifySignature(String signature, String message) {
        try {
            Mac mac = createMac();
            String expected = Base64.encode(mac.doFinal(message.getBytes()));
            logger.info("The expected Signature is: " + expected);
            logger.info("Recieved signature: " + signature);
            return signature.equals(expected);
        } catch (InvalidKeyException e) {
            logger.warn("InvalidKeyException in SignatureVerifier");
            return false;
        } catch (NoSuchAlgorithmException e) {
            logger.warn("NoSuchAlgorithmException in SignatureVerifier");
            return false;
        }
    }

    Mac createMac() throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        return mac;
    }
}
