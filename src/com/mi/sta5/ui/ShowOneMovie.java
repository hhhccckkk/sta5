package com.mi.sta5.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.mi.sta5.service.Toasts;
import com.mi.sta5.util.IsNetworkConnection;
import com.mi.sta5.util.MangerActivitys;
import com.mi.sta5.util.StartNetWork;

/**
 * 
 * @author kevin
 * @Description 用於顯示具体的电影信息，播放视频
 */
public class ShowOneMovie extends Activity {
	private WebView webView;
	private static String url; // 存储电影的播放地址
	private Button button;
	private TextView textView;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		service();
		MangerActivitys.activitys.add(this);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		setContentView(R.layout.show_movie);
		button = (Button) findViewById(R.id.back);
		layout = (LinearLayout) findViewById(R.id.pg);
		textView = (TextView) findViewById(R.id.movie_name_id);
		webView = (WebView) findViewById(R.id.web_show_onemovie_id);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setScrollBarStyle(View.SOUND_EFFECTS_ENABLED);
		webView.setScrollContainer(true);
	}

	private void service() {
		textView.setText(getIntent().getStringExtra("centerBtName"));
		button.setText(getIntent().getStringExtra("leftBtName"));
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				button.setBackgroundResource(R.drawable.tabbar_icon_1);
				ShowOneMovie.this.finish();
				overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
			}
		});
		textView.setText(getIntent().getStringExtra("centerBtName"));
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				Toast.makeText(ShowOneMovie.this, message, Toast.LENGTH_LONG)
						.show();
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}

		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

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
				webView.loadUrl("javascript:window.actAndroid.getUrl(''+"
						+ "document.getElementById('keepUrl').href+'');"); // 回調方法，獲取web頁面id爲keepUrl的href地址，即是视频播放地址（web段把地址存放在此处的）

			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toasts.toast(ShowOneMovie.this, "网络异常，数据加载失败");
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		webView.addJavascriptInterface(new startShot(), "actAndroid");
		webView.loadUrl(UrlDate.movieDetails);
	}

	class startShot {
		public void moviePlay() { // 播放視頻
			if (false == IsNetworkConnection
					.isNetworkConnection(ShowOneMovie.this)) { // 先判斷網絡鏈接是否正常
				StartNetWork.setNetworkMethod(ShowOneMovie.this,
						ShowOneMovie.this);
				return;
			}
			try {
				if (null != url) { // 判斷視頻播放地址是否爲空
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setType("video/*"); // 設置播放視頻的格式
					intent.setDataAndType(Uri.parse(url), "video/*");
					ShowOneMovie.this.startActivity(intent);
					return;
				} else {
					Toasts.toast(ShowOneMovie.this, "播放路径错误");
				}
			} catch (Exception e) {
				Log.d("exception", e.toString());
				Toasts.toast(ShowOneMovie.this, "警告！您先安装视频播放器！");
			}
		}

		public void getUrl(String movieUrl) {
			Log.i("ShowOneMovie", "getUrl" + "url" + movieUrl);
			url = movieUrl;

		}

	}

	protected void onDestroy() {
		overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
		this.finish();
		super.onDestroy();
	}

}
