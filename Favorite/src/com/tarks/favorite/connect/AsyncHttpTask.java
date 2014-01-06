package com.tarks.favorite.connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.tarks.favorite.ModApplication;
import com.tarks.favorite.R;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class AsyncHttpTask extends AsyncTask<String, Void, String> {
	private Handler handler;
	private Exception exception;
	String responseData;
	String myId, myPWord, myTitle, mySubject, myResult;

	public AsyncHttpTask(Handler handler) {
		this.handler = handler;
	}

	@Override
	protected String doInBackground(String... urls) {
		try {
			// urls[0]의 URL부터 데이터를 읽어와 String으로 리턴
			return Task(myId);
		} catch(Exception ex) {
			this.exception = ex;
			return null;
		}
	}
	
	public String Task(String url) {
		try {


			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url1 = new URL(ModApplication.getInstance().getString(R.string.server_path)
					+ "member/tarks_get_member_info.php"); // URL
			// 설정
			HttpURLConnection http = (HttpURLConnection) url1
					.openConnection(); // 접속

			// --------------------------
			// 전송 모드 설정 - 기본적인 설정이다
			// --------------------------
			
			http.setDefaultUseCaches(false);
			http.setDoInput(true); // 서버에서 읽기 모드 지정
			http.setDoOutput(true); // 서버로 쓰기 모드 지정
			http.setRequestMethod("POST"); // 전송 방식은 POST

			// 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
			http.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			// --------------------------
			// 서버로 값 전송
			// --------------------------
			StringBuffer buffer = new StringBuffer();
			buffer.append("authcode").append("=").append("642979")
					.append("&"); // php 변수에 값 대입
			buffer.append("tarks_account").append("=").append("jeonghunn");

			OutputStreamWriter outStream = new OutputStreamWriter(
					http.getOutputStream(), "utf-8");
			PrintWriter writer = new PrintWriter(outStream);
			writer.write(buffer.toString());
			writer.flush();
			// --------------------------
			// 서버에서 전송받기
			// --------------------------
			InputStreamReader tmp = new InputStreamReader(
					http.getInputStream(), "utf-8");
			BufferedReader reader = new BufferedReader(tmp);
			StringBuilder builder = new StringBuilder();
			String str;

			while ((str = reader.readLine()) != null) { // 서버에서 라인단위로
														// 보내줄
				builder.append(str); // 것이므로 라인단위로 읽는다
				// builder.append(str + "\n"); // View에 표시하기 위해 라인 구분자
				// 추가
			}
			myResult = builder.toString(); // 전송결과를 전역 변수에 저장

		} catch (MalformedURLException e) {
			//
		} catch (IOException e) {
			//
		}

		return null;
	}
	

	@Override
	protected void onPostExecute(String responseData) {
		if (exception != null) {
			Message msg = handler.obtainMessage();
			msg.what = -1;
			msg.obj = exception;
			handler.sendMessage(msg);
			return;
		} else {
			Message msg = handler.obtainMessage();
			msg.what = 3;
			msg.obj = responseData;
			handler.sendMessage(msg);
		}
	}
	

	
}

