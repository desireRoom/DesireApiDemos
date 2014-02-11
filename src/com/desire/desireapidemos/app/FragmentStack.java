
package com.desire.desireapidemos.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.desire.desireapidemos.R;
import com.desire.desireapidemos.app.FragmentCustomAnimations.CountingFragment;

public class FragmentStack extends Activity {
    private int mStackLevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_fragment_stack);

        Button add = (Button) findViewById(R.id.new_fragment);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentToStack(mStackLevel);
            }
        });

        Button del = (Button) findViewById(R.id.delete_fragment);
        del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        if (savedInstanceState == null) {
            Fragment f = CountingFragment.newInstance(mStackLevel);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.simple_fragment, f);
            ft.commit();
        } else {
            mStackLevel = savedInstanceState.getInt("level");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level", mStackLevel);
    }

    private void addFragmentToStack(int num) {
        mStackLevel++;
        
        Fragment f = CountingFragment.newInstance(mStackLevel);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.simple_fragment, f);
        ft.addToBackStack(null);
        ft.commit();

    }

}
