package com.bauandhornick.warlocksvsdemons;

import android.graphics.Bitmap;

/**
 * Created by Thomas on 4/3/2017.
 */

public abstract class Character {
    private int pos_x;
    private int pos_y;

    private int width;
    private int height;

    private int damage;

    public Character(int pos_x, int pos_y, int width, int height, int damage, int requireFlip,
                     Bitmap appearance, String weakness, String affinity) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.requireFlip = requireFlip;
        this.appearance = appearance;
        this.weakness = weakness;
        this.affinity = affinity;
    }

    private int requireFlip;


    private Bitmap appearance;

    private String weakness;
    private String affinity;

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Bitmap getAppearance() {
        return appearance;
    }

    public void setAppearance(Bitmap appearance) {
        this.appearance = appearance;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public String getAffinity() {
        return affinity;
    }

    public void setAffinity(String affinity) {
        this.affinity = affinity;
    }

    public abstract void animate();
}
