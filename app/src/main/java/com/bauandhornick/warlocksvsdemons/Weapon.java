package com.bauandhornick.warlocksvsdemons;

/**
 * Created by Alex Hornick on 4/6/2017.
 */

public class Weapon {


    private int typeOfWeapon;
    private int weaponRange;
    private int weaponAffinity;
    private int weaponSpeed;
    private int damage;
    private double rechargeRate;
    private String areaOfEffect;

    public Weapon(int typeOfWeapon, int weaponRange, int weaponAffinity, int weaponSpeed, double rechargeRate, String areaOfEffect) {
        this.typeOfWeapon = typeOfWeapon;
        this.weaponRange = weaponRange;
        this.weaponAffinity = weaponAffinity;
        this.weaponSpeed = weaponSpeed;
        this.rechargeRate = rechargeRate;
        this.areaOfEffect = areaOfEffect;
    }

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

    public void setRechargeRate(double rechargeRate) { this.rechargeRate = rechargeRate; }

    public String getAreaOfEffect() {
        return areaOfEffect;
    }

    public void setAreaOfEffect(String areaOfEffect) {
        this.areaOfEffect = areaOfEffect;
    }

}
