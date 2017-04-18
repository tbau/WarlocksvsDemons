package com.bauandhornick.warlocksvsdemons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by Thomas on 3/24/2017.
 */

public class BattleFieldView extends View {

    int currentWidth;
    int currentHeight;

    Paint paint;
    Bitmap bitmapTemp;
    Bitmap bitMap[][];

    Bitmap enemyBitmapTemp;
    List<Enemy> enemyList;

    Bitmap backgroundBitmap;
    Bitmap towerBitmap;

    Context context;
    Random rand;

    HashMap<Integer, Weapon> weapons;

    HashMap<Integer,EnemyAttributes> enemyAttributesList;

    List <Character> alliesAndEnemies;
    animateEnemies enemyThread;

    public BattleFieldView(Context context) {
        super(context);
        setUp(null);
        this.context = context;
    }

    public BattleFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setUp(attrs);
    }

    public BattleFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setUp(attrs);
    }

    public void setUp(AttributeSet attrs){

        paint = new Paint();
        paint.setColor(0xff659d32);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);


        DisplayMetrics dm = getResources().getDisplayMetrics();

        currentWidth=dm.widthPixels;
        currentHeight= dm.heightPixels;

        initializeEnemyAttributes();


        //float widthPixel= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,currentWidth,dm);
        //float heightPixel= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,currentHeight,dm);

        bitmapTemp = BitmapFactory.decodeResource(getResources(),R.drawable.dg_classm32);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp,800,1100,false);

        bitMap = new Bitmap[11][8];

        for(int i=0;i<11;i++) {
            for(int j=0;j<8;j++)
                bitMap[i][j] = Bitmap.createBitmap(bitmapTemp, 100*j, 100*i, 100, 1000 / 11);
        }

        bitmapTemp = BitmapFactory.decodeResource(getResources(),R.drawable.path2);
        backgroundBitmap = Bitmap.createScaledBitmap(bitmapTemp,currentWidth,currentHeight,false);

        bitmapTemp = BitmapFactory.decodeResource(getResources(),R.drawable.torremagica5);
        towerBitmap = Bitmap.createScaledBitmap(bitmapTemp, 250, 250, false);

        bitmapTemp = BitmapFactory.decodeResource(getResources(),R.drawable.dg_undead32);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp,800,1200,false);

        enemyList = new ArrayList<>();

        for(int i=0;i<17;i++) {
            enemyBitmapTemp = Bitmap.createBitmap(bitmapTemp, 500, 600, 100, 200);

        //enemyList.add(new Enemy(0, (int) (currentHeight*2/13.0),0,enemyBitmapTemp,));
        }

        alliesAndEnemies = new ArrayList<>();

        rand = new Random();
        for(int i=0;i<1;i++){
            alliesAndEnemies.add(new Ally(0, (int) (currentHeight * 2 / 13.0), bitMap[rand.nextInt(10)][rand.nextInt(7)],null , null, this));

        }
        bitmapTemp=null;

        enemyThread = new animateEnemies(this);
        enemyThread.execute();
        //Canvas canvas = new Canvas(bitmap);

        //drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());

        //drawable.draw(canvas);


    }
    public void initializeEnemyAttributes(){
        enemyAttributesList = new HashMap<>();

        enemyAttributesList.put(0,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.ICE,100,500,100)); //Skeleton with long sword
        enemyAttributesList.put(1,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.ICE,200,700,300)); //Skeleton with shield/sword
        enemyAttributesList.put(2,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.ICE,300,900,500)); //Skelton with double swords;

        enemyAttributesList.put(3,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,400,1200,800)); //Green orc with sheild and axe
        enemyAttributesList.put(5,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,500,1500,1000)); //Green orc yellow helmet and sword
        enemyAttributesList.put(6,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,800,1800,1200)); //Green orc with brown coat and sword

        enemyAttributesList.put(7,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.FIRE,1000,2000,1500)); //Red troll golden shield and sword
        enemyAttributesList.put(8,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.FIRE,1000,2000,1500)); //Red troll with sword

        enemyAttributesList.put(9,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.LIGHTNING,1200,2000,1600)); //Red floating skeleton
        enemyAttributesList.put(10,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.ICE,1200,2000,1600)); //Purple floating skeleton
        enemyAttributesList.put(11,new EnemyAttributes(0, Character.Element.ICE, Character.Element.FIRE,1200,2000,1600)); //Black floating skeleton

        enemyAttributesList.put(12,new EnemyAttributes(0, Character.Element.ICE, Character.Element.FIRE,1600,2500,1800)); //Fire fairy
        enemyAttributesList.put(12,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.LIGHTNING,1600,2500,1800)); //Lightning fairy
        enemyAttributesList.put(12,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.ICE,1600,2500,1800)); //Ice fairy

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawPaint(paint);

        canvas.drawBitmap(backgroundBitmap,0,0,paint);

        //for(int i=0;i<11;i++) {
            //for (int j = 0; j < 8; j++)
              // canvas.drawBitmap(bitMap[i][j],j*100,i*100,paint);

        //}
        Matrix m = new Matrix();


        //m.setRotate(80,50,50);
        m.setTranslate(500,500);
        //m.setScale(-1,1,50,50);

        if(bitmapTemp!=null) {
            canvas.drawBitmap(bitmapTemp, m, paint);
            }

        //canvas.drawBitmap(towerBitmap,m, paint);

        for(int i=alliesAndEnemies.size()-1;i>=0;i--){
            m.setTranslate(alliesAndEnemies.get(i).getPos_x(),alliesAndEnemies.get(i).getPos_y());
            canvas.drawBitmap(alliesAndEnemies.get(i).getAppearance(),m, paint);
        }

//        canvas.drawBitmap(bitmap2,0,0,paint);

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
     //   currentWidth = w;
     //   currentHeight = h;
    }
    private class animateEnemies extends AsyncTask<Void,Integer,Void>{
        private BattleFieldView context;

        public animateEnemies(BattleFieldView context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            int i=0;
            while(true){

            for(Character character:alliesAndEnemies){

             character.animate();

            }
                publishProgress(i);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if(false)
                  break;
              i++;
              if(i%5==0) {
                  alliesAndEnemies.add(new Ally(0, (int) (currentHeight * 2 / 13.0), bitMap[rand.nextInt(10)][rand.nextInt(7)],null , null, context));
              }
          }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]%5000==0)
                Toast.makeText(context.getContext().getApplicationContext(),alliesAndEnemies.size()+"",Toast.LENGTH_SHORT).show();
            invalidate();
        }
    }

    public void initializeWeapons(HashMap<Integer, Weapon> weapons)
    {
        this.weapons.put(0, new Weapon(0,0,0,0,0.0,"10"));
    }

}
