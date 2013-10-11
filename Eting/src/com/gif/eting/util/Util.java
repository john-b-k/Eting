package com.gif.eting.util;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.gif.eting_dev.R;

/**
 * 상수값 모음
 * 
 * @author lifenjoy51
 * 
 */
public class Util {
	public static String serverContext = "http://eting.cafe24.com/eting"; // 서버
	// public static String serverContext = "http://192.168.123.123:8080/eting";
	// //로컬
	public static int mainRatio = 82;
	public static long fps = 20;
	public static Typeface nanum;
	public static Drawable planetDrawable;

	public static void init(Context context) {
		nanum = Typeface.createFromAsset(context.getAssets(),
				"fonts/NanumGothic.ttf");
		planetDrawable = context.getResources().getDrawable(
				R.drawable.main_planet);
	}
}