
package com.desire.desireapidemos.app;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.desire.desireapidemos.R;

public class ActionBarDisplayOptions extends Activity implements OnClickListener, TabListener {
    private View mCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actionbar_display_options);

        findViewById(R.id.toggle_home_as_up).setOnClickListener(this);
        findViewById(R.id.toggle_show_home).setOnClickListener(this);
        findViewById(R.id.toggle_use_logo).setOnClickListener(this);
        findViewById(R.id.toggle_show_title).setOnClickListener(this);
        findViewById(R.id.toggle_show_custom).setOnClickListener(this);
        findViewById(R.id.toggle_navigation).setOnClickListener(this);
        findViewById(R.id.cycle_custom_gravity).setOnClickListener(this);

        ActionBar bar = getActionBar();
        mCustomView = new Button(this);
        ((Button) mCustomView).setText(R.string.display_options_custom_button);
        bar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        bar.addTab(bar.newTab().setText("Tab 1").setTabListener(this));
        bar.addTab(bar.newTab().setText("Tab 2").setTabListener(this));
        bar.addTab(bar.newTab().setText("Tab 3").setTabListener(this));
    }

    @Override
    public void onClick(View v) {
        ActionBar bar = getActionBar();
        int id = v.getId();
        int flag = 0;
        switch (id) {
            case R.id.toggle_home_as_up:
                flag = ActionBar.DISPLAY_HOME_AS_UP;
                break;
            case R.id.toggle_show_home:
                flag = ActionBar.DISPLAY_SHOW_HOME;
                break;
            case R.id.toggle_use_logo:
                flag = ActionBar.DISPLAY_USE_LOGO;
                break;
            case R.id.toggle_show_title:
                flag = ActionBar.DISPLAY_SHOW_TITLE;
                break;
            case R.id.toggle_show_custom:
                flag = ActionBar.DISPLAY_SHOW_CUSTOM;
                break;
            case R.id.toggle_navigation:
                bar.setNavigationMode(bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD ? ActionBar.NAVIGATION_MODE_TABS : ActionBar.NAVIGATION_MODE_STANDARD);
                return;
            case R.id.cycle_custom_gravity:
                LayoutParams lp = (LayoutParams) mCustomView.getLayoutParams();
                int newGravity = 0;
                switch (lp.gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.START:
                        newGravity = Gravity.CENTER_HORIZONTAL;
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        newGravity = Gravity.END;
                        break;
                    case Gravity.END:
                        newGravity = Gravity.START;
                        break;
                }
                lp.gravity = lp.gravity & ~Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK | newGravity;
                bar.setCustomView(mCustomView, lp);
                return;
        }
        int change = bar.getDisplayOptions() ^ flag;
        bar.setDisplayOptions(change, flag);
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
}
