package edu.epu.sentiment.analysis.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by duong on 3/29/16.
 */
public class SAFile {

    public static String home = System.getProperty("user.home");
    public static String dir = System.getProperty("user.dir");
    public static String line = System.getProperty("line.separator");

    public static void writeStringsToFile(ArrayList<String> strings, String file) {
        try {
            FileWriter writer = new FileWriter(file, true);
            for (String string : strings) {
                writer.write(string);
                writer.write(line);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeStringToFile(String string, String file) {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(string);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readStringFromFile(String file) {
        String string = "";
        try {
            FileReader reader = new FileReader(file);
            int character;
            while ((character = reader.read()) != -1) {
                string += (char) character;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }
}
