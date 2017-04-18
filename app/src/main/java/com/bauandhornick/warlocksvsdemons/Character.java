package com.bauandhornick.warlocksvsdemons;

import android.graphics.Bitmap;

/**
 * Created by Thomas on 4/3/2017.
 */

public abstract class Character {
    private int pos_x;
    private int pos_y;

    enum Element{FIRE,ICE,LIGHTNING};

    public Bitmap getAppearance() {
        return appearance;
    }

    public void setAppearance(Bitmap appearance) {
        this.appearance = appearance;
    }

    private Bitmap appearance;

    Direction directionFacing=Direction.RIGHT;

    enum Direction{RIGHT,LEFT,UP,DOWN};


    public Character(int pos_x, int pos_y,Bitmap appearance) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.appearance=appearance;
    }
    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    public abstract void animate();
}
