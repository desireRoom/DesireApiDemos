package com.desire.desireapidemos.animation;

import com.desire.desireapidemos.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Events extends Activity {
	private TextView mStartT, mRepeatT, mCancelT, mEndT;
	private TextView mStartAT, mRepeatAT, mCancelAT, mEndAT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.animation_events);
	}
	
	
	public class MyAnimationView extends View {

		public MyAnimationView(Context context) {
			super(context);
			
		}
		
	}

}
