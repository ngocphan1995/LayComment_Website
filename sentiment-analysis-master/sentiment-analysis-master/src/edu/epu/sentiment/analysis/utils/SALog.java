package edu.epu.sentiment.analysis.utils;

/**
 * Created by duong on 3/27/16.
 */
public class SALog {

    public static void prln(String msg) {
        System.out.println(msg);
    }

    public static void log(String tag, String msg) {
        System.out.println(String.format("%-8s: %s", tag, msg));
    }

    public static void pr(String msg) {
        System.out.print(msg);
    }

}
