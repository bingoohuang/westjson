package com.github.bingoohuang.westjson;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class MapTest {
    @Test
    public void simple() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "bingoo");
        map.put("age", 123);
        String json = new WestJson().json(map);
        String expected = "{name:bingoo,age:123}";
        assertThat(json).isEqualTo(expected);
        Map<String, String> mapParsed = new WestJson().parse(expected, Map.class);
        assertThat(mapParsed).isEqualTo(
                of("name", "bingoo",
                        "age", "123"));
    }

    @Test
    public void nested() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "bingoo");
        Map<String, Object> nested = new HashMap<String, Object>();
        map.put("nested", nested);
        nested.put("age", 123);
        nested.put("sth", "hello");

        String json = new WestJson().json(map);
        String expected = "{name:bingoo,nested:{sth:hello,age:123}}";
        assertThat(json).isEqualTo(expected);
        Map<String, Object> mapParsed = new WestJson().parse(expected, Map.class);
        assertThat(mapParsed).isEqualTo(
                of("name", "bingoo",
                        "nested", of("age", "123", "sth", "hello")));
    }
}
