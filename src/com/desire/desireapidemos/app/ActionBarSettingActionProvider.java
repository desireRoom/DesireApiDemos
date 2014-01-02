
package com.desire.desireapidemos.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.desire.desireapidemos.R;

public class ActionBarSettingActionProvider extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_setting_action_provider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, R.string.action_bar_settings_action_provider_no_handling, Toast.LENGTH_SHORT).show();
        return false;
    }

    public static class SettingsActionProvider extends ActionProvider {
        private static final Intent sStartSettings = new Intent(Settings.ACTION_SETTINGS);

        private Context sContext;

        public SettingsActionProvider(Context context) {
            super(context);
            sContext = context;
        }

        @Override
        public View onCreateActionView() {
            LayoutInflater inflater = LayoutInflater.from(sContext);
            View view = inflater.inflate(R.layout.actionbar_setting_action_provider, null);
            ImageButton btn = (ImageButton) view.findViewById(R.id.button);
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    sContext.startActivity(sStartSettings);
                }
            });
            return view;
        }

        @Override
        public boolean onPerformDefaultAction() {
            sContext.startActivity(sStartSettings);
            return true;
        }

    }

}
