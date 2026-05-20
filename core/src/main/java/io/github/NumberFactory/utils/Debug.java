package io.github.NumberFactory.utils;

public class Debug {
    public static final boolean DEBUG = true;

    public static void msg(String message) {
        if (DEBUG) System.out.println(message);
    }
}
