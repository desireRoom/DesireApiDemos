package com.desire.desireapidemos.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.TypeEvaluator;
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
import android.widget.Button;
import android.widget.LinearLayout;

import com.desire.desireapidemos.R;

public class CustomEvaluator extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_custom_evaluator);

		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		final MyAnimationView anim = new MyAnimationView(this);
		container.addView(anim);

		Button play = (Button) findViewById(R.id.start_btn);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				anim.startAnimation();
			}
		});
	}

	private class XYHolder {
		private float x;
		private float y;

		public XYHolder(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}
		
		@Override
		public String toString() {
			return " x = " + x + " y = " + y;
		}
	}

	private class XYEvaluator implements TypeEvaluator {

		@Override
		public Object evaluate(float fraction, Object startValue, Object endValue) {
			Log.d("@@@@", "------start evaluate!");
			XYHolder start = (XYHolder) startValue;
			XYHolder end = (XYHolder) endValue;
			Log.d("@@@@", "------ evaluate fraction = " + fraction + " -- start = " + start + " -- end = " + end);
			return new XYHolder(start.getX() + fraction * (end.getX() - start.getX()), start.getY() + fraction
					* (end.getY() - start.getY()));
		}

	}

	private class BallXYHolder {
		private ShapeHolder ball = null;

		public BallXYHolder(ShapeHolder ball) {
			this.ball = ball;
		}

		public void setXY(XYHolder xy) {
			ball.setX(xy.getX());
			ball.setY(xy.getY());
		}
		
		public XYHolder getXY() {
			Log.d("@@@@", "---- ball xy holder get xy && x = " + ball.getX() + "  y = " + ball.getY());
			return new XYHolder(ball.getX(), ball.getY());
		}
	}

	public class MyAnimationView extends View implements AnimatorUpdateListener {
		private ShapeHolder ball = null;
		private ValueAnimator anim = null;
		private BallXYHolder ballHolder = null;

		public MyAnimationView(Context context) {
			super(context);
			ball = createBall(25, 25);
			ballHolder = new BallXYHolder(ball);
		}

		private void initAnimator() {
			if (anim == null) {
				XYHolder start = new XYHolder(0f, 0f);
				XYHolder end = new XYHolder(300f, 500f);
				anim = ObjectAnimator.ofObject(ballHolder, "xY", new XYEvaluator(), end).setDuration(300);
				anim.addUpdateListener(this);
				Log.d("@@@@", "------end init animator!");
			}
		}

		private ShapeHolder createBall(float x, float y) {
			OvalShape circle = new OvalShape();
			circle.resize(100f, 100f);
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder shapeHolder = new ShapeHolder(drawable);
			shapeHolder.setX(x - 25f);
			shapeHolder.setY(y - 25f);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			int color = 0xff000000 | red << 16 | green << 8 | blue;
			int dark = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
			Paint paint = drawable.getPaint();
			RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f, color, dark, Shader.TileMode.CLAMP);
			paint.setShader(gradient);
			shapeHolder.setPaint(paint);
			return shapeHolder;
		}

		public void startAnimation() {
			initAnimator();
			Log.d("@@@@", "--- start animation!");
			anim.start();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.save();
			canvas.translate(ball.getX(), ball.getY());
			ball.getShape().draw(canvas);
			canvas.restore();
		}

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			Log.d("@@@@", "---- update!");
			invalidate();
		}
	}

}
