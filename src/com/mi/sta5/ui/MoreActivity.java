package com.mi.sta5.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mi.sta5.date.Date;
import com.mi.sta5.date.UrlDate;
import com.mi.sta5.service.AlertDialogs;
import com.mi.sta5.util.MangerActivitys;

@SuppressLint("SetJavaScriptEnabled")
public class MoreActivity extends BaseActivity{
	private WebView webView;
	private LinearLayout layout; // progressbar
	private String leftBtName;
	private String centerBtName;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		initView();
		MangerActivitys.addActivity(this);
		getDate();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void getDate() {
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				Toast.makeText(MoreActivity.this, message, Toast.LENGTH_LONG)
						.show();
				return super.onJsAlert(view, url, message, result);
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.equals(UrlDate.moreList)) {
					intent=new Intent();
					intent.putExtra("backButtonName", leftBtName);
					intent.putExtra("textName", centerBtName);
					intent.setClass(MoreActivity.this, ShowMoreActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.iphone1, R.anim.iphone2);  //动画
					return true;
				}
				else if (url.equals(UrlDate.moreListDetails)) {
					intent=new Intent();
					intent.putExtra("backButtonName", leftBtName);
					intent.putExtra("textName", centerBtName);
					intent.setClass(MoreActivity.this, ShowOneMoreActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.iphone1, R.anim.iphone2);  //动画
					return true;
				}
				webView.loadUrl(url);
				return true;
			};

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(MoreActivity.this, R.string.loadError,
						Toast.LENGTH_LONG).show();
			};

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				layout.setVisibility(View.GONE);
			}
		});
		webView.addJavascriptInterface(new startShot(), "actAndroid");
		webView.loadUrl(UrlDate.more);
	}

	private void initView() {
		layout = (LinearLayout) findViewById(R.id.pg);
		webView = (WebView) findViewById(R.id.web_more_id);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setScrollBarStyle(View.SOUND_EFFECTS_ENABLED);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		new AlertDialogs(MoreActivity.this,this).alertDialog(Date.D_TITLE,
				Date.D_COTENT, Date.D_YES, Date.D_NO,Date.D_EXIT);
		return true;
	}
	 class startShot
	    {
	    	public void actNavigation(String leftBtnTitle, String centerTitle,
					String rightBtnTitle) {
				leftBtName = leftBtnTitle;
				centerBtName = centerTitle;

			}
	    }
	@Override
	public void service() {
		// TODO Auto-generated method stub
		
	}
}
