package com.bauandhornick.warlocksvsdemons;

import android.graphics.Bitmap;

/**
 * Created by Alex Hornick on 4/6/2017.
 */

public class Weapon implements Cloneable{



    private Bitmap weaponAppearance;
    private int weaponRange;
    private Character.Element weaponAffinity;
    private int weaponSpeed;
    private int damage;
    private double rechargeRate;
    private String areaOfEffect;


    public Weapon(Bitmap weaponAppearance, Character.Element weaponAffinity, int damage,
                  int weaponRange, double rechargeRate, int weaponSpeed,  String areaOfEffect) {

        this.weaponAppearance = weaponAppearance;
        this.weaponRange = weaponRange;
        this.weaponAffinity = weaponAffinity;
        this.weaponSpeed = weaponSpeed;
        this.rechargeRate = rechargeRate;
        this.areaOfEffect = areaOfEffect;
        this.damage = damage;
    }

    public Bitmap getWeaponAppearance() {
        return weaponAppearance;
    }

    public void setWeaponAppearance(Bitmap weaponAppearance) {
        this.weaponAppearance = weaponAppearance;
    }

    public int getWeaponRange() {
        return weaponRange;
    }

    public void setWeaponRange(int weaponRange) {
        this.weaponRange = weaponRange;
    }

    public Character.Element getWeaponAffinity() {
        return weaponAffinity;
    }

    public void setWeaponAffinity(Character.Element weaponAffinity) {
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

}
