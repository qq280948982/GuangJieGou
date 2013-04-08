package com.tim.guangjiegou.fragment;

import lib.tim.fragment.ClipFragment;
import lib.tim.fragment.TabContainerFragment;
import lib.tim.fragment.TabFragment_DELETE;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;
import com.tim.guangjiegou.activity.TabActivity;
import com.tim.guangjiegou.fragment.dapei.DaPeiFragment;
import com.tim.guangjiegou.fragment.guangjie.GuangJieFragment;
import com.tim.guangjiegou.fragment.remen.RemenFragment;
import com.tim.guangjiegou.fragment.shezhi.SheZhiFragment;
import com.tim.guangjiegou.view.ItemHeaderView;
import com.tim.guangjiegou.view.ItemTabBarView;
import com.tim.guangjiegou.view.ItemTabBarView.OnClickTabItemListener;
import com.tim.guangjiegou.view.ItemTabItem;

public class GlobalTabContainerFragment extends TabContainerFragment implements OnClickTabItemListener {
	
	private static final String TAG = "GlobalTabContainerFragment";

	private TabActivity tabActivity;
	
	
	private ItemHeaderView mItemHeaderView;
//	private ItemFooterView mItemFooterView;
	private ItemTabBarView mItemTabBarView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.setDefaultReplaceContainerViewID(R.id.page_tab_content_container);
		
		View globalView=inflater.inflate(R.layout.page_tab, null);
		
		
		mItemHeaderView = (ItemHeaderView) globalView.findViewById(R.id.page_tab_header);
//		mItemFooterView = (ItemFooterView) globalView.findViewById(R.id.page_main_tab_footer_view);
//		mItemTabBarView = mItemFooterView.getTabBarView();
		mItemTabBarView = (ItemTabBarView) globalView.findViewById(R.id.page_tab_tabbar);
		super.setHeader(mItemHeaderView);
		
		mItemTabBarView.setOnClickTabItemListener(this);
		
		return globalView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tabActivity=(TabActivity)getActivity();
		
//		if(savedInstanceState == null) {
//			showHomePage();
//		}
	}
	
	public TabActivity getTabActivity(){
		return tabActivity;
	}
	
