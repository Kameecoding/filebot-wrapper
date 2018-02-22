package com.kameecoding.filebot;

import com.kameecoding.filebot.enums.ResultType;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenameResult {

    File oldFile;
    File newFile;
    ResultType result;
    List<String> reasons = new ArrayList<>();

    RenameResult() {}

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

    public List<String> getReasons() {
        return Collections.unmodifiableList(reasons);
    }

    public ResultType getResult() {
        return result;
    }

    public boolean isSuccess() {
        return ResultType.SUCCESS == result;
    }
}
