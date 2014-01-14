
package com.desire.desireapidemos.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.desire.desireapidemos.R;

public class ActivityCustomTitle extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_custom_title);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_custom_title_1);

        final TextView leftText = (TextView) findViewById(R.id.left_text);
        final TextView rightText = (TextView) findViewById(R.id.right_text);
        final EditText leftTextEdit = (EditText) findViewById(R.id.left_text_edit);
        final EditText rightTextEdit = (EditText) findViewById(R.id.right_text_edit);
        Button leftButton = (Button) findViewById(R.id.left_text_button);
        Button rightButton = (Button) findViewById(R.id.right_text_button);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                leftText.setText(leftTextEdit.getText());
            }
        });
        
        rightButton.setOnClickListener(new  OnClickListener() {
            @Override
            public void onClick(View v) {
                rightText.setText(rightTextEdit.getText());
            }
        });
    }

}
