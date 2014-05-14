//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite.connect;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.tarks.favorite.ModApplication;
import com.tarks.favorite.global.Globalvariable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
	private Handler handler;
	private Exception exception;
	int DataContent;
	String responseData;
	String fileName;
	String url, myResult;
	Context context;
	ArrayList paramNames, paramValues, files;
	int handlernum = 1;
	// String[] files;

	// ModApplication
	static ModApplication mod = ModApplication.getInstance();

	// Uploa
	private static FileInputStream mFileInputStream = null;
	private static URL connectUrl = null;

	public ImageDownloader(Context cx, String urls, Handler handler, int hnum, int Data) {
		Globalvariable.okbutton = false;
		// Set handler
		this.handler = handler;
		// Set context
		context = cx;
		// set url
		url = urls;
		// set hanler return number
		handlernum = hnum;
		// doInBackground("");
		DataContent = Data;
		super.execute("");
	}

	@Override
	protected Bitmap doInBackground(String... urls) {

		// urls[0]의 URL부터 데이터를 읽어와 String으로 리턴
		// Log.i("URL", url);
		return Task(url);

	}

	@Override
	public void onPreExecute() {
		// Log.i("Test", "onPreExecute Called on global");

	}

	@Override
	protected void onPostExecute(Bitmap responseData) {
		// Log.i("Message", "Post");

		// Log.i("Message", "1");
		// Log.i("hey", responseData);
		Message msg = handler.obtainMessage();
		msg.what = handlernum;
		msg.obj = responseData;
		msg.arg1 = DataContent;
		handler.sendMessage(msg);
		Globalvariable.okbutton = true;

	}

	public Bitmap Task(String url) {
		final DefaultHttpClient client = new DefaultHttpClient();

		// forming a HttoGet request
		final HttpGet getRequest = new HttpGet(url);
		try {

			HttpResponse response = client.execute(getRequest);

			// check 200 OK for success
			final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;

			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					// getting contents from the stream
					inputStream = entity.getContent();

					// decoding stream data back into image Bitmap that
					// android understands
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);

					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// You Could provide a more explicit error message for
			// IOException
			getRequest.abort();
			handlernum = -1;
			Log.e("ImageDownloader", "Something went wrong while"
					+ " retrieving bitmap from " + url + e.toString());
		}
		return null;
	}

}
