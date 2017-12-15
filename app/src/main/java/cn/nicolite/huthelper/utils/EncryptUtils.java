package cn.nicolite.huthelper.utils;

import android.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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


    /**
     * RSA公钥加密
     *
     * @param env       加密数据
     * @param publicKey 公钥字符串
     * @return
     */
    public static String RSAPublicKeyEncrypt(String env, InputStream publicKey) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(publicKey));
            StringBuilder stringBuilder = new StringBuilder();
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                if (readLine.charAt(0) != '-') {
                    stringBuilder.append(readLine);
                    stringBuilder.append('\r');
                }
            }
            bufferedReader.close();
            publicKey.close();

            //从字符串中获取公钥证书
            Provider provider = new BouncyCastleProvider();
            byte[] decode = Base64.decode(stringBuilder.toString(), Base64.DEFAULT);
            KeyFactory rsa = KeyFactory.getInstance("RSA", provider);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
            RSAPublicKey key = (RSAPublicKey) rsa.generatePublic(keySpec);

            //加密后 base64转码
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(env.getBytes("utf-8"));
            byte[] encode = Base64.encode(doFinal, Base64.DEFAULT);

            return Base64.encodeToString(encode, Base64.DEFAULT);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
