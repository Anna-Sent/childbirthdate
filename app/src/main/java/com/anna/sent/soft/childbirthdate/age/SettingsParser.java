package com.anna.sent.soft.childbirthdate.age;

import java.util.ArrayList;
import java.util.List;

public class SettingsParser {
    private static final String DELIMITER_LIST = ";";

    private static String saveList(List<ISetting> list) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            result.append(list.get(i).save())
                    .append(i == list.size() - 1 ? "" : DELIMITER_LIST);
        }

        return result.toString();
    }

    private static List<ISetting> loadList(String str, ISetting element) {
        List<ISetting> result = new ArrayList<>();

        if (str != null) {
            String[] tokens = str.split(DELIMITER_LIST);
            for (String token : tokens) {
                ISetting obj = element.load(token);
                if (obj != null) {
                    result.add(obj);
                }
            }
        }

        return result;
    }

    public static List<LocalizableObject> toList(String str, ISetting element) {
        List<ISetting> source = SettingsParser.loadList(str, element);
        List<LocalizableObject> destination = new ArrayList<>();
        destination.addAll(source);
        return destination;
    }

    public static String toString(List<LocalizableObject> list) {
        List<ISetting> destination = new ArrayList<>();

        for (int i = 0; i < list.size(); ++i) {
            destination.add((ISetting) list.get(i));
        }

        return SettingsParser.saveList(destination);
    }
}
