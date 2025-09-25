package com.spribe.enums;

public enum Gender {
    MALE("male"),
    FEMALE("female");

    Gender(String gender) {
        this.gender = gender;
    }

    private final String gender;

    public String getGender() {
        return gender;
    }
}
