package com.desire.desireapidemos.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.desire.desireapidemos.R;

public class ActivityForwarding extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_forwarding);
        
        Button btn = (Button) findViewById(R.id.go);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityForwarding.this, ActivityForwardTarget.class));
                finish();
            }
        });
    }

}
