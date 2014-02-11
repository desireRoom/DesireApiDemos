
package com.desire.desireapidemos.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.desire.desireapidemos.R;

public class FragmentCustomAnimations extends Activity {

    private int mStacklevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_fragment_stack);

        Button add = (Button) findViewById(R.id.new_fragment);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentToStack();
            }
        });

        if (savedInstanceState == null) {
            Fragment frag = CountingFragment.newInstance(mStacklevel);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.simple_fragment, frag);
            ft.commit();
        } else {
            mStacklevel = savedInstanceState.getInt("level");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level", mStacklevel);
    }

    private void addFragmentToStack() {
        mStacklevel++;

        Fragment frag = CountingFragment.newInstance(mStacklevel);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit, R.animator.fragment_slide_right_enter, R.animator.fragment_slide_right_exit);
        ft.replace(R.id.simple_fragment, frag);
        ft.addToBackStack(null);
        ft.commit();

    }

    public static class CountingFragment extends Fragment {

        int cNum;

        public static CountingFragment newInstance(int num) {
            CountingFragment f = new CountingFragment();

            Bundle b = new Bundle();
            b.putInt("num", num);
            f.setArguments(b);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            cNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.hello_world, container, false);
            TextView tv = (TextView) v.findViewById(R.id.text);
            tv.setText("Fragment # " + cNum);
            tv.setBackground(getResources().getDrawable(android.R.drawable.gallery_thumb));
            return v;
        }
    }

}
