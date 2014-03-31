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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;
import com.tarks.favorite.R;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.connect.ImageDownloader;
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
	TextView doc_content;
	// Member srl
	String doc_srl = "0";
	String user_srl = "0";
	//Edittext
	EditText comment_edittext;
	//ImageButton
	ImageButton send_button;
	//Button
	Button previous_comments;
	
	// ListView
		ListView listView;

		// List
		ArrayList<List> m_orders = new ArrayList<List>();
		// Define ListAdapter
		ListAdapter m_adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	// Can use progress
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
        setContentView(R.layout.doclistview);
      //액션바백버튼가져오기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
       
    	// Get Intent
		Intent intent = getIntent();// 인텐트 받아오고
		doc_srl = intent.getStringExtra("doc_srl");
        Log.i("Doc srl", doc_srl + "");
		try {
			local_path = getCacheDir().toString() + "/member/";
		} catch (Exception e) {
		}
		
		//Set List Adapter
		listView = (ListView) findViewById(R.id.listView1);
		 // Header, Footer 생성 및 등록
       View header = getLayoutInflater().inflate(R.layout.doclist_header, null, false);
       profile = (ImageView) header.findViewById(R.id.img);
       profile_title = (TextView) header.findViewById(R.id.title);
       profile_des = (TextView) header.findViewById(R.id.description);
       doc_content = (TextView) header.findViewById(R.id.content);
       comment_count = (TextView) header.findViewById(R.id.comment_count);
       
  //     profile_edit.setOnClickListener(l)
       
       profile_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GoPage(user_srl);
			}
		});

	
    
    profile.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			GoPage(user_srl);
		}
	});
    
  
   
      

       listView.addHeaderView(header);
       
       //Comment
       comment_edittext = (EditText) findViewById(R.id.comment_edittext);
       send_button = (ImageButton) findViewById(R.id.send);
       
       send_button.setOnClickListener(new OnClickListener() {

   		@Override
   		public void onClick(View v) {
   			CommentPostAct();
   		}
   	});
       previous_comments = (Button) findViewById(R.id.previous_comments_button); 
       previous_comments.setVisibility(View.GONE);
		m_adapter = new ListAdapter(this, R.layout.list, m_orders);
		listView.setAdapter(m_adapter);
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
		Paramvalue.add("page_srl//user_srl//name//title//content//date//status//privacy//comments//recommend//negative");


		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/documents_app_read.php", mHandler, Paramname, Paramvalue,
				null, 1,0);
	}
	
	public void getCommentsList(String startdoc) {
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
	//	Paramvalue.add(member_srl);
		Paramvalue.add(startdoc);
		Paramvalue.add("15");
		Paramvalue.add("srl//user_srl//name//content//status");


		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/comment_app_read.php", mHandler, Paramname, Paramvalue,
				null, 3,0);
	}
	
	
	public void getMemberInfo(String user_srl){
		if(Global.getCurrentTimeStamp() - Integer.parseInt(Global.getUser(user_srl, "0")) > 8000){
		
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
		Paramvalue
				.add("profile_pic//profile_update");
		
	

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "member/profile_info.php", mHandler, Paramname, Paramvalue,
				null, 2, Integer.parseInt(user_srl));
		Global.SaveUserSetting(user_srl, Long.toString(Global.getCurrentTimeStamp()));
		}
	}
	
	public void GoPage(String user_srl){
		Intent intent = new Intent(document_read.this, ProfileActivity.class);
		  intent.putExtra("member_srl", user_srl);
		startActivity(intent);	
	}
	
	public void ProfileUserImageDownload(String user_srl) {
		// Start Progressbar
				setSupportProgressBarIndeterminateVisibility(true);
		new ImageDownloader(this, getString(R.string.server_path)
				+ "files/profile/" + user_srl + ".jpg", mHandler, 3, Integer.parseInt(user_srl));
	}
	
	
	public void CommentPostAct() {
		setSupportProgressBarIndeterminateVisibility(true);
		
		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("doc_srl");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("content");
		Paramname.add("permission");
		Paramname.add("privacy");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("1");
		Paramvalue.add(doc_srl);
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(comment_edittext.getText().toString());
		Paramvalue.add("3");
		Paramvalue.add("0");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/comment_app_write.php", mHandler, Paramname, Paramvalue,
				null, 4,0);
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
					//page_srl//user_srl//name//title//content//date//status//privacy//comments//recommend//negative
					String[] array = msg.obj.toString().split("/LINE/.");
					Global.dumpArray(array);
					String page_srl = array[0];
					 user_srl = array[1];
					String name = array[2];
					String title = array[3];
					String content = array[4];
					String date = array[5];
					String status = array[6];
					String privacy = array[7];
					String comments = array[8];
					String recommend = array[9];
					String negative = array[10];

					

					getSupportActionBar().setTitle(name);

					getMemberInfo(user_srl);
					
					
					//Profiile
					profile.setImageDrawable(Drawable.createFromPath(local_path + "thumbnail/"
							+ user_srl + ".jpg"));
					
					profile_title.setText(name);
					profile_des.setText(Global.formatTimeString(Long.parseLong(date)));
					
					doc_content.setText(content);
					
					
				} catch (Exception e) {
				
				}
			}
			
			if (msg.what == 2) {

				try {
				
					String[] array = msg.obj.toString().split("/LINE/.");
					Global.dumpArray(array);
					String profile_pic = array[0];
					String profile_update = array[1];
					
					String user_srl = String.valueOf(msg.arg1);
					Log.i("Gett", user_srl + "");

					if (profile_pic.matches("Y")) {
						//Global.SaveUserSetting(user_srl, profile_update);
						ProfileUserImageDownload(user_srl);
						// Log.i("test", "Let s profile image download");

					}
					if (profile_pic.matches("N")) {
						File file = new File(local_path + user_srl + ".jpg");
						file.delete();
						File file_thum = new File(local_path + "thumbnail/" + user_srl + ".jpg");
						file_thum.delete();
					}
				} catch (Exception e) {
				//	MemberInfoError();

				}
			}
			
			if (msg.what == 3) {
				// Save File cache
				try {
					Log.i("Save", msg.arg1 + "");
					Global.SaveBitmapToFileCache((Bitmap) msg.obj, local_path,
							msg.arg1 + ".jpg");
					Global.createThumbnail((Bitmap) msg.obj, local_path
							+ "thumbnail/", msg.arg1 + ".jpg");
					
					m_adapter.notifyDataSetChanged();

					// Set Profile
//					profile.setImageDrawable(Drawable.createFromPath(local_path
//							+ member_srl + ".jpg"));
				//	Refresh();
				} catch (Exception e) {
				}
			}
			
			if (msg.what == 4) {
				String result = msg.obj.toString();
				if(result.matches("comment_write_succeed")) {
				m_adapter.notifyDataSetChanged();
				comment_edittext.setText(null);
				}else{
					Global.ConnectionError(document_read.this);
				}
				Log.i("Result","로그 정상 작동");
				Log.i("Result", msg.obj.toString());
			
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
