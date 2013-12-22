package com.tarks.favorite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.google.android.gcm.GCMRegistrar;
import com.tarks.favorite.start.tarks_account_login;
import com.tarks.favorite.start.welcome;

public class MainActivity extends SherlockActivity {
	// 통신 스트링

	String sVersion; // 웹페이지에서 가져온 버젼이 저장됨
	String myId, myPWord, myTitle, mySubject, myResult;
	String infoResult;
	String REGid;
	//Notice Result
	String NoticeResult;
	
	// Allow Load
	boolean load = true;

	// Download User Infomation
	private class InfoDown extends AsyncTask<String, Void, Bitmap> {

		protected Bitmap doInBackground(String... param) {
			// TODO Auto-generated method stub

		
			
			return downloadBitmap(myId);

		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");

		}

		protected void onPostExecute(Bitmap result) {
			Log.i("Async-Example", "onPostExecute Called");
		//	load = "1";
			try{
			if (infoResult.startsWith("/LINE/.")) {
				//Account Changed
				// Alert
				AlertDialog.Builder alert = new AlertDialog.Builder(
						MainActivity.this);
				alert.setTitle(getString(R.string.error));
				alert.setMessage(getString(R.string.reg_id_error));
				alert.setIcon(R.drawable.ic_launcher);
				alert.setPositiveButton(getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// Clear Old Settings
								getSharedPreferences("setting", 0).edit()
										.clear().commit();
								getSharedPreferences("temp", 0).edit()
										.clear().commit();

								// New Start
								// 로딩 화면은 종료하라.
								finish();
								// 이동한다. 메인으로
								Intent intent = new Intent(
										MainActivity.this,
										MainActivity.class);
								startActivity(intent);

							}
						});
				alert.show();
			} else {
			
				// Cut Result Value
//				StringTokenizer st = new StringTokenizer(infoResult, "/LINE/.");
//				String tarks_account = st.nextToken();
//				String name_1 = st.nextToken();
//				String name_2 = st.nextToken();
//				String permission = st.nextToken();
//				String reg_id = st.nextToken();
//				String key = st.nextToken();
//				String like_me =  String.valueOf(st.nextToken());
//				String favorite =  String.valueOf(st.nextToken());
			    Log.i("Result value",infoResult);
				String[] array = infoResult.split("/LINE/.");
				    Global.dumpArray(array);


					String tarks_account = array[0];
					String name_1 = array[1];
					String name_2 = array[2];
					String permission = array[3];
					String reg_id = array[4];
					String key = array[5];
					String like_me =  array[6];
					String favorite =  array[7];
				
				// 설정 값 저장
				// Setting Editor
				SharedPreferences edit = getSharedPreferences("setting",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = edit.edit();
				editor.putString("name_1", name_1);
				editor.putString("name_2", name_2);
				editor.putString("permission", permission);
				editor.putString("key", key);
				editor.putString("like_me", like_me);
				editor.putString("favorite", favorite);
				editor.commit();
				
				// Kind of Load Stop

				// Reg ID가 기존과 다를 때
				if (REGid.startsWith(reg_id)||reg_id.matches("null")) {
				} else {
					load = false;
					// Alert
					AlertDialog.Builder alert = new AlertDialog.Builder(
							MainActivity.this);
					alert.setTitle(getString(R.string.error));
					alert.setMessage(getString(R.string.reg_id_error));
					alert.setIcon(R.drawable.ic_launcher);
					alert.setPositiveButton(getString(R.string.yes),
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// Clear Old Settings
									getSharedPreferences("setting", 0).edit()
											.clear().commit();
									getSharedPreferences("temp", 0).edit()
											.clear().commit();

									// New Start
									// 로딩 화면은 종료하라.
									finish();
									// 이동한다. 메인으로
									Intent intent = new Intent(
											MainActivity.this,
											MainActivity.class);
									startActivity(intent);

								}
							});
					alert.show();
				}


