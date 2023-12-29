package xyz.ttyz.tourfrxohc.utils;


import java.math.BigInteger;

/**
 * @author jiangjingming
 */
public class EncrUtil {
    private static final int RADIX = 16;
    //该秘密为钥匙密码的解密秘密不可修改，不然线上报错哦
    private static final String SEED = "0933910847463829232312312";

    private static class StringUtils{
        public static boolean isBlank(String str){
            return str == null || str.isEmpty();
        }
    }
    /**
     *  加密
     * @param password
     * @return
     */
    public static final String encrypt(String password) {
        if (StringUtils.isBlank(password)) {
            return null;
        }
        BigInteger biPassword = new BigInteger(password.getBytes());
        BigInteger bi_r0 = new BigInteger(SEED);
        BigInteger bi_r1 = bi_r0.xor(biPassword);

        return bi_r1.toString(RADIX);
    }

    /**
     * 解密
     * @param encrypted
     * @return
     */
    public static final String decrypt(String encrypted) {
        if (StringUtils.isBlank(encrypted)) {
            return null;
        }

        BigInteger biConfuse = new BigInteger(SEED);

        try {
            BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
            BigInteger bi_r0 = bi_r1.xor(biConfuse);

            return new String(bi_r0.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

}

