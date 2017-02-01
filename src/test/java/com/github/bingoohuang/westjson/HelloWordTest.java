package com.github.bingoohuang.westjson;

import com.github.bingoohuang.westjson.bean.Person;
import lombok.val;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/1/30.
 */
public class HelloWordTest {
    @Test
    public void hello() {
        val person = new Person("bingoo", 123);
        val json = new WestJson().json(person);
        assertThat(json).isEqualTo("{age:123,name:bingoo}");
    }

    @Test
    public void helloParse() {
        val person = new Person("bingoo", 123);
        val parsed = new WestParser().parse("{age:123,name:bingoo}", Person.class);
        assertThat(parsed).isEqualTo(person);
    }

    @Test
    public void helloBlank() {
        val person = new Person("bin goo", 123);
        val json = new WestJson().json(person);
        assertThat(json).isEqualTo("{age:123,name:bin goo}");
    }

    @Test
    public void helloBlankParse() {
        val person = new Person("bin goo", 123);
        val parsed = new WestParser().parse("{age:123,name:bin goo}", Person.class);
        assertThat(parsed).isEqualTo(person);
    }

    @Test
    public void helloQuote() {
        val person = new Person("bin\"goo", 123);
        val json = new WestJson().json(person);
        assertThat(json).isEqualTo("{age:123,name:bin\\\"goo}");
    }

    @Test
    public void helloQuoteParse() {
        val person = new Person("bin\"goo", 123);
        val parsed = new WestParser().parse("{age:123,name:bin\\\"goo}", Person.class);
        assertThat(parsed).isEqualTo(person);
    }

    @Test
    public void helloComma() {
        val person = new Person("bin,goo", 123);
        val json = new WestJson().json(person);
        assertThat(json).isEqualTo("{age:123,name:bin\\,goo}");
    }

    @Test
    public void helloCommaParse() {
        val person = new Person("bin,goo", 123);
        val parsed = new WestParser().parse("{age:123,name:bin\\,goo}", Person.class);
        assertThat(parsed).isEqualTo(person);
    }

    @Test
    public void helloColon() {
        val person = new Person("bin:goo", 123);
        val json = new WestJson().json(person);
        assertThat(json).isEqualTo("{age:123,name:bin\\:goo}");

        val person2 = new Person("bin:g:oo", 123);
        val json2 = new WestJson().json(person2);
        assertThat(json2).isEqualTo("{age:123,name:\"bin:g:oo\"}");
    }

    @Test
    public void helloColonParse() {
        val person = new Person("bin:goo", 123);
        val parsed = new WestParser().parse("{age:123,name:bin\\:goo}", Person.class);
        assertThat(parsed).isEqualTo(person);
    }

    @Test
    public void hello2Comma() {
        val person = new Person("bin,,goo", 123);
        val json = new WestJson().json(person);
        assertThat(json).isEqualTo("{age:123,name:\"bin,,goo\"}");
    }

    @Test
    public void hello2CommaParse() {
        val person = new Person("bin,,goo", 123);
        val parsed = new WestParser().parse("{age:123,name:\"bin,,goo\"}", Person.class);
        assertThat(parsed).isEqualTo(person);
    }


}
