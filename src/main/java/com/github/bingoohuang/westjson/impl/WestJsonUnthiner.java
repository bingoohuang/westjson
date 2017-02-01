package com.github.bingoohuang.westjson.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.val;

import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonUnthiner {
    private final Map<String, String> keyMapping;
    private final Map<String, String> valueMapping;

    public WestJsonUnthiner(Map<String, String> keyMapping,
                            Map<String, String> valueMapping) {
        this.keyMapping = keyMapping;
        this.valueMapping = valueMapping;
    }


    private JSON unthin(JSONObject origin) {
        JSONObject unmapped = new JSONObject();
        for (val item : origin.entrySet()) {
            Object value = item.getValue();
            String key = item.getKey();
            String mappedKey = keyMapping.get(key);
            mappedKey = mappedKey == null ? key : mappedKey;
            unmapped.put(mappedKey, unthin(value, true));
        }

        return unmapped;
    }

    private JSON unthin(JSONArray array) {
        JSONArray ummapped = new JSONArray(array.size());
        for (int i = 0, ii = array.size(); i < ii; ++i)
            ummapped.add(unthin(array.get(i), false));

        return ummapped;
    }

    public JSON unthin(JSON object) {
        JSON result = null;
        if (object instanceof JSONObject)
            result = unthin((JSONObject) object);
        else if (object instanceof JSONArray)
            result = unthin((JSONArray) object);

        return result;
    }

    private Object unthin(Object object, boolean isValueObject) {
        if (object instanceof JSONObject)
            return unthin((JSONObject) object);

        if (object instanceof JSONArray)
            return unthin((JSONArray) object);

        if (isValueObject && object instanceof String) {
            String strValue = (String) object;
            if (strValue.startsWith("@")) {
                String ref = strValue.substring(1);
                String refValue = valueMapping.get(ref);
                return firstNonNull(refValue, object);
            }
        }

        return object;
    }
}
