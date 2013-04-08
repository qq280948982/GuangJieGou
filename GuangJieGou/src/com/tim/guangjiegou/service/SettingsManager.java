package com.tim.guangjiegou.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.taobao.top.android.auth.AccessToken;
import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.GuangJieGouApplication;
import com.tim.guangjiegou.util.CommonUtil;
import com.tim.guangjiegou.util.Log;
import com.weibo.sdk.android.Oauth2AccessToken;

public final class SettingsManager {

	private final String TAG = "SettingsManager";
	
	private GuangJieGouApplication mApplicaiton;
	
	private static SettingsManager instance = null;
	
	private SharedPreferences mSharedPreferences;
	
	private SettingsManager(GuangJieGouApplication application) {
		mApplicaiton = application;
		mSharedPreferences = mApplicaiton.getDefaultSharedPreferences();
	}
	
	public static void init(GuangJieGouApplication application) {
		if(instance == null) {
			instance = new SettingsManager(application);
		}
	}
	
	public static SettingsManager getInstance() {
		return instance;
	}
	
	public boolean saveTaobaoAccessToken(AccessToken accessToken) {
		boolean successful = false;
		if(accessToken != null && accessToken.getAdditionalInformation() != null) {
			try {
				Map<String, String> additionalInfor = accessToken.getAdditionalInformation();
				String uid = additionalInfor.get("sub_taobao_user_id");
				if(uid == null) {
					uid = additionalInfor.get("taobao_user_id");
				}
				String nick = additionalInfor.get("sub_taobao_user_nick");
				if(nick == null) {
					nick = additionalInfor.get("taobao_user_nick");
				}
				String tokenValue = accessToken.getValue();
				Editor editor = mSharedPreferences.edit();
				editor.putString(Constants.SP_KEY_TAOBAO_UID, uid);
				editor.putString(Constants.SP_KEY_TAOBAO_NICK, nick);
				editor.putString(Constants.SP_KEY_TAOBAO_TOKEN, tokenValue);
				editor.commit();
				successful = true;
			} catch(Exception e) {
				successful = false;
			}
		}
		return successful;
	}
	
	public void clearTaobaoInfo() {
		Editor editor = mSharedPreferences.edit();
		editor.remove(Constants.SP_KEY_TAOBAO_UID);
		editor.remove(Constants.SP_KEY_TAOBAO_NICK);
		editor.remove(Constants.SP_KEY_TAOBAO_TOKEN);
		editor.commit();
	}
	
