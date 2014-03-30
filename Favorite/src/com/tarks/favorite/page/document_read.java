package com.tarks.favorite.page;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.tarks.favorite.R;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.fadingactionbar.extras.actionbarsherlock.FadingActionBarHelper;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.page.ProfileActivity.List;
public class document_read extends SherlockActivity {

	
	String local_path;
	//Profile
	ImageView profile;
	TextView profile_title;
	TextView profile_des;
	TextView comment_count;
	// Member srl
	String doc_srl = "0";
	
	// ListView
		ListView listView;

		// List
		ArrayList<List> m_orders = new ArrayList<List>();
		// Define ListAdapter
		ListAdapter m_adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doclistview);
      //액션바백버튼가져오기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
       
    	// Get Intent
		Intent intent = getIntent();// 인텐트 받아오고
		doc_srl = intent.getStringExtra("doc_srl");
        
		try {
			local_path = getCacheDir().toString() + "/member/";
		} catch (Exception e) {
		}
		
		//Set List Adapter
		listView = (ListView) findViewById(R.id.listView1);
		 // Header, Footer 생성 및 등록
       View header = getLayoutInflater().inflate(R.layout.profile_avatar_layout, null, false);
       profile = (ImageView) header.findViewById(R.id.img);
       profile_title = (TextView) header.findViewById(R.id.title);
       profile_des = (TextView) header.findViewById(R.id.description);
       comment_count = (TextView) header.findViewById(R.id.comment_count);
       
  //     profile_edit.setOnClickListener(l)

       listView.addHeaderView(header);

		m_adapter = new ListAdapter(this, R.layout.list, m_orders);
        
        getDoc();
    }
    
	public void getDoc() {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);
		
		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("doc_srl");
		Paramname.add("doc_info");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("1");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(doc_srl);
		Paramvalue.add("srl//user_srl//name//content//status");


		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/documents_app_read.php", mHandler, Paramname, Paramvalue,
				null, 1,0);
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
							+ "thumbnail/" + p.getUserSrl() + ".jpg"));
					image.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(document_read.this, ProfileActivity.class);
							  intent.putExtra("member_srl",p.getUserSrl());
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
		private String Date;
		private int Tag;
		private int Position;

		public List(String _user_Srl, String _Title, String _Description, String _Date, int _Tag, int _Position) {
			this.user_srl = _user_Srl;
			this.Title = _Title;
			this.Description = _Description;
			this.Tag = _Tag;
			this.Position = _Position;
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

		public int getPos() {
			return Position;
		}

	}
	
	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			setSupportProgressBarIndeterminateVisibility(false);
			// IF Sucessfull no timeout

			if (msg.what == -1) {
				Global.ConnectionError(document_read.this);
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

					if (Global.UpdateMemberFileCache(member_srl, profile_update, profile_pic)) {
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
				
				}
			}

		}
	};
    
      //빽백키 상단액션바
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
