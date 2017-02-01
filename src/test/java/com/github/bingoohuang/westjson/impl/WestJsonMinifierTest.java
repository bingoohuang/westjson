package com.github.bingoohuang.westjson.impl;

import lombok.val;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonMinifierTest {
    @Test
    public void testNull() {
        val json = "{\"name\":null}";
        String minify = new WestJsonMinifier(json).minify();
        assertThat(minify).isEqualTo("{name:null}");
    }

    @Test
    public void testEmpty() {
        val json = "{\"name\":\"\"}";
        String minify = new WestJsonMinifier(json).minify();
        assertThat(minify).isEqualTo("{name:}");
    }

    @Test
    public void testEmpty2() {
        val json = "{\"name\":\"\",age:32}";
        String minify = new WestJsonMinifier(json).minify();
        assertThat(minify).isEqualTo("{name:,age:32}");
    }
}
