package com.mi.sta5.ui;

import com.mi.sta5.date.UrlDate;
import com.mi.sta5.util.MangerActivitys;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author kevin
 * @Description 该类用于展示视频短片和电影电视
 */
public class VideoActivity extends Activity {
	private WebView webView;
	private Button backButton;
	private LinearLayout layout;
	private TextView textView;
	private String leftBtName;
	private String centerBtName;
	private String typeString; // 接收intent傳過來的數據，分为2种，视频短片和电影电视
	private String title; // 標題上面顯示的類榮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		service();
		setLister();
		MangerActivitys.activitys.add(this);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		typeString = getIntent().getStringExtra("typeName");
		title = getIntent().getStringExtra("title");
		setContentView(R.layout.video);
		layout = (LinearLayout) findViewById(R.id.pg);
		webView = (WebView) findViewById(R.id.web_video_id);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		webView.setScrollBarStyle(View.SOUND_EFFECTS_ENABLED);
		webView.getSettings().setBlockNetworkImage(true); // 加載網頁時候，如果網頁有圖片先阻塞圖片，怎加展示速度。
		webView.getSettings().setDomStorageEnabled(true);
		textView = (TextView) findViewById(R.id.title);
		textView.setText(title);
		backButton = (Button) findViewById(R.id.dt);
	}

	private void setLister() { // 为返回按钮绑定事件，点击后，销毁当前activity
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backButton.setBackgroundResource(R.drawable.tabbar_icon_1);
				VideoActivity.this.finish();
				overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
			}
		});
	}

	private void service() {
		webView.setWebChromeClient(new WebChromeClient() {
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				result.confirm();
				return true;
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}

		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.equals(UrlDate.movieDetails)) { // 跳转到新的activity，显示电影详细信息，用于播放
					Intent intent = new Intent();
					intent.putExtra("leftBtName", leftBtName);
					intent.putExtra("centerBtName", centerBtName);
					intent.setClass(VideoActivity.this, ShowOneMovie.class);
					startActivity(intent);
					overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
					return true;
				}
				webView.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(VideoActivity.this, R.string.loadError,
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
				webView.loadUrl("javascript:initMovieList('" + typeString // 调用js，传入typeString，web段根据typeString获取相应的数据
						+ "')");
				webView.getSettings().setBlockNetworkImage(false); // 加載完成，显示图片
				layout.setVisibility(View.GONE);
			}
		});
		webView.addJavascriptInterface(new startShot(), "actAndroid"); // 为webview绑定对象，用于web调用
		webView.loadUrl(UrlDate.movie);
	}

	class startShot { // 用于web端调用的java代码
		public void actNavigation(String leftBtnTitle, String centerTitle,
				String rightBtnTitle) { // 获取标题上面显示的信息，按钮，电影名字
			leftBtName = leftBtnTitle;
			centerBtName = centerTitle;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		backButton.setBackgroundResource(R.drawable.tabbar_icon_1); // 点击返回按钮时候，替换他的背景
		VideoActivity.this.finish();
		overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
