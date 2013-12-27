
package com.desire.desireapidemos.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.desire.desireapidemos.R;

public class ViewFlip extends Activity {

    private static final String[] LIST_STRINGS_EN = new String[] {
        "One", "Two", "Three", "Four", "Five", "Six"
    };

    private static final String[] LIST_STRINGS_FR = new String[] {
        "Un", "Deux", "Trois", "Quatre", "Le Five", "Six"
    };

    private ListView mEnglistList;

    private ListView mFrenchList;

    private Interpolator mAccelerator = new AccelerateInterpolator();

    private Interpolator mDecelerator = new DecelerateInterpolator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.animation_view_flip);
        mEnglistList = (ListView) findViewById(R.id.list_en);
        mFrenchList = (ListView) findViewById(R.id.list_fr);

        final ArrayAdapter<String> enAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LIST_STRINGS_EN);
        final ArrayAdapter<String> frAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LIST_STRINGS_FR);
        mEnglistList.setAdapter(enAdapter);
        mFrenchList.setAdapter(frAdapter);
        mFrenchList.setRotationY(-90f);

        Button startBtn = (Button) findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flipList();
            }
        });
    }

    private void flipList() {
        final ListView visibleList;
        final ListView goneList;

        if (mEnglistList.getVisibility() == View.VISIBLE) {
            visibleList = mEnglistList;
            goneList = mFrenchList;
        } else {
            visibleList = mFrenchList;
            goneList = mEnglistList;
        }

        ObjectAnimator visToGone = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, 90f).setDuration(500);
        visToGone.setInterpolator(mAccelerator);

        final ObjectAnimator goneToVis = ObjectAnimator.ofFloat(goneList, "rotationY", -90f, 0f).setDuration(500);
        goneToVis.setInterpolator(mDecelerator);

        visToGone.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                visibleList.setVisibility(View.GONE);
                goneToVis.start();
                goneList.setVisibility(View.VISIBLE);
            }
        });
        visToGone.start();
    }

}
