package com.bauandhornick.warlocksvsdemons;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    Toast toast;
    BattleFieldView bfv;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout ll = (LinearLayout)findViewById(R.id.scroll);

        ImageView im;

        bfv = (BattleFieldView)findViewById(R.id.BattleFieldView);
        HorizontalScrollView hsv = (HorizontalScrollView)findViewById(R.id.scrollview);
        ViewGroup.LayoutParams params = hsv.getLayoutParams();

        // Changes the height and width to the specified *pixels*
        params.height = (int)(bfv.currentHeight*2/13.0);
        params.width = bfv.currentWidth;
        hsv.setLayoutParams(params);

        //On Title Menu, when Start is selected, start thread for enemies.

        final Button b = (Button) findViewById(R.id.start_button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bfv.gameThread==null&&bfv.alliesInBattle.size()>0){
                    bfv.gameThread = bfv.new animateGame(bfv);
                    bfv.gameThread.execute();

                }
                else
                {
                    if(bfv.bm.getMana()<bfv.availableAllyList.get(0).getAa().getCostToBuy() *(((bfv.bm.getRound()+5)/5.0))) {

                        //Reset everything
                        bfv.projectileList.clear();
                        bfv.enemyQueue.clear();
                        bfv.enemiesInBattle.clear();
                        bfv.alliesInBattle.clear();
                        bfv.bm.setMana(3000);
                        bfv.bm.setHealth(10000);
                        bfv.bm.setRound(0);
                        bfv.invalidate();

                        final Dialog dialog = new Dialog(v.getContext());

                        dialog.setContentView(R.layout.character_popup);


                        dialog.findViewById(R.id.root).setBackgroundColor(0xff000000); //set background of dialog to black

                        //Set the Ally name text using allyAttributesList.
                        TextView tv = (TextView) dialog.findViewById(R.id.ally_name);

                        tv.setText("Out of Mana");

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
                    else{
                        if(toast != null){
                            toast.cancel();
                        }
                        toast = Toast.makeText(getApplicationContext(),"Must have allies on field",Toast.LENGTH_SHORT);
                        toast.show();}
                }
            }
        });
        int k=0;
        for(int i = 0;i<12;i++){

            im = new ImageView(getApplicationContext());
            bitmap = bfv.availableAllyList.get(i).getAppearance();
            bitmap = Bitmap.createScaledBitmap(bitmap,200,200,false);
            im.setImageDrawable(new BitmapDrawable(getResources(),bitmap));
            im.setBackgroundColor(0xFF222222);
            im.setId(k);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.character_popup);

                    //Set the Ally name text using allyAttributesList.
                    dialog.findViewById(R.id.root).setBackgroundColor(0xff000000);
                    TextView tv = (TextView) dialog.findViewById(R.id.ally_name);
                    tv.setText(bfv.allyAttributesList.get(v.getId()).getName());
                    
                    //Display appearance of Ally.
                    ImageView im = (ImageView) dialog.findViewById(R.id.ally_bitmap);
                    im.setImageDrawable(new BitmapDrawable(getResources(),bfv.availableAllyList.get(v.getId()).getAppearance()));
                  
                    //Display Affinity of Ally
                    tv = (TextView) dialog.findViewById(R.id.affinity);
                    tv.setText("AFFINITY: " + bfv.allyAttributesList.get(v.getId()).getAffinity());
                    
                    //Display Damage of Ally.
                    tv = (TextView) dialog.findViewById(R.id.weakness);
                    tv.setText("DAMAGE: " + bfv.weaponList.get(v.getId()).getDamage());
                    
                    //Display Cost of Ally. Cost will change depending on Round, it gradually increases.
                    tv = (TextView) dialog.findViewById(R.id.costToBuy);
                    if(bfv.bm.getRound()>1)
                        tv.setText("COST: " + (int)(bfv.allyAttributesList.get(v.getId()).getCostToBuy()*(((bfv.bm.getRound()+5)/5.0))));
                    else
                        tv.setText("COST: " + bfv.allyAttributesList.get(v.getId()).getCostToBuy());

                    Button b = (Button) dialog.findViewById(R.id.cancel_button);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    final int id = v.getId();
                    b = (Button) dialog.findViewById(R.id.addButton);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            bfv.selected=id;
                            bfv.invalidate();
                            dialog.cancel();
                        }
                    });
                    dialog.show();

                }
            });
            k++;
            ll.addView(im);
        }
        bfv.setMainContext(this);

        //Load file information if user is loading the last game.
        Intent intent = getIntent();
        if(intent.hasExtra("load")){
            int load = intent.getIntExtra("load",0);
            if(load==1)
                readFile();
        }
        bfv.invalidate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bfv.gameThread != null) {
            bfv.gameThread.paused=false;
        }
        Log.i("Resume--","OnResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* This function reads from a file in case the user wants to load the previous game.
        It'll set info for the BattleManger, which manages the game info.
     */
    public void readFile(){

        try {
            FileInputStream fis= openFileInput("gameState.txt");
            Scanner s = new Scanner(fis);
            if(s.hasNext())
                bfv.bm.setRound(s.nextInt());
            //bfv.bm.setRound(34);

            if(s.hasNext())
                bfv.bm.setHealth(s.nextInt());
            if(s.hasNext())
                bfv.bm.setMana(s.nextInt());

            int diff=0;
            if(s.hasNext())
                diff = s.nextInt();

            BattleManager.Difficulty diff2[] = BattleManager.Difficulty.values();
            bfv.bm.setDifficulty(diff2[diff]);

            while(s.hasNext()){
                int index= s.nextInt();
                int x = s.nextInt();
                int y = s.nextInt();


                bfv.alliesInBattle.add(new Ally(bfv.availableAllyList.get(index),x,y,index));
            }

            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void saveFile(){
        try {

            FileOutputStream fos = openFileOutput("gameState.txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(bfv.bm.getRound());
            pw.println(bfv.bm.getHealth());
            pw.println(bfv.bm.getMana());
            pw.println(bfv.bm.getDifficulty().ordinal());

            for(int i=0;i<bfv.alliesInBattle.size();i++){
                pw.println(bfv.alliesInBattle.get(i).getIndex());
                pw.println(bfv.alliesInBattle.get(i).getPos_x());
                pw.println(bfv.alliesInBattle.get(i).getPos_y());
            }

            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveFile();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bfv.gameThread != null) {
            bfv.gameThread.paused = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bfv.gameThread != null) {
            bfv.gameThread.paused=true;
            bfv.gameThread.done=true;
            bfv.gameThread=null;

            bfv.bm.setRound(bfv.bm.getRound()-1);

        }
        Button b = (Button)findViewById(R.id.start_button);
        b.setVisibility(VISIBLE);

        saveFile();
    }
}
