package com.tim.guangjiegou.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tim.guangjiegou.R;
import com.tim.guangjiegou.util.CommonUtil;

public class ItemTabItem extends LinearLayout {
	
	private static final String TAG = "ItemTabItem";
	
	private ImageView mIcon;
	private TextView mText;
	
	private int mTextRes;
	private int mDrawableRes;
	
//	private TabInfo mTabInfo;
	
	private int mTabIndex;
	
	public ItemTabItem(Context context) {
		super(context);
		initComponent();
	}
	
	public ItemTabItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initComponent();
	}	
	
	private void initComponent() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_tab_item, this);
		mIcon = (ImageView) findViewById(R.id.item_tab_item_img);
		mText = (TextView) findViewById(R.id.item_tab_item_text);
	}
	
	public int getTextResId() {
		return mTextRes;
	}
	
	public void setText(int resId) {
		mTextRes = resId;
		mText.setText(mTextRes);
		final String text = mText.getText().toString();
//		if(CommonUtil.getScreenWidth(getContext()) <= 480) {
//			if(text.length() >= 14) {
//				mText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
//			}
//			else {
//				mText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
//			}
//		}
	}
	
	public int getDrawableResId() {
		return mDrawableRes;
	}
	
	public void setDrawable(int resId) {
		mDrawableRes = resId;
		mIcon.setImageResource(mDrawableRes);
	}
	
//	public void setData(int text, int drawable, TabIdentify tabIdentify) {
//		setText(text);
//		setDrawable(drawable);
//		setTabIdentify(tabIdentify);
//	}
	
//	public void setData(TabInfo tabInfo) {
//		if(tabInfo != null) {
//			mTabInfo = tabInfo;
//			setText(tabInfo.tabText);
//			setDrawable(tabInfo.tabDrawable);
//		}
//	}
//	
//	public TabInfo getData() {
//		return mTabInfo;
//	}
//	
//	public TabIdentify getTabIdentify() {
//		return mTabInfo != null ? mTabInfo.tabIdentify : null;
//	}

	@Override
	public void setSelected(boolean selected) {
		if(mIcon != null) {
			mIcon.setSelected(selected);
		}
		if(mText != null) {
			mText.setTextColor(selected ? Color.WHITE : Color.GRAY);
		}
//		setBackgroundResource(selected ? R.drawable.selected_highlight : 0);
	}
	
	@Override
	public boolean isSelected() {
		return mIcon != null && mIcon.isSelected();
	}
	
	@Override
	public void setPressed(boolean pressed) {
		if(mIcon != null) {
			mIcon.setPressed(pressed);
		}
	}

	@Override
	public boolean isPressed() {
		return mIcon != null && mIcon.isPressed();
	}

	public void setTabIndex(int index) {
		mTabIndex = index;
	}
	
	public int getTabIndex() {
		return mTabIndex;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		final int action = event.getAction();
		if(action == MotionEvent.ACTION_DOWN) {
			setPressed(true);
		}
		else if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			setPressed(false);
		}
		return true;
	}
}
