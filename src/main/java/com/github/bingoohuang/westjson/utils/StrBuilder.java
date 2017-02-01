package com.github.bingoohuang.westjson.utils;

public class StrBuilder {
    private StringBuilder sb;

    public StrBuilder(int len) {
        this(new StringBuilder(len));
    }

    public StrBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    public StrBuilder() {
        this(new StringBuilder());
    }

    public StrBuilder p(char c) {
        sb.append(c);
        return this;
    }

    public StrBuilder p(StrBuilder s) {
        sb.append(s.toString());
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }


    public void empty() {
        sb.setLength(0);
    }

    public void deleteCharAt(int pos) {
        sb.deleteCharAt(pos);
    }

    public int len() {
        return sb.length();
    }
}
