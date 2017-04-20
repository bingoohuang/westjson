package com.github.bingoohuang.westjson.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.val;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonUncompacter {
    public JSON uncompact(JSON object) {
        JSON result = object;
        if (object instanceof JSONObject) {
            result = uncompact((JSONObject) object);
        } else if (object instanceof JSONArray) {
            result = uncompact((JSONArray) object);
        }

        return result;
    }

    private JSON uncompact(JSONObject object) {
        if (isCompactedArrayFormat(object)) {
            return uncompactArrayFormat(object);
        }

        val uncompacted = new JSONObject();
        for (val item : object.entrySet()) {
            Object value = item.getValue();

            Object val = value instanceof JSON
                    ? uncompact((JSON) value)
                    : value;
            uncompacted.put(item.getKey(), val);
        }

        return uncompacted;
    }

    private JSONArray uncompact(JSONArray arr) {
        val uncompacted = new JSONArray(arr.size());
        for (Object item : arr) {
            Object obj = item instanceof JSON
                    ? uncompact((JSON) item)
                    : item;
            uncompacted.add(obj);
        }

        return uncompacted;
    }

    private boolean isCompactedArrayFormat(JSONObject object) {
        return object.size() == 2
                && object.containsKey("_h")
                && object.containsKey("_d");
    }

    private JSONArray uncompactArrayFormat(JSONObject jsonValue) {
        val head = jsonValue.getJSONArray("_h");
        val data = jsonValue.getJSONArray("_d");
        int arraySize = data.size() / head.size();
        val uncompacted = new JSONArray(arraySize);
        for (int i = 0; i < arraySize; i++) {
            val item = new JSONObject(head.size());
            uncompacted.add(item);

            for (int j = 0, jj = head.size(); j < jj; ++j)
                item.put(head.getString(j), data.get(i * jj + j));
        }

        return uncompacted;
    }

}
