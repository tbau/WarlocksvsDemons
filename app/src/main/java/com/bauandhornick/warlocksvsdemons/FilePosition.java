package com.bauandhornick.warlocksvsdemons;

/**
 * Created by Thomas on 4/18/2017.
 */

public class FilePosition {

    enum FileNames{
        DG_CLASSM32, DG_HUMANS32, DG_UNDEAD32, DG_UNIQUES32, DG_MONSTER632,DG_EFFECTS32
    }

    private int col;
    private int row;
    private FileNames fileNames;

    public FilePosition(int row, int col, FileNames fileNames) {
        this.col = col;
        this.row = row;
        this.fileNames = fileNames;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public FileNames getFileNames() {
        return fileNames;
    }

    public void setFileNames(FileNames fileNames) {
        this.fileNames = fileNames;
    }
}
