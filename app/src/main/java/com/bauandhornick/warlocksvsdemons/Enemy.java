package com.bauandhornick.warlocksvsdemons;

import android.graphics.Bitmap;

/**
 * Created by Thomas on 4/3/2017.
 */

public class Enemy extends Character {
    private int speed;
    private int health;
    private int manaGain;

    public Enemy(int pos_x, int pos_y, int width, int height, int damage, int requireFlip,
                 Bitmap appearance, String weakness, String affinity, int speed, int health, int manaGain) {
        super(pos_x, pos_y, width, height, damage, requireFlip, appearance, weakness, affinity);
        this.speed = speed;
        this.health = health;
        this.manaGain = manaGain;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getManaGain() {
        return manaGain;
    }

    public void setManaGain(int manaGain) {
        this.manaGain = manaGain;
    }
}
