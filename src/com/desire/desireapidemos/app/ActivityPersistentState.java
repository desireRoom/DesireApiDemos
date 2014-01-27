package com.desire.desireapidemos.app;

import com.desire.desireapidemos.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView.BufferType;

public class ActivityPersistentState extends Activity {
    
    private EditText mSaved;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.app_activity_persistent_state);
        
        mSaved = (EditText) findViewById(R.id.saved);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        SharedPreferences prefs = getPreferences(Activity.MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            mSaved.setText(restoredText, BufferType.EDITABLE);

            int selectionStart = prefs.getInt("selection-start", -1);
            int selectionEnd = prefs.getInt("selection-end", -1);
            if (selectionStart != -1 && selectionEnd != -1) {
                mSaved.setSelection(selectionStart, selectionEnd);
            }
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        Editor editor = getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putString("text", mSaved.getText().toString());
        editor.putInt("selection-start", mSaved.getSelectionStart());
        editor.putInt("selection-end", mSaved.getSelectionEnd());
        editor.commit();
    }

}
