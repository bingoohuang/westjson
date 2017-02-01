package com.github.bingoohuang.westjson;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westjson.impl.WestJsonQuoter;
import com.github.bingoohuang.westjson.impl.WestJsonUncompacter;
import com.github.bingoohuang.westjson.impl.WestJsonUnthiner;

import java.util.Map;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestParser {
    private WestJsonQuoter quoter = new WestJsonQuoter();
    private WestJsonUncompacter unpacter;
    private WestJsonUnthiner unthiner;

    public WestParser quoted(boolean quoted) {
        this.quoter = quoted ? new WestJsonQuoter() : null;
        return this;
    }

    public WestParser unpact(boolean unpact) {
        this.unpacter = unpact ? new WestJsonUncompacter() : null;
        return this;
    }

    public WestParser unthin(Map<String, String> keyMapping,
                             Map<String, String> valueMapping) {
        this.unthiner = new WestJsonUnthiner(keyMapping, valueMapping);
        return this;
    }

    public JSON parse(String jsonStr) {
        String quoted = quoter != null
                ? quoter.quote(jsonStr)
                : jsonStr;
        JSON json = (JSON) JSON.parse(quoted);
        JSON uncompacted = unpacter != null ? unpacter.uncompact(json) : json;
        JSON unthin = unthiner != null ? unthiner.unthin(uncompacted) : uncompacted;
        return unthin;
    }

    public <T> T parse(String jsonStr, Class<T> tClass) {
        return parse(jsonStr).toJavaObject(tClass);
    }
}
