package com.mi.sta5.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VipToast extends Activity{
	private Button extButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip_toast);
		extButton=(Button) findViewById(R.id.ext_bt);
		extButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 VipToast.this.finish();
			}
		});
	}

}
