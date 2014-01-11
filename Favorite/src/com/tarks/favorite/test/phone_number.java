package com.tarks.favorite.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;
import com.tarks.favorite.R;
import com.tarks.favorite.result;
import com.tarks.favorite.tarks_account_login;
import com.tarks.favorite.register.phone_number_reg;
import com.tarks.favorite.start.join;
import com.tarks.favorite.start.welcome;

public class phone_number extends SherlockActivity {

	EditText tv;
	TextView etv;
	String myId, myResult;
	Message msg;

	private class Downloader extends AsyncTask<String, Void, Bitmap> {

		protected Bitmap doInBackground(String... param) {
			// TODO Auto-generated method stub
			// Error Login

			return downloadBitmap(myId);
		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			 setSupportProgressBarIndeterminateVisibility(true);

		}

		protected void onPostExecute(Bitmap result) {
			Log.i("Async-Example", "onPostExecute Called");
			 setSupportProgressBarIndeterminateVisibility(false);

			msg = Message.obtain();
			msg.what = 0;
			mHandler.sendMessage(msg);

		}

		private Bitmap downloadBitmap(String url) {
			try {
				
				// 설정 값 불러오기
				SharedPreferences prefs = getSharedPreferences("setting",
						MODE_PRIVATE);
				String user_srl = prefs.getString("user_srl", "");
				
				String s1 = tv.getText().toString();
				// --------------------------
				// URL 설정하고 접속하기
				// --------------------------
				URL url1 = new URL(
						getString(R.string.server_path) + "phone_number.php"); // URL
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
				buffer.append("user_srl").append("=").append(user_srl)
				.append("&"); // php 변수에 값 대입
				buffer.append("phone_number").append("=").append(s1);
				OutputStreamWriter outStream = new OutputStreamWriter(
						http.getOutputStream(), "EUC-KR");
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
					builder.append(str); // 구분자 추가 안함
					Log.i("str", builder.toString());
					// builder.append(str + "\n");
					// View에 표시하기 위해 라인 구분자 추가
				}
				myResult = builder.toString(); // 전송결과를 전역 변수에 저장
				// Log.i("dfd", myResult);
			} catch (MalformedURLException e) {
				//
			} catch (IOException e) {
				//
			}

			return null;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Can use progress
		 requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.form);
		//no show progress now
		 setSupportProgressBarIndeterminateVisibility(false);
		// 액션바백버튼가져오기
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Define Edittext, Textview
		tv = (EditText) findViewById(R.id.form_edit);
		etv = (TextView) findViewById(R.id.textView1);
		
		//import button
		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(phone_number.this, phone_number_reg.class); 
				startActivity(intent);
				finish();
			}
		});
   
		// set edittext
		tv.setInputType(InputType.TYPE_CLASS_PHONE);

		// set DES
		etv.setText(getString(R.string.phone_number_content));

		// get Phone number
		try {
			TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String PhoneNumber = systemService.getLine1Number(); // 폰번호를 가져오는
																	// 겁니다..
			PhoneNumber = PhoneNumber.substring(PhoneNumber.length() - 10,
					PhoneNumber.length());
			PhoneNumber = "0" + PhoneNumber;
			// set Edittext
			tv.setText(PhoneNumber);
		} catch (Exception e) {

		}

	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				

				  Intent intent = new Intent(phone_number.this, result.class);
				  intent.putExtra("info", myResult);
				  intent.putExtra("kind", getString(R.string.phone_number));
		 	    	 startActivity(intent); 
				
				

				// setText

				return;
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

	// 빽백키 상단액션바
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.yes:
			//IF blank
			String s1 = tv.getText().toString();
			if(!s1.equals("")){
			// no error
			try {

				new Downloader().execute();

			} catch (Exception e) {

				// Not Connected To Internet
				AlertDialog.Builder builder = new AlertDialog.Builder(
						phone_number.this);
				builder.setMessage(getString(R.string.networkerrord))
						.setPositiveButton(getString(R.string.yes), null)
						.setTitle(getString(R.string.networkerror));
				builder.show();

			}
			}else{
				//No value
				// No Value
				AlertDialog.Builder builder = new AlertDialog.Builder(
						phone_number.this);
				builder.setMessage(getString(R.string.blank_error))
						.setPositiveButton(getString(R.string.yes), null)
						.setTitle(getString(R.string.error));
				builder.show();
			}
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
