package com.bauandhornick.warlocksvsdemons;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Thomas on 4/3/2017.
 */

public class Enemy extends Character {

    EnemyAttributes ea;

    private BattleFieldView context;
    public Enemy(int pos_x, int pos_y,Bitmap appearance,EnemyAttributes ea, BattleFieldView context) {
        super(pos_x, pos_y,appearance);
        this.ea = ea;
        this.context=context;
    }

    @Override
    public void animate() {
        int x = getPos_x();
        int y = getPos_y();

        setPos_x(getPos_x()+1);
        setPos_y(getPos_y()+1);
    }
}
