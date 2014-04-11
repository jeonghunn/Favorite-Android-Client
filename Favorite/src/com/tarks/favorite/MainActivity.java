package com.tarks.favorite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.google.android.gcm.GCMRegistrar;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.connect.ImageDownloader;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.global.Globalvariable;
import com.tarks.favorite.start.welcome;
import com.tarks.favorite.user.db.DbOpenHelper;

public class MainActivity extends SherlockActivity {
	// 통신 스트링

	String sVersion; // 웹페이지에서 가져온 버젼이 저장됨
	String myId, myPWord, myTitle, mySubject, myResult;
	String infoResult;
	String REGid;
	// Notice Result
	String NoticeResult;
	String user_srl;
	// Allow Load
	boolean load = true;
	boolean updated = false;

	// Connect timeout boolean
	boolean ConnectTimeout = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception ex) {
	        // Ignore
	    }

		


		
		// 자신의 신분 설정값을 불러옵니다.
		SharedPreferences prefs = getSharedPreferences("setting", MODE_PRIVATE);
		String frist_use = prefs.getString("frist_use_app", "true");

		ConnectTimeout();

		// Check Network Connection
		try {

			// Log.d("==============", regId);
			REGid = Global.GCMReg();
			// Let's Call Information
			// Connection Start
			// 처음 사용자가 아닐시에
			if (frist_use.matches("true")) {
				// 이동한다. 환영합니다로.
				BreakTimeout();
				Intent intent = new Intent(MainActivity.this, welcome.class);
				startActivity(intent);
				finish();
			} else {
				// First Notice Download
				InfoDown();

			}

		} catch (Exception e) {
			// If No Network Connection
			// 로딩 화면은 종료하라.
			finish();
			// 이동한다. 메인으로
			Intent intent = new Intent(MainActivity.this, main.class);
			startActivity(intent);

		}

	}

	public void InfoDown() {

		// get user_srl
		// 설정 값 불러오기
		// SharedPreferences prefs = getSharedPreferences("setting",
		// MODE_PRIVATE);
		user_srl = Global.getSetting("user_srl", "");
		String user_srl_auth = Global.getSetting("user_srl_auth", "");

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("lang");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("member_info");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add(getString(R.string.lang));
		Paramvalue.add(user_srl);
		Paramvalue.add(user_srl_auth);
		Paramvalue
				.add("tarks_account//name_1//name_2//permission//profile_pic//profile_update//reg_id//key//like_me//favorite");

		new AsyncHttpTask(this, getString(R.string.server_path) + "load.php",
				mHandler, Paramname, Paramvalue, null, 1,0);

	}

	public void ConnectTimeout() {
		new Thread(new Runnable() {
			public void run() {
				int time = 0;
				while (true) {

					try {
						// 15 seconds
						if (time != 14) {
							Thread.sleep(1000);
							time = time + 1;
							Log.i("Timeout", time + "load");
							if (ConnectTimeout == false) {
								break;
							}
						} else {
							if (this != null) {
								Message msg = mHandler.obtainMessage();
								msg.arg1 = 0;
								mHandler.sendMessage(msg);
							}
							break;
						}
						// break;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	public void BreakTimeout() {
		ConnectTimeout = false;
	}

	public void PermissionError() {
		// Alert
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage(getString(R.string.permission_denied)).setTitle(
				getString(R.string.error));
		builder.setPositiveButton(getString(R.string.check_service_status),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Global.Feedback(MainActivity.this);

					}
				});
		builder.setNegativeButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						finish();

					}
				});
		builder.show();

	}

	public void IPError() {
		// Alert
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage(getString(R.string.ip_error_des)).setTitle(
				getString(R.string.error));
		builder.setPositiveButton(getString(R.string.enquire),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Global.Feedback(MainActivity.this);
					}
				});
		builder.setNegativeButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						finish();

					}
				});
		builder.show();
	}

	public void ConnectionError() {
		// If No Network Connection
		// Check Internet Connection

		// Check Network Connection
		if (Global.InternetConnection(1) == true
				|| Global.InternetConnection(0) == true) {
			// Show Alert
			AlertDialog.Builder alert = new AlertDialog.Builder(
					MainActivity.this);
			alert.setTitle(getString(R.string.error));
			alert.setMessage(getString(R.string.server_connection_error_des));
			alert.setIcon(R.drawable.ic_launcher);
			alert.setPositiveButton(getString(R.string.check_service_status),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							Uri uri = Uri
									.parse("https://sites.google.com/site/tarksservicesstatus/");
							Intent it = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(it);
						}
					});
			alert.setNegativeButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							finish();

						}
					});
			alert.show();

		} else {
			// 로딩 화면은 종료하라.
			Toast.makeText(MainActivity.this,
					getString(R.string.networkerrord), 0).show();
			finish();
			// 이동한다. 메인으로
			Intent intent = new Intent(MainActivity.this, main.class);
			startActivity(intent);
		}
	}

	public void StartApp() {
		try {
			// Global.toast(infoResult);
			// Check mySql Error
			if (infoResult.matches("db_error")) {
				load = false;
				ConnectionError();
			}
			// Check IP Error
			if (infoResult.matches("ip_error")) {
				load = false;
				IPError();
			}

			if (infoResult.startsWith("/LINE/.")) {
				// Account Changed
				// Alert
				AlertDialog.Builder alert = new AlertDialog.Builder(
						MainActivity.this);
				alert.setTitle(getString(R.string.error));
				alert.setMessage(getString(R.string.reg_id_error));
				alert.setIcon(R.drawable.ic_launcher);
				alert.setPositiveButton(getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// Clear Old Settings
								getSharedPreferences("setting", 0).edit()
										.clear().commit();
								getSharedPreferences("temp", 0).edit().clear()
										.commit();

								// New Start
								// 로딩 화면은 종료하라.
								finish();
								// 이동한다. 메인으로
								Intent intent = new Intent(MainActivity.this,
										MainActivity.class);
								startActivity(intent);

							}
						});
				alert.show();
			} else {

				// Check Permission Error
				if (infoResult.matches("permission_error")) {
					load = false;
					PermissionError();
				}
				// Log.i("Result value",infoResult);
				String[] array = infoResult.split("/LINE/.");
				// Global.dumpArray(array);

				String tarks_account = array[0];
				String name_1 = array[1];
				String name_2 = array[2];
				String permission = array[3];
				String profile_pic = array[4];
				String profile_update = array[5];
				String reg_id = array[6];
				String key = array[7];
				String like_me = array[8];
				String favorite = array[9];

				// if(!profile_update.matches(Global.getSetting("profile_update",
				// "") || getCacheDir().toString(), "/profile.jpg")){
				// Global.DownloadImageToFile(getString(R.string.server_path)
				// + "files/profile/" + user_srl ,
				// getCacheDir().toString(), "/profile.jpg");
				// }

				if (Global.UpdateMemberFileCache(user_srl, profile_update, profile_pic)) {
					Global.SaveUserSetting(user_srl, profile_update, profile_pic);
					//Download image
					new ImageDownloader(this, getString(R.string.server_path)
							+ "files/profile/" + user_srl + ".jpg", mHandler, 2, 0);
					// Log.i("test", "Let s profile image download");

				}
				
			

				// 설정 값 저장
				// Setting Editor
				SharedPreferences edit = getSharedPreferences("setting",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = edit.edit();
				editor.putString("name_1", name_1);
				editor.putString("name_2", name_2);
				editor.putString("permission", permission);
				editor.putString("profile_pic", profile_pic);
				editor.putString("profile_update", profile_update);
				editor.putString("key", key);
				editor.putString("like_me", like_me);
				editor.putString("favorite", favorite);
				
				
				//Version Check
				PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				String versionNumber = String.valueOf(pinfo.versionCode);
				//Check Version
				if(!Global.getSetting("verCode", "0").matches(versionNumber)){
					updated = true;
					editor.putString("verCode", versionNumber);
					
				}
				
				//Commit
				editor.commit();

				// Kind of Load Stop


				// Reg ID가 기존과 다를 때
				if (!REGid.startsWith(reg_id) && !reg_id.matches("null") && updated == false ) {
			
					load = false;
					// Alert
					AlertDialog.Builder alert = new AlertDialog.Builder(
							MainActivity.this);
					alert.setTitle(getString(R.string.error));
					alert.setMessage(getString(R.string.reg_id_error));
					alert.setIcon(R.drawable.ic_launcher);
					alert.setPositiveButton(getString(R.string.yes),
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// Clear Old Settings
									getSharedPreferences("setting", 0).edit()
											.clear().commit();
									getSharedPreferences("temp", 0).edit()
											.clear().commit();

									// New Start
									// 로딩 화면은 종료하라.
									finish();
									// 이동한다. 메인으로
									Intent intent = new Intent(
											MainActivity.this,
											MainActivity.class);
									startActivity(intent);

								}
							});
					alert.show();
				}

				// Permission Denied
				// if (permission.matches("4")) {
				// load = false;
				// PermissionError();
				// }
				// 제한사항이 없을 경우
				
				if(load) Log.i("Start", "True");
				if (load == true) {
					// 로딩 화면은 종료하라.

					finish();
					// 이동한다. 메인으로
					Intent intent = new Intent(MainActivity.this, main.class);
					startActivity(intent);

				}

			}
		} catch (Exception e) {
			Global.ConnectionError(this);
		}

	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// IF Sucessfull no timeout
			if (msg.what != 0) {
				BreakTimeout();
			}

			if (msg.what == -1) {
				ConnectionError();
			}

			if (msg.what == 0) {
				ConnectionError();
			}

			if (msg.what == 1) {
				try{
				infoResult = msg.obj.toString();
				Log.i("InfoResult", infoResult + "null");
				StartApp();
				} catch (Exception e){}

			}

			if (msg.what == 2) {
				Global.SaveBitmapToFileCache((Bitmap) msg.obj, getCacheDir().toString() , "/member/" + user_srl + ".jpg");
				Global.createThumbnail((Bitmap) msg.obj, getCacheDir().toString() , "/member/thumbnail/" + user_srl + ".jpg");

			}

		}
	};

}