	public boolean saveQQTokenInfo(JSONObject jObject) {
		boolean successful = false;
		if(jObject == null) {
			return false;
		}
		try {
			Editor editor = mSharedPreferences.edit();
			editor.putString(Constants.SP_KEY_QQ_OPEN_ID, jObject.getString("openid"));
			editor.putString(Constants.SP_KEY_QQ_TOKEN, jObject.getString("access_token"));
			editor.commit();
			successful = true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return successful;
	}
	
	public void saveQQUserInfo(JSONObject jObject) {
		if(jObject == null) {
			return ;
		}
		try {
			Editor editor = mSharedPreferences.edit();
			String nickname = jObject.getString("nickname");
			if(CommonUtil.isNull(nickname)) {
				nickname = "unknown";
			}
			editor.putString(Constants.SP_KEY_QQ_NICK, nickname);
			editor.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void clearQQInfo() {
		Editor editor = mSharedPreferences.edit();
		editor.remove(Constants.SP_KEY_QQ_NICK);
		editor.remove(Constants.SP_KEY_QQ_NUMBER);
		editor.remove(Constants.SP_KEY_QQ_TOKEN);
		editor.remove(Constants.SP_KEY_QQ_OPEN_ID);
		editor.commit();
	}
	
	public void saveSinaWeiboTokenInfo(Bundle bundle) {
		Log.d(TAG, "Sina bundle: " + bundle);
		if(bundle != null) {
			Editor editor = mSharedPreferences.edit();
			editor.putString(Constants.SP_KEY_SINA_WEIBO_UID, bundle.getString("uid"));
			editor.putString(Constants.SP_KEY_SINA_WEIBO_TOKEN, bundle.getString("access_token"));
			editor.putString(Constants.SP_KEY_SINA_WEIBO_EXPIRE_IN, bundle.getString("expires_in"));
			editor.commit();
		}
	}
	
	public void saveSinaWeiboUserInfo(String json) {
		if(json != null) {
			try {
				JSONObject jObject = new JSONObject(json);
				String nickName = jObject.getString("screen_name");
				Editor editor = mSharedPreferences.edit();
				editor.putString(Constants.SP_KEY_SINA_WEIBO_NICK, nickName);
				editor.commit();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void clearSinaWeiboInfo() {
		Editor editor = mSharedPreferences.edit();
		editor.remove(Constants.SP_KEY_SINA_WEIBO_NICK);
		editor.remove(Constants.SP_KEY_SINA_WEIBO_TOKEN);
		editor.commit();
	}
	
	public String getTaobaoNickName() {
		return mSharedPreferences.getString(Constants.SP_KEY_TAOBAO_NICK, "");
	}
	
	public String getTaobaoUid() {
		return mSharedPreferences.getString(Constants.SP_KEY_TAOBAO_UID, "");
	}
	
	public long getTaobaoUidLong() {
		try {
			return Long.parseLong(getTaobaoUid());
		} catch(NumberFormatException e) {
			return 0;
		}
	}
	
	public String getTaobaoAccessToken() {
		return mSharedPreferences.getString(Constants.SP_KEY_TAOBAO_TOKEN, "");
	}
	
	public AccessToken constructTaobaoAccessToken() {
		AccessToken accessToken = new AccessToken();
		Map<String, String> information = new HashMap<String, String>();
		information.put(AccessToken.KEY_TAOBAO_USER_ID, getTaobaoUid());
		accessToken.setAdditionalInformation(information);
		accessToken.setValue(getTaobaoAccessToken());
		return accessToken;
	}
	
	public String getQQNickName() {
		return mSharedPreferences.getString(Constants.SP_KEY_QQ_NICK, "");
	}
	
	public String getQQNumber() {
		return mSharedPreferences.getString(Constants.SP_KEY_QQ_NUMBER, "");
	}
	
	public String getQQAccessToken() {
		return mSharedPreferences.getString(Constants.SP_KEY_QQ_TOKEN, "");
	}
	
	public String getSinaWeiboUid() {
		return mSharedPreferences.getString(Constants.SP_KEY_SINA_WEIBO_UID, "");
	}
	
	public String getSinaWeiboNickName() {
		return mSharedPreferences.getString(Constants.SP_KEY_SINA_WEIBO_NICK, "");
	}
	
	public String getSinaWeiboAccessToken() {
		return mSharedPreferences.getString(Constants.SP_KEY_SINA_WEIBO_TOKEN, "");
	}
	
	public String getSinaWeiboExpireIn() {
		return mSharedPreferences.getString(Constants.SP_KEY_SINA_WEIBO_EXPIRE_IN, "");
	}
	
	public Oauth2AccessToken constructSinaWeiboOauth2AccessToken() {
		Oauth2AccessToken token = new Oauth2AccessToken();
		String tokenStr = getSinaWeiboAccessToken();
		String expireIn = getSinaWeiboExpireIn();
		token.setToken(tokenStr);
		token.setExpiresIn(expireIn);
		return token;
	}
	
	public void setQualityMode(boolean qualityMode) {
		mSharedPreferences.edit().putBoolean(Constants.SP_KEY_QUALITY_MODE, qualityMode).commit();
	}
	
	public boolean isQualityMode() {
		return mSharedPreferences.getBoolean(Constants.SP_KEY_QUALITY_MODE, false);
	}
	
	public void setAllowPushNotification(boolean on) {
		mSharedPreferences.edit().putBoolean(Constants.SP_KEY_ALLOW_PUSH_NOTIFICATION, on).commit();
	}
	
	public boolean isAllowPushNotification() {
		return mSharedPreferences.getBoolean(Constants.SP_KEY_ALLOW_PUSH_NOTIFICATION, true);
	}
}
