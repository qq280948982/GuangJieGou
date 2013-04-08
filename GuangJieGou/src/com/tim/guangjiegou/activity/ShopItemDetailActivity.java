package com.tim.guangjiegou.activity;

import java.io.IOException;

import lib.tim.view.OhImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;
import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;
import com.tim.guangjiegou.pojo.ShopItem;
import com.tim.guangjiegou.pojo.TaoKeShopItem;
import com.tim.guangjiegou.service.SettingsManager;
import com.tim.guangjiegou.util.Base64Util;
import com.tim.guangjiegou.util.CommonUtil;
import com.tim.guangjiegou.util.Log;
import com.tim.guangjiegou.view.ItemHeaderView;
import com.tim.guangjiegou.view.ItemImageView;

public class ShopItemDetailActivity extends Activity {
	
	private final String TAG = "ShopItemDetailActivity";
	
	private ItemHeaderView mItemHeaderView;
	private ItemImageView mShopOwnerImage;
	private TextView mShopOwnerName;
	private ItemImageView mShopItemImage;
	private TextView mLikes;
	private TextView mPrice;
	private TextView mDetail;
	private TextView mShopItemTitle;
	private OhImageButton mReviewTab;
	private OhImageButton mShareTab;
	private OhImageButton mLikeTab;
	private OhImageButton mBuyTab;
	
	private int mScreenWidth;
	
	private boolean isNeedRequestDetail;
	private String mTaobaoShopItemId;
	private String mTaobaoShopItemLink;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_shop_item_detail);
		initComponent();
		loadData();
		if(isNeedRequestDetail) {
			requestTaobaoShopLink(3);
		}
		else {
			mDetail.setEnabled(true);
		}
	}
	
	private void initComponent() {
		mScreenWidth = CommonUtil.getScreenWidth(this);
		mItemHeaderView = (ItemHeaderView) findViewById(R.id.page_shop_item_detail_header);
		mShopOwnerImage = (ItemImageView) findViewById(R.id.page_shop_item_detail_shop_owner_img);
		mShopOwnerName = (TextView) findViewById(R.id.page_shop_item_detail_shop_owner_name);
		mShopItemImage = (ItemImageView) findViewById(R.id.page_shop_item_detail_image);
		mLikes = (TextView) findViewById(R.id.page_shop_item_detail_likes);
		mPrice = (TextView) findViewById(R.id.page_shop_item_detail_price);
		mDetail = (TextView) findViewById(R.id.page_shop_item_detail_buy);
		mShopItemTitle = (TextView) findViewById(R.id.page_shop_item_detail_title);
		mReviewTab = (OhImageButton) findViewById(R.id.page_shop_item_detail_tab_review);
		mShareTab = (OhImageButton) findViewById(R.id.page_shop_item_detail_tab_share);
		mLikeTab = (OhImageButton) findViewById(R.id.page_shop_item_detail_tab_like);
		mBuyTab = (OhImageButton) findViewById(R.id.page_shop_item_detail_tab_buy);
		
		mDetail.setEnabled(false);
		
		setShopOwnerName("");
		
		ViewGroup.LayoutParams lp = mShopItemImage.getLayoutParams();
		lp.width = mScreenWidth;
		lp.height = mScreenWidth;
		mShopItemImage.setLayoutParams(lp);
		
		setupListeners();
		
		mItemHeaderView.setTitle(R.string.item_detail_title);
	}
	
	private void setupListeners() {
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v.getId() == mBuyTab.getId() || v.getId() == mDetail.getId()) {
					if(CommonUtil.isNull(mTaobaoShopItemLink)) {
						Toast.makeText(getApplication(), R.string.tips_fetching_shop_item_link, Toast.LENGTH_SHORT).show();
					}
					else {
						Intent intent = new Intent(ShopItemDetailActivity.this, WebViewActivity.class);
						intent.putExtra(Constants.EXTRAS_TAOBAO_SHOP_ITEM_URL, mTaobaoShopItemLink);
						startActivity(intent);
					}
				}
			}
		};
		
		mDetail.setOnClickListener(listener);
		mReviewTab.setOnClickListener(listener);
		mShareTab.setOnClickListener(listener);
		mLikeTab.setOnClickListener(listener);
		mBuyTab.setOnClickListener(listener);
	}
	
	private void loadData() {
		Intent intent = getIntent();
		if(intent == null) {
			Log.w(TAG, "intent is null!");
			return ;
		}
		ShopItem item = (ShopItem) intent.getSerializableExtra(Constants.EXTRAS_SHOP_ITEM);
		TaoKeShopItem taokeItem = (TaoKeShopItem) intent.getSerializableExtra(Constants.EXTRAS_TAOKE_SHOP_ITEM);
		if(item != null) {
			isNeedRequestDetail = true;
			setBaseUI(item);
		}
		else if(taokeItem != null) {
			isNeedRequestDetail = false;
			setBaseUI(taokeItem);
			mTaobaoShopItemLink = taokeItem.getTaobaoShopItemLink();
		}
		else {
			Log.w(TAG, "Cannot receive the ShopItem !!!!!!!!");
		}
	}
	
	private void setBaseUI(ShopItem item) {
		String itemImgUrl = item.getShopItemImage();
		if(!CommonUtil.isNull(itemImgUrl)) {
			mShopItemImage.setFileName(itemImgUrl, Base64Util.encode(itemImgUrl.getBytes()));
			mShopItemImage.loadImageFromFileNameNoDelay();
		}
		mTaobaoShopItemId = item.getNum_iid();
		setShopOwnerName(item.getShopOwner());
		mPrice.setText(item.getPrices());
		mShopItemTitle.setText(item.getTitle());
	}
	
	private void setShopOwnerName(String name) {
		if(mShopOwnerName != null) {
			mShopOwnerName.setText(getString(R.string.item_detail_shop_owner, name));
		}
	}
	
	private void requestTaobaoShopLink(final int limit) {
		if(limit - 1 < 0) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getApplication(), R.string.tips_cannot_get_shop_item_link, Toast.LENGTH_LONG).show();
				}
			});
			return ;
		}
		TopAndroidClient client = TopAndroidClient.getAndroidClientByAppKey(Constants.SDK_TAOBAO_APP_KEY);
		try {
			client.addAccessToken(SettingsManager.getInstance().constructTaobaoAccessToken());
			TopParameters params = new TopParameters();
			params.setMethod("taobao.taobaoke.items.detail.get");
			params.addParam("pid", Constants.TAOKE_PID);
			params.addParam("num_iids", mTaobaoShopItemId);
			params.addFields("click_url");
			client.api(params, SettingsManager.getInstance().getTaobaoUidLong(), new TopApiListener() {
				
				@Override
				public void onException(Exception e) {
					
				}
				
				@Override
				public void onError(ApiError error) {
					
				}
				
				@Override
				public void onComplete(JSONObject json) {
					if(json != null) {
						try {
							JSONArray items = json.getJSONObject("taobaoke_items_detail_get_response")
									.getJSONObject("taobaoke_item_details")
									.getJSONArray("taobaoke_item_detail");
							if(items != null && items.length() > 0) {
								mTaobaoShopItemLink = items.getJSONObject(0).getString("click_url");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(CommonUtil.isNull(mTaobaoShopItemLink)) {
									requestTaobaoShopLink(limit - 1);
								}
								else {
									if(mDetail != null) {
										mDetail.setEnabled(true);
									}
								}
							}
						});
					}
				}
			}, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
