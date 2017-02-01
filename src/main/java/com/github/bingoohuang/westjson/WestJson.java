package com.github.bingoohuang.westjson;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westjson.impl.WestJsonMinifier;
import com.github.bingoohuang.westjson.impl.WestJsonRecover;
import com.github.bingoohuang.westjson.impl.WestJsonUtils;
import lombok.val;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/1/30.
 */
public class WestJson {
    public String json(Object bean) {
        String jsonStr = JSON.toJSONString(bean);
        return new WestJsonMinifier(jsonStr).minify();
    }


    public <T> T parse(String json, Class<T> tClass) {
        String original = new WestJsonRecover(json).recover();
        return JSON.parseObject(original, tClass);
    }
}
