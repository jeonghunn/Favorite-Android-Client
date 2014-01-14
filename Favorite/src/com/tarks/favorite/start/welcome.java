package com.tarks.favorite.start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tarks.favorite.CropManager;
import com.tarks.favorite.Global;
import com.tarks.favorite.R;
import com.tarks.favorite.tarks_account_login;
import com.tarks.favorite.connect.AsyncHttpTask;




public class welcome extends SherlockActivity {
	Button bt;
    String myId, myResult;
	Message msg;
	String ToonDataList;
	
//	 private class Downloader extends AsyncTask<String, Void, Bitmap> {
//
//			protected Bitmap doInBackground(String... param) {
//				// TODO Auto-generated method stub
//				  //Error Login
//				
//				return downloadBitmap(myId);
//			}
//
//			@Override
//			protected void onPreExecute() {
//				Log.i("Async-Example", "onPreExecute Called");
//
//			}
//
//			protected void onPostExecute(Bitmap result) {
//				Log.i("Async-Example", "onPostExecute Called");
//				
//				
//				msg = Message.obtain();
//				msg.what = 0;
//				mHandler.sendMessage(msg);
//				
//			}
//
//			private Bitmap downloadBitmap(String url ) {
//				try {
//
//
//	    			
//	    				// --------------------------
//	    				// URL 설정하고 접속하기
//	    				// --------------------------
//	    				URL url1 = new URL(
//	    						getString(R.string.server_path) + "statistics.php"); // URL
//	    																				// 설정
//	    				HttpURLConnection http = (HttpURLConnection) url1
//	    						.openConnection(); // 접속
//	    				// --------------------------
//	    				// 전송 모드 설정 - 기본적인 설정이다
//	    				// --------------------------
//	    				http.setDefaultUseCaches(false);
//	    				http.setDoInput(true); // 서버에서 읽기 모드 지정
//	    				http.setDoOutput(true); // 서버로 쓰기 모드 지정
//	    				http.setRequestMethod("POST"); // 전송 방식은 POST
//
//	    				// 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
//	    				http.setRequestProperty("content-type",
//	    						"application/x-www-form-urlencoded");
//	    				// --------------------------
//	    				// 서버로 값 전송
//	    				// --------------------------
//	    				StringBuffer buffer = new StringBuffer();
//	    				buffer.append("authcode").append("=").append("642979");
//
//	    				OutputStreamWriter outStream = new OutputStreamWriter(
//	    						http.getOutputStream(), "EUC-KR");
//	    				PrintWriter writer = new PrintWriter(outStream);
//	    				writer.write(buffer.toString());
//	    				writer.flush();
//	    				// --------------------------
//	    				// 서버에서 전송받기
//	    				// --------------------------
//	    				 InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");  
//	    	              BufferedReader reader = new BufferedReader(tmp); 
//	    	              StringBuilder builder = new StringBuilder(); 
//	    	              String str; 
//	    	              
//	    	              while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다 
//	    	                   builder.append(str);  //구분자 추가 안함
//	    	                  // builder.append(str + "\n"); 
//	    	                   // View에 표시하기 위해 라인 구분자 추가 
//	    	              } 
//	    	              myResult = builder.toString();                       // 전송결과를 전역 변수에 저장
//	    	//	Log.i("dfd", myResult);
//	    			} catch (MalformedURLException e) { 
//	    	                // 
//	    	         } catch (IOException e) { 
//	    	                //  
//	    	         }
//
//				return null;
//			}
//
//	    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
    	//no error
	//	try{	
	Log.i("Cache folder", getCacheDir().toString());
			//	new Downloader()
		//				.execute();
//        ArrayList<String> Paramname = new ArrayList<String>();
//        Paramname.add("authcode");
//        Paramname.add("tarks_account");
//        
//        ArrayList<String> Paramvalue = new ArrayList<String>();
//        Paramvalue.add("642979");
//        Paramvalue.add("jeonghunn");
//				
//        
//				new Global.AsyncHttpTask(this, getString(R.string.server_path) + "member/tarks_account_check.php", mHandler, Paramname, Paramvalue, null, 1);
//				
//				new AsyncHttpTask.execute("","","");
//
	//	} catch (Exception e){
		
			// Not Connected To Internet
//			AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
//			builder.setMessage(getString(R.string.networkerrord))
//					.setPositiveButton(getString(R.string.yes), null)
//					.setTitle(getString(R.string.networkerror));
//			builder.show();
			
	//	}
	 //Let's Start!
	Button bt2 = (Button) findViewById(R.id.tarks_login);
	bt2.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(welcome.this, tarks_account_login.class); 
			 startActivityForResult(intent, 1);
		}
	});    
	
        //Let's Start!
    	bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(welcome.this, join.class); 
				startActivity(intent);
				finish();
			}
		});
   
    }
    

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
		     	  //Save auth key to temp
		           //Setting Editor
		 			SharedPreferences edit = getSharedPreferences("temp",
		 					MODE_PRIVATE);
		 			SharedPreferences.Editor editor = edit.edit();
		 			editor.putString("temp_id",  data.getStringExtra("id"));			
		 			editor.putString("temp_id_auth",  data.getStringExtra("auth_code"));			
		 			editor.commit();
		 			
		 			Intent intent = new Intent(welcome.this, join.class); 
					startActivity(intent);
					finish();
					
					
			}
		}
		
		
	}

    
 
	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				// Cut Result Value
				StringTokenizer st = new StringTokenizer(myResult, "/LINE/.");
				int users = Integer.parseInt(st.nextToken());
				int check = Integer.parseInt(st.nextToken());
				int request = Integer.parseInt(st.nextToken());
				int message = Integer.parseInt(st.nextToken());
				//number cut
				NumberFormat nf = NumberFormat.getInstance();
				//  nf.setMaximumIntegerDigits(5); //최대수 지정
				 String users_result = nf.format(users);
				 String request_result = nf.format(request);
				//make value
				Resources res = getResources();
				String value = String.format(res.getString(R.string.statistics_des), users_result, request_result); 
			//setText
				
				TextView text = (TextView) findViewById(R.id.textView1);
				text.setText(value);
				return;
			}
			
			
			if (msg.what == 1) {
				Log.i("Success", "Congratulation you can sleep keke");
				Log.i("Success", msg.obj.toString());
			}
			
		}
	};
	
    
    	
    }
    

