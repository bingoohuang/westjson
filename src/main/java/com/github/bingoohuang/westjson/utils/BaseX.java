package com.github.bingoohuang.westjson.utils;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public abstract class BaseX {
    /**
     * All possible digits for representing a number as a String
     * This is conservative and does not include 'special'
     * characters since some browsers don't handle them right.
     * The IE for instance seems to be case insensitive in class
     * names for CSSs. Grrr.
     */
    private static final String baseDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final static char[] DIGITS = baseDigits.toCharArray();
    public static final int MAX_RADIX = DIGITS.length;

    /**
     * Codes number up to radix 62.
     * Note, this method is only public for backward compatiblity. don't
     * use it.
     *
     * @param minDigits returns a string with a least minDigits digits
     */
    public static String toString(long i, int radix, int minDigits) {
        char[] buf = new char[65];
        radix = Math.min(Math.abs(radix), MAX_RADIX);
        minDigits = Math.min(buf.length - 1, Math.abs(minDigits));
        int charPos = buf.length - 1;
        boolean negative = (i < 0);
        if (negative) {
            i = -i;
        }
        while (i >= radix) {
            buf[charPos--] = DIGITS[(int) (i % radix)];
            i /= radix;
        }
        buf[charPos] = DIGITS[(int) i];
        // if minimum length of the result string is set, pad it with the
        // zero-representation (that is: '0')
        while (charPos > buf.length - minDigits)
            buf[--charPos] = DIGITS[0];
        if (negative) {
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, buf.length - charPos);
    }

    public static String base62(int decimalNumber) {
        return fromDecimalToOtherBase(62, decimalNumber);
    }

    public static int unBase62(String base62Number) {
        return fromOtherBaseToDecimal(62, base62Number);
    }

    private static String fromDecimalToOtherBase(int base, int decimalNumber) {
        String result = decimalNumber == 0 ? baseDigits.substring(0, 1) : "";
        while (decimalNumber != 0) {
            int mod = decimalNumber % base;
            result = baseDigits.substring(mod, mod + 1) + result;
            decimalNumber = decimalNumber / base;
        }
        return result;
    }

    private static int fromOtherBaseToDecimal(int base, String number) {
        int result = 0;
        for (int pos = number.length(), multiplier = 1; pos > 0; pos--) {
            result += baseDigits.indexOf(number.substring(pos - 1, pos)) * multiplier;
            multiplier *= base;
        }
        return result;
    }
}
