/* * *  MIT License * * <p>Copyright (c) 2018 Andrej Kovac (Kameecoding) * * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software * and associated documentation files (the "Software"), to deal in the Software without restriction, * including without limitation the rights to use, copy, modify, merge, publish, distribute, * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is *  furnished to do so, subject to the following conditions: * * <p>The above copyright notice and this permission notice shall be included in all copies or *  substantial portions of the Software. * * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. * */
package com.kameecoding.filebot.enums;

/**
 * @author Andrej Kovac kameecoding (kamee@kameecoding.com) on
 * 2017-07-07.
 */
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