				// Permission Denied
				if (permission.matches("4")) {
					load = false;
					// Alert
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MainActivity.this);
					builder.setMessage(getString(R.string.permission_denied))
							.setPositiveButton(getString(R.string.yes), null)
							.setTitle(getString(R.string.error));
					builder.show();
				}
				// 제한사항이 없을 경우
				if (load == true) {
					// 로딩 화면은 종료하라.
					finish();
					// 이동한다. 메인으로
					Intent intent = new Intent(MainActivity.this, main.class);
					startActivity(intent);

				}

				
				
			
			}
			}catch (Exception e){
			ConnectionError();
			}


		}

		private Bitmap downloadBitmap(String url) {
			try {

				// get user_srl
				// 설정 값 불러오기
				SharedPreferences prefs = getSharedPreferences("setting",
						MODE_PRIVATE);
				String user_srl = prefs.getString("user_srl", "");
				String user_srl_auth = prefs.getString("user_srl_auth", "");

				// --------------------------
				// URL 설정하고 접속하기
				// --------------------------
				URL url1 = new URL(
						"http://tarks.net/app/favorite/member/member_info.php"); // URL
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
				buffer.append("lang").append("=").append(getString(R.string.lang))
				.append("&"); // php 변수에 값 대입
				buffer.append("user_srl").append("=").append(user_srl)
				.append("&"); // php 변수에 값 대입
				buffer.append("user_srl_auth").append("=").append(user_srl_auth)
				.append("&"); // php 변수에 값 대입
				buffer.append("member_info").append("=").append("tarks_account//name_1//name_2//permission//reg_id//key//like_me//favorite");

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
	
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// 자신의 신분 설정값을 불러옵니다.
		SharedPreferences prefs = getSharedPreferences("setting", MODE_PRIVATE);
		String frist_use = prefs.getString("frist_use_app", "true");

	
		// Check Network Connection
	try{
			// Google Clound Message Registartion
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			final String regId = GCMRegistrar.getRegistrationId(this);
			if ("".equals(regId)) // 구글 가이드에는 regId.equals("")로 되어 있는데
									// Exception을 피하기 위해 수정
				GCMRegistrar.register(this, "743824910564");

			Log.d("==============", regId);
			REGid = regId.toString();
			// Let's Call Information
			// Connection Start
			// 처음 사용자가 아닐시에
			if (frist_use.matches("true")) {
				// 이동한다. 환영합니다로.
				Intent intent = new Intent(MainActivity.this, welcome.class);
				startActivity(intent);
				finish();
			} else {
				//First Notice Download
				new InfoDown().execute();
			
		} 
			
	}catch (Exception e){
		// If No Network Connection
					// 로딩 화면은 종료하라.
					finish();
					// 이동한다. 메인으로
					Intent intent = new Intent(MainActivity.this, main.class);
					startActivity(intent);
		
	}

		

		// ------------------------------
		// Http Post로 주고 받기
		// ------------------------------
		if (android.os.Build.VERSION.SDK_INT > 9) {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();

			StrictMode.setThreadPolicy(policy);

		}

	}

	
	  
	  
	public static boolean isStringDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public void ConnectionError(){
		// If No Network Connection
		// Check Internet Connection
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiAvail = ni.isAvailable();
		boolean isWifiConn = ni.isConnected();
		ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileAvail = ni.isAvailable();
		boolean isMobileConn = ni.isConnected();

		// Check Network Connection
		if (isWifiConn == true || isMobileConn == true) {
			//Show Alert
			AlertDialog.Builder alert = new AlertDialog.Builder(
					MainActivity.this);
			alert.setTitle(getString(R.string.error));
			alert.setMessage(getString(R.string.server_connection_error_des));
			alert.setIcon(R.drawable.ic_launcher);
			alert.setPositiveButton(getString(R.string.check_service_status),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int which) {
							
							 Uri uri = Uri.parse("https://sites.google.com/site/tarksservicesstatus/");
								Intent it = new Intent(Intent.ACTION_VIEW, uri);
								startActivity(it);
						}
					});
			alert.setNegativeButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog,
						int which) {
				
					finish();
			

				}
			});
			alert.show();
			
		}else{
		// 로딩 화면은 종료하라.
		  Toast.makeText(MainActivity.this, getString(R.string.networkerrord), 0).show(); 
		finish();
		// 이동한다. 메인으로
		Intent intent = new Intent(MainActivity.this, main.class);
		startActivity(intent);
		}
	}
	
}