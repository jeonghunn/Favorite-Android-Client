package com.tarks.favorite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gcm.GCMRegistrar;
import com.tarks.favorite.start.join;

public final class Global {

	// private static int rosp;
	// public static boolean isFirst = true;
	// public static boolean isFirstRuned = true;
	// public static boolean isFirstMain = true;

	//Upload
	private static FileInputStream mFileInputStream = null;
	private static URL connectUrl = null;
	//private EditText mEdityEntry; 
	

	
	private Global() {

	}

	// md5 encrypt 암호화
	public static String getMD5Hash(String s) {
		MessageDigest m = null;
		String hash = null;

		try {
			m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
			hash = new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return hash;
	}

	// Google Clound Message Registartion
	public static String GCMReg() {
		GCMRegistrar.checkDevice(ModApplication.getInstance());
		GCMRegistrar.checkManifest(ModApplication.getInstance());
		final String regId = GCMRegistrar.getRegistrationId(ModApplication
				.getInstance());
		if ("".equals(regId)) // 구글 가이드에는 regId.equals("")로 되어 있는데
								// Exception을 피하기 위해 수정
			GCMRegistrar.register(ModApplication.getInstance(), "743824910564");

		return regId;

	}

	public static void toast(String str, boolean length) {
		// Log.i("ACCESS", "I can access to toast");
		Toast.makeText(ModApplication.getInstance(), str,
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
	
	//Make name
	public static String[] NameBuilder(String name_1, String name_2){
		String[] name = new String[2];
		if(ModApplication.getInstance().getString(R.string.lang).matches("ko")){
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
	
	
	public static void DoFileUpload(String file) throws IOException {
		Log.i("upload", "upload start");
		Log.d("Test" , "file path = " + file);		
		HttpFileUpload(ModApplication.getInstance().getString(R.string.server_path) + "upload.php", "profile", file);	
	}
	
	private static void HttpFileUpload(String urlString, String category, String fileName) {
		
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";	
		
		try {
			
			mFileInputStream = new FileInputStream(fileName);			
			connectUrl = new URL(urlString);
			Log.d("Test", "mFileInputStream  is " + mFileInputStream);
			
			// open connection 
			HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();			
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			
			// write data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		    dos.writeBytes(twoHyphens + boundary + lineEnd); //필드 구분자 시작
		    dos.writeBytes("Content-Disposition: form-data; name=\"authcode\""+ lineEnd);
		    dos.writeBytes(lineEnd);
		    dos.writeBytes("642979");
		    dos.writeBytes(lineEnd);
		    dos.writeBytes("Content-Disposition: form-data; name=\"category\""+ lineEnd);
		    dos.writeBytes(lineEnd);
		    dos.writeBytes(category);
		    dos.writeBytes(lineEnd);
			
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName+"\"" + lineEnd);
			dos.writeBytes(lineEnd);
			
			int bytesAvailable = mFileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			
			byte[] buffer = new byte[bufferSize];
			int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
			
			Log.d("Test", "image byte is " + bytesRead);
			
			// read image
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = mFileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
			}	
			
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			// close streams
			Log.e("Test" , "File is written");
			mFileInputStream.close();
			dos.flush(); // finish upload...			
			
			// get response
			int ch;
			InputStream is = conn.getInputStream();
			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}
			String s=b.toString(); 
			Log.e("Test", "result = " + s);
		
			dos.close();	
			
			
			
		} catch (Exception e) {
			Log.d("Test", "exception " + e.getMessage());
			// TODO: handle exception
		}		
	}
	
	
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