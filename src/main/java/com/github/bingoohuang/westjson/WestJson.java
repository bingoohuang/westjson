package com.github.bingoohuang.westjson;

import com.alibaba.fastjson.JSON;
import lombok.val;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/1/30.
 */
public class WestJson {
    public String json(Object bean) {
        String jsonStr = JSON.toJSONString(bean);
        return minifyJson(jsonStr);
    }

    private String minifyJson(String json) {
        boolean inQuote = false;
        val minified = new StringBuilder(json.length());
        val sub = new StringBuilder();
        int firstEscapePos = -1;
        int escapeTimes = 0;

        for (int i = 0, ii = json.length(); i <= ii; ++i) {
            char ch = i < ii ? json.charAt(i) : '"';
            if (ch == '\\') {
                (inQuote ? sub : minified).append(ch).append(json.charAt(++i));
            } else if (ch == '"') {
                if (inQuote) {
                    minified.append(sub);
                    if (escapeTimes >= 2) minified.append('"');
                } else {
                    sub.setLength(0);
                    escapeTimes = 0;
                }
                inQuote = !inQuote;
            } else if (inQuote && (ch == ',' || ch == ':')) {
                if (++escapeTimes == 2) {
                    minified.append('"');
                    sub.deleteCharAt(firstEscapePos);
                    sub.append(ch);
                } else {
                    sub.append('\\').append(ch);
                    firstEscapePos = sub.length() - 2;
                }
            } else {
                (inQuote ? sub : minified).append(ch);
            }
        }

        return minified.toString();
    }

    public <T> T parse(String json, Class<T> tClass) {
        String original = recover(json);
        return JSON.parseObject(original, tClass);
    }

    private String recover(String json) {
        val recovered = new StringBuilder((int) (json.length() * 1.5));
        boolean quoted = false;
        for (int i = 0, ii = json.length(); i < ii; ++i) {
            char ch = json.charAt(i);
            if (ch == '\\') {
                char escapedCh = json.charAt(++i);
                if (",:".indexOf(escapedCh) < 0) recovered.append(ch);
                recovered.append(escapedCh);
            } else if (ch == '{') {
                recovered.append(ch);
                if (i + 1 < ii) quoted = json.charAt(i + 1) == '"';
                if (!quoted) recovered.append('"');
            } else if (ch == ':' || ch == ',') {
                if (quoted) {
                    recovered.append(ch);
                } else {
                    recovered.append('"');
                    recovered.append(ch);
                    if (i + 1 < ii) quoted = json.charAt(i + 1) == '"';
                    if (!quoted) recovered.append('"');
                }
            } else if (ch == '}') {
                if (!quoted) recovered.append('"');
                recovered.append(ch);
                quoted = false;
            } else {
                recovered.append(ch);
            }
        }

        return recovered.toString();
    }
}
