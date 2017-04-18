package com.bauandhornick.warlocksvsdemons;

/**
 * Created by Thomas on 4/18/2017.
 */

public class FilePosition {

    enum FileNames{
        DG_CLASSM32, DG_HUMANS32, DG_UNDEAD32, DG_UNIQUES32, DG_MONSTER632
    }

    private int x;
    private int y;
    private FileNames fileName;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public FileNames getFileName() {
        return fileName;
    }

    public void setFileName(FileNames fileName) {
        this.fileName = fileName;
    }

    public FilePosition(int x, int y, FileNames fileName) {
        this.x = x;
        this.y = y;
        this.fileName = fileName;
    }
}
