package com.mi.sta5.service;

import com.mi.sta5.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Toasts {
   public static void toast(Context context,String textString)
   {
	   View view=LayoutInflater.from(context).inflate(R.layout.toast, null);
		TextView textView=(TextView) view.findViewById(R.id.toast_text);
		textView.setText( textString);
		Toast toast = new Toast(context);    //创建一个toast
		toast.setDuration(0);
		toast.setView(view);    //为toast设置一个view
		toast.show();
   }
}
