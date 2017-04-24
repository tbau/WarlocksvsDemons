package com.bauandhornick.warlocksvsdemons;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by Thomas on 3/24/2017.
 */

public class BattleFieldView extends View {

    BattleManager bm;

    Context context;  // Used to get View width and height in background thread;
    Random rand;      // Used to add a random enemy to list of enemies;

    int currentWidth;    //Width of View
    int currentHeight;   //Height of View

    Paint paint;         //Used to draw on Canvas

    HashMap<Integer,EnemyAttributes> enemyAttributesList;
    HashMap<Integer,AllyAttributes>  allyAttributesList;

    HashMap <Integer, FilePosition> enemyIndexes;
    HashMap <Integer, FilePosition> allyIndexes;
    HashMap <Integer, FilePosition> weaponIndexes;

    List <Enemy> availableEnemyList; //Holds list of enemies to choose from when creating a new enemy on screen
    List <Ally>  availableAllyList; //Holds list of allies to choose from when creating a new ally on screen
    List <Weapon> weaponList;

    List <Enemy> enemiesInBattle;  // Holds list of enemies on screen
    List <Ally> alliesInBattle;    // Holds list of allies on screen
    List <Projectile> projectileList;

    Bitmap dg_classm32Bitmap;   // Bitmap to hold dg_class32 images for allies and some enemies
    Bitmap dg_humans32Bitmap;   // Bitmap to hold dg_humans32 images for allies

    Bitmap dg_undead32Bitmap;   // Bitmap to hold dg_undead32 images for enemies
    Bitmap dg_uniques32Bitmap;  // Bitmap to hold dg_uniques32 images for enemies
    Bitmap dg_monster632Bitmap; // Bitmap to hold dg_monster632 images for enemies

    Bitmap dg_effects32Bitmap; // Bitmap to hold dg_monster632 images for enemies

    Bitmap backgroundBitmap;  // Holds the bitmap for the background

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

        bm = new BattleManager(10000,3000,1, BattleManager.Difficulty.NOVICE);

        enemyAttributesList = new HashMap<>();
        allyAttributesList = new HashMap<>();
        weaponIndexes = new HashMap<>();

        initializeEnemyAttributes();
        initializeAllyAttributes();

        enemyIndexes = new HashMap<>();
        allyIndexes = new HashMap<>();
        weaponIndexes=new HashMap<>();

        initializeEnemyIndexes();
        initializeAllyIndexes();

        availableEnemyList = new ArrayList<>();
        availableAllyList = new ArrayList<>();
        weaponList = new ArrayList<>();
        projectileList = new ArrayList<>();


        enemiesInBattle = new ArrayList<>();
        alliesInBattle = new ArrayList<>();


        //float widthPixel= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,currentWidth,dm);
        //float heightPixel= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,currentHeight,dm);

