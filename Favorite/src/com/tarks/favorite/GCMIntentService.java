package com.tarks.favorite;

import java.util.Iterator;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
//메세지의 고유 ID(?)정도로 생각하면 됩니다. 메세지의 중복수신을 막기 위해 랜덤값을 지정합니다

public class GCMIntentService extends GCMBaseIntentService {
	private static final String tag = "GCMIntentService";
		public static final String SEND_ID = "743824910564";
	//알림 정의
	 public int noti_id = 0; 
	 String value;
	public GCMIntentService() {
	//	this(SEND_ID);
	}

	public GCMIntentService(String senderId) {
		super(senderId);
	}

	 @Override
	  protected String[] getSenderIds(Context context) {
	     String[] ids = new String[1];
	     ids[0] = SEND_ID;
	     return ids;
	  }
	
	//get Message
	@Override
	protected void onMessage(Context context, Intent intent) {
		Bundle b = intent.getExtras();
		Iterator<String> iterator = b.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = b.get(key).toString();
			//Message Filter
			if(value.matches("do_not_collapse||743824910564")){}else{
			Intent intent1 = new Intent(GCMIntentService.this, MainActivity.class);
//Notice Message
			
			//Notice Message
			//Noti id
			Random r = new Random();
			int i1=r.nextInt(1000-1) + 1;
			
			CNotification.addNotification(GCMIntentService.this, intent1, i1,
					R.drawable.ic_launcher_simple, value,
					value, key);
			}
		}
			
//Effect
			
			
			Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(),RingtoneManager.TYPE_NOTIFICATION);
			Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
			ringtone.play();
			
			Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibe.vibrate(1000);  
	}
		
	

	@Override
	protected void onError(Context context, String errorId) {
		Log.d(tag, "onError. errorId : " + errorId);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Log.d(tag, "onRegistered. regId : " + regId);
		//Setting
		  //Setting Editor
			SharedPreferences edit = getSharedPreferences("setting",
					MODE_PRIVATE);
			SharedPreferences.Editor editor = edit.edit();
			editor.putString("regId", regId);															
			editor.commit();
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.d(tag, "onUnregistered. regId : " + regId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.d(tag, "onRecoverableError. errorId : " + errorId);
		return super.onRecoverableError(context, errorId);
	}
}