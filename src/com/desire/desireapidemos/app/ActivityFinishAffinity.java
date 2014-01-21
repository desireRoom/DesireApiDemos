package com.desire.desireapidemos.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.desire.desireapidemos.R;

public class ActivityFinishAffinity extends Activity implements OnClickListener {
    private int mNesting = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.app_activity_finish_affinity);


        mNesting = getIntent().getIntExtra("nesting", 1);
        ((TextView)findViewById(R.id.seq)).setText("Current nesting: " + mNesting);
        
        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.nest);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.finish);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.nest:
                Intent intent = new Intent(this, ActivityFinishAffinity.class);
                intent.putExtra("nesting", mNesting + 1);
                startActivity(intent);
                break;
            case R.id.finish:
                finishAffinity();
                break;
        }
    }

}
