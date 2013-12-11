package com.desire.desireapidemos.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;

import com.desire.desireapidemos.R;

public class DefaultLayoutAnimations extends Activity {
	private int mNum = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_default_layout);
		
		final GridLayout container = (GridLayout) findViewById(R.id.grid_container);
		Button add = (Button) findViewById(R.id.add_btn);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Button btn = new Button(DefaultLayoutAnimations.this);
				btn.setText(String.valueOf(mNum++));
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						container.removeView(v);
					}
				});
				
				container.addView(btn, Math.min(1, container.getChildCount()));
			}
		});
	}

}
