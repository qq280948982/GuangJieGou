package lib.tim.fragment;

import java.util.Hashtable;

import lib.tim.view.header.OhHeader;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

public class TabContainerFragment extends ClipFragment {

	private static final String TAG="TabContainerFragment";
	
	protected TabManager tabManager;
	private String lastHandledTabID=null;
	private String previousTabID=null;
	private OhHeader header=null;
	
	private static int defaultReplaceContainerViewID=0;
	
	private KeyEvent keyEventBack=new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
	
	public TabContainerFragment(){
		super(); 
	}
	
	public void initWithActivtiy___(FragmentActivity fragmentActivity,int containerViewID){
//		tabManager=new TabManager(fragmentActivity,containerViewID);
		TabManager.init(fragmentActivity, containerViewID);
		tabManager = TabManager.getInstance();
	}
	
	public void initWithFragmentSelfly(FragmentActivity fragmentActivity,int containerViewID){
//		tabManager=new TabManager(this,containerViewID);
		TabManager.init(this, containerViewID);
		tabManager = TabManager.getInstance();
	}
	
	public void init(int containerViewID) {
		TabManager.init(this, containerViewID);
		tabManager = TabManager.getInstance();
	}
	
	public TabManager getTabManager(){
		return tabManager;
	}
	
	public OhHeader getHeader() {
		return header;
	}

	public void setHeader(OhHeader header) {
		this.header = header;
		if(this.header!=null){
			this.header.setBelongToContainerFragment(this);
		}
	}
	
	@Override
	public void onDestroy() {
		if(this.header!=null){
			this.header.setBelongToContainerFragment(null);
			this.header = null;
		}
		super.onDestroy();
	}

	public static void setDefaultReplaceContainerViewID(int replaceContainerViewID){
		defaultReplaceContainerViewID=replaceContainerViewID;
	}
	public static int getDefaultReplaceContainerViewID(){
		return defaultReplaceContainerViewID;
	}
	
	public synchronized void showTabContentAndIfNotEmpty(String tabID,String pageName,
			final Class<? extends TabClipFragment> cls){
		showTabContentAndIfNotEmpty(tabID, pageName, cls, null);
	}
	public synchronized void showTabContentAndIfNotEmpty(String tabID,String pageName,
			final Class<? extends TabClipFragment> cls,Hashtable<String,Object> requestParameter){
		ClipInfo selectionInfo=new ClipInfo();
		selectionInfo.setName(pageName);
		selectionInfo.setDestFragment(cls);
		selectionInfo.setRequestParameter(requestParameter);
		showTabContentAndIfNotEmpty(tabID,selectionInfo);
	}
	public synchronized void showTabContentAndIfNotEmpty(String tabID,ClipInfo clipInfo){
		ClipInfo ciLast=tabManager.getLastClipInfo(tabID);
		if(ciLast==null){
			clipInfo.setEnterFlag(ClipInfo.ENTERTYPE_NEXT);
			showContentBySelectionInfo(tabID,clipInfo,false);
		}
		else{
			clipInfo=ciLast;
			clipInfo.setEnterFlag(ClipInfo.ENTERTYPE_FROM_HISTORY);
			showContentBySelectionInfo(tabID,clipInfo,true);
		}
	}

	public synchronized void showTabContent(String tabID,String pageName,final Class<? extends TabClipFragment> cls){
		showTabContent(tabID,pageName,cls, null,false);
	}
	
	private synchronized void showTabContent(String tabID,String pageName,final Class<? extends TabClipFragment> cls,
			Hashtable<String,Object> requestParameter,boolean isClearPagesInSameTab){
		ClipInfo selectionInfo=new ClipInfo();
		selectionInfo.setName(pageName);
		selectionInfo.setDestFragment(cls);
		selectionInfo.setRequestParameter(requestParameter);
		
		selectionInfo.setEnterFlag(ClipInfo.ENTERTYPE_FROM_HISTORY);
		
		showContentBySelectionInfo(tabID,selectionInfo,false);
	}
	
