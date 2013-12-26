
package com.desire.desireapidemos.animation;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.desire.desireapidemos.R;

public class Seeking extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_seeking);

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

        SeekBar seekbar = (SeekBar) findViewById(R.id.seek_bar);
        seekbar.setMax(3000);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animView.seeking(progress);
            }
        });
    }

    private class MyAnimationView extends View implements AnimatorUpdateListener {
        private ShapeHolder ball = null;
        private ValueAnimator animator = null;

        public MyAnimationView(Context context) {
            super(context);
            ball = addBall(200, 0);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.translate(ball.getX(), ball.getY());
            ball.getShape().draw(canvas);
        }

        private ShapeHolder addBall(float x, float y) {
            OvalShape s = new OvalShape();
            s.resize(100f, 100f);
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
            holder.setPaint(paint);
            return holder;
        }
        
        private void createAnimation() {
            if (animator == null) {
                animator = ObjectAnimator.ofFloat(ball, "y" , ball.getY(), getHeight() - 100f).setDuration(3000);
                animator.setInterpolator(new BounceInterpolator());
                animator.addUpdateListener(this);
            }
        }

        public void startAnimation() {
            createAnimation();
            animator.start();
        }

        public void seeking(long seekTime) {
            createAnimation();
            animator.setCurrentPlayTime(seekTime);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }

    }

}
