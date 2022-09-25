package top.ysqorz.forum.service;

import java.security.KeyPair;

/**
 * @author passerbyYSQ
 * @create 2022-09-25 16:42
 */
public interface RSAService {
    KeyPair generateKeyPair();

    String getPublicKey();

    String getPrivateKey();

    String decryptByPrivateKey(String encryptedText);

    String encryptByPublicKey(String rawText);
}
