package com.mi.sta5.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.tencent.weibo.oauthv2.OAuthV2;

public class SaveTenXunDate {
	public static void saveDate(String accessToKen,String date,String opid,String userToKen,Context context)
	{
		SharedPreferences sPreferences=context.getSharedPreferences(Context.ACCOUNT_SERVICE,  Context.MODE_APPEND);
		Editor editor=sPreferences.edit();
		editor.putString("accessToKen", accessToKen);
		Log.i("hck", "hck"+accessToKen);
		editor.putString("time", date);
		editor.putString("opid", opid);
		editor.putString("userToKen", userToKen);
		editor.commit();
	}
	public static OAuthV2 getDate(Context context)
	{
		SharedPreferences sPreferences=context.getSharedPreferences(Context.ACCOUNT_SERVICE,  Context.MODE_APPEND);
		OAuthV2 oAuthV2=new OAuthV2();
		oAuthV2.setAccessToken(sPreferences.getString("accessToKen", null));
		oAuthV2.setExpiresIn(sPreferences.getString("time", null));
		oAuthV2.setOpenid(sPreferences.getString("opid", null));
		oAuthV2.setAuthorizeCode(sPreferences.getString("userToKen", null));
		return oAuthV2;
		
	}
}
