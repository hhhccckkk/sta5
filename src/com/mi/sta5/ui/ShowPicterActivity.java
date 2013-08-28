package com.mi.sta5.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.sta5.service.SaveTenXunDate;
import com.mi.sta5.service.ShareService;
import com.mi.sta5.service.Toasts;
import com.mi.sta5.util.IsNetworkConnection;
import com.mi.sta5.util.MangerActivitys;
import com.mi.sta5.util.StartNetWork;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

public class ShowPicterActivity extends Activity implements OnClickListener {
	private Intent intent;
	private String imagePath;
	private Button backButton;
	private Button sendButton;
	private Button retsetButton;
	private static ImageView imageView;
	private PopupWindow pWindow;
	private View view;
	private ImageButton button;
	private ImageButton button2;
	private EditText eText;
	private String shareText;
	private Bitmap btp;
	private OAuthV2 oAuth;
	private String clientId = "801307705";
	private String clientSecret = "5cd333f45b67d03f7a2887ff0e629be3";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_picter);
		initView();
		if (false == IsNetworkConnection.isNetworkConnection(this)) {
			StartNetWork.setNetworkMethod(this, ShowPicterActivity.this);
			return;
		}
		MangerActivitys.addActivity(this);
		getData();
		service();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		eText = (EditText) findViewById(R.id.share_text);
		backButton = (Button) findViewById(R.id.back);
		sendButton = (Button) findViewById(R.id.share_bt);
		retsetButton = (Button) findViewById(R.id.reset_bt2);
		imageView = (ImageView) findViewById(R.id.photo);
		view = LayoutInflater.from(this).inflate(R.layout.popupwindow, null);
		setListener();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void getData() {
		intent = getIntent();
		imagePath = intent.getStringExtra("path");
	}

	private void setListener() {
		backButton.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		retsetButton.setOnClickListener(this);
	}

	private void service() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 10; // width，hight设为原来的十分一
		btp = BitmapFactory.decodeFile(imagePath, options);
		imageView.setImageBitmap(btp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			ShowPicterActivity.this.finish();
			overridePendingTransition(R.anim.iphone1, R.anim.iphone2);
			destroy();
			break;
		case R.id.share_bt:
			showPW(v);
			break;
		case R.id.reset_bt2:
			startActivity(new Intent(ShowPicterActivity.this,
					CameraActivity.class));
			destroy();
			ShowPicterActivity.this.finish();
			break;
		case R.id.xin_lang:
			pWindow.dismiss();
			pWindow = null;
			shareText = eText.getText().toString();
			if (null==shareText || "".equals(shareText)) {
				Toasts.toast(ShowPicterActivity.this, "请为照片添加描述");
				return;
			}
			new ShareService(ShowPicterActivity.this, imagePath, shareText)
					.share();
			break;
		case R.id.tencent:
			pWindow.dismiss();
			shareText = eText.getText().toString();
			pWindow = null;
			if (null==shareText || "".equals(shareText)) {
				Toasts.toast(ShowPicterActivity.this, "请为照片添加描述!");
				return;
			}
		  shareTenXun();
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (pWindow != null && pWindow.isShowing()) {
			pWindow.dismiss();
			pWindow = null;

		}
		return super.onTouchEvent(event);
	}

	private void showPW(View v) {
		if (pWindow==null) {
			pWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			pWindow.setFocusable(false);
			// 设置允许在外点击消失
			pWindow.setOutsideTouchable(true);
			pWindow.showAsDropDown(v, 100, 0);
			button = (ImageButton) view.findViewById(R.id.xin_lang);
			button.setOnClickListener(this);
			button2 = (ImageButton) view.findViewById(R.id.tencent);
			button2.setOnClickListener(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroy();
	}

	private void destroy() {
		if (null != btp) {
			btp.recycle(); // 回收图片所占的内存
			System.gc();// 提醒系统及时回收
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			if (resultCode == OAuthV2AuthorizeWebView.RESULT_CODE) {
				oAuth = (OAuthV2) data.getExtras().getSerializable("oauth");
				if (oAuth.getStatus() == 0)
					SaveTenXunDate.saveDate(oAuth.getAccessToken(),
							oAuth.getExpiresIn(), oAuth.getOpenid(),
							oAuth.getAuthorizeCode(), this);
				new ShareService(ShowPicterActivity.this, imagePath, shareText)
						.shareTX(oAuth);
			}
		} else {
			Toast.makeText(getApplicationContext(), "网络错误，发布失败",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void shareTenXun() {
		Log.i("hck", "shareTenXun");
		oAuth = SaveTenXunDate.getDate(this);
		oAuth.setClientId(clientId);
		oAuth.setClientSecret(clientSecret);
		if (null == oAuth.getAccessToken()) {
			startActivityForResult(new Intent(this, Login.class), 2);
		} else {
		 new ShareService(this, imagePath, shareText).shareTX(oAuth);
		 Log.i("hck", "shareTenXun"+"     ShareService");
		}

	}

	private void toasts()
	{
		View view=LayoutInflater.from(this).inflate(R.layout.toast, null);
		TextView textView=(TextView) view.findViewById(R.id.toast_text);
		textView.setText( "请为图片增加信息!");
		Toast toast = new Toast(this);    //创建一个toast
		toast.setDuration(0);
		toast.setView(view);    //为toast设置一个view
		toast.show();
	}

}
