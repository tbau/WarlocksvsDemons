package com.bauandhornick.warlocksvsdemons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Thomas on 4/3/2017.
 */

public class Enemy extends Character {

    private EnemyAttributes ea;
    private BattleFieldView context;
    private boolean [] atEdge;
    private int health;
    private int speedX;
    private int speedY;
    private int colorFilter;

    public Enemy(Enemy enemy){
        super(0, (int) (enemy.getContext().currentHeight*2/13.0),enemy.getAppearance());
        this.ea = enemy.getEa();
        this.context = enemy.getContext();
        atEdge = new boolean[13];
        this.health = ea.getHealth();
        this.colorFilter= 3;
    }

    public Enemy(int pos_x, int pos_y, Bitmap appearance, EnemyAttributes ea, BattleFieldView context) {
        super(pos_x, pos_y,appearance);
        atEdge = new boolean[13];
        this.ea = ea;
        this.context=context;
    }

    public Enemy(){super(0,0,null);}

    @Override
    public void animate() {
        int x = getPos_x();
        int y = getPos_y();

        if(directionFacing==Direction.RIGHT&&getPos_x()>context.currentWidth*8.0/9.3-20&&!atEdge[0]) {
            directionFacing = Direction.DOWN;
            setPos_x(getPos_x()-1);
            atEdge[0]=true;
        }

        else if(directionFacing==Direction.DOWN&&getPos_y()>context.currentHeight*2.8/7&&!atEdge[1]) {
            directionFacing = Direction.LEFT;
            setPos_x(getPos_x()+100);
            atEdge[1]=true;
        }
        else if(directionFacing==Direction.LEFT&&getPos_x()<context.currentWidth/12.0+80&&!atEdge[2]){
            directionFacing = Direction.DOWN;
            setPos_x(getPos_x()-100);
            atEdge[2]=true;
        }

        else if(directionFacing==Direction.DOWN&&getPos_y()>context.currentHeight*5.7/7&&!atEdge[3]) {
            directionFacing = Direction.RIGHT;
            setPos_y(getPos_y()-1);
            atEdge[3]=true;
        }
        else if(directionFacing==Direction.RIGHT&&getPos_x()>context.currentWidth*2/8.0&&atEdge[0]&&!atEdge[4]) {
            directionFacing = Direction.UP;
            setPos_x(getPos_x()-1);
            atEdge[4]=true;
        }

        else if(directionFacing==Direction.UP&&getPos_y()<context.currentHeight*4/7&&atEdge[4]&&!atEdge[5]) {
            directionFacing = Direction.RIGHT;
            setPos_y(getPos_y()+1);
            atEdge[5]=true;
        }

        else if(directionFacing==Direction.RIGHT&&getPos_x()>context.currentWidth*3.4/8.0&&atEdge[5]&&!atEdge[6]) {
            directionFacing = Direction.DOWN;
            setPos_x(getPos_x()-1);
            atEdge[6]=true;
        }

        else if(directionFacing==Direction.DOWN&&getPos_y()>context.currentHeight*5.7/7&&atEdge[6]&&!atEdge[7]) {
            directionFacing = Direction.RIGHT;
            setPos_y(getPos_y()-1);
            atEdge[7]=true;
        }
        else if(directionFacing==Direction.RIGHT&&getPos_x()>context.currentWidth*4.9/8.0&&atEdge[7]&&!atEdge[8]) {
            directionFacing = Direction.UP;
            setPos_x(getPos_x()-1);
            atEdge[8]=true;
        }
        else if(directionFacing==Direction.UP&&getPos_y()<context.currentHeight*4.3/7&&atEdge[8]&&!atEdge[9]) {
            directionFacing = Direction.RIGHT;
            setPos_y(getPos_y()+1);
            atEdge[9]=true;
        }

        else if(directionFacing==Direction.RIGHT&&getPos_x()>context.currentWidth*6.8/8.0&&atEdge[9]&&!atEdge[10]) {
            directionFacing = Direction.DOWN;
            setPos_x(getPos_x()-1);
            atEdge[10]=true;
        }

        else if(directionFacing==Direction.DOWN&&getPos_y()>context.currentHeight*5.7/7&&atEdge[10]&&!atEdge[11]) {
            directionFacing = Direction.RIGHT;
            setPos_y(getPos_y()-1);
            atEdge[11]=true;
        }

        if(directionFacing==Direction.RIGHT)
        {
            setPos_x(getPos_x()+20);
            speedX=20;
            speedY=0;
        }

        else if(directionFacing==Direction.LEFT) {
            setPos_x(getPos_x() - 20);
            speedX=-20;
            speedY=0;
        }

        else if(directionFacing==Direction.DOWN) {
            setPos_y(getPos_y() + 20);
            speedX=0;
            speedY=20;
        }

        else
        {
        setPos_y(getPos_y() - 20);
            speedX=0;
            speedY=-20;
        }

    }
    public EnemyAttributes getEa() {
        return ea;
    }

    public void setEa(EnemyAttributes ea) {
        this.ea = ea;
    }

    public BattleFieldView getContext() {
        return context;
    }

    public void setContext(BattleFieldView context) {
        this.context = context;
    }

    public boolean[] getAtEdge() {
        return atEdge;
    }

    public void setAtEdge(boolean[] atEdge) {
        this.atEdge = atEdge;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public int getColorFilter() {
        return colorFilter;
    }

    public void setColorFilter(int colorFilter) {
        this.colorFilter = colorFilter;
    }


}
