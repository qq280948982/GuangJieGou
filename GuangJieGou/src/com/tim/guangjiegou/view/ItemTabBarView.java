package com.tim.guangjiegou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.tim.guangjiegou.R;

public class ItemTabBarView extends LinearLayout implements OnClickListener {
	
	private static final String TAG = "ItemTabBarView";

	private ItemTabItem mTabItem1;
	private ItemTabItem mTabItem2;
	private ItemTabItem mTabItem3;
	private ItemTabItem mTabItem4;
	private ItemTabItem mTabItem5;
	private ItemTabItem mSelectedTab;
	
	private OnClickTabItemListener mListener;
	
	public ItemTabBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_tabbar, this);
		mTabItem1 = (ItemTabItem) findViewById(R.id.item_tabbar_item1);
		mTabItem2 = (ItemTabItem) findViewById(R.id.item_tabbar_item2);
		mTabItem3 = (ItemTabItem) findViewById(R.id.item_tabbar_item3);
		mTabItem4 = (ItemTabItem) findViewById(R.id.item_tabbar_item4);
		mTabItem5 = (ItemTabItem) findViewById(R.id.item_tabbar_item5);
		
		mTabItem1.setTabIndex(0);
		mTabItem2.setTabIndex(1);
		mTabItem3.setTabIndex(2);
		mTabItem4.setTabIndex(3);
		mTabItem5.setTabIndex(4);
		
		mTabItem1.setText(R.string.tab_renmen);
		mTabItem2.setText(R.string.tab_tapei);
		mTabItem3.setText(R.string.tab_guangjie);
		mTabItem4.setText(R.string.tab_shoucang);
		mTabItem5.setText(R.string.tab_shezhi);
		
		setupListeners();
	}
	
	public void setupListeners() {
		mTabItem1.setOnClickListener(this);
		mTabItem2.setOnClickListener(this);
		mTabItem3.setOnClickListener(this);
		mTabItem4.setOnClickListener(this);
		mTabItem5.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v instanceof ItemTabItem) {
			ItemTabItem item = (ItemTabItem) v;
			mSelectedTab = item;
			if(mListener != null) {
				mListener.onClickTabItem(item);
			}
		}
	}
	
	public void setSelectedTab(ItemTabItem tabItem) {
		onClick(tabItem);
	}
	
	public void clearSelected() {
		mTabItem1.setSelected(false);
		mTabItem2.setSelected(false);
		mTabItem3.setSelected(false);
		mTabItem4.setSelected(false);
		mTabItem5.setSelected(false);
	}
	
	public ItemTabItem getSelectedTab() {
		return mSelectedTab;
	}
	
	public void updateByLanguageChanged() {
		
	}
	
	public void release() {
		mListener = null;
	}

	public void setOnClickTabItemListener(OnClickTabItemListener l) {
		mListener = l;
	}
	
	public static interface OnClickTabItemListener {
		void onClickTabItem(ItemTabItem tabItem);
	}
}
