//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.

/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tarks.favorite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import java.io.File;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.photoview.PhotoViewAttacher;
import com.tarks.favorite.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import com.tarks.favorite.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class GalleryView extends SherlockActivity {

//    static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %%";
 //   static final String SCALE_TOAST_STRING = "Scaled to: %.2ff";

    private TextView mCurrMatrixTv;

    private PhotoViewAttacher mAttacher;

    private Toast mCurrentToast;

    private Matrix mCurrentDisplayMatrix = null;
    
    ImageView mImageView;
    String path;
    boolean edit_mode;
    Uri image_uri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.imageview);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
        //Hide logo
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        //Load partially transparent black background
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg_black));

         mImageView = (ImageView) findViewById(R.id.iv_photo);

        Intent intent = getIntent();// 인텐트 받아오고
		path = intent.getStringExtra("path");
		image_uri = intent.getParcelableExtra("uri");
        edit_mode = intent.getBooleanExtra("edit", false);
       // Drawable bitmap = getResources().getDrawable(R.drawable.splash);
       if(path != null) mImageView.setImageBitmap(Global.UriToBitmapCompress(Uri.fromFile(new File(path))));
       if(image_uri != null) mImageView.setImageURI(image_uri);
        // The MAGIC happens here!
        mAttacher = new PhotoViewAttacher(mImageView);

        // Lets attach some listeners, not required though!
        mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
        mAttacher.setOnPhotoTapListener(new PhotoTapListener());
    }
    

    private Intent createShareIntent() {
     	Bitmap bm = null;
        if(path != null) bm = Global.UriToBitmapCompress(Uri.fromFile(new File(path)));
        if(image_uri != null) bm =  Global.UriToBitmapCompress(image_uri);
        String share_temp_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +  "/Favorite/";
        String filename = "temp.jpg";
        Global.SaveBitmapToFileCache(bm, share_temp_path, filename);
        
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		  shareIntent.setType("image/*");
		  File pathfile = new File(share_temp_path + filename);
		  Uri uri = Uri.fromFile(pathfile);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

		
		return shareIntent;
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem item;
		
	  
 if(edit_mode){
		menu.add(0, 1, 0, getString(R.string.ok)).setIcon(R.drawable.accept)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
 }else{
		// Inflate your menu.
		getSupportMenuInflater().inflate(R.menu.share_action_provider, menu);

		// Set file with share history to the provider and set the share intent.
		MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.
		actionProvider.setShareIntent(createShareIntent());
 }

		return true;

	}
    
    //빽백키 상단액션바
	   @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case 1:
	    	Bitmap bm = null;
	        if(path != null) bm = Global.UriToBitmapCompress(Uri.fromFile(new File(path)));
	        if(image_uri != null) bm =  Global.UriToBitmapCompress(image_uri);
	        Global.SaveBitmapToFileCache(bm, getCacheDir().toString(), "/attach_image.jpg");
			//Globalvariable.imagebitmap = null;
	     			
	    			  // Intent 생성
	    			   Intent intent = new Intent();
	    			   // 생성한 Intent에 데이터 입력
//	    			   intent.putExtra("image", b);
	    			   // 결과값 설정(결과 코드, 인텐트)
	    			   this.setResult(RESULT_OK,intent);
	    			   // 본 Activity 종료
	    			   finish();
	    			   
	    			   return true;
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    
	    
	   }
    @Override
    public void onDestroy() {
        super.onDestroy();

        Global.recycleBitmap(mImageView);
        // Need to call clean-up
        mAttacher.cleanup();
    }

  
   
    private class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;

        //    showToast(String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage));
        }
    }

//    private void showToast(CharSequence text) {
//        if (null != mCurrentToast) {
//            mCurrentToast.cancel();
//        }
//
//        mCurrentToast = Toast.makeText(PhotoView.this, text, Toast.LENGTH_SHORT);
//        mCurrentToast.show();
//    }

    private class MatrixChangeListener implements OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {
        //    mCurrMatrixTv.setText(rect.toString());
        }
    }
    
 

}
