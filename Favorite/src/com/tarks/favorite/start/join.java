package com.tarks.favorite.start;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;
import com.google.android.gcm.GCMRegistrar;
import com.tarks.favorite.CropManager;
import com.tarks.favorite.Global;
import com.tarks.favorite.MainActivity;
import com.tarks.favorite.R;
import com.tarks.favorite.tarks_account_login;
import com.tarks.favorite.R.string;
import com.tarks.favorite.connect.AsyncHttpTask;

public class join extends SherlockActivity implements OnCheckedChangeListener {
	// Imageview
	ImageView profile;
	// bitmap
	Bitmap profile_bitmap;
	// RadioGroup

	RadioGroup rg1;
	// name
	String first_name;
	String last_name;
	String name_1, name_2;
	// User Auth key
	String auth_key;
	int gender = 1; // Default gender is male
	boolean okbutton = true;
	// Profile pick
	int REQ_CODE_PICK_PICTURE = 0;
	int IMAGE_EDIT = 1;

	// Upload
	private FileInputStream mFileInputStream = null;
	private URL connectUrl = null;
	private EditText mEdityEntry;

	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";

	public class InfoDown extends AsyncTask<String, Void, Bitmap> {

		public Bitmap doInBackground(String... param) {
			// TODO Auto-generated method stub

			// Check This Tarks Account Already Have Buldang Account
			return downloadBitmap(myId);

		}

		@Override
		public void onPreExecute() {
			// Log.i("Async-Example", "onPreExecute Called");
		}

		public void onPostExecute(Bitmap result) {
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
				Global.dumpArray(array);
				String user_srl = array[0];
				auth_key = array[1];
				name_1 = array[2];
				name_2 = array[3];
				gender = Integer.parseInt(array[4]);
				// Download Profile image
				new ImageDownloader().execute(getString(R.string.server_path)
						+ "files/profile/" + auth_key + ".jpg");
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

		}

		public Bitmap downloadBitmap(String url) {
			try {

				// get ID
				// 설정 값 불러오기
				SharedPreferences prefs = getSharedPreferences("temp",
						MODE_PRIVATE);
				String id = prefs.getString("temp_id", "null");

				// --------------------------
				// URL 설정하고 접속하기
				// --------------------------
				URL url1 = new URL(getString(R.string.server_path)
						+ "member/tarks_get_member_info.php"); // URL
				// 설정
				HttpURLConnection http = (HttpURLConnection) url1
						.openConnection(); // 접속

				// --------------------------
				// 전송 모드 설정 - 기본적인 설정이다
				// --------------------------
				http.setDefaultUseCaches(false);
				http.setDoInput(true); // 서버에서 읽기 모드 지정
				http.setDoOutput(true); // 서버로 쓰기 모드 지정
				http.setRequestMethod("POST"); // 전송 방식은 POST

				// 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
				http.setRequestProperty("content-type",
						"application/x-www-form-urlencoded");
				// --------------------------
				// 서버로 값 전송
				// --------------------------
				StringBuffer buffer = new StringBuffer();
				buffer.append("authcode").append("=").append("642979")
						.append("&"); // php 변수에 값 대입
				buffer.append("tarks_account").append("=").append(id);

				OutputStreamWriter outStream = new OutputStreamWriter(
						http.getOutputStream(), "utf-8");
				PrintWriter writer = new PrintWriter(outStream);
				writer.write(buffer.toString());
				writer.flush();
				// --------------------------
				// 서버에서 전송받기
				// --------------------------
				InputStreamReader tmp = new InputStreamReader(
						http.getInputStream(), "utf-8");
				BufferedReader reader = new BufferedReader(tmp);
				StringBuilder builder = new StringBuilder();
				String str;

				while ((str = reader.readLine()) != null) { // 서버에서 라인단위로
															// 보내줄
					builder.append(str); // 것이므로 라인단위로 읽는다
					// builder.append(str + "\n"); // View에 표시하기 위해 라인 구분자
					// 추가
				}
				infoResult = builder.toString(); // 전송결과를 전역 변수에 저장

			} catch (MalformedURLException e) {
				//
			} catch (IOException e) {
				//
			}

			return null;
		}

	}

	private class Downloader extends AsyncTask<String, Void, Bitmap> {

		protected Bitmap doInBackground(String... param) {
			// TODO Auto-generated method stub
			// Error Login

			return downloadBitmap(myId);
		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			// set Progressbar
			setSupportProgressBarIndeterminateVisibility(true);

		}

