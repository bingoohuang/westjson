package com.github.bingoohuang.westjson.impl;

public class StrBuilder {
    private StringBuilder sb;

    public StrBuilder(int len) {
        this(new StringBuilder(len));
    }

    public StrBuilder(StringBuilder sb) {
        this.sb = sb;
    }


    public StrBuilder p(char c) {
        sb.append(c);
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
