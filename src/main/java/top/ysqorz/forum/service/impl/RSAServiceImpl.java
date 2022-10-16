package top.ysqorz.forum.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.service.RSAService;
import top.ysqorz.forum.service.RedisService;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author passerbyYSQ
 * @create 2022-09-25 15:36
 */
@Service
@Slf4j
public class RSAServiceImpl implements RSAService, ApplicationListener<ContextRefreshedEvent> {
    @Resource
    private RedisService redisService;

    @Override
    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024, new SecureRandom());
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 对于web应用会出现父子容器，这样就会触发两次
        if (event.getApplicationContext().getParent() == null) {
            redisService.saveRSAKeyPair();
            log.info("成功将RSA的KeyPair缓存至Redis");
        }
    }

    public String getPublicKey() {
        KeyPair keyPair = redisService.getRSAKeyPair();
        byte[] publicBytes = keyPair.getPublic().getEncoded();
        byte[] base64Bytes = Base64.getEncoder().encode(publicBytes);
        return new String(base64Bytes);
    }

    public String getPrivateKey() {
        KeyPair keyPair = redisService.getRSAKeyPair();
        byte[] privateBytes = keyPair.getPrivate().getEncoded();
        byte[] base64Bytes = Base64.getEncoder().encode(privateBytes);
        return new String(base64Bytes);
    }

    public String decryptByPrivateKey(String encryptedText) {
        if (ObjectUtils.isEmpty(encryptedText)) {
            return null;
        }
        try {
            KeyPair keyPair = redisService.getRSAKeyPair();
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText.getBytes());
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encryptByPublicKey(String rawText) {
        if (ObjectUtils.isEmpty(rawText)) {
            return null;
        }
        try {
            KeyPair keyPair = redisService.getRSAKeyPair();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] encryptedBytes = cipher.doFinal(rawText.getBytes());
            byte[] base64Bytes = Base64.getEncoder().encode(encryptedBytes);
            return new String(base64Bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
