package com.desire.desireapidemos.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.desire.desireapidemos.R;

public class ActivityIntents extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.app_activity_intents);
        
        findViewById(R.id.get_music).setOnClickListener(new  OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivity(Intent.createChooser(intent, "Select music"));
            }
        });
    }

}
