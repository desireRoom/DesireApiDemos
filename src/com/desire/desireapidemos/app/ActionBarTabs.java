
package com.desire.desireapidemos.app;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.desire.desireapidemos.R;

public class ActionBarTabs extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_actionbar_tab);
    }

    public void onAddTab(View v) {
        ActionBar bar = getActionBar();
        int tabCount = bar.getTabCount();
        String text = "Tab " + tabCount;
        bar.addTab(bar.newTab().setText(text).setTabListener(new TabListener(new TabContentFragement(text))));
    }

    public void onRemoveTab(View v) {
        ActionBar bar = getActionBar();
        if (bar.getTabCount() > 0) {
            bar.removeTabAt(bar.getTabCount() - 1);
        }
    }

    public void onToggleTabs(View v) {
        ActionBar bar = getActionBar();
        if (bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
        } else {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        }
    }

    public void onRemoveAllTabs(View v) {
        getActionBar().removeAllTabs();
    }

    private class TabListener implements ActionBar.TabListener {
        private TabContentFragement tFragement;

        public TabListener(TabContentFragement fragement) {
            tFragement = fragement;
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.add(R.id.fragment_content, tFragement, tFragement.getText());
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(tFragement);
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Toast.makeText(ActionBarTabs.this, "Reselected! " + tab.getPosition(), Toast.LENGTH_SHORT).show();
        }

    }

    private class TabContentFragement extends Fragment {
        private String tText;

        public TabContentFragement(String text) {
            tText = text;
        }

        public String getText() {
            return tText;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View fragView = inflater.inflate(R.layout.actionbar_tab_content, container, false);
            TextView text = (TextView) fragView.findViewById(R.id.text);
            text.setText(tText);
            return fragView;
        }

    }

}
