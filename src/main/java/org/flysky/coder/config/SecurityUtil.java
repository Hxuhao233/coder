package org.flysky.coder.config;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class SecurityUtil {
    public static final String SALT="pmhdfiudfhguiodfh0864984864gfds%@$%#2068";

    public static String encrypt(String content) {
        String newContent = new SimpleHash("md5", content, ByteSource.Util.bytes(SALT),1).toHex();
        return newContent;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("123"));
    }
}
