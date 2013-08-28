package com.mi.sta5.ui;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity{
	public static boolean isNetWork1 = true; // 是否正常获取数据
	public static boolean isNetWork2 = true; // 是否正常获取数据
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public abstract void service();

}
