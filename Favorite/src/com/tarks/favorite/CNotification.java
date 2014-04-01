package com.tarks.favorite;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

public class CNotification {  
	  
  
    public static void addNotification(Context context, Intent intent,  
            int noticeId, int iconId, String ticker, String title,  
            String message) {  
    	
    	
    	
  
        Notification noti = new Notification(iconId, ticker,  
                System.currentTimeMillis());  
  
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        PendingIntent pintent = PendingIntent  
                .getActivity(context, 0, intent, 0);  
  

        noti.setLatestEventInfo(context, title, message, pintent);  
  
   
        NotificationManager nm = (NotificationManager) context  
                .getSystemService(Context.NOTIFICATION_SERVICE);  
        nm.cancel(noticeId);  
        nm.notify(noticeId, noti);  
        
        
    }  
      
    public static void removeNotification(Context context, int noticeId) {  
        NotificationManager nm = (NotificationManager) context  
                .getSystemService(Context.NOTIFICATION_SERVICE);  
        nm.cancel(noticeId);  
    }  
      
}  