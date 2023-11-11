package kz.product.dreamteam.utils;

public class Util {

    public static boolean notNullOrEmptyStr(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
