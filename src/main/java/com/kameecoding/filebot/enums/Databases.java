package com.kameecoding.filebot.enums;

/** @author Andrej Kovac kameecoding (kamee@kameecoding.com) on 2018-01-16. */
public enum Databases {
    TheTvDB("TheTVDB"),
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
