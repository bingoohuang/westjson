package com.github.bingoohuang.westjson;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westjson.impl.WestJsonCompacter;
import com.github.bingoohuang.westjson.impl.WestJsonThiner;
import com.github.bingoohuang.westjson.impl.WestJsonUnquoter;

import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/1/30.
 */
public class WestJson {
    private WestJsonThiner thiner;
    private WestJsonCompacter compacter;
    private WestJsonUnquoter unquoter = new WestJsonUnquoter();

    public WestJson unquoted(boolean unquoted) {
        this.unquoter = unquoted ? new WestJsonUnquoter() : null;
        return this;
    }

    public WestJson thin(boolean thin) {
        this.thiner = thin ? new WestJsonThiner() : null;
        return this;
    }

    public Map<String, String> getKeyMapping() {
        return thiner != null ? thiner.getKeyMapping() : null;
    }

    public Map<String, String> getValueMapping() {
        return thiner != null ? thiner.getValueMapping() : null;
    }

    /**
     * 启用浓缩模式。
     * 比如[{c:d1,d:1},{c:d2,d:2},{c:d3,d:3}]
     * 浓缩{_h:[c,d],_d:[d1,1,d2,2,d3,3]}
     *
     * @param compact should compact or not
     * @return The WestJson itself
     */
    public WestJson compact(boolean compact) {
        this.compacter = compact ? new WestJsonCompacter() : null;
        return this;
    }

    public String json(String json) {
        Object object = JSON.parse(json);
        return json(object);
    }

    public String json(Object bean) {
        String str = makeThin(bean);
        String compacted = makeCompact(str);

        return makeUnquote(compacted);
    }

    private String makeUnquote(String str) {
        return unquoter != null ? unquoter.unquote(str) : str;
    }

    private String makeThin(Object bean) {
        return thiner != null ? thiner.thin(bean) : toJSONString(bean);
    }

    private String makeCompact(String str) {
        if (compacter == null) return str;

        JSON json = (JSON) JSON.parse(str);
        return toJSONString(compacter.compact(json));
    }


}
