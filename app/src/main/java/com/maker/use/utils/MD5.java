package com.maker.use.utils;

import java.security.MessageDigest;

/**
 * 说明：MD5处理
 */
public class MD5 {

    private static MessageDigest md;

    public static String md5(String str) {
        try {
            if (md == null) {
                md = MessageDigest.getInstance("MD5");
            }
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }
}
