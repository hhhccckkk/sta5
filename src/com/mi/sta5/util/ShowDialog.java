package com.mi.sta5.util;
import android.app.Activity;
/**
 * 
 * @author kevin
 * @Description 彈出選擇提示框
 */
import android.content.DialogInterface;
public class ShowDialog implements android.content.DialogInterface.OnClickListener{
	private Activity activity;
    public  ShowDialog(Activity activity)
    {
    	this.activity=activity;
    }
	@Override
	public void onClick(DialogInterface dialog, int which) {
    		activity.finish();
	}

}
