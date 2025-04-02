package com.example.myapplication;

public class TmpDesignData {
    private final String fileName;
    private final float size;

    public TmpDesignData(String fileName, float size) {
        this.fileName = fileName;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public float getSize() {
        return size;
    }
}
