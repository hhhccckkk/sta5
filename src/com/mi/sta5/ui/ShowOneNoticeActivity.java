package com.mi.sta5.ui;

import com.mi.sta5.date.UrlDate;
import com.mi.sta5.util.MangerActivitys;

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
import android.widget.Toast;
/**
 * 
 * @author kevin
 * @Description 显示具体的新闻，搞笑或者是公告的详细信息
 */
public class ShowOneNoticeActivity extends Activity implements OnClickListener {
	private WebView webView;
	private Button button;
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
		setContentView(R.layout.show_one_news);
		layout = (LinearLayout) findViewById(R.id.pg);
		webView = (WebView) findViewById(R.id.web_show_onenew);
		button = (Button) findViewById(R.id.back);
		button.setOnClickListener(this);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setScrollBarStyle(View.SOUND_EFFECTS_ENABLED);
		webView.setScrollContainer(true);
	}

	private void service() {
            webView.setWebChromeClient(new WebChromeClient()
            {
            	@Override
            	public boolean onJsAlert(WebView view, String url,
            			String message, JsResult result) {
            		Toast.makeText(ShowOneNoticeActivity.this, message, Toast.LENGTH_LONG).show();
            		return super.onJsAlert(view, url, message, result);
            	}
            	@Override
            	public void onProgressChanged(WebView view, int newProgress) {
            		super.onProgressChanged(view, newProgress);
            	}
            });
         webView.setWebViewClient(new WebViewClient()
         {
        	 @Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		webView.loadUrl(url);
				return true;
        	}
        	 @Override
        	public void onPageFinished(WebView view, String url) {
        		super.onPageFinished(view, url);
        		layout.setVisibility(View.GONE);
        	}
         });
           webView.loadUrl(UrlDate.newsDetails); 	
	}

	@Override
	public void onClick(View v) {
		button.setBackgroundResource(R.drawable.btn_close_pre);
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
