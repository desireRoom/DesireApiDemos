package com.desire.desireapidemos.app;

import com.desire.desireapidemos.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class ActivityHelloWorld extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setTextAppearance(this, android.R.attr.textAppearanceMedium);
        tv.setText(R.string.hello_world);
        setContentView(tv);
    }

}
