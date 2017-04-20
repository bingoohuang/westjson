package com.github.bingoohuang.westjson.impl;

import com.github.bingoohuang.westjson.utils.StrBuilder;

import static com.github.bingoohuang.westjson.utils.WestJsonUtils.*;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonQuoter {
    private String json;
    private StrBuilder res;
    private int ii;

    int i = 0;
    int quoted = -1;
    char prevCh, currCh, nextCh;

    public void init(String json) {
        this.json = json;
        this.ii = json.length();
        this.res = new StrBuilder((int) (json.length() * 1.5));
    }

    public String quote(String json) {
        init(json);

        for (; i < ii; ++i) {
            prevCh = i == 0 ? ' ' : json.charAt(i - 1);
            currCh = json.charAt(i);
            nextCh = i + 1 < ii ? json.charAt(i + 1) : ' ';

            if (processEscape()) continue;

            if (quoted < 0) {
                if (processLeftBrace()) continue;
                if (processColon()) continue;
                if (processComma()) continue;
                if (processRightBrace()) continue;
            } else {
                if (processQuote() && i > quoted + 1) {
                    quoted = -1;
                    continue;
                }
            }

            res.p(currCh);
        }

        return res.toString();
    }

    private boolean processQuote() {
        return currCh == '\"';
    }

    private boolean processEscape() {
        if (currCh != '\\') return false;

        ++i;
        if (!isMeta(nextCh)) res.p(currCh);
        res.p(nextCh);
        return true;
    }

    private boolean processLeftBrace() {
        if (!isLBoundary(currCh)) return false;

        res.p(currCh);
        quoted = nextCh == '"' ? i : -1;
        if (quoted < 0 && !isBoundary(nextCh)) res.p('"');
        return true;
    }

    private boolean processColon() {
        if (currCh != ':') return false;

        if (!isBoundary(prevCh)) res.p('"');
        res.p(currCh);
        quoted = nextCh == '"' ? i : -1;
        if (quoted < 0 && !isLBoundary(nextCh) && !isRKey(json, i, ii)) res.p('"');

        return true;
    }

    private boolean processComma() {
        if (currCh != ',') return false;

        if (!isRBoundary(prevCh) && !isLKey(json, i, ii)) res.p('"');
        res.p(currCh);
        quoted = nextCh == '"' ? i : -1;
        if (quoted < 0 && !isLBoundary(nextCh) && !isRKey(json, i, ii)) res.p('"');

        return true;
    }


    private boolean processRightBrace() {
        if (!isRBoundary(currCh)) return false;

        if (quoted < 0 && !isBoundary(prevCh) && !isLKey(json, i, ii)) res.p('"');
        res.p(currCh);
        quoted = -1;
        return true;
    }

}
