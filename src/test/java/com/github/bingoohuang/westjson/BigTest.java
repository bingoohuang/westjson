package com.github.bingoohuang.westjson;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static com.github.bingoohuang.westjson.utils.WestJsonUtils.convertNumberToString;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class BigTest {
    @Test
    public void test() {
        bigTest("big/gateway.1.log.gz");
    }

    @SneakyThrows
    private void bigTest(String classpathFileName) {
        val cl = BigTest.class.getClassLoader();
        @Cleanup val is = cl.getResourceAsStream(classpathFileName);
        @Cleanup val gis = new GZIPInputStream(is);
        BufferedReader reader = new BufferedReader(new InputStreamReader(gis, Charsets.UTF_8));
        long fileStart = System.currentTimeMillis();
        int totalJsonsNum = 0;
        for (String line; (line = reader.readLine()) != null; ) {
            List<String> jsons = parseLine(line);
            int size = jsons.size();
            totalJsonsNum += size;
            for (String json : jsons) {
                compressJson(json);
            }
        }
        System.out.println("file cost " + (System.currentTimeMillis() - fileStart) / 1000. / 60. + "min");
        System.out.println("total json num: " + totalJsonsNum);
        System.out.println("process total old: " + totalOldSize / 1024. + "K , " +
                "compact total size: " + totalCompactSize / 1024. + "K");
    }


    private long totalOldSize = 0;
    private long totalCompactSize = 0;

    @SneakyThrows
    private void compressJson(String cdrJson) {
        totalOldSize += cdrJson.length();

        try {
            WestJson westJson = new WestJson();
            String compressed = westJson.json(cdrJson, WestJson.UNQUOTED | WestJson.THIN | WestJson.COMPACT);
            totalCompactSize += compressed.length();

            WestParser westParser = new WestParser()
                    .unthin(westJson.getKeyMapping(), westJson.getValueMapping());
            JSON parse = westParser.parse(compressed, WestParser.QUOTED | WestParser.UNCOMPACT);
            JSON direct = (JSON) JSON.parse(cdrJson);

            if (parse.equals(direct)) return;
            if (parse.toJSONString().equals(direct.toJSONString())) return;
            if (parse.toJSONString().equals(convertNumberToString(cdrJson)))
                return;

            System.out.println("keyMap:" + westJson.getKeyMapping());
            System.out.println("cdrJson:" + cdrJson);
            System.out.println("parse:" + parse);
            System.out.println("direct:" + direct);

            Assert.fail();
        } catch (Throwable ex) {
            System.out.println("error json:" + cdrJson);
            System.out.println("error reason:" + ex.getMessage());
            throw ex;
        }
    }


    private List<String> parseLine(String line) {
        ArrayList<String> jsons = new ArrayList<String>();
        int lastPos = -1;
        int leftNum = 0;
        boolean openQuote = false; // 双引号开始
        for (int i = 0, ii = line.length(); i < ii; ++i) {
            char ch = line.charAt(i);
            if (ch == '\\' && i + 1 < ii) {
                ++i;
                continue;
            }

            if (ch == '{' && !openQuote) {
                ++leftNum;
                if (leftNum == 1) lastPos = i;
            }

            if (lastPos < 0) continue;

            if (ch == '"') {
                openQuote = !openQuote;
            } else if (ch == '}' && !openQuote) {
                --leftNum;
            }

            if (lastPos >= 0 && leftNum == 0) {
                String fullJson = line.substring(lastPos, i + 1);
                jsons.add(fullJson);
                lastPos = -1;
            }
        }

        return jsons;
    }
}
