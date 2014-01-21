package com.desire.desireapidemos.app;

import com.desire.desireapidemos.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityForwardTarget extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView tv = new TextView(this);
        tv.setText(R.string.forward_target);
        tv.setTextAppearance(this, android.R.attr.textAppearanceMedium);
        setContentView(tv);
    }

}
