//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.page;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.tarks.favorite.MainActivity;
import com.tarks.favorite.R;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.connect.ImageDownloader;
import com.tarks.favorite.fadingactionbar.FadingActionBarHelperBase;
import com.tarks.favorite.fadingactionbar.extras.actionbarsherlock.FadingActionBarHelper;
import com.tarks.favorite.global.Global;

public class ProfileActivity extends SherlockActivity {

	// Profile image local path
	String local_path;
	// User name
	String title;
	// Member srl
	String member_srl = "0";
	int write_status;
	int your_status;
	int me_status;
	int page_admin;
	int user_srl;
	// String profile_user_srl = "0";
	// Profile
	ImageView profile;

	// ListView
	ListView listView;
	// FadingActionbar
	FadingActionBarHelper helper;
	// Menu state
	private boolean add_menu_state = false;
	// List
	ArrayList<List> m_orders = new ArrayList<List>();
	// Define ListAdapter
	ListAdapter m_adapter;
	// Menu
	private Menu optionsMenu;
	private String[] member;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Can use progress
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 getSupportActionBar().setDisplayShowHomeEnabled(false);

		// Get Intent
		Intent intent = getIntent();// 인텐트 받아오고
		member_srl = intent.getStringExtra("member_srl");
		user_srl = Integer.parseInt(Global.getSetting("user_srl", "0"));
		Log.i("member_srl", member_srl);
		load();

	}

	public void load() {
		setFadingActionBar();
		// local_path = getCacheDir().toString()
		// + "/member/";
		try {
			local_path = getCacheDir().toString() + "/member/";
		} catch (Exception e) {
		}
		profile = (ImageView) findViewById(R.id.image_header);
		try {
			profile.setImageDrawable(Drawable.createFromPath(local_path
					+ member_srl + ".jpg"));
		} catch (Exception e) {
		}

		getProfileInfo();

		setListAdapter();

		getDocList("0");
	}

	public void getProfileInfo() {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);

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
				.add("tarks_account//write_status//admin//name_1//name_2//gender//birthday//join_day//profile_pic//profile_update//lang//country");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "member/profile_info.php", mHandler, Paramname, Paramvalue,
				null, 1, 0);
	}


	public void setFadingActionBar() {
		helper = new FadingActionBarHelper()
				.actionBarBackground(R.drawable.ab_background)
				.headerLayout(R.layout.profile)
				.contentLayout(R.layout.activity_listview);
		setContentView(helper.createView(this));
		helper.initActionBar(this);
		helper.initContext(this);

	}

	public void setListAdapter() {
		// profile = (ImageView) findViewById(R.id.image_header);
		listView = (ListView) findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg0.getAdapter() instanceof HeaderViewListAdapter) {
					if (((HeaderViewListAdapter) arg0.getAdapter())
							.getWrappedAdapter() instanceof ListAdapter) {
						ListAdapter ca = (ListAdapter) ((HeaderViewListAdapter) arg0
								.getAdapter()).getWrappedAdapter();

						List ls = (List) ca.getItem(arg2 - 1);

						Intent intent = new Intent(ProfileActivity.this,
								document_read.class);
						intent.putExtra("doc_srl",
								String.valueOf(ls.getDocSrl()));
						startActivityForResult(intent, 1);

					}
				}

			}
		});
		// listView.setOnScrollListener(this);
		m_adapter = new ListAdapter(this, R.layout.profile_list, m_orders);

		// Set Profile
		profile.setImageDrawable(Drawable.createFromPath(local_path
				+ member_srl + ".jpg"));

		listView.setAdapter(m_adapter);

	}

	public void setList(int doc_srl, String user_srl, String name,
			String contents, int status) {

		// Get Profile
		// getMemberInfo(user_srl);

		List p1 = new List(user_srl, name, contents, 1, doc_srl, status);
		m_orders.add(p1);

		// ListView listview = (ListView) findViewById(R.id.listView1);

	}

	public void getDocList(String startdoc) {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("doc_user_srl");
		Paramname.add("start_doc");
		Paramname.add("doc_number");
		Paramname.add("doc_info");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("0");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(member_srl);
		Paramvalue.add(startdoc);
		Paramvalue.add("15");
		Paramvalue.add("srl//user_srl//name//content//status");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/documents_app_read.php", mHandler, Paramname,
				Paramvalue, null, 3, 0);
	}

	public void MoreLoad(String number) {
		if (listView.getLastVisiblePosition() >= listView.getCount() - 1
				&& listView.getChildAt(0).getTop() != 0) {
			getDocList(number);
		}

	}

	public void getMemberInfo(String user_srl) {
		if (Global.getUpdatePossible(user_srl)) {
			// Log.i("Update", "Updateing");
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
			Paramvalue.add(String.valueOf(user_srl));
			Paramvalue.add("profile_pic//profile_update");

			new AsyncHttpTask(this, getString(R.string.server_path)
					+ "member/profile_info.php", mHandler, Paramname,
					Paramvalue, null, 4, Integer.parseInt(user_srl));

		}
	}

	public void addFavorite(String user_srl) {

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("category");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("value");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("3");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(user_srl);

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "favorite/favorite_app_add.php", mHandler, Paramname,
				Paramvalue, null, 6, Integer.parseInt(user_srl));

	}

	public void getPageAuthCode(String user_srl) {

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("value");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("get_page_auth");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(user_srl);

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "member/page_auth_app.php", mHandler, Paramname, Paramvalue,
				null, 7, Integer.parseInt(user_srl));

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
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);
		new ImageDownloader(this, getString(R.string.server_path)
				+ "files/profile/" + member_srl + ".jpg", mHandler, 2, 0);
	}

	public void ProfileUserImageDownload(String user_srl) {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);
		new ImageDownloader(this, getString(R.string.server_path)
				+ "files/profile/thumbnail/" + user_srl + ".jpg", mHandler, 5,
				Integer.parseInt(user_srl));
	}

	public void MemberInfoError() {
		Global.Infoalert(this, getString(R.string.error),
				getString(R.string.member_info_error_des),
				getString(R.string.yes));
	}

	public void ChangeUserAlert(final String user_srl) {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				ProfileActivity.this);
		builder.setMessage(getString(R.string.change_user_des)).setTitle(
				getString(R.string.change_user));
		builder.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						ChangeUserAct(user_srl);
					}
				});
		builder.setNegativeButton(getString(R.string.no), null);
		builder.show();

	}

	public void ChangeUserAct(String user_srl) {
		// Setting Editor
		SharedPreferences edit = getSharedPreferences("setting", MODE_PRIVATE);
		SharedPreferences.Editor editor = edit.edit();
		editor.putString("default_user", "N");
		editor.putString("default_user_srl", Global.getSetting("user_srl", "0"));
		editor.putString("default_user_srl_auth",
				Global.getSetting("user_srl_auth", "null"));
		editor.putString("user_srl", member_srl);
		editor.putString("user_srl_auth", user_srl);

		// Commit
		editor.commit();

		MainActivity.INSTANCE.restartApplication();

	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			setSupportProgressBarIndeterminateVisibility(false);
			// IF Sucessfull no timeout

			if (msg.what == -1) {
				Global.ConnectionError(ProfileActivity.this);
			}

			if (msg.what == 1) {

				try {
					String[] array = msg.obj.toString().split("/LINE/.");
				//	Global.dumpArray(array);
					String tarks_account = array[0];
					write_status = Integer.parseInt(array[1]);
					page_admin = Integer.parseInt(array[2]);
					String name_1 = array[3];
					String name_2 = array[4];
					String gender = array[5];
					String birthday = array[6];
					String join_day = array[7];
					String profile_pic = array[8];
					String profile_update = array[9];
					String lang = array[10];
					String country = array[11];
					your_status = Integer.parseInt(array[12]);
					me_status = Integer.parseInt(array[13]);

					title = Global.NameMaker(lang, name_1, name_2);

					getSupportActionBar().setTitle(title);

					if (Global.UpdateMemberFileCache(member_srl,
							profile_update, profile_pic)) {
						Global.SaveUserSetting(member_srl, profile_update,
								null, profile_pic);
						ProfileImageDownload();
						// Log.i("test", "Let s profile image download");

					}
					if (profile_pic.matches("N")) {
						File file = new File(local_path + member_srl + ".jpg");
						file.delete();
						profile.setImageDrawable(null);
					}

					if (me_status < 3) {
						add_menu_state = true;
					}
					invalidateOptionsMenu();
				} catch (Exception e) {
					MemberInfoError();

				}
			}

			if (msg.what == 2) {
				// Save File cache
				try {
					Global.SaveBitmapToFileCache((Bitmap) msg.obj, local_path,
							member_srl + ".jpg");

					// Set Profile
					profile.setImageDrawable(Drawable.createFromPath(local_path
							+ member_srl + ".jpg"));
					// Refresh();
				} catch (Exception e) {
				}
			}

			if (msg.what == 3) {
				try {
					// Log.i("Doc", msg.obj.toString());
					String[] doc = msg.obj.toString().split("/DOC/.");

					for (int i = 0; i < doc.length; i++) {
						String[] array = doc[i].split("/LINE/.");
						// Global.dumpArray(array);
						String srl = array[0];
						String user_srl = array[1];
						String name = array[2];
						String content = array[3];
						String status = array[4];

						// Log.i("user", user_srl);
						getMemberInfo(user_srl);
						setList(Integer.parseInt(srl), user_srl, name, content,
								Integer.parseInt(status));
						m_adapter.notifyDataSetChanged();
			

					}
					//Unlock listview
					FadingActionBarHelperBase.mLockListView = false;
				} catch (Exception e) {

				}
			}

			if (msg.what == 4) {

				try {

					String[] array = msg.obj.toString().split("/LINE/.");
					// Global.dumpArray(array);
					String profile_pic = array[0];
					String profile_update = array[1];

					String user_srl = String.valueOf(msg.arg1);
					Global.SaveUserSetting(user_srl, null,
							String.valueOf(Global.getCurrentTimeStamp()),
							profile_pic);

					if (profile_pic.matches("Y")) {
						// Global.SaveUserSetting(user_srl, profile_update);
						ProfileUserImageDownload(user_srl);
						// Log.i("test", "Let s profile image download");

					}
					if (profile_pic.matches("N")) {
						File file = new File(local_path + user_srl + ".jpg");
						file.delete();
						File file_thum = new File(local_path + "thumbnail/"
								+ user_srl + ".jpg");
						file_thum.delete();
					}
				} catch (Exception e) {
					// MemberInfoError();

				}
			}

			if (msg.what == 5) {
				// Save File cache
				try {
					// Log.i("Save", msg.arg1 + "");
					Global.SaveBitmapToFileCache((Bitmap) msg.obj, local_path
							+ "thumbnail/", msg.arg1 + ".jpg");

					m_adapter.notifyDataSetChanged();

					// Set Profile
					// profile.setImageDrawable(Drawable.createFromPath(local_path
					// + member_srl + ".jpg"));
					// Refresh();
				} catch (Exception e) {
				}
			}

			if (msg.what == 6) {
				String result = msg.obj.toString();
				if (result.matches("favorite_add_succeed")) {
					Global.toast(getString(R.string.added));
					add_menu_state = false;
					invalidateOptionsMenu();
				} else {
					Global.ConnectionError(ProfileActivity.this);
				}
				// Log.i("Result","로그 정상 작동");
				// Log.i("Result", msg.obj.toString());

			}

			if (msg.what == 7) {
				ChangeUserAlert(msg.obj.toString());

			}

		}
	};

	// public void Refresh() {
	//
	// setList();
	// }

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
					// Log.i("test", p.getStatus() + "kk" +p.getTitle() );
					// Status not public
					if (p.getStatus() > 1) {
						tt.setTextColor(Color.GRAY);
					} else {
						tt.setTextColor(Color.BLACK);
					}

				}
				if (bt != null) {
					bt.setText(Global.getValue(p.getDes()));
				}
				if (image != null) {

					boolean state = Global.CheckFileState(local_path
							+ "thumbnail/" + p.getUserSrl() + ".jpg");

					if (state) {
						image.setImageDrawable(Drawable
								.createFromPath(local_path + "thumbnail/"
										+ p.getUserSrl() + ".jpg"));
					} else {
						image.setImageResource(R.drawable.person);
					}
					image.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(ProfileActivity.this,
									ProfileActivity.class);
							intent.putExtra("member_srl", p.getUserSrl());
							startActivity(intent);
						}
					});
				}
			}
			return v;
		}
	}

	class List {

		private String user_srl;
		private String Title;
		private String Description;
		private int Tag;
		private int Doc_srl;
		private int status;

		public List(String _user_Srl, String _Title, String _Description,
				int _Tag, int _Doc_srl, int _status) {
			this.user_srl = _user_Srl;
			this.Title = _Title;
			this.Description = _Description;
			this.Tag = _Tag;
			this.Doc_srl = _Doc_srl;
			this.status = _status;
		}

		public String getUserSrl() {
			return user_srl;
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

		public int getDocSrl() {
			return Doc_srl;
		}

		public int getStatus() {
			return status;
		}

	}

	// @Override
	// public void onScroll(AbsListView view, int firstVisibleItem,
	// int visibleItemCount, int totalItemCount)
	// {
	// helper.ScrollListener(activity);
	//
	// // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
	// // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
	// int count = totalItemCount - visibleItemCount;
	//
	//
	//
	// if(firstVisibleItem >= count && totalItemCount != 0
	// && mLockListView == false)
	// {
	// Log.i("로그", "Loading next items");
	// addItems(totalItemCount);
	// }
	// }

	/**
	 * 임의의 방법으로 더미 아이템을 추가합니다.
	 * 
	 * @param size
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.optionsMenu = menu;
		MenuItem item;

		menu.add(0, 0, 0, getString(R.string.add_favorite))
				.setIcon(R.drawable.add)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 1, 0, getString(R.string.write)).setIcon(R.drawable.write)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add(0, 100, 0, getString(R.string.change_user)).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);

		
		menu.add(0, 2, 0, getString(R.string.information)).setIcon(R.drawable.ic_menu_light).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);

		menu.findItem(0).setVisible(add_menu_state);
		menu.findItem(1).setVisible(write_status <= your_status);
		menu.findItem(100).setVisible(page_admin == user_srl);
		if(title != null && title.length() < 20) menu.findItem(2).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// item = menu.add(0, 1, 0, R.string.Main_MenuAddBookmark);
		// item.setIcon(R.drawable.ic_menu_add_bookmark);

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			// setListAdapter();
			m_adapter.clear();
			load();
		}

	}

	// 빽백키 상단액션바
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			// Alert
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ProfileActivity.this);
			builder.setMessage(getString(R.string.favorite_add_des)).setTitle(
					getString(R.string.add_favorite));
			builder.setPositiveButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							addFavorite(member_srl);
						}
					});
			builder.setNegativeButton(getString(R.string.no), null);
			builder.show();
			return true;
		case 1:
			Intent intent1 = new Intent(ProfileActivity.this,
					document_write.class);
			intent1.putExtra("page_srl", member_srl);
			intent1.putExtra("page_name", title);
			startActivityForResult(intent1, 1);

			return true;
		case 2:
			Intent intent2 = new Intent(ProfileActivity.this, ProfileInfo.class);
			intent2.putExtra("member_srl", member_srl);
			startActivityForResult(intent2, 1);
			return true;
		case 100:
			getPageAuthCode(member_srl);
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
}
