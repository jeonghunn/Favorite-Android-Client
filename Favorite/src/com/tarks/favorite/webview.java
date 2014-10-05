package com.tarks.favorite;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.tarks.favorite.global.Global;

public final class webview extends SherlockActivity {
	private WebView browser;
	private ProgressBar progressBar;
	//Check Error
	 private boolean firsttime_error = true;
	// 엽로드정의
	private static final int FILECHOOSER_RESULTCODE = 1;
	private ValueCallback<Uri> uploadMessage = null;

	// 업로로로드드드드당담
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == FILECHOOSER_RESULTCODE && uploadMessage != null) {
			Uri result = data == null || resultCode != RESULT_OK ? null : data
					.getData();
			uploadMessage.onReceiveValue(result);
			uploadMessage = null;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		//actionbar back
	//	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent(); // 인텐트 받아오고
		String url = intent.getStringExtra("url"); // 인텐트로 부터 데이터 가져오고
		browser = (WebView) findViewById(R.id.webView1);

		// habilitamos javascript y el zoom
		browser.getSettings().setJavaScriptEnabled(true);
		// 진저스크롤
		browser.setVerticalScrollbarOverlay(true);

		browser.loadUrl(url);

		browser.setWebViewClient(new WebViewClient() {

			// evita que los enlaces se abran fuera nuestra app en el navegador
			// de android
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("about:blank")) {
					return false;

				}

				if (url.startsWith("http://tarks.net")) {
					browser.loadUrl(url);
					
					return false;

				} else {

					// 메
				

						if (url.matches("(?i).*htm.*")) {
							browser.loadUrl(url);

							return true;

						} else {

							Uri uri = Uri.parse(url);
							Intent it = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(it);

						}

					

					// 분리싹
				}

				return true;

			}

			// gestión de errores

			private void loadUrl(String url) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if(firsttime_error){
					firsttime_error = false;
				browser.loadUrl("https://sites.google.com/site/tarksservicesstatus/");
				}else{
					browser.loadUrl("about:blank");
				Global.Infoalert(webview.this, getString(R.string.networkerror), getString(R.string.networkerrord), getString(R.string.yes));
				}
				// AlertDialog.Builder builder = new
				// AlertDialog.Builder(webview.this);
				// builder.setMessage(getString(R.string.networkerrord)).setPositiveButton(getString(R.string.yes),
				// null).setTitle(getString(R.string.networkerror));
				// builder.show();
			}
		});

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		browser.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				progressBar.setProgress(0);
				progressBar.setVisibility(View.VISIBLE);
				webview.this.setProgress(progress * 1000);

				progressBar.incrementProgressBy(progress);

				if (progress == 100) {
					progressBar.setVisibility(View.GONE);
				}

			}

			// ICS 에서도 동작하기 위해서는 아래메소드도 넣어줘야함.

			public void openFileChooser(ValueCallback<Uri> uploadFile,
					String acceptType) {
				openFileChooser(uploadFile);
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				openFileChooser(uploadMsg);
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				uploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				startActivityForResult(Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);
			}

			// 웹뷰랑께?
			@Override
			public void onReceivedTitle(WebView view, String title) {
				webview.this.setTitle(webview.this.browser.getTitle());
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

//		if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {
//
//			browser.goBack();
//
//			return true;
//
//		}

		return super.onKeyDown(keyCode, event);

	}

	// //////////BOTONES DE NAVEGACI?N /////////

	// oculta el teclado al pulsar el botón

	// he observado que si se pulsa "Ir" sin modificarse la url no se
	// ejecuta el método onPageStarted, así que nos aseguramos
	// que siempre que se cargue una url, aunque sea la que se está
	// mostrando, se active el botón "detener"

	private static boolean canGoBack() {
		// TODO Auto-generated method stub
		return false;
	}

	public void anterior(View view) {
		browser.goBack();
	}

	public void siguiente(View view) {
		browser.goForward();
	}

	public void detener(View view) {
		browser.stopLoading();
	}

	
}