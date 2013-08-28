package com.mi.sta5.ui;

import com.mi.sta5.date.UrlDate;
import com.mi.sta5.ui.ShowMoreActivity.startShot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowOneMoreActivity extends Activity {
	private TextView textView;
	private Button backbButton;
	private WebView webView;
	private LinearLayout layout; // progressbar

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		service();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		setContentView(R.layout.show_more_one);
		layout = (LinearLayout) findViewById(R.id.pg);
		backbButton = (Button) findViewById(R.id.back);
		backbButton.setText(getIntent().getStringExtra("backButtonName"));
		textView = (TextView) findViewById(R.id.more_name_id);
		textView.setText(getIntent().getStringExtra("textName"));
		webView = (WebView) findViewById(R.id.web_more_one__id);
		webView.getSettings().setJavaScriptEnabled(true); // 让webview支持javascript
		webView.getSettings().setDomStorageEnabled(true); // 让webview支持LocalStorage
		webView.setScrollBarStyle(View.SOUND_EFFECTS_ENABLED); // 设置webview滚动条样式
		setListener();
	}

	private void setListener() {
		backbButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowOneMoreActivity.this.finish();
				overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
			}
		});
	}

	private void service() {
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				Toast.makeText(ShowOneMoreActivity.this, message,
						Toast.LENGTH_LONG).show();
				return super.onJsAlert(view, url, message, result);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.equals(UrlDate.moreListDetails)) {
					return true;
				}
				webView.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				layout.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

		});
		webView.loadUrl(UrlDate.moreListDetails);
	}

}
