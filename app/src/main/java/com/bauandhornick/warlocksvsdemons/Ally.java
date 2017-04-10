package com.bauandhornick.warlocksvsdemons;

import android.content.Context;
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
    }

    public int getCostToBuy() {
        return costToBuy;
    }

    public void setCostToBuy(int costToBuy) {
        this.costToBuy = costToBuy;
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
