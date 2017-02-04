package com.github.bingoohuang.westjson;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westjson.impl.*;
import com.github.bingoohuang.westjson.utils.WestJsonUtils;

import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/1/30.
 */
public class WestJson {
    public static final int UNQUOTED = 0x01;
    public static final int THIN = 0x02;
    /*
     * 启用浓缩模式。
     * 比如[{c:d1,d:1},{c:d2,d:2},{c:d3,d:3}]
     * 浓缩{_h:[c,d],_d:[d1,1,d2,2,d3,3]}
     *
     */
    public static final int COMPACT = 0x04;

    private WestJsonThiner thiner;

    public Map<String, String> keyMapping() {
        return thiner != null ? thiner.getKeyMapping() : null;
    }

    public Map<String, String> valueMapping() {
        return thiner != null ? thiner.getValueMapping() : null;
    }

    public String json(String json) {
        return json(json, UNQUOTED);
    }

    public String json(String json, int flags) {
        Object object = JSON.parse(json);
        return json(object, flags);
    }

    public String json(Object bean) {
        return json(bean, UNQUOTED);
    }

    public String json(Object bean, int flags) {
        String str = makeThin(bean, flags & THIN);
        String compacted = makeCompact(str, flags & COMPACT);

        return makeUnquote(compacted, flags & UNQUOTED);
    }

    private String makeUnquote(String str, int unquotedFlag) {
        if (unquotedFlag == 0) return str;

        return new WestJsonUnquoter().unquote(str);
    }

    private String makeThin(Object bean, int thinFlag) {
        this.thiner = thinFlag > 0 ? new WestJsonThiner() : null;
        return thiner != null ? thiner.thin(bean) : toJSONString(bean);
    }

    private String makeCompact(String str, int compactFlag) {
        if (compactFlag == 0) return str;

        JSON json = (JSON) JSON.parse(str);
        JSON compacted = new WestJsonCompacter().compact(json);
        return toJSONString(compacted);
    }

    private WestJsonUnthiner unthiner;

    public WestJson unthin(Map<String, String> keyMapping,
                           Map<String, String> valueMapping) {
        this.unthiner = new WestJsonUnthiner(keyMapping, valueMapping);
        return this;
    }

    public JSON parse(String jsonStr) {
        return parse(jsonStr, UNQUOTED);
    }

    public JSON parse(String jsonStr, int flags) {
        try {
            String quoted = (flags & UNQUOTED) > 0
                    ? new WestJsonQuoter().quote(jsonStr) : jsonStr;
            JSON json = WestJsonUtils.parseJSON(quoted);
            JSON uncompacted = (flags & COMPACT) > 0
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
