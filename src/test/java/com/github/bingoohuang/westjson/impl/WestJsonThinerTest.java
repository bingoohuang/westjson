package com.github.bingoohuang.westjson.impl;

import com.alibaba.fastjson.JSON;
import lombok.val;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/3.
 */
public class WestJsonThinerTest {
    @Test
    public void test() {
        val json = "{\"busiorder\":\"BUSI2096\",\"record\":[{\"calldate\":\"20130301\",\"calllonghour\":\"142\",\"calltime\":\"204558\",\"calltype\":\"1\",\"cellid\":\"180\",\"deratefee\":\"\",\"homearea\":\"010\",\"homenum\":\"01068054359\",\"landfee\":\"0.00\"},{\"calldate\":\"20130301\",\"calllonghour\":\"18\",\"calltime\":\"200318\",\"calltype\":\"1\",\"cellid\":\"60\",\"deratefee\":\"\",\"homearea\":\"010\",\"homenum\":null,\"landfee\":\"0.00\"}]}";
        WestJsonThiner thiner = new WestJsonThiner();
        JSON raw = (JSON) JSON.parse(json);
        String thin = thiner.thin(raw);
        JSON parse = (JSON) JSON.parse(thin);
        JSON unthin = new WestJsonUnthiner(thiner.getKeyMapping(), thiner.getValueMapping()).unthin(parse);
        assertThat(unthin).isEqualTo(raw);
    }
}
