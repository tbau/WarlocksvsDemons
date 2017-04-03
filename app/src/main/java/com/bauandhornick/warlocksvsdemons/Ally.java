package com.bauandhornick.warlocksvsdemons;

import android.graphics.Bitmap;

/**
 * Created by Thomas on 4/3/2017.
 */

public class Ally extends Character {
    private int typeOfWeapon;
    private int weaponRange;
    private int weaponAffinity;
    private int weaponSpeed;
    private double rechargeRate;

    public Ally(int pos_x, int pos_y, int width, int height, int damage, int requireFlip,
                Bitmap appearance, String weakness, String affinity, int typeOfWeapon,
                int weaponRange, int weaponAffinity, int weaponSpeed, double rechargeRate,
                int costToBuy, String areaOfEffect) {
        super(pos_x, pos_y, width, height, damage, requireFlip, appearance, weakness, affinity);
        this.typeOfWeapon = typeOfWeapon;
        this.weaponRange = weaponRange;
        this.weaponAffinity = weaponAffinity;
        this.weaponSpeed = weaponSpeed;
        this.rechargeRate = rechargeRate;
        this.costToBuy = costToBuy;
        this.areaOfEffect = areaOfEffect;
    }

    private int costToBuy;
    private String areaOfEffect;


    public int getTypeOfWeapon() {
        return typeOfWeapon;
    }

    public void setTypeOfWeapon(int typeOfWeapon) {
        this.typeOfWeapon = typeOfWeapon;
    }

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

    public void setRechargeRate(double rechargeRate) {
        this.rechargeRate = rechargeRate;
    }

    public int getCostToBuy() {
        return costToBuy;
    }

    public void setCostToBuy(int costToBuy) {
        this.costToBuy = costToBuy;
    }

    public String getAreaOfEffect() {
        return areaOfEffect;
    }

    public void setAreaOfEffect(String areaOfEffect) {
        this.areaOfEffect = areaOfEffect;
    }

}
