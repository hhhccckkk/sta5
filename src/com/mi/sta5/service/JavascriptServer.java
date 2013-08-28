package com.mi.sta5.service;
import android.content.Context;
import android.util.Log;
import com.mi.sta5.ui.BaseActivity;
public class JavascriptServer {
	private Context context;
	private BaseActivity activity;

	public JavascriptServer(Context context, BaseActivity activity) {
		this.context = context;
		this.activity = activity;
	}

	public void offLine() {
		Log.i("hck", "msms");
		BaseActivity.isNetWork1 = false;
		BaseActivity.isNetWork2 = false;
		new AlertDialogs(context, activity).alertDialog("网络异常", "网络异常，获取数据失败", "刷新", "退出", "net");
	}

}
