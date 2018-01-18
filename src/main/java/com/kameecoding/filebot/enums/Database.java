/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on
 * 2018-01-16.
 */
package com.kameecoding.filebot.enums;


public enum Database {
	TheTvDB("TheTVDB"),
	TheMovieDB("TheMovieDB");
	
	private final String option;
	
	Database(String option) {
		this.option = option;
	}

	@Override
	public String toString() {
		return option;
	}
}
