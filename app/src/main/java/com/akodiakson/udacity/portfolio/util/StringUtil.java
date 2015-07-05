package com.akodiakson.udacity.portfolio.util;

public class StringUtil {
    public static boolean isEmpty(String string){
        return string == null || string.trim().isEmpty();
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }
}
