package com.bauandhornick.warlocksvsdemons;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Thomas on 4/3/2017.
 */

public class Ally extends Character {
    Weapon weapon;
    private BattleFieldView context;

    public Ally(int pos_x, int pos_y, int requireFlip,
                Bitmap appearance, String weakness, String affinity, int costToBuy, Weapon weapon,BattleFieldView context) {
        super(pos_x, pos_y, requireFlip, appearance, weakness, affinity);
        this.weapon=weapon;
        this.costToBuy = costToBuy;
        this.context=context;
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

    @Override
    public void animate() {
        int x = getPos_x();
        int y = getPos_y();

        if(directionFacing==Direction.RIGHT&&getPos_x()>context.currentWidth*8.0/9.3) {
            directionFacing = Direction.DOWN;
            setPos_x(getPos_x()-20);
        }

        else if(directionFacing==Direction.DOWN&&getPos_y()>context.currentHeight*3.0/7) {
            directionFacing = Direction.LEFT;
            setPos_x(getPos_x()-20);
        }


        if(directionFacing==Direction.RIGHT)
            setPos_x(getPos_x()+20);

        else if(directionFacing==Direction.LEFT)
            setPos_x(getPos_x()-20);

        else if(directionFacing==Direction.DOWN)
            setPos_y(getPos_y()+20);

        else
            setPos_y(getPos_y()-20);

    }
}
