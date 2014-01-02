
package com.desire.desireapidemos.app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.desire.desireapidemos.R;

public class ActionBarShareActionProvider extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        copyPrivateRawResuorceToPubliclyAccessibleFile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_share_action_provider, menu);
        MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
        ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        actionProvider.setShareIntent(createIntent());
        
        MenuItem overflowItem = menu.findItem(R.id.menu_item_share_action_provider_overflow);
        ShareActionProvider overflowProvider = (ShareActionProvider) actionItem.getActionProvider();
        overflowProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        overflowProvider.setShareIntent(createIntent());
        return true;
    }

    private Intent createIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return shareIntent;
    }
    
    private void copyPrivateRawResuorceToPubliclyAccessibleFile() {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = getResources().openRawResource(R.raw.robot);
            fos = openFileOutput("shared.png", Context.MODE_WORLD_READABLE | Context.MODE_APPEND);
            byte[] buffer = new byte[1024];
            int length = 0;
            try {
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0 , length);
                }
            } catch (IOException e) {
            }
            
        } catch (Exception e) {
        } finally {
            try {
                is.close();
            } catch (Exception e2) {
            }
            try {
                fos.close();
            } catch (Exception e2) {
            }
        }
    }
}
