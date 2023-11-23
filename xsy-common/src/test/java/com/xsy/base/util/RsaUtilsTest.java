package com.xsy.base.util;

import java.security.KeyPair;

public class RsaUtilsTest {
	public static void main(String[] args) {
		KeyPair keyPair = RsaUtils.genKeyPair();
		String publicKeyToString = RsaUtils.getPublicKeyToString(keyPair);
		String privateKeyToString = RsaUtils.getPrivateKeyToString(keyPair);
		String str = "abc123啊啊啊,./";
//		String str= "1222222222211111131111111333333332111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
		String abc = RsaUtils.encrypt(str, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9AEjquX3o/HGHC6Rvrdr7jpDX\n" +
				"XAWxkNIVHsGw4LI42pYQfOYHQjrA9zHpYPlspxE4Lc+BzDpQvIUuLHBtx9kI+OMo\n" +
				"pzAzHpKwD6FKwWcrLgV2Vznw+AIpnAi/ibqT7Q56FKq4lzxYI30p/LERhXviVpGr\n" +
				"W7nnwEt5vPg5rYoSVwIDAQAB");

		String decrypt = RsaUtils.decrypt(abc, "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAL0ASOq5fej8cYcL\n" +
				"pG+t2vuOkNdcBbGQ0hUewbDgsjjalhB85gdCOsD3Melg+WynETgtz4HMOlC8hS4s\n" +
				"cG3H2Qj44yinMDMekrAPoUrBZysuBXZXOfD4AimcCL+JupPtDnoUqriXPFgjfSn8\n" +
				"sRGFe+JWkatbuefAS3m8+DmtihJXAgMBAAECgYEAiLXlCIxGDDpWMDX2qqWaKa4V\n" +
				"DQJOSAOWQpqtxjCyGbfbJnABv6xjbWMhIkv3/2TVasqQN7YvVGkY+K5CYNZXIQqR\n" +
				"DjP46aJhdcw/CHmLexC6K4O27axJwPcsLm5/xM9FVoUgZnqyuJL4EbKfCRu/5bdF\n" +
				"AfYa9U6TNz5aFE4+3JkCQQD5dRTqehYRAGXOg3f4hpWYMOURTZMZcT/YodEuT5I2\n" +
				"7IcDm3XhbtTBM1Qcp3T9fci9ZyDsBJgerelU+66KnwMjAkEAwfVI9kydqLGdt5l0\n" +
				"D40Ii5GMe9T0U0Ti/0TK7XkEy3fimqXyUjcaco5UNJdQ7FMBiKXyU4OLmTW3wUMY\n" +
				"M1sRPQJBAJhgTEZuDMyV0Zoz9DR7ASXVuBa5rAD+jGFndr8zG70w/cfWTktFx++7\n" +
				"ysU2BOBS1Svcst94bvNOiBjBI/zI5MECQQC5LiHiHhYsmJ/3Sgh4THPLJUSWeiRn\n" +
				"WA7OH3ULs45zYNu2QRgRjNCwbNlSeOHnL/z35VBukdp3hcUSPKffaGQVAkB76sva\n" +
				"g2A4YX9msF4btu7R2cghfwHD6Wmumgbr+XWwlOKOoQ6u4mW/wlWNNEk6V3Z/cMnh\n" +
				"VcA++fjp3HNfFP1h");
	}

}