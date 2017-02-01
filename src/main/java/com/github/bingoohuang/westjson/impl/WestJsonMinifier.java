package com.github.bingoohuang.westjson.impl;

import static com.github.bingoohuang.westjson.impl.WestJsonUtils.isMeta;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonMinifier {
    private final String json;
    private final StringBuilder minified;
    private final int ii;

    private boolean inQuote = false;
    private StringBuilder sub = new StringBuilder();
    private int firstEscapePos = -1;
    private int escapeTimes = 0;
    private StringBuilder curr;
    private int i = 0;

    public WestJsonMinifier(String json) {
        this.json = json;
        this.ii = json.length();
        this.minified = new StringBuilder(json.length());
    }

    public String minify() {
        curr = minified;
        for (; i < ii; ++i) {
            char ch = json.charAt(i);
            if (processEsacpe(ch)) continue;
            if (processQuote(ch)) continue;
            if (processQuotedMeta(ch)) continue;

            curr.append(ch);
        }

        return minified.toString();
    }

    private boolean processEsacpe(char ch) {
        if (ch != '\\') return false;

        curr.append(ch).append(json.charAt(++i));
        return true;
    }

    private boolean processQuote(char ch) {
        if (ch != '"') return false;

        if (inQuote) {
            minified.append(sub);
            if (escapeTimes >= 2) minified.append('"');
        } else {
            sub.setLength(0);
            escapeTimes = 0;
        }
        inQuote = !inQuote;
        curr = (inQuote ? sub : minified);
        return true;
    }

    private boolean processQuotedMeta(char ch) {
        if (!(inQuote && isMeta(ch))) return false;

        if (++escapeTimes == 2) {
            minified.append('"');
            sub.deleteCharAt(firstEscapePos);
            sub.append(ch);
        } else {
            sub.append('\\').append(ch);
            firstEscapePos = sub.length() - 2;
        }

        return true;
    }
}
