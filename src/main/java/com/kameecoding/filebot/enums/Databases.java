package com.kameecoding.filebot.enums;


public enum Databases {
    TheTvDB("TheMovieDB::TV"),
    TVmaze("TVmaze"),
    TheMovieDB("TheMovieDB");

    private final String option;

    Databases(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return option;
    }
}
