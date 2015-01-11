//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.ui;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tarks.favorite.R;
import com.tarks.favorite.core.connect.AsyncHttpTask;
import com.tarks.favorite.core.global.Global;
import com.tarks.favorite.core.global.Globalvariable;
import com.tarks.favorite.ui.start.join;
import com.tarks.favorite.ui.start.welcome;

public class tarks_account_login extends ActionBarActivity {
	Button bt;
	Button bt2;
	String myId, myPWord, myTitle, mySubject;
	String myResult = null;
	EditText edit1, edit2;
	String s1, s2;
//	boolean okbutton = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Can use progress
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.tarks_account);
		setSupportProgressBarIndeterminateVisibility(false);
		// 액션바백버튼가져오기
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				 getSupportActionBar().setDisplayShowHomeEnabled(false);
				 
					Intent intent = getIntent(); // 인텐트 받아오고
					boolean auto_login = intent.getBooleanExtra("auto", false); // 인텐트로 부터 데이터 가져오고
					 s1 = intent.getStringExtra("id"); // 인텐트로 부터 데이터 가져오고
					 s2 = intent.getStringExtra("password"); // 인텐트로 부터 데이터 가져오고
				

		// define edittext
		edit1 = (EditText) findViewById(R.id.editText1);
		edit2 = (EditText) findViewById(R.id.editText2);
		
		if(s1 != null) edit1.setText(s1);
		if(s2 != null) edit2.setText(s2);

		bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("https://tarks.net/index.php?mid=main&act=dispMemberFindAccount");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		bt2 = (Button) findViewById(R.id.button2);
		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(tarks_account_login.this, sign_up.class); 
				 startActivityForResult(intent, 1);
			}
		});
		
		//Auto Login
		if(auto_login)
			try {
				TarksAccountLogin();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
//	public void ConnectionError() {
//		// If No Network Connection
//		// Check Internet Connection
//
//		// Check Network Connection
//		if (Global.InternetConnection(1) == true
//				|| Global.InternetConnection(0) == true) {
//
//			 Intent intent = new Intent(tarks_account_login.this, webview.class);
//			  intent.putExtra("url", getString(R.string.server_path));
//	 	    	 startActivity(intent);
//
//	 	    	 finish();
//
//
//		} else {
//
//
//		    	// 로딩 화면은 종료하라.
//				Toast.makeText(tarks_account_login.this,
//						getString(R.string.networkerrord), 0).show();
//
//
//
//		}
//	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			//Stop progressbar
			setSupportProgressBarIndeterminateVisibility(false);
			
			if (msg.what == -1) {
				Global.ConnectionError(tarks_account_login.this, msg.obj.toString());
				
			}

			if (msg.what == 1) {
				myResult = msg.obj.toString();
				if (myResult.matches("")) {
					// Error Login
					AlertDialog.Builder builder1 = new AlertDialog.Builder(
							tarks_account_login.this);
					builder1.setMessage(getString(R.string.error_login))
							.setPositiveButton(getString(R.string.yes), null)
							.setTitle(getString(R.string.error));
					builder1.show();
				} else {


					// Intent 생성
					Intent intent = new Intent();
					// 생성한 Intent에 데이터 입력
					intent.putExtra("id", edit1.getText().toString());
					intent.putExtra("auth_code", myResult);
					// 결과값 설정(결과 코드, 인텐트)
					tarks_account_login.this.setResult(RESULT_OK, intent);
					// 본 Activity 종료
					finish();
				}

			}

		}
	};

	public void TarksAccountLogin() throws NoSuchAlgorithmException {
		// Set Progress
		setSupportProgressBarIndeterminateVisibility(true);

		// import EditText string

		String s1 = edit1.getText().toString();
		String s2 = edit2.getText().toString();

		
	//	Log.i("password", enc);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("a");
        Paramname.add("id");
		Paramname.add("password");


		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("make_tarks_authcode");
        Paramvalue.add(s1);
		Paramvalue.add(s2);

		new AsyncHttpTask(this, getString(R.string.server_api_path)
				, mHandler, Paramname,
				Paramvalue, null, 1,0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
		     
				String id = data.getStringExtra("id");
				String password = data.getStringExtra("password");
				
				edit1.setText(id);
				edit2.setText(password);
				
				try {
					TarksAccountLogin();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 
					
					
			}
		}
		
		
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	//	this.optionsMenu = menu;
	//	ActionMenuItem item = null;


		
		MenuItemCompat.setShowAsAction(	menu.add(0, 1, 0, getString(R.string.ok)).setIcon(R.drawable.accept), MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case 1:
			// Check okbutton
			if (Globalvariable.okbutton == true) {
				edit1 = (EditText) findViewById(R.id.editText1);
				s1 = edit1.getText().toString();

				// no err
				try {
					// import EditText

					// edit2 = (EditText) findViewById(R.id.editText2);
					// String s2 = edit2.getText().toString();

					if (s1.matches("")) {
						// Show type id noti
						Global.Infoalert(this,
								getString(R.string.notification),
								getString(R.string.type_id),
								getString(R.string.yes));
					} else {
						// TODO Auto-generated method stub
						TarksAccountLogin();
					}
				} catch (Exception e) {
					// Log.i("ERROR", "App has been error");
					// System.out.println();
					// Not Connected To Internet
					Global.ConnectionError(this, e.toString());

				}
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
