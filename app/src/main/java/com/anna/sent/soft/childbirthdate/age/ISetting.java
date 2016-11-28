package com.anna.sent.soft.childbirthdate.age;

public interface ISetting extends LocalizableObject {
    String save();

    ISetting load(String str);
}