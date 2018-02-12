package com.kameecoding.filebot.enums;

/** @author Andrej Kovac kameecoding (kamee@kameecoding.com) on 2017-07-07. */
public enum FilebotOptions {
    rename("-rename", "", true),
    format("--format", "", true),
    db("--db", "", true),
    nonStrict("-non-strict", "", false),
    action("--action", "", true),
    output("--output", "", true),
    lang("--lang", "", true);

    private String opt;
    private String description;
    private boolean hasArgument;

    FilebotOptions(String opt, String description, boolean hasArgument) {
        this.opt = opt;
        this.description = description;
        this.hasArgument = hasArgument;
    }

    public String getOpt() {
        return opt;
    }

    public String getDescription() {
        return description;
    }
}