        dg_classm32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_classm32new); // Load bitmap for some allies
        dg_classm32Bitmap = Bitmap.createScaledBitmap(dg_classm32Bitmap,800,1100,false);

        dg_humans32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_humans32); // Load bitmap for some allies
        dg_humans32Bitmap = Bitmap.createScaledBitmap(dg_humans32Bitmap,700,600,false);

        dg_effects32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_effects32); // Load bitmap for some allies
        dg_effects32Bitmap = Bitmap.createScaledBitmap(dg_effects32Bitmap,1200,1100,false);

        initializeWeaponIndexes();
        initializeWeapons();

        Matrix flipMatrix = new Matrix();
        flipMatrix.setScale(-1,1);

        for(int i=0;i<12;i++) {

            Log.i("hi",i+"");

            if(allyIndexes.get(i).getFileNames().equals(FilePosition.FileNames.DG_CLASSM32)){
                Bitmap temp = Bitmap.createBitmap(dg_classm32Bitmap, 100 * allyIndexes.get(i).getCol(),
                        100 * allyIndexes.get(i).getRow(), 100, 100);

                if(allyAttributesList.get(i).getRequireFlip()==1) {
                temp = Bitmap.createBitmap(temp, 0,
                        0,100,100,flipMatrix,false);
                }

            availableAllyList.add(new Ally(0, (int) (currentHeight*2/13.0),temp,allyAttributesList.get(i),weaponList.get(i),this));
            }
            else if(allyIndexes.get(i).getFileNames().equals(FilePosition.FileNames.DG_HUMANS32)) {

                Bitmap temp = Bitmap.createBitmap(dg_humans32Bitmap, 100 * allyIndexes.get(i).getCol(),
                        100 * allyIndexes.get(i).getRow(), 100, 100);

                if(allyAttributesList.get(i).getRequireFlip()==1) {
                    temp = Bitmap.createBitmap(temp,0,0,100,100,flipMatrix,false);
                }
                availableAllyList.add(new Ally(0, (int) (currentHeight*2/13.0),temp,allyAttributesList.get(i),weaponList.get(i),this));

            }


        }

        dg_undead32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_undead32);   // Load bitmap for some enemies
        dg_undead32Bitmap = Bitmap.createScaledBitmap(dg_undead32Bitmap,700,900,false);

        dg_uniques32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_uniques32); // Load bitmap for some allies
        dg_uniques32Bitmap = Bitmap.createScaledBitmap(dg_uniques32Bitmap,1000,900,false);

        dg_monster632Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_monster632); // Load bitmap for some allies
        dg_monster632Bitmap = Bitmap.createScaledBitmap(dg_monster632Bitmap,600,1300,false);

        backgroundBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.path2);
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap,currentWidth,currentHeight,false);



        /*for(int i=0;i<17;i++) {
            enemyBitmapTemp = Bitmap.createBitmap(bitmapTemp, 500, 600, 100, 200);

        //enemyList.add(new Enemy(0, (int) (currentHeight*2/13.0),0,enemyBitmapTemp,));
        }

        alliesAndEnemies = new ArrayList<>();
*/
        rand = new Random();


        for(int i=0;i<17;i++) {

            Log.i("enemy",i+"");

            if(enemyIndexes.get(i).getFileNames().equals(FilePosition.FileNames.DG_UNIQUES32)){
                availableEnemyList.add(new Enemy(0, (int) (currentHeight*2/13.0),Bitmap.createBitmap(dg_uniques32Bitmap, 100*enemyIndexes.get(i).getCol(),
                        100*enemyIndexes.get(i).getRow(), 100, 100),enemyAttributesList.get(i),this));
            }
            else if(enemyIndexes.get(i).getFileNames().equals(FilePosition.FileNames.DG_UNDEAD32)){
                availableEnemyList.add(new Enemy(0, (int) (currentHeight*2/13.0),Bitmap.createBitmap(dg_undead32Bitmap, 100*enemyIndexes.get(i).getCol(),
                        100*enemyIndexes.get(i).getRow(), 100, 100),enemyAttributesList.get(i),this));
            }

            else if(enemyIndexes.get(i).getFileNames().equals(FilePosition.FileNames.DG_MONSTER632)){
                availableEnemyList.add(new Enemy(0, (int) (currentHeight*2/13.0),Bitmap.createBitmap(dg_monster632Bitmap, 100*enemyIndexes.get(i).getCol(),
                        100*enemyIndexes.get(i).getRow(), 100, 100),enemyAttributesList.get(i),this));
            }

            else if(enemyIndexes.get(i).getFileNames().equals(FilePosition.FileNames.DG_CLASSM32)){
                availableEnemyList.add(new Enemy(0, (int) (currentHeight*2/13.0),Bitmap.createBitmap(dg_classm32Bitmap, 100*enemyIndexes.get(i).getCol(),
                        100*enemyIndexes.get(i).getRow(), 100, 100),enemyAttributesList.get(i),this));
            }

        }

        projectileList.add(new Projectile(100,200,1,1,weaponList.get(0)));
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
        enemyAttributesList.put(4,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,500,1500,1000)); //Green orc yellow helmet and sword
        enemyAttributesList.put(5,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,800,1800,1200)); //Green orc with brown coat and sword

        enemyAttributesList.put(6,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,800,1800,1200)); //Troll with Shield & Battle Axe
        enemyAttributesList.put(7,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,800,1800,1200)); //Troll with cross shield and sword
        enemyAttributesList.put(8,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,800,1800,1200)); //Troll with cross shield & mace


        enemyAttributesList.put(9,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.FIRE,1000,2000,1500)); //Red troll golden shield and sword
        enemyAttributesList.put(10,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.FIRE,1000,2000,1500)); //Red troll with sword

        enemyAttributesList.put(11,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.LIGHTNING,1200,2000,1600)); //Red floating skeleton
        enemyAttributesList.put(12,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.ICE,1200,2000,1600)); //Purple floating skeleton
        enemyAttributesList.put(13,new EnemyAttributes(0, Character.Element.ICE, Character.Element.FIRE,1200,2000,1600)); //Black floating skeleton

        enemyAttributesList.put(14,new EnemyAttributes(0, Character.Element.ICE, Character.Element.FIRE,1600,2500,1800)); //Fire fairy
        enemyAttributesList.put(15,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.LIGHTNING,1600,2500,1800)); //Lightning fairy
        enemyAttributesList.put(16,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.ICE,1600,2500,1800)); //Ice fairy

    }
    private void initializeAllyAttributes() {
        allyAttributesList = new HashMap<>();

        //Name, requireFlip, Weakness, Affinity, cost
        allyAttributesList.put(0, new AllyAttributes("Novice Fire Mage", 1, Character.Element.LIGHTNING, Character.Element.FIRE,0));
        allyAttributesList.put(1, new AllyAttributes("Novice Ice Mage", 1, Character.Element.FIRE, Character.Element.ICE,0));
        allyAttributesList.put(2, new AllyAttributes("Novice Lightning Mage", 0, Character.Element.ICE, Character.Element.LIGHTNING,0));
        allyAttributesList.put(3, new AllyAttributes("Apprentice Fire Mage", 0, Character.Element.LIGHTNING, Character.Element.FIRE,0));
        allyAttributesList.put(4, new AllyAttributes("Apprentice Ice Mage", 0, Character.Element.FIRE, Character.Element.ICE,0));
        allyAttributesList.put(5, new AllyAttributes("Apprentice Lightning Mage", 0, Character.Element.ICE, Character.Element.LIGHTNING,0));
        allyAttributesList.put(6, new AllyAttributes("Advent Fire Mage", 0, Character.Element.LIGHTNING, Character.Element.FIRE,0));
        allyAttributesList.put(7, new AllyAttributes("Advent Ice Mage", 0, Character.Element.FIRE, Character.Element.ICE,0));
        allyAttributesList.put(8, new AllyAttributes("Advent Lightning Mage", 0, Character.Element.ICE, Character.Element.LIGHTNING,0));
        allyAttributesList.put(9, new AllyAttributes("Expert Fire Mage", 0, Character.Element.LIGHTNING, Character.Element.FIRE,0));
        allyAttributesList.put(10, new AllyAttributes("Expert Ice Mage", 0, Character.Element.FIRE, Character.Element.ICE,0));
        allyAttributesList.put(11, new AllyAttributes("Expert Lightning Mage", 0, Character.Element.ICE, Character.Element.LIGHTNING,0));


    }
    public void initializeWeaponIndexes(){
        weaponIndexes.put(0,new FilePosition(4,0, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(1,new FilePosition(4,3, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(2,new FilePosition(4,6, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(3,new FilePosition(8,0, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(4,new FilePosition(8,5, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(5,new FilePosition(8,3, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(6,new FilePosition(8,6, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(7,new FilePosition(8,11, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(8,new FilePosition(8,9, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(9,new FilePosition(5,0, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(10,new FilePosition(5,3, FilePosition.FileNames.DG_EFFECTS32));
        weaponIndexes.put(11,new FilePosition(5,6, FilePosition.FileNames.DG_EFFECTS32));
    }

    public void initializeWeapons()
    {
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(0).getCol(),
                  100*weaponIndexes.get(0).getRow(),100,100), Character.Element.FIRE,125,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(1).getCol(),
                100*weaponIndexes.get(1).getRow(),100,100), Character.Element.ICE,125,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(2).getCol(),
                100*weaponIndexes.get(2).getRow(),100,100), Character.Element.LIGHTNING,125,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(3).getCol(),
                100*weaponIndexes.get(3).getRow(),100,100), Character.Element.FIRE,250,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(4).getCol(),
                100*weaponIndexes.get(4).getRow(),100,100), Character.Element.ICE,250,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(5).getCol(),
                100*weaponIndexes.get(5).getRow(),100,100), Character.Element.LIGHTNING,250,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(6).getCol(),
                100*weaponIndexes.get(6).getRow(),100,100), Character.Element.FIRE,500,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(7).getCol(),
                100*weaponIndexes.get(7).getRow(),100,100), Character.Element.ICE,500,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(8).getCol(),
                100*weaponIndexes.get(8).getRow(),100,100), Character.Element.LIGHTNING,500,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(9).getCol(),
                100*weaponIndexes.get(9).getRow(),100,100), Character.Element.FIRE,1000,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(10).getCol(),
                100*weaponIndexes.get(10).getRow(),100,100), Character.Element.ICE,1000,0,0,10,"no"));

        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(11).getCol(),
                100*weaponIndexes.get(11).getRow(),100,100), Character.Element.LIGHTNING,1000,0,0,10,"no"));

     /* Bitmap weaponAppearance, Character.Element weaponAffinity, int damage, int weaponRange,
     double rechargeRate, int weaponSpeed,  String areaOfEffect)
       */


    }

    private void initializeEnemyIndexes() {
        enemyIndexes.put(0, new FilePosition(5, 3, FilePosition.FileNames.DG_UNDEAD32));
        enemyIndexes.put(1, new FilePosition(5, 6, FilePosition.FileNames.DG_UNDEAD32));
        enemyIndexes.put(2, new FilePosition(5, 5, FilePosition.FileNames.DG_UNDEAD32));
        enemyIndexes.put(3, new FilePosition(10, 7, FilePosition.FileNames.DG_CLASSM32));
        enemyIndexes.put(4, new FilePosition(10, 3, FilePosition.FileNames.DG_CLASSM32));
        enemyIndexes.put(5, new FilePosition(10, 5, FilePosition.FileNames.DG_CLASSM32));
        enemyIndexes.put(6, new FilePosition(1, 2, FilePosition.FileNames.DG_UNIQUES32));
        enemyIndexes.put(7, new FilePosition(1, 5, FilePosition.FileNames.DG_UNIQUES32));
        enemyIndexes.put(8, new FilePosition(1, 6, FilePosition.FileNames.DG_UNIQUES32));
        enemyIndexes.put(9, new FilePosition(1, 4, FilePosition.FileNames.DG_UNIQUES32));
        enemyIndexes.put(10, new FilePosition(4, 0, FilePosition.FileNames.DG_MONSTER632));
        enemyIndexes.put(11, new FilePosition(1, 1, FilePosition.FileNames.DG_UNDEAD32));
        enemyIndexes.put(12, new FilePosition(1, 3, FilePosition.FileNames.DG_UNDEAD32));
        enemyIndexes.put(13, new FilePosition(1, 6, FilePosition.FileNames.DG_UNDEAD32));
        enemyIndexes.put(14, new FilePosition(3, 6, FilePosition.FileNames.DG_UNIQUES32));
        enemyIndexes.put(15, new FilePosition(3, 7, FilePosition.FileNames.DG_UNIQUES32));
        enemyIndexes.put(16, new FilePosition(4, 7, FilePosition.FileNames.DG_UNIQUES32));
    }

    private void initializeAllyIndexes() {
        allyIndexes.put(0, new FilePosition(5, 1, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(1, new FilePosition(0, 2, FilePosition.FileNames.DG_HUMANS32));
        allyIndexes.put(2, new FilePosition(5, 2, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(3, new FilePosition(3, 2, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(4, new FilePosition(8, 2, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(5, new FilePosition(2, 3, FilePosition.FileNames.DG_HUMANS32));
        allyIndexes.put(6, new FilePosition(2, 2, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(7, new FilePosition(1, 2, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(8, new FilePosition(4, 2, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(9, new FilePosition(2, 4, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(10, new FilePosition(1, 4, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(11, new FilePosition(4, 4, FilePosition.FileNames.DG_CLASSM32));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(backgroundBitmap,0,0,paint);

        for (Ally ally : alliesInBattle) {
            canvas.drawBitmap(ally.getAppearance(),ally.getPos_x(),ally.getPos_y(),paint);
        }

        for(Enemy enemy: enemiesInBattle){
            canvas.drawBitmap(enemy.getAppearance(),enemy.getPos_x(),enemy.getPos_y(),paint);
        }

        for(Projectile projectile:projectileList){
            canvas.drawBitmap(projectile.getWeapon().getWeaponAppearance(),projectile.getX(),projectile.getY(),paint);
        }
        Matrix m = new Matrix();


        //m.setRotate(80,50,50);
        m.setTranslate(500,500);
        //m.setScale(-1,1,50,50);

        //canvas.drawBitmap(towerBitmap,m, paint);
/*
        for(int i=alliesAndEnemies.size()-1;i>=0;i--){
            m.setTranslate(alliesAndEnemies.get(i).getPos_x(),alliesAndEnemies.get(i).getPos_y());
            canvas.drawBitmap(alliesAndEnemies.get(i).getAppearance(),m, paint);
        }
*/
//        canvas.drawBitmap(bitmap2,0,0,paint);

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
     //   currentWidth = w;
     //   currentHeight = h;
    }
    private class animateEnemies extends AsyncTask<Void,Double,Void>{
        private BattleFieldView context;

        public animateEnemies(BattleFieldView context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            double i=0;
            int k=0;
            while(true){

            for(int j=0; j<enemiesInBattle.size();j++){

             enemiesInBattle.get(j).animate();
                //if(enemiesInBattle.get(i).getPos_x()>context.currentWidth||enemiesInBattle.get(i).getPos_x()<0)
            // postInvalidate();
            }
                publishProgress(i);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if(false)
                  break;
                i+=0.2;
              if(i>=1) {

                  Enemy enemy= new Enemy(availableEnemyList.get(k));

                  enemiesInBattle.add(enemy);


                  k++;
                 i=0;
                  if(k==17)
                      k=0;
              }
          }
            return null;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            super.onProgressUpdate(values);
            //if(values[0]%5000==0)
             //   Toast.makeText(context.getContext().getApplicationContext(),alliesAndEnemies.size()+"",Toast.LENGTH_SHORT).show();
            Log.i("enemy: ",values[0]+"");
            invalidate();
        }
    }
}
