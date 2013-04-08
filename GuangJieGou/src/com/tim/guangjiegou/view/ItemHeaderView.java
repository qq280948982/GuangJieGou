package com.tim.guangjiegou.view;

import lib.tim.view.OhTextView;
import lib.tim.view.header.OhHeader;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tim.guangjiegou.R;
import com.tim.guangjiegou.activity.TabActivity;

public class ItemHeaderView extends OhHeader {
	
	private OhTextView mTitle;
	private OhTextView mBackBtn;
	private OhTextView mEditBtn;
	private View mHomeBtn;

	private OnClickRemenHeaderListener mOnClickRemenHeaderListener;
	private View mHeaderForRemenLayout;
	private TextView mHeaderForRemenItemText;
	private View mHeaderForRemenItemBorder;
	private TextView mHeaderForRemenShopText;
	private View mHeaderForRemenShopBorder;
	
	private TabActivity mTabActivity;
	
	private OnClickHeaderButtonsListener mOnClickHeaderButtonsListener;

	public ItemHeaderView(final Context context, AttributeSet attrs) {
		super(context, attrs);

//		super.addLeftButtonIds(new int[]{R.id.item_header_back_btn,R.id.item_header_location_btn, R.id.item_header_edit_btn});
//		super.addRightButtonIds(new int[]{R.id.item_header_home_btn});
//		super.setBackButtonId(R.id.item_header_back_btn);
		
		mTitle = (OhTextView) findViewById(R.id.item_header_title);
		
		mHeaderForRemenLayout = findViewById(R.id.item_header_for_remen);
		mHeaderForRemenItemText = (TextView) findViewById(R.id.item_header_for_remen_item);
		mHeaderForRemenItemBorder = findViewById(R.id.item_header_for_remen_item_border);
		mHeaderForRemenShopText = (TextView) findViewById(R.id.item_header_for_remen_shop);
		mHeaderForRemenShopBorder = findViewById(R.id.item_header_for_remen_shop_border);
		
		mHeaderForRemenItemText.setOnClickListener(this);
		mHeaderForRemenShopText.setOnClickListener(this);
		
//		mHomeBtn = findViewById(R.id.item_header_home_btn);
//		mLocationBtn = findViewById(R.id.item_header_location_btn);
//		mBackBtn = (OhTextView) findViewById(R.id.item_header_back_btn);
//		mEditBtn = (OhTextView) findViewById(R.id.item_header_edit_btn);
		
		if(context instanceof TabActivity) {
			mTabActivity = (TabActivity) context;
		}
		
//		if(mBackBtn != null) {
//			mBackBtn.setVisibility(View.INVISIBLE);	//Disable back button
//		}
	}
	
	@Override
	protected int getResLayout() {
		return R.layout.item_header;
	}

	public void setBackButtonValid(boolean isBackable) {
		
	}
	
	public void setTitle(int resId) {
		if(resId > 0) {
			mTitle.setText(resId);
		}
	}
	
	public void setTitle(String title) {
		if(mTitle != null) {
			mTitle.setText(title);
		}
	}
	
	public void showHomeBtn() {
		setHomeBtnVisibility(View.VISIBLE);
	}
	
	public void hideHomeBtn() {
		setHomeBtnVisibility(View.GONE);
	}
	
	private void setHomeBtnVisibility(int visibility) {
		if(mHomeBtn != null) {
			mHomeBtn.setVisibility(visibility);
		}
	}
	
	public void showEditButton() {
		super.showButton(mEditBtn, true);
	}
	
	public void hideEditButton() {
		super.hideButton(mEditBtn, true);
	}
	
	@Override
	public void showButton(View view,boolean isLeftOrRight) {
		if(view == mBackBtn) {
			return ;	//Disable back button.
		}
		super.showButton(view, isLeftOrRight);
	}
	
	@Override
	public synchronized void onClick(View view) {
		if(view.getId() == mHeaderForRemenItemText.getId()) {
			selectRemen(true);
			if(mOnClickRemenHeaderListener != null) {
				mOnClickRemenHeaderListener.onClickRemenHeader(view, true);
			}
			return ;
		}
		if(view.getId() == mHeaderForRemenShopText.getId()) {
			selectRemen(false);
			if(mOnClickRemenHeaderListener != null) {
				mOnClickRemenHeaderListener.onClickRemenHeader(view, false);
			}
			return ;
		}
		super.onClick(view);
	}
	
