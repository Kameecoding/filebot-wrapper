package com.kameecoding.filebot;

import com.kameecoding.filebot.enums.Databases;
import com.kameecoding.filebot.enums.FilebotOptions;
import com.kameecoding.filebot.enums.ResultType;
import com.neovisionaries.i18n.LanguageAlpha3Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rename implements Callable<RenameResult> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Rename.class);
    private Pattern renamePattern =
            Pattern.compile("from \\[(.*)\\] to \\[(.*)\\]", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private File logfile;
    private File executable;
    private List<String> arguments;

    private Rename() {}

    @Override
    public RenameResult call() {
        RenameResult renameResult = new RenameResult();
        Filebot filebot = Filebot.newInstance(executable, arguments, logfile);
        filebot.run();
        if (!filebot.isSuccess()) {
            renameResult.result = ResultType.SUCCESS;
            renameResult.reasons.add("Filebot failed");
            return renameResult;
        }

        Matcher m = renamePattern.matcher(filebot.getOutput());

        renameResult.result = ResultType.SUCCESS;
        if (m.find()) {
            renameResult.oldFile = new File(m.group(1));
            renameResult.newFile = new File(m.group(2));
            if (!renameResult.newFile.exists()) {
                renameResult.result = ResultType.SUCCESS;
                renameResult.reasons.add("File was renamed but output doesn't exist");
            }
            LOGGER.info("Renamed {} to {}", renameResult.getOldName(), renameResult.getNewName());
        } else {
            renameResult.result = ResultType.SUCCESS;
            renameResult.reasons.add("Failed to parse output");
        }

        return renameResult;
    }

    public static class RenameConfigurator {
        Rename rename = new Rename();
        String input;
        String output;
        boolean dryRun;
        boolean strict;
        String conflict = "override";
        Databases db = Databases.TheTvDB;
        String format = "{plex}";
        LanguageAlpha3Code language = LanguageAlpha3Code.eng;

        public RenameConfigurator(File executable, File input) {
            rename.executable = executable;
            this.input = input.getAbsolutePath();
        }

        public RenameConfigurator logFile(File logFile) {
            rename.logfile = logFile;
            return this;
        }

        public RenameConfigurator dryRun(boolean dryRun) {
            this.dryRun =dryRun;
            return this;
        }

        public RenameConfigurator output(File path) {
            this.output = path.getAbsolutePath();
            return this;
        }

        public RenameConfigurator db(Databases db) {
            this.db = db;
            return this;
        }

        public RenameConfigurator conflict(String conflict) {
            this.conflict = conflict;
            return this;
        }

        public RenameConfigurator strict(boolean strict) {
            this.strict = strict;
            return this;
        }

        public RenameConfigurator language(LanguageAlpha3Code language) {
            this.language = language;
            return this;
        }

        public Rename configure() {
            rename.arguments = new ArrayList<>();
            rename.arguments.add(FilebotOptions.rename.getOpt());
            rename.arguments.add(input);
            if (dryRun) {
                rename.arguments.add(FilebotOptions.action.getOpt());
                rename.arguments.add("test");
            }
            rename.arguments.add(FilebotOptions.lang.getOpt());
            rename.arguments.add(language.getAlpha3B().toString());
            rename.arguments.add(FilebotOptions.format.getOpt());
            rename.arguments.add("{plex}");
            rename.arguments.add(FilebotOptions.db.getOpt());
            rename.arguments.add(db.toString());
            rename.arguments.add(FilebotOptions.output.getOpt());
            rename.arguments.add(output);
            rename.arguments.add("--conflict");
            rename.arguments.add(conflict);
            if (!strict) {
                rename.arguments.add(FilebotOptions.nonStrict.getOpt());
            }
            return rename;
        }
    }
}
