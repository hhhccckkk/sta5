package com.mi.sta5.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.mi.sta5.util.MangerActivitys;
/**
 * 
 * @author kevin
 * @Description 控制各個按鈕的切換，顯示不同的activity
 */
public class MainActivity extends TabActivity {
	private TabHost tabHost; 
	private TabSpec tabSpec;
	public static RadioGroup radioGroup;
	public static FrameLayout fLayout;
	public static ImageView bg;
	private static final String HOME_TAB = "home";  //大廳
	private static final String SURPLUS_TAB = "surplus";    //余票
	private static final String VIP_TAB = "vip";    //vip
	private static final String TRAVE_TAB = "trave";    //出行
	private static final String MORE_TAB = "more";    //更多
	private static final String TAG="MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		MangerActivitys.addActivity(this);
		initView();
		setListern();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void initView() // 初始化view和数据
	{
		Log.i(TAG, "MainActivity" + "initView");
		fLayout = (FrameLayout) findViewById(R.id.rl1);
		radioGroup = (RadioGroup) findViewById(R.id.RadioG);
		bg = (ImageView) findViewById(R.id.bg_id);
		tabHost = this.getTabHost();
		tabSpec = tabHost.newTabSpec(HOME_TAB).setIndicator(HOME_TAB)
				.setContent(new Intent(this, HomeActivity.class));
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec(SURPLUS_TAB).setIndicator(SURPLUS_TAB)
				.setContent(new Intent(this, SurplusActivity.class));
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec(VIP_TAB).setIndicator(VIP_TAB)
				.setContent(new Intent(this, VipActivity2.class));
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec(TRAVE_TAB).setIndicator(TRAVE_TAB)
				.setContent(new Intent(this, TraveActivity.class));
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec(MORE_TAB).setIndicator(MORE_TAB)
				.setContent(new Intent(this, MoreActivity.class));
		tabHost.addTab(tabSpec);

	}

	private void setListern() {
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.home_id:
					tabHost.setCurrentTab(0);
					break;
				case R.id.surplus_id:
					tabHost.setCurrentTab(1);
					break;
				case R.id.vip_id:
					tabHost.setCurrentTab(2);
					break;
				case R.id.trave_id:
					tabHost.setCurrentTab(3);
					break;
				case R.id.more_id:
					tabHost.setCurrentTab(4);
					break;
				default:
					break;
				}
			}
		});

	}

}