		protected void onPostExecute(Bitmap result) {
			Log.i("Async-Example", "onPostExecute Called");
			// set Progressbar
			setSupportProgressBarIndeterminateVisibility(false);
			// import EditText
			EditText edit1 = (EditText) findViewById(R.id.editText1);
			String s1 = edit1.getText().toString();

			EditText edit2 = (EditText) findViewById(R.id.editText2);
			String s2 = edit2.getText().toString();

			// EditText edit3 = (EditText) findViewById(R.id.editText3);
			// String s3 = edit3.getText().toString();
			// Check Success

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
				Global.dumpArray(array);

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
			// Set ok button enable
		}

		private Bitmap downloadBitmap(String url) {
			try {

				// get ID

				SharedPreferences prefs = getSharedPreferences("temp",
						MODE_PRIVATE);
				String id = prefs.getString("temp_id", "null");

				// REG ID

				SharedPreferences prefs1 = getSharedPreferences("setting",
						MODE_PRIVATE);

				// import EditText
				EditText edit1 = (EditText) findViewById(R.id.editText1);
				String s1 = edit1.getText().toString();

				EditText edit2 = (EditText) findViewById(R.id.editText2);
				String s2 = edit2.getText().toString();

				// EditText edit3 = (EditText) findViewById(R.id.editText3);
				// String s3 = edit3.getText().toString();

				// Make name
				String[] name = Global.NameBuilder(s1, s2);

				first_name = name[0];
				last_name = name[1];

				// Reg id null
				if (reg_id.matches("")) {
					reg_id = "null";
				}

				// --------------------------
				// URL 설정하고 접속하기
				// --------------------------
				URL url1 = new URL(getString(R.string.server_path)
						+ "member/join.php"); // URL
				// 설정
				HttpURLConnection http = (HttpURLConnection) url1
						.openConnection(); // 접속

				// --------------------------
				// 전송 모드 설정 - 기본적인 설정이다
				// --------------------------
				http.setDefaultUseCaches(false);
				http.setDoInput(true); // 서버에서 읽기 모드 지정
				http.setDoOutput(true); // 서버로 쓰기 모드 지정
				http.setRequestMethod("POST"); // 전송 방식은 POST

				// 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
				http.setRequestProperty("content-type",
						"application/x-www-form-urlencoded");
				// --------------------------
				// 서버로 값 전송
				// --------------------------
				StringBuffer buffer = new StringBuffer();
				buffer.append("authcode").append("=").append("642979")
						.append("&"); // php 변수에 값 대입
				buffer.append("tarks_account").append("=").append(id_auth)
						.append("&");
				buffer.append("name_1").append("=").append(first_name)
						.append("&");
				buffer.append("name_2").append("=").append(last_name)
						.append("&");
				buffer.append("gender").append("=").append(gender).append("&");
				buffer.append("reg_id").append("=").append(reg_id);

				OutputStreamWriter outStream = new OutputStreamWriter(
						http.getOutputStream(), "utf-8");
				PrintWriter writer = new PrintWriter(outStream);
				writer.write(buffer.toString());
				writer.flush();
				// --------------------------
				// 서버에서 전송받기
				// --------------------------
				InputStreamReader tmp = new InputStreamReader(
						http.getInputStream(), "EUC-KR");
				BufferedReader reader = new BufferedReader(tmp);
				StringBuilder builder = new StringBuilder();
				String str;

				while ((str = reader.readLine()) != null) { // 서버에서 라인단위로 보내줄
															// 것이므로 라인단위로 읽는다
					builder.append(str); // View에 표시하기 위해 라인 구분자 추가
				}
				myResult = builder.toString(); // 전송결과를 전역 변수에 저장

			} catch (MalformedURLException e) {
				//
			} catch (IOException e) {
				//
			}

			return null;
		}

	}

	private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

		protected Bitmap doInBackground(String... param) {
			// TODO Auto-generated method stub
			return downloadBitmap(param[0]);
		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");

		}

		protected void onPostExecute(Bitmap result) {
			Log.i("Async-Example", "onPostExecute Called");
			profile.setImageBitmap(result);
			// simpleWaitDialog.dismiss();

		}

		private Bitmap downloadBitmap(String url) {
			// initilize the default HTTP client object
			final DefaultHttpClient client = new DefaultHttpClient();

			// forming a HttoGet request
			final HttpGet getRequest = new HttpGet(url);
			try {

				HttpResponse response = client.execute(getRequest);

				// check 200 OK for success
				final int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {
					Log.w("ImageDownloader", "Error " + statusCode
							+ " while retrieving bitmap from " + url);
					return null;

				}

				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					try {
						// getting contents from the stream
						inputStream = entity.getContent();

						// decoding stream data back into image Bitmap that
						// android understands
						final Bitmap bitmap = BitmapFactory
								.decodeStream(inputStream);

						return bitmap;
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (Exception e) {
				// You Could provide a more explicit error message for
				// IOException
				getRequest.abort();
				Log.e("ImageDownloader", "Something went wrong while"
						+ " retrieving bitmap from " + url + e.toString());
			}

			return null;
		}

	}

	String user_srl, name, number, phone_number;
	String regId;
	String id, id_auth;
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

		// get ID

		// HttpResponse result = null;

		// Intent intent = getIntent();// 인텐트 받아오고

		SharedPreferences prefs = getSharedPreferences("temp", MODE_PRIVATE);
		id = prefs.getString("temp_id", "");
		id_auth = prefs.getString("temp_id_auth", "null");

		// RadioButton
		rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
		rg1.setOnCheckedChangeListener(this);

		// Define profile imageview
		profile = (ImageView) findViewById(R.id.profile_image);

		profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK);
				i.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
				i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // images
																							// on
																							// the
																							// SD
																							// card.

