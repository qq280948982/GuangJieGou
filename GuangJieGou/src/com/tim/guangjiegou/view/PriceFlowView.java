package com.tim.guangjiegou.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tim.guangjiegou.R;
import com.tim.guangjiegou.pojo.ShopItem;
import com.tim.guangjiegou.util.Base64Util;
import com.tim.guangjiegou.util.CommonUtil;
import com.tim.guangjiegou.util.ImageCache.Options;
import com.youxilua.waterfall.Constants;
import com.youxilua.waterfall.item.FlowView;

public class PriceFlowView extends FlowView {
	
	private TextView mPriceView;
	private ItemImageView mItemImageView;
	private ShopItem mShopItem;

	public PriceFlowView(Context c) {
		super(c);
		
		mPriceView = (TextView) findViewById(R.id.item_price_flow_view_price);
		mItemImageView = (ItemImageView) findViewById(getImageViewId());
	}

	@Override
	public View getView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.item_price_flow_view, null);
		return view;
	}

	@Override
	public int getImageViewId() {
		return R.id.item_price_flow_view_image;
	}
	
	public void setPrice(double price) {
		setPrice(String.valueOf(price));
	}
	
	public void setPrice(String price) {
		if(mPriceView != null) {
			mPriceView.setText(price);
		}
	}
	
	public void setShopItem(ShopItem shopItem) {
		mShopItem = shopItem;
	}

	public ShopItem getShopItem() {
		return mShopItem;
	}

	@Override
	public void setFileName(String fileName) {
		if(CommonUtil.isNull(fileName)) {
			return ;
		}
		super.setFileName(fileName);
		if(mItemImageView != null) {
			Options options = new Options();
			options.width = ItemWidth;
			mItemImageView.setFileName(fileName, Base64Util.encode(fileName.getBytes()), options);
		}
	}
	
	@Override
	public void loadImage() {
		if(mItemImageView != null && viewHandler != null) {
			viewHandler.post(new Runnable() {
				
				@Override
				public void run() {
					mItemImageView.loadImageFromFileName();
					Handler h = getViewHandler();
					Message m = h.obtainMessage(Constants.HANDLER_WHAT,
							ItemWidth, 150, PriceFlowView.this);
					h.sendMessage(m);
				}
			});
		}
	}

	@Override
	public void reload() {
		if(mItemImageView != null && viewHandler != null) {
			viewHandler.post(new Runnable() {
				
				@Override
				public void run() {
					viewHandler.sendEmptyMessage(1);
					mItemImageView.loadImageFromFileName();
				}
			});
		}
	}
}
