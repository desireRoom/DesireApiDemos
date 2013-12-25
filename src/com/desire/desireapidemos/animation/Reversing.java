
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.desire.desireapidemos.R;

public class Reversing extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.animation_reversing);

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

        Button reverseBtn = (Button) findViewById(R.id.reverse_btn);
        reverseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animView.reverseAnimation();
            }
        });
    }

    private class MyAnimationView extends View implements AnimatorUpdateListener {
        private ShapeHolder ball = null;

        private ValueAnimator animator = null;

        public MyAnimationView(Context context) {
            super(context);
            ball = createBall(25, 25);
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
            ShapeDrawable d = new ShapeDrawable(s);
            ShapeHolder holder = new ShapeHolder(d);
            holder.setX(x - 25);
            holder.setY(y - 25);
            int red = (int) (Math.random() * 255);
            int green = (int) (Math.random() * 255);
            int blue = (int) (Math.random() * 255);
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
                animator = ObjectAnimator.ofFloat(ball, "y", ball.getY(), getHeight() - 50).setDuration(1500);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.addUpdateListener(this);
            }
        }

        public void startAnimation() {
            createAnimation();
            animator.start();
        }

        public void reverseAnimation() {
            createAnimation();
            animator.reverse();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }
    }

}
