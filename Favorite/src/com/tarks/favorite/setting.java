package com.tarks.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.page.ProfileActivity;
import com.tarks.favorite.page.ProfileInfo;

@SuppressWarnings("deprecation")
public class setting extends SherlockPreferenceActivity {
	private boolean mIsBackKeyPressed = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 getSupportActionBar().setDisplayShowHomeEnabled(false);
		addPreferencesFromResource(R.layout.setting);



		/********** FEEDBACK ***********/
		Preference profile = findPreference("profile");
		profile.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(setting.this, ProfileInfo.class);
				  intent.putExtra("member_srl", Global.getSetting("user_srl", "0"));
				startActivity(intent);	

				return false;
			}
		});
		
		/********** NOTICE ***********/
		Preference notice = findPreference("notice");
		notice.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(setting.this, ProfileActivity.class);
				  intent.putExtra("member_srl", "9");
				startActivity(intent);	
				return false;
			}
		});

		/********** HELP ***********/
//		Preference help = findPreference("help");
//		help.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				Intent intent = new Intent(setting.this, webview.class);
//				intent.putExtra("url", "http://tarks.net/fastengine_help/?l="
//						+ getString(R.string.lang));
//				startActivity(intent);
//				return false;
//			}
//		});

		/********** FEEDBACK ***********/
		Preference feedback = findPreference("feedback");
		feedback.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {

				Global.FeedbackWrite(setting.this);
				

				return false;
			}
		});

		/********** INFO ***********/
		Preference info = findPreference("info");
		info.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				startActivity(new Intent(setting.this, info.class));
				return false;
			}
		});

//		final CheckBoxPreference chk = (CheckBoxPreference) findPreference("searchmode");
//		chk.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference,
//					Object newValue) {
//				// If Checked
//				// if (!chk.isChecked()) {
//				//
//				// //Make Notification
//				// Intent intent1 = new Intent(setting.this,
//				// MainActivity.class);
//				//
//				// CNotification.addNotification(setting.this, intent1, 0,
//				// R.drawable.ic_stat_fastengine,
//				// getString(R.string.searchnow),
//				// getString(R.string.app_name),
//				// getString(R.string.searchnow));
//				// } else {
//				// // No Check ACC
//				// NotificationManager notificationManager =
//				// (NotificationManager)
//				// getSystemService(Context.NOTIFICATION_SERVICE);
//				// notificationManager.cancel(0);
//				// }
//				return true;
//			}
//		});

	}

	@Override
	public void onBackPressed() {
		if (mIsBackKeyPressed == false) {
			mIsBackKeyPressed = true;
			// startActivity(new Intent(setting.this, main.class));
			finish();
			// Intent in = getBaseContext().getPackageManager()
			// .getLaunchIntentForPackage(
			// getBaseContext().getPackageName());
			// in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(in);
			// System.exit(-1);
		}

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}