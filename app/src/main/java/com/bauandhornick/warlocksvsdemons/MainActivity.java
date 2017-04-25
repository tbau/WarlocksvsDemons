package com.bauandhornick.warlocksvsdemons;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class MainActivity extends AppCompatActivity {

    BattleFieldView bfv;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout ll = (LinearLayout)findViewById(R.id.scroll);
        ImageView im = new ImageView(getApplicationContext());


        bfv = (BattleFieldView)findViewById(R.id.BattleFieldView);
        HorizontalScrollView hsv = (HorizontalScrollView)findViewById(R.id.scrollview);
        ViewGroup.LayoutParams params = hsv.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = (int)(bfv.currentHeight*2/13.0);
        params.width = bfv.currentWidth;
        hsv.setLayoutParams(params);


        final Button b = (Button) findViewById(R.id.start_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bfv.enemyThread==null){
                    bfv.enemyThread = bfv.new animateEnemies(bfv);
                    bfv.enemyThread.execute();
                }}
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
                        //Toast.makeText(getApplicationContext(),v.getId()+" ",Toast.LENGTH_LONG).show();
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.character_popup);

                        dialog.findViewById(R.id.root).setBackgroundColor(0xff000000);
                        TextView tv = (TextView) dialog.findViewById(R.id.ally_name);
                        tv.setText(bfv.allyAttributesList.get(v.getId()).getName());
                        ImageView im = (ImageView) dialog.findViewById(R.id.ally_bitmap);
                        im.setImageDrawable(new BitmapDrawable(getResources(),bfv.availableAllyList.get(v.getId()).getAppearance()));
                        tv = (TextView) dialog.findViewById(R.id.affinity);
                        tv.setText("AFFINITY: " + bfv.allyAttributesList.get(v.getId()).getAffinity());
                        tv = (TextView) dialog.findViewById(R.id.weakness);
                        tv.setText("DAMAGE: " + bfv.weaponList.get(v.getId()).getDamage());
                        tv = (TextView) dialog.findViewById(R.id.costToBuy);
                        if(bfv.bm.getRound()>1)
                            tv.setText("COST: " + (int)(bfv.allyAttributesList.get(v.getId()).getCostToBuy()*(((bfv.bm.getRound()+5)/5.0))));
                        else
                          tv.setText("COST: " + bfv.allyAttributesList.get(v.getId()).getCostToBuy());



                        Button b = (Button) dialog.findViewById(R.id.cancel_button);
                        //b.setWidth((int)(dialog.getWindow().getDecorView().getWidth()/2.0));
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        final int id = v.getId();
                        b = (Button) dialog.findViewById(R.id.addButton);
                        //b.setWidth((int)(dialog.getWindow().getDecorView().getWidth()/2.0));
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                bfv.selected=id;
                                bfv.invalidate();
                                dialog.cancel();
                            }
                        });
                        dialog.show();

                        //bfv.bitmapTemp=  Bitmap.createBitmap(bfv.bitMap[i][j]);
                        //bfv.invalidate();

                    }
                });
                k++;
            ll.addView(im);
            }
        readFile();
        bfv.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
   // readFile();
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

    public void readFile(){
        FileInputStream streamIn = null;
        try {
            streamIn = openFileInput("gameState.txt");
            ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
            //bfv.bm = (BattleManager) objectinputstream.readObject();
            for(int i=0;i<bfv.alliesInBattle.size();i++){
                bfv.alliesInBattle.add((Ally)objectinputstream.readObject());
            }

           // bfv.alliesInBattle = (List<Ally>) objectinputstream.readObject();

            streamIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
/*
        try {

            FileOutputStream fout = openFileOutput("gameState.txt",MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);

            for(int i=0;i<bfv.alliesInBattle.size();i++){
                oos.writeObject(bfv.alliesInBattle.get(i));
            }
            oos.close();
            //oos.writeObject(bfv.bm);
            //oos.writeObject(bfv.alliesInBattle);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
