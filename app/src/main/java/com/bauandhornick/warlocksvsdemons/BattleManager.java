package com.bauandhornick.warlocksvsdemons;

import java.io.Serializable;

/**
 * Created by Thomas on 4/18/2017.
 */

public class BattleManager implements Serializable {

    enum Difficulty{NOVICE, APPRENTICE, ADVENT, EXPERT }

    private int health;
    private int mana;
    private int round;
    private Difficulty difficulty;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public BattleManager(int health, int mana, int round, Difficulty difficulty) {
        this.health = health;
        this.mana = mana;
        this.round = round;
        this.difficulty = difficulty;
    }
}
