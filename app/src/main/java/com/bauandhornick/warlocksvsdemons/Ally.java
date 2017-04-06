package com.bauandhornick.warlocksvsdemons;

import android.graphics.Bitmap;

/**
 * Created by Thomas on 4/3/2017.
 */

public class Ally extends Character {
    Weapon weapon;
    private int costToBuy;


    public Ally(int pos_x, int pos_y, int width, int height, int damage, int requireFlip,
                Bitmap appearance, String weakness, String affinity, int costToBuy, Weapon weapon) {
        super(pos_x, pos_y, width, height, damage, requireFlip, appearance, weakness, affinity);
        this.weapon = weapon;
        /*this.typeOfWeapon = typeOfWeapon;
        this.weaponRange = weaponRange;
        this.weaponAffinity = weaponAffinity;
        this.weaponSpeed = weaponSpeed;
        this.rechargeRate = rechargeRate;*/
        this.costToBuy = costToBuy;
        //this.areaOfEffect = areaOfEffect;
    }



    public int getCostToBuy() {
        return costToBuy;
    }

    public void setCostToBuy(int costToBuy) {
        this.costToBuy = costToBuy;
    }



}
