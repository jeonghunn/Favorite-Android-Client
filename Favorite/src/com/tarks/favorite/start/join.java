//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.start;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;
import com.tarks.favorite.CropManager;
import com.tarks.favorite.MainActivity;
import com.tarks.favorite.R;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.connect.ImageDownloader;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.global.Globalvariable;

public class join extends SherlockActivity implements OnCheckedChangeListener {
	// Imageview
	ImageView profile;
	// bitmap
	Bitmap profile_bitmap;
	// IMG
	Uri mImageUri;
	// RadioGroup

	RadioGroup rg1;
	// name
	String first_name;
	String last_name;
	String name_1, name_2;
	// Profile
	String profile_pic;
	// Country code and Phone number
	String PhoneNumber;
	String CountryCode;
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

	public void InfoDownAct() {

		try {
			// Log.i("Async-Example", "onPostExecute Called");
			// import EditText
			EditText edit1 = (EditText) findViewById(R.id.editText1);
			String s1 = edit1.getText().toString();

			EditText edit2 = (EditText) findViewById(R.id.editText2);
			String s2 = edit2.getText().toString();
			//
			// EditText edit3 = (EditText) findViewById(R.id.editText3);
			// String s3 = edit3.getText().toString();

			// Check Tarks Account Exist
			if (!infoResult.matches("null")) {
				// Cut Result Value
				String[] array = infoResult.split("/LINE/.");
			//	Global.dumpArray(array);
				String user_srl = array[0];
				auth_key = array[1];
				name_1 = array[2];
				name_2 = array[3];
				gender = Integer.parseInt(array[4]);
				profile_pic = array[5];
				// Download Profile image
				if (profile_pic.matches("Y")) {
					profile_changed = true;
					new ImageDownloader(this, getString(R.string.server_path)
							+ "files/profile/" + user_srl  + ".jpg", mHandler, 3,0);
				}
				// Set EditText
				// Country

				String[] name = Global.NameBuilder(name_1, name_2);
				edit1.setText(name[0]);
				edit2.setText(name[1]);

				// If female check second
				if (gender == 2) {
					rg1.check(R.id.radio1);
				}
				// edit3.setText(phone_number);

			} else {
				// if null
			}

		} catch (Exception e) {
			Global.Infoalert(join.this, getString(R.string.error),
					getString(R.string.error_des), getString(R.string.yes));
		}
	}

