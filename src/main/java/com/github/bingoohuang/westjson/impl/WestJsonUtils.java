package com.github.bingoohuang.westjson.impl;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public abstract class WestJsonUtils {
    public static boolean isJsonMetaChar(char ch) {
        return ch == ',' || ch == ':'
                || ch == '{' || ch == '}'
                || ch == '[' || ch == ']';
    }
}
