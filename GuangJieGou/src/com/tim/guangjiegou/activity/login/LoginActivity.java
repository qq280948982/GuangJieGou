package com.tim.guangjiegou.activity.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taobao.top.android.TOPUtils;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AccessToken;
import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;
import com.tim.guangjiegou.service.SettingsManager;
import com.tim.guangjiegou.util.Log;
import com.tim.guangjiegou.view.ItemHeaderView;

public class LoginActivity extends Activity {
	
	private final String TAG = "LoginActivity";
	
	protected ItemHeaderView mItemHeaderView;
	protected WebView mWebView;
	protected ProgressDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_login);
		findViews();
		
		Intent intent = getIntent();
		int loginType = intent.getIntExtra(Constants.EXTRAS_LOGIN_TYPE, -1);
		switch (loginType) {
		case Constants.LOGIN_TYPE_QQ:
			
			break;
			
		case Constants.LOGIN_TYPE_SINA_WEIBO:
			
			break;
			
		case Constants.LOGIN_TYPE_TAOBAO:
			mItemHeaderView.setTitle(R.string.login_taobao);
			loginForTaobao();
			break;
			
		case Constants.LOGIN_TYPE_TENCENT_WEIBO:
			
			break;

		default:
			finish();
			return ;
		}
	}
	
	private void findViews() {
		mItemHeaderView = (ItemHeaderView) findViewById(R.id.page_login_header);
		mWebView = (WebView) findViewById(R.id.page_login_webview);
		
		mLoadingDialog = new ProgressDialog(this);
		mLoadingDialog.setMessage("加载中...");
	}
	
	private void loginForTaobao() {
		mWebView.setWebViewClient(new TaobaoLoginWebClient());
		String url = getIntent().getStringExtra(Constants.EXTRAS_LOGIN_TAOBAO_URL);
		if(url == null) {
			finish();
			return ;
		}
		mWebView.loadUrl(url);
	}
	
	private abstract class LoginWebClient extends WebViewClient {
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			mLoadingDialog.show();
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			mLoadingDialog.dismiss();
			super.onPageFinished(view, url);
		}
	}
	
	/**
	 * 淘宝的WebViewClient
	 * @author Tim
	 *
	 */
	private class TaobaoLoginWebClient extends LoginWebClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i(TAG, "shouldOverrideUrlLoading: " + url);
			if(url != null && url.startsWith("callback:")) {
				Uri uri = Uri.parse(url);
				if(uri.getScheme().equals("callback")) {
					String error = uri.getQueryParameter("error");
					if(error == null) {
						String[] params = uri.getFragment().split("&");
						if(params != null) {
							Bundle bundle = new Bundle();
							final int paramSize = params.length;
							for(int i = 0; i < paramSize; i++) {
								String[] keyValue = params[i].split("=");
								if(keyValue == null || keyValue.length != 2) {
									continue;
								}
								bundle.putString(keyValue[0], keyValue[1]);
							}
							AccessToken accessToken = TOPUtils.convertToAccessToken(bundle);
							boolean successful = SettingsManager.getInstance().saveTaobaoAccessToken(accessToken);
							Intent intent = new Intent();
							intent.putExtra(Constants.EXTRAS_LOGIN_SUCCESSFUL, successful);
							setResult(Constants.RESULT_CODE_LOGIN, intent);
							finish();
						}
						
					}
				}
				return true;
			}
			return false;
		}
	}
}
