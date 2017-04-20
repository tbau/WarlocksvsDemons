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

    BattleManager bm;

    Context context;  // Used to get View width and height in background thread;
    Random rand;      // Used to add a random enemy to list of enemies;

    int currentWidth;    //Width of View
    int currentHeight;   //Height of View

    Paint paint;         //Used to draw on Canvas

    HashMap<Integer,EnemyAttributes> enemyAttributesList;
    HashMap<Integer,AllyAttributes>  allyAttributesList;
    HashMap<Integer, Weapon> weapons;

    HashMap <Integer, FilePosition> enemyIndexes;
    HashMap <Integer, FilePosition> allyIndexes;

    List <Enemy> availableEnemyList; //Holds list of enemies to choose from when creating a new enemy on screen
    List <Ally>  availableAllyList; //Holds list of allies to choose from when creating a new ally on screen

    List <Enemy> enemiesInBattle;  // Holds list of enemies on screen
    List <Ally> alliesInBattle;    // Holds list of allies on screen

    Bitmap dg_classm32Bitmap;   // Bitmap to hold dg_class32 images for allies and some enemies
    Bitmap dg_humans32Bitmap;   // Bitmap to hold dg_humans32 images for allies

    Bitmap dg_undead32Bitmap;   // Bitmap to hold dg_undead32 images for enemies
    Bitmap dg_uniques32Bitmap;  // Bitmap to hold dg_uniques32 images for enemies
    Bitmap dg_monster632Bitmap; // Bitmap to hold dg_monster632 images for enemies

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

        bm = new BattleManager();

        enemyAttributesList = new HashMap<>();
        allyAttributesList = new HashMap<>();
        weapons = new HashMap<>();

        initializeEnemyAttributes();
        initializeAllyAttributes();
        initializeWeapons();

        enemyIndexes = new HashMap<>();
        allyIndexes = new HashMap<>();

        initializeEnemyIndexes();
        initializeAllyIndexes();

        availableEnemyList = new ArrayList<>();
        availableAllyList = new ArrayList<>();

        enemiesInBattle = new ArrayList<>();
        alliesInBattle = new ArrayList<>();


        //float widthPixel= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,currentWidth,dm);
        //float heightPixel= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,currentHeight,dm);

        dg_classm32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_classm32new); // Load bitmap for some allies
        dg_classm32Bitmap = Bitmap.createScaledBitmap(dg_classm32Bitmap,800,1100,false);

        dg_humans32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_humans32); // Load bitmap for some allies
        dg_humans32Bitmap = Bitmap.createScaledBitmap(dg_classm32Bitmap,600,700,false);



        for(int i=0;i<11;i++) {
            for(int j=0;j<8;j++)

                bitMap[i][j] = Bitmap.createBitmap(dg_classm32Bitmap, 100*j, 100*i, 100, 1000 / 11);
        }

        dg_undead32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_undead32);   // Load bitmap for some enemies
        dg_undead32Bitmap = Bitmap.createScaledBitmap(dg_undead32Bitmap,700,900,false);

        dg_uniques32Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_uniques32); // Load bitmap for some allies
        dg_uniques32Bitmap = Bitmap.createScaledBitmap(dg_uniques32Bitmap,1000,900,false);

        dg_monster632Bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dg_monster632); // Load bitmap for some allies
        dg_monster632Bitmap = Bitmap.createScaledBitmap(dg_monster632Bitmap,600,1300,false);

        backgroundBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.path2);
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap,currentWidth,currentHeight,false);


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
    private void initializeAllyAttributes() {
        allyAttributesList = new HashMap<>();

        //Name, requireFlip, Weakness, Affinity, cost
        allyAttributesList.put(0, new AllyAttributes("Novice Fire Mage", 0, Character.Element.LIGHTNING, Character.Element.FIRE,0));
        allyAttributesList.put(1, new AllyAttributes("Novice Ice Mage", 0, Character.Element.FIRE, Character.Element.ICE,0));
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

    public void initializeWeapons()
    {
        //weapons.put(0, new Weapon(0,0,0,0,0.0,"10"));
    }

    private void initializeEnemyIndexes() {



    }
    private void initializeAllyIndexes() {
        allyIndexes.put(0, new FilePosition(5, 6, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(1, new FilePosition(0, 2, FilePosition.FileNames.DG_HUMANS32));
        allyIndexes.put(2, new FilePosition(5, 2, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(3, new FilePosition(3, 3, FilePosition.FileNames.DG_CLASSM32));
        allyIndexes.put(4, new FilePosition(8, 2, FilePosition.FileNames.DG_CLASSM32));
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



}
