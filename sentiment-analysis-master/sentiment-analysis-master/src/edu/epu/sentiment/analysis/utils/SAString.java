package edu.epu.sentiment.analysis.utils;

/**
 * Created by duong on 3/29/16.
 */
public class SAString {

    public static String normalizeTitle(String title) {
        return title.replaceAll("[\\\\/:\"*?<>|]+", " ");
    }

}
