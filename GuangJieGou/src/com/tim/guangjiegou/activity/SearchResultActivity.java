package com.tim.guangjiegou.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;
import com.tim.guangjiegou.pojo.TaoKeShopItem;
import com.tim.guangjiegou.service.APIManager;
import com.tim.guangjiegou.util.CommonUtil;
import com.tim.guangjiegou.view.ItemHeaderView;
import com.tim.guangjiegou.view.PriceFlowView;
import com.youxilua.waterfall.WaterFallOption;
import com.youxilua.waterfall.WaterFallView;
import com.youxilua.waterfall.WaterFallView.OnScrollListener;
import com.youxilua.waterfall.item.FlowView;
import com.youxilua.waterfall.item.FlowViewHandler;

public class SearchResultActivity extends Activity 
	implements OnScrollListener, OnClickListener {
	
	private ItemHeaderView mItemHeaderView;
	private WaterFallView mWaterFallView;
	private LinearLayout mWaterFallContainer;
	private FlowViewHandler mFlowViewHandler;
	private int mItemWidth;
	private int mColumnCount = 3;
	
	private final int PAGE_SIZE = 40;
	
	private String mKeyWord;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_search_result);
		
		findViews();
		setupListeners();
		
		mKeyWord = getIntent().getStringExtra(Constants.EXTRAS_SEARCH_KEYWORD);
		if(!doSearch(mKeyWord)) {
			Toast.makeText(this, R.string.tips_search_keyword_cannot_be_null, Toast.LENGTH_LONG).show();
			finish();
		}
		
		mItemHeaderView.setTitle(mKeyWord);
	}
	
	private void findViews() {
		mItemWidth = getWindowManager().getDefaultDisplay().getWidth() / mColumnCount;
		
		mItemHeaderView = (ItemHeaderView) findViewById(R.id.page_search_result_header);
		mWaterFallView = (WaterFallView) findViewById(R.id.page_search_result_waterfall_scroll);
		mWaterFallContainer = (LinearLayout) findViewById(R.id.page_search_result_waterfall_container);
		mFlowViewHandler = new FlowViewHandler(mWaterFallView);
		WaterFallOption options = new WaterFallOption(mWaterFallContainer, mItemWidth, mColumnCount);
		mWaterFallView.commitWaterFall(options, mWaterFallView);
	}
	
	protected void setupListeners() {
		mWaterFallView.setOnScrollListener(this);
	}
	
	private boolean doSearch(final String keyword) {
		if(CommonUtil.isNull(keyword)) {
			return false;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<TaoKeShopItem> shopItemList = APIManager.getInstance().searchTaoKeShopItems(keyword, mWaterFallView.current_page);
				for(int i = 0; i < shopItemList.size(); i++) {
					mWaterFallView.loaded_count++;
					final TaoKeShopItem shopItem = shopItemList.get(i);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							AddItem(shopItem, 
									(int) Math.ceil(mWaterFallView.loaded_count / (double) mColumnCount), 
									mWaterFallView.loaded_count);
						}
					});
					
				}
			}
		}).start();
		
		return true;
	}
	
	private void AddItem(TaoKeShopItem shopItem, int rowIndex, int id) {
		PriceFlowView item = new PriceFlowView(this);
		item.setPadding(1, 1, 1, 1);
		item.setRowIndex(rowIndex);
		item.setId(id);
		item.setViewHandler(mFlowViewHandler);
		item.setFileName(shopItem.getShopItemImage());
		item.setItemWidth(mItemWidth);
		item.setPrice(shopItem.getPrices());
		item.setShopItem(shopItem);
		item.loadImage();
		item.setOnClickListener(this);
	}

	@Override
	public void onBottom() {
		if(FlowView.LoadImageThreadCount != 0 || FlowView.ReloadImageThreadCount != 0) {
			return ;
		}
		if(mWaterFallView.current_page * PAGE_SIZE < TaoKeShopItem.SearchResultCount()) {
			++ mWaterFallView.current_page;
			doSearch(mKeyWord);
		}
	}

	@Override
	public void onTop() {
		
	}

	@Override
	public void onScroll() {
		
	}

	@Override
	public void onAutoScroll(int l, int t, int oldl, int oldt) {
		
	}

	@Override
	public void onClick(View v) {
		if(v instanceof PriceFlowView) {
			PriceFlowView flowView = (PriceFlowView) v;
			TaoKeShopItem shopItem = (TaoKeShopItem) flowView.getShopItem();
			Intent intent = new Intent(this, ShopItemDetailActivity.class);
			intent.putExtra(Constants.EXTRAS_TAOKE_SHOP_ITEM, shopItem);
			startActivity(intent);
		}
	}
	
}
