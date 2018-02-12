package com.kameecoding.filebot;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** @author Andrej Kovac kameecoding (kamee@kameecoding.com) on 2017-07-07. */
public class Filebot implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Filebot.class);

    private ProcessBuilder processBuilder;
    private Pattern renamePattern =
            Pattern.compile("from \\[(.*)] to \\[(.*)]", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private Pattern failurePattern =
            Pattern.compile(".*failure.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private File oldFile;
    private File newFile;
    private boolean success;
    private File logfile;


    private Filebot() {}

    public static Filebot newInstance(String executable, List<String> args, File logfile) {
        Filebot instance = new Filebot();
        instance.logfile = logfile;
        List<String> arguments = new ArrayList<>(args);
        arguments.add(0, executable);
        instance.processBuilder = new ProcessBuilder(arguments);
        return instance;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Filebot running");
            if (logfile != null) {
                logfile.getParentFile().mkdirs();
                processBuilder.redirectError(logfile);
            } else {
                processBuilder.redirectError(Redirect.INHERIT);
            }
            Process process = processBuilder.start();
            String encoding = "windows-1252";
            if (SystemUtils.IS_OS_LINUX) {
                encoding = "UTF-8";
            }
            LineIterator lineIterator = IOUtils.lineIterator(process.getInputStream(), encoding);
            StringBuilder sb = new StringBuilder();
            String s = null;
            Matcher m = null;
            Matcher fail = null;
            while (true) {
                if (lineIterator.hasNext()) {
                    String line = lineIterator.nextLine();
                    System.out.println(line);
                    sb.append(line);
                } else if (!process.isAlive()) {
                    break;
                }
            }
            s = sb.toString();
            m = renamePattern.matcher(s);
            fail = failurePattern.matcher(s);
            if (m.find()) {
                oldFile = new File(m.group(1));
                newFile = new File(m.group(2));
                if (!newFile.exists()) {
                    success = false;
                    throw new Exception("File was renamed but output doesn't exist");
                }
                LOGGER.info("Renamed {} to {}", getOldName(), getNewName());
                success = true;
            } else if (fail.find()) {
                success = false;
            }
        } catch (Exception e) {
            LOGGER.error("Filebot failed", e);
        }
    }

    /** @return absolute path to file after rename */
    public String getNewName() {
        return newFile.getAbsolutePath();
    }

    /** @return absolute path to file before rename */
    public String getOldName() {
        return oldFile.getAbsolutePath();
    }

    public File getOldFile() {
        return oldFile;
    }

    public File getNewFile() {
        return newFile;
    }

    public boolean isSuccess() {
        return success;
    }
}
