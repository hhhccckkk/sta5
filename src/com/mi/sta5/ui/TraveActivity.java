package com.mi.sta5.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mi.sta5.date.Date;
import com.mi.sta5.date.UrlDate;
import com.mi.sta5.service.AlertDialogs;
import com.mi.sta5.util.IsNetworkConnection;
import com.mi.sta5.util.MangerActivitys;
import com.mi.sta5.util.StartNetWork;

@SuppressLint("SetJavaScriptEnabled")
public class TraveActivity extends BaseActivity {
	private WebView webView;
	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trave);
		initView();
		MangerActivitys.addActivity(this);
		server();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (false == IsNetworkConnection.isNetworkConnection(this)) {
			StartNetWork.setNetworkMethod(this, TraveActivity.this);
		}
	}

	private void server() {
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				layout.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
				
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
           @Override
        public void onPageFinished(WebView view, String url) {
        	super.onPageFinished(view, url);
        	layout.setVisibility(View.GONE);
        }
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(TraveActivity.this, R.string.loadError,
						Toast.LENGTH_LONG).show();
			}
		});
		webView.loadUrl(UrlDate.trip);
	}

	private void initView() {
		layout = (LinearLayout) findViewById(R.id.pg);
		webView = (WebView) findViewById(R.id.web_trave_id);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setScrollBarStyle(View.SOUND_EFFECTS_ENABLED);
		webView.setScrollContainer(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		new AlertDialogs(TraveActivity.this,this).alertDialog(Date.D_TITLE,
				Date.D_COTENT, Date.D_YES, Date.D_NO,Date.D_EXIT);
		return false;
	}

	@Override
	public void service() {
		// TODO Auto-generated method stub
		
	}
}
