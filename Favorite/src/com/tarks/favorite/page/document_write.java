package com.tarks.favorite.page;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;



import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;
import com.tarks.favorite.R;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.global.Globalvariable;
import com.tarks.favorite.start.join;
import com.tarks.favorite.start.welcome;

public class document_write extends SherlockActivity {

	Button bt;

	String content;
	String page_srl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Can use progress
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.document_write);
		// 액션바백버튼가져오기
		 getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Get Intent
		Intent intent = getIntent();// 인텐트 받아오고
		page_srl = intent.getStringExtra("page_srl");



	}

	public void PostAct() {
		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("page_srl");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("title");
		Paramname.add("content");
		Paramname.add("permission");
		Paramname.add("status");
		Paramname.add("privacy");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("1");
		Paramvalue.add(page_srl);
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add("null");
		Paramvalue.add(Global.setValue(content));
		Paramvalue.add("3");
		Paramvalue.add("0");
		Paramvalue.add("0");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/documents_app_write.php", mHandler, Paramname, Paramvalue,
				null, 1,0);
	}
	
	public void FinishAct(){
		  Intent intent = new Intent();
		   this.setResult(RESULT_OK,intent);
		finish();
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// IF Sucessfull no timeout
			setSupportProgressBarIndeterminateVisibility(false);
			if (msg.what == -1) {
				Global.ConnectionError(document_write.this);
			}

			if (msg.what == 1) {
				String result = msg.obj.toString();
				if(result.matches("document_write_succeed")) {
				FinishAct();
				}else{
					Global.ConnectionError(document_write.this);
				}
				Log.i("Result","로그 정상 작동");
				Log.i("Result", msg.obj.toString());
			
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
		case  R.id.yes:
			if (Globalvariable.okbutton == true) {
				// Set ok button disable
				Globalvariable.okbutton = false;
				Global.ButtonEnable(1);
				setSupportProgressBarIndeterminateVisibility(true);
				EditText edit1 = (EditText) findViewById(R.id.editText1);
				content = edit1.getText().toString();

				PostAct();

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
