package com.mi.sta5.service;

import java.io.IOException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

public class ShareService {
	private Context context;
	private Weibo weibo;
	private Message message;
	public Oauth2AccessToken accessToken;
	private static final String URL_STRING = "http://www.sina.com";
	private String imagePath;
	private static final int ERROR = 1;
	private static final int IOEXCEPTION = 0;
	private static final int SUCCESS = 2;
	private String textString;
	private ProgressDialog pDialog;

	public ShareService(Context context, String imagePath, String textsString) {
		this.context = context;
		this.imagePath = imagePath;
		this.textString = textsString;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pDialog.dismiss();
			switch (msg.what) {
			case IOEXCEPTION:
				Toasts.toast(context, "网速不给力，发送失败");
				break;
			case ERROR:
				Toasts.toast(context, "不能连续发布相同信息，发送失败");
				break;
			case SUCCESS:
				Toasts.toast(context, "分享成功");
				break;
			default:
				break;
			}
		};
	};

	public void share() {
		accessToken = AccessTokenKeeper.readAccessToken(context);
		if (!accessToken.isSessionValid()) {
			Toasts.toast(context, "第一次使用，需要进行授权");
			weibo = Weibo.getInstance("1587758551", URL_STRING);
			weibo.authorize(context, new AuthDialogListener());
		} else {
			shareXinLang();
		}
	}

	class AuthDialogListener implements WeiboAuthListener {
		@SuppressLint("SimpleDateFormat")
		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			accessToken = new Oauth2AccessToken(token, expires_in);
			if (accessToken.isSessionValid()) {
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.format(new java.util.Date(accessToken.getExpiresTime()));
				Log.i("hck", "认证成功: \r\n access_token: " + token + "\r\n"
						+ "expires_in: " + expires_in + "\r\n有效期：" + date);
				try {
					Class sso = Class
							.forName("com.weibo.sdk.android.api.WeiboAPI");// 如果支持weiboapi的话，显示api功能演示入口按钮
					// apiBtn.setVisibility(View.VISIBLE);
				} catch (ClassNotFoundException e) {
					// e.printStackTrace();
					Log.i("hck", "com.weibo.sdk.android.api.WeiboAPI not found");
				}
				AccessTokenKeeper.keepAccessToken(context, accessToken);
				shareXinLang();

			}
		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onError(WeiboDialogError arg0) {
			Toast.makeText(context, "Auth cancel", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			Toast.makeText(context, "Auth exception : " + arg0.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	public void shareTX(OAuthV2 oAuthV2) {
		pDialog = ProgressDialog.show(context, "", "发布中,请稍等...", true);
		new Threads(oAuthV2).start();
	}

	class Threads extends Thread {
		private OAuthV2 oAuthV2;

		public Threads(OAuthV2 oAuthV2) {
			this.oAuthV2 = oAuthV2;
		}

		@Override
		public void run() {
			message = new Message();
			String response=null;
			TAPI tAPI = null;
			tAPI = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
			try {
				response=tAPI.addPic(oAuthV2, "json", textString, "127.0.0.1", imagePath);
				tAPI.shutdownConnection();
				if (null!=response) {
					message.what = SUCCESS;
					handler.sendMessage(message);
				}
				else {
					message.what = IOEXCEPTION;
					handler.sendMessage(message);
				}
			} catch (Exception e) {
				message.what = IOEXCEPTION;
				handler.sendMessage(message);
			}
			
		
			super.run();
		}
	}

	private void shareXinLang() {
		pDialog = ProgressDialog.show(context, "发布中", "请稍等...", true);
		StatusesAPI api = new StatusesAPI(accessToken);
		if (textString.length() >= 140) {
			Toasts.toast(context, "字数不能大于140字符");
			return;
		}
		api.upload(textString, imagePath, "190.0", "190.0",
				new RequestListener() {
					@Override
					public void onIOException(IOException arg0) {
						message = new Message();
						message.what = IOEXCEPTION;
						handler.sendMessage(message);
						Log.i("hck", arg0.toString());
					}

					@Override
					public void onError(WeiboException arg0) {
						message = new Message();
						message.what = ERROR;
						handler.sendMessage(message);
						Log.i("hck", arg0.toString());
					}

					@Override
					public void onComplete(String arg0) {
						message = new Message();
						message.what = SUCCESS;
						handler.sendMessage(message);
					}
				});
	}

}
