//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite;

import java.util.Iterator;
import java.util.Set;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.tarks.favorite.core.global.Global;
import com.tarks.favorite.ui.page.ProfileActivity;
import com.tarks.favorite.ui.page.document_read;

//메세지의 고유 ID(?)정도로 생각하면 됩니다. 메세지의 중복수신을 막기 위해 랜덤값을 지정합니다

public class GCMIntentService extends GCMBaseIntentService {
	private static final String tag = "GCMIntentService";
	public static final String SEND_ID = "743824910564";
	// 알림 정의
	public int noti_id = 0;
	String value;

	public GCMIntentService() {
		// this(SEND_ID);
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

	// get Message
	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		   Bundle bundle = arg1.getExtras();
	        Set<String> setKey = bundle.keySet();
	        Iterator<String> iterKey = setKey.iterator();
	        String send_user_srl = null;
	        String kind = null;
            String number = null;
            String title = null;
            String des = null;
            String content = null;
	        while (iterKey.hasNext()){
	            String key = iterKey.next();
	            String value = bundle.getString(key);
	         
	         
	        //    Log.d("GCMIntentService", "onMessage. key = " + key + ", value = " + value);
	            
	            if(key.matches("collapse_key")){
		            String[] keyarray = value.split("//");
	                kind = keyarray[0];
	                number = keyarray[1];
		            }
	                //Documents
		            if(key.matches("data")){
		            	   String[] array = value.split("/LINE/.");
		            	   send_user_srl = array[0];
		            	   title = array[1];
		            	   content = array[2];
		            	   des = array[3];
		            	   
		            	   if(des.matches("new_document")) des = getString(R.string.notice_new_document);
		            	   if(des.matches("new_comment")) des = getString(R.string.notice_new_comment);
		            	   if(des.matches("added_to_favorite")) {des = getString(R.string.notice_added_to_favorite); content = getString(R.string.notice_added_to_favorite);}
		            	 
		              }
	        }

	        
	        	
	    		NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

	 
	    		Intent intent = null;
	   
	    		if(kind.matches("1")){
	    			intent = new Intent(GCMIntentService.this, document_read.class);
			      	   intent.putExtra("doc_srl", number);
	    		}
	    		if(kind.matches("2")){
	    			intent = new Intent(GCMIntentService.this, document_read.class);
			      	   intent.putExtra("doc_srl", number);
	    		}
	    		if(kind.matches("3")){
	    			intent = new Intent(GCMIntentService.this, ProfileActivity.class);
			      	   intent.putExtra("member_srl", send_user_srl);
	    		}
	    		// Because clicking the notification opens a new ("special") activity, there's
	    		// no need to create an artificial back stack.
		 
		      	PendingIntent resultPendingIntent =
		      	    PendingIntent.getActivity(
		      	    this,
		      	    0,
		      	  intent,
		      	    PendingIntent.FLAG_UPDATE_CURRENT
		      	);
		      	
		      	//User Explain
		      	Resources res = getResources();
		      	String user_explain = String.format(res.getString(R.string.user_explain), title); 
		      	
	    		// Constructs the Builder object.
		      	Notification builder =
	    		        new NotificationCompat.Builder(GCMIntentService.this)
		      	        .setTicker(user_explain + " " + des)
	    		        .setSmallIcon(R.drawable.ic_stat_favorite_white)
	    		        .setContentTitle(title)
	    		        .setContentText(des)
	    		        .setLargeIcon(Global.filetobitmap(getCacheDir().toString() + "/member/thumbnail/" + send_user_srl + ".jpg"))
                        .setDefaults(Notification.FLAG_AUTO_CANCEL)
	    		        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
	    		        	.setSubText(getString(R.string.app_name))
	    		        		    		.setContentIntent(resultPendingIntent)
	    		.setLights(0xff00ff00, 300, 200)
	    		        .setStyle(new NotificationCompat.BigTextStyle()
	    		                .bigText(Global.getValue(content))).build();
	    		


	    		builder.flags =  Notification.FLAG_AUTO_CANCEL;
	    	//	builder.ledARGB = Color.YELLOW;
	    			manager.notify(Integer.parseInt(send_user_srl) *10, builder);
	
//Effect
			
			
//			Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(),RingtoneManager.TYPE_NOTIFICATION);
//			Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
//			ringtone.play();
//			
//			Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//			vibe.vibrate(1000);  
	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.d(tag, "onError. errorId : " + errorId);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Log.d(tag, "onRegistered. regId : " + regId);
		// Setting
		// Setting Editor
		SharedPreferences edit = getSharedPreferences("setting", MODE_PRIVATE);
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