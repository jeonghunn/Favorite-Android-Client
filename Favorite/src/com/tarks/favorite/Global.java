package com.tarks.favorite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gcm.GCMRegistrar;


public final class Global {
	

//	private static int rosp;
//	public static boolean isFirst = true;
//	public static boolean isFirstRuned = true;
//	public static boolean isFirstMain = true;
	
	private Global(){
		
	}
	
	public static void alert(String str,boolean length){
		
		
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
		Log.i("ACCESS", "I can access to toast");
		Toast.makeText(ModApplication.getInstance(), str, (length ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
	}
	
	public static void toast(String str){
		toast(str,false);
	}
	
	
	
}