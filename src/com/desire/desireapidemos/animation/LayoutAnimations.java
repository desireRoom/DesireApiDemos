
package com.desire.desireapidemos.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.desire.desireapidemos.R;

public class LayoutAnimations extends Activity {
    private int mNumBtn = 1;

    ViewGroup container = null;

    Animator defaultAppearingAnim, defaultDisappearingAnim;

    Animator defaultChangingAppearingAnim, defaultChangingDisappearingAnim;

    Animator customAppearingAnim, customDisappearingAnim;

    Animator customChangingAppearingAnim, customChangingDisappearingAnim;

    Animator currentAppearingAnim, currentDisappearingAnim;

    Animator currentChangingAppearingAnim, currentChangingDisappearingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_animations);

        container = new FixedGridLayout(this);
        container.setClipChildren(false);
        ((FixedGridLayout) container).setCellHeight(90);
        ((FixedGridLayout) container).setCellWidth(100);
        final LayoutTransition transitioner = new LayoutTransition();
        container.setLayoutTransition(transitioner);
        defaultAppearingAnim = transitioner.getAnimator(LayoutTransition.APPEARING);
        defaultDisappearingAnim = transitioner.getAnimator(LayoutTransition.DISAPPEARING);
        defaultChangingAppearingAnim = transitioner.getAnimator(LayoutTransition.CHANGE_APPEARING);
        defaultChangingDisappearingAnim = transitioner.getAnimator(LayoutTransition.CHANGE_DISAPPEARING);

        createCustomAnim(transitioner);

        ViewGroup parent = (ViewGroup) findViewById(R.id.parent);
        parent.addView(container);
        parent.setClipChildren(false);

        Button addBtn = (Button) findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button newBtn = new Button(v.getContext());
                newBtn.setText(String.valueOf(mNumBtn++));
                newBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeView(v);
                    }
                });
                container.addView(newBtn, Math.min(1, container.getChildCount()));
            }
        });

        CheckBox customCB = (CheckBox) findViewById(R.id.custom_anim_cb);
        customCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setupTransition(transitioner);
            }
        });

        CheckBox appearingCB = (CheckBox) findViewById(R.id.appearing_cb);
        appearingCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setupTransition(transitioner);
            }
        });
        CheckBox disappearingCB = (CheckBox) findViewById(R.id.disappearing_cb);
        disappearingCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setupTransition(transitioner);
            }
        });
        CheckBox changingAppearingCB = (CheckBox) findViewById(R.id.changing_appearing_cb);
        changingAppearingCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setupTransition(transitioner);
            }
        });
        CheckBox changingDisappearingCB = (CheckBox) findViewById(R.id.changing_disappearing_cb);
        changingDisappearingCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setupTransition(transitioner);
            }
        });
    }

    private void setupTransition(LayoutTransition transitioner) {
        CheckBox customAnimCB = (CheckBox) findViewById(R.id.custom_anim_cb);
        CheckBox appearingCB = (CheckBox) findViewById(R.id.appearing_cb);
        CheckBox disappearingCB = (CheckBox) findViewById(R.id.disappearing_cb);
        CheckBox changingAppearingCB = (CheckBox) findViewById(R.id.changing_appearing_cb);
        CheckBox changingDisappearingCB = (CheckBox) findViewById(R.id.changing_disappearing_cb);

        transitioner.setAnimator(LayoutTransition.APPEARING, appearingCB.isChecked() ? (customAnimCB.isChecked() ? customAppearingAnim : defaultAppearingAnim) : null);
        transitioner.setAnimator(LayoutTransition.DISAPPEARING, disappearingCB.isChecked() ? (customAnimCB.isChecked() ? customDisappearingAnim : defaultDisappearingAnim) : null);
        transitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changingAppearingCB.isChecked() ? (customAnimCB.isChecked() ? customChangingAppearingAnim : defaultChangingAppearingAnim) : null);
        transitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changingDisappearingCB.isChecked() ? (customAnimCB.isChecked() ? customChangingDisappearingAnim
                : defaultChangingDisappearingAnim) : null);

    }

    private void createCustomAnim(LayoutTransition transitioner) {
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);

        customChangingAppearingAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY).setDuration(
                transitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
        customChangingAppearingAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = (View) ((ObjectAnimator) animation).getTarget();
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        });

        // Changing while Removing
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
        Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
        customChangingDisappearingAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation).setDuration(
                transitioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
        customChangingDisappearingAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotation(0f);
            }
        });

        // Adding
        customAppearingAnim = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).setDuration(transitioner.getDuration(LayoutTransition.APPEARING));
        customAppearingAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationY(0f);
            }
        });

        // Removing
        customDisappearingAnim = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).setDuration(transitioner.getDuration(LayoutTransition.DISAPPEARING));
        customDisappearingAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationX(0f);
            }
        });

    }
}
