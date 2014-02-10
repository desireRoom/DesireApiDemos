
package com.desire.desireapidemos.app;

import com.desire.desireapidemos.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentArguments extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_fragment_argument);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment f = MyFragment.newInstance("from Arguments");
            ft.add(R.id.created, f);
            ft.commit();
        }
    }

    public static class MyFragment extends Fragment {
        CharSequence mLabel;

        static MyFragment newInstance(CharSequence label) {
            MyFragment frag = new MyFragment();
            Bundle b = new Bundle();
            b.putCharSequence("label", label);
            frag.setArguments(b);
            return frag;

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle b = getArguments();
            if (b != null) {
                mLabel = b.getCharSequence("label");
            }
        }

        @Override
        public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
            super.onInflate(activity, attrs, savedInstanceState);
            TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.FragmentArguments);
            mLabel = a.getText(R.styleable.FragmentArguments_android_label);
            a.recycle();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.hello_world, container, false);
            TextView tv = (TextView) v.findViewById(R.id.text);
            tv.setText(mLabel != null ? mLabel : "(no label)");
            tv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.gallery_thumb));
            return v;
        }

    }

}
