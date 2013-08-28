package com.mi.sta5.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import android.os.Environment;
import android.util.Log;
public class SavePicterService {
private File path=Environment.getExternalStorageDirectory();
public void saveImage(byte [] bt)
{
	File file=new File(path+"/train/");
	if (!file.exists()) {
		file.mkdir();
	}
	  File file2=new File(file,"train.jpg");
	Log.i("hck'", "(file)"+file.toString());
	try {
		OutputStream oStream=new FileOutputStream(file2);
		oStream.write(bt);
		oStream.flush();
		oStream.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
