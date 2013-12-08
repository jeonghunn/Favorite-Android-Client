package com.tarks.favorite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.ShareActionProvider;

public class result_view extends SherlockActivity {

	TextView people_text;
	String people;
	String kind;
	String result_value;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_result);
		// 액션바백버튼가져오기
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// get Intent
		Intent intent = getIntent(); // 인텐트 받아오고
		people = intent.getStringExtra("people"); // 인텐트로 부터 데이터 가져오고
		kind = intent.getStringExtra("kind"); // 인텐트로 부터 데이터 가져오고
		result_value = intent.getStringExtra("result_value"); // 인텐트로 부터 데이터 가져오고

		// Import Textview
		people_text = (TextView) findViewById(R.id.textView1);
		TextView kind_Text = (TextView) findViewById(R.id.textView3);

		// setText
		kind_Text.setText(kind);
		// Number Effect
		new Thread(new Runnable() {
			public void run() {
				int i = 0;
				while (true) {
					if (i > Integer.parseInt(people)) {
						break;
					} else {
						Message msg = handler.obtainMessage();
						msg.arg1 = i;
						handler.sendMessage(msg);
					}
					try {
						if (Integer.parseInt(people) - i < 10) {
							Thread.sleep(600);

						} else {
							Thread.sleep(50);
						}
						i += 1;
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
			}
		}).start();

	}

	// 빽백키 상단액션바
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate your menu.
		getSupportMenuInflater().inflate(R.menu.share_action_provider, menu);

		// Set file with share history to the provider and set the share intent.
		MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		actionProvider.setShareIntent(createShareIntent());

		// XXX: For now, ShareActionProviders must be displayed on the action
		// bar
		// Set file with share history to the provider and set the share intent.
		// MenuItem overflowItem =
		// menu.findItem(R.id.menu_item_share_action_provider_overflow);
		// ShareActionProvider overflowProvider =
		// (ShareActionProvider) overflowItem.getActionProvider();
		// overflowProvider.setShareHistoryFileName(
		// ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		// overflowProvider.setShareIntent(createShareIntent());

		return true;
	}

	/**
	 * Creates a sharing {@link Intent}.
	 * 
	 * @return The sharing intent.
	 */
	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addCategory(Intent.CATEGORY_DEFAULT);

		shareIntent.putExtra(Intent.EXTRA_TEXT, result_value);

		shareIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));

		shareIntent.setType("text/plain");
		return shareIntent;
	}

	// 핸들러
	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			people_text.setText(String.valueOf(msg.arg1));
		}

	};
}
