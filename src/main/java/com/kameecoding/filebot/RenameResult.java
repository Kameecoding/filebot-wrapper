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
    String errorMessage;

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

    public String getErrorMessage() {
        return errorMessage;
    }

    void setResult(ResultType result) {
        this.result = result;
    }

    void setErrorMessage(String errorMessage) {
        this.result = ResultType.FAILURE;
        this.errorMessage = errorMessage;
    }

    public ResultType getResult() {
        return result;
    }

    public boolean isSuccess() {
        return ResultType.SUCCESS == result;
    }
}
