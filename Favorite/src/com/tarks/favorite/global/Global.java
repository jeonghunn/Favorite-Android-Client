//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.global;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.tarks.favorite.webview;
import com.tarks.favorite.page.document_write;
import com.tarks.favorite.user.db.DbOpenHelper;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import com.google.android.gcm.GCMRegistrar;
import com.tarks.favorite.MainActivity;
import com.tarks.favorite.ModApplication;
import com.tarks.favorite.R;
import com.tarks.favorite.main;

public final class Global {

	// private static int rosp;
	// public static boolean isFirst = true;
	// public static boolean isFirstRuned = true;
	// public static boolean isFirstMain = true;

	// private EditText mEdityEntry;
	// ModApplication
	static ModApplication mod = ModApplication.getInstance();

	private Global() {

	}

	// md5 encrypt 암호화 compatible with php
	public static String getMD5Hash(String s) throws NoSuchAlgorithmException {

		String result = s;
		if (s != null) {
			MessageDigest md = MessageDigest.getInstance("MD5"); // or "SHA-1"
			md.update(s.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			while (result.length() < 32) { // 40 for SHA-1
				result = "0" + result;
			}
		}
		return result;
	}

	// MD5 default
	// MessageDigest m = null;
	// String hash = null;
	//
	// try {
	// m = MessageDigest.getInstance("MD5");
	// m.update(s.getBytes(), 0, s.length());
	// hash = new BigInteger(1, m.digest()).toString(16);
	// } catch (NoSuchAlgorithmException e) {
	// e.printStackTrace();
	// }
	//
	// return hash;

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

		try {

			if (Globalvariable.alert_status == true) {
				Globalvariable.alert_status = false;
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				
				
				
				
				builder.setMessage(message).setPositiveButton(button, null)
						.setTitle(title);

				//Check OS
				if (Build.VERSION.SDK_INT >= 17) {
				// Dialog Dismiss시 Event 받기
				builder.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						Globalvariable.alert_status = true;
					}
				});
				
				}else{
					Globalvariable.alert_status = true;
				}
				
		
				builder.show();
				
			


			}

		} catch (Exception e) {
			Globalvariable.alert_status = true;
		}
	}
	
	
	// 배열을 화면에, 요소별로 알기 쉽게 출력
	public static void dumpArray(String[] array) {
		for (int i = 0; i < array.length; i++)
			System.out.format("array[%d] = %s%n", i, array[i]);
	}

	// Check network connection
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

	// Default Connection Error
	public static void ConnectionError(Context cx) {
		if (InternetConnection(1) == true || InternetConnection(0) == true) {
			Infoalert(cx, cx.getString(R.string.error),
					cx.getString(R.string.error_des),
					cx.getString(R.string.yes));
			
			
		} else {
			Infoalert(cx, cx.getString(R.string.networkerror),
					cx.getString(R.string.networkerrord),
					cx.getString(R.string.yes));

		}
		//
		// if (InternetConnection(1) == true || InternetConnection(0) == true) {
		// toast(mod.getString(R.string.error_des), false);
		// }else{
		// toast(mod.getString(R.string.networkerrord), false);
		// }
	}
	
	

	public static String[] NameBuilder(String name_1, String name_2) {
		return NameBuilder(mod.getString(R.string.lang), name_1, name_2);
	}

	// Make name
	public static String[] NameBuilder(String lang, String name_1, String name_2) {
		String[] name = new String[2];
		if (lang.matches("ko")) {
			name[0] = name_1;
			name[1] = name_2;
		} else {
			name[0] = name_2;
			name[1] = name_1;
		}
		return name;
	}

	// Make name value
	public static String NameMaker(String lang, String name_1, String name_2) {
		String name;
		if (lang.matches("ko")) {
			name = name_1 + name_2;
		} else {
			name = name_2 + " " + name_1;
		}
		return name;
	}

	public static int[] getIMGSize(ContentResolver cr, Uri uri)
			throws FileNotFoundException {
		Bitmap bm;
		int[] size = new int[2];

		InputStream in = cr.openInputStream(uri);
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inPurgeable = true;
		option.inJustDecodeBounds = true;
		// BitmapFactory.decodeStream(in, null, option);
		bm = BitmapFactory.decodeStream(in, null, option);
		// 1is height
		size[1] = option.outHeight;
		size[0] = option.outWidth;

		return size;

	}

	public static InputStream editIMGSize(ContentResolver cr, Uri uri)
			throws FileNotFoundException {
		int[] size = new int[2];
		Bitmap bm;
		InputStream in = cr.openInputStream(uri);
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inPurgeable = true;
		option.inJustDecodeBounds = true;
		// BitmapFactory.decodeStream(in, null, option);
		bm = BitmapFactory.decodeStream(in, null, option);
		// 1is height
		size[1] = option.outHeight;
		size[0] = option.outWidth;
		//
		// if(editsize == true){
		if (size[1] > 4000)
			option.inSampleSize = 2;
		if (size[1] > 1024)
			option.inSampleSize = 4;
		// }
		//
		return in;

	}

	// public static void makeThumbNail(Bitmap src,String dest){
	// src = UIUtil.rotate(getApplicationContext(), b, path, uriId);
	//
	// // 1is height
	// size[1] = option.outHeight;
	// size[0] = option.outWidth;
	// //
	// // if(editsize == true){
	//
	// option.inSampleSize = 4;
	// // }
	// //
	// return in;
	//
	// }

	public static Bitmap filetobitmap(String path) {
		Bitmap bitmap = null;
		File f = new File(path);

		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null,
					options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	// Bitmap to File
	public static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath,
			String filename) {

		File file = null;
		try {
			file = new File(strFilePath);
		} catch (Exception e) {
		}

		// If no folders
		if (!file.exists()) {
			file.mkdirs();
			// Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
		}

		File fileCacheItem = new File(strFilePath + filename);
		OutputStream out = null;

		try {
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);
			bitmap.compress(CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getValue(String value) {
		value = value.replace("<etr>", "\n");
		return value;
	}

	public static String setValue(String value) {
		value = value.replace("\n", "<etr>");
		return value;
	}

	// Bitmap to File
	public static void createThumbnail(Bitmap bitmap, String strFilePath,
			String filename) {

		File file = new File(strFilePath);

		// If no folders
		if (!file.exists()) {
			file.mkdirs();
			// Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
		}

		File fileCacheItem = new File(strFilePath + filename);
		OutputStream out = null;

		try {

			int height = bitmap.getHeight();
			int width = bitmap.getWidth();

			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);
			bitmap = Bitmap.createScaledBitmap(bitmap, 160, height
					/ (width / 160), true);
			bitmap.compress(CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String arraylistToString(ArrayList<String> array,
			String delimiter) {
		// String str;
		StringBuilder sb = new StringBuilder();
		for (String str : array) {
			sb.append(str).append(delimiter); // separating contents using semi
												// colon
		}
		return sb.toString();

	}

	public static String arrayToString(String[] array, String delimiter) {
		StringBuilder arTostr = new StringBuilder();
		if (array.length > 0) {
			arTostr.append(array[0]);
			for (int i = 1; i < array.length; i++) {
				arTostr.append(delimiter);
				arTostr.append(array[i]);
			}
		}
		return arTostr.toString();
	}

	// Bitmap to uri
	public static Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public static boolean UpdateMemberFileCache(String user_srl,
			String new_update, String profile_pic) {
		String local_path = mod.getCacheDir().toString() + "/member/";
		if (Global.UpdateFileCache(new_update,
				Global.getUser(user_srl, "profile_update"),
				mod.getString(R.string.server_path) + "files/profile/"
						+ user_srl + ".jpg", local_path, user_srl + ".jpg")
				&& profile_pic.matches("Y"))
			return true;

		return false;
	}

	public static boolean UpdateFileCache(String new_update, String updatetime,
			String filepath, String filesavepath, String filename) {
		File files = new File(filesavepath + filename);
		if (Long.parseLong(new_update) > Long.parseLong(updatetime)
				|| files.exists() == false) {
			Log.i("true", "Update need");
			return true;
		}
		return false;
	}

	public static boolean CheckFileState(String path) {
		File files = new File(path);

		return files.exists();

	}

	// public static void DownloadImageToFile(String filepath,
	// String filesavepath, String filename) {
	// new ImageDownloader(mod, filepath, mHandler, 1);
	// Globalvariable.filesavepath = filesavepath;
	// Globalvariable.filename = filename;
	// }

	// public static String getDate(long timeStamp){
	// Locale systemLocale = mod.getResources().getConfiguration().locale;
	// SimpleDateFormat objFormatter = new SimpleDateFormat("dd-MM-yyyy");
	// objFormatter.setTimeZone(TimeZone.getTimeZone(systemLocale));
	//
	// Calendar objCalendar =
	// Calendar.getInstance(TimeZone.getTimeZone(systemLocale));
	// objCalendar.setTimeInMillis(timeStamp*1000);//edit
	// String result = objFormatter.format(objCalendar.getTime());
	// objCalendar.clear();
	// return result;
	// }

	public static long getCurrentTimeStamp() {

		return System.currentTimeMillis() / 1000;
	}

	public static String getDate(String timeStamp) {

		return getDate(Long.parseLong(timeStamp), mod.getString(R.string.date));
	}

	private static class TIME_MAXIMUM {
		public static final int SEC = 60;
		public static final int MIN = 60;
		public static final int HOUR = 24;
		public static final int DAY = 30;
		public static final int MONTH = 12;
	}

	public static String formatTimeString(long timestamp) {

		long curTime = System.currentTimeMillis() / 1000;
		long regTime = timestamp;
		long diffTime = (curTime - regTime);

		String msg = null;
		if (diffTime < TIME_MAXIMUM.SEC) {
			// sec
			msg = mod.getString(R.string.a_moment_ago);
		} else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
			// min

			int plural = diffTime > 1 ? 2 : 1;
			msg = String.format(
					mod.getResources().getQuantityString(R.plurals.minute,
							plural), diffTime);
		} else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
			// hour
			int plural = diffTime > 1 ? 2 : 1;
			msg = String.format(
					mod.getResources()
							.getQuantityString(R.plurals.hour, plural),
					diffTime);

		} else {
			msg = getDate(timestamp, mod.getString(R.string.date));
		}

		return msg;
	}

	public static String getDate(long timeStamp, String DateFormat) {
		java.text.DateFormat objFormatter = new SimpleDateFormat(DateFormat);
		objFormatter.setTimeZone(TimeZone.getDefault());

		Calendar objCalendar = Calendar.getInstance(TimeZone.getDefault());
		objCalendar.setTimeInMillis(timeStamp * 1000);// edit
		String result = objFormatter.format(objCalendar.getTime());
		objCalendar.clear();
		return result;
	}

	// public static String getDate(String time){
	// Date date = new Date(Long.parseLong(time));
	// SimpleDateFormat dateFormat =
	// new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
	// return dateFormat.format(date);
	// }
	//
	// public static String getDate(long timeStamp){
	//
	// try{
	// java.text.DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	// Date netDate = (new Date(timeStamp));
	// return sdf.format(netDate);
	// }
	// catch(Exception ex){
	// return "xx";
	// }
	// }

	// protected static Handler mHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// String local_path = mod.getCacheDir().toString() + "/member/";
	// String member_srl = "0";
	// if (msg.what == 1) {
	// // SaveBitmapToFileCache((Bitmap) msg.obj, );
	// Global.SaveBitmapToFileCache((Bitmap) msg.obj,
	// Globalvariable.filesavepath, Globalvariable.filename);
	// createThumbnail((Bitmap) msg.obj, local_path
	// + "thumbnail/", member_srl + ".jpg");
	// Globalvariable.filesavepath = null;
	// Globalvariable.filename = null;
	// }
	//
	// if (msg.what == 2) {
	//
	// try {
	//
	//
	// String[] array = msg.obj.toString().split("/LINE/.");
	// Global.dumpArray(array);
	// member_srl = array[0];
	// String profile_update = array[1];
	// String profile_pic = array[2];
	//
	//
	// if (Global.UpdateFileCache(profile_update,
	// Global.getUser(member_srl, "0"),
	// mod.getString(R.string.server_path) + "files/profile/"
	// + member_srl + ".jpg", local_path,
	// member_srl + ".jpg")
	// && profile_pic.matches("Y")) {
	// SaveUserSetting(member_srl, profile_update);
	// DownloadImageToFile(mod.getString(R.string.server_path) +
	// "files/profile/"
	// + member_srl + ".jpg",
	// local_path,
	// member_srl + ".jpg");
	//
	// }
	//
	//
	// } catch (Exception e) {
	// }
	// }
	//
	// }
	// };

	// public static int dp(int value) {
	// float density = mod.getResources().getDisplayMetrics().density;
	// return (int)(density * value);
	// }
	//
	// public static Bitmap loadBitmap(String path, float maxWidth, float
	// maxHeight) {
	// BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	// bmOptions.inJustDecodeBounds = true;
	// BitmapFactory.decodeFile(path, bmOptions);
	// float photoW = bmOptions.outWidth;
	// float photoH = bmOptions.outHeight;
	// float scaleFactor = Math.max(photoW / maxWidth, photoH / maxHeight);
	// if (scaleFactor < 1) {
	// scaleFactor = 1;
	// }
	// bmOptions.inJustDecodeBounds = false;
	// bmOptions.inSampleSize = (int)scaleFactor;
	//
	// ExifInterface exif;
	// Matrix matrix = null;
	// try {
	// exif = new ExifInterface(path);
	// int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
	// matrix = new Matrix();
	// switch (orientation) {
	// case ExifInterface.ORIENTATION_ROTATE_90:
	// matrix.postRotate(90);
	// break;
	// case ExifInterface.ORIENTATION_ROTATE_180:
	// matrix.postRotate(180);
	// break;
	// case ExifInterface.ORIENTATION_ROTATE_270:
	// matrix.postRotate(270);
	// break;
	// }
	// } catch (Exception e) {
	// // FileLog.e("tmessages", e);
	// }
	//
	// Bitmap b;
	// try {
	// b = BitmapFactory.decodeFile(path, bmOptions);
	// if (b != null && matrix != null) {
	// b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix,
	// true);
	// }
	// } catch (Exception e) {
	// // FileLoader.Instance.memCache.evictAll();
	// b = BitmapFactory.decodeFile(path, bmOptions);
	// if (b != null && matrix != null) {
	// b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix,
	// true);
	// }
	// }
	//
	// return b;
	// }

	public static void addMediaToGallery(String fromPath) {
		if (fromPath == null) {
			return;
		}
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(fromPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		mod.sendBroadcast(mediaScanIntent);
	}

	public static void recycleBitmap(ImageView iv) {
		try {
			Drawable d = iv.getDrawable();
			if (d instanceof BitmapDrawable) {
				Bitmap b = ((BitmapDrawable) d).getBitmap();
				b.recycle();
			} // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.

			d.setCallback(null);
		} catch (Exception e) {
		}
	}

	// public static String getRealPathURI(Uri uriThatYouCurrentlyHave){
	// // Will return "image:x*"
	// String wholeID =
	// DocumentsContract.getDocumentId(uriThatYouCurrentlyHave);
	//
	// // Split at colon, use second item in the array
	// String id = wholeID.split()[1];
	//
	// String[] column = { MediaStore.Images.Media.DATA };
	//
	// // where id is equal to
	// String sel = MediaStore.Images.Media._ID + "=?";
	//
	// Cursor cursor = mod.getContentResolver().
	// query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	// column, sel, new String[]{ id }, null);
	//
	// String filePath = "";
	//
	// int columnIndex = cursor.getColumnIndex(column[0]);
	//
	// if (cursor.moveToFirst()) {
	// filePath = cursor.getString(columnIndex);
	// }
	//
	// cursor.close();
	//
	// return filePath;
	// }
	//
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	public static String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			Log.i("test", contentUri.toString());
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	@SuppressWarnings("null")
	public static Bitmap UriToBitmapCompress(Uri image_uri) {
		Bitmap bm = null;
		ContentResolver cr = mod.getContentResolver();
		// image size
		int[] imagesize;
		try {
			imagesize = getIMGSize(cr, image_uri);

			InputStream in = cr.openInputStream(image_uri);
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inPurgeable = true;
			option.inDither = true;
			if (android.os.Build.VERSION.SDK_INT < 14) {
				if (imagesize[1] > 1024)
					option.inSampleSize = Integer.parseInt(mod
							.getString(R.string.pic_size_devide)) * 1;
			}

			if (imagesize[1] > 2048)
				option.inSampleSize = Integer.parseInt(mod
						.getString(R.string.pic_size_devide)) * 2;
			if (imagesize[1] > 4096)
				option.inSampleSize = Integer.parseInt(mod
						.getString(R.string.pic_size_devide)) * 4;
			if (imagesize[1] > 8192)
				option.inSampleSize = Integer.parseInt(mod
						.getString(R.string.pic_size_devide)) * 8;
			// BitmapFactory.decodeStream(in, null, option);
			bm = BitmapFactory.decodeStream(in, null, option);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}

	// Make temporary file
	public static File createTemporaryFile(String part, String ext)
			throws Exception {
		File tempDir = mod.getExternalCacheDir();
		tempDir = new File(tempDir.getAbsolutePath()); // added
														// missing
														// ")"
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}
		return File.createTempFile(part, ext, tempDir);
	}

	// Get image
	public static Bitmap grabImage(Uri mImageUri) {
		mod.getContentResolver().notifyChange(mImageUri, null);
		ContentResolver cr = mod.getContentResolver();
		Bitmap bitmap = null;
		try {
			bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,
					mImageUri);
			// imageView.setImageBitmap(bitmap);
		} catch (Exception e) {
			Toast.makeText(mod, "Failed to load", Toast.LENGTH_SHORT).show();
			// Log.d(TAG, "Failed to load", e);
		}

		return bitmap;
	}

	// This is prevent too many server requests
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

	public static boolean getUpdatePossible(String user_srl) {
		boolean possible = false;
		if (getCurrentTimeStamp()
				- Integer.parseInt(Global.getUser(user_srl,
						"profile_update_thumbnail")) > 5000
				|| Global.CheckFileState(mod.getCacheDir().toString()
						+ "/member/thumbnail/" + user_srl + ".jpg") == false
				&& Global.getUser(user_srl, "profile_pic").matches("Y"))
			possible = true;
		return possible;
	}

	public static String getSetting(String setting, String default_value) {
		SharedPreferences prefs = mod.getSharedPreferences("setting",
				mod.MODE_PRIVATE);
		return prefs.getString(setting, default_value);
	}

	public static String getUser(String user_srl, String value) {
		// Log.i("db", "helloget");
		// DB Create and Open
		DbOpenHelper mDbOpenHelper = new DbOpenHelper(mod);
		mDbOpenHelper.open();
		String pu;
		Cursor csr = mDbOpenHelper.getUserInfo(user_srl);
		// Log.i("DB", csr.getCount() + "count");

		if (csr.getCount() == 0) {
			pu = "0";
		} else {
			pu = csr.getString(csr.getColumnIndex(value));
		}

		// if(pu == null){

		// }

		csr.close();
		// Log.i("DB", pu + "ddddd");

		mDbOpenHelper.close();

		return pu;
	}

	public static void SaveUserSetting(String user, String profile_update,
			String profile_update_thumbnail, String profile_pic) {

		// 설정 값 저장
		// Setting Editor
		DbOpenHelper mDbOpenHelper = new DbOpenHelper(mod);
		mDbOpenHelper.open();

		Cursor csr = mDbOpenHelper.getUserInfo(user);
		// Log.i("DB", csr.getCount() + "count");
		if (csr.getCount() == 0) {
			mDbOpenHelper.insertColumn(user,
					Long.toString(getCurrentTimeStamp()),
					Long.toString(getCurrentTimeStamp()), profile_pic);
			// Log.i("DB", "added");
		} else {
			if (profile_update == null) {
				mDbOpenHelper.updateProfileUpdateThumbnail(user,
						profile_update_thumbnail, profile_pic);
			}
			if (profile_update_thumbnail == null) {
				mDbOpenHelper.updateProfileUpdate(user, profile_update,
						profile_pic);
			}
			// Log.i("DB", "update");
		}

		// Log.i("db", profile_update);

		csr.close();
		mDbOpenHelper.close();

	}

	public static String getPhoneNumber(boolean getphonenumber) {
		// Get Country number and phone number
		// get Phone number
		String result = null;
		try {
			TelephonyManager systemService = (TelephonyManager) mod
					.getSystemService(Context.TELEPHONY_SERVICE);
			String Number = systemService.getLine1Number(); // 폰번호를 가져오는
															// 겁니다..
			String PhoneNumber = Number.substring(Number.length() - 10,
					Number.length());

			String country_code = Number.substring(0, Number.length()
					- PhoneNumber.length());

			if (getphonenumber == true) {
				result = PhoneNumber;
			} else {
				country_code = country_code.replace("+", "");
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

	public static String getCountryValue() {
		String countryCode;

		TelephonyManager systemService = (TelephonyManager) mod
				.getSystemService(Context.TELEPHONY_SERVICE);
		countryCode = systemService.getSimCountryIso();

		if (countryCode.matches(""))
			countryCode = mod.getString(R.string.default_country);

		// Log.i("Country", countryCode + "df");
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
		// if (!FileUtil.exists(imgFilePath)) {
		// throw new FileNotFoundException("background-image file not found : "
		// + imgFilePath);
		// }

		// 폰의 화면 사이즈를 구한다.
		// Display display = ((WindowManager) context
		// .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		// int displayWidth = display.getWidth();
		// int displayHeight = display.getHeight();
		//
		// 읽어들일 이미지의 사이즈를 구한다.
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgFilePath, options);

		// 화면 사이즈에 가장 근접하는 이미지의 리스케일 사이즈를 구한다.
		// 리스케일의 사이즈는 짝수로 지정한다. (이미지 손실을 최소화하기 위함.)
		// float widthScale = options.outWidth / displayWidth;
		// float heightScale = options.outHeight / displayHeight;
		// float scale = widthScale > heightScale ? widthScale : heightScale;
		//
		// if(scale >= 8) {
		// options.inSampleSize = 8;
		// } else if(scale >= 6) {
		// options.inSampleSize = 6;
		// } else if(scale >= 4) {
		// options.inSampleSize = 4;
		// } else if(scale >= 2) {
		// options.inSampleSize = 2;
		// } else {
		// options.inSampleSize = 1;
		// }
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(imgFilePath, options);
	}

	// InternetConnection Error Message

	// public static Bitmap getCroppedBitmap(Bitmap bitmap) {
	// Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	// bitmap.getHeight(), Config.ARGB_8888);
	// Canvas canvas = new Canvas(output);
	//
	// final int color = 0xff424242;
	// final Paint paint = new Paint();
	// final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	//
	// paint.setAntiAlias(true);
	// canvas.drawARGB(0, 0, 0, 0);
	// paint.setColor(color);
	// // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	// canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	// bitmap.getWidth() / 2, paint);
	// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	// canvas.drawBitmap(bitmap, rect, rect, paint);
	// //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
	// //return _bmp;
	// return output;
	// }

	public static void Feedback(Context cx) {
		// System info
		String s = "Device info:";
		s += "\n OS Version: " + System.getProperty("os.version") + "("
				+ android.os.Build.VERSION.INCREMENTAL + ")";
		s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
		s += "\n Device: " + android.os.Build.DEVICE;
		s += "\n Model (and Product): " + android.os.Build.MODEL + " ("
				+ android.os.Build.PRODUCT + ")";

		// Send email
		Intent Email = new Intent(Intent.ACTION_SEND);
		Email.setType("text/email");
		Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "main@tarks.net" });
		Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
		Email.putExtra(Intent.EXTRA_TEXT, s);
		cx.startActivity(Intent.createChooser(Email,
				cx.getString(R.string.choose_email_app)));
	}

	public static void FeedbackWrite(Context cx) {
		// System info
		String s = "Device info:";
		s += "\n OS Version: " + System.getProperty("os.version") + "("
				+ android.os.Build.VERSION.INCREMENTAL + ")";
		s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
		s += "\n Device: " + android.os.Build.DEVICE;
		s += "\n Model (and Product): " + android.os.Build.MODEL + " ("
				+ android.os.Build.PRODUCT + ")";

		Intent intent1 = new Intent(cx, document_write.class);
		intent1.putExtra("page_srl", "13");
		intent1.putExtra("page_name", cx.getString(R.string.send_feedback));
		intent1.putExtra("doc_contents", s);
		cx.startActivity(intent1);
	}

}