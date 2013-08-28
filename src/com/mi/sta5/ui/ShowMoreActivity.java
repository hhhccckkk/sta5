package com.mi.sta5.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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

import com.mi.sta5.date.UrlDate;

public class ShowMoreActivity extends Activity {
	private WebView webView;
	private TextView textView;
	private LinearLayout layout;
	private String leftBtName;    //左边按钮的名字
	private String centerBtName;    //标题中间显示文字
	private Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		service();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		setContentView(R.layout.show_more);
		layout = (LinearLayout) findViewById(R.id.pg);
		backButton = (Button) findViewById(R.id.back);
		backButton.setText(getIntent().getStringExtra("backButtonName"));
		textView = (TextView) findViewById(R.id.more_name_id);
		textView.setText(getIntent().getStringExtra("textName"));
		webView = (WebView) findViewById(R.id.web_show_more_id);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setScrollBarStyle(View.SOUND_EFFECTS_ENABLED);
		setListener();
	}

	private void setListener() {
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowMoreActivity.this.finish();
				overridePendingTransition(R.anim.iphone2, R.anim.iphone1);
			}
		});
	}

	private void service() {
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				Toast.makeText(ShowMoreActivity.this, message,
						Toast.LENGTH_LONG).show();
				return super.onJsAlert(view, url, message, result);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.equals(UrlDate.moreListDetails)) {
					Intent intent = new Intent();
					intent.putExtra("backButtonName", leftBtName);
					intent.putExtra("textName", centerBtName);
					intent.setClass(ShowMoreActivity.this,
							ShowOneMoreActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.iphone1, R.anim.iphone2); // 动画
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
		webView.addJavascriptInterface(new startShot(), "actAndroid");
		webView.loadUrl(UrlDate.moreList);
	}

	class startShot {
		public void actNavigation(String leftBtnTitle, String centerTitle,
				String rightBtnTitle) {
			leftBtName = leftBtnTitle;
			centerBtName = centerTitle;
			Log.i("hck", leftBtnTitle);

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShowMoreActivity.this.finish();
		overridePendingTransition(R.anim.iphone2, R.anim.iphone1);
	}
}
