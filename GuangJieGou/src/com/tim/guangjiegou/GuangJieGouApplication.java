package com.tim.guangjiegou;

import android.app.Application;
import android.content.SharedPreferences;

import com.taobao.top.android.TopAndroidClient;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tim.guangjiegou.service.APIManager;
import com.tim.guangjiegou.service.SettingsManager;
import com.tim.guangjiegou.util.ImageCache;
import com.tim.guangjiegou.util.Log;

public class GuangJieGouApplication extends Application {

	private final String DEFAULT_SHARE_PREFERENCES_NAME = "GuangJieGou.sp";
	
	private SharedPreferences mDefaultSharedPreferences;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.loggagble(Constants.ENABLE_DEBUG);
		
		initTaobaoSDK();
		initQQSDK();
		
		mDefaultSharedPreferences = getSharedPreferences(DEFAULT_SHARE_PREFERENCES_NAME, MODE_WORLD_WRITEABLE);
		
		ImageCache.init(this);
		SettingsManager.init(this);
		APIManager.init(this);
	}
	
	private void initTaobaoSDK() {
		TopAndroidClient.registerAndroidClient(
				getApplicationContext(), 
				Constants.SDK_TAOBAO_APP_KEY, 
				Constants.SDK_TAOBAO_APP_SECRET, 
				Constants.SDK_TAOBAO_APP_CALL_BACK_URL);
	}
	
	private void initQQSDK() {
		IWXAPI api = WXAPIFactory.createWXAPI(getApplicationContext(), null);
		api.registerApp(Constants.SDK_QQ_APP_KEY);
	}
	
	public void doTerminate() {
		System.exit(0);
	}
	
	public SharedPreferences getDefaultSharedPreferences() {
		return mDefaultSharedPreferences;
	}
}
