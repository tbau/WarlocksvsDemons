package com.bauandhornick.warlocksvsdemons;

import android.graphics.Bitmap;

/**
 * Created by Thomas on 4/11/2017.
 */

public class AllyAttributes {

    private String name;
    private int requireFlip;
    private Character.Element weakness;
    private Character.Element affinity;
    private int costToBuy;

    public AllyAttributes(String name, int requireFlip, Character.Element weakness, Character.Element affinity, int costToBuy) {
        this.name = name;
        this.requireFlip = requireFlip;
        this.weakness = weakness;
        this.affinity = affinity;
        this.costToBuy = costToBuy;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getRequireFlip() {
        return requireFlip;
    }

    public void setRequireFlip(int requireFlip) {
        this.requireFlip = requireFlip;
    }

    public Character.Element getWeakness() {
        return weakness;
    }

    public void setWeakness(Character.Element weakness) {
        this.weakness = weakness;
    }

    public Character.Element getAffinity() {
        return affinity;
    }

    public void setAffinity(Character.Element affinity) {
        this.affinity = affinity;
    }

    public int getCostToBuy() {
        return costToBuy;
    }

    public void setCostToBuy(int costToBuy) {
        this.costToBuy = costToBuy;
    }
}
