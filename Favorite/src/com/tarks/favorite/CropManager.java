//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite;

import java.io.ByteArrayOutputStream;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tarks.favorite.cropper.CropImageView;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.global.Globalvariable;
import com.actionbarsherlock.view.Menu;

public class CropManager extends SherlockActivity {

	// Static final constants
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final int ROTATE_NINETY_DEGREES = 90;
	private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
	private static final int ON_TOUCH = 1;

	// Instance variables
	private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
	private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

	//Bitmap croppedImage;
	CropImageView cropImageView;
	Bitmap bm;
	
	//bitmap
    Bitmap croppedImage;

	// Saves the state upon rotating the screen/restarting the activity
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
		bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
	}

	// Restores the state upon rotating the screen/restarting the activity
	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
		mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
		mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cropper);

		// 액션바백버튼가져오기
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 getSupportActionBar().setDisplayShowHomeEnabled(false);

		// get intent
		Intent intent = getIntent();// 인텐트 받아오고
		Uri image_uri = intent.getParcelableExtra("uri");


		// Sets fonts for all
		// Typeface mFont = Typeface.createFromAsset(getAssets(),
		// "Roboto-Thin.ttf");
		// ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
		// setFont(root, mFont);

	
		Bitmap firstbm;

		bm = Global.UriToBitmapCompress(image_uri);
		
		
				
				// Initialize components of the app
				cropImageView = (CropImageView) findViewById(R.id.CropImageView);
				
	
				
	
			cropImageView.setImageBitmap(bm);
			cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES,
					DEFAULT_ASPECT_RATIO_VALUES);
	
	

		
	

		// Sets aspectRatioX
		// final TextView aspectRatioX = (TextView)
		// findViewById(R.id.aspectRatioX);

		// aspectRatioXSeek.setOnSeekBarChangeListener(new
		// OnSeekBarChangeListener() {
		// @Override
		// public void onProgressChanged(SeekBar aspectRatioXSeek, int progress,
		// boolean fromUser) {
		// try {
		// mAspectRatioX = progress;
		// cropImageView.setAspectRatio(progress, mAspectRatioY);
		// aspectRatioX.setText(" " + progress);
		// } catch (IllegalArgumentException e) {
		// }
		// }
		//
		// @Override
		// public void onStartTrackingTouch(SeekBar seekBar) {
		// }
		//
		// @Override
		// public void onStopTrackingTouch(SeekBar seekBar) {
		// }
		// });

		// Sets aspectRatioY
		// final TextView aspectRatioY = (TextView)
		// findViewById(R.id.aspectRatioY);

		// aspectRatioYSeek.setOnSeekBarChangeListener(new
		// OnSeekBarChangeListener() {
		// @Override
		// public void onProgressChanged(SeekBar aspectRatioYSeek, int progress,
		// boolean fromUser) {
		// try {
		// mAspectRatioY = progress;
		// cropImageView.setAspectRatio(mAspectRatioX, progress);
		// aspectRatioY.setText(" " + progress);
		// } catch (IllegalArgumentException e) {
		// }
		// }

		// @Override
		// public void onStartTrackingTouch(SeekBar seekBar) {
		// }
		//
		// @Override
		// public void onStopTrackingTouch(SeekBar seekBar) {
		// }
		// });

		// Sets up the Spinner
		// showGuidelinesSpin.setOnItemSelectedListener(new
		// AdapterView.OnItemSelectedListener() {
		// public void onItemSelected(AdapterView<?> adapterView, View view, int
		// i, long l) {
		// cropImageView.setGuidelines(i);
		// }
		//
		// public void onNothingSelected(AdapterView<?> adapterView) {
		// return;
		// }
		// });

		// final Button cropButton = (Button) findViewById(R.id.Button_crop);
		// cropButton.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
 
		// ImageView croppedImageView = (ImageView)
		// findViewById(R.id.croppedImageView);
		// croppedImageView.setImageBitmap(croppedImage);
		// }
		// });

	}

	/*
	 * Sets the font on all TextViews in the ViewGroup. Searches recursively for
	 * all inner ViewGroups as well. Just add a check for any other views you
	 * want to set as well (EditText, etc.)
	 */
//	public void setFont(ViewGroup group, Typeface font) {
//		int count = group.getChildCount();
//		View v;
//		for (int i = 0; i < count; i++) {
//			v = group.getChildAt(i);
//			if (v instanceof TextView || v instanceof EditText
//					|| v instanceof Button) {
//				((TextView) v).setTypeface(font);
//			} else if (v instanceof ViewGroup)
//				setFont((ViewGroup) v, font);
//		}
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// 메뉴 버튼 구현부분
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.cropper, menu);
		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.yes:
			//result
			 croppedImage = cropImageView.getCroppedImage();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			croppedImage.compress(CompressFormat.JPEG, 100, stream); 
			byte[] b = stream.toByteArray();

			
	Globalvariable.image = b;
 			
			  // Intent 생성
			   Intent intent = new Intent();
			   // 생성한 Intent에 데이터 입력
//			   intent.putExtra("image", b);
			   // 결과값 설정(결과 코드, 인텐트)
			   this.setResult(RESULT_OK,intent);
			   // 본 Activity 종료
			   finish();
			return true;
		case R.id.rotate:
			cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
