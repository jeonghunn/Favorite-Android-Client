//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.global;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import com.tarks.favorite.ModApplication;
import android.app.Application;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;

public class Filedw {
	
	public static String referer = "http://httphttp.http/http";

	@SuppressWarnings("deprecation")
	public static void startdownload(String url, String userAgent,
			String contentDisposition, String mimetype, long contentLength) {
		Application app = ModApplication.getInstance();
		MimeTypeMap mtm = MimeTypeMap.getSingleton();
		final DownloadManager downloadManager = (DownloadManager) app.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri downloadUri = Uri.parse(url);

		
		String fileName = downloadUri.getLastPathSegment();
		String mimeType = null;
		Log.i("Tarks",url);
		int pos = 0;
		if(contentDisposition != null){
			if ((pos = contentDisposition.toLowerCase().lastIndexOf("filename=")) >= 0) {
				fileName = contentDisposition.substring(pos + 9);
				pos = fileName.lastIndexOf(";");
				if (pos > 0) {
					fileName = fileName.substring(0, pos - 1);
				}
			}
			fileName = decodeURL(fileName);
		}else{
			if(fileName == null){
				String[] ets = {".png",".jpg",".gif",".jpeg",".bmp",".tiff"};
				String fileex = downloadUri.toString();
				String fn = "";
				int i=0;
				for(i=0;i<ets.length;i+=1){
					if(fileex.contains(ets[i])){
						for(int j=fileex.indexOf(ets[i])+ets[i].length()-1;j>=0;j-=1){
							if(fileex.charAt(j) == '/'){
								break;
							}else{
								fn += new Character(fileex.charAt(j)).toString();
							}
						}
						break;
					}
				}
				if(fn.length() > 3){
					fileName = new StringBuilder(fn).reverse().toString();
				}
				if(fileName == null){
					fileName = "file.extensions";
				}
				if(mimetype == null || mimetype.equalsIgnoreCase("")){
					mimeType = mtm.getMimeTypeFromExtension(ets[i]);
				}else{
					mimeType = mimetype;
				}
			}
		}
		if(fileName.startsWith("\"")){
			fileName = fileName.substring(1);
		}
		if(fileName.endsWith("\"")){
			fileName = fileName.substring(0, fileName.length() - 1);
		}
		String dlPath = Environment.DIRECTORY_DOWNLOADS; // downloadPath
		File downloadFURL;
		 if (Environment.getExternalStorageState() == null) {
             //create new file directory object
			 downloadFURL = new File(Environment.getDataDirectory() + "/" + dlPath +"/");
		 }else{
			 downloadFURL = new File(Environment.getExternalStorageDirectory() + "/" + dlPath +"/");
		 }
		
		
		File exists;
		int i=0;
		
		String flEx; // fileExtension
		String flRn; // fileRealname
		System.out.println(fileName);
		if(fileName.contains("tar.")){
			flEx = fileName.substring(fileName.lastIndexOf(".") - 3, fileName.length()).toLowerCase();
		}else{
			flEx = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
		}
		/*
		String[] sb = fileName.split(".");
		i = sb.length;
		System.out.println(sb.length);
		if(i == 1){
			flEx = "";
		}else{
			if(i >= 3){
				if(sb[i-2].equalsIgnoreCase("tar")){
					flEx = sb[i-2] + sb[i-1];
				}else{
					flEx = sb[i-1];
				}
			}else{
				flEx = sb[i-1];
			}
		}
		i = 0;
		flEx = flEx.toLowerCase();
		*/
		if(fileName.contains("tar.")){
			flRn = fileName.substring(0,fileName.lastIndexOf(".")-3);
		}else{
			flRn = fileName.substring(0,fileName.lastIndexOf("."));
		}
		//Global.toast(fileName + "  " + downloadFURL.getAbsolutePath());
		while(true){
			exists = new File(downloadFURL.getAbsolutePath() + "/" + fileName);
			if(exists.exists()){
				i += 1;
			//	System.out.println("EXISTS");
				fileName = flRn + " - (" + i + ")" + "." + flEx;
			}else{
				break;
			}
		}

		// MIME Type을 확장자를 통해 예측한다.
		if(mimeType == null){
			if(mimetype.equalsIgnoreCase("application/octet-stream")){
				mimetype = null;
			}
			if(mimetype == null || mimetype.equalsIgnoreCase("")){
				//String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
				String fileExtension = flEx;
				mimeType = mtm.getMimeTypeFromExtension(fileExtension);
			}else{
				mimeType = mimetype;
			}
			fileName = fileName.replace("\"", "");
		}

		// Download 디렉토리에 저장하도록 요청을 작성

		final Request request = new DownloadManager.Request(downloadUri);
		request.addRequestHeader("Cookie", CookieManager.getInstance().getCookie(url));
		if(userAgent != null){
			request.addRequestHeader("User-Agent", userAgent);
		}
		if(referer != null){
			request.addRequestHeader("Referer", referer);
		}
		Log.i("TTTT",downloadUri.toString() + " + " + fileName + " + ");
		request.setTitle(fileName);
		request.setDescription(url);
		request.setDestinationUri(downloadUri);
		//Log.i("Tarks",mimeType);
		request.setMimeType(mimeType);
		if(android.os.Build.VERSION.SDK_INT <= 10){
			request.setShowRunningNotification(true);
		}else{
			request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		request.setDestinationInExternalPublicDir(dlPath, fileName);

		// 다운로드 매니저에 요청 등록

		new Thread("Browser download") {
            public void run() {
            	downloadManager.enqueue(request);
            }
        }.start();

	}
	
	public static void viewDownloads(Context con){
		con.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
	}
	public static String decodeURL(String arg){
		try {
			return URLDecoder.decode(arg, "UTF-8").replace("+", " ");
		} catch (UnsupportedEncodingException e) {
			return arg;
		}
	}

}
