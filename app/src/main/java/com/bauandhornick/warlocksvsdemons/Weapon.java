package com.bauandhornick.warlocksvsdemons;

import android.graphics.Bitmap;

/**
 * Created by Alex Hornick on 4/6/2017.
 */

public class Weapon {


    private Bitmap weaponAppearance;
    private int weaponRange;
    private int weaponAffinity;
    private int weaponSpeed;
    private int damage;
    private double rechargeRate;
    private String areaOfEffect;

    public Weapon(Bitmap weaponAppearance, int weaponRange, int weaponAffinity, int weaponSpeed, double rechargeRate, String areaOfEffect) {
        this.weaponAppearance = weaponAppearance;
        this.weaponRange = weaponRange;
        this.weaponAffinity = weaponAffinity;
        this.weaponSpeed = weaponSpeed;
        this.rechargeRate = rechargeRate;
        this.areaOfEffect = areaOfEffect;
    }

    public Bitmap getWeaponAppearance() {
        return weaponAppearance;
    }

    public void setTypeOfWeapon(Bitmap weaponAppearance) { this.weaponAppearance = weaponAppearance; }

    public int getWeaponRange() {
        return weaponRange;
    }

    public void setWeaponRange(int weaponRange) {
        this.weaponRange = weaponRange;
    }

    public int getWeaponAffinity() {
        return weaponAffinity;
    }

    public void setWeaponAffinity(int weaponAffinity) {
        this.weaponAffinity = weaponAffinity;
    }

    public int getWeaponSpeed() {
        return weaponSpeed;
    }

    public void setWeaponSpeed(int weaponSpeed) {
        this.weaponSpeed = weaponSpeed;
    }

    public double getRechargeRate() {
        return rechargeRate;
    }

    public void setRechargeRate(double rechargeRate) { this.rechargeRate = rechargeRate; }

    public String getAreaOfEffect() {
        return areaOfEffect;
    }

    public void setAreaOfEffect(String areaOfEffect) {
        this.areaOfEffect = areaOfEffect;
    }

}
