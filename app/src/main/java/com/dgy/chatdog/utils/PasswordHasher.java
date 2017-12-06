package com.dgy.chatdog.utils;

/**
 * Created by DGY on 2017/5/17.
 */
import org.apache.shiro.codec.Base64;

public class PasswordHasher {

    public static String Md5Encoder(String param) {
        String s = null;
        byte[] source = param.getBytes();
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>>
                // 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * base64编码
     *
     * @param bstr
     * @return String
     */

    public static String base64Encode(String bstr) {
        return Base64.encodeToString(bstr.getBytes());
    }

    /**
     * base64解码
     *
     * @param str
     * @return string
     */
    public static byte[] base64Decode(String str) {
        byte[] bt = null;
        bt = Base64.decode(str);

        return bt;
    }

    public static String getBase64(String s) {
        if (s == null)
            return null;
        return Base64.encodeToString(s.getBytes());
    }

    // 将 BASE64 编码的字符串 s 进行解码
    public static String fromBase64(String s) {
        if (s == null)
            return null;
        return Base64.decodeToString(s.getBytes());
		/*
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}*/
    }

    public static String PasswordEncode(String str) {
        String result = str;
        result = base64Encode(result);
        // System.out.println(result);
        result = Md5Encoder(result);
        // System.out.println(result);
        return result;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(PasswordHasher.PasswordEncode("123456"));
    }


}
