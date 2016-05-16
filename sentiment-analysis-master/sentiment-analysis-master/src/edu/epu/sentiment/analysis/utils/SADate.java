package edu.epu.sentiment.analysis.utils;

import java.util.HashMap;

/**
 * Created by duong on 3/30/16.
 */
public class SADate {
    public static HashMap<String, String> months = new HashMap<>();
    private static SADate instance = null;

    private SADate() {
        months.put("tháng mười hai", "12");
        months.put("tháng mười một", "11");
        months.put("tháng mười", "10");
        months.put("tháng chín", "09");
        months.put("tháng tám", "08");
        months.put("tháng bảy", "07");
        months.put("tháng sáu", "06");
        months.put("tháng năm", "05");
        months.put("tháng tư", "04");
        months.put("tháng ba", "03");
        months.put("tháng hai", "02");
        months.put("tháng một", "01");

        months.put("tháng 12", "12");
        months.put("tháng 11", "11");
        months.put("tháng 10", "10");
        months.put("tháng 09", "09");
        months.put("tháng 08", "08");
        months.put("tháng 07", "07");
        months.put("tháng 06", "06");
        months.put("tháng 05", "05");
        months.put("tháng 04", "04");
        months.put("tháng 03", "03");
        months.put("tháng 02", "02");
        months.put("tháng 01", "01");

        months.put("tháng 1", "01");
        months.put("tháng 2", "02");
        months.put("tháng 3", "03");
        months.put("tháng 4", "04");
        months.put("tháng 5", "05");
        months.put("tháng 6", "06");
        months.put("tháng 7", "07");
        months.put("tháng 8", "08");
        months.put("tháng 9", "09");
    }

    public static SADate getInstance() {
        if (instance == null) {
            instance = new SADate();
        }
        return instance;
    }
}
