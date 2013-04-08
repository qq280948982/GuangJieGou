package com.tim.guangjiegou.fragment.shezhi;

import java.io.IOException;

import lib.tim.util.OhAsyncTask;
import lib.tim.view.OhSwitchView;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.taobao.top.android.TopAndroidClient;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;
import com.tim.guangjiegou.activity.login.LoginActivity;
import com.tim.guangjiegou.fragment.TabContentFragment;
import com.tim.guangjiegou.service.NotificationCenter;
import com.tim.guangjiegou.service.NotificationCenter.NotificationObserver;
import com.tim.guangjiegou.service.SettingsManager;
import com.tim.guangjiegou.util.CommonUtil;
import com.tim.guangjiegou.util.Log;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.sso.SsoHandler;

public class SheZhiFragment extends TabContentFragment 
	implements OnClickListener, NotificationObserver {
	
	private final String TAG = "SheZhiFragment";
	
	private View mTaobaoLoginButton;
	private TextView mTaobaoLabel;
	
	private View mQQLoginButton;
	private TextView mQQLabel;
	
	private View mSinaLoginButton;
	private TextView mSinaLabel;
	
	private OhSwitchView mBrowseModeSwitch;
	private OhSwitchView mPushSettingsSwitch;

	@Override
	protected int getModuleTitleRes() {
		return R.string.tab_shezhi;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NotificationCenter.registerNotification(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		NotificationCenter.unregisterNotification(this);
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.page_shezhi, container, false);
	}

	@Override
	protected void findViews() {
		mTaobaoLoginButton = findViewById(R.id.page_shezhi_login_btn_taobao);
		mTaobaoLabel = (TextView) findViewById(R.id.page_shezhi_taobao_user);
		mQQLoginButton = findViewById(R.id.page_shezhi_login_btn_qq);
		mQQLabel = (TextView) findViewById(R.id.page_shezhi_qq_user);
		mSinaLoginButton = findViewById(R.id.page_shezhi_login_btn_sina);
		mSinaLabel = (TextView) findViewById(R.id.page_shezhi_sina_user);
		mBrowseModeSwitch = (OhSwitchView) findViewById(R.id.page_shezhi_switch_browse_mode);
		mPushSettingsSwitch = (OhSwitchView) findViewById(R.id.page_shezhi_switch_push_settings);
		
		updateUserInfo();
		loadGeneralSettings();
	}

	@Override
	protected void setupListeners() {
		mTaobaoLoginButton.setOnClickListener(this);
		mQQLoginButton.setOnClickListener(this);
		mSinaLoginButton.setOnClickListener(this);
		
		final OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SettingsManager settingsManager = SettingsManager.getInstance();
				if(buttonView.getId() == mBrowseModeSwitch.getId()) {
					settingsManager.setQualityMode(isChecked);
				}
				else if(buttonView.getId() == mPushSettingsSwitch.getId()) {
					settingsManager.setAllowPushNotification(isChecked);
				}
			}
		};
		mBrowseModeSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
		mPushSettingsSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
	}
	
	public void onClickClearImageCache(View view) {
		Log.d(TAG, "onClickClearImageCache");
	}

	@Override
	public void onClick(View v) {
		SettingsManager settingsManager = SettingsManager.getInstance();
		//淘宝
		if(v.getId() == mTaobaoLoginButton.getId()) {
			String taobaoNick = settingsManager.getTaobaoNickName();
			if(CommonUtil.isNull(taobaoNick)) {
				TopAndroidClient client = TopAndroidClient.getAndroidClientByAppKey(Constants.SDK_TAOBAO_APP_KEY);
				if(client != null) {
					String authUrl = client.getAuthorizeLink();
					Intent intent = new Intent(getTabActivity(), LoginActivity.class);
					intent.putExtra(Constants.EXTRAS_LOGIN_TYPE, Constants.LOGIN_TYPE_TAOBAO);
					intent.putExtra(Constants.EXTRAS_LOGIN_TAOBAO_URL, authUrl);
					Log.i(TAG, "authUrl >> " + authUrl);
					
					getTabActivity().startActivityForResult(intent, Constants.REQUEST_CODE_LOGIN);
				}
			}
			else {
				settingsManager.clearTaobaoInfo();
				updateUserInfo();
			}
		}
		
		//QQ
		else if(v.getId() == mQQLoginButton.getId()) {
			String qqNick = settingsManager.getQQNickName();
			if(CommonUtil.isNull(qqNick)) {
				final Tencent tencentClient = Tencent.createInstance(Constants.SDK_QQ_APP_KEY, getTabActivity());
				if(tencentClient != null) {
					tencentClient.login(getActivity(), Constants.SDK_QQ_APP_SCOPE, new IUiListener() {
						@Override
						public void onError(UiError error) {
							
						}
						
						@Override
						public void onComplete(JSONObject jsonObject) {
//							Log.d(TAG, "== onComplete ==");
//							Log.d(TAG, "json: " + jsonObject);
							SettingsManager.getInstance().saveQQTokenInfo(jsonObject);
							new OhAsyncTask() {
								
								private boolean ready() {
							        boolean ready = tencentClient.isSessionValid()
							                && tencentClient.getOpenId() != null;
							        return ready;
							    }
								
								@Override
								protected Object doInBackground(Object... parameters) {
									JSONObject responseJson = null;
									int times = 10;
									while(!ready() && --times > 0) {
										try {
											Thread.sleep(300);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
									if(ready()) {
										responseJson = tencentClient.request(com.tencent.tauth.Constants.GRAPH_SIMPLE_USER_INFO, null, com.tencent.tauth.Constants.HTTP_GET);
//										Log.e(TAG, "response: " + responseJson);
									}
									return responseJson;
								}
								
								@Override
								protected void onPostExecute(Object result) {
									super.onPostExecute(result);
									if(result != null) {
										SettingsManager.getInstance().saveQQUserInfo((JSONObject) result);
										updateUserInfo();
									}
								}
							}.execute();
						}
						
						@Override
						public void onCancel() {
							
						}
					});
				}
			}
			else {
				settingsManager.clearQQInfo();
				updateUserInfo();
			}
		}
		
		//Sina
		else if(v.getId() == mSinaLoginButton.getId()) {
			String sinaNick = settingsManager.getSinaWeiboNickName();
			if(CommonUtil.isNull(sinaNick)) {
				final Weibo sinaWeibo = Weibo.getInstance(Constants.SDK_SINA_WEIBO_APP_KEY, Constants.SDK_SINA_WEIBO_APP_CALL_BACK_URL);
				
				final WeiboAuthListener authListener = new WeiboAuthListener() {
					
					@Override
					public void onWeiboException(WeiboException exception) {
						
					}
					
					@Override
					public void onError(WeiboDialogError error) {
						
					}
					
					@Override
					public void onCancel() {
						
					}
					
					@Override
					public void onComplete(Bundle bundle) {
//						Log.e(TAG, "== onComplete ==");
//						Log.e(TAG, "Bundle >> " + bundle);
						SettingsManager.getInstance().saveSinaWeiboTokenInfo(bundle);
						UsersAPI userApi = new UsersAPI(SettingsManager.getInstance().constructSinaWeiboOauth2AccessToken());
//						Log.d(TAG, "Check User: " + SettingsManager.getInstance().getSinaWeiboUid());
						try {
							long uid = Long.parseLong(SettingsManager.getInstance().getSinaWeiboUid());
							userApi.show(uid, new RequestListener() {
								
								@Override
								public void onIOException(IOException exception) {
									Log.w(TAG, "== onIOException ==");
									exception.printStackTrace();
								}
								
								@Override
								public void onError(WeiboException exception) {
									Log.w(TAG, "== onError ==");
									exception.printStackTrace();
								}
								
								@Override
								public void onComplete(String json) {
									SettingsManager.getInstance().saveSinaWeiboUserInfo(json);
									if(getTabActivity() != null) {
										getTabActivity().runOnUiThread(new Runnable() {
											
											@Override
											public void run() {
												updateUserInfo();
											}
										});
									}
								}
							});
						} catch(NumberFormatException e) {
							
						}
					}
				};
				
				SsoHandler ssoHandler = getTabActivity().getSinaWeiboSsoHandler();
				if(ssoHandler != null) {
					ssoHandler.authorize(authListener);
				}
				else {
					sinaWeibo.authorize(getTabActivity(), authListener);
				}
				
				
			}
			else {
				settingsManager.clearSinaWeiboInfo();
				updateUserInfo();
			}
		}
	}

	@Override
	public void onReceiveNotification(int notificationCode) {
		if(notificationCode == Constants.NOTIFICATION_CODE_UPDATE_USER_INFO) {
			updateUserInfo();
		}
	}

	private void updateUserInfo() {
		//淘宝
		String taobaoNick = SettingsManager.getInstance().getTaobaoNickName();
		if(CommonUtil.isNull(taobaoNick)) {
			mTaobaoLabel.setText(getString(R.string.shezhi_taobao_login));
			mTaobaoLoginButton.setBackgroundResource(R.drawable.settings_login_btn);
		}
		else {
			mTaobaoLabel.setText(getString(R.string.shezhi_bind_user, taobaoNick));
			mTaobaoLoginButton.setBackgroundResource(R.drawable.settings_logout_btn);
		}
		
		//QQ
		String qqNick = SettingsManager.getInstance().getQQNickName();
		if(CommonUtil.isNull(qqNick)) {
			mQQLabel.setText(getString(R.string.shezhi_qq_login));
			mQQLoginButton.setBackgroundResource(R.drawable.settings_login_btn);
		}
		else {
			mQQLabel.setText(getString(R.string.shezhi_bind_user, qqNick));
			mQQLoginButton.setBackgroundResource(R.drawable.settings_logout_btn);
		}
		
		//新浪微博
		String sinaNick = SettingsManager.getInstance().getSinaWeiboNickName();
		if(CommonUtil.isNull(sinaNick)) {
			mSinaLabel.setText(getString(R.string.shezhi_sina_weibo_login));
			mSinaLoginButton.setBackgroundResource(R.drawable.settings_login_btn);
		}
		else {
			mSinaLabel.setText(getString(R.string.shezhi_bind_user, sinaNick));
			mSinaLoginButton.setBackgroundResource(R.drawable.settings_logout_btn);
		}
	}
	
	public void loadGeneralSettings() {
		boolean isQualityMode = SettingsManager.getInstance().isQualityMode();
		boolean isAllowPushNotification = SettingsManager.getInstance().isAllowPushNotification();
		mBrowseModeSwitch.setChecked(isQualityMode);
		mPushSettingsSwitch.setChecked(isAllowPushNotification);
	}
}
