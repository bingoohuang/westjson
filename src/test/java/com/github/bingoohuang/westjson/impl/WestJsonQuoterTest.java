package com.github.bingoohuang.westjson.impl;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westjson.WestJson;
import com.github.bingoohuang.westjson.WestParser;
import lombok.val;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonQuoterTest {
    @Test
    public void simple() {
        String str = "{expiredTime:{}}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo("{\"expiredTime\":{}}");
    }

    @Test
    public void testNull() {
        String str = "{expiredTime:null}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo("{\"expiredTime\":null}");
    }

    @Test
    public void testEmpty() {
        String str = "{expiredTime:}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo("{\"expiredTime\":\"\"}");
    }

    @Test
    public void testEmpty2() {
        String str = "{name:,age:32}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo("{\"name\":\"\",\"age\":\"32\"}");
    }

    @Test
    public void parseArray() {
        val json = "{record:[{calldate:20130101},{calldate:20130101}]}";
        val expected = "{\"record\":[{\"calldate\":\"20130101\"},{\"calldate\":\"20130101\"}]}";
        String recover = new WestJsonQuoter().quote(json);
        assertThat(recover).isEqualTo(expected);

        val json2 = "{all:5.35,record:[{calldate:20130101},{calldate:20130101}]}";
        val expected2 = "{\"all\":\"5.35\",\"record\":[{\"calldate\":\"20130101\"},{\"calldate\":\"20130101\"}]}";
        String recover2 = new WestJsonQuoter().quote(json2);
        assertThat(recover2).isEqualTo(expected2);

        String minified2 = new WestJsonUnquoter().unquote(expected2);
        assertThat(minified2).isEqualTo(json2);
    }

    @Test
    public void testSimilarNull1() {
        String str = "{expiredTime:nullable}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo("{\"expiredTime\":\"nullable\"}");
    }

    @Test
    public void testSimilarNull2() {
        String str = "{expiredTime:abcnull}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo("{\"expiredTime\":\"abcnull\"}");
    }

    @Test
    public void testTrue() {
        String str = "{expiredTime:true}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo("{\"expiredTime\":true}");
    }

    @Test
    public void testFalse() {
        String str = "{expiredTime:false}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo("{\"expiredTime\":false}");
    }

    @Test
    public void test1() {
        String str = "{binding:18602506990,domain:10010.com,expiredTime:{}," +
                "fromAction:null,ip:192.168.1.108,minituesToLive:{}," +
                "safeKeyName:c30b87b5-80ae-4e63-a27d-7c9bbb48a623," +
                "securityContent:" +
                "{HLoSRPXKfg:DdjIe5yYuYAQjcpupd2QWsyaFXB9Xe," +
                "PWePBugXwj:EqTCXsMIJ3kP3U7P38tN1edGO9FuaR," +
                "PvmZOLwkWk:sojjuvv4YItlZMHUs1feq4P76qaCqP," +
                "QljtUpvKDR:z793oA05G9lTbl8Qz4uDOrDP1tueuf," +
                "TjkoAaRPzZ:NbRdVh8gJvdl0BSz2kHQr6F1mjridF," +
                "ZwWryjhLGN:nhCwAJXaDiiQ0hqrDaL3bZPEt67Do7," +
                "dSrowjSqTr:TAfFbM6wUQnAEnX0pYVnhAE5t4ZvYB," +
                "fVLxhJiYkP:y246He6jm7S6MN76giaNiZxZIzSeGu," +
                "iJmELhTYHg:HBvmSiHBHsPco2x3IPYwDBqPeBT2MI," +
                "kyDuqHoHZX:bsvMCohphETRPQTO7n5Vkxfki9uAL7," +
                "mGmGpoEghU:x53Uzzny8P0y06jvEEnhIBZaghyhVn," +
                "qbtWHCwPBK:NZwXdnHMifrtpFujaQBWjm5aQeyCEm," +
                "tsWWGyCmiz:BqXJnUKOEbyVp9N52reVDcmOMt2HCW," +
                "wZtgWAuXMp:vPKn9A0DxDudgO9980HaPJyeCBsk6n," +
                "zqQqEtiePX:zxgnjid0jxn4pYN2Ijo7CjdLYVQScX}," +
                "securitySafekey:c06a4f76-049d-455c-94ad-d5cd03c562e2," +
                "securityToken:71e0f055-2621-41e7-900f-7cd3327ac872," +
                "stepAction:98bde530-5194-496e-b1d4-b091067f8142}";
        String recover = new WestJsonQuoter().quote(str);
        String minify = new WestJsonUnquoter().unquote(recover);
        assertThat(minify).isEqualTo(str);
    }

    @Test
    public void test2() {
        val text = "{\"jdbcUrl\":\"jdbc:wrap-jdbc:filters=default:name=com.alibaba.dragoon.monitor:jdbc:mysql://10.20.129.167/dragoon_v25monitordb?useUnicode=true&characterEncoding=UTF-8\"}";
        val text2 = "{jdbcUrl:\"jdbc:wrap-jdbc:filters=default:name=com.alibaba.dragoon.monitor:jdbc:mysql://10.20.129.167/dragoon_v25monitordb?useUnicode=true&characterEncoding=UTF-8\"}";
        String minify = new WestJsonUnquoter().unquote(text);
        assertThat(minify).isEqualTo(text2);
    }

    @Test
    public void test3() {
        val str = "{3:\"起始时间:20130501;截止时间:20130510;月固定费:66.00;增值业务费:9.00;\",C:26126.0}";
        val str2 = "{\"3\":\"起始时间:20130501;截止时间:20130510;月固定费:66.00;增值业务费:9.00;\",\"C\":\"26126.0\"}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo(str2);
    }

    @Test
    public void test4() {
        val str = "{_d:[@3,]}";
        val str2 = "{\"_d\":[\"@3\",\"\"]}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo(str2);
    }

    @Test
    public void test41() {
        val str = "{_d:[,]}";
        val str2 = "{\"_d\":[\"\",\"\"]}";
        String recover = new WestJsonQuoter().quote(str);
        assertThat(recover).isEqualTo(str2);
    }

    @Test
    public void test5() {
        val str = "{\"trxid\":\"GWAY201305100001309552\",\"rspmsg\":{\"imsi\":\"\",\"nextproductid\":null,\"nowpackageinfo\":{\"nowbillingcode\":\"\",\"nowbillingname\":\"\",\"nowpackagecode\":\"99104722\"},\"productid\":\"\",\"userid\":\"\"}}";
        WestJson westJson = new WestJson();
        String json = westJson.json(str, WestJson.UNQUOTED | WestJson.THIN | WestJson.COMPACT);

        JSON parse = new WestParser()
                .unthin(westJson.getKeyMapping(), westJson.getValueMapping())
                .parse(json, WestParser.QUOTED | WestParser.UNCOMPACT);
        JSON parse1 = (JSON) JSON.parse(str);
        assertThat(parse1.toJSONString()).isEqualTo(parse.toJSONString());
    }
}
