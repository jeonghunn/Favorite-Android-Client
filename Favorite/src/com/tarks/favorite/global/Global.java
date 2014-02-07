package com.tarks.favorite.global;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.android.gcm.GCMRegistrar;
import com.tarks.favorite.ModApplication;
import com.tarks.favorite.R;

public final class Global {

	// private static int rosp;
	// public static boolean isFirst = true;
	// public static boolean isFirstRuned = true;
	// public static boolean isFirstMain = true;

	 //private EditText mEdityEntry; 
	//ModApplication
	static ModApplication mod = ModApplication.getInstance();
	


	
	private Global() {

	}

	// md5 encrypt 암호화 compatible with php
	public static String getMD5Hash(String s) throws NoSuchAlgorithmException {

		  String result = s;
		    if(s != null) {
		        MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
		        md.update(s.getBytes());
		        BigInteger hash = new BigInteger(1, md.digest());
		        result = hash.toString(16);
		        while(result.length() < 32) { //40 for SHA-1
		            result = "0" + result;
		        }
		    }
		    return result;
	}
	//MD5 default
//	MessageDigest m = null;
//	String hash = null;
//
//	try {
//		m = MessageDigest.getInstance("MD5");
//		m.update(s.getBytes(), 0, s.length());
//		hash = new BigInteger(1, m.digest()).toString(16);
//	} catch (NoSuchAlgorithmException e) {
//		e.printStackTrace();
//	}
//
//	return hash;
	

	// Google Clound Message Registartion
	public static String GCMReg() {
		GCMRegistrar.checkDevice(mod);
		GCMRegistrar.checkManifest(mod);
		final String regId = GCMRegistrar.getRegistrationId(ModApplication
				.getInstance());
		if ("".equals(regId)) // 구글 가이드에는 regId.equals("")로 되어 있는데
								// Exception을 피하기 위해 수정
			GCMRegistrar.register(mod, "743824910564");

		return regId;

	}

	public static void toast(String str, boolean length) {
		// Log.i("ACCESS", "I can access to toast");
		Toast.makeText(mod, str,
				(length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
	}

	public static void toast(String str) {
		toast(str, false);
	}

	// Show Information alert
	public static void Infoalert(Context context, String title, String message,
			String button) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message).setPositiveButton(button, null)
				.setTitle(title);
		builder.show();

	}

	// 배열을 화면에, 요소별로 알기 쉽게 출력
	public static void dumpArray(String[] array) {
		for (int i = 0; i < array.length; i++)
			System.out.format("array[%d] = %s%n", i, array[i]);
	}

	//Check network connection
	// 1 is wifi 0 is mobile
	public static boolean InternetConnection(int network) {
		ConnectivityManager cm = (ConnectivityManager) ModApplication
				.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni;
		boolean connect;
		if (network == 1) {
			ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			// boolean isWifiAvail = ni.isAvailable();
			connect = ni.isConnected();
		} else {
			ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			// boolean isMobileAvail = ni.isAvailable();
			connect = ni.isConnected();
		}
		return connect;
	}
	
	//Default Connection Error
	public static void ConnectionError(Context cx){
		if (InternetConnection(1) == true || InternetConnection(0) == true) {
			Infoalert(cx, cx.getString(R.string.error),
					cx.getString(R.string.error_des), cx.getString(R.string.yes));
		} else{
		Infoalert(cx, cx.getString(R.string.networkerror),
					cx.getString(R.string.networkerrord),
					cx.getString(R.string.yes));

		}
	}
	
	//Make name
	public static String[] NameBuilder(String name_1, String name_2){
		String[] name = new String[2];
		if(mod.getString(R.string.lang).matches("ko")){
			 name[0] = name_1;
			 name[1] = name_2;
		}else{
			name[0] = name_2;
			name[1] = name_1;
		}
		return name;
	}
	
	
	public static int[] getIMGSize(ContentResolver cr, Uri uri) throws FileNotFoundException{
		Bitmap bm;
		int[] size = new int[2];
		
		  InputStream in = cr.openInputStream(uri);
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inPurgeable = true;
			 option.inJustDecodeBounds = true;
			//  BitmapFactory.decodeStream(in, null, option);
			bm = BitmapFactory.decodeStream(in,null, option);
			//1is height
			size[1] = option.outHeight;
			 size[0] = option.outWidth;
			 
			
			   
			   return size;

		}
	
	
	public static InputStream editIMGSize(ContentResolver cr, Uri uri) throws FileNotFoundException{
		int[] size = new int[2];
		Bitmap bm;
		  InputStream in = cr.openInputStream(uri);
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inPurgeable = true;
			 option.inJustDecodeBounds = true;
			//  BitmapFactory.decodeStream(in, null, option);
			bm = BitmapFactory.decodeStream(in,null, option);
			//1is height
			size[1] = option.outHeight;
			 size[0] = option.outWidth;
//			 
//			 if(editsize == true){
					if(size[1] > 4000)	option.inSampleSize = 8;
					if(size[1] > 1024)	option.inSampleSize = 4;
//			 }
//			   
			   return in;

		}
	
	public static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, String filename) {
        
		File file = new File(strFilePath);
		
		//If no folders
		 if( !file.exists() ) {
			 file.mkdirs();
	        //	Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
	        }
	 
		 
        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;
        
       
        try
        {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
 
            bitmap.compress(CompressFormat.JPEG, 100, out);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
  }
	
	//This is prevent too many server requests
	public static boolean ButtonEnable(final int s) {
		new Thread(new Runnable() {
			public void run() {
				int i = 0;
				while (true) {
					if (i > s) {
						Globalvariable.okbutton = true;
						break;
					} else {
						try {
							Thread.sleep(1000);
							i += 1;
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
					}

				}
			}
		}).start();
		return Globalvariable.okbutton;
	}
	
	public static String getPhoneNumber(boolean getphonenumber){
		// Get Country number and phone number
				// get Phone number
		String result = null;
				try {
					TelephonyManager systemService = (TelephonyManager) mod.getSystemService(Context.TELEPHONY_SERVICE);
					String Number = systemService.getLine1Number(); // 폰번호를 가져오는
																	// 겁니다..
					String PhoneNumber = Number.substring(Number.length() - 10,
							Number.length());
					
					String country_code = Number.substring(0,
							Number.length() - PhoneNumber.length());
					
					//KR
			//		String countryCode = systemService.getSimCountryIso();

					if(getphonenumber == true){
						result = PhoneNumber;
					}else{
						country_code = country_code.replace("+" , "");
						result = country_code;
					}
					// set Edittext
					// tv.setText(PhoneNumber);
					
					Log.i("Result", result);
				} catch (Exception e) {

				}
				
		
				
				return result;
	}
	

	
	//InternetConnection Error Message
	
	
	
//	public static Bitmap getCroppedBitmap(Bitmap bitmap) {
//	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
//	            bitmap.getHeight(), Config.ARGB_8888);
//	    Canvas canvas = new Canvas(output);
//
//	    final int color = 0xff424242;
//	    final Paint paint = new Paint();
//	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//	    paint.setAntiAlias(true);
//	    canvas.drawARGB(0, 0, 0, 0);
//	    paint.setColor(color);
//	    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
//	            bitmap.getWidth() / 2, paint);
//	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//	    canvas.drawBitmap(bitmap, rect, rect, paint);
//	    //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
//	    //return _bmp;
//	    return output;
//	}

	
}