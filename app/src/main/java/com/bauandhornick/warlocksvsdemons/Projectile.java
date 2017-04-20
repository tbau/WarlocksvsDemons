package com.bauandhornick.warlocksvsdemons;

/**
 * Created by Alex Hornick on 4/18/2017.
 */

public class Projectile {
    private int x;
    private int y;
    private int velocity_y;

    public Projectile(int x, int y, int velocity_y, int velocity_x, Weapon weapon) {
        this.x = x;
        this.y = y;
        this.velocity_y = velocity_y;
        this.velocity_x = velocity_x;
        this.weapon = weapon;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVelocity_y() {
        return velocity_y;
    }

    public void setVelocity_y(int velocity_y) {
        this.velocity_y = velocity_y;
    }

    public int getVelocity_x() {
        return velocity_x;
    }

    public void setVelocity_x(int velocity_x) {
        this.velocity_x = velocity_x;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    private int velocity_x;
    private Weapon weapon;

}
