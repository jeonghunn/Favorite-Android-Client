//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.ui;

import com.tarks.favorite.R;
import com.tarks.favorite.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;


public class setting extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //액션바백버튼가져오기
        if (android.os.Build.VERSION.SDK_INT > 10) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new setting_fragment()).commit();
        }else{
        	Intent intent1 = new Intent(setting.this, setting_compat.class);
			startActivity(intent1);
			finish();
        }
       
    }
      //빽백키 상단액션바
 	   @Override
 	    public boolean onOptionsItemSelected(MenuItem item) {
 	    switch (item.getItemId()) {
 	        case android.R.id.home:
 	            onBackPressed();
 	            return true;
 	        default:
 	            return super.onOptionsItemSelected(item);
 	    }
 	    
 	    
 	   }
    
}
