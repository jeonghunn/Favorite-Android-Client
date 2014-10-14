//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.page;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;
import com.tarks.favorite.CropManager;
import com.tarks.favorite.R;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.global.Globalvariable;

public class page_create extends SherlockActivity {
	// Imageview
	ImageView profile;
	// bitmap
	Bitmap profile_bitmap;
	// IMG
	Uri mImageUri;
	// name
	String first_name;
	String last_name;

	// Profile
	String profile_pic;
	// Country code and Phone number
	// User Auth key
	String auth_key;
	int gender = 1; // Default gender is male
	// boolean okbutton = true;
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

	String user_srl, name, number, phone_number;
	String regId;
	String id;
	String id_auth;
	String reg_id;
	String myId, myPWord, myTitle, mySubject, myResult;
	String infoResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Can use progress
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.create_page);
		// no show progress now
		setSupportProgressBarIndeterminateVisibility(false);

		// 액션바백버튼가져오기
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 getSupportActionBar().setDisplayShowHomeEnabled(false);


		// Define profile imageview
		profile = (ImageView) findViewById(R.id.profile_image);

		profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				v.showContextMenu();
			}
		});

		registerForContextMenu(profile);
	

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
			page_create.this.startActivityForResult(intent, CAMERA_PIC_REQUEST);
			break;

		case 3:
			profile.setImageResource(R.drawable.black_button);
			profile_changed = false;
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
				Intent intent = new Intent(page_create.this, CropManager.class);
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
			}
		}

		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
			// ImageView imageView;
			// ... some code to inflate/create/find appropriate ImageView to
			// place grabbed image
			// profile_bitmap = Global.grabImage(mImageUri);
			Intent intent = new Intent(page_create.this, CropManager.class);
			intent.putExtra("uri", mImageUri);
			startActivityForResult(intent, IMAGE_EDIT);
		}

	}

	
	public void deletetemp() {
		Globalvariable.temp_id = null;
		Globalvariable.temp_id_auth = null;

	}

	// 백키를 눌렀을때의 반응.

	
	// Call connection Error
	public void ConnectionError() {
		Global.ConnectionError(this);
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			setSupportProgressBarIndeterminateVisibility(false);

			if (msg.what == -1) {
				ConnectionError();
			}

			// Join Activity
			if (msg.what == 1) {
				finish();
				Intent intent = new Intent(page_create.this, ProfileActivity.class);
				  intent.putExtra("member_srl", msg.obj.toString());
				startActivity(intent);	

			}

			
		}
	};
	
	public void createAct() {
		

		EditText edit2 = (EditText) findViewById(R.id.editText1);
		String s2 = edit2.getText().toString();

		// no value on name
		if (s2.matches("")) {
			// No Value
			Global.Infoalert(this, getString(R.string.error),
					getString(R.string.noname), getString(R.string.yes));
		} else {
			// dont make error

			try {

			
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("name");
		Paramname.add("lang");
		Paramname.add("country");
		Paramname.add("profile_pic");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(s2);
		Paramvalue.add(getString(R.string.lang));
		Paramvalue.add(Global.getCountryValue());
		Paramvalue.add(profile_changed ? "Y" : "N");
		
		// Files null if no profile changed
		ArrayList<String> files = null;
		if (profile_changed == true && profile_bitmap != null) {
			Global.SaveBitmapToFileCache(profile_bitmap, getCacheDir().toString(), "/profile.jpg");
			files = new ArrayList<String>();
			files.add(getCacheDir().toString() + "/profile.jpg");
		}
	
		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "member/page_create_app.php", mHandler, Paramname,
				Paramvalue, files, 1, 0);
		
			} catch (Exception e) {
				// Show network error
				e.printStackTrace();
				Global.Infoalert(this,
						getString(R.string.networkerror),
						getString(R.string.networkerrord),
						getString(R.string.yes));

			}
	}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// 메뉴 버튼 구현부분
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.accept, menu);
		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.yes:
		
			if (Globalvariable.okbutton == true) {

			createAct();
				
			}

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