				// 결과를 리턴하는 Activity 호출
				startActivityForResult(i, REQ_CODE_PICK_PICTURE);
			}
		});

		// set id Text
		TextView ids = (TextView) findViewById(R.id.textView2);
		ids.setText(id);

		if (!id.matches("")) {
			// Connection Start
			try {
				new InfoDown().execute();
			} catch (Exception e) {
				// Not Connected To Internet
				AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
				builder.setMessage(getString(R.string.networkerrord))
						.setPositiveButton(getString(R.string.yes), null)
						.setTitle(getString(R.string.networkerror));
				builder.show();
			}
		}

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
				byte[] b = Global.image;
				profile_bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
				// Log.i("datasetdata", data.getData().toString() + "ssdsd");
				profile.setImageBitmap(profile_bitmap); // 사진 선택한 사진URI로 연결하기
				//Set global image null
				Global.image = null;
			}
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
		// Setting Editor DeleteTemp
		SharedPreferences edit = getSharedPreferences("temp", MODE_PRIVATE);
		SharedPreferences.Editor editor = edit.edit();
		editor.remove("temp_id");
		editor.remove("temp_id_auth");
		editor.commit();
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

	public boolean ButtonEnable(final int s) {
		new Thread(new Runnable() {
			public void run() {
				int i = 0;
				while (true) {
					if (i > s) {
						okbutton = true;
						break;
					} else {
						try {
							Thread.sleep(1000);
							i += 1;
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
					}

				}
			}
		}).start();
		return okbutton;
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
			Global.dumpArray(array);

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
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == 1) {
				myResult = msg.obj.toString();
				// Stop progress bar
				setSupportProgressBarIndeterminateVisibility(false);
				joinAct();

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
			if (okbutton == true) {
				// Set ok button disable
				okbutton = false;
				ButtonEnable(1);

			
				// import EditText
				EditText edit1 = (EditText) findViewById(R.id.editText1);
				String s1 = edit1.getText().toString();
				EditText edit2 = (EditText) findViewById(R.id.editText2);
				String s2 = edit2.getText().toString();

				// no value on name
				if (s1.matches("") || s2.matches("")) {
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
						Global.SaveBitmapToFileCache(profile_bitmap,
								"sdcard/favorite/temp/", "profile.jpg");
						// Global.DoFileUpload("sdcard/favorite/temp/profile.jpg");

						// get ID

						SharedPreferences prefs = getSharedPreferences("temp",
								MODE_PRIVATE);
						String id = prefs.getString("temp_id", "null");

						// REG ID

						SharedPreferences prefs1 = getSharedPreferences(
								"setting", MODE_PRIVATE);

						// EditText edit3 = (EditText)
						// findViewById(R.id.editText3);
						// String s3 = edit3.getText().toString();

						// Make name
						String[] name = Global.NameBuilder(s1, s2);

						first_name = name[0];
						last_name = name[1];

				//		Log.i("Name", last_name + first_name);

						// Reg id null
						if (reg_id.matches(""))
							reg_id = "null";

						ArrayList<String> Paramname = new ArrayList<String>();
						Paramname.add("authcode");
						Paramname.add("tarks_account");
						Paramname.add("name_1");
						Paramname.add("name_2");
						Paramname.add("gender");
						Paramname.add("reg_id");

						ArrayList<String> Paramvalue = new ArrayList<String>();
						Paramvalue.add("642979");
						Paramvalue.add(id_auth);
						Paramvalue.add(first_name);
						Paramvalue.add(last_name);
						Paramvalue.add(String.valueOf(gender));
						Paramvalue.add(reg_id);

						ArrayList<String> files = new ArrayList<String>();
						files.add("sdcard/favorite/temp/profile.jpg");

						new AsyncHttpTask(this, getString(R.string.server_path)
								+ "member/join.php", mHandler, Paramname,
								Paramvalue, files, 1);

					} catch (Exception e) {
						// Show network error
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
