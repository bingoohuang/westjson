package com.github.bingoohuang.westjson;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.io.InputStream;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class DemoTest {
    @Test
    public void demo() {
        demo("demo");
        demo("form");
        demo("18610441001_201301");
    }

    public void demo(final String name) {
        String keyMapping = readClasspathFile("cdr/" + name + ".0.thin.keyMapping.json");
        String valueMapping = readClasspathFile("cdr/" + name + ".0.thin.valueMapping.json");
        String raw = readClasspathFile("cdr/" + name + ".1.raw.json");
        String unquoted = readClasspathFile("cdr/" + name + ".2.unquoted.json");
        val quoted = new WestJson().json(raw);
        assertThat(quoted).isEqualTo(unquoted);

        WestJson westJson = new WestJson();
        val thined = westJson.json(raw, WestJson.UNQUOTED | WestJson.THIN);
        assertThat(new WestJson().json(westJson.getKeyMapping())).isEqualTo(keyMapping);
        assertThat(new WestJson().json(westJson.getValueMapping())).isEqualTo(valueMapping);

        String thinExpected = readClasspathFile("cdr/" + name + ".3.unquoted.thin.json");
        assertThat(thined).isEqualTo(thinExpected);

        String compactExpected = readClasspathFile("cdr/" + name + ".4.unquoted.thin.compact.json");
        String compact = new WestJson().json(raw, WestJson.UNQUOTED | WestJson.THIN | WestJson.COMPACT);
        assertThat(compact).isEqualTo(compactExpected);

        JSON parsed = new WestParser()
                .unthin(westJson.getKeyMapping(), westJson.getValueMapping())
                .parse(compact, WestParser.QUOTED | WestParser.UNCOMPACT);
        JSON rawParsed = (JSON) JSON.parse(raw);
//        System.out.println(JSON.toJSONString(parsed, true));
//        System.out.println(JSON.toJSONString(rawParsed, true));
        assertThat(parsed).isEqualTo(rawParsed);

        JSON thinParsed = new WestParser()
                .unthin(westJson.getKeyMapping(), westJson.getValueMapping())
                .parse(thined);
        assertThat(thinParsed).isEqualTo(rawParsed);

        JSON quotedParsed = new WestParser().parse(quoted, WestParser.QUOTED | WestParser.UNCOMPACT);
        assertThat(quotedParsed).isEqualTo(rawParsed);
    }

    @SneakyThrows
    public static String readClasspathFile(String name) {
        val cl = DemoTest.class.getClassLoader();
        InputStream is = cl.getResourceAsStream(name);
        return new String(ByteStreams.toByteArray(is), Charsets.UTF_8);
    }
}
