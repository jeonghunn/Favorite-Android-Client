package com.tarks.favorite.core.connect;

/**
 * Created by JHRunning on 12/17/14.
 */


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tarks.favorite.R;
import com.tarks.favorite.core.global.Global;
import com.tarks.favorite.ui.page.PageActivity;
import com.tarks.favorite.ui.page.document_read;

import java.util.Iterator;
import java.util.Set;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM";

    @Override
    protected void onHandleIntent(Intent intent) {
//        Bundle extras = intent.getExtras();
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
//        // The getMessageType() intent parameter must be the intent you received
//        // in your BroadcastReceiver.
//        String messageType = gcm.getMessageType(intent);
//
//        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
//            /*
//             * Filter messages based on message type. Since it is likely that GCM will be
//             * extended in the future with new message types, just ignore any message types you're
//             * not interested in, or that you don't recognize.
//             */
//            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                sendNotification("Send error: " + extras.toString());
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendNotification("Deleted messages on server: " + extras.toString());
//                // If it's a regular GCM message, do some work.
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//                // This loop represents the service doing some work.
//                for (int i = 0; i < 5; i++) {
//                    Log.i(TAG, "Working... " + (i + 1)
//                            + "/5 @ " + SystemClock.elapsedRealtime());
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//                }
//                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
//                // Post notification of received message.
//                sendNotification("Received: " + extras.toString());
//                Log.i(TAG, "Received: " + extras.toString());
//            }
//        }
//        // Release the wake lock provided by the WakefulBroadcastReceiver.
//        GcmBroadcastReceiver.completeWakefulIntent(intent);


        Bundle bundle = intent.getExtras();
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



        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Intent intenta = null;

        if(kind.matches("1")){
            intenta = new Intent(GcmIntentService.this, document_read.class);
            intenta.putExtra("doc_srl", number);
        }
        if(kind.matches("2")){
            intenta = new Intent(GcmIntentService.this, document_read.class);
            intenta.putExtra("doc_srl", number);
        }
        if(kind.matches("3")){
            intenta = new Intent(GcmIntentService.this, PageActivity.class);
            intenta.putExtra("member_srl", send_user_srl);
        }
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intenta,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        //User Explain
        Resources res = getResources();
        String user_explain = String.format(res.getString(R.string.user_explain), title);

        // Constructs the Builder object.
        Notification builder =
                new NotificationCompat.Builder(GcmIntentService.this)
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

    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
//        mNotificationManager = (NotificationManager)
//                this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, main.class), 0);
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setContentTitle("GCM Notification")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(msg))
//                        .setContentText(msg);
//
//        mBuilder.setContentIntent(contentIntent);
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}