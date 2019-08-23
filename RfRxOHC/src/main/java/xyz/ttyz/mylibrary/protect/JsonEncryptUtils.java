package xyz.ttyz.mylibrary.protect;

import xyz.ttyz.mylibrary.encryption_decryption.AES;
import xyz.ttyz.mylibrary.encryption_decryption.RSA;

public class JsonEncryptUtils {
    public static final String RSAKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpCeRhdvkpRht3Gr1GPy393+CDn5t7A1pFntQut8oa+hd/b+CUopP53Yw+QoX88X/F7IlOyWLKRswPLyWbLqlFrsm6/dBtJlMTQuDMnySpkvwYsWT3q2DPeGNdPErV8dj/YzEh27oOKh/d1rbzeyn9K5GmS27MfIYQjQFi/sNQqQIDAQAB";

    /**
     * RSA加密
     */
    public static String rsaEncrypt(String content, String encryptKey) {
        try {
            return RSA.encrypt(content, encryptKey);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * AES加密
     */
    public static String aesEncrypt(String content, String aesRandom) {
        try {
            return AES.encryptToBase64(content, aesRandom);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
