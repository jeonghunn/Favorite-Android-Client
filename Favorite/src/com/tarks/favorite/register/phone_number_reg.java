package com.tarks.favorite.register;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.tarks.favorite.R;
import com.tarks.favorite.start.join;
import com.tarks.favorite.test.phone_number;

public class phone_number_reg extends SherlockActivity {

	//Edittex
	  EditText tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number);
      //액션바백버튼가져오기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
        
        
        //Import Edittext
      tv = (EditText) findViewById(R.id.phone_number_form);
      
   // set edittext
   		tv.setInputType(InputType.TYPE_CLASS_PHONE);
        
        
      //import button
      		ImageButton bt = (ImageButton) findViewById(R.id.imageButton1);
      		bt.setOnClickListener(new OnClickListener() {

      			@Override
      			public void onClick(View v) {
      				
   
      		        Intent intent = new Intent(Intent.ACTION_PICK);
      		   	 intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
      	         startActivityForResult(intent, 0);
      			}
      		});
         
      		
      

   
   
       
    }
    


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode == RESULT_OK)
    	{
    		try{
    			Cursor cursor = getContentResolver().query(data.getData(), 
    					new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, 
    				ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
    			cursor.moveToFirst();
     	               tv.setText(cursor.getString(1));       //0 = name 1= number
                cursor.close();
    		}catch (Exception e){
    			Toast.makeText(phone_number_reg.this, getString(R.string.contact_pick_error),
						0).show();
    		}
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    
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

			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

    
}
