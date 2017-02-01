package com.github.bingoohuang.westjson.impl;

import static com.github.bingoohuang.westjson.impl.WestJsonUtils.isJsonMetaChar;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonRecover {
    private final String json;
    private final StringBuilder recovered;
    private final int ii;

    int i = 0;
    boolean quoted = false;

    public WestJsonRecover(String json) {
        this.json = json;
        this.ii = json.length();
        this.recovered = new StringBuilder((int) (json.length() * 1.5));
    }


    public String recover() {
        for (; i < ii; ++i) {
            char ch = json.charAt(i);
            if (processEscape(ch)) continue;
            if (processLeftBrace(ch)) continue;
            if (processQuote(ch)) continue;
            if (processRightBrace(ch)) continue;

            recovered.append(ch);
        }

        return recovered.toString();
    }

    private boolean processEscape(char ch) {
        if (ch != '\\') return false;

        char ech = json.charAt(++i);
        if (!isJsonMetaChar(ech)) recovered.append(ch);
        recovered.append(ech);
        return true;
    }

    private boolean processLeftBrace(char ch) {
        if (ch != '{') return false;

        recovered.append(ch);
        quoted = json.charAt(i + 1) == '"';
        if (!quoted) recovered.append('"');
        return true;
    }

    private boolean processQuote(char ch) {
        if (!(ch == ':' || ch == ',')) return false;

        if (quoted) {
            recovered.append(ch);
        } else {
            recovered.append('"');
            recovered.append(ch);
            quoted = json.charAt(i + 1) == '"';
            if (!quoted) recovered.append('"');
        }
        return true;
    }

    private boolean processRightBrace(char ch) {
        if (ch != '}') return false;

        if (!quoted) recovered.append('"');
        recovered.append(ch);
        quoted = false;
        return true;
    }
}
