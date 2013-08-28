package com.mi.sta5.ui;

import net.hockeyapp.android.CheckUpdateTask;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mi.sta5.date.Date;
import com.mi.sta5.date.UrlDate;
import com.mi.sta5.service.AlertDialogs;
import com.mi.sta5.service.JavascriptServer;
import com.mi.sta5.util.IsNetworkConnection;
import com.mi.sta5.util.MangerActivitys;
import com.mi.sta5.util.StartNetWork;

/**
 * 
 * @author kevin
 * @Description 界面中的大厅功能项使用，显示火车票信息;
 * @Creation Date 2012.12.13
 */
@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
public class HomeActivity extends BaseActivity implements OnClickListener {
	private CheckUpdateTask checkUpdateTask;
	final boolean DEBUG = true; // 正式发布时,请设为false;
	private WebView webView; // 申明Webview
	private Spinner spinner; // 下拉選擇控件
	private String[] city; // 城市数组
	private Button datButton; // 返回按钮
	private static final String TAG = "HomeActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		initView(); // 初始化數據
		service(); // 獲取數據
		MangerActivitys.addActivity(this); // 把當前activity加進集閤中，應用結束後銷燬
		if (DEBUG) {
			System.setProperty("http.keepAlive", "false");
			UpdateActivity.iconDrawableId = R.drawable.app; // 设置更新时的应用图标
			checkForUpdates();// 调用更新检查函数(注:这个函数是需要在当前类中加入的)
		}
	}

	/**
	 * @Description 用戶設置完網絡鏈接後，返回當前activity時候調用，重新獲取數據
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			Log.i(TAG, "onActivityResult");
			initView();
			service();
		}
	}

	@Override
	protected void onResume() {
		checkForCrashes();
		if (isNetWork1 == false) { // 如果爲假則重新獲取數據
			service();
			isNetWork1 = true;
		}
		if (false == IsNetworkConnection.isNetworkConnection(this)) { // 判斷用戶網絡鏈接是否打開
			StartNetWork.setNetworkMethod(this, HomeActivity.this); // 弹出网络设置页面，让用户设置网络
		}
		super.onResume();
	}

	private void initView() {
		Log.i(TAG, "initView");
		setContentView(R.layout.home);
		datButton = (Button) findViewById(R.id.dt);
		datButton.setOnClickListener(this);
		webView = (WebView) findViewById(R.id.web_home_id);
		webView.getSettings().setJavaScriptEnabled(true); // 让webview支持javascript
		webView.getSettings().setDomStorageEnabled(true); // 让webview支持LocalStorage
		webView.setScrollBarStyle(View.SOUND_EFFECTS_ENABLED); // 设置webview滚动条样式
		webView.setScrollContainer(true);
		city = getResources().getStringArray(R.array.city);
		spinner = (Spinner) findViewById(R.id.sp);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.city, R.layout.spinner_text_color);
		adapter.setDropDownViewResource(R.layout.spinner);
		spinner.setAdapter(adapter);
		spinner.setPrompt("城市选择");
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				webView.loadUrl("javascript:appendToListBox('" + city[arg2] // 调用js方法显示不同位置的火车票信息
						+ "')");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	};

	@Override
	public void service() {

		Log.i(TAG, "getDate");
		webView.setWebChromeClient(new WebChromeClient() { // 輔助webview處理js腳本
			public boolean onJsAlert(WebView view, String url, String message, // 捕获网页弹出的信息
					final JsResult result) {
				Log.i(TAG, "getDate" + "onJsAlert" + message);
				result.confirm();
				return true;
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) { // 加载网页时候的进度
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					Log.d("HomeActivity", "getDate" + newProgress + "");
				}
			}
		});
		webView.setWebViewClient(new WebViewClient() { // 設置
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i(TAG, "getDate" + "shouldOverrideUrlLoading" + url);
				if (UrlDate.indexcheck.equals(url)) { // 當加載indexcheck頁面時候，跳轉到新的activity
					startActivity(new Intent(HomeActivity.this,
							NoticeActivity.class));
					overridePendingTransition(R.anim.iphone1, R.anim.iphone2); // 动画
					return true;
				}
				webView.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, // 加載網頁失敗時候，給用戶提示
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(HomeActivity.this, R.string.loadError,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) { // 加載網頁開始時候，显示progressbar
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) { // 网页加载完成
				super.onPageFinished(view, url);
				webView.loadUrl("javascript:initTicketInfo(appendToListBox)");
			}
		});
		webView.addJavascriptInterface(new JavascriptServer(this, this),
				"actAndroid");
		webView.loadUrl(UrlDate.index);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { // 按返回键时候，提示用户是否退出
		new AlertDialogs(HomeActivity.this,this).alertDialog(Date.D_TITLE,
				Date.D_COTENT, Date.D_YES, Date.D_NO,Date.D_EXIT);
		return true;
	}

	// private void requestLocationUpdate() {
	// isStartLocationServer = LocationService.getLocationService()
	// .isServiceEnabled(this);
	// if (!isStartLocationServer) {
	// return;
	// } else {
	// LocationService.getLocationService().requestLocationUpdates();
	// }
	// }

	@Override
	protected void onDestroy() {
		if (null != webView) {
			webView.destroy();
		}
		super.onDestroy();
	}

	// 处理转屏部分
	@Override
	public Object onRetainNonConfigurationInstance() {
		// a4. ---处理转屏--------------------------------------
		if (DEBUG) {
			checkUpdateTask.detach();
			return checkUpdateTask;
		} else
			return null;
		// a4-------------------------------------------------
	}

	// b3. 必需加入的---------------
	private void checkForCrashes() {
		CrashManager.setAutoSubmitCrashReport(false);// 设置为false将会弹出确认的提示对话框,默认为true
		// CrashManager.register(this,
		// "http://192.168.2.17/quincy/crash_v200.php");//设置Web端的URL
		CrashManager.register(this,
				"http://118.145.12.100/quincy/crash_v200.php");// 设置Web端的URL
	}

	// b3---------------------------
	// a5. 必需加入的---------------
	private void checkForUpdates() {
		checkUpdateTask = (CheckUpdateTask) getLastNonConfigurationInstance();// 处理转屏部分
		if (checkUpdateTask != null) {
			checkUpdateTask.attach(this);
		} else {
			checkUpdateTask = new CheckUpdateTask(this,
					"http://192.168.2.17/hockey/", null);// 注意URL必需以"/"结尾,不能为https
			checkUpdateTask.execute();
		}
	}

	/**
	 * @Description 跳转到新的activity显示公告信息
	 */
	@Override
	public void onClick(View v) { // 点击公告按钮时候触发
		datButton.setBackgroundResource(R.drawable.tabbar_icon_1);
		Intent intent = new Intent();
		intent.putExtra("centerName", "公告");
		intent.putExtra("leftName", "返回");
		intent.putExtra("type", "notice");
		intent.setClass(HomeActivity.this, NewsActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
	}

}
