package com.tim.guangjiegou.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tim.guangjiegou.R;
import com.tim.guangjiegou.pojo.DaPeiItem;
import com.tim.guangjiegou.util.Base64Util;

public class ItemDaPeiListItem extends LinearLayout {
	
	private TextView mTitle;
	private TextView mCreateDate;
	private ItemImageView mFrontCover;
	private TextView mDescription;
	
	private DaPeiItem mDaPeiItem;

	public ItemDaPeiListItem(Context context) {
		super(context);
		initComponent();
	}

	private void initComponent() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.item_dapei, this);
		
		mTitle = (TextView) findViewById(R.id.item_dapei_title);
		mCreateDate = (TextView) findViewById(R.id.item_dapei_create_date);
		mFrontCover = (ItemImageView) findViewById(R.id.item_dapei_front_cover);
		mDescription = (TextView) findViewById(R.id.item_dapei_description);
		
		mFrontCover.setScaleTypeAsTopCrop(true);
	}
	
	public void setData(DaPeiItem dapeiItem) {
		mDaPeiItem = dapeiItem;
		if(mDaPeiItem != null) {
			mTitle.setText(mDaPeiItem.getTitle());
			mCreateDate.setText(mDaPeiItem.getCreate_date());
			mFrontCover.setFileName(mDaPeiItem.getFront_cover_url(), Base64Util.encode(mDaPeiItem.getFront_cover_url().getBytes()));
			mDescription.setText(mDaPeiItem.getDescription());
			
			mFrontCover.loadImageFromFileName();
		}
	}

}
