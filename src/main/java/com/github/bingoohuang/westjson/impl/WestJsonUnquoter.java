package com.github.bingoohuang.westjson.impl;

import com.github.bingoohuang.westjson.utils.StrBuilder;

import static com.github.bingoohuang.westjson.utils.WestJsonUtils.isMeta;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonUnquoter {
    private String json;
    private StrBuilder minified;
    private int ii;

    private boolean inQuote = false;
    private StrBuilder sub = new StrBuilder();
    private int escapePos = -1;
    private int escapeTimes = 0;
    private StrBuilder curr;
    private int i = 0;

    public String unquote(String json) {
        this.json = json;
        this.ii = json.length();
        this.minified = new StrBuilder(json.length());

        curr = minified;
        for (; i < ii; ++i) {
            char ch = json.charAt(i);
            if (processEsacpe(ch)) continue;
            if (processQuote(ch)) continue;
            if (processQuotedMeta(ch)) continue;

            curr.p(ch);
        }

        return minified.toString();
    }

    private boolean processEsacpe(char ch) {
        if (ch != '\\') return false;

        curr.p(ch).p(json.charAt(++i));
        return true;
    }

    private boolean processQuote(char ch) {
        if (!(ch == '"' || ch == '\'')) return false;

        if (inQuote) {
            minified.p(sub);
            if (escapeTimes >= 2) minified.p('"');
        } else {
            sub.empty();
            escapeTimes = 0;
        }

        inQuote = !inQuote;
        curr = inQuote ? sub : minified;
        return true;
    }

    private boolean processQuotedMeta(char ch) {
        if (!(inQuote && isMeta(ch))) return false;

        ++escapeTimes;
        if (escapeTimes == 1) {
            sub.p('\\').p(ch);
            escapePos = sub.len() - 2;
        } else if (escapeTimes == 2) {
            minified.p('"');
            sub.deleteCharAt(escapePos);
            sub.p(ch);
        } else {
            sub.p(ch);
        }

        return true;
    }
}
