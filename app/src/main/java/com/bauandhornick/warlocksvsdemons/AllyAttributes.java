package com.bauandhornick.warlocksvsdemons;

import android.graphics.Bitmap;

/**
 * Created by Thomas on 4/11/2017.
 */

public class AllyAttributes {

    private int requireFlip;
    private String weakness;
    private String affinity;
    private int costToBuy;

    public int getRequireFlip() {
        return requireFlip;
    }

    public void setRequireFlip(int requireFlip) {
        this.requireFlip = requireFlip;
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

    public int getCostToBuy() {
        return costToBuy;
    }

    public void setCostToBuy(int costToBuy) {
        this.costToBuy = costToBuy;
    }
}
