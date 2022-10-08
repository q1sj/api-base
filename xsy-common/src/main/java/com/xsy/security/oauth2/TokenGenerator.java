/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.oauth2;


import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * 生成token
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
public class TokenGenerator {

    public final static String KEY_SEED = "xsy";

    public final static Key KEY;

    static {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(TokenGenerator.KEY_SEED.getBytes());
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(secureRandom);
            KEY = generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateValue() {
        return encrypt(KEY_SEED + UUID.randomUUID().toString());
    }

    public static boolean validToken(String token) {
        String decrypt = null;
        try {
            decrypt = decrypt(token);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return false;
        }
        return decrypt.startsWith(KEY_SEED);
    }

    /**
     * 根据密钥对指定的明文plainText进行加密.
     *
     * @param plainText 明文
     * @return 加密后的密文.
     */
    public static final String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, KEY);
            byte[] p = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] result = cipher.doFinal(p);
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据密钥对指定的密文cipherText进行解密.
     *
     * @param cipherText 密文
     * @return 解密后的明文.
     */
    public static String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, KEY);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] c = decoder.decodeBuffer(cipherText);
        byte[] result = cipher.doFinal(c);
        return new String(result, StandardCharsets.UTF_8);
    }
}