	private synchronized void showContentBySelectionInfo(String tabID,ClipInfo clipInfo,boolean isHistoryFromTop){
		if(clipInfo==null){
//			Log.w(TAG, "showContentBySelectionInfo>>>clipInfo is null");
			return;
		}
		
		Class<? extends TabClipFragment> destFragment=(Class<? extends TabClipFragment>)clipInfo.getDestFragment();
		if(destFragment==null){
			return;
		}
		int replaceContainerViewID=clipInfo.getReplaceContainerViewID();
		if(replaceContainerViewID==0){
			replaceContainerViewID=getDefaultReplaceContainerViewID();
		}
		if(replaceContainerViewID==0){
//			Log.w(TAG, "showContentBySelectionInfo>>>replaceContainerViewID==0");
			return;
		}
		
		int enterFlag=clipInfo.getEnterFlag();
		
		switch(enterFlag){
		case ClipInfo.ENTERTYPE_NEXT:
			tabManager.handleAccessNextClip(tabID, clipInfo);
			break;
		case ClipInfo.ENTERTYPE_FROM_HISTORY:
			if(isHistoryFromTop){
				tabManager.handleAccessClipInHistory(tabID, clipInfo,false);
			}
			else{
				ClipInfo historyInfo=tabManager.getClipInfoByName(tabID, clipInfo.getName());
				if(historyInfo!=null){
					clipInfo=historyInfo;
				}
				tabManager.handleAccessClipInHistory(tabID, clipInfo,false);
			}
			break;
		}

		
		int size=tabManager.fetchTabHistory(tabID).getHistory().size();
//		Log.w(TAG, "showContentBySelectionInfo>>>tabID="+tabID+",size="+size);
		
		boolean isBackable=tabManager.hasPreviousClip(tabID);
		this.onBackButtonValid(isBackable);
		if(tabID!=null && !tabID.equals(lastHandledTabID)){
			previousTabID=lastHandledTabID;
		}
		lastHandledTabID=tabID;
		
		if(tabManager.getLastDisplayedFragment()!=null){
			tabManager.getLastDisplayedFragment().onFragmentDidAppear();
		}
	}

	public synchronized void showNextContentByClipInfo(ClipInfo selectionInfo){
		if(selectionInfo==null){
			return;
		}
		selectionInfo.setEnterFlag(ClipInfo.ENTERTYPE_NEXT);
		showContentBySelectionInfo(lastHandledTabID, selectionInfo,false);
	}
	
	public synchronized void backPreviousContent(){
//		if(lastHandledTabID==null){
//			Log.w(TAG, "backPreviousContent>>>lastHandledTabID is null");
//			return;
//		}
//		tabManager.handleBackPreviousClip(lastHandledTabID);
//		boolean isBackable=tabManager.hasPreviousClip(lastHandledTabID);
//		this.onBackButtonValid(isBackable);
		backPreviousContent(false);
	}
	
	public synchronized void backPreviousContent(boolean canBackToPreviousTab){
		if(lastHandledTabID==null){
//			Log.w(TAG, "backPreviousContent>>>lastHandledTabID is null");
			return;
		}
		tabManager.handleBackPreviousClip(lastHandledTabID);
		boolean isBackable=tabManager.hasPreviousClip(lastHandledTabID);
		this.onBackButtonValid(isBackable);
		
		if(!isBackable && canBackToPreviousTab){
			backPreviousTab();
		}
	}
	
	public synchronized void backPreviousTab(){
		if(previousTabID!=null){
			ClipInfo ciLast=tabManager.getLastClipInfo(previousTabID);
			if(ciLast!=null){
				ciLast.setEnterFlag(ClipInfo.ENTERTYPE_FROM_HISTORY);
				showContentBySelectionInfo(previousTabID,ciLast,true);
			}
		}
	}
	
	protected void onBackButtonValid(boolean isBackable){
//		Log.i(TAG, "onBackButtonValid>>>isBackable="+isBackable);
		if(header!=null){
//			Log.i(TAG, "onBackButtonValid>>>to set,isBackable="+isBackable);
			if(isBackable){
				header.showBackButton();
			}
			else{
				header.hideBackButton();
			}
		}
	}
	
	public String getCurrentTabID(){
		return lastHandledTabID;
	}
	
	public boolean onKeyUpBackBehavior(){
		return onKeyUp(keyEventBack.getKeyCode(), keyEventBack);
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		try{
			ClipFragment cf=tabManager.getLastDisplayedFragment();
			boolean isProccessedByInner=false;
			if(cf!=null){
				isProccessedByInner= cf.onKeyUp(keyCode, event);
			}
			if(!isProccessedByInner){
				if(keyCode == KeyEvent.KEYCODE_BACK) {
					if(tabManager.hasPreviousClip(lastHandledTabID)){
						backPreviousContent();
						isProccessedByInner=true;
					}
				}
			}
			return isProccessedByInner;
		}
		catch(Exception e){
//			Log.e(TAG, "onKeyUp>>>e="+e.toString());
			e.printStackTrace();
		}
		return false;
	}
}
