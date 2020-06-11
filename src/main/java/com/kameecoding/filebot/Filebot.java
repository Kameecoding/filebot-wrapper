package com.kameecoding.filebot;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filebot implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(Filebot.class);
  private ProcessBuilder processBuilder;
  private Pattern failurePattern =
      Pattern.compile(".*failure.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
  private boolean success = true;
  private File logfile;
  private String output;

  private Filebot() {}

  public static Filebot newInstance(File executable, List<String> args, File logfile) {
    Filebot instance = new Filebot();
    instance.logfile = logfile;
    List<String> arguments = new ArrayList<>(args);
    arguments.add(0, executable.getAbsolutePath());
    instance.processBuilder = new ProcessBuilder(arguments);
    return instance;
  }

  @Override
  public void run() {
    LOGGER.trace("Filebot running");
    try {
      if (logfile != null) {
        logfile.getParentFile().mkdirs();
        processBuilder.redirectError(logfile);
      } else {
        processBuilder.redirectError(Redirect.INHERIT);
      }
      Process process = processBuilder.start();
//      String encoding = "windows-1252";
//      if (SystemUtils.IS_OS_LINUX) {
//        encoding = "UTF-8";
//      }

      StringBuilder processOutput = new StringBuilder();
      try (BufferedReader processOutputReader =
          new BufferedReader(new InputStreamReader(process.getInputStream())); ) {
        String readLine;
        while ((readLine = processOutputReader.readLine()) != null) {
          processOutput.append(readLine).append(System.lineSeparator());
        }
        process.waitFor();
        output = processOutput.toString();
      } catch (Exception e) {
        success = false;
        LOGGER.error("Filebot Execution failed", e);
      }
      if (process.exitValue() != 0) {
        Matcher fail;
        fail = failurePattern.matcher(output);
        if (fail.find()) {
          success = false;
          LOGGER.error("Filebot execution failed");
        }
      }

    } catch (Exception e) {
      LOGGER.error("Filebot execution failed", e);
    }
    LOGGER.trace("Filebot finished");
  }

  public boolean isSuccess() {
    return success;
  }

  public String getOutput() {
    return output;
  }
}
