
package com.desire.desireapidemos.animation;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.desire.desireapidemos.R;

public class MultipleProperties extends Activity {
    private final static int DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_multiple);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        final MyAnimationView animView = new MyAnimationView(this);
        container.addView(animView);

        Button startBtn = (Button) findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animView.startAnimation();
            }
        });

    }

    private class MyAnimationView extends View implements AnimatorUpdateListener {
        private static final float BALL_SIZE = 100f;

        private List<ShapeHolder> balls = new ArrayList<ShapeHolder>();

        private Animator animator = null;

        public MyAnimationView(Context context) {
            super(context);
            addBall(50, 0);
            addBall(150, 0);
            addBall(250, 0);
            addBall(350, 0);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            for (ShapeHolder ball : balls) {
                canvas.translate(ball.getX(), ball.getY());
                ball.getShape().draw(canvas);
                canvas.translate(-ball.getX(), -ball.getY());
            }
        }

        private void addBall(float x, float y) {
            OvalShape s = new OvalShape();
            s.resize(BALL_SIZE, BALL_SIZE);
            ShapeDrawable d = new ShapeDrawable(s);
            ShapeHolder holder = new ShapeHolder(d);
            holder.setX(x);
            holder.setY(y);
            int red = (int) (100 + Math.random() * 155);
            int green = (int) (100 + Math.random() * 155);
            int blue = (int) (100 + Math.random() * 155);
            int color = 0xff000000 | red << 16 | green << 8 | blue;
            int dark = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
            Paint paint = d.getPaint();
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f, color, dark, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            balls.add(holder);
        }

        public void startAnimation() {
            createAnimation();
            animator.start();
        }

        public void createAnimation() {
            if (animator == null) {
                ShapeHolder ball = balls.get(0);
                ObjectAnimator yBounce = ObjectAnimator.ofFloat(ball, "y", ball.getY(), getHeight() - BALL_SIZE).setDuration(DURATION);
                yBounce.setInterpolator(new BounceInterpolator());
                yBounce.addUpdateListener(this);

                ball = balls.get(1);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", ball.getY(), getHeight() - BALL_SIZE);
                PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f);
                ObjectAnimator yAlpha = ObjectAnimator.ofPropertyValuesHolder(ball, pvhY, pvhAlpha).setDuration(DURATION / 2);
                yAlpha.setInterpolator(new AccelerateInterpolator());
                yAlpha.setRepeatCount(1);
                yAlpha.setRepeatMode(ValueAnimator.REVERSE);

                ball = balls.get(2);
                PropertyValuesHolder pvhW = PropertyValuesHolder.ofFloat("width", ball.getWidth(), ball.getWidth() * 2);
                PropertyValuesHolder pvhH = PropertyValuesHolder.ofFloat("height", ball.getHeight(), ball.getHeight() * 2);
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", ball.getX(), ball.getX() - BALL_SIZE / 2f);
                pvhY = PropertyValuesHolder.ofFloat("y", ball.getY(), ball.getY() - BALL_SIZE / 2f);
                ObjectAnimator whxyAnim = ObjectAnimator.ofPropertyValuesHolder(ball, pvhW, pvhH, pvhX, pvhY).setDuration(DURATION / 2);
                whxyAnim.setRepeatCount(1);
                whxyAnim.setRepeatMode(ValueAnimator.REVERSE);

                ball = balls.get(3);
                pvhY = PropertyValuesHolder.ofFloat("y", ball.getY(), getHeight() - BALL_SIZE);
                float ballX = ball.getX();
                Keyframe kf0 = Keyframe.ofFloat(0f, ballX);
                Keyframe kf1 = Keyframe.ofFloat(.5f, ballX + 100f);
                Keyframe kf2 = Keyframe.ofFloat(1f, ballX - 150f);
                pvhX = PropertyValuesHolder.ofKeyframe("x", kf0, kf1, kf2);
                ObjectAnimator xyAnim = ObjectAnimator.ofPropertyValuesHolder(ball, pvhY, pvhX).setDuration(DURATION / 2);
                xyAnim.setRepeatCount(1);
                xyAnim.setRepeatMode(ValueAnimator.REVERSE);

                animator = new AnimatorSet();
                ((AnimatorSet) animator).playTogether(yBounce, yAlpha, whxyAnim, xyAnim);
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }

    }

}
