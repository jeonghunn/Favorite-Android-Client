package com.tarks.favorite;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

@SuppressWarnings("deprecation")
public class setting extends SherlockPreferenceActivity {
	private boolean mIsBackKeyPressed = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		addPreferencesFromResource(R.layout.setting);



		/********** NOTICE ***********/
//		Preference notice = findPreference("notice");
//		notice.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				Intent intent = new Intent(setting.this, webview.class);
//				intent.putExtra("url", "http://tarks.net/fastengine_notice/?l="
//						+ getString(R.string.lang));
//				startActivity(intent);
//				return false;
//			}
//		});

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

				// System info
				String s = "Device info:";
				s += "\n OS Version: " + System.getProperty("os.version") + "("
						+ android.os.Build.VERSION.INCREMENTAL + ")";
				s += "\n OS API Level: " + android.os.Build.VERSION.SDK;
				s += "\n Device: " + android.os.Build.DEVICE;
				s += "\n Model (and Product): " + android.os.Build.MODEL + " ("
						+ android.os.Build.PRODUCT + ")";

				// Send email
				Intent Email = new Intent(Intent.ACTION_SEND);
				Email.setType("text/email");
				Email.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "main@tarks.net" });
				Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
				Email.putExtra(Intent.EXTRA_TEXT, s);
				startActivity(Intent.createChooser(Email,
						getString(R.string.send_feedback)));
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