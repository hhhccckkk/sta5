package com.mi.sta5.ui;

import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.sta5.date.UrlDate;
import com.mi.sta5.service.Toasts;
import com.mi.sta5.util.Exit;
import com.mi.sta5.util.IsNetworkConnection;
import com.mi.sta5.util.MangerActivitys;
import com.mi.sta5.util.StartNetWork;

/**
 * 
 * @author kevin
 * @Description 新聞，公告，笑話使用，展示各自類榮
 */
public class NewsActivity extends Activity implements OnClickListener {
	private WebView webView;
	private Button button;
	private TextView textView;
	private LinearLayout layout;
	private Timer timer;
	private final static long TIMEOUT = 8000;
	private Message message;
	private String type; // 用于判断是新闻，公告或者笑话
	private String leftTitle; // 左邊按鈕的名字
	private String centerTitle; // 中間textview的顯示信息
	private Intent intent;
	private static final String TAG = "NewsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		Log.i(TAG, "onCreate");
		initView();
		setLinster();
		service();
		MangerActivitys.activitys.add(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (false == IsNetworkConnection.isNetworkConnection(this)) {
			StartNetWork.setNetworkMethod(this, NewsActivity.this);
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		Log.i(TAG, "initView");
		webView = (WebView) findViewById(R.id.web_new_id);
		textView = (TextView) findViewById(R.id.title_id);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		button = (Button) findViewById(R.id.back);
		layout = (LinearLayout) findViewById(R.id.pg);
		intent = getIntent();
		type = intent.getStringExtra("type");
		leftTitle = intent.getStringExtra("leftName");
		centerTitle = intent.getStringExtra("centerName");
		textView.setText(centerTitle);
		button.setText(leftTitle);
	}

	private void setLinster() {
		button.setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				layout.setVisibility(View.GONE);
				new AlertDialog.Builder(NewsActivity.this)
						.setTitle(R.string.connection_error)
						.setPositiveButton("退出", new Exit(NewsActivity.this))
						.setNegativeButton("確定", null).show();
				break;
			default:
				break;
			}
		};
	};

	private void service() {
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					layout.setVisibility(View.GONE);
					Log.d("HomeActivity", "getDate" + newProgress + "");
				}
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.equals(UrlDate.newsDetails)) {
					startActivity(new Intent(NewsActivity.this,
							ShowOneNoticeActivity.class));
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
				Toasts.toast(NewsActivity.this, "数据加载失败，请检查网络");
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				layout.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if (webView.getProgress() < 100) {
							message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							timer.cancel();
							timer.purge();
						}
					}
				}, TIMEOUT);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				timer.cancel();
				timer.purge();
				Log.i(TAG, "setWebViewClient" + "onPageFinished" + type);
				webView.loadUrl("javascript:initNewsList('" + type + "')");
			}
		});
		webView.loadUrl(UrlDate.news);

	}

	@Override
	public void onClick(View v) {
		button.setBackgroundResource(R.drawable.tabbar_icon_1);
		this.finish();
		overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
	}

	@Override
	protected void onDestroy() {
		this.finish();
		overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
		super.onDestroy();
	}
}
