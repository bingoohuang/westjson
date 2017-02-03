package com.github.bingoohuang.westjson.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.google.common.base.Charsets;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public abstract class WestJsonUtils {
    public static boolean isNumeric(String str) {
        return str != null && str.length() > 0 && str.matches("[+-]?\\d*(\\.\\d+)?");
    }

    public static boolean isMeta(char ch) {
        return ch == ',' || ch == ':' || isBoundary(ch);
    }

    public static boolean isBoundary(char p) {
        return isRBoundary(p) || isLBoundary(p);
    }

    public static boolean isLBoundary(char p) {
        return p == '{' || p == '[';
    }

    public static boolean isRBoundary(char p) {
        return p == '}' || p == ']';
    }

    public static boolean isRKey(String json, int i, int ii) {
        return isRTrue(json, i, ii) && isSep(json, i, ii, 5)
                || isRFalse(json, i, ii) && isSep(json, i, ii, 6)
                || isRNull(json, i, ii) && isSep(json, i, ii, 5);
    }

    private static boolean isSep(String json, int i, int ii, int offset) {
        if (i + offset >= ii) return true;

        return isMeta(json.charAt(i + offset));
    }

    public static boolean isLKey(String json, int i, int ii) {
        return isRTrue(json, i - 5, ii) && isSep(json, i, ii, -5)
                || isRFalse(json, i - 6, ii) && isSep(json, i, ii, -6)
                || isRNull(json, i - 5, ii) && isSep(json, i, ii, -5);
    }

    public static boolean isRTrue(String json, int i, int ii) {
        return i >= 0 && ii - i > 4
                && json.charAt(i + 1) == 't'
                && json.charAt(i + 2) == 'r'
                && json.charAt(i + 3) == 'u'
                && json.charAt(i + 4) == 'e';
    }

    public static boolean isRFalse(String json, int i, int ii) {
        return i >= 0 && ii - i > 5
                && json.charAt(i + 1) == 'f'
                && json.charAt(i + 2) == 'a'
                && json.charAt(i + 3) == 'l'
                && json.charAt(i + 4) == 's'
                && json.charAt(i + 5) == 'e';
    }

    public static boolean isRNull(String json, int i, int ii) {
        return i >= 0 && ii - i > 4
                && json.charAt(i + 1) == 'n'
                && json.charAt(i + 2) == 'u'
                && json.charAt(i + 3) == 'l'
                && json.charAt(i + 4) == 'l';
    }

    public static int bytesLen(String strValue) {
        return strValue.getBytes(Charsets.UTF_8).length;
    }

    public static <K, V> Map<V, K> invert(Map<K, V> source) {
        Map<V, K> reversedMap = new HashMap<V, K>(source.size());
        for (Map.Entry<K, V> entry : source.entrySet())
            reversedMap.put(entry.getValue(), entry.getKey());

        return reversedMap;
    }

    public static JSON parseJSON(String json) {
        try {
            return (JSON) JSON.parse(json);
        } catch (Throwable e) {
            throw new RuntimeException("JSON:" + json, e);
        }
    }

    static ValueFilter filter = new ValueFilter() {
        @Override
        public Object process(Object source, String name, Object value) {
            if (value instanceof Number) return String.valueOf(value);
            return value;
        }
    };

    public static String convertNumberToString(String raw) {
        Object parse = JSON.parse(raw);
        return JSON.toJSONString(parse,
                new SerializeFilter[]{filter}, WriteMapNullValue);
    }
}
