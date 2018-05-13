package cn.nicolite.huthelper.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * Created by nicolite on 17-10-8.
 */

public class EncryptUtils {
    /**
     * SHA1 安全加密算法
     */
    public static String SHA1(String env) {
        try {
            //指定sha1算法
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(env.getBytes());
            //获取字节数组
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (byte aMessageDigest : messageDigest) {
                String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString().toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BASE64 加密
     *
     * @param env
     * @return
     */
    public static String BASE64(String env) {
        try {
            byte[] encode = env.getBytes("UTF-8");
            // base64 加密
            return new String(Base64.encode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * MD5加码 生成32位md5码
     */
    public static String MD5(String env) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(env.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String md5Hex = Integer.toHexString(aMessageDigest & 0xFF);
                if (md5Hex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(md5Hex);
            }
            return hexString.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
