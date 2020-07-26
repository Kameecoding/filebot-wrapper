package com.kameecoding.filebot.enums;

public enum FilebotOptions {
    rename("-rename", "", true),
    format("--format", "", true),
    db("--db", "", true),
    nonStrict("-non-strict", "", false),
    action("--action", "", true),
    output("--output", "", true),
    lang("--lang", "", true),
    query("--q", "" ,true);

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
