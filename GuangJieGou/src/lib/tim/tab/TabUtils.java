package lib.tim.tab;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

class TabUtils {
	
	private static final String TAG = "TabUtils";

	private static int SCREEN_SIZE_1 = -1;
	private static int SCREEN_SIZE_2 = -1;
	
	public static int getScreenWidth(Context context) {
		if (SCREEN_SIZE_1 <= 0 || SCREEN_SIZE_2 <= 0) {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			if(wm != null) {
				wm.getDefaultDisplay().getMetrics(dm);
			}
			SCREEN_SIZE_1 = dm.widthPixels;
			SCREEN_SIZE_2 = dm.heightPixels;
		}

		Configuration conf = context.getResources().getConfiguration();
		switch (conf.orientation) {
		case Configuration.ORIENTATION_LANDSCAPE:
			return SCREEN_SIZE_1 > SCREEN_SIZE_2 ? SCREEN_SIZE_1 : SCREEN_SIZE_2;
		case Configuration.ORIENTATION_PORTRAIT:
			return SCREEN_SIZE_1 < SCREEN_SIZE_2 ? SCREEN_SIZE_1 : SCREEN_SIZE_2;
		default:
			Log.e(TAG, "can't get screen width!");
		}
		return SCREEN_SIZE_1;
	}

	public static int getScreenHeight(Context context) {
		if (SCREEN_SIZE_1 <= 0 || SCREEN_SIZE_2 <= 0) {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(dm);
			SCREEN_SIZE_1 = dm.widthPixels;
			SCREEN_SIZE_2 = dm.heightPixels;
		}

		Configuration conf = context.getResources().getConfiguration();
		switch (conf.orientation) {
		case Configuration.ORIENTATION_LANDSCAPE:
			return SCREEN_SIZE_1 < SCREEN_SIZE_2 ? SCREEN_SIZE_1 : SCREEN_SIZE_2;
		case Configuration.ORIENTATION_PORTRAIT:
			return SCREEN_SIZE_1 > SCREEN_SIZE_2 ? SCREEN_SIZE_1 : SCREEN_SIZE_2;
		default:
			Log.e(TAG, "can't get screen height!");
		}
		return SCREEN_SIZE_1;
	}
	
	/**
	 * dip单位值转成px单位值
	 * 
	 * @param px单位的值
	 * @return dip单位的值
	 */
	public static float px2dip(Context context, int px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (px - 0.5f) / scale;
	}

	/**
	 * dip单位值转成px单位值
	 * 
	 * @param dip
	 *            dip单位的值
	 * @return px单位的值
	 */
	public static int dip2px(Context context, int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}
	
	public static int getPxValue(Context context, int tabConfigValue, int defaultValue) {
		return tabConfigValue > 0 ? tabConfigValue : dip2px(context, defaultValue);
	}

	public static int getValue(int configValue, int defaultValue) {
		return configValue > 0 ? configValue : defaultValue;
	}
	
}
