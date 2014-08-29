package com.desire.desireapidemos.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desire.desireapidemos.R;

public class Events extends Activity {
	private TextView mStartT, mRepeatT, mCancelT, mEndT;
	private TextView mStartAT, mRepeatAT, mCancelAT, mEndAT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.animation_events);

		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		final MyAnimationView view = new MyAnimationView(this);
		container.addView(view);

		mStartT = (TextView) findViewById(R.id.start_text);
		mStartT.setAlpha(.5f);
		mRepeatT = (TextView) findViewById(R.id.repeat_text);
		mRepeatT.setAlpha(.5f);
		mCancelT = (TextView) findViewById(R.id.cancel_text);
		mCancelT.setAlpha(.5f);
		mEndT = (TextView) findViewById(R.id.end_text);
		mEndT.setAlpha(.5f);

		mStartAT = (TextView) findViewById(R.id.start_anim_text);
		mStartAT.setAlpha(.5f);
		mRepeatAT = (TextView) findViewById(R.id.repeat_anim_text);
		mRepeatAT.setAlpha(.5f);
		mCancelAT = (TextView) findViewById(R.id.cancel_anim_text);
		mCancelAT.setAlpha(.5f);
		mEndAT = (TextView) findViewById(R.id.end_anim_text);
		mEndAT.setAlpha(.5f);

		final CheckBox cb = (CheckBox) findViewById(R.id.end_cb);
		Button startBtn = (Button) findViewById(R.id.play_btn);
		startBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				view.startAnimation(cb.isChecked());
			}
		});
		Button cancelBtn = (Button) findViewById(R.id.cancel_btn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				view.cancelAnimation();
			}
		});
		Button endBtn = (Button) findViewById(R.id.end_btn);
		endBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				view.endAnimation();
			}
		});
	}

	public class MyAnimationView extends View implements AnimatorListener, AnimatorUpdateListener {
		private ShapeHolder ball = null;
		private Animator animation = null;
		private boolean endImmediately = false;

		public MyAnimationView(Context context) {
			super(context);
			ball = createBall(25f, 25f);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			canvas.save();
			canvas.translate(ball.getX(), ball.getY());
			ball.getShape().draw(canvas);
			canvas.restore();
		}

		private ShapeHolder createBall(float x, float y) {
			OvalShape s = new OvalShape();
			s.resize(50f, 50f);
			ShapeDrawable drawable = new ShapeDrawable(s);
			ShapeHolder holder = new ShapeHolder(drawable);
			holder.setX(x - 25f);
			holder.setY(y - 25f);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			int color = 0xff000000 | red << 16 | green << 8 | blue;
			int dark = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
			RadialGradient gradient = new RadialGradient(x, y, 50f, color, dark, Shader.TileMode.CLAMP);
			Paint paint = drawable.getPaint();
			paint.setShader(gradient);
			holder.setPaint(paint);
			return holder;
		}

		private void createAnimation() {
			if (animation == null) {
				ObjectAnimator yAnim = ObjectAnimator.ofFloat(ball, "y", ball.getY(), getHeight() - 50f).setDuration(3000);
				yAnim.setRepeatCount(0);
				yAnim.setRepeatMode(ValueAnimator.REVERSE);
				yAnim.setInterpolator(new AccelerateInterpolator());
				yAnim.addUpdateListener(this);
				yAnim.addListener(this);
				
				ObjectAnimator xAnim = ObjectAnimator.ofFloat(ball, "x", ball.getX(), ball.getX() + 300).setDuration(3000);
				xAnim.setStartDelay(0);
				xAnim.setRepeatCount(2);
				xAnim.setRepeatMode(ValueAnimator.REVERSE);
				xAnim.setInterpolator(new AccelerateInterpolator());
				xAnim.addUpdateListener(this);
				
				animation = new AnimatorSet();
				((AnimatorSet) animation).playTogether(xAnim, yAnim);
				animation.addListener(this);
			}
		}

		public void startAnimation(boolean endImmediately) {
			Log.d("@@@@", "-- start animation && end immediately = " + endImmediately);
			this.endImmediately = endImmediately;
			
			mStartT.setAlpha(.5f);
			mRepeatT.setAlpha(.5f);
			mCancelT.setAlpha(.5f);
			mEndT.setAlpha(.5f);

			mStartAT.setAlpha(.5f);
			mRepeatAT.setAlpha(.5f);
			mCancelAT.setAlpha(.5f);
			mEndAT.setAlpha(.5f);
			
			createAnimation();
			animation.start();
		}

		public void cancelAnimation() {
			Log.d("@@@@", "-- cancel animation!");
			createAnimation();
			animation.cancel();
		}

		public void endAnimation() {
			Log.d("@@@@", "-- end animation!");
			createAnimation();
			animation.end();
		}

		@Override
		public void onAnimationStart(Animator animation) {
			if (animation instanceof AnimatorSet) {
				mStartT.setAlpha(1.0f);
			} else {
				mStartAT.setAlpha(1.0f);
			}
			
			if (endImmediately) {
				animation.end();
			}
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			if (animation instanceof AnimatorSet) {
				mCancelT.setAlpha(1.0f);
			} else {
				mCancelAT.setAlpha(1.0f);
			}
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			if (animation instanceof AnimatorSet) {
				mEndT.setAlpha(1.0f);
			} else {
				mEndAT.setAlpha(1.0f);
			}
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			if (animation instanceof AnimatorSet) {
				mRepeatT.setAlpha(1.0f);
			} else {
				mRepeatAT.setAlpha(1.0f);
			}
		}

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			invalidate();
		}
	}
}
