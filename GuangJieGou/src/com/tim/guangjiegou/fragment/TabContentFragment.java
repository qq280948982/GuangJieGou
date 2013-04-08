package com.tim.guangjiegou.fragment;

import lib.tim.fragment.TabClipFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tim.guangjiegou.activity.TabActivity;
import com.tim.guangjiegou.view.ItemHeaderView;
import com.tim.guangjiegou.view.ItemTabBarView;

public abstract class TabContentFragment extends TabClipFragment {
	
	private static final String TAG = "TabContentFragment";

	protected TabActivity mTabActivity;
	protected ItemHeaderView mItemHeaderView;
	protected ItemTabBarView mItemTabbarView;
	
	protected View mRootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mTabActivity = (TabActivity) getActivity();
		mItemHeaderView = getItemHeaderView();
		mItemTabbarView = getItemTabBarView();
		mRootView = onCreateContentView(inflater, container, savedInstanceState);
		findViews();
		setupListeners();
		if(mItemHeaderView != null) {
			mItemHeaderView.showCommonTitleHeader();
		}
		return mRootView;
	}
	
	@Override
	public void onFragmentDidAppear() {
		super.onFragmentDidAppear();
		if(mItemHeaderView != null) {
			mItemHeaderView.showCommonTitleHeader();
		}
	}
	
	protected View findViewById(int id) {
		View view = null;
		if(mRootView != null) {
			view = mRootView.findViewById(id);
		}
		return view;
	}

	public TabActivity getTabActivity() {
		if(mTabActivity == null) {
			mTabActivity = (TabActivity) getActivity();
		}
		return mTabActivity;
	}
	
	public ItemHeaderView getItemHeaderView() {
		if(mItemHeaderView == null && getTabActivity() != null) {
			mItemHeaderView = getTabActivity().getItemHeaderView();
		}
		return mItemHeaderView;
	}
	
	public ItemTabBarView getItemTabBarView() {
		if(mItemTabbarView == null && getTabActivity() != null) {
			mItemTabbarView = getTabActivity().getItemTabBarView();
		}
		return mItemTabbarView;
	}
	
	@Override
	public void onFragmentResume() {
		super.onFragmentResume();
//		getView().setBackgroundResource(R.drawable.bg);
		getItemHeaderView().showHomeBtn();
		getItemHeaderView().setTitle(getModuleTitleRes());
		getItemHeaderView().hideEditButton();
	}
	
	protected void onFragmentChanged() {
		//在Fragment切換時調用，如FragmentA切換到FragmentB，則會在切換後調用FragmentA的onFragmentChanged()方法.
		//需要在切換前做事情的Fragment可重寫父類的此方法
	}

	@Override
	public void onDestroy() {
		mTabActivity = null;
		mItemHeaderView = null;
		mItemTabbarView = null;
		super.onDestroy();
	}

	protected abstract int getModuleTitleRes();
	protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);
	protected abstract void findViews();
	protected abstract void setupListeners();
}
