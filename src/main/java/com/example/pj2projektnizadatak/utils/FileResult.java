package com.example.pj2projektnizadatak.utils;

public class FileResult {
    String fileName;
    String filePath;

    public FileResult(String fileName, String filePath){
        this.fileName=fileName;
        this.filePath=filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
