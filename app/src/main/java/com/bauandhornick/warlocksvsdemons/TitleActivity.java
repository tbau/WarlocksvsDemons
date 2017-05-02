package com.bauandhornick.warlocksvsdemons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TitleActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_menu);

        Button b = (Button) findViewById(R.id.start);
        b.setOnClickListener(this);

        b = (Button) findViewById(R.id.about);
        b.setOnClickListener(this);

        b = (Button) findViewById(R.id.continueButton);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.start) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("load",0);
            startActivity(intent);
        }
        else if(v.getId() == R.id.about)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        else if(v.getId() == R.id.continueButton)
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("load",1);
            startActivity(intent);

        }
    }

}
