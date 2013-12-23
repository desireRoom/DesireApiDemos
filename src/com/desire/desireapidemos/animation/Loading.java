
package com.desire.desireapidemos.animation;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.desire.desireapidemos.R;

public class Loading extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_loading);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        final MyAnimationView view = new MyAnimationView(this);
        container.addView(view);

        Button startBtn = (Button) findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                view.startAnimation();
            }
        });

    }

    private class MyAnimationView extends View implements AnimatorUpdateListener {

        private static final float BALL_SIZE = 100f;

        private final List<ShapeHolder> balls = new ArrayList<ShapeHolder>();

        private Animator animator = null;

        public MyAnimationView(Context context) {
            super(context);
            addBall(50, 50);
            addBall(200, 50);
            addBall(350, 50);
            addBall(500, 50, Color.GREEN);
        }

        private void createAnimation() {
            if (animator == null) {
                Context app = Loading.this;

                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(app, R.animator.object_animator);
                anim.addUpdateListener(this);
                anim.setTarget(balls.get(0));

                ValueAnimator fader = (ValueAnimator) AnimatorInflater.loadAnimator(app, R.animator.animator);
                fader.addUpdateListener(new AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        balls.get(1).setAlpha((Float) animation.getAnimatedValue());
                    }
                });

                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(app, R.animator.animator_set);
                set.setTarget(balls.get(2));

                ObjectAnimator colorizer = (ObjectAnimator) AnimatorInflater.loadAnimator(app, R.animator.color_animator);
                colorizer.setTarget(balls.get(3));

                animator = new AnimatorSet();
                ((AnimatorSet) animator).playTogether(anim, fader, set, colorizer);
            }
        }

        public void startAnimation() {
            createAnimation();
            animator.start();
        }

        private void addBall(float x, float y, int color) {
            ShapeHolder holder = createBall(x, y);
            holder.setColor(color);
            balls.add(holder);
        }

        private void addBall(float x, float y) {
            ShapeHolder holder = createBall(x, y);
            int red = (int) (100 + Math.random() * 155);
            int green = (int) (100 + Math.random() * 155);
            int blue = (int) (100 + Math.random() * 155);
            int color = 0xff000000 | red << 16 | green << 8 | blue;
            int dark = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
            Paint paint = holder.getShape().getPaint();
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f, color, dark, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            holder.setPaint(paint);
            balls.add(holder);
        }

        private ShapeHolder createBall(float x, float y) {
            OvalShape s = new OvalShape();
            s.resize(BALL_SIZE, BALL_SIZE);
            ShapeDrawable d = new ShapeDrawable(s);
            ShapeHolder holder = new ShapeHolder(d);
            holder.setX(x);
            holder.setY(y);
            return holder;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            for (ShapeHolder ball : balls) {
                canvas.translate(ball.getX(), ball.getY());
                ball.getShape().draw(canvas);
                canvas.translate(-ball.getX(), -ball.getY());
            }
            canvas.restore();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }

    }

}
