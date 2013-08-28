package com.mi.sta5.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
public class ToastShareActivity extends Activity implements OnClickListener {
	private Button shareBt1;
	private Button closeBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.toastshare);
		shareBt1 = (Button) findViewById(R.id.share_bt1);
		closeBt = (Button) findViewById(R.id.close_bt1);
		setListener();
	}
	private void setListener()
	{
		shareBt1.setOnClickListener(this);
		closeBt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_bt1:
			startActivity(new Intent(ToastShareActivity.this,CameraActivity.class));
			ToastShareActivity.this.finish();
			break;
      case R.id.close_bt1:
    	  ToastShareActivity.this.finish();
		default:
			break;
		}
	}

	
}
