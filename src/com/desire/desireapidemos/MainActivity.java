package com.desire.desireapidemos;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String path = intent.getStringExtra("com.desire.apis.path");
		if (path == null) {
			path = "";
		}

		setListAdapter(new SimpleAdapter(this, getData(path), android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 }));
	}

	private List<Map<String, Object>> getData(String path) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN);
		mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);

		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
		if (list != null) {
			String[] prefixPath;
			String prefixWithSlash = path;

			if (path.equals("")) {
				prefixPath = null;
			} else {
				prefixPath = path.split("/");
				prefixWithSlash = path + "/";
			}

			int len = list.size();
			Map<String, Boolean> entries = new HashMap<String, Boolean>();

			for (int i = 0; i < len; i++) {
				ResolveInfo info = list.get(i);
				CharSequence labelSeq = info.loadLabel(pm);
				String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;

				if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {
					String[] labelPath = label.split("/");
					String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];

					if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
						addItem(data, nextLabel,
								activityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
					} else {
						if (entries.get(nextLabel) == null) {
							addItem(data, nextLabel, browseIntent(prefixWithSlash + nextLabel));
							entries.put(nextLabel, true);
						}
					}
				}
			}
			Collections.sort(data, sDisplayNameComparator);
		}
		return data;
	}

	private final static Comparator<Map<String, Object>> sDisplayNameComparator = new Comparator<Map<String, Object>>() {
		private final Collator collator = Collator.getInstance();
		@Override
		public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
			return collator.compare(lhs.get("title"), rhs.get("title"));
		}

	};

	private Intent activityIntent(String pkg, String componentName) {
		Intent intent = new Intent();
		intent.setClassName(pkg, componentName);
		return intent;
	}

	private Intent browseIntent(String path) {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		intent.putExtra("com.desire.apis.path", path);
		return intent;
	}

	private void addItem(List<Map<String, Object>> data, String name, Intent intent) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", name);
		map.put("intent", intent);
		data.add(map);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
		Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}
	
}
