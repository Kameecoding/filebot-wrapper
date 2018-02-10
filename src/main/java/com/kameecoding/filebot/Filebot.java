/* * *  MIT License * * <p>Copyright (c) 2018 Andrej Kovac (Kameecoding) * * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software * and associated documentation files (the "Software"), to deal in the Software without restriction, * including without limitation the rights to use, copy, modify, merge, publish, distribute, * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is *  furnished to do so, subject to the following conditions: * * <p>The above copyright notice and this permission notice shall be included in all copies or *  substantial portions of the Software. * * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. * */
package com.kameecoding.filebot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrej Kovac kameecoding (kamee@kameecoding.com) on
 * 2017-07-07.
 */
public class Filebot implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Filebot.class);

	private ProcessBuilder processBuilder;
	private Process process;
	private Pattern renamePattern = Pattern.compile("from \\[(.*)] to \\[(.*)]",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private Pattern failurePattern = Pattern.compile(".*failure.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private File oldFile;
	private File newFile;
	private boolean success;
	private boolean finished;
	private BufferedReader stdInput;
	private BufferedReader stdError;

	private Filebot() {
	}

	public static Filebot newInstance(String executable, List<String> args) {
		Filebot instance = new Filebot();
		List<String> arguments = new ArrayList<>(args);
		arguments.add(0, executable);
		instance.processBuilder = new ProcessBuilder(arguments);
		return instance;
	}

	@Override
	public void run() {
		try {
			LOGGER.info("Filebot running");
			processBuilder.redirectError(Redirect.INHERIT);
			process = processBuilder.start();

			stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			//stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			StringBuilder sb = new StringBuilder();
			String s = null;
			Matcher m = null;
			Matcher fail = null;
			while ((s = stdInput.readLine()) != null) {
				sb.append(s);
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

			finished = true;
		} catch (Exception e) {
			LOGGER.error("Filebot failed", e);
		}
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	/**
	 * @return absolute path to file after rename
	 */
	public String getNewName() {
		return newFile.getAbsolutePath();
	}

	/**
	 * @return absolute path to file before rename
	 */
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

	public boolean isFinished() {
		return finished;
	}
}
