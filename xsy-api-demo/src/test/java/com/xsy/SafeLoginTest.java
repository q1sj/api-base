package com.xsy;

import com.xsy.base.util.RsaUtils;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Q1sj
 * @date 2023.2.20 9:52
 */
@Slf4j
public class SafeLoginTest {

    Map<UUID, KeyPair> keyPairMap = new HashMap<>();

    public static void main(String[] args) {
        boolean login = new SafeLoginTest().login("a", "123");
        boolean login2 = new SafeLoginTest().login("a", "321");
    }

    public boolean login(String username, String password) {
        LoginPre loginPre = loginPre();
        log.info("{}", loginPre);
        String encryptPassword = RsaUtils.encrypt(password, loginPre.publicKey);
        LoginReq req = new LoginReq(loginPre.uuid, username, encryptPassword);
        log.info("{}", req);
        return validLogin(req);
    }

    public boolean validLogin(LoginReq req) {
        KeyPair keyPair = keyPairMap.remove(req.uuid);
        if (keyPair == null) {
            return false;
        }
        return "123".equals(RsaUtils.decrypt(req.encryptPassword, RsaUtils.getPrivateKeyToString(keyPair)));
    }

    private LoginPre loginPre() {
        UUID uuid = UUID.randomUUID();
        KeyPair keyPair = RsaUtils.genKeyPair();
        keyPairMap.put(uuid, keyPair);
        return new LoginPre(uuid, RsaUtils.getPublicKeyToString(keyPair));
    }


    @ToString
    @AllArgsConstructor
    static class LoginPre {
        UUID uuid;
        String publicKey;
    }

    @ToString
    @AllArgsConstructor
    static class LoginReq {
        UUID uuid;
        String username;
        String encryptPassword;
    }
}
