package com.rzico.util;

import com.google.common.base.Joiner;

import java.text.MessageFormat;

/**
 * 生成Key
 *
 * @author gstripe@gmail.com
 */
public class Keys {

    public static final char REDIS_KEY_SEPARATOR = ':';

    /**
     * 生成Redis Key
     * @param parts
     * @return
     */
    public static String redis(String ... parts) {
        return Joiner.on(REDIS_KEY_SEPARATOR).skipNulls().join(parts);
    }

    /**
     * String messageFormat ="lexical error at position {0}, encountered {1}, expected {2}"
     * @param messageFormat
     * @param parts
     * @return
     */
    public static String format(String messageFormat, Object ... parts) {
        return MessageFormat.format(messageFormat, parts);
    }

    public static void main(String[] args) {
        System.out.println(redis("test", "x", "a", "b"));
    }

}
