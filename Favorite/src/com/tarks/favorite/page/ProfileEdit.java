package com.tarks.favorite.page;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;
import com.tarks.favorite.CropManager;
import com.tarks.favorite.R;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.connect.ImageDownloader;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.global.Globalvariable;
import com.tarks.favorite.page.ProfileInfo.List;
import com.tarks.favorite.start.join;

public class ProfileEdit extends SherlockActivity {
	
	ImageView profile;
	
	String local_path;
	String member_srl = "0";
	String name_1;
	String name_2;
	String lang;
	
	//Name Edittext
	EditText edittext_name_1;
	EditText edittext_name_2;
	
	// ListView
	ListView listView;
// List
	ArrayList<List> m_orders = new ArrayList<List>();
	// Define ListAdapter
	ListAdapter m_adapter;
	
	//Array member info
	String[] array;

	
	// bitmap
	Bitmap profile_bitmap;
	// IMG
	Uri mImageUri;
	// RadioGroup
	
	
	// Profile pick
		int REQ_CODE_PICK_PICTURE = 0;
		int IMAGE_EDIT = 1;
		int CAMERA_PIC_REQUEST = 2;

		// Camera
		static final String[] IMAGE_PROJECTION = {
				MediaStore.Images.ImageColumns.DATA,
				MediaStore.Images.Thumbnails.DATA };

		final Uri uriImages = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		final Uri uriImagesthum = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;

		// Profile picture changed
		boolean profile_changed = false;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	// Can use progress
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
        setContentView(R.layout.listview);
      //액션바백버튼가져오기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
        
    	local_path = getCacheDir().toString() + "/member/";
		// Get Intent
		Intent intent = getIntent();// 인텐트 받아오고
		member_srl = intent.getStringExtra("member_srl");
		
		
		setListAdapter();
		getUseInfo();
    }
    
    public void getUseInfo(){
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
    			Paramvalue.add(member_srl);
    			Paramvalue
    					.add("tarks_account//admin//name_1//name_2//gender//birthday//country_code//phone_number//join_day//profile_pic//profile_update//lang//country//like_me//favorite");

    			new AsyncHttpTask(this, getString(R.string.server_path)
    					+ "member/profile_info.php", mHandler, Paramname, Paramvalue,
    					null, 1,0);
    }
    
public void setList(){
		

		String tarks_account = array[0];
		String admin = array[1];
		String name_1 = array[2];
		String name_2 = array[3];
		String gender = array[4];
		String birthday = array[5];
		String country_code = array[6];
		String phone_number = array[7];
		String join_day = array[8];
		String profile_pic = array[9];
		String profile_update = array[10];
		String lang = array[11];
		String country = array[12];
		String like_me = array[13];
		String favorite = array[14];

		
		if(tarks_account.matches("null")) tarks_account = getString(R.string.add);
		if(birthday.matches("0")) birthday = getString(R.string.add);
		if(join_day.matches("null")) join_day = getString(R.string.add);
		if(phone_number.matches("null")) phone_number = getString(R.string.add);
		
		//Null : 0 Normal Text : -1 Tarks Account : 1 Gender : 2 birthday: 3 Phone Number : 4
		
		AddList(getString(R.string.tarks_account) , tarks_account, 1);
		 AddList(getString(R.string.gender) , gender.matches("1") ? getString(R.string.male) : getString(R.string.female), 2);
		 AddList(getString(R.string.birthday) , birthday, 3);
		AddList(getString(R.string.join) , Global.getDate(join_day), 0);
		AddList(getString(R.string.phone_number) , "+" + country_code + phone_number,4);
		
		

		
	//	if(!tarks_account.matches("null")) AddList(getString(R.string.tarks_account) , tarks_account);
	//	if(!tarks_account.matches("null")) AddList(getString(R.string.tarks_account) , tarks_account);
		

		listView.setAdapter(m_adapter);
		
	}

public void AddList(String title, String description, int Tag){
	List p1 = new List(title, description, Tag);
	m_orders.add(p1);
}
    
    public void setListAdapter() {
		listView = (ListView) findViewById(R.id.listView1);
	       View header = getLayoutInflater().inflate(R.layout.profile_edit_header, null, false);
	       edittext_name_1 = (EditText) header.findViewById(R.id.editText1);
	       edittext_name_2 = (EditText) header.findViewById(R.id.editText2);
	        profile = (ImageView) header.findViewById(R.id.profile_image);
	        profile.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					v.showContextMenu();
				}
			});
	         
	        registerForContextMenu(profile);