	private void selectRemen(boolean remenItem) {
		final Resources res = getContext().getResources();
//		mHeaderForRemenItemText.setTextColor(remenItem ? res.getColor(R.color.orange) : res.getColor(R.color.black));
		mHeaderForRemenItemBorder.setBackgroundColor(remenItem ? res.getColor(R.color.white) : res.getColor(R.color.orange));
		
//		mHeaderForRemenShopText.setTextColor(remenItem ? res.getColor(R.color.black) : res.getColor(R.color.orange));
		mHeaderForRemenShopBorder.setBackgroundColor(remenItem ? res.getColor(R.color.orange) : res.getColor(R.color.white));
	}

	@Override
	protected void onClickHeaderButton(View buttonView) {
		
		if(mOnClickHeaderButtonsListener != null) {
			mOnClickHeaderButtonsListener.onClickHeaderButton(buttonView);
		}
		
		/*
		int viewId=buttonView.getId();
		if(viewId==R.id.item_header_home_btn){
			if(mTabActivity != null) {
				mTabActivity.showHomePage();
			}
		}
		else if(viewId == R.id.item_header_location_btn) {
			if(mTabActivity != null) {
				ClipFragment currentFragment = mTabActivity.getGlobalTabContainerFragment().getTabManager().getLastDisplayedFragment();
				if(currentFragment instanceof BranchAtmFragment) {
					BranchAtmFragment atmFragment = (BranchAtmFragment) currentFragment;
					ClipInfo info = new ClipInfo();
					final Hashtable<String, Object> bundle = new Hashtable<String, Object>();
					info.setEnterFlag(ClipInfo.ENTERTYPE_NEXT);
					info.setDestFragment(MapFragment.class);
					info.setName(TabIdentify.Map.toString());
					info.setEnterAnimationResId(R.anim.right_in);
					info.setExitAnimationResId(R.anim.left_out);
					bundle.put(Constants.EXTRA_CURRENT_DISTRICT_INDEX, atmFragment.getCurrentRegionIndex());
					bundle.put(Constants.EXTRA_CURRENT_ATM_TYPE_INDEX, atmFragment.getCurrentAtmTypeIndex());
					info.setRequestParameter(bundle);
					mTabActivity.getGlobalTabContainerFragment().showNextContentByClipInfo(info);
				}
			}
		}
		else if(viewId==R.id.item_header_edit_btn){
			if(mTabActivity != null) {
				ClipFragment currentFragment = mTabActivity.getGlobalTabContainerFragment().getTabManager().getLastDisplayedFragment();
				if(currentFragment instanceof MortgageFragment) {
					final boolean isSelected = mEditBtn.isSelected();
					final int textRes = isSelected ? R.string.btn_edit : R.string.btn_done;
					mEditBtn.setTextRes(textRes);
					mEditBtn.setSelected(!isSelected);
					((MortgageFragment) currentFragment).editMyWatchList(!isSelected);
				}
				else {
					hideEditButton();
				}
			}
			else {
				hideEditButton();
			}
		}
		*/
	}

	public void updateByLanguageChanged() {
		if(mTitle != null) {
			mTitle.updateText();
		}
		if(mBackBtn != null) {
			mBackBtn.updateText();
		}
		if(mEditBtn != null) {
			mEditBtn.updateText();
		}
	}
	
	public void setTitleVisble(boolean visible) {
		if(mTitle != null) {
			mTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
		}
	}
	
	public void showCommonTitleHeader() {
		showHeader(true);
	}
	
	public void showHeaderForRemen() {
		showHeader(false);
	}
	
	private void showHeader(boolean common) {
		setTitleVisble(common);
		if(mHeaderForRemenLayout != null) {
			mHeaderForRemenLayout.setVisibility(common ? View.GONE : View.VISIBLE);
		}
	}

	public void release() {
		mTabActivity = null;
	}
	
	public void setOnClickRemenHeaderListener(OnClickRemenHeaderListener l) {
		mOnClickRemenHeaderListener = l;
	}
	
	public interface OnClickRemenHeaderListener {
		void onClickRemenHeader(View v, boolean remenItem);
	}

	public void setOnClickHeaderButtonsListener(OnClickHeaderButtonsListener l) {
		mOnClickHeaderButtonsListener = l;
	}
	
	public interface OnClickHeaderButtonsListener {
		void onClickHeaderButton(View v);
	}
}
