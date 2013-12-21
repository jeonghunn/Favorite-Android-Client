package com.tarks.favorite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.math.BigInteger;
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
	

//	private static int rosp;
//	public static boolean isFirst = true;
//	public static boolean isFirstRuned = true;
//	public static boolean isFirstMain = true;
	
	private Global(){
		
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
	public static String GCMReg(){
		GCMRegistrar.checkDevice(ModApplication.getInstance());
		GCMRegistrar.checkManifest(ModApplication.getInstance());
		final String regId = GCMRegistrar.getRegistrationId(ModApplication.getInstance());
		if ("".equals(regId)) // 구글 가이드에는 regId.equals("")로 되어 있는데
								// Exception을 피하기 위해 수정
			GCMRegistrar.register(ModApplication.getInstance(), "743824910564");

	return regId;
		
	}
	
	public static void toast(String str,boolean length){
		//Log.i("ACCESS", "I can access to toast");
		Toast.makeText(ModApplication.getInstance(), str, (length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
	}
	
	public static void toast(String str){
		toast(str,false);
	}
	
	//Show Information alert
	public static void Infoalert(Context context, String title, String message, String button){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setPositiveButton(button, null)
				.setTitle(title);
		builder.show();
		
	}
	
	 // 배열을 화면에, 요소별로 알기 쉽게 출력
	  public static void dumpArray(String[] array) {
	    for (int i = 0; i < array.length; i++)
	      System.out.format("array[%d] = %s%n", i, array[i]);
	  }

	
	
	
}