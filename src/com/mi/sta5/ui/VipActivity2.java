package com.mi.sta5.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.mi.sta5.adpter.VipAdpter;
import com.mi.sta5.date.Date;
import com.mi.sta5.service.AlertDialogs;
import com.mi.sta5.util.Exit;
import com.mi.sta5.util.MangerActivitys;

/**
 * 
 * @author kevin
 * @Description 用于vip功能項目使用
 * @Creation Date 2012.12.13
 */
public class VipActivity2 extends BaseActivity implements OnItemClickListener {
	private static final int NEWS = 0; // 新聞
	private static final int JOKE = 1; // 搞笑搞怪
	private static final int MUSIC = 2; // 音樂視聽
	private static final int VIDEO = 3; // 視頻短片
	private static final int MOVIE = 4; // 電影電視
	private static final int BOOK = 5; // 高清雜誌
	private static final int WIFI = 6; // 免費wifi
	private static final int SHOW = 7; // 照片分享
	private View view;
	private GridView gridView;
	private Intent intent;
	private int vipButton[] = { R.drawable.vip_news, R.drawable.vip_funny,
			R.drawable.vip_music, R.drawable.vip_video, R.drawable.vip_tv,
			R.drawable.vip_book, R.drawable.vip_wifi, R.drawable.vip_prize_pld }; // 初始化數組，各項功能背景圖片(新聞，視頻，等)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		MangerActivitys.activitys.add(this);
	}

	private void init() {
		setContentView(R.layout.gridview_menu);
		view = LayoutInflater.from(this).inflate(R.layout.toast, null);
		view.getBackground().setAlpha(200);    //設置Toast的透明度
		VipAdpter adpter = new VipAdpter(vipButton, this);    //创建一个适配器
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setOnItemClickListener(this);
		gridView.setAdapter(adpter);
		gridView.setFocusable(true);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case NEWS:    //点击新闻时候，跳到新的activity，显示新闻详细信息
			intent = new Intent();
			intent.putExtra("type", "news");
			intent.putExtra("leftName", "返回");
			intent.putExtra("centerName", "新闻动态");
			intent.setClass(VipActivity2.this, NewsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.iphone2, R.anim.iphone1);
			break;
		case JOKE:
			intent = new Intent();
			intent.putExtra("leftName", "返回");
			intent.putExtra("centerName", "搞笑搞怪");
			intent.putExtra("type", "joke");
			intent.setClass(VipActivity2.this, NewsActivity.class);
			startActivity(intent);
			break;
		case MUSIC:
			vipToast();
			break;
		case VIDEO:
			intent = new Intent();
			intent.putExtra("typeName", "video");
			intent.putExtra("title", "视频短片");
			intent.setClass(VipActivity2.this, VideoActivity.class);
			startActivity(intent);

			break;
		case MOVIE:
			intent = new Intent();
			intent.putExtra("typeName", "movie");
			intent.putExtra("title", "电影电视");
			intent.setClass(VipActivity2.this, VideoActivity.class);
			startActivity(intent);
			break;
		case BOOK:
			vipToast();
			break;
		case WIFI:
			vipToast();
			break;
		case SHOW:
			share();
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		new AlertDialogs(VipActivity2.this,this).alertDialog(Date.D_TITLE,
				Date.D_COTENT, Date.D_YES, Date.D_NO,Date.D_EXIT);
		return true;

	}
	private void share()
	{
		startActivity(new Intent(VipActivity2.this,ToastShareActivity.class));
	}
	private void vipToast()
	{
		startActivity(new Intent(VipActivity2.this,VipToast.class));
	}

	@Override
	public void service() {
		// TODO Auto-generated method stub
		
	}
}
