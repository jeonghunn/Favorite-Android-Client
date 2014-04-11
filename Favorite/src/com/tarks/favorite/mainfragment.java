package com.tarks.favorite;

import java.io.File;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Window;
import com.tarks.favorite.cardsui.MyPlayCard;
import com.tarks.favorite.cardsui.like_card;
import com.tarks.favorite.cardsui.profile_card;
import com.tarks.favorite.cardsui.views.CardUI;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.connect.ImageDownloader;
import com.tarks.favorite.fadingactionbar.extras.actionbarsherlock.FadingActionBarHelper;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.page.ProfileActivity;
import com.tarks.favorite.page.document_read;
import com.tarks.favorite.page.document_write;
import com.tarks.favorite.test.phone_number;
import com.tarks.widget.listviewutil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class mainfragment extends SherlockFragment implements
		OnItemLongClickListener {

	// Profile image local path
	String local_path;

	private ListView maListViewPerso;
	private ScrollView sv;
	// 카드 유아이 정의한다.
	CardUI mCardView;
	View rootView;

	// On déclare la HashMap qui contiendra les informations pour un item
	HashMap<String, String> map;

	// Like me or favorite
	String like_me_result;
	String favorite_result;
	
	//User content
	ArrayList<String> user_content_array = new ArrayList<String>();
	ArrayList<String> user_name_array = new ArrayList<String>();
	// ListView
	ListView listView;
	// List
	ArrayList<List> m_orders = new ArrayList<List>();
	// Define ListAdapter
	ListAdapter m_adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.listview2, container, false);

		// + "/member/";
		try {
			local_path = getActivity().getCacheDir().toString() + "/member/";
		} catch (Exception e) {
		}

		setfavorite();

	
		setListAdapter();

		m_adapter = new ListAdapter(getActivity(), R.layout.profile_list,
				m_orders);
		listView.setAdapter(m_adapter);
		loadFavorite(Global.getSetting("user_srl", "0"));
		// Set Favorite

		// Import ListView

		return rootView;
	}

	public void setListAdapter() {
		listView = (ListView) rootView.findViewById(R.id.listView1);
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

						Intent intent = new Intent(getActivity(),
								ProfileActivity.class);
						intent.putExtra("member_srl", ls.getUserSrl());
						startActivity(intent);

					}
				}

			}
		});

		View header = getActivity().getLayoutInflater().inflate(
				R.layout.like_card, null, false);
		TextView favorite_tv = (TextView) header
				.findViewById(R.id.favorite_textView);
		TextView like_me_tv = (TextView) header
				.findViewById(R.id.like_me_textView);

		favorite_tv.setText(favorite_result);
		like_me_tv.setText(like_me_result);
		// profile_edit.setOnClickListener(l)
		listView.addHeaderView(header, null, false);
	//	listView.addHeaderView(header);
		

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setfavorite() {
		// 자신의 신분 설정값을 불러옵니다.
		SharedPreferences prefs = getActivity().getSharedPreferences("setting",
				getActivity().MODE_PRIVATE);
		int like_me = Integer.parseInt(prefs.getString("like_me", "0"));
		int favorite = Integer.parseInt(prefs.getString("favorite", "0"));

		// Import textview
		TextView favorite_text = (TextView) rootView
				.findViewById(R.id.textView4);
		TextView like_me_text = (TextView) rootView
				.findViewById(R.id.textView3);

		// number cut
		NumberFormat nf = NumberFormat.getInstance();
		// nf.setMaximumIntegerDigits(5); //최대수 지정
		like_me_result = nf.format(like_me);
		favorite_result = nf.format(favorite);

		if (like_me > 9999)
			like_me_result = "9999+";
		if (favorite > 9999)
			favorite_result = "9999+";

		// loadFavorite(Global.getSetting("user_srl", "0"));

	}

	public void setheader() {
		like_card header = (new like_card(like_me_result, favorite_result,
				false));

		mCardView.addCard(header);
	}

	public void setList(String user_srl, String title, String des) {
		List p1 = new List(user_srl, title, des, 0);
		m_orders.add(p1);

	}

	public void loadFavorite(String user_srl) {

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("category");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("value");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("2");
		Paramvalue.add("3");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(user_srl);

		new AsyncHttpTask(getActivity(), getString(R.string.server_path)
				+ "favorite/favorite_app_read.php", mHandler, Paramname,
				Paramvalue, null, 1, Integer.parseInt(user_srl));

	}
	
	public void loadUsers(ArrayList<String> users_srl) {

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("users_srl");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("4");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(Global.arraylistToString(users_srl, "//"));

		new AsyncHttpTask(getActivity(), getString(R.string.server_path)
				+ "board/documents_app_read.php", mHandler, Paramname,
				Paramvalue, null, 2, 0);

	}
	
	public void getMemberInfo(String user_srl) {
		if (Global.getCurrentTimeStamp()
				- Integer.parseInt(Global.getUser(user_srl, "profile_update")) > 8000 || Global.CheckFileState(local_path + user_srl +  ".jpg")  == false && Global.getUser(user_srl, "profile_pic").matches("Y")) {
Log.i("Update", "Updateing");
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

			new AsyncHttpTask(getActivity(), getString(R.string.server_path)
					+ "member/profile_info.php", mHandler, Paramname,
					Paramvalue, null, 3, Integer.parseInt(user_srl));

		}
	}
	
	public void ProfileUserImageDownload(String user_srl) {
		// Start Progressbar
	//	setSupportProgressBarIndeterminateVisibility(true);
		new ImageDownloader(getActivity(), getString(R.string.server_path)
				+ "files/profile/" + user_srl + ".jpg", mHandler, 4,
				Integer.parseInt(user_srl));
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// IF Sucessfull no timeout
			// rootView.setSupportProgressBarIndeterminateVisibility(false);
			if (msg.what == -1) {
				Global.ConnectionError(getActivity());
			}

			if (msg.what == 1) {
				String result = msg.obj.toString();
				
				Log.i("Result", msg.obj.toString());
				try{
				ArrayList<String> ar = new ArrayList<String>();
				 String[] profile = result.split("/PFILE/.");
					for (int i = 0; i < profile.length; i++) {
						 String[] users = profile[i].split("/LINE/.");
						user_content_array.add(users[0]);
						user_name_array.add(users[1]);
					}
				// Global.dumpArray(array);
				loadUsers(user_content_array);
				}catch(Exception e){
					
				}
              
			}
			
			if (msg.what == 2) {
				try{
				String result = msg.obj.toString();
				Log.i("Result", msg.obj.toString());
				String[] array = result.split("/LINE/.");
				for (int i = 0; i < user_content_array.size(); i++) {
					getMemberInfo(String.valueOf(user_content_array.get(i)));
					setList(user_content_array.get(i), user_name_array.get(i), array[i]);
					m_adapter.notifyDataSetChanged();
				}
				} catch (Exception e){
					
				}
			}
			
			if (msg.what == 3) {
				try {

					String[] array = msg.obj.toString().split("/LINE/.");
					Global.dumpArray(array);
					String profile_pic = array[0];
					String profile_update = array[1];

					String user_srl = String.valueOf(msg.arg1);
			//		Log.i("Gett", user_srl + "");
					Global.SaveUserSetting(user_srl,String.valueOf(Global.getCurrentTimeStamp()), profile_pic);
					
					Global.SaveUserSetting(user_srl,profile_update, profile_pic);

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
			
			if (msg.what == 4) {
				try {
					Log.i("Save", msg.arg1 + "");
					Global.SaveBitmapToFileCache((Bitmap) msg.obj, local_path,
							msg.arg1 + ".jpg");
					Global.createThumbnail((Bitmap) msg.obj, local_path
							+ "thumbnail/", msg.arg1 + ".jpg");

					m_adapter.notifyDataSetChanged();

					// Set Profile
					// profile.setImageDrawable(Drawable.createFromPath(local_path
					// + member_srl + ".jpg"));
					// Refresh();
				} catch (Exception e) {
				}
			}

		}
	};

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
				LayoutInflater vi = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
					bt.setText(Global.getValue(p.getDes()));
				}
				if (image != null) {

					image.setImageDrawable(Drawable.createFromPath(local_path
							+ "thumbnail/" + p.getUserSrl() + ".jpg"));
					image.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),
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

		public List(String _user_Srl, String _Title, String _Description,
				int _Tag) {
			this.user_srl = _user_Srl;
			this.Title = _Title;
			this.Description = _Description;
			this.Tag = _Tag;
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

	}

}
