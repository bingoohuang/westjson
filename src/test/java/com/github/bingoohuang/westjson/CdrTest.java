package com.github.bingoohuang.westjson;

import com.github.bingoohuang.westjson.impl.WestJsonUnquoter;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class CdrTest {
    @Test
    public void test1() {
        String complex = "{\"billstr\":\"月固定费:126.00;语音通话费:22.95;" +
                "增值业务费:10.20;调增减项:-6.00;消费合计:153.15;抵扣合计:153.15;" +
                "实际应缴合计:0.00;|3G业务M（多媒体）值【WCDMA(3G)-126元基本套餐B(首月套餐资费）】0M," +
                "3G业务T（文本）值【WCDMA(3G)-126元基本套餐B(首月套餐资费）】0T," +
                "数据业务流量【WCDMA(3G)-126元基本套餐B(首月套餐资费）】45.0542MB(非定向流量)," +
                "可视电话（分钟数）【WCDMA(3G)-126元基本套餐B(首月套餐资费）】0分钟(时长)," +
                "普通语音通话（分钟数）【WCDMA(3G)-126元基本套餐B(首月套餐资费）】680分钟(时长),;\"}";
        String expected = "{billstr:\"月固定费:126.00;语音通话费:22.95;" +
                "增值业务费:10.20;调增减项:-6.00;消费合计:153.15;抵扣合计:153.15;" +
                "实际应缴合计:0.00;|3G业务M（多媒体）值【WCDMA(3G)-126元基本套餐B(首月套餐资费）】0M," +
                "3G业务T（文本）值【WCDMA(3G)-126元基本套餐B(首月套餐资费）】0T," +
                "数据业务流量【WCDMA(3G)-126元基本套餐B(首月套餐资费）】45.0542MB(非定向流量)," +
                "可视电话（分钟数）【WCDMA(3G)-126元基本套餐B(首月套餐资费）】0分钟(时长)," +
                "普通语音通话（分钟数）【WCDMA(3G)-126元基本套餐B(首月套餐资费）】680分钟(时长),;\"}";
        String minified = new WestJsonUnquoter().unquote(complex);
        assertThat(minified).isEqualTo(expected);
    }
}
