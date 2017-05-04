package com.bauandhornick.warlocksvsdemons;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Thomas on 4/3/2017.
 */

public class Ally extends Character implements Serializable {

    private Weapon weapon;
    private BattleFieldView context;
    private AllyAttributes aa;
    private int recharge=0;
    private int index;

    public Ally(Ally ally,int x,int y,int index){
        super(x,y,ally.getAppearance());
        this.weapon=ally.weapon;
        this.aa = ally.aa;
        this.context=ally.context;
        this.index=index;

    }
    public Ally(int pos_x, int pos_y,Bitmap appearance,AllyAttributes aa,
                Weapon weapon,BattleFieldView context) {
        super(pos_x, pos_y,appearance);
        this.weapon=weapon;
        this.aa = aa;
        this.context=context;
    }

    @Override
    public void animate() {

        if(recharge==0) {
            for (int i = 0; i < context.enemiesInBattle.size(); i++) {
                if (Math.sqrt(Math.pow((double) context.enemiesInBattle.get(i).getPos_x() - getPos_x(), 2.0) +
                        Math.pow((double) context.enemiesInBattle.get(i).getPos_y() - getPos_y(), 2.0)) < weapon.getWeaponRange()) {
                    if(context.enemiesInBattle.get(i).getPos_y()>getPos_y())
                        context.projectileList.add(new Projectile(getPos_x(), getPos_y(), context.enemiesInBattle.get(i).getSpeedX(),
                            0, weapon,context.enemiesInBattle.get(i)));
                    else
                        context.projectileList.add(new Projectile(getPos_x(), getPos_y(), context.enemiesInBattle.get(i).getSpeedX(),
                            0, weapon,context.enemiesInBattle.get(i)));

                    break;
                }
            }
        recharge=weapon.getRechargeRate();
        }
        recharge--;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public BattleFieldView getContext() {
        return context;
    }

    public void setContext(BattleFieldView context) {
        this.context = context;
    }

    public AllyAttributes getAa() {
        return aa;
    }

    public void setAa(AllyAttributes aa) {
        this.aa = aa;
    }

    public int getRecharge() {
        return recharge;
    }

    public void setRecharge(int recharge) {
        this.recharge = recharge;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
