package com.tarks.favorite.global;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
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
import com.tarks.favorite.connect.ImageDownloader;

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
	
	//Make name value
	public static String NameMaker(String name_1, String name_2){
		String name;
		if(mod.getString(R.string.lang).matches("ko")){
			name = name_1 + name_2;
		}else{
			name = name_2 + " " + name_1;
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
	
	//Bitmap to File
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
	
	//Bitmap to uri
	public static  Uri getImageUri(Context inContext, Bitmap inImage) {
		  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		  inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		  String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		  return Uri.parse(path);
		}
	
	
	public static void UpdateFileCache(String new_update, String updatetime, String filepath ,String filesavepath, String filename){
		  File files = new File(filesavepath + filename);
		if(!new_update.matches(updatetime) || files.exists() == false){
		DownloadImageToFile(filepath, filesavepath, filename);
		}
	}
	
	public static void DownloadImageToFile(String filepath, String filesavepath, String filename){
		new ImageDownloader(mod, filepath, mHandler, 1);
		Globalvariable.filesavepath = filesavepath;
		Globalvariable.filename =  filename;
	}
	
	protected static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
		if(msg.what == 1){ 
		//	SaveBitmapToFileCache((Bitmap) msg.obj, );
			Global.SaveBitmapToFileCache((Bitmap) msg.obj,
					Globalvariable.filesavepath, Globalvariable.filename);
			Globalvariable.filesavepath = null;
			Globalvariable.filename = null;
		}

		}
	};
	
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
	
	public static String getSetting(String setting, String default_value){
		SharedPreferences prefs = mod.getSharedPreferences("setting",
				mod.MODE_PRIVATE);
		return prefs.getString(setting, default_value);
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
					


					if(getphonenumber == true){
						result = PhoneNumber;
					}else{
						country_code = country_code.replace("+" , "");
						result = country_code;
					}
					// set Edittext
					// tv.setText(PhoneNumber);
					
			// Log.i("Result", result);
		} catch (Exception e) {
			result = "0";
				}
				
		
				
				return result;
	}
	
	
	public static String getCountryValue(){
		String countryCode;

		TelephonyManager systemService = (TelephonyManager) mod.getSystemService(Context.TELEPHONY_SERVICE);
		countryCode = systemService.getSimCountryIso();
		
		if(countryCode.matches(""))countryCode = mod.getString(R.string.default_country);
		
		//Log.i("Country", countryCode + "df");
		return countryCode;
	}
	
	/**
	  * 지정한 패스의 파일을 읽어서 Bitmap을 리턴 (화면사이즈에 최다한 맞춰서 리스케일한다.)
	  *
	  * @param context
	  *            application context
	  * @param imgFilePath
	  *            bitmap file path
	  * @return Bitmap
	  * @throws IOException
	  */
	public static Bitmap loadBackgroundBitmap(Context context,
	        String imgFilePath) throws Exception, OutOfMemoryError { 
//	    if (!FileUtil.exists(imgFilePath)) {
//	        throw new FileNotFoundException("background-image file not found : " + imgFilePath);
//	    }

	    // 폰의 화면 사이즈를 구한다.
	  //  Display display = ((WindowManager) context
	  //          .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	//    int displayWidth = display.getWidth();
	//    int displayHeight = display.getHeight();
	// 
	    // 읽어들일 이미지의 사이즈를 구한다.
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inPreferredConfig = Config.RGB_565;
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(imgFilePath, options);
	 
	    // 화면 사이즈에 가장 근접하는 이미지의 리스케일 사이즈를 구한다.
	    // 리스케일의 사이즈는 짝수로 지정한다. (이미지 손실을 최소화하기 위함.) 
	//    float widthScale = options.outWidth / displayWidth;
	//    float heightScale = options.outHeight / displayHeight;
	 //   float scale = widthScale > heightScale ? widthScale : heightScale;
//	            
//	    if(scale >= 8) {
//	        options.inSampleSize = 8;
//	    } else if(scale >= 6) {
//	        options.inSampleSize = 6;
//	    } else if(scale >= 4) {
//	        options.inSampleSize = 4;
//	    } else if(scale >= 2) {
//	        options.inSampleSize = 2;
//	    } else {
//	        options.inSampleSize = 1;
//	    }
	    options.inJustDecodeBounds = false;
	 
	    return BitmapFactory.decodeFile(imgFilePath, options);
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