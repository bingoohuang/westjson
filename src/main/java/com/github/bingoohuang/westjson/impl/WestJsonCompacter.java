package com.github.bingoohuang.westjson.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.val;

import java.util.TreeSet;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonCompacter {
    public JSON compact(JSON object) {
        JSON result = object;
        if (object instanceof JSONObject) {
            result = compactConvert((JSONObject) object);
        } else if (object instanceof JSONArray) {
            result = compactConvert((JSONArray) object);
        }

        return result;
    }

    private JSONObject compactConvert(JSONObject jsonObject) {
        val compactJsonObject = new JSONObject();
        for (val entry : jsonObject.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (value instanceof JSONArray) {
                value = compactConvert((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = compactConvert((JSONObject) value);
            }

            compactJsonObject.put(key, value);
        }

        return compactJsonObject;
    }

    private JSON compactConvert(JSONArray value) {
        if (value == null || value.size() == 0) return value;

        JSONObject compactObject = new JSONObject();
        JSONArray header = null;
        JSONArray data = null;
        int lineNo = 0;
        int arraySize = value.size();

        for (Object item : value) {
            ++lineNo;
            if (!(item instanceof JSONObject)) return value; // 不转换

            JSONObject objItem = (JSONObject) item;
            if (lineNo == 1) {
                header = new JSONArray();
                if (arraySize == 1) {
                    header.add(compact(objItem));
                    return header;
                }

                data = new JSONArray();
                val headSet = new TreeSet<String>();
                for (String key : objItem.keySet()) {
                    headSet.add(key);
                }

                for (String head : headSet) {
                    header.add(head);
                    data.add(objItem.get(head));
                }
                continue;
            }

            if (!hasSameColumns(header, objItem)) return value;

            for (Object key : header) {
                data.add(objItem.get(key));
            }
        }

        compactObject.put("_h", header);
        compactObject.put("_d", data);

        return compactObject;
    }

    private boolean hasSameColumns(JSONArray header, JSONObject objItem) {
        if (objItem.size() != header.size()) return false;

        for (String obj : objItem.keySet())
            if (!header.contains(obj)) return false;

        return true;
    }
}
