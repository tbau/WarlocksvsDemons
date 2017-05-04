package com.bauandhornick.warlocksvsdemons;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Thomas on 3/24/2017.
 */

public class BattleFieldView extends View implements View.OnTouchListener {

    MainActivity mainContext;

    Object lock;

    BattleManager bm;

    Context context;  // Used to get View width and height in background thread;
    Random rand;      // Used to add a random enemy to list of enemies;

    int currentWidth;    //Width of View
    int currentHeight;   //Height of View

    ColorFilter filter[];

    Paint paint;         //Used to draw on Canvas

    HashMap<Integer,EnemyAttributes> enemyAttributesList;
    HashMap<Integer,AllyAttributes>  allyAttributesList;

    HashMap <Integer, FilePosition> enemyIndexes;
    HashMap <Integer, FilePosition> allyIndexes;
    HashMap <Integer, FilePosition> weaponIndexes;

    List <Enemy> availableEnemyList; //Holds list of enemies to choose from when creating a new enemy on screen
    List <Ally>  availableAllyList; //Holds list of allies to choose from when creating a new ally on screen
    List <Weapon> weaponList;

    List<Enemy> enemyQueue;
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

    Matrix flipMatrix;

    animateGame gameThread;

    int selected=0;

    Ally tempAlly;

    public void setMainContext(MainActivity mainContext){
        this.mainContext = mainContext;
    }
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

        lock = new Object();

        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(40);
        paint.setAntiAlias(true);

        selected=-1;

        DisplayMetrics dm = getResources().getDisplayMetrics();

        //Get current width and height of screen
        currentWidth=dm.widthPixels;
        currentHeight= dm.heightPixels;


        // bm = new BattleManager(10000,3000,0, BattleManager.Difficulty.NOVICE);

        //Battle Manager initializes and manages the player's health, mana, round, and difficulty level.
        bm = new BattleManager(10000,3000,0, BattleManager.Difficulty.NOVICE);

        enemyAttributesList = new HashMap<>();
        allyAttributesList = new HashMap<>();
        weaponIndexes = new HashMap<>();

        //initialize the Enemy and Ally attributes
        initializeEnemyAttributes();
        initializeAllyAttributes();

        //Initialize the file/sprite sheet information for enemies, allies, and weapons.
        enemyIndexes = new HashMap<>();
        allyIndexes = new HashMap<>();
        weaponIndexes=new HashMap<>();
        initializeEnemyIndexes();
        initializeAllyIndexes();

        availableEnemyList = new ArrayList<>(); //A list of all available enemies
        availableAllyList = new ArrayList<>(); // A list of all available allies
        weaponList = new ArrayList<>(); //A list of weapons
        projectileList = new ArrayList<>(); //A list of projectiles

        enemyQueue = new ArrayList<>();
        enemiesInBattle = new ArrayList<>(); //Stores all enemies currently in battle.
        alliesInBattle = new ArrayList<>(); //Stores all allies currently in battle.

        //float widthPixel= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,currentWidth,dm);
        //float heightPixel= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,currentHeight,dm);

