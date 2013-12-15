package com.tarks.favorite.start;

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
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;
import com.google.android.gcm.GCMRegistrar;
import com.tarks.favorite.Global;
import com.tarks.favorite.MainActivity;
import com.tarks.favorite.R;
import com.tarks.favorite.R.string;



public class join extends SherlockActivity {
	
	//name
	String first_name;
	String last_name;

	private class InfoDown extends AsyncTask<String, Void, Bitmap> {

		protected Bitmap doInBackground(String... param) {
			// TODO Auto-generated method stub

			// Check This Tarks Account Already Have Buldang Account
			return downloadBitmap(myId);

		}

		@Override
		protected void onPreExecute() {
			//Log.i("Async-Example", "onPreExecute Called");
		}

		protected void onPostExecute(Bitmap result) {
		//	Log.i("Async-Example", "onPostExecute Called");
			// import EditText
			EditText edit1 = (EditText) findViewById(R.id.editText1);
			String s1 = edit1.getText().toString();

			EditText edit2 = (EditText) findViewById(R.id.editText2);
			String s2 = edit2.getText().toString();
//
//			EditText edit3 = (EditText) findViewById(R.id.editText3);
//			String s3 = edit3.getText().toString();

			// Check Tarks Account Exist
			if (!infoResult.matches("null")) {
				// Cut Result Value
				StringTokenizer st = new StringTokenizer(infoResult, "/LINE/.");
				String user_srl = st.nextToken();
				String name_1 = st.nextToken();
				String name_2 = st.nextToken();

				// Set EditText
				//Country
				if(getString(R.string.lang).matches("ko")){
				edit1.setText(name_1);
				edit2.setText(name_2);
				}else{
					edit1.setText(name_2);
					edit2.setText(name_1);
				}
//				edit3.setText(phone_number);
			 

		}else{
			//if null
		}
			
		}

		private Bitmap downloadBitmap(String url) {
			try {

				// get ID
				// 설정 값 불러오기
				SharedPreferences prefs = getSharedPreferences("temp",
						MODE_PRIVATE);
				String id = prefs.getString("temp_id", "null");

		

					// --------------------------
					// URL 설정하고 접속하기
					// --------------------------
					URL url1 = new URL(
							"http://tarks.net/app//favorite/member/get_member_info.php"); // URL
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
			//set Progressbar
		      setSupportProgressBarIndeterminateVisibility(true);

		}

		protected void onPostExecute(Bitmap result) {
			Log.i("Async-Example", "onPostExecute Called");
			//set Progressbar
		      setSupportProgressBarIndeterminateVisibility(false);
			// import EditText
			EditText edit1 = (EditText) findViewById(R.id.editText1);
			String s1 = edit1.getText().toString();

			EditText edit2 = (EditText) findViewById(R.id.editText2);
			String s2 = edit2.getText().toString();

//			EditText edit3 = (EditText) findViewById(R.id.editText3);
//			String s3 = edit3.getText().toString();
			// Check Success
			
			
			

			if (myResult.matches("")) {
				// IF Fail
				AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
				builder.setMessage(getString(R.string.error_des))
						.setPositiveButton(getString(R.string.yes), null)
						.setTitle(getString(R.string.error));
				builder.show();
			} else {
				// Go to Next Step

				// Setting Editor
				SharedPreferences edit = getSharedPreferences("setting",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = edit.edit();
				editor.putString("frist_use_app", "false"); // Ű��,
				editor.putString("user_srl", myResult);
				editor.putString("name_1", s1);
				editor.putString("name_2", s2);
				editor.commit();
				Intent intent = new Intent(join.this, MainActivity.class);
				startActivity(intent);
				finish();

			}
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

//				EditText edit3 = (EditText) findViewById(R.id.editText3);
//				String s3 = edit3.getText().toString();
				
				//Make name
				if(getString(R.string.lang).matches("ko")){
					first_name = s1;
					last_name = s2;
				}else{
					first_name = s2;
					last_name = s1;
				}
				
				//Reg id null
				if(reg_id.matches("")){
					reg_id = "null";
				}

				// --------------------------
				// URL 설정하고 접속하기
				// --------------------------
				URL url1 = new URL("http://tarks.net/app/favorite/member/join.php"); // URL
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
				buffer.append("tarks_account").append("=").append(id)
						.append("&");
				buffer.append("name_1").append("=").append(first_name).append("&");
				buffer.append("name_2").append("=").append(last_name).append("&");
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

	String user_srl, name, number, phone_number;
	String regId;
	String id;
	String reg_id;
	String myId, myPWord, myTitle, mySubject, myResult;
	String infoResult;
	//press back key
	private boolean mIsBackKeyPressed = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//Can use progress
		 requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.join);
		//no show progress now
		 setSupportProgressBarIndeterminateVisibility(false);

		// 액션바백버튼가져오기
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// get ID
		
		 Intent intent = getIntent();//인텐트  받아오고



		SharedPreferences prefs = getSharedPreferences("temp", MODE_PRIVATE);
		String id = prefs.getString("temp_id", "");
		String id_auth = prefs.getString("temp_id_auth", "");

		// set id Text
		TextView ids = (TextView) findViewById(R.id.textView2);
		ids.setText(id);


	
if(!id.matches("")){
			// Connection Start
	try{
			new InfoDown().execute();
	}catch (Exception e){
		// Not Connected To Internet
		AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
		builder.setMessage(getString(R.string.networkerrord))
				.setPositiveButton(getString(R.string.yes), null)
				.setTitle(getString(R.string.networkerror));
		builder.show();
	}
}
		
	}

	//백키를 눌렀을때의 반응.
		@Override
		public void onBackPressed() {
			if (mIsBackKeyPressed == false) {
				mIsBackKeyPressed = true;
				
				//Delete Temp ID
				  //Setting Editor
	 			SharedPreferences edit = getSharedPreferences("temp",
	 					MODE_PRIVATE);
	 			SharedPreferences.Editor editor = edit.edit();
	 			editor.remove("temp_id");		
	 			editor.remove("temp_id_auth");	
	 			editor.commit();
	 			//Go Back
				Intent intent = new Intent(join.this, tarks_account_login.class);
				startActivity(intent);
				finish();
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


			
				// import EditText
				EditText edit1 = (EditText) findViewById(R.id.editText1);
				String s1 = edit1.getText().toString();
				EditText edit2 = (EditText) findViewById(R.id.editText2);
				String s2 = edit2.getText().toString();

				// no value on name
				if (s1.matches("") || s2.matches("")) {
					// No Value
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							join.this);
//					builder.setMessage(getString(R.string.noname))
//							.setPositiveButton(getString(R.string.yes), null)
//							.setTitle(getString(R.string.error));
//					builder.show();
					Global.Infoalert(this ,getString(R.string.error), getString(R.string.noname), getString(R.string.yes));
				} else {
					//dont make error
					
			
					try{
					
				//Show Registering toast
						Global.toast(getString(R.string.registering));
					
				//Register GCM
					reg_id = Global.GCMReg();
				

					// Connection Start
					new Downloader().execute();
					
					}catch (Exception e){
				//Show network error
						Global.Infoalert(this ,getString(R.string.networkerror), getString(R.string.networkerrord), getString(R.string.yes));
					
					}
				}
			
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
