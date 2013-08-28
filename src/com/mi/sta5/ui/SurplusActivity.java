package com.mi.sta5.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.sta5.date.Date;
import com.mi.sta5.date.UrlDate;
import com.mi.sta5.service.AlertDialogs;
import com.mi.sta5.service.JavascriptServer;
import com.mi.sta5.util.IsNetworkConnection;
import com.mi.sta5.util.MangerActivitys;
import com.mi.sta5.util.StartNetWork;

@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
public class SurplusActivity extends BaseActivity {
	private WebView webView;
	private TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surplus);
		initView();
		service();
		MangerActivitys.addActivity(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			initView();
			service();
		}
	}

	@Override
	protected void onResume() {
		if (isNetWork2 == false) {
			service();
			isNetWork2 = true;
		}
		if (false == IsNetworkConnection.isNetworkConnection(this)) {
			StartNetWork.setNetworkMethod(this, SurplusActivity.this);
		}
		super.onResume();
	}

	public void service() {
		
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					textView.setVisibility(View.GONE);
					Log.d("SurplusActivity", "service" + newProgress + "");
				}
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(SurplusActivity.this, R.string.loadError,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				webView.loadUrl("javascript:initTicketInfo(appendToTicketBox)");
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}
		});
		webView.addJavascriptInterface(new JavascriptServer(this, this),
				"actAndroid");
		webView.loadUrl(UrlDate.ticket);
		
	}

	private void initView() {
		textView = (TextView) findViewById(R.id.hidden_text);
		webView = (WebView) findViewById(R.id.web_surplus_id);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollContainer(true);
		webView.setScrollBarStyle(View.SOUND_EFFECTS_ENABLED);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		new AlertDialogs(SurplusActivity.this,this).alertDialog(Date.D_TITLE,
				Date.D_COTENT, Date.D_YES, Date.D_NO,Date.D_EXIT);
		return true;
	}
	@Override
	protected void onDestroy() {
		if (null != webView) {
			webView.clearHistory();
			webView.destroy();
		}
		super.onDestroy();
	}

	

}
