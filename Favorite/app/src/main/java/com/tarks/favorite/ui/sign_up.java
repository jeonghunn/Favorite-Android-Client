//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.tarks.favorite.R;
import com.tarks.favorite.core.connect.AsyncHttpTask;
import com.tarks.favorite.core.global.Global;
import com.tarks.favorite.core.global.Globalvariable;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class sign_up extends ActionBarActivity {
	private Button bt;
	private Button bt2;
	private String myId, myPWord, myTitle, mySubject;
	private String myResult = null;
	private EditText email_edittext, id_edittext, password_edittext;
	private String email, id, password;

	// boolean okbutton = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Can use progress
		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.sign_up);
		setSupportProgressBarIndeterminateVisibility(false);
		// 액션바백버튼가져오기
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		// define edittext
		email_edittext = (EditText) findViewById(R.id.email_edittext);
		id_edittext = (EditText) findViewById(R.id.editText1);
		password_edittext = (EditText) findViewById(R.id.editText2);

		email_edittext.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {

				if (!s.toString().contains("@")) {
					id_edittext.setText(s.toString());
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

	}

//	public void ConnectionError() {
//		// If No Network Connection
//		// Check Internet Connection
//
//		// Check Network Connection
//		if (Global.InternetConnection(1) == true
//				|| Global.InternetConnection(0) == true) {
//
//			Intent intent = new Intent(sign_up.this, webview.class);
//			intent.putExtra("url", getString(R.string.server_path));
//			startActivity(intent);
//
//			finish();
//
//		} else {
//
//			// 로딩 화면은 종료하라.
//			Toast.makeText(sign_up.this, getString(R.string.networkerrord), Toast.LENGTH_SHORT).show();
//
//		}
//	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// Stop progressbar
			setSupportProgressBarIndeterminateVisibility(false);

			if (msg.what == -1) {
				Global.ConnectionError(sign_up.this);

			}

			if (msg.what == 1) {
				myResult = msg.obj.toString();
				//Global.toast(myResult);
				if (!myResult.matches("succeed")) {
					

if(myResult.matches("special_char_error")) 	Global.Infoalert(sign_up.this ,getString(R.string.error),getString(R.string.id_password_special_char_error),getString(R.string.yes));
if(myResult.matches("email_exist_error")) 	Global.Infoalert(sign_up.this ,getString(R.string.error),getString(R.string.sign_up_email_exist),getString(R.string.yes));
if(myResult.matches("id_exist_error")) Global.Infoalert(sign_up.this ,getString(R.string.error),getString(R.string.sign_up_id_exist),getString(R.string.yes));
if(myResult.matches("error")) Global.ConnectionError(sign_up.this);

				
				} else {
			
					
	
					
					// Intent 생성
					Intent intent = new Intent();
					// 생성한 Intent에 데이터 입력
					intent.putExtra("id", id);
					intent.putExtra("password", password);
					// 결과값 설정(결과 코드, 인텐트)
					sign_up.this.setResult(RESULT_OK, intent);
					// 본 Activity 종료
					finish();
				}

			}

		}
	};

	public void SignUpTarksAccount(String email, String id, String password) throws NoSuchAlgorithmException {
		// Set Progress
		setSupportProgressBarIndeterminateVisibility(true);


		// Log.i("password", enc);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("a");
		Paramname.add("email");
		Paramname.add("id");
		Paramname.add("password");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("tarks_sign_up");
		Paramvalue.add(email);
		Paramvalue.add(id);
		Paramvalue.add(password);

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "api.php", mHandler, Paramname,
				Paramvalue, null, 1, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// this.optionsMenu = menu;
		// ActionMenuItem item = null;

		MenuItemCompat.setShowAsAction(menu
				.add(0, 1, 0, getString(R.string.ok))
				.setIcon(R.drawable.accept),
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

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

				email = email_edittext.getText().toString();
				id = id_edittext.getText().toString();
				password = password_edittext.getText().toString();

				// no err
				try {
					// import EditText

					// edit2 = (EditText) findViewById(R.id.editText2);
					// String s2 = edit2.getText().toString();

					if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
						// Show type id noti
						Global.Infoalert(this,
								getString(R.string.alert),
								getString(R.string.email_type_des),
								getString(R.string.yes));
					} else if(id.length() < 3 || id.length() > 20) {
						Global.Infoalert(this,
								getString(R.string.alert),
								getString(R.string.id_type_des),
								getString(R.string.yes));
					//	TarksAccountLogin();
					}else if(password.length() < 6 || password.length() > 20){
						Global.Infoalert(this,
								getString(R.string.alert),
								getString(R.string.password_type_des),
								getString(R.string.yes));
					}else{
						//RUN
						SignUpTarksAccount(email, id, password);
					}
				} catch (Exception e) {
					// Log.i("ERROR", "App has been error");
					// System.out.println();
					// Not Connected To Internet
                    Global.ConnectionError(sign_up.this);

				}
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