//	       profile_edit = (ImageButton) header.findViewById(R.id.edit);
//	       profile_title = (TextView) header.findViewById(R.id.title);
//	       profile_des = (TextView) header.findViewById(R.id.description);
	      profile.setImageDrawable(Drawable.createFromPath(local_path + "thumbnail/"
					+ member_srl + ".jpg"));
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

//						List ls = (List) ca.getItem(arg2 - 1);
//
//						Intent intent = new Intent(ProfileActivity.this,
//								document_read.class);
//						intent.putExtra("doc_srl",
//								String.valueOf(ls.getDocSrl()));
//						startActivity(intent);
						
						
						

					}
				}

			}
		});
		 listView.addHeaderView(header);
		// listView.setOnScrollListener(this);
		m_adapter = new ListAdapter(this, R.layout.list, m_orders);

		listView.setAdapter(m_adapter);

		
	}
    public void NameUpdate(){
    	
		// Make name
		String[] name = Global.NameBuilder(edittext_name_1.getText().toString(), edittext_name_2.getText().toString());

		String last_name = name[0];
		String first_name = name[1];
    	
 	   ArrayList<String> Paramname = new ArrayList<String>();
 		Paramname.add("authcode");
 		Paramname.add("kind");
 		Paramname.add("user_srl");
 		Paramname.add("user_srl_auth");
 		Paramname.add("profile_user_srl");
 		Paramname.add("name_1");
 		Paramname.add("name_2");
 		Paramname.add("lang");

 		ArrayList<String> Paramvalue = new ArrayList<String>();
 		Paramvalue.add("642979");
 		Paramvalue.add("2");
 		Paramvalue.add(Global.getSetting("user_srl",
 				Global.getSetting("user_srl", "0")));
 		Paramvalue.add(Global.getSetting("user_srl_auth",
 				Global.getSetting("user_srl_auth", "null")));
 		Paramvalue.add(member_srl);
 		Paramvalue.add(last_name);
 		Paramvalue.add(first_name);
 		Paramvalue.add(getString(R.string.lang));

 		// Files null if no profile changed
 		ArrayList<String> files = null;
 		if (profile_changed == true && profile_bitmap != null) {
 			Global.SaveBitmapToFileCache(profile_bitmap, getCacheDir().toString(), "/profile.jpg");
 			files = new ArrayList<String>();
 			files.add(getCacheDir().toString() + "/profile.jpg");
 		}

 		new AsyncHttpTask(this, getString(R.string.server_path)
 				+ "member/profile_update_app.php", mHandler, Paramname,
 				Paramvalue, files, 0,0);

    }
    
   public void ProfileUpdate(){
	   ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("profile_user_srl");
		Paramname.add("profile_pic");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("1");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(member_srl);
		Paramvalue.add(profile_changed ? "Y" : "N");

		// Files null if no profile changed
		ArrayList<String> files = null;
		if (profile_changed == true && profile_bitmap != null) {
			Global.SaveBitmapToFileCache(profile_bitmap, getCacheDir().toString(), "/profile.jpg");
			files = new ArrayList<String>();
			files.add(getCacheDir().toString() + "/profile.jpg");
		}

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "member/profile_update_app.php", mHandler, Paramname,
				Paramvalue, files, 0,0);

   }
    
    public void ProfileImageDownload() {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);
		new ImageDownloader(this, getString(R.string.server_path)
				+ "files/profile/" + member_srl + ".jpg", mHandler, 2, 0);
	}
    
	public void MemberInfoError() {
		Global.Infoalert(this, getString(R.string.error),
				getString(R.string.member_info_error_des),
				getString(R.string.yes));
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		Log.i("ContextMenu", "Contextmenu");
		if (v.getId() == R.id.profile_image) {

			menu.setHeaderIcon(android.R.drawable.btn_star);
			// menu.setHeaderTitle("공지사항");
			menu.add(Menu.NONE, 1, Menu.NONE,
					getString(R.string.choose_picture));
			menu.add(Menu.NONE, 2, Menu.NONE, getString(R.string.camera));
			menu.add(Menu.NONE, 3, Menu.NONE, getString(R.string.delete));

		}

		super.onCreateContextMenu(menu, v, menuInfo);

	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			Intent i = new Intent(Intent.ACTION_PICK);
			i.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
			i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // images
			// 결과를 리턴하는 Activity 호출
			startActivityForResult(i, REQ_CODE_PICK_PICTURE);
			break;

		case 2:
			int w,
			h;
			// Intent cameraIntent = new
			// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// startActivityForResult(cameraIntent , CAMERA_PIC_REQUEST);

			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			File photo;
			try {
				// place where to store camera taken picture
				photo = Global.createTemporaryFile("picture", ".jpg");
				photo.delete();
			} catch (Exception e) {
				Global.toast(getString(R.string.no_storage_error));
				return false;
			}
			mImageUri = Uri.fromFile(photo);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
			// start camera intent
			ProfileEdit.this.startActivityForResult(intent, CAMERA_PIC_REQUEST);
			break;

		case 3:
			profile.setImageResource(R.drawable.black_button);
			profile_changed = false;
			ProfileUpdate();
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_CODE_PICK_PICTURE) {
			if (resultCode == Activity.RESULT_OK) {
				// Log.i("datasetdata", data.getData().toString() + "ssdsd");
				Intent intent = new Intent(ProfileEdit.this, CropManager.class);
				intent.putExtra("uri", data.getData());
				startActivityForResult(intent, IMAGE_EDIT);

			}
		}

		if (requestCode == IMAGE_EDIT) {
			// Log.i("Imageresult", "itsok");
			if (resultCode == Activity.RESULT_OK) {
				byte[] b = Globalvariable.image;
				profile_bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
				// Log.i("datasetdata", data.getData().toString() + "ssdsd");
				profile.setImageBitmap(profile_bitmap); // 사진 선택한 사진URI로 연결하기
				// Profile changed
				profile_changed = true;
				// Set global image null
				Globalvariable.image = null;
				//Start update
				ProfileUpdate();
			}
		}

		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
			// ImageView imageView;
			// ... some code to inflate/create/find appropriate ImageView to
			// place grabbed image
			// profile_bitmap = Global.grabImage(mImageUri);
			Intent intent = new Intent(ProfileEdit.this, CropManager.class);
			intent.putExtra("uri", mImageUri);
			startActivityForResult(intent, IMAGE_EDIT);
		}

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
		private int Tag;

		public List( String _Title, String _Description, int _Tag) {
			this.Title = _Title;
			this.Description = _Description;
			this.Tag = _Tag;
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



	}
	
	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			setSupportProgressBarIndeterminateVisibility(false);
			// IF Sucessfull no timeout

			if (msg.what == -1) {
				Global.ConnectionError(ProfileEdit.this);
			}

			if (msg.what == 1) {

				try {
				  array = msg.obj.toString().split("/LINE/.");
				//	Global.dumpArray(array);
					 
						String tarks_account = array[0];
						String admin = array[1];
						 name_1 = array[2];
						 name_2 = array[3];
						String gender = array[4];
						String birthday = array[5];
						String country_code = array[6];
						String phone_number = array[7];
						String join_day = array[8];
						String profile_pic = array[9];
						String profile_update = array[10];
						 lang = array[11];
						String country = array[12];
						String like_me = array[13];
						String favorite = array[14];
					
						String[] name = Global.NameBuilder(name_1, name_2);
						edittext_name_1.setText(name[0]);
						edittext_name_2.setText(name[1]);

					
					
					
					if (Global.UpdateFileCache(profile_update,
							Global.getUser(member_srl, "profile_update"),
							getString(R.string.server_path) + "files/profile/thumbnail/"
									+ member_srl + ".jpg", local_path,
							member_srl + ".jpg")
							&& profile_pic.matches("Y")) {
						Global.SaveUserSetting(member_srl,null, profile_update, profile_pic);
						ProfileImageDownload();
						// Log.i("test", "Let s profile image download");

					}
					
					if (profile_pic.matches("N")) {
					
						File file = new File(local_path + member_srl + ".jpg");
						file.delete();
						File file_thum = new File(local_path + "thumbnail/" + member_srl + ".jpg");
						file_thum.delete();
				//	profile.setImageDrawable(R.drawable.people);
						profile.setImageResource(R.drawable.person);
					//	profile.setId(null);
					//	profile.setImageDrawable(res.getDrawable(R.drawable.person));
					}
					
				 	setList();
				
				} catch (Exception e) {
					MemberInfoError();
					e.printStackTrace();
				}
			}

	

		}
	};
	
	@Override
	public void onBackPressed() {
		try{
	if(!edittext_name_1.getText().toString().matches(name_1) || !edittext_name_2.getText().toString().matches(name_2)){
		NameUpdate();
	}
		} catch (Exception e){
			
		}
	  // Intent 생성
	   Intent intent = new Intent();
	   // 생성한 Intent에 데이터 입력
//	   intent.putExtra("image", b);
	   // 결과값 설정(결과 코드, 인텐트)
	   this.setResult(RESULT_OK,intent);
	finish();
	}
	
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
