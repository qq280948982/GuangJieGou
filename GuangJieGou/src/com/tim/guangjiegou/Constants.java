package com.tim.guangjiegou;

import android.os.Environment;

public final class Constants {

	public static final boolean ENABLE_DEBUG = true;
	
	/*-- Path --*/
	public static final String PATH_LOCAL_IMAGE_LIBRARY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/guangjiegou/images/";
	/*-- End Path --*/

	/*-- Url --*/
	public static final String URL_IMAGE_PREFIX = null;
	/*-- End Url --*/
	
	/*-- SDK info --*/
	public static final String SDK_TAOBAO_APP_KEY = "21436863";
	public static final String SDK_TAOBAO_APP_SECRET = "d7707a18e0521a65395dca658271cd48";
	public static final String SDK_TAOBAO_APP_CALL_BACK_URL = "callback://authroize";
	public static final String SDK_QQ_APP_KEY = "100405318";
	public static final String SDK_QQ_APP_SECRET = "f987e205d4fb6d391445c1e2f23101b4";
	public static final String SDK_QQ_APP_SCOPE = "get_simple_userinfo,get_user_profile,add_share,get_info";
	public static final String SDK_SINA_WEIBO_APP_KEY = "599998127";
	public static final String SDK_SINA_WEIBO_APP_SECRET = "5caa4aada97cc2414abc5d3185393723";
	public static final String SDK_SINA_WEIBO_APP_CALL_BACK_URL = "http://www.guangjiegou.com/authroize";
	/*-- End SDK info --*/
	
	/*-- Intent Extras --*/
	public static final String EXTRAS_LOGIN_TYPE = "EXTRAS_LOGIN_TYPE";
	public static final String EXTRAS_LOGIN_TAOBAO_URL = "EXTRAS_LOGIN_TAOBAO_URL";
	public static final String EXTRAS_LOGIN_SUCCESSFUL = "EXTRAS_LOGIN_SUCCESSFUL";
	public static final String EXTRAS_SHOP_ITEM = "EXTRAS_SHOP_ITEM";
	public static final String EXTRAS_TAOBAO_SHOP_ITEM_URL = "EXTRAS_TAOBAO_SHOP_URL";
	public static final String EXTRAS_TAOBAO_SHOP_SELLER_NICK = "EXTRAS_TAOBAO_SHOP_SELLER_NICK";
	public static final String EXTRAS_TAOBAO_SHOP_TITLE = "EXTRAS_TAOBAO_SHOP_TITLE";
	public static final String EXTRAS_TAOKE_SHOP_ITEM = "EXTRAS_TAOKE_SHOP_ITEM";
	public static final String EXTRAS_SEARCH_KEYWORD = "EXTRAS_SEARCH_KEYWORD";
	/*-- End Intent Extras --*/
	
	/*-- SharePreferences Keys --*/
	public static final String SP_KEY_TAOBAO_UID = "SP_KEY_TAOBAO_UID";
	public static final String SP_KEY_TAOBAO_NICK = "SP_KEY_TAOBAO_NICK";
	public static final String SP_KEY_TAOBAO_TOKEN = "SP_KEY_TAOBAO_TOKEN";
	public static final String SP_KEY_QQ_NUMBER = "SP_KEY_QQ_NUMBER";
	public static final String SP_KEY_QQ_NICK= "SP_KEY_QQ_NICK";
	public static final String SP_KEY_QQ_TOKEN = "SP_KEY_QQ_TOKEN";
	public static final String SP_KEY_QQ_OPEN_ID = "SP_KEY_QQ_OPEN_ID";
	public static final String SP_KEY_SINA_WEIBO_UID = "SP_KEY_SINA_WEIBO_UID";
	public static final String SP_KEY_SINA_WEIBO_NICK = "SP_KEY_SINA_WEIBO_NICK";
	public static final String SP_KEY_SINA_WEIBO_TOKEN= "SP_KEY_SINA_WEIBO_TOKEN";
	public static final String SP_KEY_SINA_WEIBO_EXPIRE_IN = "SP_KEY_SINA_WEIBO_EXPIRE_IN";
	public static final String SP_KEY_QUALITY_MODE = "SP_KEY_QUALITY_MODE";
	public static final String SP_KEY_ALLOW_PUSH_NOTIFICATION = "SP_KEY_ALLOW_PUSH_NOTIFICATION";
	/*-- End SharePreferences Keys --*/
	
	/*-- Result & Request Code --*/
	public static final int REQUEST_CODE_LOGIN = 123;
	public static final int RESULT_CODE_LOGIN = 321;
	public static final int REQUEST_CODE_SINA_WEIBO_SSO_HANDLER = 32973;	//注意，这里的RequestCode参考自新浪微博SDK源码里面的，如果版本更新了可能会有改动。
	/*-- End Result & Request Code --*/
	
	/*-- Login Type --*/
	public static final int LOGIN_TYPE_TAOBAO = 11;
	public static final int LOGIN_TYPE_QQ = 22;
	public static final int LOGIN_TYPE_SINA_WEIBO = 33;
	public static final int LOGIN_TYPE_TENCENT_WEIBO = 44;
	
	/*-- End Login Type --*/
	
	/*-- Notification Codes --*/
	public static final int NOTIFICATION_CODE_UPDATE_USER_INFO = 1001;
	/*-- End Notification Codes --*/


	/*-- Others --*/
	public static final int LOADING_IMAGE_MIN_DELAY = 300;

	public static String TAOKE_PID = "40598920";	//Tim
//	public static String TAOKE_PID = "30427671";	//JianPeng
	/*-- End Others --*/
	
}
