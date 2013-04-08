package com.tim.guangjiegou.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tim.guangjiegou.R;
import com.tim.guangjiegou.pojo.Shop;
import com.tim.guangjiegou.util.Base64Util;
import com.tim.guangjiegou.util.CommonUtil;
import com.tim.guangjiegou.util.ImageCache;
import com.tim.guangjiegou.util.ImageCache.Options;
import com.youxilua.waterfall.Constants;
import com.youxilua.waterfall.item.FlowView;

public class ShopFlowView extends FlowView {
	
	private TextView mTitleView;
	private ItemImageView mItemImageView;
	private Shop mShop;

	public ShopFlowView(Context c) {
		super(c);
		
		int screenWidth = CommonUtil.getScreenWidth(c);
		mTitleView = (TextView) findViewById(R.id.item_shop_flow_view_title);
		mItemImageView = (ItemImageView) findViewById(getImageViewId());
		ImageView imageView = (ImageView) findViewById(getImageViewId());
		
		if(imageView != null) {
			int imageSize = screenWidth / 3 - 10;
			imageView.setLayoutParams(new LayoutParams(imageSize, imageSize));
		}
		if(mTitleView != null) {
			int titleWidth = screenWidth / 3 - 10;
			ViewGroup.LayoutParams lp = mTitleView.getLayoutParams();
			lp.width = titleWidth;
			mTitleView.setLayoutParams(lp);
		}
	}

	@Override
	public View getView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.item_shop_flow_view, null);
		return view;
	}

	@Override
	public int getImageViewId() {
		return R.id.item_shop_flow_view_image;
	}
	
	
	public void setTitle(String title) {
		if(mTitleView != null) {
			mTitleView.setText(title);
		}
	}
	
	public Shop getShop() {
		return mShop;
	}

	public void setShop(Shop shop) {
		this.mShop = shop;
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
							ItemWidth, 150, ShopFlowView.this);
					h.sendMessage(m);
				}
			});
		}
	}

	@Override
	public void reload() {
		if(mItemImageView != null) {
			mItemImageView.loadImageFromFileName();
		}
	}
}