	public void InfoDown() {
		String id = Globalvariable.temp_id;

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("tarks_account");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add(id);

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "member/tarks_get_member_info.php", mHandler, Paramname,
				Paramvalue, null, 2,0);
	}

	String user_srl, name, number, phone_number;
	String regId;
	String id;
	String id_auth;
	String reg_id;
	String myId, myPWord, myTitle, mySubject, myResult;
	String infoResult;
	// press back key
	private boolean mIsBackKeyPressed = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Can use progress
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.join);
		// no show progress now
		setSupportProgressBarIndeterminateVisibility(false);

		// 액션바백버튼가져오기
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 getSupportActionBar().setDisplayShowHomeEnabled(false);

		// get ID

		// HttpResponse result = null;

		// Intent intent = getIntent();// 인텐트 받아오고

		id = Globalvariable.temp_id;
		id_auth = Globalvariable.temp_id_auth;
		// RadioButton
		rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
		rg1.setOnCheckedChangeListener(this);

		// Define profile imageview
		profile = (ImageView) findViewById(R.id.profile_image);

		profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				v.showContextMenu();
			}
		});

		registerForContextMenu(profile);
		// LongTimeclicklistener
		// profile.setOnLongClickListener(new OnLongClickListener(){
		//
		// @Override
		// public boolean onLongClick(View v) {
		// // TODO Auto-generated method stub
		// AlertDialog.Builder alert = new AlertDialog.Builder(
		// join.this);
		// alert.setTitle(getString(R.string.delete));
		// alert.setMessage(getString(R.string.delete_profile_photo));
		// alert.setPositiveButton(getString(R.string.yes),
		// new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // Clear Old Settings
		// profile.setImageResource(R.drawable.black_button);
		// // profile.setBackgroundResource(R.drawable.black_button);
		// profile_changed = true;
		// }
		//
		//
		// });
		// alert.setNegativeButton(getString(R.string.no),
		// new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog,
		// int which) {
		//
		//
		// }
		// });
		//
		// alert.show();
		// return false;
		// }
		// });

		// set id Text
		TextView ids = (TextView) findViewById(R.id.textView2);
		ids.setText(id);

		if (id != null) {
			// Connection Start
			InfoDown();
		}

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
			join.this.startActivityForResult(intent, CAMERA_PIC_REQUEST);
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
				Intent intent = new Intent(join.this, CropManager.class);
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
			Intent intent = new Intent(join.this, CropManager.class);
			intent.putExtra("uri", mImageUri);
			startActivityForResult(intent, IMAGE_EDIT);
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub

		switch (arg1) {

		case R.id.radio0:
			gender = 1;

			break;

		case R.id.radio1:
			gender = 2;
			break;

		}
	}

	public void deletetemp() {
		Globalvariable.temp_id = null;
		Globalvariable.temp_id_auth = null;

	}

	// 백키를 눌렀을때의 반응.
	@Override
	public void onBackPressed() {
		if (mIsBackKeyPressed == false) {
			mIsBackKeyPressed = true;

			// Delete Temp ID
			// Setting Editor
			deletetemp();
			// Go Back
			Intent intent = new Intent(join.this, welcome.class);
			startActivity(intent);
			finish();
		}
	}

	public void joinAct() {
		// set Progressbar

		// import EditText
		EditText edit1 = (EditText) findViewById(R.id.editText1);
		String s1 = edit1.getText().toString();

		EditText edit2 = (EditText) findViewById(R.id.editText2);
		String s2 = edit2.getText().toString();

		// EditText edit3 = (EditText) findViewById(R.id.editText3);
		// String s3 = edit3.getText().toString();
		// Check Success
		try {
			if (myResult.matches("")) {
				// IF Fail
				// AlertDialog.Builder builder = new
				// AlertDialog.Builder(join.this);
				// builder.setMessage(getString(R.string.error_des))
				// .setPositiveButton(getString(R.string.yes), null)
				// .setTitle(getString(R.string.error));
				// builder.show();
				//
				Global.Infoalert(join.this, getString(R.string.error),
						getString(R.string.error_des), getString(R.string.yes));
			} else {
				// Go to Next Step

				String[] array = myResult.split("//");
			//이미 	Global.dumpArray(array);

				// Setting Editor
				SharedPreferences edit = getSharedPreferences("setting",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = edit.edit();
				editor.putString("frist_use_app", "false"); // Ű��,
				editor.putString("user_srl", array[0]);
				editor.putString("user_srl_auth", array[1]);
				editor.putString("name_1", s1);
				editor.putString("name_2", s2);
				editor.commit();

				deletetemp();
				Intent intent = new Intent(join.this, MainActivity.class);
				startActivity(intent);
				finish();
			}

		} catch (Exception e) {
			Global.Infoalert(join.this, getString(R.string.error),
					getString(R.string.error_des), getString(R.string.yes));
		}
	}

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
				myResult = msg.obj.toString();
				// Stop progress bar
				joinAct();

			}

			// Get Member Information
			if (msg.what == 2) {
				infoResult = msg.obj.toString();
				InfoDownAct();
			}

			// Get Profile Image
			if (msg.what == 3) {
				if ((Bitmap) msg.obj != null)
					profile.setImageBitmap((Bitmap) msg.obj);
			}

		}
	};

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

				// import EditText
				EditText edit1 = (EditText) findViewById(R.id.editText1);
				String s1 = edit1.getText().toString();
				EditText edit2 = (EditText) findViewById(R.id.editText2);
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

						// Show Registering toast
						Global.toast(getString(R.string.registering));

						// Register GCM
						reg_id = Global.GCMReg();

						// Make name
						String[] name = Global.NameBuilder(s1, s2);

						first_name = name[0];
						last_name = name[1];

						// Log.i("Name", last_name + first_name);

						// Reg id null
						if (reg_id.matches(""))
							reg_id = "null";


						ArrayList<String> Paramname = new ArrayList<String>();
						Paramname.add("authcode");
					//	Paramname.add("admin");
						Paramname.add("tarks_account");
						Paramname.add("name_1");
						Paramname.add("name_2");
						Paramname.add("gender");
						Paramname.add("country_code");
						Paramname.add("phone_number");
						Paramname.add("profile_pic");
						Paramname.add("reg_id");
						Paramname.add("lang");
						Paramname.add("country");

						ArrayList<String> Paramvalue = new ArrayList<String>();
						Paramvalue.add("642979");
					//	Paramname.add("1");
						Paramvalue.add(id_auth.matches("") || id_auth == null ? "null" : id_auth );
						Paramvalue.add(first_name);
						Paramvalue.add(last_name);
						Paramvalue.add(String.valueOf(gender));
						Paramvalue.add(Global.getPhoneNumber(false));
						Paramvalue.add(Global.getPhoneNumber(true));
						Paramvalue.add(profile_changed ? "Y" : "N");
						Paramvalue.add(reg_id);
						Paramvalue.add(getString(R.string.lang));
						Paramvalue.add(Global.getCountryValue());

						// Files null if no profile changed
						ArrayList<String> files = null;
						if (profile_changed == true && profile_bitmap != null) {
							Global.SaveBitmapToFileCache(profile_bitmap, getCacheDir().toString(), "/profile.jpg");
							files = new ArrayList<String>();
							files.add(getCacheDir().toString() + "/profile.jpg");
						}

						new AsyncHttpTask(this, getString(R.string.server_path)
								+ "member/join_app.php", mHandler, Paramname,
								Paramvalue, files, 1,0);

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

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
