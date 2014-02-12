
package com.desire.desireapidemos.app;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.desire.desireapidemos.R;

public class LoaderCustom extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();

        if (fm.findFragmentById(android.R.id.content) == null) {
            Fragment f = new AppListFragment();
            fm.beginTransaction().add(android.R.id.content, f).commit();
        }
    }

    public static class AppListFragment extends ListFragment implements OnQueryTextListener,
            LoaderCallbacks<List<AppEntry>> {

        AppListAdapter mAdapter;

        String mCurFilter;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            setEmptyText("No Applications");

            setHasOptionsMenu(true);

            mAdapter = new AppListAdapter(getActivity());
            setListAdapter(mAdapter);

            setListShown(false);

            getLoaderManager().initLoader(0, null, this);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

            // Place an action bar item for searching.
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            SearchView sv = new SearchView(getActivity());
            sv.setOnQueryTextListener(this);
            item.setActionView(sv);
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
            mAdapter.getFilter().filter(mCurFilter);
            return true;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("LoaderCustom", "Item clicked: " + id);
        }

        @Override
        public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
            return new AppListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
            mAdapter.setData(data);

            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<AppEntry>> loader) {
            mAdapter.setData(null);
        }

    }

    private static class AppListAdapter extends ArrayAdapter<AppEntry> {
        private final LayoutInflater mInflater;

        public AppListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            mInflater = LayoutInflater.from(context);
        }

        public void setData(List<AppEntry> data) {
            clear();
            if (data != null) {
                addAll(data);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (convertView == null) {
                v = mInflater.inflate(R.layout.list_item_icon_text, parent, false);
            } else {
                v = convertView;
            }

            AppEntry item = getItem(position);
            ((ImageView) v.findViewById(R.id.icon)).setImageDrawable(item.getIcon());
            ((TextView) v.findViewById(R.id.text)).setText(item.getLabel());

            return v;
        }

    }

    private static class AppEntry {
        private final AppListLoader mLoader;

        private final ApplicationInfo mInfo;

        private final File mApkFile;

        private String mLabel;

        private Drawable mIcon;

        private boolean mMounted;

        public AppEntry(AppListLoader loader, ApplicationInfo info) {
            mLoader = loader;
            mInfo = info;
            mApkFile = new File(info.sourceDir);
            loadLabel();
        }

        private void loadLabel() {
            if (mLabel == null || !mMounted) {
                if (mApkFile.exists()) {
                    mMounted = true;
                    CharSequence label = mInfo.loadLabel(mLoader.mPm);
                    mLabel = label == null ? mInfo.packageName : label.toString();
                } else {
                    mMounted = false;
                    mLabel = mInfo.packageName;
                }
            }
        }

        public ApplicationInfo getAppInfo() {
            return mInfo;
        }

        public String getLabel() {
            return mLabel;
        }

        @Override
        public String toString() {
            return mLabel;
        }

        public Drawable getIcon() {
            if (mIcon == null) {
                if (mApkFile.exists()) {
                    mIcon = mInfo.loadIcon(mLoader.mPm);
                    return mIcon;
                } else {
                    mMounted = false;
                }
            } else if (!mMounted) {
                // If the app wasn't mounted but is now mounted, reload
                // its icon.
                if (mApkFile.exists()) {
                    mMounted = true;
                    mIcon = mInfo.loadIcon(mLoader.mPm);
                    return mIcon;
                }
            } else {
                return mIcon;
            }

            return mLoader.getContext().getResources()
                    .getDrawable(android.R.drawable.sym_def_app_icon);
        }
    }

    private static Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<LoaderCustom.AppEntry>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(AppEntry item1, AppEntry item2) {
            return sCollator.compare(item1.mLabel, item2.mLabel);
        }
    };
    
    /**
     * Helper class to look for interesting changes to the installed apps
     * so that the loader can be updated.
     */
    public static class PackageIntentReceiver extends BroadcastReceiver {
        final AppListLoader mLoader;

        public PackageIntentReceiver(AppListLoader loader) {
            mLoader = loader;
            IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
            filter.addDataScheme("package");
            mLoader.getContext().registerReceiver(this, filter);
            // Register for events related to sdcard installation.
            IntentFilter sdFilter = new IntentFilter();
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
            mLoader.getContext().registerReceiver(this, sdFilter);
        }

        @Override public void onReceive(Context context, Intent intent) {
            // Tell the loader about the change.
            mLoader.onContentChanged();
        }
    }

    /**
     * Helper for determining if the configuration has changed in an interesting
     * way so we need to rebuild the app list.
     */
    public static class InterestingConfigChanges {
        final Configuration mLastConfiguration = new Configuration();
        int mLastDensity;

        boolean applyNewConfig(Resources res) {
            int configChanges = mLastConfiguration.updateFrom(res.getConfiguration());
            boolean densityChanged = mLastDensity != res.getDisplayMetrics().densityDpi;
            if (densityChanged || (configChanges&(ActivityInfo.CONFIG_LOCALE
                    |ActivityInfo.CONFIG_UI_MODE|ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
                mLastDensity = res.getDisplayMetrics().densityDpi;
                return true;
            }
            return false;
        }
    }

    private static class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {
        InterestingConfigChanges mConfig = new InterestingConfigChanges();
        PackageIntentReceiver mPackageObserver;
        final PackageManager mPm;
        List<AppEntry> mApps;

        public AppListLoader(Context context) {
            super(context);
            mPm = context.getPackageManager();
        }

        @Override
        public List<AppEntry> loadInBackground() {
            List<ApplicationInfo> apps = mPm
                    .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
                            | PackageManager.GET_DISABLED_COMPONENTS);
            if (apps == null) {
                apps = new ArrayList<ApplicationInfo>();
            }
            
            List<AppEntry> entries = new ArrayList<LoaderCustom.AppEntry>();
            for (int i = 0; i < apps.size(); i++) {
                AppEntry entry = new AppEntry(this, apps.get(i));
                entries.add(entry);
            }
            
            Collections.sort(entries, ALPHA_COMPARATOR);

            return entries;
        }

        @Override
        public void deliverResult(List<AppEntry> data) {
            
            if (isReset() && data != null) {
                onReleaseResources(data);
            }
            
            List<AppEntry> old = data;
            mApps = old;
            
            if (isStarted()) {
                super.deliverResult(data);
            }
            
            if (old != null) {
                onReleaseResources(old);
            }
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            
            if (mApps != null) {
                deliverResult(mApps);
            }
            
            if (mPackageObserver == null) {
                mPackageObserver = new PackageIntentReceiver(this);
            }
            
            boolean configChange = mConfig.applyNewConfig(getContext().getResources());
            
            if (takeContentChanged() || mApps == null || configChange) {
                forceLoad();
            }
        }


        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            cancelLoad();
        }

        @Override
        public void onCanceled(List<AppEntry> data) {
            super.onCanceled(data);
            
            onReleaseResources(data);
        }

        @Override
        protected void onReset() {
            super.onReset();
            
            stopLoading();
            
            if (mApps != null) {
                onReleaseResources(mApps);
                mApps = null;
            }
            
            if (mPackageObserver != null) {
                getContext().unregisterReceiver(mPackageObserver);
                mPackageObserver = null;
            }
        }

        /**
         * Helper function to take care of releasing resources associated with
         * an actively loaded data set.
         */
        protected void onReleaseResources(List<AppEntry> apps) {
            // For a simple List<> there is nothing to do. For something
            // like a Cursor, we would close it here.
        }

    }

}
