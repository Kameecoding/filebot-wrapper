/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on
 * 2017-07-07.
 */
package com.kameecoding.filebot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filebot implements Runnable {

	private ProcessBuilder processBuilder;
	private Process process;
	private Pattern renamePattern = Pattern.compile("from \\[(.*)] to \\[(.*)]",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private Pattern failurePattern = Pattern.compile(".*failure.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private String oldName;
	private String newName;
	private boolean success;
	private boolean finished;
	private BufferedReader stdInput;
	private BufferedReader stdError;

	private Filebot() {
	}

	public static Filebot newInstance(String location, List<String> args) {
		Filebot instance = new Filebot();
		args.add(0, location);
		instance.processBuilder = new ProcessBuilder(args);
		return instance;
	}

	@Override
	public void run() {
		try {
			process = processBuilder.start();

			stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			// read the output from the command
			// System.out.println("Here is the standard output of the command:\n");
			StringBuilder sb = new StringBuilder();
			String s = null;
			Matcher m = null;
			Matcher fail = null;
			while ((s = stdInput.readLine()) != null) {
				sb.append(s);
				// System.out.println(s);
			}
			s = sb.toString();
			m = renamePattern.matcher(s);
			fail = failurePattern.matcher(s);
			if (m.find()) {
				oldName = m.group(1);
				newName = m.group(2);
				success = true;
			} else if (fail.find()) {
				success = false;
			}

			finished = true;
			// read any errors from the attempted command
			/*
			 * System.out.println("Here is the standard error of the command (if any):\n");
			 * while ((s = stdError.readLine()) != null) { System.out.println(s); }
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public String getNewName() {
		return newName;
	}

	public String getOldName() {
		return oldName;
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean isFinished() {
		return finished;
	}
}
