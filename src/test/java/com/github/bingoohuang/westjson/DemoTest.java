package com.github.bingoohuang.westjson;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class DemoTest {
    @Test
    public void demo() throws IOException {
        String keyMapping = readClasspathFile("cdr/demo.0.thin.keyMapping.json");
        String valueMapping = readClasspathFile("cdr/demo.0.thin.valueMapping.json");
        String raw = readClasspathFile("cdr/demo.1.raw.json");
        String unquoted = readClasspathFile("cdr/demo.2.unquoted.json");
        val quoted = new WestJson().json(raw);
        assertThat(quoted).isEqualTo(unquoted);

        WestJson westJson = new WestJson().thin(true);
        val thined = westJson.json(raw);
        assertThat(new WestJson().json(westJson.getKeyMapping())).isEqualTo(keyMapping);
        assertThat(new WestJson().json(westJson.getValueMapping())).isEqualTo(valueMapping);

        String thinExpected = readClasspathFile("cdr/demo.3.unquoted.thin.json");
        assertThat(thined).isEqualTo(thinExpected);

        String compactExpected = readClasspathFile("cdr/demo.4.unquoted.thin.compact.json");
        String compact = new WestJson()
                .unquoted(true).thin(true).compact(true).json(raw);
        assertThat(compact).isEqualTo(compactExpected);

        JSON parsed = new WestParser().unpact(true)
                .unthin(westJson.getKeyMapping(), westJson.getValueMapping())
                .parse(compact);
        JSON rawParsed = (JSON) JSON.parse(raw);
        assertThat(parsed).isEqualTo(rawParsed);

        JSON thinParsed = new WestParser()
                .unthin(westJson.getKeyMapping(), westJson.getValueMapping())
                .parse(thined);
        assertThat(thinParsed).isEqualTo(rawParsed);

        JSON quotedParsed = new WestParser().quoted(true).parse(quoted);
        assertThat(quotedParsed).isEqualTo(rawParsed);
    }

    @SneakyThrows
    public static String readClasspathFile(String name) {
        val cl = DemoTest.class.getClassLoader();
        InputStream is = cl.getResourceAsStream(name);
        return new String(ByteStreams.toByteArray(is), Charsets.UTF_8);
    }
}
