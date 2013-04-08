package com.tim.guangjiegou.activity;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;
import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;
import com.tim.guangjiegou.service.SettingsManager;
import com.tim.guangjiegou.util.CommonUtil;
import com.tim.guangjiegou.view.ItemHeaderView;
import com.tim.guangjiegou.view.ItemHeaderView.OnClickHeaderButtonsListener;

public class WebViewActivity extends Activity {
	
	private final String TAG = "WebViewActivity";
	
	private ItemHeaderView mItemHeaderView;
	private WebView mWebView;
	private ProgressBar mLoadingView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_webview);
		findViews();
		loadUrl();
	}
	
	private void findViews() {
		mItemHeaderView = (ItemHeaderView) findViewById(R.id.page_webview_header);
		mWebView = (WebView) findViewById(R.id.page_webview_browser);
		mLoadingView = (ProgressBar) findViewById(R.id.page_webview_loading);
		
		mItemHeaderView.setTitle(R.string.taobao_webview_title);
		mItemHeaderView.showBackButton();
		mItemHeaderView.setOnClickHeaderButtonsListener(new OnClickHeaderButtonsListener() {
			
			@Override
			public void onClickHeaderButton(View v) {
				onKeyUp(KeyEvent.KEYCODE_BACK, null);
			}
		});
		
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.setWebViewClient(new WebViewClient() {
			
			private boolean sClick = false;
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				showLoading();
				super.onPageStarted(view, url, favicon);
				if(url.startsWith("http://s.click.taobao.com")) {
					sClick = true;
				}
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				hideLoading();
				super.onPageFinished(view, url);
				if(sClick && (url.indexOf("m.taobao.com") >= 0 || url.indexOf("tmall.com") >= 0)) {
					sClick = false;
					if(mWebView != null) {
						mWebView.clearHistory();
					}
				}
			}
		});
	}
	
	private void showLoading() {
		if(mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
		}
	}
	
	private void hideLoading() {
		if(mLoadingView != null) {
			mLoadingView.setVisibility(View.GONE);
		}
	}
	
	private void loadUrl() {
		Intent intent = getIntent();
		String url = intent.getStringExtra(Constants.EXTRAS_TAOBAO_SHOP_ITEM_URL);
		if(url != null) {
			mWebView.loadUrl(url);
		}
		else {
			final String seller_nick = intent.getStringExtra(Constants.EXTRAS_TAOBAO_SHOP_SELLER_NICK);
			final String shopTitle = intent.getStringExtra(Constants.EXTRAS_TAOBAO_SHOP_TITLE);
			if(seller_nick == null || shopTitle == null) {
				finish();
				return ;
			}
			showLoading();
			requestShopLink(3, seller_nick.trim(), shopTitle.trim());
		}
	}
	
	private void requestShopLink(final int limit, final String seller_nick, final String shopTitle) {
		if(limit - 1 < 0) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getApplication(), R.string.tips_cannot_get_shop_link, Toast.LENGTH_LONG).show();
					finish();
				}
			});
			return ;
		}
		try {
			TopAndroidClient client = TopAndroidClient.getAndroidClientByAppKey(Constants.SDK_TAOBAO_APP_KEY);
			client.addAccessToken(SettingsManager.getInstance().constructTaobaoAccessToken());
			TopParameters params = new TopParameters();
			params.setMethod("taobao.taobaoke.shops.get");
			params.addParam("pid", Constants.TAOKE_PID);
			params.addParam("keyword", shopTitle);
			params.addFields("click_url, seller_nick");
			client.api(params, SettingsManager.getInstance().getTaobaoUidLong(), new TopApiListener() {
				
				@Override
				public void onComplete(JSONObject json) {
					String url = null;
					if(json != null) {
						try {
							JSONArray items = json.getJSONObject("taobaoke_shops_get_response")
									.getJSONObject("taobaoke_shops")
									.getJSONArray("taobaoke_shop");
							if(items != null && items.length() > 0) {
								final int length = items.length();
								for(int i = 0; i < length; i++) {
									JSONObject aShop = items.getJSONObject(i);
									if(aShop.getString("seller_nick").trim().equals(seller_nick)) {
										url = aShop.getString("click_url");
										break ;
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					if(!CommonUtil.isNull(url)) {
						final String shopLink = url;
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								hideLoading();
								if(mWebView != null) {
									mWebView.loadUrl(shopLink);
								}
							}
						});
					}
					else {
						requestShopLink(limit - 1, seller_nick, shopTitle);
					}
				}
				
				@Override
				public void onError(ApiError error) {
					
				}
				
				@Override
				public void onException(Exception e) {
					
				}
				
			}, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
				return true;
			}
			finish();
		}
		return false;
	}
}
