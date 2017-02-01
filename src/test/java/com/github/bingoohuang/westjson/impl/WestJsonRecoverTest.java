package com.github.bingoohuang.westjson.impl;

import lombok.val;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonRecoverTest {
    @Test
    public void simple() {
        String str = "{expiredTime:{}}";
        String recover = new WestJsonRecover(str).recover();
        assertThat(recover).isEqualTo("{\"expiredTime\":{}}");
    }

    @Test
    public void testNull() {
        String str = "{expiredTime:null}";
        String recover = new WestJsonRecover(str).recover();
        assertThat(recover).isEqualTo("{\"expiredTime\":null}");
    }

    @Test
    public void testSimilarNull1() {
        String str = "{expiredTime:nullable}";
        String recover = new WestJsonRecover(str).recover();
        assertThat(recover).isEqualTo("{\"expiredTime\":\"nullable\"}");
    }

    @Test
    public void testSimilarNull2() {
        String str = "{expiredTime:abcnull}";
        String recover = new WestJsonRecover(str).recover();
        assertThat(recover).isEqualTo("{\"expiredTime\":\"abcnull\"}");
    }

    @Test
    public void testTrue() {
        String str = "{expiredTime:true}";
        String recover = new WestJsonRecover(str).recover();
        assertThat(recover).isEqualTo("{\"expiredTime\":true}");
    }

    @Test
    public void testFalse() {
        String str = "{expiredTime:false}";
        String recover = new WestJsonRecover(str).recover();
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
        String recover = new WestJsonRecover(str).recover();
        String minify = new WestJsonMinifier(recover).minify();
        assertThat(minify).isEqualTo(str);
    }

    @Test
    public void test2() {
        val text = "{'jdbcUrl':'jdbc:wrap-jdbc:filters=default:name=com.alibaba.dragoon.monitor:jdbc:mysql://10.20.129.167/dragoon_v25monitordb?useUnicode=true&characterEncoding=UTF-8'}";
        val text2 = "{jdbcUrl:\"jdbc:wrap-jdbc:filters=default:name=com.alibaba.dragoon.monitor:jdbc:mysql://10.20.129.167/dragoon_v25monitordb?useUnicode=true&characterEncoding=UTF-8\"}";
        String minify = new WestJsonMinifier(text).minify();
        assertThat(minify).isEqualTo(text2);
    }
}
