package com.cwnu.monitor;

/**
 * Created by rey on 2018/4/9.
 */

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar。
 *
 */
public class RSAUtils {
    public static final String WEB_MODULUS = "abcb0c959f370bcaae9dff448826ccec64616debac81b220d3302296c1f7cc845b83c449c0ae35bfe49869d0924466de5dfdf416998516e95bd4ec0fdfb825eb3c1b2864dad0786f35675f5cf63fbea271cb1079330ef84dab8941d140aa57f93c853e0518051e5751e78512667d8047b82e7e3adae0bf3dcb95c6a7852763db";
    public static final String WEB_PRIVATEEXPONENT = "528eb47fd3ab3348beb8c0d44f94dc380bb916266e190c321b6927fa1f8d60dda273c389398db0eb3c3cb1ac2ec049e6247cd6b70e158d200c6ef8bc42110c5d103cd5e5c51ce265f20dbd967231a505b5f179701e7b5e5e6d59530a9f291464c3101a18e7755fc9dfbefc091c52e51528d47a7b87a6f2349fb8c2a9ffc5f4d9";

    /**
     * * 生成密钥对
     * @return KeyPair
     * @throws Exception
     */
    public static KeyPair generateKeyPair(File cfgFile) throws Exception {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            // 没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
            final int KEY_SIZE = 1024;
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            saveKeyPair(keyPair, cfgFile);
            return keyPair;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * getKeyPair
     * @param cfgFile
     * @return
     * @throws Exception
     */
    public static KeyPair getKeyPair(File cfgFile) throws Exception {
        FileInputStream fis = new FileInputStream(cfgFile);
        ObjectInputStream oos = new ObjectInputStream(fis);
        KeyPair kp = (KeyPair) oos.readObject();
        oos.close();
        fis.close();
        return kp;
    }

    /**
     * saveKeyPair
     * @param kp
     * @param cfgFile
     * @throws Exception
     */
    public static void saveKeyPair(KeyPair kp, File cfgFile) throws Exception {
        FileOutputStream fos = new FileOutputStream(cfgFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        //生成密钥
        oos.writeObject(kp);
        oos.close();
        fos.close();
    }

    /**
     * * 生成公钥
     * @param modulus
     * @param publicExponent
     * @return RSAPublicKey
     * @throws Exception
     */
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws Exception {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.getMessage());
        }
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * * 生成私钥
     * @param modulus
     * @param privateExponent
     * @return RSAPrivateKey
     * @throws Exception
     */
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws Exception {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.getMessage());
        }

        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * * 加密
     * @param pk 加密的密钥
     * @param data 待加密的明文数据
     * @return 加密后的数据
     * @throws Exception
     */
    public static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
            // 加密块大小为127
            // byte,加密后为128个byte;因此共有2个加密块，第一个127
            // byte第二个为1个byte
            int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
                }
                // 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
                // ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
                // OutputSize所以只好用dofinal方法。
                i++;
            }
            return raw;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 解密
     * @param pk 解密的密钥
     * @param raw 已经加密的数据
     * @return 解密后的明文
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public static byte[] decrypt(PrivateKey pk, byte[] raw) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, pk);
            int blockSize = cipher.getBlockSize();
            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
            int j = 0;

            while (raw.length - j * blockSize > 0) {
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
                j++;
            }
            return bout.toByteArray();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * hexStringToBytes
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (null == hexString || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String result = "3bcf5f398f5017b66d3d8df2db3d88c9be06f8397b28b6f3b6e430d5abd84b3644d601b477c4292b53d4c28f17b9864803bdd8c93a2c2a4e9b277f89c5c648de96da9cc91d1cdab3e3adcdb837f0f0fbd6be8b6c2e36d735f8aa1674439b11ed3520303cef5c2c886363c63db70b844c95c8284b12bdb48b43ade84b707627a2";
        System.out.println("原文加密后为：");
        System.out.println(result);
        //new BigInteger(result, 16).toByteArray();
        byte[] en_result = hexStringToBytes(result);
        String modulus = WEB_MODULUS;
        String privateExponent = WEB_PRIVATEEXPONENT;
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) RSAUtils.generateRSAPrivateKey(new BigInteger(modulus, 16).toByteArray(), new BigInteger(privateExponent, 16).toByteArray());
        byte[] de_result = RSAUtils.decrypt(rsaPrivateKey, en_result);
        System.out.println("还原密文：");
        StringBuffer sb = new StringBuffer();
        sb.append(new String(de_result));
        System.out.println(sb.reverse().toString().substring(13));
    }
}
