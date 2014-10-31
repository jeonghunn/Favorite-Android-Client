//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.ui.start;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.tarks.favorite.R;
import com.tarks.favorite.ui.tarks_account_login;
import com.tarks.favorite.core.global.Globalvariable;




public class welcome extends ActionBarActivity {
	Button bt;
    String myId, myResult;
	Message msg;
	String ToonDataList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
       // Global.getCountryValue();

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
				
				SignupWithoutIDAlert();
				
			
			}
		});
   
    }
    
    
	public void SignupWithoutIDAlert() {
		// Alert
		AlertDialog.Builder builder = new AlertDialog.Builder(welcome.this);
		builder.setMessage(getString(R.string.sign_up_without_id_des)).setTitle(
				getString(R.string.alert));
		builder.setPositiveButton(getString(R.string.continu),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						
						Intent intent = new Intent(welcome.this, join.class); 
						startActivity(intent);
						finish();

					}
				});
		builder.setNegativeButton(getString(R.string.no),null);
				
		builder.show();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
		     	  //Save auth key to temp
		           //Setting Editor
				Globalvariable.temp_id = data.getStringExtra("id");
				Globalvariable.temp_id_auth = data.getStringExtra("auth_code");
//		 			SharedPreferences edit = getSharedPreferences("temp",
//		 					MODE_PRIVATE);
//		 			SharedPreferences.Editor editor = edit.edit();
//		 			editor.putString("temp_id",  data.getStringExtra("id"));			
//		 			editor.putString("temp_id_auth",  data.getStringExtra("auth_code"));			
//		 			editor.commit();
		 			
		 			Intent intent = new Intent(welcome.this, join.class); 
					startActivity(intent);
					finish();
					
					
			}
		}
		
		
	}
	
	

    
    	
    }
    

