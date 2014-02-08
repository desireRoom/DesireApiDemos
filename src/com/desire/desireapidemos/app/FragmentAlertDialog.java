
package com.desire.desireapidemos.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.desire.desireapidemos.R;

public class FragmentAlertDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_fragment_alert_dialog);

        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText("Example of displaying an alert dialog with a DialogFragment");

        Button btn = (Button) findViewById(R.id.show);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    void showDialog() {
        MyAlertDialogFragment newFram = MyAlertDialogFragment.newInstance(R.string.alert_dialog_two_buttons_title);
        newFram.show(getFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }

    private static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("title", title);
            frag.setArguments(bundle);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");
            return new AlertDialog.Builder(getActivity()).setIcon(R.drawable.alert_dialog_icon)
                    .setTitle(title)
                    .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((FragmentAlertDialog)getActivity()).doNegativeClick();
                        }
                    })
                    .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((FragmentAlertDialog)getActivity()).doPositiveClick();
                        }
                    }).create();
        }

    }
}
