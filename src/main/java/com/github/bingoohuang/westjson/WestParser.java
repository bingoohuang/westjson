package com.github.bingoohuang.westjson;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westjson.impl.WestJsonQuoter;
import com.github.bingoohuang.westjson.impl.WestJsonUncompacter;
import com.github.bingoohuang.westjson.impl.WestJsonUnthiner;
import com.github.bingoohuang.westjson.utils.WestJsonUtils;

import java.util.Map;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestParser {
    private WestJsonUnthiner unthiner;

    public static final int QUOTED = 0x01;
    public static final int UNCOMPACT = 0x04;

    public WestParser unthin(Map<String, String> keyMapping,
                             Map<String, String> valueMapping) {
        this.unthiner = new WestJsonUnthiner(keyMapping, valueMapping);
        return this;
    }

    public JSON parse(String jsonStr) {
        return parse(jsonStr, QUOTED);
    }

    public JSON parse(String jsonStr, int flags) {
        try {
            String quoted = (flags & QUOTED) > 0
                    ? new WestJsonQuoter().quote(jsonStr) : jsonStr;
            JSON json = WestJsonUtils.parseJSON(quoted);
            JSON uncompacted = (flags & UNCOMPACT) > 0
                    ? new WestJsonUncompacter().uncompact(json) : json;
            return unthiner != null ? unthiner.unthin(uncompacted) : uncompacted;
        } catch (Throwable e) {
            throw new RuntimeException("parse error:" + jsonStr, e);
        }
    }

    public <T> T parse(String jsonStr, Class<T> tClass) {
        return parse(jsonStr).toJavaObject(tClass);
    }
}
