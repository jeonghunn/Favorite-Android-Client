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
package com.tarks.favorite.page;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.tarks.favorite.R;
import com.tarks.favorite.R.drawable;
import com.tarks.favorite.R.id;
import com.tarks.favorite.R.layout;
import com.tarks.favorite.R.string;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.connect.ImageDownloader;
import com.tarks.favorite.fadingactionbar.extras.actionbarsherlock.FadingActionBarHelper;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.global.Globalvariable;

public class ProfileActivity extends SherlockActivity {

	// Profile image local path
	String local_path;
	//User name
	String title;
	// Member srl
	String member_srl = "0";
	// Profile
	ImageView profile;

	// ListView
	ListView listView;
	// FadingActionbar
	FadingActionBarHelper helper;

	// List
	ArrayList<List> m_orders = new ArrayList<List>();
	// Define ListAdapter
	ListAdapter m_adapter;
	// Menu
	private Menu optionsMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Get Intent
		Intent intent = getIntent();// 인텐트 받아오고
		member_srl = intent.getStringExtra("member_srl");

		helper = new FadingActionBarHelper()
				.actionBarBackground(R.drawable.ab_background)
				.headerLayout(R.layout.profile)
				.contentLayout(R.layout.activity_listview);
		setContentView(helper.createView(this));
		helper.initActionBar(this);

		// local_path = getCacheDir().toString()
		// + "/member/";
		try {
			local_path = getCacheDir().toString() + "/member/";
		} catch (Exception e) {
		}
		profile = (ImageView) findViewById(R.id.image_header);
		profile.setImageDrawable(Drawable.createFromPath(local_path
				+ member_srl + ".jpg"));

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
				.add("tarks_account//name_1//name_2//gender//birthday//join_day//profile_pic//profile_update//lang//country");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "member/profile_info.php", mHandler, Paramname, Paramvalue,
				null, 1);
		setList();

	}

	public void setList() {
		listView = (ListView) findViewById(android.R.id.list);
		m_adapter = new ListAdapter(this, R.layout.profile_list, m_orders);

		for (int i = 0; i <= 10; i++) {
			List p1 = new List("최진영", "헤헤헿헤헤헿헤ㅔ헿헤헤헤", 1, 1);
			m_orders.add(p1);

			List p2 = new List("이진오", "그래서요 음 어 그러게", 1, 1);
			m_orders.add(p2);
		}

		listView.setAdapter(m_adapter);

		// ListView listview = (ListView) findViewById(R.id.listView1);

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

	public void MemberInfoError() {
		Global.Infoalert(this, getString(R.string.error),
				getString(R.string.member_info_error_des),
				getString(R.string.yes));
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// IF Sucessfull no timeout

			if (msg.what == -1) {
				Global.ConnectionError(ProfileActivity.this);
			}

			if (msg.what == 1) {

				try {
					String[] array = msg.obj.toString().split("/LINE/.");
					Global.dumpArray(array);
					String tarks_account = array[0];
					String name_1 = array[1];
					String name_2 = array[2];
					String gender = array[3];
					String birthday = array[4];
					String join_day = array[5];
					String profile_pic = array[6];
					String profile_update = array[7];
					String lang = array[8];
					String country = array[9];

					title = Global.NameMaker(lang, name_1, name_2);

					getSupportActionBar().setTitle(title);

					if (Global.UpdateFileCache(profile_update,
							Global.getUser(member_srl, "0"),
							getString(R.string.server_path) + "files/profile/"
									+ member_srl + ".jpg", local_path,
							member_srl + ".jpg")
							&& profile_pic.matches("Y")) {
						Global.SaveUserSetting(member_srl, profile_update);
						ProfileImageDownload();
						// Log.i("test", "Let s profile image download");

					}
					if (profile_pic.matches("N")) {
						File file = new File(local_path + member_srl + ".jpg");
						file.delete();
						profile.setImageDrawable(null);
					}
				} catch (Exception e) {
					MemberInfoError();

				}
			}

			if (msg.what == 2) {
				// Save File cache
				try {
					Global.SaveBitmapToFileCache((Bitmap) msg.obj, local_path,
							member_srl + ".jpg");
					Global.createThumbnail((Bitmap) msg.obj, local_path
							+ "thumbnail/", member_srl + ".jpg");

					// Set Profile
					profile.setImageDrawable(Drawable.createFromPath(local_path
							+ member_srl + ".jpg"));
					Refresh();
				} catch (Exception e) {
				}
			}

		}
	};

	public void Refresh() {

		setList();
	}

	private class ListAdapter extends ArrayAdapter<List> {

		private ArrayList<List> items;

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<List> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.profile_list, null);
			}
			final List p = items.get(position);
			if (p != null) {
				TextView tt = (TextView) v.findViewById(R.id.titre);
				TextView bt = (TextView) v.findViewById(R.id.description);
				ImageView image = (ImageView) v.findViewById(R.id.img);
				if (tt != null) {
					tt.setText(p.getTitle());
				}
				if (bt != null) {
					bt.setText(p.getDes());
				}
				if (image != null) {
					image.setImageDrawable(Drawable.createFromPath(local_path
							+ "thumbnail/" + member_srl + ".jpg"));
					image.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

						}
					});
				}
			}
			return v;
		}
	}

	class List {

		private String Title;
		private String Description;
		private int Tag;
		private int Position;

		public List(String _Title, String _Description, int _Tag, int _Position) {
			this.Title = _Title;
			this.Description = _Description;
			this.Tag = _Tag;
			this.Position = _Position;
		}

		public String getTitle() {
			return Title;
		}

		public String getDes() {
			return Description;
		}

		public int getTag() {
			return Tag;
		}

		public int getPos() {
			return Position;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.optionsMenu = menu;
		MenuItem item;

		menu.add(0, 0, 0, getString(R.string.write)).setIcon(R.drawable.write)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 1, 0, getString(R.string.information)).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);

		// item = menu.add(0, 1, 0, R.string.Main_MenuAddBookmark);
		// item.setIcon(R.drawable.ic_menu_add_bookmark);

		return true;
	}

	// 빽백키 상단액션바
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(ProfileActivity.this,
					document_write.class);
			startActivity(intent);
			return true;
		case 1:
			Intent intent2 = new Intent(ProfileActivity.this,
					ProfileInfo.class);
			  intent2.putExtra("member_srl", member_srl);
			startActivity(intent2);
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
}
