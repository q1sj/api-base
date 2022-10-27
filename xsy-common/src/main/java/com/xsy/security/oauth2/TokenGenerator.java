/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.oauth2;


import com.xsy.base.util.Base64Utils;
import lombok.extern.slf4j.Slf4j;

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

    private static final String KEY_SEED = "xsy";

    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private static final Key KEY;

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

    /**
     * 加密对象
     */
    private static final Cipher ENCRYPT_CIPHER;
    /**
     * 解密对象
     */
    private static final Cipher DECRYPT_CIPHER;

    static {
        try {
            ENCRYPT_CIPHER = Cipher.getInstance(TRANSFORMATION);
            ENCRYPT_CIPHER.init(Cipher.ENCRYPT_MODE, KEY);
            DECRYPT_CIPHER = Cipher.getInstance(TRANSFORMATION);
            DECRYPT_CIPHER.init(Cipher.DECRYPT_MODE, KEY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成token
     *
     * @return
     */
    public static String generateValue() {
        return encrypt(KEY_SEED + UUID.randomUUID().toString());
    }

    /**
     * 校验token是否合法,解密后{@link #KEY_SEED}开头为合法token
     *
     * @param token
     * @return
     */
    public static boolean validToken(String token) {
        String decrypt;
        try {
            decrypt = decrypt(token);
        } catch (Exception e) {
            log.warn("token:{}解密失败 {}",token,e.getMessage());
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
    private static String encrypt(String plainText) {
        try {
            byte[] p = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] result = ENCRYPT_CIPHER.doFinal(p);
            return Base64Utils.encodeToUrlSafeString(result);
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
    private static String decrypt(String cipherText) throws Exception {
        byte[] c = Base64Utils.decodeFromUrlSafeString(cipherText);
        byte[] result = DECRYPT_CIPHER.doFinal(c);
        return new String(result, StandardCharsets.UTF_8);
    }
}
