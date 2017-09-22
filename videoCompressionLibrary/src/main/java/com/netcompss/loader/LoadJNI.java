package com.netcompss.loader;


import com.adstringo.android_client.Prefs;

import android.content.Context;
import android.util.Log;

public final class LoadJNI {

	static {
		System.loadLibrary("loader-jni");
	}

	/**
	 * 
	 * @param args command
	 * @param videokitSdcardPath working directory 
	 * @param ctx Android context
	 */
	public void run(String[] args, String videokitSdcardPath, Context ctx) {
		load(args, videokitSdcardPath, getVideokitLibPath(ctx), Prefs.isComplex(ctx));
	}

	private static String getVideokitLibPath(Context ctx) {
		String videokitLibPath = ctx.getFilesDir().getParent()  + "/lib/libvideokit.so";
		Log.i(Prefs.TAG, "videokitLibPath: " + videokitLibPath);
		return videokitLibPath;

	}

	public void fExit( Context ctx) {
		fexit(getVideokitLibPath(ctx));
	}

	public native String fexit(String videokitLibPath);

	public native String load(String[] args, String videokitSdcardPath, String videokitLibPath, boolean isComplex);
}
