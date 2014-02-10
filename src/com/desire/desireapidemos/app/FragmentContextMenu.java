package com.desire.desireapidemos.app;

import com.desire.desireapidemos.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;

public class FragmentContextMenu extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        MyContextMenuFragment f = new MyContextMenuFragment();
        getFragmentManager().beginTransaction().add(android.R.id.content, f).commit();
    }
    
    public static class MyContextMenuFragment extends Fragment {
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.app_fragment_context_menu, container, false);
            registerForContextMenu(root.findViewById(R.id.long_press));
            return root;
        }
        
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.add(Menu.NONE, R.id.a_item, Menu.NONE, "menu A");
            menu.add(Menu.NONE, R.id.b_item, Menu.NONE, "menu B");
        }
        
        @Override
        public boolean onContextItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.a_item:
                    Toast.makeText(getActivity(), "Item 1a was choosed!", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.b_item:
                    Toast.makeText(getActivity(), "Item 2b was choosed!", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return super.onContextItemSelected(item);
        }
        
    }
}
