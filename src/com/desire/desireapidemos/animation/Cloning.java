package com.desire.desireapidemos.animation;

import java.util.ArrayList;
import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.desire.desireapidemos.R;

public class Cloning extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.animation_cloning);
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		final MyAnimationView anim = new MyAnimationView(this);
		container.addView(anim);

		Button run = (Button) findViewById(R.id.start_btn);
		run.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				anim.startAnimation();
			}
		});

	}

	public class MyAnimationView extends View implements ValueAnimator.AnimatorUpdateListener {
		public final List<ShapeHolder> balls = new ArrayList<ShapeHolder>();
		private float mDensity;
		private AnimatorSet animator = null;

		public MyAnimationView(Context context) {
			super(context);

			mDensity = context.getResources().getDisplayMetrics().density;

			addBall(50f, 25f);
			addBall(150f, 25f);
			addBall(250f, 25f);
			addBall(350f, 25f);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			for (int i = 0; i < balls.size(); i++) {
				ShapeHolder ball = balls.get(i);
				canvas.save();
				canvas.translate(ball.getX(), ball.getY());
				ball.getShape().draw(canvas);
				canvas.restore();
			}
		}

		private void addBall(float x, float y) {
			OvalShape circle = new OvalShape();
			circle.resize(50f * mDensity, 50f * mDensity);
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder ball = new ShapeHolder(drawable);
			ball.setX(x - 25f);
			ball.setY(y - 25f);
			int red = (int) (100 + Math.random() * 155);
			int green = (int) (100 + Math.random() * 155);
			int blue = (int) (100 + Math.random() * 155);
			int color = 0xff000000 | red << 16 | green << 8 | blue;
			int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
			Paint paint = drawable.getPaint();
			RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP);
			paint.setShader(gradient);
			ball.setPaint(paint);
			balls.add(ball);
		}

		private void initAnimation() {
			if (animator == null) {
				ObjectAnimator anim1 = ObjectAnimator.ofFloat(balls.get(0), "y", 0f, getHeight() - balls.get(0).getHeight())
						.setDuration(3000);
				ObjectAnimator anim2 = anim1.clone();
				anim2.setTarget(balls.get(1));
				anim1.addUpdateListener(this);
				
				ShapeHolder ball3 = balls.get(2);
				ObjectAnimator animDown = ObjectAnimator.ofFloat(ball3, "y", 0f, getHeight() - ball3.getHeight()).setDuration(3000);
				animDown.setInterpolator(new AccelerateInterpolator());
				ObjectAnimator animUp = ObjectAnimator.ofFloat(ball3, "y", getHeight() - ball3.getHeight(), 0f).setDuration(3000);
				animUp.setInterpolator(new DecelerateInterpolator());
				AnimatorSet s1 = new AnimatorSet();
				s1.playSequentially(animDown, animUp);
				animDown.addUpdateListener(this);
				animUp.addUpdateListener(this);
				AnimatorSet s2 = s1.clone();
				s2.setTarget(balls.get(3));
				
				animator = new AnimatorSet();
				animator.playTogether(anim1, anim2, s1);
				animator.playSequentially(s1, s2);
			}
		}

		public void startAnimation() {
			initAnimation();
			animator.start();
		}

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			invalidate();
		}

	}

}
