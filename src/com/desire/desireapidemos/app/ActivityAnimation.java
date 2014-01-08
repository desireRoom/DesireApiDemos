
package com.desire.desireapidemos.app;

import com.desire.desireapidemos.R;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActivityAnimation extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_animation);

        findViewById(R.id.fade_animation).setOnClickListener(this);
        findViewById(R.id.zoom_animation).setOnClickListener(this);
        findViewById(R.id.modern_fade_animation).setOnClickListener(this);
        findViewById(R.id.modern_zoom_animation).setOnClickListener(this);
        findViewById(R.id.scale_up_animation).setOnClickListener(this);
        findViewById(R.id.zoom_thumbnail_animation).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fade_animation:
                startActivity(new Intent(this, AlertDialogs.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                break;
            case R.id.zoom_animation:
                startActivity(new Intent(this, AlertDialogs.class));
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                break;
            case R.id.modern_fade_animation:
                ActivityOptions opts = ActivityOptions.makeCustomAnimation(this, R.anim.fade, R.anim.hold);
                startActivity(new Intent(this, AlertDialogs.class), opts.toBundle());
                break;
            case R.id.modern_zoom_animation:
                ActivityOptions opts1 = ActivityOptions.makeCustomAnimation(this, R.anim.zoom_enter, R.anim.zoom_enter);
                startActivity(new Intent(this, AlertDialogs.class), opts1.toBundle());
                break;
            case R.id.scale_up_animation:
                ActivityOptions opts2 = ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
                startActivity(new Intent(this, AlertDialogs.class), opts2.toBundle());
                break;
            case R.id.zoom_thumbnail_animation:
                v.setDrawingCacheEnabled(true);
                v.setPressed(false);
                v.refreshDrawableState();
                Bitmap bm = v.getDrawingCache();
                Canvas c = new Canvas(bm);
                ActivityOptions opts3 = ActivityOptions.makeThumbnailScaleUpAnimation(v, bm, 0, 0);
                startActivity(new Intent(this, AlertDialogs.class), opts3.toBundle());
                v.setDrawingCacheEnabled(false);
                break;
        }
    }

}
