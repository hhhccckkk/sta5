package com.mi.sta5.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import com.mi.sta5.util.MangerActivitys;
import com.mi.sta5.util.ShowDialog;

public class CameraActivity extends Activity {
	public static Bitmap bitmap = null;
	private final static int REQUESTCODE = 1;
	private String imagePath = null;
	private File path = Environment.getExternalStorageDirectory(); // sdcard路径
	private String sdcard = Environment.getExternalStorageState(); // 用于判断sdcard是否存在
	private File saveFile; // 所保存文件名字
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		MangerActivitys.addActivity(this);
		sdcardIsExist();
	}

	private void sdcardIsExist() {
		if (!sdcard.equals(Environment.MEDIA_MOUNTED)) { // 判断sdcard是否存在
			new AlertDialog.Builder(this)
					.setTitle("错误提示！")
					.setMessage("sdcard不存在")
					.setPositiveButton("确定",
							new ShowDialog(CameraActivity.this)).show();
			return;
		} else {
			getCameraForPicter();
		}
	}

	private void getCameraForPicter() {
		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		if (null != bitmap) {
			bitmap.recycle();
		}
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); // 启动照相机照相
		startActivityForResult(intent, REQUESTCODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUESTCODE) {
			if (data == null) {
				CameraActivity.this.finish();
				return;
			}
		}
		if (requestCode == REQUESTCODE && data != null) {
			Uri uri = data.getData();
			Bundle bundle = data.getExtras();
			if (null != uri) {
				try {
					getPath(uri);
					Intent intent = new Intent();
					intent.putExtra("path", imagePath);
					intent.setClass(CameraActivity.this,
							ShowPicterActivity.class);
					startActivity(intent);
					if (null != bitmap) {
						bitmap.recycle();
					}
					CameraActivity.this.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			} else if (bundle != null) {
				pDialog=ProgressDialog.show(CameraActivity.this, "请稍等", "正在处理中...");
				new thread().start();
				if (bundle != null) {
					bitmap = (Bitmap) bundle.get("data");
				}
			}
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pDialog.dismiss();
			Intent intent = new Intent();
			intent.putExtra("path", imagePath);
			intent.setClass(CameraActivity.this, ShowPicterActivity.class);
			startActivity(intent);
			CameraActivity.this.finish();
		};
	};

	class thread extends Thread {
		@Override
		public void run() {
			saveFile = new File(path + "/train");
			saveFile.mkdir();
			File file = new File(saveFile, System.currentTimeMillis() + ".jpg");
			OutputStream os = null;
			try {
				os = new FileOutputStream(file);
				bitmap.compress(CompressFormat.JPEG, 100, os);
				os.flush();
				os.close();
			} catch (IOException e) {
				Log.i("hckhck", e + "");
			}
			imagePath = file.toString();
			handler.sendMessage(new Message());
		}
	}

	private void getPath(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, proj, null, null, null);
		// 獲取图片的索引值
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		// 根据索引值获取图片路径
		imagePath = cursor.getString(column_index);
		Log.e("sys", imagePath);
	}

}
