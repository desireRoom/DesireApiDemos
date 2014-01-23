
package com.desire.desireapidemos.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.desire.desireapidemos.MainActivity;
import com.desire.desireapidemos.R;

public class ActivityIntentActivityFlags extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_intent_activity_flags);

        // Watch for button clicks.
        Button button = (Button) findViewById(R.id.flag_activity_clear_task);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.flag_activity_clear_task_pi);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.flag_activity_clear_task:
                startActivities(buildIntentsToViewLists());
                break;
            case R.id.flag_activity_clear_task_pi:
                PendingIntent pi = PendingIntent.getActivities(this, 0, buildIntentsToViewLists(), PendingIntent.FLAG_UPDATE_CURRENT);
                try {
                    pi.send();
                } catch (Exception e) {
                    Log.w("IntentActivityFlags", "Failed sending PendingIntent", e);
                }
                break;

        }
    }

    private Intent[] buildIntentsToViewLists() {
        Intent[] is = new Intent[3];
        is[0] = Intent.makeRestartActivityTask(new ComponentName(this, MainActivity.class));

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClass(this, MainActivity.class);
        i.putExtra("com.desire.apis.path", "Views");
        is[1] = i;

        i = new Intent(Intent.ACTION_MAIN);
        i.setClass(this, MainActivity.class);
        i.putExtra("com.desire.apis.path", "Views/Lists");
        is[2] = i;
        return is;
    }
}
