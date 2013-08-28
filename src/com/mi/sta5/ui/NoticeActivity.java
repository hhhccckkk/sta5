package com.mi.sta5.ui;

import com.mi.sta5.date.UrlDate;
import com.mi.sta5.util.MangerActivitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Button;
import android.widget.Toast;
/**
 * 
 * @author kevin
 * @Description 展示公告，新闻或者搞笑列表
 */
public class NoticeActivity extends Activity implements OnClickListener {
	private WebView webView;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		service();
		MangerActivitys.activitys.add(this);  // 把當前activity添加到集合中
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		setContentView(R.layout.notice);
		button = (Button) findViewById(R.id.back);
		button.setOnClickListener(this);
		webView = (WebView) findViewById(R.id.web_video_id);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		webView.setScrollBarStyle(View.SOUND_EFFECTS_ENABLED);
		webView.getSettings().setBlockNetworkImage(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);    //設置緩存，数据先从缓存获取

	}
	private void service() {
		webView.setWebChromeClient(new WebChromeClient() {
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Toast.makeText(NoticeActivity.this, message, Toast.LENGTH_LONG)
						.show();
				result.confirm();
				return true;
			}
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					webView.getSettings().setBlockNetworkImage(false);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				webView.loadUrl(url);

				return true;
			}
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(NoticeActivity.this, R.string.loadError,
						Toast.LENGTH_LONG).show();
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.d("hck", "start" + url);
				super.onPageStarted(view, url, favicon);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Log.i("hck", url + "jieshu");
				webView.getSettings().setBlockNetworkImage(false);
			}
		});
		webView.loadUrl(UrlDate.indexcheck);
	}
	@Override
	public void onClick(View v) {
		button.setBackgroundResource(R.drawable.tabbar_icon_1);
		this.finish();
		overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
		overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
	}
}
