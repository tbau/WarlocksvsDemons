package com.bauandhornick.warlocksvsdemons;

/**
 * Created by Alex Hornick on 4/18/2017.
 */

public class Projectile {
    private int x;
    private int y;
    private int vel_x;
    private int vel_y;
    private int lifetime = 30;
    private Weapon weapon;
    private Enemy enemy;

    public Projectile(int x, int y, int vel_x, int vel_y, Weapon weapon, Enemy enemy) {
        this.x = x;
        this.y = y;
        this.vel_x = vel_x;
        this.vel_y = vel_y;
        this.weapon=weapon;
        this.enemy=enemy;
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

    public int getVel_x() {
        return vel_x;
    }

    public void setVel_x(int vel_x) {
        this.vel_x = vel_x;
    }

    public int getVel_y() {
        return vel_y;
    }

    public void setVel_y(int vel_y) {
        this.vel_y = vel_y;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }
}