        dg_classm32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_classm32new); // Load bitmap for some allies
        dg_classm32Bitmap = Bitmap.createScaledBitmap(dg_classm32Bitmap,800,1100,false);

        dg_humans32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_humans32); // Load bitmap for some allies
        dg_humans32Bitmap = Bitmap.createScaledBitmap(dg_humans32Bitmap,700,600,false);

        dg_effects32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_effects32); // Load bitmap for some allies
        dg_effects32Bitmap = Bitmap.createScaledBitmap(dg_effects32Bitmap,1200,1100,false);

        //Initialize weapon file information and attributes
        initializeWeaponIndexes();
        initializeWeapons();

        //flipMatrix is used to flip Enemies horizontally
        flipMatrix = new Matrix();
        flipMatrix.setScale(-1,1);

        //This loop loads all of the Allies into memory using allyIndexes and Attributes, and stores it in availableAllyList.
        for(int i=0;i<12;i++) {

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

        backgroundBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.path2); //Load bitmap for the Grass Path background
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap,currentWidth,currentHeight,false);

        rand = new Random();


        //This loops loads all enemies into memory, using enemeyIndex and enemyAttributesList, and stores it in availableEnemyList.
        for(int i=0;i<17;i++) {

            if(enemyIndexes.get(i).getFileNames().equals(FilePosition.FileNames.DG_UNIQUES32)){
                Bitmap temp = Bitmap.createBitmap(dg_uniques32Bitmap, 100 * enemyIndexes.get(i).getCol(),
                        100 * enemyIndexes.get(i).getRow(), 100, 100);

                if(enemyAttributesList.get(i).getRequireFlip()==1) {
                    temp = Bitmap.createBitmap(temp,0,0,100,100,flipMatrix,false);
                }

                availableEnemyList.add(new Enemy(0, (int) (currentHeight*2/13.0),temp,enemyAttributesList.get(i),this));
            }
            else if(enemyIndexes.get(i).getFileNames().equals(FilePosition.FileNames.DG_UNDEAD32)){
                Bitmap temp = Bitmap.createBitmap(dg_undead32Bitmap, 100 * enemyIndexes.get(i).getCol(),
                        100 * enemyIndexes.get(i).getRow(), 100, 100);

                if(enemyAttributesList.get(i).getRequireFlip()==1) {
                    temp = Bitmap.createBitmap(temp,0,0,100,100,flipMatrix,false);
                }

                availableEnemyList.add(new Enemy(0, (int) (currentHeight*2/13.0),temp,enemyAttributesList.get(i),this));
            }

            else if(enemyIndexes.get(i).getFileNames().equals(FilePosition.FileNames.DG_MONSTER632)){
                Bitmap temp = Bitmap.createBitmap(dg_monster632Bitmap, 100 * enemyIndexes.get(i).getCol(),
                        100 * enemyIndexes.get(i).getRow(), 100, 100);

                if(enemyAttributesList.get(i).getRequireFlip()==1) {
                    temp = Bitmap.createBitmap(temp,0,0,100,100,flipMatrix,false);
                }

                availableEnemyList.add(new Enemy(0, (int) (currentHeight*2/13.0),temp,enemyAttributesList.get(i),this));
            }

            else if(enemyIndexes.get(i).getFileNames().equals(FilePosition.FileNames.DG_CLASSM32)){
                Bitmap temp = Bitmap.createBitmap(dg_classm32Bitmap, 100 * enemyIndexes.get(i).getCol(),
                        100 * enemyIndexes.get(i).getRow(), 100, 100);

                if(enemyAttributesList.get(i).getRequireFlip()==1) {
                    temp = Bitmap.createBitmap(temp,0,0,100,100,flipMatrix,false);
                }

                availableEnemyList.add(new Enemy(0, (int) (currentHeight*2/13.0),temp,enemyAttributesList.get(i),this));
            }

        }
        filter = new ColorFilter[4];
        filter[0] = new LightingColorFilter(Color.RED,0);
        filter[1] = new LightingColorFilter(Color.CYAN,0);
        filter[2] = new LightingColorFilter(Color.YELLOW,0);
        filter[3] = new LightingColorFilter(Color.WHITE,0);

        this.setOnTouchListener(this);
    }

    /* Initialize all Enemy Attributes to show affinity,
    weakness, and health, mana, damage requireFlip info in a HashMap */
    public void initializeEnemyAttributes(){
        enemyAttributesList = new HashMap<>();

        //id, requireFlip, weakness, affinity, health, manaGain, damage;
        enemyAttributesList.put(0,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.ICE,100,500,100)); //Skeleton with long sword
        enemyAttributesList.put(1,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.ICE,500,1000,300)); //Skeleton with shield/sword
        enemyAttributesList.put(2,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.ICE,1000,2000,500)); //Skelton with double swords;
        enemyAttributesList.put(3,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,2000,3000,800)); //Green orc with sheild and axe
        enemyAttributesList.put(4,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,4000,5000,1000)); //Green orc yellow helmet and sword
        enemyAttributesList.put(5,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,8000,7000,1200)); //Green orc with brown coat and sword
        enemyAttributesList.put(6,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,12000,10000,1200)); //Troll with Shield & Battle Axe
        enemyAttributesList.put(7,new EnemyAttributes(0,Character.Element.ICE, Character.Element.FIRE,15000,15000,1200)); //Troll with cross shield and sword
        enemyAttributesList.put(8,new EnemyAttributes(1,Character.Element.ICE, Character.Element.FIRE,20000,20000,1200)); //Troll with cross shield & mace
        enemyAttributesList.put(9,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.FIRE,25000,30000,1500)); //Red troll golden shield and sword
        enemyAttributesList.put(10,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.FIRE,25000,45000,1500)); //Red troll with sword
        enemyAttributesList.put(11,new EnemyAttributes(0, Character.Element.FIRE, Character.Element.LIGHTNING,30000,60000,1600)); //Red floating skeleton
        enemyAttributesList.put(12,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.ICE,30000,70000,1600)); //Purple floating skeleton
        enemyAttributesList.put(13,new EnemyAttributes(0, Character.Element.ICE, Character.Element.FIRE,45000,80000,1600)); //Black floating skeleton
        enemyAttributesList.put(14,new EnemyAttributes(0, Character.Element.ICE, Character.Element.FIRE,45000,100000,1800)); //Fire fairy
        enemyAttributesList.put(15,new EnemyAttributes(1, Character.Element.FIRE, Character.Element.LIGHTNING,50000,100000,1800)); //Lightning fairy
        enemyAttributesList.put(16,new EnemyAttributes(0, Character.Element.LIGHTNING, Character.Element.ICE,55000,10000,1800)); //Ice fairy

    }

    /* Initialize all Ally Attributes, including the Ally name, whether it require a flip,
        the Ally type weakness, the Ally type affinity, and the cost to buy the ally.
     */
    private void initializeAllyAttributes() {
        allyAttributesList = new HashMap<>();

        //Name, requireFlip, Weakness, Affinity, cost
        allyAttributesList.put(0, new AllyAttributes("Novice Fire Mage", 1, Character.Element.LIGHTNING, Character.Element.FIRE,3000));
        allyAttributesList.put(1, new AllyAttributes("Novice Ice Mage", 1, Character.Element.FIRE, Character.Element.ICE,3000));
        allyAttributesList.put(2, new AllyAttributes("Novice Lightning Mage", 0, Character.Element.ICE, Character.Element.LIGHTNING,3000));
        allyAttributesList.put(3, new AllyAttributes("Apprentice Fire Mage", 0, Character.Element.LIGHTNING, Character.Element.FIRE,12000));
        allyAttributesList.put(4, new AllyAttributes("Apprentice Ice Mage", 0, Character.Element.FIRE, Character.Element.ICE,12000));
        allyAttributesList.put(5, new AllyAttributes("Apprentice Lightning Mage", 0, Character.Element.ICE, Character.Element.LIGHTNING,12000));
        allyAttributesList.put(6, new AllyAttributes("Advent Fire Mage", 0, Character.Element.LIGHTNING, Character.Element.FIRE,48000));
        allyAttributesList.put(7, new AllyAttributes("Advent Ice Mage", 0, Character.Element.FIRE, Character.Element.ICE,48000));
        allyAttributesList.put(8, new AllyAttributes("Advent Lightning Mage", 0, Character.Element.ICE, Character.Element.LIGHTNING,48000));
        allyAttributesList.put(9, new AllyAttributes("Expert Fire Mage", 0, Character.Element.LIGHTNING, Character.Element.FIRE,192000));
        allyAttributesList.put(10, new AllyAttributes("Expert Ice Mage", 0, Character.Element.FIRE, Character.Element.ICE,192000));
        allyAttributesList.put(11, new AllyAttributes("Expert Lightning Mage", 0, Character.Element.ICE, Character.Element.LIGHTNING,192000));
    }

    /* This function initializes all the weapon indexes, it sends the row and column and file name of the sprite sheet
    where the projectile is located.
     */
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

    /*
        This function creates the bitmaps for the projectiles using the Indexes initialized in the function above, and it creates them
        from the projectile sprite sheets.
        It sends other weapon info like range, affinity, speed, damage, etc.
     */
    public void initializeWeapons()
    {
        //weaponAppearance, weaponRange, weaponAffinity, weaponSpeed, rechargeRate, areaOfEffect, damage
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(0).getCol(),
                100*weaponIndexes.get(0).getRow(),100,100), Character.Element.FIRE,50,1250,8,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(1).getCol(),
                100*weaponIndexes.get(1).getRow(),100,100), Character.Element.ICE,50,1250,8,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(2).getCol(),
                100*weaponIndexes.get(2).getRow(),100,100), Character.Element.LIGHTNING,50,1250,8,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(3).getCol(),
                100*weaponIndexes.get(3).getRow(),100,100), Character.Element.FIRE,200,750,14,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(4).getCol(),
                100*weaponIndexes.get(4).getRow(),100,100), Character.Element.ICE,200,750,14,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(5).getCol(),
                100*weaponIndexes.get(5).getRow(),100,100), Character.Element.LIGHTNING,200,750,14,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(6).getCol(),
                100*weaponIndexes.get(6).getRow(),100,100), Character.Element.FIRE,2000,1000,20,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(7).getCol(),
                100*weaponIndexes.get(7).getRow(),100,100), Character.Element.ICE,2000,1000,20,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(8).getCol(),
                100*weaponIndexes.get(8).getRow(),100,100), Character.Element.LIGHTNING,2000,1000,20,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(9).getCol(),
                100*weaponIndexes.get(9).getRow(),100,100), Character.Element.FIRE,10000,500,25,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(10).getCol(),
                100*weaponIndexes.get(10).getRow(),100,100), Character.Element.ICE,10000,500,25,10,"no"));
        weaponList.add(new Weapon(Bitmap.createBitmap(dg_effects32Bitmap, 100*weaponIndexes.get(11).getCol(),
                100*weaponIndexes.get(11).getRow(),100,100), Character.Element.LIGHTNING,10000,500,25,10,"no"));

     /* Bitmap weaponAppearance, Character.Element weaponAffinity, int damage, int weaponRange,
     double rechargeRate, int weaponSpeed,  String areaOfEffect)
       */

    }

    /*
    This function initializes the Enemy Indexes with info about row and column in the particular Sprite Sheet file.
     */
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

    /*
    This function initializes the Ally Indexes with info about row and column in the particular Sprite Sheet file.
     */
    private void initializeAllyIndexes() {

        //These Ally IDs correspond with the IDs in the AllyAttributes list.
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

        canvas.drawBitmap(backgroundBitmap,0,0,paint); //Draw grass path background

        //Draw all allies in BattleField at their coordinates.
        for (Ally ally : alliesInBattle) {
            canvas.drawBitmap(ally.getAppearance(), ally.getPos_x(), ally.getPos_y(), paint);
        }
        //For each enemy, draw on the canvas. Depending on direction facing, it may need to be flipped using flipMatrix
    
        synchronized (lock){
        for(int p= enemiesInBattle.size()-1;p>=0;p--){
            int colorFilter=enemiesInBattle.get(p).getColorFilter();
            if(colorFilter>=0&&colorFilter<4)
                paint.setColorFilter(filter[colorFilter]);
            if(enemiesInBattle.get(p).directionFacing!= Character.Direction.LEFT)
                canvas.drawBitmap(enemiesInBattle.get(p).getAppearance(),
                        enemiesInBattle.get(p).getPos_x(),enemiesInBattle.get(p).getPos_y(),paint);

            else{
                flipMatrix.setScale(-1,1);
                flipMatrix.postTranslate(enemiesInBattle.get(p).getPos_x(),
                        enemiesInBattle.get(p).getPos_y());

                canvas.drawBitmap(enemiesInBattle.get(p).getAppearance(),flipMatrix,paint);
            }
        }}
        paint.setColorFilter(null);

        synchronized (lock) {
            for (int i=0;i< projectileList.size();i++) {
                canvas.drawBitmap(projectileList.get(i).getWeapon().getWeaponAppearance(), projectileList.get(i).getX(),
                        projectileList.get(i).getY(), paint);
            }
        }

//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "ComingSoon.ttf");
//        Typeface bold = Typeface.create(tf, Typeface.BOLD);
//        paint.setTypeface(bold);
        canvas.drawText("Level: "+bm.getRound(),currentWidth-320,225,paint);
        canvas.drawText("Health: "+bm.getHealth(),currentWidth-320,275,paint);
        canvas.drawText("Mana: "+bm.getMana(),currentWidth-320,315,paint);
        if(tempAlly!=null)
            canvas.drawBitmap(tempAlly.getAppearance(),tempAlly.getPos_x(),tempAlly.getPos_y(),paint);


        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        canvas.drawRect(10, (int)(currentHeight*7.8/9.0),110,(int)(currentHeight*7.9/9.0)+(int)(currentHeight/20.0),paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(60);
        canvas.drawText(">>",30,(int)(currentHeight*8.2/9.0),paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // currentWidth = w;
        // currentHeight = h;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(event.getAction()){

            case MotionEvent.ACTION_DOWN:
                int x = (int)event.getX();
                int y = (int)event.getY();
                if(selected!=-1&&bm.getMana()>= availableAllyList.get(selected).getAa().getCostToBuy() *(((bm.getRound()+5)/5.0))){
                    tempAlly= new Ally(availableAllyList.get(selected),(int)event.getX()-50,(int)event.getY()-50,selected);
                    invalidate();}
                else if(x >10&&y> (int)(currentHeight*7.8/9.0)&&x<110&&y<(int)(currentHeight*7.8/9.0)+50){
                    if(gameThread!=null)
                    gameThread.speed=5;
                }
                else if(selected!=1)
                {
                    for(int i=alliesInBattle.size()-1;i>=0;i--){
                        if(x>alliesInBattle.get(i).getPos_x()&&x<alliesInBattle.get(i).getPos_x()+100&&
                                y>alliesInBattle.get(i).getPos_y()&&y<alliesInBattle.get(i).getPos_y()+100)
                        {
                            //When an Ally on the BattleField has been touched, make a dialog that displays its info and sacrifice info.
                            final Dialog dialog = new Dialog(mainContext);
                            dialog.setContentView(R.layout.character_popup);

                            dialog.findViewById(R.id.root).setBackgroundColor(0xff000000);
                            TextView tv = (TextView) dialog.findViewById(R.id.ally_name);
                            tv.setText(alliesInBattle.get(i).getAa().getName());
                            ImageView im = (ImageView) dialog.findViewById(R.id.ally_bitmap);
                            im.setImageDrawable(new BitmapDrawable(getResources(),alliesInBattle.get(i).getAppearance()));
                            tv = (TextView) dialog.findViewById(R.id.affinity);
                            tv.setText("AFFINITY: " + alliesInBattle.get(i).getAa().getAffinity());
                            tv = (TextView) dialog.findViewById(R.id.weakness);
                            tv.setText("DAMAGE: " + alliesInBattle.get(i).getWeapon().getDamage());
                            tv = (TextView) dialog.findViewById(R.id.costToBuy);

                            //Sacrifice value will change, but the player will earn mana from sacrificing an Ally.
                            if(bm.getRound()>1)
                                tv.setText("SACRIFICE FOR: "+ (int)(0.5*(alliesInBattle.get(i).getAa().getCostToBuy() *((bm.getRound()+5)/5.0))));
                            else
                                tv.setText("SACRIFICE FOR: "+(int)(0.5*alliesInBattle.get(i).getAa().getCostToBuy()));

                            //Cancel Button Listener.
                            Button b = (Button) dialog.findViewById(R.id.cancel_button);
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });

                            final int id = v.getId();
                            final int index = i;
                            b = (Button) dialog.findViewById(R.id.addButton);
                            b.setText("Sacrifice");

                            //When addButton is selected to Sacrifice an Ally, add the calculated mana back to the user's total mana.
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(bm.getRound()>1)
                                        bm.setMana((int)(0.5*(alliesInBattle.get(index).getAa().getCostToBuy() *((bm.getRound()+5)/5.0))+bm.getMana()));
                                    else
                                        bm.setMana((int)(0.5*alliesInBattle.get(index).getAa().getCostToBuy()+bm.getMana()));
                                    alliesInBattle.remove(index);
                                    invalidate();

                                    dialog.cancel();
                                }
                            });
                            dialog.show();
                            break;
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(selected!=-1&&bm.getMana()>= availableAllyList.get(selected).getAa().getCostToBuy() *(((bm.getRound()+5)/5.0))){
                    tempAlly= new Ally(availableAllyList.get(selected),(int)event.getX()-50,(int)event.getY()-50,selected);
                    invalidate();}
                return true;
            case MotionEvent.ACTION_UP:
                if(selected!=-1&&bm.getMana()>= availableAllyList.get(selected).getAa().getCostToBuy() *(((bm.getRound()+5)/5.0))){
                    alliesInBattle.add(tempAlly);
                    bm.setMana(bm.getMana()-(int)(availableAllyList.get(selected).getAa().getCostToBuy()*(((bm.getRound()+5)/5.0))));
                    selected=-1;
                    tempAlly=null;
                    invalidate();}
                if(gameThread!=null)
                    gameThread.speed=50;
                return true;
        }
        return false;
    }

    /* animateGame is a class that extends AsyncTask that manages how everything is animated */
    public class animateGame extends AsyncTask<Object, Object, Void> {

        public boolean paused= false;
        public boolean done = false;
        public int speed= 50;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Hide the START ROUND Button from the BattleFieldView.
            Button b = (Button) mainContext.findViewById(R.id.start_button);
            b.setVisibility(INVISIBLE);

        }

        private BattleFieldView context;

        public animateGame(BattleFieldView context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Object... params) {
            double i=0;

            //Increases the round
            bm.setRound(bm.getRound()+1);

            //Sets up the queue of enemies
            generateRound();

            //done and paused variables are used to keep the
            //thread running when the app is paused or closed

            while(!done){
            while(!paused){

                    synchronized (lock){
                        for(int j=0; j<enemiesInBattle.size();j++){

                            enemiesInBattle.get(j).animate();
                            enemiesInBattle.get(j).setColorFilter(3);

                            //Goes through all of the projectiles
                            for(int m=0;m<projectileList.size();m++){
                                int x=projectileList.get(m).getX();
                                int y=projectileList.get(m).getY();

                                //Suppose to remove projectiles that are homing in on a dead enemy
                                if(projectileList.get(m).getEnemy()==null) {
                                    projectileList.remove(m);
                                }
                                //Checks if a projectile is at the enemy
                                if(x>enemiesInBattle.get(j).getPos_x()-20&&x<enemiesInBattle.get(j).getPos_x()+120&&
                                        y>enemiesInBattle.get(j).getPos_y()-20&&y<enemiesInBattle.get(j).getPos_y()+120){

                                    //Damage the enemy
                                    enemiesInBattle.get(j).setHealth(enemiesInBattle.get(j).getHealth()-projectileList.get(m).getWeapon().getDamage());

                                    //Make the color filter of the enemy match what it was hit with
                                    enemiesInBattle.get(j).setColorFilter(projectileList.get(m).getWeapon().getWeaponAffinity().ordinal());
                                    projectileList.remove(m);

                                    //Removes enemies that have negative health
                                    if(enemiesInBattle.get(j).getHealth()<=0){
                                        bm.setMana(bm.getMana()+enemiesInBattle.get(j).getEa().getManaGain());
                                        enemiesInBattle.remove(j);
                                    }
                                    break;
                                }

                            }
                            //If an enemy reaches the end, take away health
                            if(enemiesInBattle.size()>0&&j<enemiesInBattle.size() && enemiesInBattle.get(j).getPos_x()>currentWidth){
                                bm.setHealth(bm.getHealth()-enemiesInBattle.get(j).getEa().getDamage());

                                enemiesInBattle.remove(j);
                            }
                        }}

                    //Creates new projectiles for every ally
                    for(int j=0;j<alliesInBattle.size();j++){
                        alliesInBattle.get(j).animate();
                    }
                    synchronized (lock) {
                        for (int j = 0; j < projectileList.size(); j++) {
                            Projectile temp = projectileList.get(j);

                            //Lifetime of projectile
                            temp.setLifetime(temp.getLifetime()-1);

                            //Remove projectile if it is too olde
                            if (temp.getEnemy() == null||temp.getLifetime()<=0) {
                                projectileList.remove(j);
                                continue;
                            }

                            //Make the projectile move depending on the enemy it is
                            //homing into
                            if (temp.getEnemy().getPos_x() > temp.getX())
                                temp.setVel_x(20);
                            else
                                temp.setVel_x(-20);

                            //Make the projectile move depending on the enemy it is
                            //homing into
                            if (temp.getEnemy().getPos_y() > temp.getY())
                                temp.setVel_y(Math.abs(temp.getY() - temp.getEnemy().getPos_y()) / 10);
                            else
                                temp.setVel_y(-Math.abs(temp.getY() - temp.getEnemy().getPos_y()) / 10);

                            temp.setX(temp.getX() + temp.getVel_x());
                            temp.setY(temp.getY() + temp.getVel_y());

                        }
                    }

                    postInvalidate();

                    //If there are no enemies in battle and no enemies in the queue, the round is over
                    if(enemiesInBattle.size()==0&&enemyQueue.size()==0){
                        projectileList.clear();
                        if(bm.getRound()%10==0)
                            bm.setHealth(bm.getHealth()+1000);
                        postInvalidate();
                        return null;
                    }
                    //If you run out of health, display a popup and reset the game
                    if(bm.getHealth()<=0){
                        projectileList.clear();
                        enemyQueue.clear();
                        enemiesInBattle.clear();
                        alliesInBattle.clear();
                        bm.setMana(3000);
                        bm.setHealth(10000);
                        bm.setRound(0);
                        postInvalidate();

                        post(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog dialog = new Dialog(mainContext);
                                dialog.setContentView(R.layout.character_popup);

                                dialog.findViewById(R.id.root).setBackgroundColor(0xff000000);
                                TextView tv = (TextView) dialog.findViewById(R.id.ally_name);
                                tv.setText("Battle lost");

                                Button b = (Button) dialog.findViewById(R.id.cancel_button);
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });

                                b.setVisibility(View.INVISIBLE);

                                b = (Button) dialog.findViewById(R.id.addButton);
                                b.setText("Restart");
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                            }
                        });
                        return null;
                    }

                    //Slow down the game depending on speed
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {


                    }

                    //Counter to keep track of when to add another enemy from the queue
                    i+=0.2;

                    //Get an enemy from the queue and add it to battle
                    if(i>=1&&enemyQueue.size()>0) {
                        Enemy enemy = new Enemy(enemyQueue.get(enemyQueue.size()-1));
                        enemiesInBattle.add(enemy);
                        enemyQueue.remove(enemyQueue.size()-1);
                        i=0;
                    }
                    postInvalidate();
                }
            }
            //Makes sure to clear everything
            if(enemiesInBattle.size()>0){
                enemiesInBattle.clear();
            }
            if(enemyQueue.size()>0)
                enemyQueue.clear();

            return null;
        }

        /* This function generates the amount of enemies for a new round, which will change as the rounds progress */
        public void generateRound(){

            //Algorithm to generate round
            Enemy enemy;

            //This is responsible for what enemies can show up
            //this round.
            //Every ten rounds, a new enemy is available.
            int i =(bm.getRound()/10)+1;

            //If the round is too high, set i to the highest,
            //so it is in bounds of the array

            if(i>availableEnemyList.size())
                i=availableEnemyList.size();

            //
            if(i==0)
                i=1;

            //Gets the mana and makes sure it is not 0
            int k = bm.getMana()+1;

            int l=10;

            //Don't know why all of this seems to give
            //good results

            //l is used to to calculate number of enemies in queue
            if(alliesInBattle.size()>0)
                l = rand.nextInt(alliesInBattle.size()+(i*alliesInBattle.size())/k)+1;

            //num is number of enemies to add.
            int num = l*i+(rand.nextInt(4)+1)*(rand.nextInt(2)+1);

            //Didn't want the player to be overly outnumbered
            //Max number of enemies depends on allies on field
            if(num>10*alliesInBattle.size())
                num=4*alliesInBattle.size();

            //Adds a random enemy to queue
            for(int count=0;count<num;count++){
                enemy = new Enemy(availableEnemyList.get(rand.nextInt(i)));
                enemyQueue.add(enemy);
            }

        }
        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Makes the start round button visible
            Button b = (Button) mainContext.findViewById(R.id.start_button);
            b.setVisibility(VISIBLE);

            //Saves everything to the file
            mainContext.saveFile();
            gameThread=null;
        }
    }

}
