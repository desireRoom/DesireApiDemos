
package com.desire.desireapidemos.app;

import com.desire.desireapidemos.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ActivityDialog extends Activity implements OnClickListener {
    private LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_LEFT_ICON);

        setContentView(R.layout.app_activity_dialog);
        getWindow().setTitle("This is just test");
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);
        
        mContainer = (LinearLayout) findViewById(R.id.inner_content);

        Button button = (Button) findViewById(R.id.add);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.remove);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add:
                ImageView iv = new ImageView(this);
                iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                iv.setPadding(4, 4, 4, 4);
                mContainer.addView(iv);
                break;
            case R.id.remove:
                int count = mContainer.getChildCount();
                if (count > 0) {
                    mContainer.removeViewAt(count - 1);
                }
                break;
        }
    }

}
