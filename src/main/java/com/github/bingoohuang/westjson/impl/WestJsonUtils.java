package com.github.bingoohuang.westjson.impl;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public abstract class WestJsonUtils {
    public static boolean isMeta(char ch) {
        return ch == ',' || ch == ':'
                || ch == '{' || ch == '}'
                || ch == '[' || ch == ']';
    }

    public static boolean isBoundary(char p) {
        return p == '}' || p == ']' || p == '{' || p == '[';
    }

    public static boolean isRKey(String json, int i, int ii) {
        return isTrue(json, i, ii)
                || isFalse(json, i, ii)
                || isNull(json, i, ii);
    }

    public static boolean isLKey(String json, int i, int ii) {
        return i > 4 && isTrue(json, i - 5, ii)
                || i > 5 && isFalse(json, i - 6, ii)
                || i > 4 && isNull(json, i - 5, ii);
    }

    public static boolean isTrue(String json, int i, int ii) {
        return ii - i > 4 && json.charAt(i + 1) == 't'
                && json.charAt(i + 2) == 'r'
                && json.charAt(i + 3) == 'u'
                && json.charAt(i + 4) == 'e';
    }

    public static boolean isFalse(String json, int i, int ii) {
        return ii - i > 5 && json.charAt(i + 1) == 'f'
                && json.charAt(i + 2) == 'a'
                && json.charAt(i + 3) == 'l'
                && json.charAt(i + 4) == 's'
                && json.charAt(i + 5) == 'e';
    }

    public static boolean isNull(String json, int i, int ii) {
        return ii - i > 4 && json.charAt(i + 1) == 'n'
                && json.charAt(i + 2) == 'u'
                && json.charAt(i + 3) == 'l'
                && json.charAt(i + 4) == 'l';
    }
}
