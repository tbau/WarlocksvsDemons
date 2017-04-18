package com.bauandhornick.warlocksvsdemons;

import android.graphics.Bitmap;

/**
 * Created by Thomas on 4/11/2017.
 */

public class EnemyAttributes {
    private int requireFlip;
    private Character.Element weakness;
    private Character.Element affinity;
    private int health;
    private int manaGain;
    private int damage;

    public EnemyAttributes(int requireFlip, Character.Element weakness, Character.Element affinity, int health, int manaGain, int damage) {
        this.requireFlip = requireFlip;
        this.weakness = weakness;
        this.affinity = affinity;
        this.health = health;
        this.manaGain = manaGain;
        this.damage = damage;
    }

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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
