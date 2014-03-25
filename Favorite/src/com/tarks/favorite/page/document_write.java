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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.tarks.favorite.R;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.start.join;
import com.tarks.favorite.start.welcome;

public class document_write extends Activity {

	Button bt;

	String content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.documet_write);
		// 액션바백버튼가져오기
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		

		// Let's Start!
		bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText edit1 = (EditText) findViewById(R.id.editText1);
				content = edit1.getText().toString();

				PostAct();
			}
		});

	}

	public void PostAct() {
		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
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
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add("null");
		Paramvalue.add(content);
		Paramvalue.add("3");
		Paramvalue.add("1");
		Paramvalue.add("0");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/documents_app.php", mHandler, Paramname, Paramvalue,
				null, 1);
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// IF Sucessfull no timeout

			if (msg.what == -1) {
				Global.ConnectionError(document_write.this);
			}

			if (msg.what == 1) {
				String result = msg.obj.toString();
				if(result.matches("document_write_succeed")) {
					finish();
				}else{
					Global.ConnectionError(document_write.this);
				}
				Log.i("Result","로그 정상 작동");
				Log.i("Result", msg.obj.toString());
			}

		}
	};

	

}
