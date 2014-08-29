
package com.desire.desireapidemos.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.desire.desireapidemos.R;

public class ActivityPresentation extends Activity implements OnCheckedChangeListener,
        OnClickListener {
    private final String TAG = "ActivityPresentation";

    private static final String PRESENTATION_KEY = "presentation";

    private static final int[] PHOTOS = new int[] {
            R.drawable.frantic, R.drawable.photo1, R.drawable.photo2, R.drawable.photo3,
            R.drawable.photo4, R.drawable.photo5, R.drawable.photo6, R.drawable.sample_4
    };

    private DisplayManager mDisplayManager;

    private DisplayListAdapter mAdapter;

    private CheckBox mShowAllDisplaysCheckbox;

    private ListView mListView;

    private int mNextImageNumber;

    private SparseArray<PresentationContents> mSavedPresentationContents;

    private SparseArray<DemoPresentation> mActivePresentations = new SparseArray<DemoPresentation>();

    private DisplayManager.DisplayListener mDisplayListener = new DisplayListener() {

        @Override
        public void onDisplayRemoved(int displayId) {
            Log.d(TAG, "Display #" + displayId + " removed.");
            mAdapter.updateContents();

        }

        @Override
        public void onDisplayChanged(int displayId) {
            Log.d(TAG, "Display #" + displayId + " changed.");
            mAdapter.updateContents();

        }

        @Override
        public void onDisplayAdded(int displayId) {
            Log.d(TAG, "Display #" + displayId + " added.");
            mAdapter.updateContents();

        }
    };

    private DialogInterface.OnDismissListener mDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            DemoPresentation demo = (DemoPresentation) dialog;
            int displayId = demo.getDisplay().getDisplayId();
            Log.d(TAG, "Presentation on display #" + displayId + " was dismissed.");
            mActivePresentations.delete(displayId);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSavedPresentationContents = savedInstanceState
                    .getSparseParcelableArray(PRESENTATION_KEY);
        } else {
            mSavedPresentationContents = new SparseArray<PresentationContents>();
        }

        mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);

        setContentView(R.layout.app_activity_presentation);

        mShowAllDisplaysCheckbox = (CheckBox) findViewById(R.id.show_all_displays);
        mShowAllDisplaysCheckbox.setOnCheckedChangeListener(this);

        mListView = (ListView) findViewById(R.id.display_list);
        mAdapter = new DisplayListAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAdapter.updateContents();

        int numDisplay = mAdapter.getCount();
        for (int i = 0; i < numDisplay; i++) {
            final Display display = mAdapter.getItem(i);
            final PresentationContents content = mSavedPresentationContents.get(display
                    .getDisplayId());
            if (content != null) {
                showPresentation(display, content);
            }
        }
        mSavedPresentationContents.clear();

        mDisplayManager.registerDisplayListener(mDisplayListener, null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mDisplayManager.unregisterDisplayListener(mDisplayListener);
        Log.d(TAG, "Activity is being paused.  Dismissing all active presentation.");

        for (int i = 0; i < mActivePresentations.size(); i++) {
            DemoPresentation demo = mActivePresentations.valueAt(i);
            int displayId = mActivePresentations.keyAt(i);
            mSavedPresentationContents.put(displayId, demo.dContent);
            demo.dismiss();
        }
        mActivePresentations.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSparseParcelableArray(PRESENTATION_KEY, mSavedPresentationContents);
    }

    @Override
    public void onClick(View v) {
        Context c = v.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        final Display display = (Display) v.getTag();
        Resources res = c.getResources();
        AlertDialog alert = builder
                .setTitle(
                        res.getString(R.string.presentation_alert_info_text, display.getDisplayId()))
                .setMessage(display.toString())
                .setNegativeButton(R.string.presentation_alert_dismiss_text,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
        alert.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mShowAllDisplaysCheckbox) {
            mAdapter.updateContents();
        } else {
            final Display display = (Display) buttonView.getTag();
            if (isChecked) {
                PresentationContents content = new PresentationContents(getNextPhoto());
                showPresentation(display, content);
            } else {
                hidePresentation(display);
            }
        }
    }

    private int getNextPhoto() {
        final int photo = mNextImageNumber;
        mNextImageNumber = (photo + 1) % PHOTOS.length;
        return photo;
    }

    private void showPresentation(Display display, PresentationContents content) {
        final int displayId = display.getDisplayId();
        if (mActivePresentations.get(displayId) != null) {
            return;
        }

        Log.d(TAG, "Showing presentation photo #" + content.photo + " on display #" + displayId
                + ".");
        DemoPresentation demo = new DemoPresentation(this, display, content);
        demo.show();
        demo.setOnDismissListener(mDismissListener);
        mActivePresentations.put(displayId, demo);
    }

    private void hidePresentation(Display display) {
        final int displayId = display.getDisplayId();
        DemoPresentation demo = mActivePresentations.get(displayId);
        if (demo == null) {
            return;
        }

        Log.d(TAG, "Dismissing presentation on display #" + displayId + ".");
        demo.dismiss();
        mActivePresentations.delete(displayId);
    }

    private class DisplayListAdapter extends ArrayAdapter<Display> {
        final Context dContext;

        public DisplayListAdapter(Context context) {
            super(context, R.layout.presentation_list_item);
            dContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View v;
            if (convertView == null) {
                v = LayoutInflater.from(dContext).inflate(R.layout.presentation_list_item, null);
            } else {
                v = convertView;
            }

            final Display display = getItem(position);
            final int displayId = display.getDisplayId();

            CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox_presentation);
            cb.setTag(display);
            cb.setOnCheckedChangeListener(ActivityPresentation.this);
            cb.setChecked(mActivePresentations.indexOfKey(displayId) >= 0
                    || mSavedPresentationContents.indexOfKey(displayId) >= 0);

            TextView tv = (TextView) v.findViewById(R.id.display_id);
            tv.setText(v.getContext().getResources()
                    .getString(R.string.presentation_display_id_text, displayId, display.getName()));

            Button b = (Button) v.findViewById(R.id.info);
            b.setTag(display);
            b.setOnClickListener(ActivityPresentation.this);
            return v;
        }

        public void updateContents() {
            clear();

            String displayCategory = getDisplayCategory();
            Display[] displays = mDisplayManager.getDisplays(displayCategory);
            addAll(displays);

            Log.d(TAG, "There are currently " + displays.length + " displays connected.");
            for (Display display : displays) {
                Log.d(TAG, "  " + display);
            }
        }

        private String getDisplayCategory() {
            return mShowAllDisplaysCheckbox.isChecked() ? null
                    : DisplayManager.DISPLAY_CATEGORY_PRESENTATION;
        }

    }

    private final class DemoPresentation extends Presentation {
        private PresentationContents dContent;

        public DemoPresentation(Context outerContext, Display display, PresentationContents content) {
            super(outerContext, display);
            dContent = content;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Resources res = getContext().getResources();
            setContentView(R.layout.presentation_content);

            final Display display = getDisplay();
            final int displayId = display.getDisplayId();
            final int photo = dContent.photo;

            TextView tv = (TextView) findViewById(R.id.text);
            tv.setText(res.getString(R.string.presentation_photo_text, photo, displayId,
                    display.getName()));

            ImageView iv = (ImageView) findViewById(R.id.image);
            iv.setImageDrawable(res.getDrawable(PHOTOS[photo]));

            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);

            Point p = new Point();
            getDisplay().getSize(p);
            drawable.setGradientRadius(Math.max(p.x, p.y) / 2);
            drawable.setColors(dContent.colors);
            findViewById(android.R.id.content).setBackground(drawable);
        }

    }

    private final static class PresentationContents implements Parcelable {
        final int photo;

        final int[] colors;

        public static final Creator<PresentationContents> CREATOR = new Creator<PresentationContents>() {

            @Override
            public PresentationContents createFromParcel(Parcel source) {
                return new PresentationContents(source);
            }

            @Override
            public PresentationContents[] newArray(int size) {
                return new PresentationContents[size];
            }

        };

        public PresentationContents(int photo) {
            this.photo = photo;
            colors = new int[] {
                    ((int) (Math.random() * Integer.MAX_VALUE)) | 0xFF000000,
                    ((int) (Math.random() * Integer.MAX_VALUE)) | 0xFF000000
            };
        }

        public PresentationContents(Parcel in) {
            photo = in.readInt();
            colors = new int[] {
                    in.readInt(), in.readInt()
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(photo);
            dest.writeInt(colors[0]);
            dest.writeInt(colors[1]);
        }

    }

}
