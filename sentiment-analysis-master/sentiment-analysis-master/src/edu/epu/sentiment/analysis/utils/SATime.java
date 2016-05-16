package edu.epu.sentiment.analysis.utils;

/**
 * Created by duong on 3/30/16.
 */
public class SATime {

    public static long millisecondsInSeconds(long seconds) {
        return seconds * 1000;
    }

    // Minutes
    public static long millisecondsInMinutes(long minutes) {
        return millisecondsInSeconds(secondsInMinutes(minutes));
    }

    public static long secondsInMinutes(long minutes) {
        return minutes * 60;
    }

    // Hours
    public static long minutesInHours(long hours) {
        return hours * 60;
    }

    public static long secondsInHours(long hours) {
        return secondsInMinutes(minutesInHours(hours));
    }

    public static long millisecondsInHours(long hours) {
        return millisecondsInMinutes(minutesInHours(hours));
    }

    // Days
    public static long hoursInDays(long days) {
        return days * 24;
    }

    public static long minutesInDays(long days) {
        return minutesInHours(hoursInDays(days));
    }

    public static long secondsInDays(long days) {
        return secondsInMinutes(minutesInHours(hoursInDays(days)));
    }

    public static long millisecondsInDays(long days) {
        return millisecondsInSeconds(secondsInMinutes(minutesInHours(hoursInDays(days))));
    }
}

