/*
 * Copyright (C) 2013 Manuel Peinado
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tarks.favorite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.connect.ImageDownloader;
import com.tarks.favorite.fadingactionbar.extras.actionbarsherlock.FadingActionBarHelper;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.global.Globalvariable;

public class ProfileActivity extends SherlockActivity {

	// Member srl
	String member_srl = "5";
	// Profile
	ImageView profile;

	// ListView
	ListView listView;
	// FadingActionbar
	FadingActionBarHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		helper = new FadingActionBarHelper()
				.actionBarBackground(R.drawable.ab_background)
				.headerLayout(R.layout.profile)
				.contentLayout(R.layout.activity_listview);
		setContentView(helper.createView(this));
		helper.initActionBar(this);

		getSupportActionBar().setTitle(
				Global.NameMaker(Global.getSetting("name_1", ""),
						Global.getSetting("name_2", "")));

		profile = (ImageView) findViewById(R.id.image_header);
		profile.setImageDrawable(Drawable.createFromPath(getCacheDir()
				.toString() + "/member/" + member_srl + ".jpg"));

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("profile_user_srl");
		Paramname.add("member_info");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(String.valueOf(member_srl));
		Paramvalue
				.add("tarks_account//name_1//name_2//gender//birthday//join_day//profile_update//lang//country");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "member/profile_info.php", mHandler, Paramname, Paramvalue,
				null, 1);
		setList();

	}

	public void setList() {
		listView = (ListView) findViewById(android.R.id.list);
		ArrayList<String> items = new ArrayList<String>();

		items.add("솔직히 오늘 무슨일이 있을지는 미지수입니다.");
		items.add("음 그래서 그래서 물어보는데 ");
		items.add("아따아");
		items.add("Once upon a midnight dreary");
		items.add("While I pondered weak and weary");
		items.add("Over many a quaint and curious volume of forgotten lore");
		items.add("Once upon a midnight dreary");
		items.add("While I pondered weak and weary");
		items.add("Over many a quaint and curious volume of forgotten lore");
		items.add("Once upon a midnight dreary");
		items.add("While I pondered weak and weary");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		listView.setAdapter(adapter);
	}

	/**
	 * @return A list of Strings read from the specified resource
	 */
	private ArrayList<String> loadItems(int rawResourceId) {
		try {
			ArrayList<String> countries = new ArrayList<String>();
			InputStream inputStream = getResources().openRawResource(
					rawResourceId);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				countries.add(line);
			}
			reader.close();
			return countries;
		} catch (IOException e) {
			return null;
		}
	}

	public void ProfileImageDownload() {
		new ImageDownloader(this, getString(R.string.server_path)
				+ "files/profile/" + member_srl + ".jpg", mHandler, 2);
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// IF Sucessfull no timeout

			if (msg.what == -1) {
				Global.ConnectionError(ProfileActivity.this);
			}

			if (msg.what == 1) {
				String[] array = msg.obj.toString().split("/LINE/.");

				String tarks_account = array[0];
				String name_1 = array[1];
				String name_2 = array[2];
				String gender = array[3];
				String birthday = array[4];
				String join_day = array[5];
				String profile_update = array[6];
				String lang = array[7];
				String country = array[8];

				if (Global.UpdateFileCache(profile_update,
						Global.getUser(member_srl, "0"),
						getString(R.string.server_path) + "files/profile/"
								+ member_srl + ".jpg", getCacheDir().toString()
								+ "/member/", member_srl + ".jpg")) {
					Global.SaveUserSetting(member_srl, profile_update);
					ProfileImageDownload();
					Log.i("test", "Let s profile image download");

				}
			}

			if (msg.what == 2) {
				// Save File cache
				Global.SaveBitmapToFileCache((Bitmap) msg.obj, getCacheDir()
						.toString() + "/member/", member_srl + ".jpg");
				// Set Profile
				profile.setImageDrawable(Drawable.createFromPath(getCacheDir()
						.toString() + "/member/" + member_srl + ".jpg"));
				Refresh();
			}

		}
	};

	public void Refresh() {
		helper = new FadingActionBarHelper()
				.actionBarBackground(R.drawable.ab_background)
				.headerLayout(R.layout.profile)
				.contentLayout(R.layout.activity_listview);
		setContentView(helper.createView(this));
		helper.initActionBar(this);

		setList();
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
}
