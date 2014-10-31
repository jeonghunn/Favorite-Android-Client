//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.ui.page;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.tarks.favorite.ui.GalleryView;
import com.tarks.favorite.R;
import com.tarks.favorite.core.connect.AsyncHttpTask;
import com.tarks.favorite.core.connect.ImageDownloader;
import com.tarks.favorite.core.global.Global;

public class PageInfo extends ActionBarActivity {

	String local_path;
	String member_srl = "0";
	String title;
	String profile_pic;
	private int your_status, me_status;
	private boolean profile_changed = false;
	// ListView
	ListView listView;

	// Edit
	ImageButton profile_edit;
	// Profile
	ImageView profile;
	// TextView
	TextView profile_title;
	TextView profile_des;

	// List
	ArrayList<List> m_orders = new ArrayList<List>();
	// Define ListAdapter
	ListAdapter m_adapter;
	
	private Map<String, String> resultmap;

	// self
	boolean self_profile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Can use progress
     //   supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
		//supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.listview);
		// 액션바백버튼가져오기
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 getSupportActionBar().setDisplayShowHomeEnabled(false);
		Load();

	}

	public void Load() {
		local_path = getCacheDir().toString() + "/member/";
		// Get Intent
		Intent intent = getIntent();// 인텐트 받아오고
		member_srl = intent.getStringExtra("member_srl");

		// IF self profile
		// self_profile = Global.getSetting("user_srl",
		// "0").matches(member_srl);
		// Set List Adapter
		listView = (ListView) findViewById(R.id.listView1);
		// Header, Footer 생성 및 등록
		View header = getLayoutInflater().inflate(
				R.layout.profile_avatar_layout, null, false);
		profile = (ImageView) header.findViewById(R.id.profile_img);
		ImageButton profile_button = (ImageButton) header
				.findViewById(R.id.edit_info);
		profile_edit = (ImageButton) header.findViewById(R.id.edit);
		profile_title = (TextView) header.findViewById(R.id.title);
		profile_des = (TextView) header.findViewById(R.id.description);
		profile.setImageDrawable(Drawable.createFromPath(local_path
				+ "thumbnail/" + member_srl + ".jpg"));

		// profile_edit.setOnClickListener(l)
		profile_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Log.i("Clicked", "Profile");
				if (profile_pic.matches("Y")) {
					Intent intent = new Intent(PageInfo.this,
							GalleryView.class);
					intent.putExtra("path", local_path + member_srl + ".jpg");
					startActivity(intent);
				}
			}
		});
		profile_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(PageInfo.this, PageEdit.class);
				intent1.putExtra("member_srl", member_srl);
				startActivityForResult(intent1, 1);
			}
		});

		listView.addHeaderView(header);



		
	
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

						
						//Phone Number
					if(ls.getCategory().matches("phone_number")){
						Intent intent = new Intent(Intent.ACTION_DIAL);
						intent.setData(Uri.parse("tel:" + ls.getDes()));
						startActivity(intent); 
					}

					}
				}

			}
		});
		
		m_adapter = new ListAdapter(this, R.layout.list, m_orders);
		setProfileInfo();

	}

	public void setProfileInfo() {
		// Start Progressbar
		m_adapter.clear();
		setSupportProgressBarIndeterminateVisibility(true);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("apiv");
		Paramname.add("a");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("page_srl");
		Paramname.add("member_info");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("1");
		Paramvalue.add("page_info");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(String.valueOf(member_srl));
		Paramvalue
				.add("tarks_account//admin//name_1//name_2//gender//birthday//country_code//phone_number//join_day//profile_pic//profile_update//lang//country//like_me//favorite//rel_you_status//rel_me_status");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "api.php", mHandler, Paramname, Paramvalue,
				null, 1, 0);
	}

	public void setProfileList(int like_me, String profile_pic) {

		setProfile(profile_pic);

		// number cut
		NumberFormat nf = NumberFormat.getInstance();
		// nf.setMaximumIntegerDigits(5); //최대수 지정
		String result = nf.format(like_me);

		if (like_me != 0) {
			int plural = like_me > 1 ? 2 : 1;
			profile_des.setText(String.format(
					getResources()
							.getQuantityString(R.plurals.like_him, plural),
					result));
		}

	}

	public void setProfile(String profile_pic) {
		profile.setImageResource(R.drawable.person);
		if (profile_pic.matches("Y"))
			profile.setImageDrawable(Drawable.createFromPath(local_path
					+ "thumbnail/" + member_srl + ".jpg"));
		profile_title.setText(title);

		if (!self_profile) {
			profile_edit.setOnClickListener(null);
			profile_edit.setBackgroundResource(R.drawable.transparent);
			profile_edit.setImageResource(R.drawable.transparent);
		}
	}

	public void setList() {


		
		
//		String tarks_account = [0array];
//		String admin = array[1];
//		String name_1 = array[2];
//		String name_2 = array[3];
//		String gender = array[4];
//		String birthday = array[5];
//		String country_code = array[6];
//		String phone_number = array[7];
//		String join_day = array[8];
//		String profile_pic = array[9];
//		String profile_update = array[10];
//		String lang = array[11];
//		String country = array[12];
//		String like_me = array[13];
//		String favorite = array[14];
//		int your_status = Integer.parseInt(array[15]);
//		int me_status = Integer.parseInt(array[16]);
		
		String tarks_account = String.valueOf(resultmap.get("tarks_account"));
		String admin = String.valueOf(resultmap.get("admin"));
		String name_1 = String.valueOf(resultmap.get("name_1"));
		String name_2 = String.valueOf(resultmap.get("name_2"));
		String gender = String.valueOf(resultmap.get("gender"));
		String birthday = String.valueOf(resultmap.get("birthday"));
		String country_code = String.valueOf(resultmap.get("country_code"));
		String phone_number = String.valueOf(resultmap.get("phone_number"));
		String join_day = String.valueOf(resultmap.get("join_day"));
		profile_pic = String.valueOf(resultmap.get("profile_pic"));
		String profile_update = String.valueOf(resultmap.get("profile_update"));
		String lang = String.valueOf(resultmap.get("lang"));
		String country = String.valueOf(resultmap.get("country"));
		String like_me = String.valueOf(resultmap.get("like_me"));
		String favorite = String.valueOf(resultmap.get("favorite"));
		int your_status =  Integer.parseInt(String.valueOf(resultmap.get("rel_you_status")));
		int me_status = Integer.parseInt(String.valueOf(resultmap.get("rel_me_status")));

		self_profile = your_status == 4;
		setProfileList(Integer.parseInt(like_me), profile_pic);
		if (!tarks_account.matches("null"))
			AddList(getString(R.string.tarks_account), tarks_account, "tarks_account");
		if (!gender.matches("0"))
			AddList(getString(R.string.gender),
					gender.matches("1") ? getString(R.string.male)
							: getString(R.string.female), "gender");
		if (!birthday.matches("null") && !birthday.matches("0"))
			AddList(getString(R.string.birthday), birthday, "birthday");
		if (!join_day.matches("null"))
			AddList(getString(R.string.join), Global.getDate(join_day), "join_day");
		if (!phone_number.matches("null") && !phone_number.matches("0") && !phone_number.matches("")){
			String plus_mark = "+";
			if(country_code.matches("0")) plus_mark = "";
			
			AddList(getString(R.string.phone_number), plus_mark + country_code
					+ phone_number, "phone_number");
		}

		// if(!tarks_account.matches("null"))
		// AddList(getString(R.string.tarks_account) , tarks_account);
		// if(!tarks_account.matches("null"))
		// AddList(getString(R.string.tarks_account) , tarks_account);
		
		

		listView.setAdapter(m_adapter);

	}

	public void deleteFavoirte() {
		// Show Alert
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.warning));
		alert.setMessage(getString(R.string.delete_favorite_des));
		alert.setPositiveButton(getString(R.string.unfavorite),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						deleteFavoriteAct(member_srl);
					}
				});
		alert.setNegativeButton(getString(R.string.no), null);
		alert.show();

	}

	public void deleteFavoriteAct(String user_srl) {

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("category");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("value");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("favorite_delete");
		Paramvalue.add("3");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(user_srl);

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "favorite/favorite_app.php", mHandler, Paramname, Paramvalue,
				null, 3, Integer.parseInt(user_srl));

	}

	public void AddList(String title, String description, String category) {
		List p1 = new List(title, description, category);
		m_orders.add(p1);
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
				v = vi.inflate(R.layout.list_info, null);
			}
			final List p = items.get(position);
			if (p != null) {
				TextView tt = (TextView) v.findViewById(R.id.title);
				TextView des = (TextView) v.findViewById(R.id.description);

				if (tt != null) {
					tt.setText(p.getTitle());
				}

				if (des != null) {
					des.setText(p.getDes());
				}

			}
			return v;
		}
	}

	class List {

		private String Title;
		private String Description;
		private String category;
		
		public List(String _Title, String _Description, String _Category) {
			this.Title = _Title;
			this.Description = _Description;
			this.category = _Category;
		}

		public String getTitle() {
			return Title;
		}

		public String getDes() {
			return Description;
		}
		
		public String getCategory() {
			return category;
		}

	}

	public void ProfileImageDownload() {
		new ImageDownloader(this, getString(R.string.server_path)
				+ "files/profile/thumbnail/" + member_srl + ".jpg", mHandler,
				2, 0);
	}

	public void PageInfoError() {
		Global.Infoalert(this, getString(R.string.error),
				getString(R.string.unknown_info_error_des),
				getString(R.string.yes));
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			setSupportProgressBarIndeterminateVisibility(false);
			// IF Sucessfull no timeout

			if (msg.what == -1) {
				Global.ConnectionError(PageInfo.this);
			}

			if (msg.what == 1) {
				try {
					resultmap = Global.getJSONArray(msg.obj.toString());
					// Global.dumpArray(array);

//					String tarks_account = array[0];
//					String admin = array[1];
//					String name_1 = array[2];
//					String name_2 = array[3];
//					String gender = array[4];
//					String birthday = array[5];
//					String country_code = array[6];
//					String phone_number = array[7];
//					String join_day = array[8];
//					profile_pic = array[9];
//					String profile_update = array[10];
//					String lang = array[11];
//					String country = array[12];
//					String like_me = array[13];
//					String favorite = array[14];
//					your_status = Integer.parseInt(array[15]);
//					me_status = Integer.parseInt(array[16]);
					
					String tarks_account = String.valueOf(resultmap.get("tarks_account"));
					String admin = String.valueOf(resultmap.get("admin"));
					String name_1 = String.valueOf(resultmap.get("name_1"));
					String name_2 = String.valueOf(resultmap.get("name_2"));
					String gender = String.valueOf(resultmap.get("gender"));
					String birthday = String.valueOf(resultmap.get("birthday"));
					String country_code = String.valueOf(resultmap.get("country_code"));
					String phone_number = String.valueOf(resultmap.get("phone_number"));
					String join_day = String.valueOf(resultmap.get("join_day"));
					profile_pic = String.valueOf(resultmap.get("profile_pic"));
					String profile_update = String.valueOf(resultmap.get("profile_update"));
					String lang = String.valueOf(resultmap.get("lang"));
					String country = String.valueOf(resultmap.get("country"));
					String like_me = String.valueOf(resultmap.get("like_me"));
					String favorite = String.valueOf(resultmap.get("favorite"));
					your_status =  Integer.parseInt(String.valueOf(resultmap.get("rel_you_status")));
					me_status = Integer.parseInt(String.valueOf(resultmap.get("rel_me_status")));

					title = Global.NameMaker(lang, name_1, name_2);

					if (Global.UpdateFileCache(profile_update, Global.getUser(
							member_srl, "profile_update_thumbnail"),
							getString(R.string.server_path)
									+ "files/profile/thumbnail/" + member_srl
									+ ".jpg", local_path, member_srl + ".jpg")
							&& profile_pic.matches("Y")) {
						Global.SaveUserSetting(member_srl, null,
								profile_update, profile_pic);
						ProfileImageDownload();
						// Log.i("test", "Let s profile image download");

					}

					if (profile_pic.matches("N")) {

						File file = new File(local_path + member_srl + ".jpg");
						file.delete();
						File file_thum = new File(local_path + "thumbnail/"
								+ member_srl + ".jpg");
						file_thum.delete();
						// profile.setImageDrawable(R.drawable.people);
						profile.setImageResource(R.drawable.person);
						// profile.setId(null);
						// profile.setImageDrawable(res.getDrawable(R.drawable.person));
					}

					setList();
					invalidateOptionsMenu(); // refreshmenu

				} catch (Exception e) {
					PageInfoError();
					e.printStackTrace();
				}
			}

			if (msg.what == 2) {
				// Save File cache
				try {
					Global.SaveBitmapToFileCache((Bitmap) msg.obj, local_path
							+ "thumbnail/", member_srl + ".jpg");

					// setList();
					setProfile(profile_pic);

				} catch (Exception e) {
				}
			}

			if (msg.what == 3) {
				if(msg.obj.toString().matches("favorite_delete_succeed")){
					Global.toast(getString(R.string.completed));
					setProfileInfo();
					profile_changed = true;
				}else{
					Global.ConnectionError(PageInfo.this);
				}

			}

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			setProfileInfo();
			profile_changed = true;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// this.optionsMenu = menu;
		MenuItemCompat.setShowAsAction(	menu.add(0, 1, 0, getString(R.string.unfavorite)), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);



		menu.findItem(1).setVisible(me_status == 3);
		// item = menu.add(0, 1, 0, R.string.Main_MenuAddBookmark);
		// item.setIcon(R.drawable.ic_menu_add_bookmark);

		return true;
	}

	public void onBackPressed() {

		if (profile_changed) {
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	// 빽백키 상단액션바
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			deleteFavoirte();
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
