package com.tarks.favorite.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

import com.tarks.favorite.R;
import com.tarks.favorite.core.global.Global;
import com.tarks.favorite.ui.page.PageActivity;
import com.tarks.favorite.ui.page.PageInfo;

public class setting_compat extends PreferenceActivity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//
	//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		 getActionBar().setDisplayShowHomeEnabled(false);
		addPreferencesFromResource(R.layout.setting);



		/********** FEEDBACK ***********/
		Preference profile = findPreference("profile");
		profile.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent =  new Intent(setting_compat.this, PageInfo.class);
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
				Intent intent = new Intent(setting_compat.this, PageActivity.class);
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

				Global.FeedbackWrite(setting_compat.this);
				

				return false;
			}
		});

		/********** INFO ***********/
		Preference info = findPreference("info");
		info.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				startActivity(new Intent(setting_compat.this, com.tarks.favorite.ui.info.class));
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


}