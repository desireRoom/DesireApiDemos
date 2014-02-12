
package com.desire.desireapidemos.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleCursorAdapter;

public class LoaderCursor extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();

        if (fm.findFragmentById(android.R.id.content) == null) {
            CursorLoaderListFragment f = new CursorLoaderListFragment();
            fm.beginTransaction().add(android.R.id.content, f).commit();
        }

    }

    public static class CursorLoaderListFragment extends ListFragment implements OnQueryTextListener, LoaderCallbacks<Cursor> {

        // These are the Contacts rows that we will retrieve.
        static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
            Contacts._ID, Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS, Contacts.CONTACT_PRESENCE, Contacts.PHOTO_ID, Contacts.LOOKUP_KEY,
        };

        private SimpleCursorAdapter mAdapter;

        private String mCurFilter;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            setEmptyText("No phone numbers!");

            setHasOptionsMenu(true);

            mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, null, new String[] {
                Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS
            }, new int[] {
                android.R.id.text1, android.R.id.text2
            }, 0);

            setListAdapter(mAdapter);

            setListShown(false);
            getLoaderManager().initLoader(0, null, this);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

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
            String newFilter = TextUtils.isEmpty(newText) ? null : newText;
            if (mCurFilter == null && newFilter == null) {
                return true;
            }

            if (mCurFilter != null && mCurFilter.equals(newFilter)) {
                return true;
            }

            mCurFilter = newFilter;
            getLoaderManager().restartLoader(0, null, this);
            return true;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);

            Log.i("FragmentComplexList", "Item clicked: " + id);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri baseUri;
            if (mCurFilter == null) {
                baseUri = Contacts.CONTENT_URI;
            } else {
                baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI, Uri.encode(mCurFilter));
            }

            String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND (" + Contacts.HAS_PHONE_NUMBER + "=1) AND (" + Contacts.DISPLAY_NAME + " != '' ))";
            return new CursorLoader(getActivity(), baseUri, CONTACTS_SUMMARY_PROJECTION, select, null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
            
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }

    }

}