//	private synchronized void showTabContent(String tabID,String pageName,final Class<? extends TabClipFragment> cls){
//		showTabContent(tabID,pageName,cls, null,false);
//	}
//	
//	private synchronized void showTabContent(String tabID,String pageName,final Class<? extends TabClipFragment> cls,
//			Hashtable<String,Object> requestParameter,boolean isClearPagesInSameTab){
//		ClipInfo selectionInfo=new ClipInfo();
//		selectionInfo.setName(pageName);
//		selectionInfo.setDestFragment(cls);
//		selectionInfo.setRequestParameter(requestParameter);
//		
//		selectionInfo.setEnterFlag(ClipInfo.ENTERTYPE_FROM_HISTORY);
//		
//		showPageBySelectionInfo(tabID,selectionInfo);
//	}
//	
//	public synchronized void showPageBySelectionInfo(String tabID,ClipInfo clipInfo){
//		if(clipInfo==null){
//			return;
//		}
//		
//		Class<? extends TabContentFragment> destFragment=(Class<? extends TabContentFragment>)clipInfo.getDestFragment();
//		if(destFragment==null){
//			return;
//		}
//		int replaceContainerViewID=clipInfo.getReplaceContainerViewID();
//		if(replaceContainerViewID==0){
//			replaceContainerViewID=ClipInfo.getDefaultReplaceContainerViewID();
//		}
//		if(replaceContainerViewID==0){
//			return;
//		}
//		
//		int enterFlag=clipInfo.getEnterFlag();
//		
//		switch(enterFlag){
//		case ClipInfo.ENTERTYPE_NEXT:
//			tabManager.handleAccessNextClip(tabID, clipInfo);
//			break;
//		case ClipInfo.ENTERTYPE_FROM_HISTORY:
//			tabManager.handleAccessClipInHistory(tabID, clipInfo);
//			break;
//		}
//
//		boolean isBackable=mPageManager.hasPreviousPage(tabID);
//		mItemHeaderView.setBackButtonValid(isBackable);
//	}
//
//	public synchronized void showNextPageBySelectionInfo(SelectionInfo selectionInfo){
//		if(selectionInfo==null){
//			return;
//		}
//		String selectedTabID=mItemFooterView.getSelectedTabID();
//		showPageBySelectionInfo(selectedTabID, selectionInfo);
//	}
//	
//	public synchronized void backPreviousPage(){
//		String selectedTabID=mItemFooterView.getSelectedTabID();
//		mPageManager.handleBackPreviousPage(selectedTabID);
//		boolean isBackable=mPageManager.hasPreviousPage(selectedTabID);
//		mItemHeaderView.setBackButtonValid(isBackable);
//	}
	
	public ItemHeaderView getItemHeaderView() {
		return mItemHeaderView;
	}

	public ItemTabBarView getItemTabbarView() {
		return mItemTabBarView;
	}

	@Override
	public void onClickTabItem(ItemTabItem tabItem) {
		if(tabItem == null) {
			return ;
		}
		
		Class<? extends TabContentFragment> toFragment = null;
		
		switch (tabItem.getTabIndex()) {
		case 0:
			toFragment = RemenFragment.class;
			break;
			
		case 1:
			toFragment = DaPeiFragment.class;
			break;
			
		case 2:
			toFragment = GuangJieFragment.class;
			break;
			
		case 3:
			
			break;
			
		case 4:
			toFragment = SheZhiFragment.class;
			break;
		}
		
		if(toFragment != null) {
			String tabName = toFragment.getPackage().getName();
			super.showTabContentAndIfNotEmpty(tabName, tabName, toFragment);
		}
		
		/*
		if(tabItem != null) {
			final TabIdentify tabIdentify = tabItem.getTabIdentify();
			if(tabIdentify == TabIdentify.Edit) {
				getTabActivity().showTabEditPage();
			}
			else if(tabIdentify == TabIdentify.MobileBanking) {
				//TODO Show WebView from base layout.
				getTabActivity().doWebBrowse(Constants.URL_MOBILE_BANKING, false);
			}
			else {
				TabIdentify.CurrentSelectedTab = tabIdentify;
				final Class<? extends TabContentFragment> clazz = TabIdentify.getTabContentClass(tabIdentify);
				if(clazz != null) {
//				showTabContent(tabIdentify.toString(), clazz.getPackage().getName(), clazz);
					invokeOnFragmentChangedForCurrentFragment();
					super.showTabContentAndIfNotEmpty(tabIdentify.toString(), clazz.getPackage().getName(), clazz);
				}
			}
		}
		*/
	}
	
	/*
	public void selectTab(TabIdentify tabIdentify) {
		if(tabIdentify == null) {
			return ;
		}
		GAManager.getInstance().trackView(TabIdentify.getGAViewName(tabIdentify));
		TabIdentify.CurrentSelectedTab = tabIdentify;
		final ItemTabItem tabItem = mItemTabBarView.getTabItemByIdentifyIfExist(tabIdentify);
		if(tabItem != null) {	//The selected tab exist tab bar.
			mItemTabBarView.setSelectedTab(tabItem);	//The method will be call onClickTabItem() to show tab content;
			if(tabIdentify == TabIdentify.MobileBanking) {
				getTabActivity().doWebBrowse(Constants.URL_MOBILE_BANKING, false);
			}
		}
		else if(tabIdentify == TabIdentify.MobileBanking) {
			//TODO Show WebView from base layout.
			getTabActivity().doWebBrowse(Constants.URL_MOBILE_BANKING, false);
		}
		else {	//The selected tab not exist tab bar.
			mItemTabBarView.clearSelected();
			final Class<? extends TabContentFragment> clazz = TabIdentify.getTabContentClass(tabIdentify);
			if(clazz != null) {
//				showTabContent(tabIdentify.toString(), clazz.getPackage().getName(), clazz);
				invokeOnFragmentChangedForCurrentFragment();
				super.showTabContentAndIfNotEmpty(tabIdentify.toString(), clazz.getPackage().getName(), clazz);
			}
		}
	}
	
	*/
	
	public void invokeOnFragmentChangedForCurrentFragment() {
		if(tabManager == null) {
			return ;
		}
		final ClipFragment currentFragment = tabManager.getLastDisplayedFragment();
		if(currentFragment != null && currentFragment instanceof TabContentFragment) {
			((TabContentFragment) currentFragment).onFragmentChanged();
		}
	}
	
	public void updateByLanguageChanged() {
		mItemHeaderView.updateByLanguageChanged();
		mItemTabBarView.updateByLanguageChanged();
	}
	
	@Override
	public void onDestroy() {
		if(mItemHeaderView != null) {
			mItemHeaderView.release();
		}
		if(mItemTabBarView != null) {
			mItemTabBarView.release();
		}
		super.onDestroy();
	}
}
