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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.desire.desireapidemos.R;

public class HideShowAnimations extends Activity {
	private ViewGroup mAnimLayout;
	private LayoutTransition mLayoutTransition = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_hide_show);

		final CheckBox hideGoneCb = (CheckBox) findViewById(R.id.hide_show_cb);

		mAnimLayout = new LinearLayout(this);
		mAnimLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		Button newBtn = null;
		for (int i = 0; i < 4; i++) {
			newBtn = new Button(this);
			newBtn.setText(String.valueOf(i));
			mAnimLayout.addView(newBtn);
			newBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setVisibility(hideGoneCb.isChecked() ? View.GONE : View.INVISIBLE);
				}
			});
		}

		resetLayoutTransition();

		LinearLayout root = (LinearLayout) findViewById(R.id.container);
		root.addView(mAnimLayout);

		Button showBtn = (Button) findViewById(R.id.show_btn);
		showBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View view = null;
				for (int i = 0; i < mAnimLayout.getChildCount(); i++) {
					view = mAnimLayout.getChildAt(i);
					view.setVisibility(View.VISIBLE);
				}
			}
		});

		CheckBox customCb = (CheckBox) findViewById(R.id.custom_cb);
		customCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int duration = 0;
				if (isChecked) {
					mLayoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 1000);
					mLayoutTransition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 1000);
					setupCustomAnimations();
					duration = 5000;
				} else {
					resetLayoutTransition();
					duration = 3000;
				}
				mLayoutTransition.setDuration(duration);
			}
		});
	}

	private void resetLayoutTransition() {
		mLayoutTransition = new LayoutTransition();
		mAnimLayout.setLayoutTransition(mLayoutTransition);
	}

	private void setupCustomAnimations() {
		// changing when adding
		PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
		PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
		PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
		PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
		PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
		ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY)
				.setDuration(mLayoutTransition.getDuration(LayoutTransition.CHANGE_APPEARING));
		mLayoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
		changeIn.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				View view = (View) ((ObjectAnimator) animation).getTarget();
				view.setScaleX(1f);
				view.setScaleY(1f);
			}
		});

		// changing when removing
		Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
		Keyframe kf1 = Keyframe.ofFloat(.999f, 360f);
		Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
		PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
		ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation)
				.setDuration(mLayoutTransition.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
		mLayoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
		changeOut.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				View view = (View) ((ObjectAnimator) animation).getTarget();
				view.setRotation(0f);
			}
		});

		// adding
		ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).setDuration(
				mLayoutTransition.getDuration(LayoutTransition.APPEARING));
		mLayoutTransition.setAnimator(LayoutTransition.APPEARING, animIn);
		animIn.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				View view = (View) ((ObjectAnimator) animation).getTarget();
				view.setRotationY(0f);
			}
		});

		// removing
		ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).setDuration(
				mLayoutTransition.getDuration(LayoutTransition.DISAPPEARING));
		mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, animOut);
		animOut.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				View view = (View) ((ObjectAnimator) animation).getTarget();
				view.setRotationX(0f);
			}
		});

	}

}