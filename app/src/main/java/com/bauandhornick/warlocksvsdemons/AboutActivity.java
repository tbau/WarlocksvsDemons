package com.bauandhornick.warlocksvsdemons;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //OnClick to pop-up Credits dialog
        Button b = (Button) findViewById(R.id.credits);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AboutActivity.this);
                dialog.setContentView(R.layout.credits_dialog);

                dialog.findViewById(R.id.root).setBackgroundColor(0xff000000);
                Button b = (Button) dialog.findViewById(R.id.cancel_button);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }
}
