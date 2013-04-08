package com.tim.guangjiegou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;
import com.tim.guangjiegou.fragment.GlobalTabContainerFragment;
import com.tim.guangjiegou.service.NotificationCenter;
import com.tim.guangjiegou.util.Log;
import com.tim.guangjiegou.view.ItemHeaderView;
import com.tim.guangjiegou.view.ItemTabBarView;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.sso.SsoHandler;

public class TabActivity extends FragmentActivity {
	
	private final String TAG = "TabActivity";
	
	private GlobalTabContainerFragment mGlobalTabContainerFragment;
	
	private SsoHandler mSinaWeiboSsoHandle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_main);
        
        FragmentManager fm = super.getSupportFragmentManager();
        mGlobalTabContainerFragment = (GlobalTabContainerFragment) fm.findFragmentById(R.id.global_tabcontainer_fragment);
        mGlobalTabContainerFragment.init(R.id.page_tab_content_container);
        
        checkSinaWeiboSsoHandler();
    }

	public ItemHeaderView getItemHeaderView() {
		return mGlobalTabContainerFragment.getItemHeaderView();
	}

	public ItemTabBarView getItemTabBarView() {
		return mGlobalTabContainerFragment.getItemTabbarView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constants.REQUEST_CODE_LOGIN) {
			boolean successful = data != null && data.getBooleanExtra(Constants.EXTRAS_LOGIN_SUCCESSFUL, false);
			if(successful) {
				NotificationCenter.sendNotification(Constants.NOTIFICATION_CODE_UPDATE_USER_INFO);
			}
			else {
				Toast.makeText(this, "授权失败！", Toast.LENGTH_SHORT).show();
			}
		}
		else if(requestCode == Constants.REQUEST_CODE_SINA_WEIBO_SSO_HANDLER) {
			Log.d(TAG, "SSO Handler activity result.");
			if(mSinaWeiboSsoHandle != null) {
				mSinaWeiboSsoHandle.authorizeCallBack(requestCode, resultCode, data);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void checkSinaWeiboSsoHandler() {
		try {
			Class.forName("com.weibo.sdk.android.sso.SsoHandler");
			Weibo sinaWeibo = Weibo.getInstance(Constants.SDK_SINA_WEIBO_APP_KEY, Constants.SDK_SINA_WEIBO_APP_CALL_BACK_URL);
			mSinaWeiboSsoHandle = new SsoHandler(this, sinaWeibo);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public SsoHandler getSinaWeiboSsoHandler() {
		return mSinaWeiboSsoHandle;
	}
	
//	@Override
//	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//		super.onActivityResult(arg0, arg1, arg2);
//	}
    
}
