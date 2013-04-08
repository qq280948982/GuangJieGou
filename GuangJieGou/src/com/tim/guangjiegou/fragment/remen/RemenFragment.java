package com.tim.guangjiegou.fragment.remen;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;
import com.tim.guangjiegou.activity.ShopItemDetailActivity;
import com.tim.guangjiegou.activity.WebViewActivity;
import com.tim.guangjiegou.fragment.TabContentFragment;
import com.tim.guangjiegou.pojo.RemenShopList;
import com.tim.guangjiegou.pojo.Shop;
import com.tim.guangjiegou.pojo.ShopItem;
import com.tim.guangjiegou.service.APIManager;
import com.tim.guangjiegou.view.ItemHeaderView.OnClickRemenHeaderListener;
import com.tim.guangjiegou.view.PriceFlowView;
import com.tim.guangjiegou.view.ShopFlowView;
import com.youxilua.waterfall.WaterFallOption;
import com.youxilua.waterfall.WaterFallView;
import com.youxilua.waterfall.WaterFallView.OnScrollListener;
import com.youxilua.waterfall.item.FlowViewHandler;

public class RemenFragment extends TabContentFragment 
	implements OnScrollListener, OnClickListener, OnClickRemenHeaderListener {
	
	private WaterFallView mWaterFallView;
	private LinearLayout mWaterFallContainer;
	private FlowViewHandler mFlowViewHandler;
	private int mItemWidth;
	private int mColumnCount = 3;
	
	private boolean mCurrentIsRemenItem;

	@Override
	protected int getModuleTitleRes() {
		return R.string.tab_renmen;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		getItemHeaderView().showHeaderForRemen();
		getItemHeaderView().setOnClickRemenHeaderListener(this);
		mCurrentIsRemenItem = true;
		return view;
	}
	
	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.page_remen, container, false);
	}
	
	@Override
	public void onFragmentDidAppear() {
		super.onFragmentDidAppear();
		if(getItemHeaderView() != null) {
			getItemHeaderView().showHeaderForRemen();
		}
	}

	@Override
	protected void findViews() {
		mItemWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth() / mColumnCount;
		
		mWaterFallView = (WaterFallView) findViewById(R.id.page_remen_waterfall_scroll);
		mWaterFallContainer = (LinearLayout) findViewById(R.id.page_remen_waterfall_container);
		mFlowViewHandler = new FlowViewHandler(mWaterFallView);
		WaterFallOption options = new WaterFallOption(mWaterFallContainer, mItemWidth, mColumnCount);
		mWaterFallView.commitWaterFall(options, mWaterFallView);
	}

	@Override
	protected void setupListeners() {
		mWaterFallView.setOnScrollListener(this);
		
		addItemsToContainer(mWaterFallView.current_page);
	}
	
	private void addItemsToContainer(int page) {
//		int currentIndex = page * pageCount;
//		int imagecount = mWaterFallView.pictureTotalCount;
		
		List<ShopItem> shopItemList = APIManager.getInstance().listRemenShopItems();
		for(int i = 0; i < shopItemList.size(); i++) {
			mWaterFallView.loaded_count++;
			ShopItem shopItem = shopItemList.get(i);
			AddItem(shopItem, 
					(int) Math.ceil(mWaterFallView.loaded_count / (double) mColumnCount), mWaterFallView.loaded_count);
		}
		
	}
	
	private void AddItem(ShopItem shopItem, int rowIndex, int id) {
		PriceFlowView item = new PriceFlowView(getTabActivity());
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

	private void addShopsToContainer(int page) {
		RemenShopList shopListObj = APIManager.getInstance().listRemenShopList();
		final int size = shopListObj.getShopList().size();
		for(int i = 0; i < size; i++) {
			Shop shop = shopListObj.getShop(i);
			AddShop(shop, (int) Math.ceil(mWaterFallView.loaded_count / (double) mColumnCount), mWaterFallView.loaded_count);
		}
	}
	
	private void AddShop(Shop shop, int rowIndex, int id) {
		ShopFlowView item = new ShopFlowView(getTabActivity());
		item.setPadding(1, 1, 1, 1);
		item.setRowIndex(rowIndex);
		item.setId(id);
		item.setViewHandler(mFlowViewHandler);
		item.setFileName(shop.getShopLogo());
		item.setItemWidth(mItemWidth);
		item.setTitle(shop.getTitle());
		item.setShop(shop);
		item.loadImage();
		item.setOnClickListener(this);
	}
	
	@Override
	public void onBottom() {
//		addItemsToContainer(++mWaterFallView.current_page);
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
			Intent intent = new Intent(getTabActivity(), ShopItemDetailActivity.class);
			intent.putExtra(Constants.EXTRAS_SHOP_ITEM, ((PriceFlowView) v).getShopItem());
			getTabActivity().startActivity(intent);
		}
		else if(v instanceof ShopFlowView) {
			ShopFlowView flowView = (ShopFlowView) v;
			Shop shop = flowView.getShop();
			if(shop != null && shop.getNick() != null) {
				Intent intent = new Intent(getTabActivity(), WebViewActivity.class);
				intent.putExtra(Constants.EXTRAS_TAOBAO_SHOP_SELLER_NICK, shop.getNick());
				intent.putExtra(Constants.EXTRAS_TAOBAO_SHOP_TITLE, shop.getTitle());
				getTabActivity().startActivity(intent);
			}
		}
	}

	@Override
	public void onClickRemenHeader(View v, boolean remenItem) {
		if(remenItem != mCurrentIsRemenItem) {
			mFlowViewHandler.destroy();
			mFlowViewHandler = new FlowViewHandler(mWaterFallView);
			mCurrentIsRemenItem = remenItem;
			mWaterFallView.reset();
			if(remenItem) {
				addItemsToContainer(mWaterFallView.current_page);
			}
			else {
				addShopsToContainer(mWaterFallView.current_page);
			}
		}
	}

}
