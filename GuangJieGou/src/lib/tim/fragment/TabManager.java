package lib.tim.fragment;

import java.util.List;

import lib.tim.util.OrderHashtable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class TabManager {

	private static final String TAG="TabManager";
	
	private static TabManager instance = null;
	
	private FragmentActivity fragmentActivity;
	private Fragment fragmentContainer=null;
	private int containerViewID=0;
	
	private OrderHashtable<String,TabHistory> tabHistoryMap=new OrderHashtable<String,TabHistory>();
	private ClipFragment lastDisplayedFragment=null;
	
	private String lastFragmentTag = null;
	
	public static void init(FragmentActivity fragmentActivity,int containerViewID) {
		if(instance == null) {
			instance = new TabManager(fragmentActivity, containerViewID);
		}
		else {
			instance.fragmentActivity = fragmentActivity;
			instance.containerViewID = containerViewID;
		}
		instance.fetchLastDisplayedFragment();
	}
	
	public static void init(Fragment fragmentContainer,int containerViewID) {
		if(instance == null) {
			instance = new TabManager(fragmentContainer, containerViewID);
		}
		else {
			instance.fragmentContainer = fragmentContainer;
			instance.containerViewID = containerViewID;
			instance.fragmentActivity = fragmentContainer.getActivity();
		}
		instance.fetchLastDisplayedFragment();
	}
	
	public static void destroy() {
		if(instance != null) {
			instance.release();
			instance = null;
		}
	}
	
	public static TabManager getInstance() {
		return instance;
	}
	
	private TabManager(FragmentActivity fragmentActivity,int containerViewID){
		this.fragmentActivity=fragmentActivity;
		this.containerViewID=containerViewID;
	}
	
	private TabManager(Fragment fragmentContainer,int containerViewID){
		this.fragmentContainer=fragmentContainer;
		this.containerViewID=containerViewID;
		this.fragmentActivity = fragmentContainer.getActivity();
	}
	
	private void fetchLastDisplayedFragment(){
		if(lastFragmentTag==null){
			return;
		}
		lastDisplayedFragment=(ClipFragment)getFragmentManager().findFragmentByTag(lastFragmentTag);
	}
	
	public boolean containsInfoByName(String tabID,String name){
		return fetchTabHistory(tabID).containsClipInfoByName(name);
	}
	
	public boolean containsInfoByClipFragment(String tabID,Class<? extends ClipFragment> cls){
		return fetchTabHistory(tabID).containsClipInfoByClipFragment(cls);
	}
	
	public ClipInfo getClipInfoByName(String tabID,String name){
		return fetchTabHistory(tabID).getClipInfoByName(name);
	}
	
	public ClipInfo getInfoByClipFragment(String tabID,Class<? extends ClipFragment> cls){
		return fetchTabHistory(tabID).getClipInfoByClipFragment(cls);
	}
	
	public int getIndexOfClipInfo(String tabID,ClipInfo info){
		return fetchTabHistory(tabID).indexOfClipInfo(info);
	}
	
	public ClipFragment getLastDisplayedFragment(){
		return lastDisplayedFragment;
	}
	
	public synchronized ClipInfo getLastClipInfo(String tabID){
		return fetchTabHistory(tabID).getLastClip();
	}
	
	public synchronized TabHistory fetchTabHistory(String tabID){
		if(!tabHistoryMap.containsKey(tabID)){
			TabHistory tabHistory=new TabHistory(tabID);
			addTabHistory(tabHistory);
		}
		return tabHistoryMap.get(tabID);
	}
		
	public synchronized void addTabHistory(TabHistory tabHistory){
		tabHistoryMap.put(tabHistory.getTabID(), tabHistory);
	}
	
	public synchronized void removeTabHistory(String tabID){
		tabHistoryMap.remove(tabID);
	}
	
	public synchronized void handleAccessNextClip(String tabID,ClipInfo info){
		showClipByInfoForNextType(tabID, info, ClipInfo.ENTERTYPE_NEXT);
	}
	
	/**
	 * 
	 * @param tabID
	 * @param info
	 * @param isRemoveAllPreviousPagesInSameTab Make the tab displays the top fragment. The pages after info will be removed.
	 */
	public synchronized void handleAccessClipInHistory(String tabID,ClipInfo info,boolean isRemoveAllPreviousPagesInSameTab){
		TabHistory tabHistory=fetchTabHistory(tabID);
		int index=tabHistory.indexOfClipInfo(info);
//		Log.i(TAG, "handleAccessClipInHistory>>>index="+index+",info:"+info.getName()+","+info.getDestFragment().getName());
		if(index>0){
			if(isRemoveAllPreviousPagesInSameTab){//TODO 有问题，好似删错野。
				while(index>0){
					ClipInfo ci=tabHistory.getClipInfoByIndex(0);
					if(ci==info){
//						Log.i(TAG, "handleAccessClipInHistory>>>break; index="+index);
						break;
					}
					tabHistory.removeClipInfo(ci);
					this.removeFragmentByInfo(ci,0);
					index--;
				}
			}
			
		}
		else if(index<0){//No such fragment existing.
//			Log.w(TAG, "handleAccessClipInHistory>>>to next; index="+index);
			showClipByInfoForNextType(tabID, info, ClipInfo.ENTERTYPE_FROM_HISTORY);
			return;
		}
		showClipByInfoForBackOrHistoryType(tabID, info, ClipInfo.ENTERTYPE_FROM_HISTORY);
	}

	public synchronized void handleBackPreviousClip(String tabID){
//		try{
//			TabHistory tabHistory=fetchTabHistory(tabID);
//			if(tabHistory.hasPreviousClip()){
//				ClipInfo removedInfo=tabHistory.popupLastClip();
//				this.removeFragmentByInfo(removedInfo);
//				ClipInfo info=tabHistory.getLastClip();
//				showClipByInfo(tabID,info,ClipInfo.ENTERTYPE_BACK);
//			}
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
		handleBackPreviousClip(tabID,1);
	}
	
	public synchronized void handleBackPreviousClip(String tabID,int backNumber){
		if(backNumber<=0){
			return;
		}
		try{
			TabHistory tabHistory=fetchTabHistory(tabID);
			int index=tabHistory.getHistory().size()-1-backNumber;
			ClipInfo toClipInfo=tabHistory.getClipInfoByIndex(index);
			int backCount=0;
			for(int i=0;i<backNumber;i++){
				if(tabHistory.hasPreviousClip()){
					ClipInfo removedInfo=tabHistory.popupLastClip();
					this.removeFragmentByInfo(removedInfo,toClipInfo.getBackExitAnimationResId());
					backCount++;
				}
				else{
					break;
				}
			}
			if(backCount>0){
				ClipInfo info=tabHistory.getLastClip();
				showClipByInfoForBackOrHistoryType(tabID,info,ClipInfo.ENTERTYPE_BACK);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized boolean hasPreviousClip(String tabID){
		return fetchTabHistory(tabID).hasPreviousClip();
	}
	
	private FragmentManager getFragmentManager(){
		if(fragmentContainer!=null){
			return fragmentContainer.getFragmentManager();
		}
		else if(fragmentActivity!=null){
			return fragmentActivity.getSupportFragmentManager();
		}
		return null;
	}
	
	public synchronized void removeFragmentByInfo(ClipInfo info,int animationResId){
		String tag=info.getTag();
		FragmentManager fragmentManager=getFragmentManager();
		if(animationResId==0){
			animationResId=info.getExitAnimationResId();
		}
		removeFragment((ClipFragment)fragmentManager.findFragmentByTag(tag),animationResId);
	}
	
	public synchronized void removeFragment(ClipFragment fragment,int exitAnimResId){
		if(fragment==null){
			return;
		}
		FragmentManager fragmentManager=getFragmentManager();
		FragmentTransaction ft=fragmentManager.beginTransaction();
		ft.setCustomAnimations(0, exitAnimResId);
		ft.remove(fragment);
		ft.commit();
		if(fragment==lastDisplayedFragment){
			lastDisplayedFragment=null;
		}
	}
	
	public synchronized void removeInfoAndFragment(ClipInfo info){
		removeClipInfo(info);
		String tag=info.getTag();
		FragmentManager fragmentManager=getFragmentManager();
		removeFragment((ClipFragment)fragmentManager.findFragmentByTag(tag),info.getExitAnimationResId());
	}
	
	public String getTabIDByInfo(ClipInfo info){
		for(TabHistory tabHistory:tabHistoryMap.getOrderList()){
			if(tabHistory.containsClipInfo(info)){
				return tabHistory.getTabID();
			}
		}
		return null;
	}
	
	public void removeClipInfo(ClipInfo info){
		for(TabHistory tabHistory:tabHistoryMap.getOrderList()){
			tabHistory.removeClipInfo(info);
		}
	}
	
	private synchronized void showClipByInfoForBackOrHistoryType(String tabID,ClipInfo info,int enterType){
		try{	
			Class<? extends ClipFragment> cls=info.getDestFragment();
			
			FragmentManager fragmentManager=getFragmentManager();
			
			if(lastDisplayedFragment != null) {
//				Log.e(TAG, "current FM: " + fragmentManager + "  ,  lastFM: " + lastDisplayedFragment.getFragmentManager());
			}
			
			Fragment existingFragment=fragmentManager.findFragmentByTag(info.getTag());
//			FragmentTransaction ft=fragmentManager.beginTransaction();
			ClipFragment toOpenFragment=null;
			if(existingFragment==null){
				toOpenFragment=cls.newInstance();
//				toOpenFragment = (ClipFragment) Fragment.instantiate(fragmentActivity, cls.getName());
				toOpenFragment.setInfo(info);
				toOpenFragment.setEnterType(enterType);
				
				//Log.w(TAG, "showClipByInfoForBackOrHistoryType>>>add,info.getTag()="+info.getTag()+",toOpenFragment="+toOpenFragment.getTag());
				FragmentTransaction ft=fragmentManager.beginTransaction();
				setupCustomAnimation(ft,info,enterType);
				ft.add(containerViewID, toOpenFragment, info.getTag());
				ft.commit();
			}
			else{
				toOpenFragment=(ClipFragment)existingFragment;
				toOpenFragment.setEnterType(enterType);
				
				//Log.w(TAG, "showClipByInfoForBackOrHistoryType>>>show,info.getTag()="+info.getTag()+",toOpenFragment="+toOpenFragment.getTag());
				FragmentTransaction ft=fragmentManager.beginTransaction();
				setupCustomAnimation(ft,info,enterType);
				ft.show(toOpenFragment);
				ft.commit();
			}
			
			if(lastDisplayedFragment!=null && lastDisplayedFragment!=toOpenFragment){

				if(lastDisplayedFragment.isAllowRemove()){
					lastDisplayedFragment.onSaveData(lastDisplayedFragment.getInfo().initSavedData());
					//Log.w(TAG, "showClipByInfoForBackOrHistoryType>>>remove"+",toOpenFragment="+lastDisplayedFragment.getTag());
					FragmentTransaction ft=fragmentManager.beginTransaction();
					setupCustomAnimation(ft,info,enterType);
					ft.remove(lastDisplayedFragment);
					ft.commit();
				}
				else{
					//Log.w(TAG, "showClipByInfoForBackOrHistoryType>>>hide"+",toOpenFragment="+lastDisplayedFragment.getTag());
					FragmentTransaction ft=fragmentManager.beginTransaction();
					setupCustomAnimation(ft,info,enterType);
					ft.hide(lastDisplayedFragment);
					ft.commit();
				}
			}
			toOpenFragment.onFragmentResume();
//			ft.commit();
			lastDisplayedFragment=toOpenFragment;
			lastFragmentTag = lastDisplayedFragment.getTag();
			
			if(existingFragment==null){
				if(toOpenFragment.isEnterTypeBack()){
					//notify the page, then it can restore the index of listview .etc
					toOpenFragment.onRestoreSavedData(info.getSavedData());
					
				}
				else if(toOpenFragment.isEnterTypeFromHistory()){
					toOpenFragment.onRestoreSavedData(info.getSavedData());
				}
			}
			
//			ft.commit();
//			lastDisplayedFragment=toOpenFragment;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private synchronized void showClipByInfoForNextType(String tabID,ClipInfo info,int enterType){
		try{	
			Class<? extends ClipFragment> cls=info.getDestFragment();
			TabHistory tabHistory=fetchTabHistory(tabID);
			
			FragmentManager fragmentManager=getFragmentManager();
			
			if(lastDisplayedFragment != null) {
//				Log.e(TAG, "current FM: " + fragmentManager + "  ,  lastFM: " + lastDisplayedFragment.getFragmentManager());
			}
			
			ClipFragment toOpenFragment=null;
			toOpenFragment=cls.newInstance();
//			toOpenFragment = (ClipFragment) Fragment.instantiate(fragmentActivity, cls.getName());
			toOpenFragment.setInfo(info);
			toOpenFragment.setEnterType(enterType);
//			FragmentTransaction ft=fragmentManager.beginTransaction();
			if(lastDisplayedFragment!=null){
//				Log.i(TAG, "showClipByInfoForNextType>>>lastDisplayedFragment is not null");
				if(lastDisplayedFragment.isAllowRemove()){
					lastDisplayedFragment.onSaveData(lastDisplayedFragment.getInfo().initSavedData());
//					Log.w(TAG, "showClipByInfoForNextType>>>remove"+",toOpenFragment="+lastDisplayedFragment.getTag());
					FragmentTransaction ft=fragmentManager.beginTransaction();
					setupCustomAnimation(ft, info,enterType);
					ft.remove(lastDisplayedFragment);
					ft.commit();
				}
				else{
//					Log.w(TAG, "showClipByInfoForNextType>>>hide"+",toOpenFragment="+lastDisplayedFragment.getTag());
					FragmentTransaction ft=fragmentManager.beginTransaction();
					setupCustomAnimation(ft, info,enterType);
					ft.hide(lastDisplayedFragment);
					ft.commit();
				}
			}
//			if(toOpenFragment instanceof InsuranceFragment){
//				
//			}
//			else{
//			Log.w(TAG, "showClipByInfoForNextType>>>add,info.getTag()="+info.getTag()+",toOpenFragment="+toOpenFragment.getTag());
			FragmentTransaction ft=fragmentManager.beginTransaction();
			setupCustomAnimation(ft, info,enterType);
				ft.add(containerViewID, toOpenFragment, info.getTag());
//			}
			ft.commit();
			lastDisplayedFragment=toOpenFragment;
			lastFragmentTag = lastDisplayedFragment.getTag();
			
			tabHistory.addClipInfoToHistory(info);
		}
		catch(Exception e){
//			Log.e(TAG,"showClipByInfoForNextType>>>e="+e.toString());
			e.printStackTrace();
		}
	}
	
//	private void setupCustomAnimation__(FragmentTransaction ft,ClipInfo ci){
//		ft.setCustomAnimations(ci.getEnterAnimationResId(), ci.getExitAnimationResId());
//	}
	
	private void setupCustomAnimation(FragmentTransaction ft,ClipInfo ci,int enterType){
		if(enterType==ClipInfo.ENTERTYPE_BACK){
			ft.setCustomAnimations(ci.getBackEnterAnimationResId(), ci.getBackExitAnimationResId());
		}
		else if(enterType==ClipInfo.ENTERTYPE_NEXT){
			ft.setCustomAnimations(ci.getEnterAnimationResId(), ci.getExitAnimationResId());
		}
	}
	
	public void release() {
		FragmentManager fManager = getFragmentManager();
		for(String historyKey : tabHistoryMap.keySet()) {
			TabHistory tabHistory = tabHistoryMap.get(historyKey);
			List<ClipInfo> clipInfos = tabHistory.getHistory();
			for(ClipInfo clipInfo : clipInfos) {
				String tag = clipInfo.getTag();
				Fragment fragment = fManager.findFragmentByTag(tag);
				if(fragment != null) {
					fragment.getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
				}
				clipInfo.clearData();
			}
			tabHistory.clearHistory();
		}
		tabHistoryMap.clear();
		ClipInfo.reset();
		lastDisplayedFragment = null;
		lastFragmentTag = null;
		fragmentActivity = null;
		fragmentContainer = null;
	}
}
