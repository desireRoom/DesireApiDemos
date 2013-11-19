package com.desire.desireapidemos.animation;

import java.util.ArrayList;
import java.util.List;

import com.desire.desireapidemos.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

public class BouncingBalls extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bouncing_balls);
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		container.addView(new MyAnimationView(this));
	}
	
	public class MyAnimationView extends View {
        private static final int RED = 0xffFF8080;
        private static final int BLUE = 0xff8080FF;
        private static final int CYAN = 0xff80ffff;
        private static final int GREEN = 0xff80ff80;
        
        private final List<ShapeHolder> balls = new ArrayList<ShapeHolder>();

		public MyAnimationView(Context context) {
			super(context);
			
			ValueAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", RED, BLUE, GREEN);
			colorAnim.setDuration(3000);
			colorAnim.setEvaluator(new ArgbEvaluator());
			colorAnim.setRepeatCount(ValueAnimator.INFINITE);
			colorAnim.setRepeatMode(ValueAnimator.REVERSE);
			colorAnim.start();
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() != MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_MOVE) {
				return false;
			}
			
			ShapeHolder newBall = addBalls(event.getX(), event.getY());
			
			float startY = newBall.getY();
			float endY = getHeight() - 50f;
			int h = getHeight();
			float y = event.getY();
			int duration = (int) (500 * ((h - y) / h));
			ValueAnimator down = ObjectAnimator.ofFloat(newBall, "y", startY, endY);
			down.setDuration(duration);
			down.setInterpolator(new AccelerateInterpolator());
			
			ValueAnimator squash1 = ObjectAnimator.ofFloat(newBall, "x", newBall.getX(), newBall.getX() - 25f);
			squash1.setDuration(duration / 4);
			squash1.setInterpolator(new AccelerateInterpolator());
			squash1.setRepeatCount(1);
			squash1.setRepeatMode(ValueAnimator.REVERSE);
			ValueAnimator squash2 = ObjectAnimator.ofFloat(newBall, "width", newBall.getWidth(), newBall.getWidth() + 50f);
			squash2.setDuration(duration / 4);
			squash2.setInterpolator(new AccelerateInterpolator());
			squash2.setRepeatCount(1);
			squash2.setRepeatMode(ValueAnimator.REVERSE);
			ValueAnimator squash3 = ObjectAnimator.ofFloat(newBall, "y", endY, endY + 25f);
			squash3.setDuration(duration / 4);
			squash3.setInterpolator(new AccelerateInterpolator());
			squash3.setRepeatCount(1);
			squash3.setRepeatMode(ValueAnimator.REVERSE);
			ValueAnimator squash4 = ObjectAnimator.ofFloat(newBall, "height", newBall.getHeight(), newBall.getHeight() - 25f);
			squash4.setDuration(duration / 4);
			squash4.setInterpolator(new AccelerateInterpolator());
			squash4.setRepeatCount(1);
			squash4.setRepeatMode(ValueAnimator.REVERSE);
			
			ValueAnimator up = ObjectAnimator.ofFloat(newBall, "y", endY, startY);
			up.setDuration(duration);
			up.setInterpolator(new AccelerateInterpolator());
			
			AnimatorSet bouncer = new AnimatorSet();
			bouncer.play(down).before(squash1);
			bouncer.play(squash1).with(squash2);
			bouncer.play(squash1).with(squash3);
			bouncer.play(squash1).with(squash4);
			bouncer.play(up).after(squash4);
			
            // Fading animation - remove the ball when the animation is done
            ValueAnimator fadeAnim = ObjectAnimator.ofFloat(newBall, "alpha", 1f, 0f);
            fadeAnim.setDuration(250);
            fadeAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    balls.remove(((ObjectAnimator)animation).getTarget());

                }
            });

            // Sequence the two animations to play one after the other
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(bouncer).before(fadeAnim);

            // Start the animation
            animatorSet.start();
			
			return true;
		}
		
		private ShapeHolder addBalls(float x, float y) {
			OvalShape circle = new OvalShape();
			circle.resize(50f, 50f);
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder ball = new ShapeHolder(drawable);
			ball.setX(x - 25f);
			ball.setY(y - 25f);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			int color = 0xff000000 | red << 16 | green << 8 | blue;
			int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;
			Paint paint = drawable.getPaint();
			RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP);
			paint.setShader(gradient);
			ball.setPaint(paint);
			balls.add(ball);
			return ball;
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			for (int i = 0; i < balls.size(); i++) {
				ShapeHolder shape = balls.get(i);
				canvas.save();
				canvas.translate(shape.getX(), shape.getY());
				shape.getShape().draw(canvas);
				canvas.restore();
			}
		}
		
	}

}
