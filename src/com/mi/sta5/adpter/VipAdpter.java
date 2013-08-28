package com.mi.sta5.adpter;

import com.mi.sta5.ui.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class VipAdpter extends BaseAdapter {
	private int vipButton[];    //保存图片id
	private Context context;

	public VipAdpter(int[] vipButton, Context context) {
		this.vipButton = vipButton;
		this.context = context;
	}

	@Override
	public int getCount() {
		return vipButton.length;
	}

	@Override
	public Object getItem(int position) {
		return vipButton[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
	    LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.vip_item, null);
		InitView.button = (ImageView) view.findViewById(R.id.vip_button);
		InitView.button.setBackgroundResource(vipButton[position]);
		return view;
	}

	public static class InitView {
		public static ImageView button;
	}

}
