package com.mi.sta5.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import com.mi.sta5.util.MangerActivitys;

/**
 * 
 * @author kevin
 * @Description 加載动画使用
 */
public class LogoActivity extends Activity {
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.logo);
		MangerActivitys.addActivity(this);
		imageView = (ImageView) findViewById(R.id.logo_image_id);
		AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f); // 設置動畫
		animation.setDuration(1000); // 動畫顯示時間
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent();
				intent.setClass(LogoActivity.this, MainActivity.class);
				startActivity(intent);
				LogoActivity.this.finish();
			}
		});
		imageView.setAnimation(animation); // 爲imageview設置動畫
	}

}
